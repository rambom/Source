using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Common;
using System.Diagnostics;
using System.IO;
using System.Text;
using System.Timers;
using Oracle.DataAccess.Client;

namespace Amazon_analyzer.Database
{
    /// <summary>
    /// bulk insert with oracle
    /// add by qblong
    /// </summary>
    internal class OracleBulkInsert
    {
        #region const properties

        /// <summary>
        /// default over time of write
        /// </summary>
        private const int DEFAULT_TIME_OUT = 0x1e;
        /// <summary>
        /// max batch size of commit
        /// </summary>
        private const int MAX_BATCH_SIZE = 0xfffe;
        /// <summary>
        /// one seconds by milliseconds
        /// </summary>
        private const int ONE_SECONDS = 0x3e8;
        /// <summary>
        /// default begin read row number
        /// </summary>
        private const int DEFAULT_ROW_NUMBER = 0x1;

        #endregion

        #region private properties

        /// <summary>
        /// source column and destination column mapping
        /// </summary>
        private readonly Dictionary<string, string> _columnMappings;
        /// <summary>
        /// addtional column mapping
        /// like that (targetFieldName,fieldValue)
        /// </summary>
        private readonly Dictionary<string, object> _extraColumnMappings;
        /// <summary>
        /// destination table field datatype info
        /// </summary>
        private IList<ColumnInfo> _columnSchema;
        /// <summary>
        /// import data source type
        /// </summary>
        private DataSourceType _datasourceType;
        /// <summary>
        /// read a line from file
        /// </summary>
        private string _textLine;
        /// <summary>
        /// text file separator
        /// </summary>
        private string[] _delimited;
        /// <summary>
        /// text file head line
        /// </summary>
        private string _headLine;
        /// <summary>
        /// to save text file headline column index
        /// </summary>
        private Dictionary<string, int> _dicSourceColumnIndex;
        /// <summary>
        /// don't need set value parameters
        /// </summary>
        private Dictionary<string, string> _dicNoInitValueParameters;
        /// <summary>
        /// target table to write
        /// </summary>
        private string _destinationTableName;
        /// <summary>
        /// target database connection string
        /// </summary>
        private string _connectionString;

        /// <summary>
        /// target database connection
        /// </summary>
        private OracleConnection _oracleConnection;
        /// <summary>
        /// import data source
        /// </summary>
        private object _datasource;
        /// <summary>
        /// batch size to commit
        /// </summary>
        private int _batchSize;
        /// <summary>
        /// over run time to quit
        /// </summary>
        private int _timeOut;
        /// <summary>
        /// line number of file to read
        /// </summary>
        private uint _startRow = 1;
        /// <summary>
        /// write row number to oracle flag
        /// </summary>
        private bool _insertRowNumber = false;
        /// <summary>
        /// rownumber column name to save
        /// </summary>
        private string _rowNumberColumn = null;

        #endregion

        #region public properties

        /// <summary>
        /// take seconds of completed
        /// </summary>
        public long TakeMilliseconds { get; private set; }

        /// <summary>
        /// import row count
        /// </summary>
        public int CopiedRowCount { get; private set; }

        /// <summary>
        /// batch size to commit
        /// </summary>
        public int BatchSize
        {
            get { return _batchSize; }
            set { _batchSize = value > 0 && value < MAX_BATCH_SIZE ? value : MAX_BATCH_SIZE; }
        }

        /// <summary>
        /// over run time to quit
        /// </summary>
        public int TimeOut
        {
            get { return _timeOut; }
            set { _timeOut = (value > 0 ? value : DEFAULT_TIME_OUT) * ONE_SECONDS; }
        }

        /// <summary>
        /// save rownum flag
        /// </summary>
        public bool InsertRowNumber
        {
            get { return this._insertRowNumber; }
            set { this._insertRowNumber = value; }
        }

        /// <summary>
        /// begin read source rownum
        /// </summary>
        public uint StartRow
        {
            get { return this._startRow; }
            set { this._startRow = value; }
        }

        /// <summary>
        /// save field name of rownum to oracle
        /// </summary>
        public string RownumColumnName
        {
            get { return this._rowNumberColumn; }
            set { this._rowNumberColumn = value; }
        }

        /// <summary>
        /// target table to write
        /// </summary>
        public string DestinationTableName
        {
            get { return _destinationTableName; }
            set { _destinationTableName = value.Replace("'", "''"); }
        }

        /// <summary>
        /// target database connection string
        /// </summary>
        public string ConnectionString
        {
            get { return _connectionString; }
            set { _connectionString = value; }
        }

        /// <summary>
        /// target database connection
        /// </summary>
        public OracleConnection OracleConnection
        {
            get { return _oracleConnection; }
            set { _oracleConnection = value; }
        }

        #endregion

        #region constructor

        /// <summary>
        /// constructor 1
        /// </summary>
        /// <param name="connectionString">destination database connection string</param>
        /// <param name="destinationTableName">target tableName</param>
        /// <param name="columnMapping">source column with target column mapping</param>
        public OracleBulkInsert(string connectionString, string destinationTableName, Dictionary<string, string> columnMapping)
            : this(connectionString, destinationTableName, columnMapping, null, DEFAULT_ROW_NUMBER, null, MAX_BATCH_SIZE, DEFAULT_TIME_OUT)
        {
        }

        public OracleBulkInsert(OracleConnection conn, string destinationTableName, Dictionary<string, string> columnMapping)
            : this(conn, destinationTableName, columnMapping, null, DEFAULT_ROW_NUMBER, null, MAX_BATCH_SIZE, DEFAULT_TIME_OUT)
        {
        }

        /// <summary>
        /// constructor 2
        /// </summary>
        /// <param name="connectionString">destination database connection string</param>
        /// <param name="destinationTableName">target tableName</param>
        /// <param name="columnMapping">source column with target column mapping</param>
        /// <param name="extraMapping">addtional column mapping</param>
        /// <param name="startRow">begin import row number</param>
        /// <param name="rownumColumn"></param>
        /// <param name="batchSize">batch size to commit data</param>
        /// <param name="timeout">over seconds for quit</param>
        public OracleBulkInsert(string connectionString, string destinationTableName, Dictionary<string, string> columnMapping, Dictionary<string, object> extraMapping, uint startRow, string rownumColumn, int batchSize, int timeout)
        {
            TakeMilliseconds = 0x0;
            CopiedRowCount = 0x0;
            this._connectionString = connectionString;
            this.BatchSize = batchSize;
            this.TimeOut = timeout;
            this.DestinationTableName = destinationTableName;
            this._insertRowNumber = !string.IsNullOrEmpty(rownumColumn);
            this._rowNumberColumn = string.IsNullOrEmpty(rownumColumn) ? null : rownumColumn.ToUpper();
            this._startRow = startRow;

            _columnMappings = new Dictionary<string, string>();

            if (null != columnMapping)
            {
                foreach (var map in columnMapping)
                {
                    this._columnMappings.Add(map.Key.ToUpper(), map.Value.ToUpper());
                }
            }

            _extraColumnMappings = new Dictionary<string, object>();

            if (null != extraMapping)
            {
                foreach (var map in extraMapping)
                {
                    this._extraColumnMappings.Add(map.Key.ToUpper(), map.Value);
                }
            }
        }


        /// <summary>
        /// constructor 2
        /// </summary>
        /// <param name="conn">destination database connection string</param>
        /// <param name="destinationTableName">target tableName</param>
        /// <param name="columnMapping">source column with target column mapping</param>
        /// <param name="extraMapping">addtional column mapping</param>
        /// <param name="startRow">begin import row number</param>
        /// <param name="rownumColumn"></param>
        /// <param name="batchSize">batch size to commit data</param>
        /// <param name="timeout">over seconds for quit</param>
        public OracleBulkInsert(OracleConnection conn, string destinationTableName, Dictionary<string, string> columnMapping, Dictionary<string, object> extraMapping, uint startRow, string rownumColumn, int batchSize, int timeout)
        {
            TakeMilliseconds = 0x0;
            CopiedRowCount = 0x0;
            this._oracleConnection = conn;
            this.BatchSize = batchSize;
            this.TimeOut = timeout;
            this.DestinationTableName = destinationTableName;
            this._insertRowNumber = !string.IsNullOrEmpty(rownumColumn);
            this._rowNumberColumn = string.IsNullOrEmpty(rownumColumn) ? null : rownumColumn.ToUpper();
            this._startRow = startRow;

            _columnMappings = new Dictionary<string, string>();

            if (null != columnMapping)
            {
                foreach (var map in columnMapping)
                {
                    this._columnMappings.Add(map.Key.ToUpper(), map.Value.ToUpper());
                }
            }

            _extraColumnMappings = new Dictionary<string, object>();

            if (null != extraMapping)
            {
                foreach (var map in extraMapping)
                {
                    this._extraColumnMappings.Add(map.Key.ToUpper(), map.Value);
                }
            }
        }

        /// <summary>
        /// destruction
        /// </summary>
        ~OracleBulkInsert()
        {
            this.CloseDatasource();
        }

        #endregion

        #region private members

        /// <summary>
        /// check column maping is correct
        /// </summary>
        private void VerifyColumnMapping()
        {
            //column mapping not null
            if (_columnMappings.Count <= 0 && _extraColumnMappings.Count <= 0)
            {
                throw new OracleBulkInsertException(BulkInsertCode.ColumnMappingNull);
            }

            //check all column mapping is unique
            foreach (var kvp in _columnMappings)
            {
                if (_extraColumnMappings.ContainsKey(kvp.Key))
                {
                    throw new OracleBulkInsertException(BulkInsertCode.ColumnMapNotUnique, kvp.Key);
                }
            }

            if (this._insertRowNumber && (this._columnMappings.ContainsKey(this._rowNumberColumn) || this._extraColumnMappings.ContainsKey(this._rowNumberColumn)))
            {
                throw new OracleBulkInsertException(BulkInsertCode.RownumColumnExists, this._rowNumberColumn);
            }
        }

        /// <summary>
        /// fetch target table column description data
        /// </summary>
        private void GetTableSchemaData()
        {
            string strCommand = @"SELECT A.COLUMN_NAME AS columnName,decode(instr(lower(A.DATA_TYPE), 'timestamp', 1, 1),1,'timestamp',lower(A.DATA_TYPE)) AS columnType,
       nvl(decode(A.DATA_TYPE,'DATE',23,'NUMBER',A.DATA_PRECISION,A.DATA_LENGTH),0) length,decode(A.DATA_PRECISION, null, '0', a.DATA_PRECISION) AS precision,
       decode(A.DATA_SCALE, NULL, '0', a.DATA_SCALE) AS scale,DECODE(A.NULLABLE, 'Y', 'Y', '') AS isnullable FROM USER_ALL_TABLES AA,USER_TAB_COLUMNS A,
       USER_COL_COMMENTS F,USER_TAB_COMMENTS TT,
       (SELECT B.TABLE_NAME,B.INDEX_NAME,B.UNIQUENESS,C.COLUMN_NAME,DECODE(D.CONSTRAINT_NAME, NULL, 'No', 'Yes') KEY FROM USER_INDEXES B,USER_IND_COLUMNS C,
               (SELECT CONSTRAINT_NAME FROM USER_CONSTRAINTS WHERE CONSTRAINT_TYPE = 'P') D WHERE B.INDEX_NAME = C.INDEX_NAME
           AND B.INDEX_NAME = D.CONSTRAINT_NAME(+) AND B.UNIQUENESS = 'UNIQUE') E WHERE a.table_name=upper('{0}')
   and AA.TABLE_NAME = A.TABLE_NAME AND A.TABLE_NAME = E.TABLE_NAME(+) AND A.COLUMN_NAME = E.COLUMN_NAME(+) AND A.TABLE_NAME = F.TABLE_NAME
   AND A.COLUMN_NAME = F.COLUMN_NAME AND AA.table_name = TT.table_name";

            strCommand = string.Format(strCommand, this._destinationTableName);

            //filter by columnname
            string strFilterExpression = SchemaField.COLUMN_NAME + "='{0}'";

            DataTable dtSchema = new DataTable("schema");

            //ignore case
            dtSchema.CaseSensitive = false;

            if (string.IsNullOrEmpty(this._connectionString))
            {
                using (OracleDataAdapter oracleDataAdapter = new OracleDataAdapter(strCommand, this._oracleConnection))
                {
                    oracleDataAdapter.Fill(dtSchema);
                }
            }
            else
            {
                using (OracleDataAdapter oracleDataAdapter = new OracleDataAdapter(strCommand, this._connectionString))
                {
                    oracleDataAdapter.Fill(dtSchema);
                }
            }
           

            if (dtSchema.Rows.Count <= 0)
            {
                throw new OracleBulkInsertException(BulkInsertCode.TableSchemaNotExists, this._destinationTableName);
            }
            else
            {
                foreach (var kvp in _columnMappings)
                {
                    //notice datarow select method ignore case
                    DataRow[] rows = dtSchema.Select(string.Format(strFilterExpression, kvp.Key));

                    if (rows.Length > 0)
                    {
                        AddColumnInfo(kvp.Key, kvp.Value, null, MappingType.Normal, rows[0]);
                    }
                    else
                    {
                        throw new OracleBulkInsertException(BulkInsertCode.ColumnMapNotExists, kvp.Key);
                    }
                }

                //aditional extra mapping
                foreach (var map in _extraColumnMappings)
                {
                    DataRow[] rows = dtSchema.Select(string.Format(strFilterExpression, map.Key));

                    if (rows.Length > 0)
                    {
                        AddColumnInfo(map.Key, null, map.Value, MappingType.Extra, rows[0]);
                    }
                    else
                    {
                        throw new OracleBulkInsertException(BulkInsertCode.ColumnMapNotExists, map.Key);
                    }
                }

                //add rownumber column mapping
                if (this._insertRowNumber)
                {
                    DataRow[] rows = dtSchema.Select(string.Format(strFilterExpression, this._rowNumberColumn));
                    if (rows.Length > 0)
                    {
                        AddColumnInfo(_rowNumberColumn, _rowNumberColumn, null, MappingType.Rownum, rows[0]);
                    }
                    else
                    {
                        throw new OracleBulkInsertException(BulkInsertCode.ColumnMapNotExists, this._rowNumberColumn);
                    }
                }
            }
        }

        /// <summary>
        /// add a new columnInfo
        /// </summary>
        /// <param name="targetColumn">target column name</param>
        /// <param name="sourceColumn">source column name</param>
        /// <param name="defaultValue">column default value</param>
        /// <param name="mappingType">column mapping type</param>
        /// <param name="row">column schema</param>
        private void AddColumnInfo(string targetColumn, string sourceColumn, object defaultValue, MappingType mappingType, DataRow row)
        {
            ColumnInfo columnInfo = new ColumnInfo
                                        {
                                            SourceColumn = sourceColumn,
                                            ColumnName = targetColumn,
                                            ColumnType =
                                                (OracleDbType)
                                                Enum.Parse(typeof(OracleDbType),
                                                           row[SchemaField.COLUMN_TYPE].ToString(), true),
                                            Length = int.Parse(row[SchemaField.LENGTH].ToString()),
                                            IsNullable =
                                                "Y".Equals(row[SchemaField.ISNULLABLE].ToString(),
                                                           StringComparison.CurrentCultureIgnoreCase)
                                                    ? true
                                                    : false,
                                            Precision = int.Parse(row[SchemaField.PRECISION].ToString()),
                                            Scale = int.Parse(row[SchemaField.SCALE].ToString()),
                                            DefaultValue = defaultValue,
                                            MappingType = mappingType
                                        };

            if (null == _columnSchema) _columnSchema = new List<ColumnInfo>();
            _columnSchema.Add(columnInfo);
        }

        /// <summary>
        /// build bulk insert sql
        /// </summary>
        /// <returns></returns>
        private string BuildBulkInsertCommandText()
        {
            StringBuilder stringBuilderColumn = new StringBuilder();

            StringBuilder stringBuilderParameter = new StringBuilder();

            foreach (ColumnInfo columnInfo in _columnSchema)
            {
                stringBuilderColumn.AppendFormat(",{0}", columnInfo.ColumnName);
                stringBuilderParameter.AppendFormat(",:{0}", columnInfo.ColumnName);
            }

            return string.Format("insert into {0}({1}) values ({2})", this._destinationTableName, stringBuilderColumn.ToString(1, stringBuilderColumn.Length - 1), stringBuilderParameter.ToString(1, stringBuilderParameter.Length - 1));
        }

        /// <summary>
        /// build bulk insert parameters
        /// </summary>
        /// <returns></returns>
        private OracleParameter[] BuildCommandParameter()
        {
            OracleParameter[] oracleParameters = new OracleParameter[_columnSchema.Count];
            int intIndex = 0;

            if (null == this._dicNoInitValueParameters)
                this._dicNoInitValueParameters = new Dictionary<string, string>();

            Dictionary<string, int> dicSourceColumn = new Dictionary<string, int>();

            foreach (ColumnInfo columnInfo in _columnSchema)
            {
                OracleParameter parameter = new OracleParameter
                {
                    ParameterName = columnInfo.ColumnName,
                    Direction = ParameterDirection.Input,
                    Scale = (byte)columnInfo.Scale
                };

                if (columnInfo.Precision > 0)
                    parameter.Precision = (byte)columnInfo.Precision;

                if (columnInfo.Length > 0)
                    parameter.Size = columnInfo.Length;

                switch (columnInfo.ColumnType)
                {
                    case OracleDbType.BinaryDouble:
                        parameter.OracleDbType = Oracle.DataAccess.Client.OracleDbType.BinaryDouble;
                        break;
                    case OracleDbType.BinaryFloat:
                        parameter.OracleDbType = Oracle.DataAccess.Client.OracleDbType.BinaryFloat;
                        break;
                    case OracleDbType.Blob:
                        parameter.OracleDbType = Oracle.DataAccess.Client.OracleDbType.Blob;
                        break;
                    case OracleDbType.Char:
                        parameter.OracleDbType = Oracle.DataAccess.Client.OracleDbType.Char;
                        break;
                    case OracleDbType.Clob:
                        parameter.OracleDbType = Oracle.DataAccess.Client.OracleDbType.Clob;
                        break;
                    case OracleDbType.Date:
                        parameter.OracleDbType = Oracle.DataAccess.Client.OracleDbType.Date;
                        break;
                    case OracleDbType.Long:
                        parameter.OracleDbType = Oracle.DataAccess.Client.OracleDbType.Long;
                        break;
                    case OracleDbType.NClob:
                        parameter.OracleDbType = Oracle.DataAccess.Client.OracleDbType.NClob;
                        break;
                    case OracleDbType.Number:
                        parameter.OracleDbType = Oracle.DataAccess.Client.OracleDbType.Decimal;
                        break;
                    case OracleDbType.NVarChar2:
                        parameter.OracleDbType = Oracle.DataAccess.Client.OracleDbType.NVarchar2;
                        break;
                    case OracleDbType.Raw:
                        parameter.OracleDbType = Oracle.DataAccess.Client.OracleDbType.Raw;
                        break;
                    case OracleDbType.Timestamp:
                        parameter.OracleDbType = Oracle.DataAccess.Client.OracleDbType.TimeStamp;
                        break;
                    case OracleDbType.VarChar2:
                        parameter.OracleDbType = Oracle.DataAccess.Client.OracleDbType.Varchar2;
                        break;
                }

                object[] objValue = null;

                if (MappingType.Normal.Equals(columnInfo.MappingType))
                {
                    if (dicSourceColumn.ContainsKey(columnInfo.SourceColumn))
                    {
                        this._dicNoInitValueParameters.Add(columnInfo.ColumnName, columnInfo.SourceColumn);
                        objValue = oracleParameters[dicSourceColumn[columnInfo.SourceColumn]].Value as object[];
                    }
                    else
                    {
                        objValue = new object[this._batchSize];
                        dicSourceColumn.Add(columnInfo.SourceColumn, intIndex);
                    }
                }
                else if (MappingType.Extra.Equals(columnInfo.MappingType))
                {
                    this._dicNoInitValueParameters.Add(columnInfo.ColumnName, columnInfo.SourceColumn);
                    objValue = new object[this._batchSize];
                    for (int i = 0; i < this._batchSize; i++)
                    {
                        //initialize parameter default value
                        objValue[i] = columnInfo.DefaultValue;
                    }
                }
                else if (MappingType.Rownum.Equals(columnInfo.MappingType))
                {
                    this._dicNoInitValueParameters.Add(columnInfo.ColumnName, columnInfo.SourceColumn);
                    objValue = new object[this._batchSize];
                }

                parameter.Value = objValue;

                oracleParameters[intIndex] = parameter;
                ++intIndex;
            }
            return oracleParameters;
        }

        /// <summary>
        /// set data reader column index
        /// </summary>
        private void SetDataReaderColumnIndex()
        {
            IDataReader dr = this._datasource as IDataReader;

            foreach (KeyValuePair<string, string> kvp in this._columnMappings)
            {
                for (int i = 0; i < dr.FieldCount; i++)
                {
                    if (!kvp.Value.Equals(dr.GetName(i), StringComparison.CurrentCultureIgnoreCase)) continue;

                    if (!this._dicSourceColumnIndex.ContainsKey(kvp.Key))
                    {
                        this._dicSourceColumnIndex.Add(kvp.Key, i);
                        break;
                    }
                }
            }
        }

        /// <summary>
        /// set text file column index
        /// </summary>
        private void SetTextColumnIndex()
        {
            if (string.IsNullOrEmpty(this._headLine))
            {
                throw new OracleBulkInsertException(BulkInsertCode.HeadColumnIsNull);
            }

            string[] arrColumn = this._headLine.Split(this._delimited, StringSplitOptions.RemoveEmptyEntries);

            foreach (KeyValuePair<string, string> kvp in this._columnMappings)
            {
                for (int i = 0; i < arrColumn.Length; i++)
                {
                    if (!kvp.Value.Equals(arrColumn[i], StringComparison.CurrentCultureIgnoreCase)) continue;

                    if (!this._dicSourceColumnIndex.ContainsKey(kvp.Key))
                    {
                        this._dicSourceColumnIndex.Add(kvp.Key, i);
                        break;
                    }
                }
            }
        }

        /// <summary>
        /// init data source column index
        /// </summary>
        private void LoadSourceColumnIndex()
        {
            if (null == this._dicSourceColumnIndex)
                this._dicSourceColumnIndex = new Dictionary<string, int>();

            switch (_datasourceType)
            {
                case DataSourceType.DbDataReader:
                    SetDataReaderColumnIndex();
                    break;
                case DataSourceType.TextFile:
                    SetTextColumnIndex();
                    break;
            }

            if (this._columnMappings.Count == this._dicSourceColumnIndex.Count) return;

            foreach (var kvp in this._columnMappings)
            {
                if (!this._dicSourceColumnIndex.ContainsKey(kvp.Key))
                {
                    throw new OracleBulkInsertException(BulkInsertCode.SourceColumnNotExists, kvp.Value);
                }
            }
        }


        /// <summary>
        /// skip special row from datasource
        /// </summary>
        private void SkipRows()
        {
            for (int i = 0; i < this._startRow - 1; i++)
            {
                if (!this.ReadSource()) break;
            }
        }

        /// <summary>
        /// reader data source
        /// </summary>
        /// <returns></returns>
        private bool ReadSource()
        {
            bool blnFlag = false;
            switch (_datasourceType)
            {
                case DataSourceType.DbDataReader:
                    blnFlag = ((IDataReader)this._datasource).Read();
                    break;
                case DataSourceType.TextFile:
                    blnFlag = !((StreamReader)this._datasource).EndOfStream;
                    this._textLine = ((StreamReader)this._datasource).ReadLine();//read a line from text
                    break;
            }
            return blnFlag;
        }

        /// <summary>
        /// close data source
        /// </summary>
        private void CloseDatasource()
        {
            if (null != this._dicNoInitValueParameters) this._dicNoInitValueParameters.Clear();
            if (null != this._dicSourceColumnIndex) this._dicSourceColumnIndex.Clear();
            if (null != this._columnSchema) this._columnSchema.Clear();

            switch (_datasourceType)
            {
                case DataSourceType.DbDataReader:
                    //if need close this source,must be call DbCommand method Cancel()
                    break;
                case DataSourceType.TextFile:
                    if (null != this._datasource)
                    {
                        ((StreamReader)this._datasource).BaseStream.Close();
                        ((StreamReader)this._datasource).Close();
                        this._delimited = null;
                        this._textLine = null;
                        this._dicSourceColumnIndex = null;
                        this._datasource = null;
                    }
                    break;
            }
        }

        /// <summary>
        /// set parameter value from datareader 
        /// </summary>
        /// <param name="rowIndex">current bind row index</param>
        /// <param name="parameters">command parameters</param>
        private void SetValueFromDataReader(int rowIndex, IList<OracleParameter> parameters)
        {
            foreach (var parameter in parameters)
            {
                ((object[])parameter.Value)[rowIndex] = ((IDataReader)this._datasource)[this._dicSourceColumnIndex[parameter.ParameterName]];
            }
        }

        /// <summary>
        /// set object value from text file
        /// </summary>
        /// <param name="rowIndex">current bind row index</param>
        /// <param name="parameters">command parameters</param>
        private void SetValueFromTextFile(int rowIndex, IList<OracleParameter> parameters)
        {
            if (string.IsNullOrEmpty(this._textLine)) return;
            string[] arryList = this._textLine.ToLower().Split(this._delimited, StringSplitOptions.None);
            foreach (var parameter in parameters)
            {
                int intIndex = this._dicSourceColumnIndex[parameter.ParameterName];
                ((object[])parameter.Value)[rowIndex] = intIndex < arryList.Length ? arryList[intIndex] : null;
            }
        }


        /// <summary>
        /// set object source value
        /// </summary>
        /// <param name="rowIndex">current bind row index</param>
        /// <param name="parameters">command parameters</param>
        private void SetParameterValue(int rowIndex, IList<OracleParameter> parameters)
        {
            switch (_datasourceType)
            {
                case DataSourceType.DbDataReader:
                    SetValueFromDataReader(rowIndex, parameters);
                    break;
                case DataSourceType.TextFile:
                    SetValueFromTextFile(rowIndex, parameters);
                    break;
            }
        }

        /// <summary>
        /// write data source to db
        /// </summary>
        private void WriteToDatabase()
        {
            //初始化超时标记
            bool timeIsOver = false;

            //实始化消耗时间
            TakeMilliseconds = 0x0;

            //创建超时定时器
            Timer timer = new Timer(this._timeOut);
            timer.Elapsed += new ElapsedEventHandler((object source, ElapsedEventArgs e) => timeIsOver = true);
            timer.AutoReset = false;
            timer.Enabled = true;

            //创建秒表
            Stopwatch stopwatch = new Stopwatch();

            //检查列映射配置
            VerifyColumnMapping();

            //加载数据源列索引
            LoadSourceColumnIndex();

            //获取目标表结构信息
            GetTableSchemaData();

            //跳过指定行数
            SkipRows();

            OracleConnection oracleConnection;
            if(string.IsNullOrEmpty(this._connectionString))
            {
                 oracleConnection = this._oracleConnection;
            }
            else
            {
                 oracleConnection = new OracleConnection(this._connectionString);
            }
           

            OracleCommand commandOracle = new OracleCommand(BuildBulkInsertCommandText(), oracleConnection) { BindByName = true };

            commandOracle.Parameters.AddRange(BuildCommandParameter());

            //需要绑定值的参数
            IList<OracleParameter> parameterCollection = new List<OracleParameter>();

            foreach (OracleParameter parameter in commandOracle.Parameters)
            {
                if (!this._dicNoInitValueParameters.ContainsKey(parameter.ParameterName))
                {
                    parameterCollection.Add(parameter);
                }
            }

            int intBatchIndex = 0x0;
            int intLineNo = 0x0;
            CopiedRowCount = 0x0;

            try
            {
                //开始记时
                stopwatch.Start();

                oracleConnection.Open();

                while (ReadSource())
                {
                    if (timeIsOver)
                    {
                        throw new OracleBulkInsertException(BulkInsertCode.ExecuteTimeOut, (this._timeOut / OracleBulkInsert.ONE_SECONDS).ToString());
                    }

                    //绑定参数值
                    SetParameterValue(intBatchIndex, parameterCollection);

                    //设定行号
                    if (this._insertRowNumber)
                    {
                        ((object[])commandOracle.Parameters[_rowNumberColumn].Value)[intBatchIndex] = this._startRow - 1 + ++intLineNo;
                    }

                    //批次累加
                    ++intBatchIndex;

                    //未达到提交批次
                    if (intBatchIndex % this._batchSize != 0) continue;

                    commandOracle.ArrayBindCount = intBatchIndex;
                    commandOracle.ExecuteNonQuery();

                    //设置提交行数
                    CopiedRowCount += intBatchIndex;
                    //恢复批次
                    intBatchIndex = 0x0;
                }

                if (intBatchIndex % this._batchSize != 0)
                {//提交最后不足一批的剩余记录
                    commandOracle.ArrayBindCount = intBatchIndex;
                    commandOracle.ExecuteNonQuery();

                    //设置提交行数
                    CopiedRowCount += intBatchIndex;
                }
            }
            catch (OracleBulkInsertException ex)
            {
                throw new OracleBulkInsertException(ex.Message, CopiedRowCount);
            }
            catch (Exception ex)
            {
                throw new OracleBulkInsertException(ex.ToString(), CopiedRowCount);
            }
            finally
            {
                //关闭定时器
                timer.Enabled = false;
                timer.Close();
                timer.Dispose();

                //释放连接
                oracleConnection.Close();
                oracleConnection.Dispose();

                //关闭数据源
                CloseDatasource();

                //关闭秒表
                stopwatch.Stop();
                TakeMilliseconds = stopwatch.ElapsedMilliseconds;
            }
        }


        #endregion

        #region public members

        /// <summary>
        /// write dataReader to oracle
        /// </summary>
        /// <param name="dataReader">source data for write</param>
        /// <returns></returns>
        public void WriteToServer(IDataReader dataReader)
        {
            //set data source type with dbdatareader)
            this._datasourceType = DataSourceType.DbDataReader;
            //set import data source
            this._datasource = dataReader;
            this.WriteToDatabase();
        }

        /// <summary>
        /// write text file to oracle
        /// </summary>
        /// <param name="streamReader">source data for write</param>
        /// <param name="headLine">text file head colum style</param>
        /// <param name="delimited">separator of column</param>
        /// <returns></returns>
        public void WriteToServer(StreamReader streamReader, string headLine, string delimited)
        {
            //set data source type with dbdatareader
            this._datasourceType = DataSourceType.TextFile;

            //initionaize separator
            this._delimited = new string[] { delimited };

            //set file head line
            this._headLine = headLine;

            //set import data source
            this._datasource = streamReader;

            WriteToDatabase();
        }

        #endregion

        #region inner class

        /// <summary>
        /// field description name
        /// </summary>
        private sealed class SchemaField
        {
            #region const properties

            public const string COLUMN_NAME = "columnName";
            public const string COLUMN_TYPE = "columnType";
            public const string LENGTH = "length";
            public const string PRECISION = "precision";
            public const string SCALE = "scale";
            public const string ISNULLABLE = "isnullable";

            #endregion
        }

        /// <summary>
        /// oracle datatype
        /// </summary>
        private enum OracleDbType
        {
            BinaryDouble = 0,
            BinaryFloat = 1,
            Blob = 2,
            Clob = 3,
            Char = 4,
            Date = 5,
            Long = 6,
            NClob = 7,
            Number = 8,
            NVarChar2 = 9,
            Raw = 10,
            Timestamp = 11,
            VarChar2 = 12
        }

        /// <summary>
        /// import datasource type
        /// </summary>
        private enum DataSourceType
        {
            DbDataReader,
            TextFile
        }

        /// <summary>
        /// column mapping type
        /// </summary>
        private enum MappingType
        {
            Normal,
            Extra,
            Rownum
        }

        /// <summary>
        /// save mapping column schema data
        /// </summary>
        private struct ColumnInfo
        {
            #region properties

            public string SourceColumn { get; set; }
            public string ColumnName { get; set; }
            public OracleDbType ColumnType { get; set; }
            public int Precision { get; set; }
            public int Length { get; set; }
            public int Scale { get; set; }
            public bool IsNullable { get; set; }
            public object DefaultValue { get; set; }
            public MappingType MappingType { get; set; }
            #endregion
        }

        #endregion
    }

    #region help Class

    /// <summary>
    /// bulk insert exception class
    /// </summary>
    public sealed class OracleBulkInsertException : DbException
    {
        #region private properties

        private readonly int _errorCode;

        #endregion

        #region public properties

        ///<summary>
        /// override Exception Message properties
        ///</summary>
        public new readonly string Message;
        ///<summary>
        /// returns already commit row count
        ///</summary>
        public readonly int CommitRowCount;

        #endregion

        #region constructor

        /// <summary>
        /// internal default constructor
        /// </summary>
        private OracleBulkInsertException()
        {
        }

        /// <summary>
        /// internal constructor 1
        /// </summary>
        /// <param name="commitRowCount">commit row count</param>
        /// <param name="errorMessage">errorMessage</param>
        internal OracleBulkInsertException(string errorMessage, int commitRowCount)
        {
            this.Message = errorMessage;
            this.CommitRowCount = commitRowCount;
        }

        /// <summary>
        /// internal constructor 2
        /// </summary>
        /// <param name="errorCode">error code</param>
        /// <param name="addtional">error message</param>
        internal OracleBulkInsertException(BulkInsertCode errorCode, params string[] addtional)
        {
            this._errorCode = (int)errorCode;
            this.CommitRowCount = 0;
            switch (errorCode)
            {
                case BulkInsertCode.ColumnMapNotExists:
                    this.Message = string.Format(BulkInsertMessage.COLUMN_MAPPING_NOT_EXISTS, addtional);
                    break;
                case BulkInsertCode.TableSchemaNotExists:
                    this.Message = string.Format(BulkInsertMessage.TABLE_SCHEMA_NOT_EXISTS, addtional);
                    break;
                case BulkInsertCode.ExecuteTimeOut:
                    this.Message = string.Format(BulkInsertMessage.EXECUTE_TIME_OUT, addtional);
                    break;
                case BulkInsertCode.ColumnMappingNull:
                    this.Message = BulkInsertMessage.COLUMN_MAPPING_EMPTY;
                    break;
                case BulkInsertCode.HeadColumnIsNull:
                    this.Message = BulkInsertMessage.HEAD_LINE_IS_NULL;
                    break;
                case BulkInsertCode.ColumnMapNotUnique:
                    this.Message = string.Format(BulkInsertMessage.COLUMN_MAPPING_NOT_UNIQUE, addtional);
                    break;
                case BulkInsertCode.SourceColumnNotExists:
                    this.Message = string.Format(BulkInsertMessage.SOURCE_COLUMN_NOT_EXISTS, addtional);
                    break;
                case BulkInsertCode.RownumColumnExists:
                    this.Message = string.Format(BulkInsertMessage.ROWNUM_COLUMN_EXISTS, addtional);
                    break;
            }
        }

        #endregion

        #region override memeber

        ///<summary>
        ///</summary>
        ///<returns></returns>
        public override string ToString()
        {
            return string.Format("OBI-{0}:{1}\nalready commit row count:{2}\n{3}", this._errorCode, this.Message, this.CommitRowCount, base.ToString());
        }

        #endregion
    }

    /// <summary>
    /// exception message list
    /// </summary>
    internal sealed class BulkInsertMessage
    {
        #region const fields

        internal const string COLUMN_MAPPING_EMPTY = "column mapping is null.";
        internal const string COLUMN_MAPPING_NOT_EXISTS = "destination mapping column [{0}] not exists";
        internal const string TABLE_SCHEMA_NOT_EXISTS = "get destination table [{0}] schema error";
        internal const string EXECUTE_TIME_OUT = "execute time is out [{0}]";
        internal const string FILE_NOT_EXISTS = "text file not exists [{0}]";
        internal const string NOT_SUPPORT_FILE = "just support txt file format [{0}]";
        internal const string HEAD_LINE_IS_NULL = "head column must be provider.";
        internal const string COLUMN_MAPPING_NOT_UNIQUE = "there is a same column  [{0}] mapping between columnMapping and extraMapping";
        internal const string SOURCE_COLUMN_NOT_EXISTS = "source column [{0}] not exists";
        internal const string ROWNUM_COLUMN_EXISTS = "rownum column name [{0}] exists in column/extra mappings";

        #endregion

    }

    /// <summary>
    /// exception code enum
    /// </summary>
    public enum BulkInsertCode
    {
        ColumnMappingNull = 850001,
        ColumnMapNotExists,
        SourceColumnNotExists,
        ColumnMapNotUnique,
        TableSchemaNotExists,
        ExecuteTimeOut,
        HeadColumnIsNull,
        RownumColumnExists,
    }
    #endregion
}
