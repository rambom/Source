using System;
using System.Data;
using System.Collections.Generic;
using System.Linq;
using System.Data.Common;
using Oracle.DataAccess.Client;
using System.IO;
using System.Text;



namespace Amazon_analyzer.Database
{
    /// <summary>
    /// Oracle具体数据库操作类
    /// </summary>
    public class OracleHandler : IDataBase, IDisposable
    {
        /// <summary>
        /// Oracle数据库连接
        /// </summary>
        private OracleConnection _oracleConn = null;
        /// <summary>
        /// 数据库连接字符串
        /// </summary>
        private string _strConnString = string.Empty;
        /// <summary>
        /// 事务处理
        /// </summary>
        private OracleTransaction _oracleTran = null;

        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="strConn">连接字符串</param>
        public OracleHandler(string strConn)
        {
            _strConnString = strConn;
        }
        /// <summary>
        /// 析构函数：关闭数据库连接
        /// </summary>
        ~OracleHandler()
        {
            this.Dispose();
        }
        /// <summary>
        /// 获取参数化绑定前缀
        /// </summary>
        /// <returns>返回字符</returns>
        public string GetDbPrefix()
        {
            return ":";
        }

        /// <summary>
        /// 获取数据库拼接字符 如ORACLE"||"
        /// </summary>
        /// <returns>返回字符</returns>
        public string GetDbJoinString()
        {
            return "||";
        }

        #region 数据库连接开/关
        /// <summary>
        /// 数据库连接打开
        /// </summary>
        public void OpenConn()
        {
            if (_oracleConn == null)
            {
                _oracleConn = new OracleConnection(_strConnString);
                _oracleConn.Open();
            }
            else if (_oracleConn.State != ConnectionState.Open)
            {
                _oracleConn.Open();
            }
        }
        /// <summary>
        /// 数据库连接关闭
        /// </summary>
        public void CloseConn()
        {
            if (_oracleConn == null) return;
            _oracleConn.Close();
            _oracleConn.Dispose();
            _oracleConn = null;
        }
        #endregion

        #region 创建数据库适配器 private OracleDataAdapter GetDataAdapter
        /// <summary>
        /// 创建数据库适配器
        /// </summary>
        /// <param name="strSQL">SQL语句</param>
        /// <returns>返回数据</returns>
        private OracleDataAdapter GetDataAdapter(string strSQL)
        {
            return GetDataAdapter(strSQL, null);
        }

        /// <summary>
        /// 创建数据库适配器，带参数
        /// </summary>
        /// <param name="strSQL">SQL语句</param>
        /// <param name="arrParam">参数</param>
        /// <returns>返回数据</returns>
        private OracleDataAdapter GetDataAdapter(string strSQL, List<DataParameter> arrParam)
        {
            return GetDataAdapter(strSQL, arrParam, CommandType.Text);
        }
        /// <summary>
        ///  创建数据库适配器，带参数和类型
        /// </summary>
        /// <param name="strSQL">SQL语句</param>
        /// <param name="arrParam">参数</param>
        /// <param name="comType">命令类型</param>
        /// <returns>OracleDataAdapter</returns>
        private OracleDataAdapter GetDataAdapter(string strSQL, List<DataParameter> arrParam, CommandType comType)
        {
            OpenConn();
            OracleDataAdapter myAdp = new OracleDataAdapter(strSQL, _oracleConn);
            myAdp.SelectCommand.BindByName = true;
            myAdp = AnalysisParameter(myAdp, arrParam);
            //只有存储过程才判断添加输出参数
            if (comType == CommandType.StoredProcedure)
            {
                //添加输出参数
                myAdp = AnalysisParameter(myAdp, strSQL);
            }

            if (this._oracleTran != null)
            {
                myAdp.SelectCommand.Transaction = _oracleTran;
            }
            myAdp.SelectCommand.CommandType = comType == CommandType.StoredProcedure ? CommandType.StoredProcedure : CommandType.Text;
            return myAdp;
        }
        #endregion

        #region 创建数据库命令 private OracleCommand GetOracleCommand
        /// <summary>
        /// 创建数据库命令
        /// </summary>
        /// <param name="strSql">SQL语句</param>
        /// <returns>OracleCommand</returns>
        private OracleCommand GetOracleCommand(string strSql)
        {
            return GetOracleCommand(strSql, null);
        }
        /// <summary>
        /// 创建数据库命令，带参数
        /// </summary>
        /// <param name="strSql">SQL语句</param>
        /// <param name="arrParam">参数</param>
        /// <returns>OracleCommand</returns>
        private OracleCommand GetOracleCommand(string strSql, List<DataParameter> arrParam)
        {
            return GetOracleCommand(strSql, arrParam, CommandType.Text);
        }
        /// <summary>
        /// 创建数据库命令，带参数和类型
        /// </summary>
        /// <param name="strSql">SQL语句</param>
        /// <param name="arrParam">参数</param>
        /// <param name="comdType">CommandType类型</param>
        /// <returns>OracleCommand</returns>
        private OracleCommand GetOracleCommand(string strSql, List<DataParameter> arrParam, CommandType comdType)
        {
            OpenConn();
            OracleCommand myComd = new OracleCommand(strSql, _oracleConn);
            myComd.BindByName = true;
            if (_oracleTran != null)
            {
                myComd.Transaction = _oracleTran;
            }
            myComd.CommandType = comdType == CommandType.StoredProcedure ? CommandType.StoredProcedure : CommandType.Text;
            return AnalysisParameter(myComd, arrParam);
        }
        #endregion

        #region IDataBase 成员

        #region 事物处理 开/提交/回滚
        /// <summary>
        /// 事物开启
        /// </summary>
        public void OpenTrans()
        {
            OpenConn();
            if (_oracleTran == null)
            {
                _oracleTran = _oracleConn.BeginTransaction();
            }
        }

        /// <summary>
        /// 事物提交
        /// </summary>
        public void CommitTrans()
        {
            if (_oracleTran == null)
            {
                throw new NullReferenceException("没有事务可进行提交!");
            }
            else
            {
                _oracleTran.Commit();
                _oracleTran.Dispose();
                _oracleTran = null;
                CloseConn();
            }
        }

        /// <summary>
        /// 事物回滚
        /// </summary>
        public void RollbackTrans()
        {
            if (_oracleTran == null)
            {
                throw new NullReferenceException("没有事务可进行回滚!");
            }
            else
            {
                _oracleTran.Rollback();
                _oracleTran.Dispose();
                _oracleTran = null;
                CloseConn();
            }
        }


        #endregion

        /// <summary>
        /// 执行SQL语句，返回行数
        /// </summary>
        /// <param name="strSql">SQL语句</param>
        /// <param name="arrParam">参数数组</param>
        /// <returns>影响行数</returns>
        public int ExecuteNoQuery(string strSql, List<DataParameter> arrParam)
        {
            OracleCommand myComd = GetOracleCommand(strSql, arrParam);
            string begintime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            OpenConn();
            int intCount = myComd.ExecuteNonQuery();
            string endtime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            return intCount;
        }

        /// <summary>
        /// 执行SQL语句返回DataTable
        /// </summary>
        /// <param name="strSql">SQL语句</param>
        /// <param name="arrParam">参数数组</param>
        /// <returns>DataTable</returns>
        public DataTable ExecuteDataTable(string strSql, List<DataParameter> arrParam)
        {
            DataTable dt = new DataTable();
            string begintime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            OracleDataAdapter myAdp = GetDataAdapter(strSql, arrParam);
            OpenConn();
            myAdp.Fill(dt);
            string endtime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            return dt;
        }

        /// <summary>
        /// 执行SQL语句返回DataSet
        /// </summary>
        /// <param name="strSql">SQL语句</param>
        /// <param name="arrParam">参数数组</param>
        /// <returns>DataSet</returns>
        public DataSet ExecuteDataSet(string strSql, List<DataParameter> arrParam)
        {
            DataSet ds = new DataSet();
            string begintime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            OracleDataAdapter myAdp = GetDataAdapter(strSql, arrParam);
            OpenConn();
            myAdp.Fill(ds);
            string endtime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            return ds;
        }

        /// <summary>
        /// 执行SQL语句返回Scalar
        /// </summary>
        /// <param name="strSql">SQL语句</param>
        /// <param name="arrParam">参数数组</param>
        /// <returns>Scalar string</returns>
        public dynamic ExecuteScalar(string strSql, List<DataParameter> arrParam)
        {
            string begintime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            OracleCommand myComd = GetOracleCommand(strSql, arrParam);
            OpenConn();
            dynamic dynReturn = myComd.ExecuteScalar();
            string endtime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            return dynReturn;
        }

        /// <summary>
        /// 执行SQL语句返回DbDataReader
        /// </summary>
        /// <param name="strSql">SQL语句</param>
        /// <param name="arrParam">参数数组</param>
        /// <returns>DbDataReader</returns>
        public DbDataReader ExecuteDataReader(string strSql, List<DataParameter> arrParam)
        {
            string begintime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            OracleCommand myComd = GetOracleCommand(strSql, arrParam);
            OpenConn();
            DfkDataReader dfkdr = new DfkDataReader(myComd, myComd.ExecuteReader(CommandBehavior.CloseConnection));
            string endtime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            return dfkdr;
        }
        /// <summary>
        /// 执行存储过程返回DbDataReader
        /// </summary>
        /// <param name="strSpName">SQL语句</param>
        /// <param name="arrParam">参数数组</param>
        /// <returns>DbDataReader</returns>
        public DbDataReader ExecuteDataReaderSp(string strSpName, List<DataParameter> arrParam)
        {
            string begintime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            OracleCommand myComd = GetOracleCommand(strSpName, arrParam);
            OpenConn();
            DfkDataReader dfkdr = new DfkDataReader(myComd, myComd.ExecuteReader(CommandBehavior.CloseConnection));
            string endtime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            return dfkdr;
        }

        /// <summary>
        /// 将DataTable数据集导入到数据库
        /// </summary>
        /// <param name="dtImport">数据集</param>
        /// <param name="strSql">执行SQL</param>
        public void WriteDataTableToDB(DataTable dtImport, string strSql)
        {
            WriteDataTableToDB(dtImport, strSql, 0);
        }
        /// <summary>
        /// 将设定开始行的DataTable数据集导入到数据库
        /// </summary>
        /// <param name="dtImport">数据集</param>
        /// <param name="strSql">执行SQL</param>
        /// <param name="intStartRow">读取数据集的起始行</param>
        public void WriteDataTableToDB(DataTable dtImport, string strSql, int intStartRow)
        {
            OracleDataAdapter myAdp = GetDataAdapter(strSql);
            DataTable dtWrite = new DataTable();
            myAdp.Fill(dtWrite);
            OracleCommandBuilder comBuilder = new OracleCommandBuilder(myAdp);//这一句很关键，必不可少。
            if (dtWrite.Columns.Count != dtImport.Columns.Count)
            {
                throw new Exception("对不起，导入文件的列数不符合导入规范，请检查！");
            }
            for (int i = intStartRow; i < dtImport.Rows.Count; i++)
            {
                dtWrite.Rows.Add(dtImport.Rows[i].ItemArray);
            }
            OpenConn();
            myAdp.Update(dtWrite);
        }
        /// <summary>
        /// 以Adapter方式导入Datatable映射的列到数据库表
        /// </summary>
        /// <param name="dataTable">要导入的dataTable</param>
        /// <param name="tableName">目标表名</param>
        /// <param name="columnMapping">列映射关系(第1列为源列,第2列为目标列)</param>
        /// <returns>成功返回success，否则为返回错误消息</returns>
        public string ImportDatatableToDB(DataTable dataTable, string tableName, DataRow[] columnMapping)
        {
            string field = "";
            foreach (DataRow row in columnMapping)
            {
                field = field + "," + row[1];
                dataTable.Columns[row[0].ToString()].ColumnName = row[1].ToString();//映射列名                
            }
            if (field.StartsWith(","))
                field = field.Substring(1);

            OracleDataAdapter adapter = GetDataAdapter(string.Format("select {0} from {1} where 1=2", field, tableName));
            OracleCommandBuilder cmb = new OracleCommandBuilder(adapter);
            OpenConn();
            adapter.Update(dataTable);
            return "success";
        }
        /// <summary>
        /// 快速导入dataTable映射的列至数据库表(目标列名区分大小写)
        /// </summary>
        /// <param name="dataTable">要导入的dataTable</param>
        /// <param name="tableName">目标表名</param>
        /// <param name="columnMapping">列映射关系</param>
        /// <returns>成功返回success，否则为返回错误消息</returns>
        public string FastImportDatatableToDB(DataTable dataTable, string tableName, DataRow[] columnMapping)
        {
            OracleBulkCopy bulkCopy = new OracleBulkCopy(this._strConnString, OracleBulkCopyOptions.UseInternalTransaction);
            bulkCopy.DestinationTableName = tableName;
            foreach (DataRow row in columnMapping)
            {
                bulkCopy.ColumnMappings.Add(row[0].ToString(), row[1].ToString());
            }
            OpenConn();
            bulkCopy.WriteToServer(dataTable);
            return "success";
        }

        /// <summary>
        /// 快速导入dataTable映射的列名称至数据库表(目标列名区分大小写)
        /// </summary>
        /// <param name="dataTable">要导入的dataTable</param>
        /// <param name="tableName">目标表名</param>
        /// <param name="arrStrTarGetColumns">目标列名</param>
        /// <returns>成功返回success，否则为返回错误消息</returns>
        public string FastImportDatatableToDB(DataTable dataTable, string tableName, string[] arrStrTarGetColumns)
        {
            Dictionary<string, string> columnMapping = new Dictionary<string, string>();

            for (int i = 0; i < arrStrTarGetColumns.Length; i++)
            {
                columnMapping.Add(arrStrTarGetColumns[i], dataTable.Columns[i].ColumnName);
            }
            OracleBulkInsert bulkCopy = new OracleBulkInsert(this._strConnString, tableName, columnMapping);
            OpenConn();
            bulkCopy.WriteToServer(dataTable.CreateDataReader());
            return "success";
        }

        /// <summary>
        /// 快速导入dataTable映射的列名称至数据库表(目标列名区分大小写)
        /// </summary>
        /// <param name="dataTable">要导入的dataTable</param>
        /// <param name="tableName">目标表名</param>
        /// <param name="arrStrTarGetColumns">目标列名</param>
        /// <returns>成功返回success，否则为返回错误消息</returns>
        public string FastImportDatatableToDBNotClose(DataTable dataTable, string tableName, string[] arrStrTarGetColumns)
        {
            Dictionary<string, string> columnMapping = new Dictionary<string, string>();

            for (int i = 0; i < arrStrTarGetColumns.Length; i++)
            {
                columnMapping.Add(arrStrTarGetColumns[i], dataTable.Columns[i].ColumnName);
            }
            OracleBulkInsert bulkCopy = new OracleBulkInsert(this._oracleConn, tableName, columnMapping);
            OpenConn();
            bulkCopy.WriteToServer(dataTable.CreateDataReader());
            return "success";
        }

        /// <summary>
        /// 快速DataReader映射的列至数据库表，并设置最大行数和每次提交行数，计算执行的时间
        /// </summary>
        /// <param name="strConnectionString">目的数据库连接字符串</param>
        /// <param name="intBatchSize">每次导入多少条后提交</param>
        /// <param name="intTimeOut">导入最大超时时间</param>
        /// <param name="dataReader">要导入的DataReader</param>
        /// <param name="strTableName">目标表名</param>
        /// <param name="columnMapping">列映射关系(目标列,源列)</param>
        /// <param name="intCommitRowCount">导入成功行数</param>
        /// <param name="spentTime">消耗时间</param>
        /// <param name="strErrMsg">错误消息</param>
        /// <returns>true成功，false失败</returns>
        public bool FastImportDataReaderToDb(string strConnectionString, int intBatchSize, int intTimeOut, IDataReader dataReader, string strTableName, Dictionary<string, string> columnMapping, out int intCommitRowCount, out long spentTime, out string strErrMsg)
        {
            bool blnFlag = false;
            intCommitRowCount = 0;
            spentTime = 0;
            strErrMsg = string.Empty;

            OracleBulkInsert oracleBulkInsert = new OracleBulkInsert(strConnectionString, strTableName, columnMapping, null, 1, null, intBatchSize, intTimeOut);

            try
            {
                OpenConn();
                oracleBulkInsert.WriteToServer(dataReader);
                blnFlag = true;
            }
            catch (OracleBulkInsertException ex)
            {
                strErrMsg = ex.ToString();
            }
            finally
            {
                intCommitRowCount = oracleBulkInsert.CopiedRowCount;
                spentTime = oracleBulkInsert.TakeMilliseconds;
            }

            return blnFlag;
        }

        /// <summary>
        /// 快速导入从第几行开始的文本文件映射的列至数据库表。并设置最大行数和每次提交行数，计算执行的时间。
        /// </summary>
        /// <param name="strConnectionString">目的数据库连接字符串,oracle连接字符串如："Data Source=(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=xxxx)(PORT=1521))(CONNECT_DATA=(SID=yyyy)));User Id=sys;Password=sys;"</param>
        /// <param name="intTimeOut">导入最大超时时间</param>
        /// <param name="strTxtFile">要导入的文本文件路径</param>
        /// <param name="strHead">列头格式带分隔符</param>
        /// <param name="strDelimited">列分隔符</param>
        /// <param name="strTableName">目标表名</param>
        /// <param name="columnMapping">列映射关系(目标列,源列)</param>
        /// <param name="intBatchSize">每次导入多少条后提交</param>
        /// <param name="extraMapping">额外列映射</param>
        /// <param name="strRowNumberColumn">保存行号列名(null:不保存行号)</param>
        /// <param name="intCommitRowCount">导入成功行数</param>
        /// <param name="lSpentTime">消耗时间</param>
        /// <param name="strErrMsg">错误消息</param>
        /// <param name="intStartRow">从第X行开始导入</param>
        /// <returns>true成功，false失败</returns>
        public bool FastImportTextToDb(string strConnectionString, int intBatchSize, int intTimeOut, string strTxtFile, string strHead, string strDelimited, string strTableName, Dictionary<string, string> columnMapping, Dictionary<string, object> extraMapping, uint intStartRow, string strRowNumberColumn, out int intCommitRowCount, out long lSpentTime, out string strErrMsg)
        {
            bool blnFlag = false;
            intCommitRowCount = 0;
            lSpentTime = 0;
            strErrMsg = string.Empty;

            if (!".txt".Equals(Path.GetExtension(strTxtFile), StringComparison.CurrentCultureIgnoreCase))
            {
                throw new Exception("只支持导入txt文件...");
            }

            FileStream fileStream = new FileStream(strTxtFile, FileMode.Open, FileAccess.Read, FileShare.None);
            StreamReader streamReader = new StreamReader(fileStream, Encoding.UTF8);

            OracleBulkInsert oracleBulkInsert = new OracleBulkInsert(strConnectionString, strTableName, columnMapping, extraMapping, intStartRow, strRowNumberColumn, intBatchSize, intTimeOut);

            try
            {
                oracleBulkInsert.WriteToServer(streamReader, strHead, strDelimited);
                blnFlag = true;
            }
            catch (OracleBulkInsertException ex)
            {
                strErrMsg = ex.ToString();
            }
            finally
            {
                intCommitRowCount = oracleBulkInsert.CopiedRowCount;
                lSpentTime = oracleBulkInsert.TakeMilliseconds;
            }
            return blnFlag;
        }

        /// <summary>
        /// 执行存储过程返回DataSet
        /// </summary>
        /// <param name="proName">存储过程</param>
        /// <param name="arrParam">参数数组</param>
        /// <returns>DataSet</returns>
        public DataSet ExecuteDataSetSP(string proName, List<DataParameter> arrParam)
        {
            DataSet ds = new DataSet();
            string begintime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            OracleDataAdapter myAdp = GetDataAdapter(proName, arrParam, CommandType.StoredProcedure);
            OpenConn();
            myAdp.Fill(ds);
            string endtime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            return GetOutPutDataSet(ds, myAdp);
        }

        /// <summary>
        /// 执行存储过程返回Scalar
        /// </summary>
        /// <param name="proName">存储过程</param>
        /// <param name="arrParam">参数数组</param>
        /// <returns>Scalar string</returns>
        public dynamic ExecuteScalarSP(string proName, List<DataParameter> arrParam)
        {
            string begintime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            OracleCommand myComd = GetOracleCommand(proName, arrParam, CommandType.StoredProcedure);
            OpenConn();
            dynamic dynReturn = myComd.ExecuteScalar();
            string endtime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            return dynReturn;
        }

        /// <summary>
        /// 执行存储过程返回输出参数
        /// </summary>
        /// <param name="proName">存储过程</param>
        /// <param name="arrParam">参数数组</param>
        /// <returns>输出参数的值</returns>
        public string ExecuteStringSP(string proName, List<DataParameter> arrParam)
        {
            string begintime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            OracleCommand myComd = GetOracleCommand(proName, arrParam, CommandType.StoredProcedure);

            //获得输出参数的数据集
            DataTable dataTable = GetOutParameter(proName, "out");
            string endtime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();

            OracleParameter[] arr = new OracleParameter[dataTable.Rows.Count];
            if (dataTable.Rows.Count > 0)
            {
                arr[0] = new OracleParameter(dataTable.Rows[0]["PARAMETER_NAME"].ToString().TrimStart(':'),
                    GetOutParameterType(dataTable.Rows[0]["TYPE_NAME"].ToString()), 500);
                if (dataTable.Rows[0]["OBJECT_TYPE"].ToString() == "FUNCTION")
                {
                    arr[0].Direction = ParameterDirection.ReturnValue;
                }
                else
                {
                    arr[0].Direction = ParameterDirection.Output;
                }
                myComd.Parameters.Add(arr[0]);
                OpenConn();
                myComd.ExecuteNonQuery();
                return arr[0].Value.ToString();
            }
            return "";
        }

        /// <summary>
        /// 执行存储过程，无返回参数
        /// </summary>
        /// <param name="proName">存储过程</param>
        /// <param name="arrParam">参数数组</param>
        public void ExecuteNoQuerySP(string proName, List<DataParameter> arrParam)
        {
            string begintime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
            OracleCommand myComd = GetOracleCommand(proName, arrParam, CommandType.StoredProcedure);
            OpenConn();
            myComd.ExecuteNonQuery();
            string endtime = DateTime.Now.ToString() + "  " + DateTime.Now.Millisecond.ToString();
        }

        /// <summary>
        /// 查询获取分页结果集
        /// </summary>
        /// <param name="pagedTableName">表名</param>
        /// <param name="pagedSelectColumns">列</param>
        /// <param name="pagedPrimaryKey">主键</param>
        /// <param name="whereCondition">条件</param>
        /// <param name="arrParam">条件参数</param>
        /// <param name="pagedOrderColumns">排序</param>
        /// <param name="lowIndex">起始</param>
        /// <param name="topIndex">截至</param>
        /// <returns>返回查询结果集</returns>
        public DataTable ExecuteDataTablePaged(string pagedTableName, string pagedSelectColumns, string pagedPrimaryKey, string whereCondition, List<DataParameter> arrParam, string pagedOrderColumns, int lowIndex, int topIndex)
        {
            if (pagedSelectColumns.Trim() == "*")
            {
                pagedSelectColumns = pagedTableName + ".*";
            }

            if (!string.IsNullOrEmpty(pagedOrderColumns))
            {
                pagedOrderColumns += "order by " + pagedOrderColumns + "";
            }
            string strReturn = "select * from (select temp.*,rownum as num from " +
                                " ( " +
                                " select " + pagedSelectColumns + "from " + pagedTableName +
                                " where 1=1 " + whereCondition + pagedOrderColumns +
                                " ) temp where rownum <=" + topIndex +
                                ") where num > " + lowIndex;
            return ExecuteDataTable(string.Format(strReturn, GetDbPrefix(), "||"), arrParam);
        }

        /// <summary>
        /// 查询获取分页总记录数
        /// </summary>
        /// <param name="pagedTbleName">表名</param>
        /// <param name="whereCondition">条件</param>
        /// <param name="arrParam">条件参数</param>
        /// <returns>返回查询结果集</returns>
        public DataTable ExecuteDataTablePaged(string pagedTbleName, string whereCondition, List<DataParameter> arrParam)
        {
            return ExecuteDataTable(
                string.Format("select count(1) from " + pagedTbleName + " where " + whereCondition, GetDbPrefix(), "||"),
                arrParam);
        }

        /// <summary>
        /// 获得输出的数据集
        /// </summary>
        /// <param name="dataSet">返回记录集的DataSet</param>
        /// <param name="myAdp">OracleDataAdapter对象</param>
        /// <returns>返回查询结果集</returns>
        private DataSet GetOutPutDataSet(DataSet dataSet, OracleDataAdapter myAdp)
        {

            for (int i = 0; i < myAdp.SelectCommand.Parameters.Count; i++)
            {
                if ((myAdp.SelectCommand.Parameters[i].Direction == ParameterDirection.Output || myAdp.SelectCommand.Parameters[i].Direction == ParameterDirection.ReturnValue)
                    && myAdp.SelectCommand.Parameters[i].OracleDbType != OracleDbType.RefCursor)
                {
                    if (myAdp.SelectCommand.Parameters[i].Value != null)
                    {
                        //构造存储总记录数的数据表
                        DataTable dataTable = new DataTable();
                        dataTable.Columns.Add("c1");
                        DataRow dataRow = dataTable.NewRow();
                        dataRow[0] = myAdp.SelectCommand.Parameters[i].Value.ToString();
                        dataTable.Rows.Add(dataRow);
                        dataSet.Tables.Add(dataTable);
                    }
                }
            }
            return dataSet;
        }
        /// <summary>
        /// 获得输出参数的类型
        /// </summary>
        /// <param name="strTypeName">类型名称</param>
        private dynamic GetOutParameterType(string strTypeName)
        {
            dynamic dO = null;
            switch (strTypeName)
            {
                case "NUMBER":
                    dO = OracleDbType.Decimal;
                    break;
                case "REF CURSOR":
                    dO = OracleDbType.RefCursor;
                    break;
                case "VARCHAR2":
                    dO = OracleDbType.Varchar2;
                    break;
                default:
                    dO = OracleDbType.Object;
                    break;
            }
            return dO;
        }

        #endregion

        #region IDisposable 接口实现
        /// <summary>
        /// 清理回收
        /// </summary>
        public void Dispose()
        {
            CloseConn();
        }
        #endregion

        #region other
        /// <summary>
        /// 解析参数到OracleCommand
        /// </summary>
        /// <param name="comd">OracleCommand</param>
        /// <param name="dataParameter">Dcjet参数集合</param>
        /// <returns>绑定好参数的OracleCommand</returns>
        private OracleCommand AnalysisParameter(OracleCommand comd, List<DataParameter> dataParameter)
        {
            //先清空参数列表
            comd.Parameters.Clear();
            //循环添加参数
            if (dataParameter != null)
            {
                foreach (DataParameter dbParam in dataParameter)
                {
                    comd.Parameters.Add(GetSqlParameter(dbParam));
                }
            }

            return comd;
        }
        /// <summary>
        /// 解析参数到OracleDataAdapter
        /// </summary>
        /// <param name="myAdp">OracleDataAdapter</param>
        /// <param name="dataParameter">Dcjet参数集合</param>
        /// <returns>绑定好参数的OracleDataAdapter</returns>
        private OracleDataAdapter AnalysisParameter(OracleDataAdapter myAdp, List<DataParameter> dataParameter)
        {
            //先清空参数列表
            myAdp.SelectCommand.Parameters.Clear();
            //循环添加参数
            if (dataParameter != null)
            {
                foreach (DataParameter dbParam in dataParameter.Where(dbParam => dbParam != null))
                {
                    myAdp.SelectCommand.Parameters.Add(GetSqlParameter(dbParam));
                }
            }
            return myAdp;
        }
        /// <summary>
        /// 解析参数名称到OracleDataAdapter
        /// </summary>
        /// <param name="myAdp">OracleDataAdapter</param>
        /// <param name="strProcedureName">存储过程名称</param>
        /// <returns>绑定好参数的OracleDataAdapter</returns>
        private OracleDataAdapter AnalysisParameter(OracleDataAdapter myAdp, string strProcedureName)
        {
            //获得输出参数的数据集
            DataTable dataTable = GetOutParameter(strProcedureName, "OUT");
            if (dataTable.Rows.Count == 0)
            {
                return myAdp;
            }
            foreach (DataRow dataRow in dataTable.Rows)
            {
                OracleParameter parameter;
                if (dataRow["OBJECT_TYPE"].ToString() == "FUNCTION")
                {
                    //如果是函数，应使用return
                    parameter = new OracleParameter(dataRow["PARAMETER_NAME"].ToString(),
                                                    GetOutParameterType(dataRow["TYPE_NAME"].ToString()),
                                                    ParameterDirection.ReturnValue);
                    if (dataRow["TYPE_NAME"].ToString() == "VARCHAR2")
                    {
                        parameter.Size = 2000;
                    }
                }
                else
                {
                    //如果是存储过程，使用out
                    parameter = new OracleParameter(dataRow["PARAMETER_NAME"].ToString(),
                                                    GetOutParameterType(dataRow["TYPE_NAME"].ToString()),
                                                    ParameterDirection.Output);
                    //因为输出参数不设置长度，单字符型必须得有长度，所以这边写死了
                    if (dataRow["TYPE_NAME"].ToString() == "VARCHAR2")
                    {
                        parameter.Size = 2000;
                    }
                }
                myAdp.SelectCommand.Parameters.Add(parameter);

            }
            return myAdp;
        }
        /// <summary>
        /// 转换DataParameter到OracleParameter
        /// </summary>
        /// <param name="dbparam">参数列表</param>
        /// <returns>OracleParameter</returns>
        private OracleParameter GetSqlParameter(DataParameter dbparam)
        {
            OracleParameter oracleParam = new OracleParameter
            {
                ParameterName = dbparam.ParameterName,
                Size = dbparam.Size,
                Value = dbparam.Value,
                DbType = dbparam.DbType
            };
            return oracleParam;
        }

        /// <summary>
        /// 获得存储过程的参数的集合
        /// </summary>
        /// <param name="strSpName">存储过程名称</param>
        /// <param name="strParameterType">参数类型：(输出:OUT;输入:IN)</param>
        /// <returns>返回DataTable</returns>
        private DataTable GetOutParameter(string strSpName, string strParameterType)
        {
            string strDBOwner = GetDBOwner();
            //            string strSql = @"SELECT argument_name PARAMETER_NAME, data_type TYPE_NAME,in_out PARAMETER_TYPE
            //                              from all_arguments WHERE upper(in_out)=upper(:in_out) and object_id in 
            //                              (select object_id from all_objects where upper(object_name)=upper(:object_Name) and upper(owner)=upper(:dbOwner))";

            string strSql = @"SELECT argument_name PARAMETER_NAME, data_type TYPE_NAME,in_out PARAMETER_TYPE,object_type
                            from all_arguments 
                            inner join 
                            (select object_id,object_type from all_objects 
                            where upper(object_name)=upper(:object_Name) and upper(owner)=upper(:dbOwner) AND (object_type='PROCEDURE' or object_type='FUNCTION') 
                            ) tmp on all_arguments.object_id=tmp.object_id 
                            where  all_arguments.in_out=upper(:in_out)";
            List<DataParameter> dbpara = new List<DataParameter>();
            dbpara.Add(new DataParameter(paramName: "in_out", paramType: DbType.String, paramSize: 200, paramValue: strParameterType));
            dbpara.Add(new DataParameter(paramName: "object_Name", paramType: DbType.String, paramSize: 200, paramValue: strSpName));
            dbpara.Add(new DataParameter(paramName: "dbOwner", paramType: DbType.String, paramSize: 200, paramValue: strDBOwner));
            return ExecuteDataTable(strSql, dbpara);
        }
        #endregion

        /// <summary>
        /// 获得数据库的所有者，用于获取存储过程参数
        /// </summary>
        /// <returns>数据库的所有者</returns>
        private string GetDBOwner()
        {
            string strReturn = "";
            string[] Conn = this._oracleConn.ConnectionString.ToUpper().Split(';');
            foreach (string t in Conn.Where(t => t.IndexOf("USER ID") >= 0))
            {
                strReturn = t.Split('=')[1];
                break;
            }
            return strReturn;
        }



    }
}
