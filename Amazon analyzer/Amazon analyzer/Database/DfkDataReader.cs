using System;
using System.Data.Common;
using System.Data;

namespace Amazon_analyzer.Database
{
    /// <summary>
    /// DataReader类
    /// </summary>
    class DfkDataReader : DbDataReader
    {
        private DbDataReader dataReader;
        private DbCommand command;


        /// <summary>
        /// 构造函数，带DbCommand和DataReader参数
        /// </summary>
        /// <param name="dbcmd">DbCommand</param>
        /// <param name="ddr">DataReader</param>
        internal DfkDataReader(DbCommand dbcmd, DbDataReader ddr)
        {
            this.dataReader = ddr;
            this.command = dbcmd;
        }

        /// <summary>
        /// 关闭对象
        /// </summary>
        public override void Close()
        {
            command.Cancel();
            dataReader.Close();
        }

        /// <summary>
        /// 获取一个当前行嵌套深度的值
        /// </summary>
        public override int Depth
        {
            get { return dataReader.Depth; }
        }

        /// <summary>
        /// 获取当前行的列数
        /// </summary>
        public override int FieldCount
        {
            get { return dataReader.FieldCount; }
        }

        /// <summary>
        /// 获取指定列布尔形式的值
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override bool GetBoolean(int ordinal)
        {
            return dataReader.GetBoolean(ordinal);
        }

        /// <summary>
        /// 获取指定列字节形式的值
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override byte GetByte(int ordinal)
        {
            return dataReader.GetByte(ordinal);
        }

        /// <summary>
        /// 从指定列读取一个字节流，读到缓冲区中
        /// </summary>
        /// <param name="ordinal">从第几行开始读取</param>
        /// <param name="dataOffset">栏位的索引</param>
        /// <param name="buffer">目的缓冲区</param>
        /// <param name="bufferOffset">缓冲区内开始索引</param>
        /// <param name="length">复制到缓冲区的最大长度</param>
        /// <returns>字节流</returns>
        public override long GetBytes(int ordinal, long dataOffset, byte[] buffer, int bufferOffset, int length)
        {
            return dataReader.GetBytes(ordinal, dataOffset, buffer, bufferOffset, length);
        }

        /// <summary>
        /// 获取指定列单字符串形式的值
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override char GetChar(int ordinal)
        {
            return dataReader.GetChar(ordinal);
        }

        /// <summary>
        /// 从指定列读取一个字符集流，读到缓冲区中
        /// </summary>
        /// <param name="ordinal">从第几行开始读取</param>
        /// <param name="dataOffset">栏位的索引</param>
        /// <param name="buffer">目的缓冲区</param>
        /// <param name="bufferOffset">缓冲区内开始索引</param>
        /// <param name="length">复制到缓冲区的最大长度</param>
        /// <returns>字符集流</returns>
        public override long GetChars(int ordinal, long dataOffset, char[] buffer, int bufferOffset, int length)
        {
            return dataReader.GetChars(ordinal, dataOffset, buffer, bufferOffset, length);
        }

        /// <summary>
        /// 获取指定类的数据类习惯
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override string GetDataTypeName(int ordinal)
        {
            return dataReader.GetDataTypeName(ordinal);
        }

        /// <summary>
        /// 获取指定类DateTime对象形式的值
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override DateTime GetDateTime(int ordinal)
        {
            return dataReader.GetDateTime(ordinal);
        }

        /// <summary>
        /// 获取指定类Decimal对象形式的值
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override decimal GetDecimal(int ordinal)
        {
            return dataReader.GetDecimal(ordinal);
        }

        /// <summary>
        /// 获取指定类Double对象形式的值
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override double GetDouble(int ordinal)
        {
            return dataReader.GetDouble(ordinal);
        }

        /// <summary>
        /// 返回一个System.Collections.IEnumerator值，可以循环访问数据器中的行
        /// </summary>
        /// <returns>返回集合</returns>
        public override System.Collections.IEnumerator GetEnumerator()
        {
            return dataReader.GetEnumerator();
        }

        /// <summary>
        /// 获取指定类型的数据类型
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override Type GetFieldType(int ordinal)
        {
            return dataReader.GetFieldType(ordinal);
        }

        /// <summary>
        /// 获取指定列单精度浮点数形式的值
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override float GetFloat(int ordinal)
        {
            return dataReader.GetFloat(ordinal);
        }

        /// <summary>
        /// 获取指定列GUID形式的值
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override Guid GetGuid(int ordinal)
        {
            return dataReader.GetGuid(ordinal);
        }

        /// <summary>
        /// 获取指定列16位带符号的整数形式的值
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override short GetInt16(int ordinal)
        {
            return dataReader.GetInt16(ordinal);
        }

        /// <summary>
        /// 获取指定列32位带符号的整数形式的值
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override int GetInt32(int ordinal)
        {
            return dataReader.GetInt32(ordinal);
        }

        /// <summary>
        /// 获取指定列64位带符号的整数形式的值
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override long GetInt64(int ordinal)
        {
            return dataReader.GetInt64(ordinal);
        }

        /// <summary>
        /// 给定了从0开始的列序号时，获取列的名称
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override string GetName(int ordinal)
        {
            return dataReader.GetName(ordinal);
        }

        /// <summary>
        /// 给定列名称是获得列序号
        /// </summary>
        /// <param name="name">列名称</param>
        /// <returns>返回值</returns>
        public override int GetOrdinal(string name)
        {
            return dataReader.GetOrdinal(name);
        }

        /// <summary>
        /// dataReader转换为datatable
        /// </summary>
        /// <returns>返回datatable</returns>
        public override DataTable GetSchemaTable()
        {
            return dataReader.GetSchemaTable();
        }

        /// <summary>
        /// 获取指定列为String的实例的值
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override string GetString(int ordinal)
        {
            return dataReader.GetString(ordinal);
        }

        /// <summary>
        /// 获取指定列为object的实例的值
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override object GetValue(int ordinal)
        {
            return dataReader.GetValue(ordinal);
        }

        /// <summary>
        /// 使用当前列来填充对象数组
        /// </summary>
        /// <param name="values">对象数组</param>
        /// <returns>返回值</returns>
        public override int GetValues(object[] values)
        {
            return dataReader.GetValues(values);
        }

        /// <summary>
        /// 判断dataReader是否有数据
        /// </summary>
        public override bool HasRows
        {
            get { return dataReader.HasRows; }
        }

        /// <summary>
        /// 判断dataReader是否已关闭
        /// </summary>
        public override bool IsClosed
        {
            get { return dataReader.IsClosed; }
        }

        /// <summary>
        /// 获取一个值，该值指示列中是否包含不存在的或已丢失的值。
        /// </summary>
        /// <param name="ordinal">列序号</param>
        /// <returns>返回值</returns>
        public override bool IsDBNull(int ordinal)
        {
            return dataReader.IsDBNull(ordinal);
        }

        /// <summary>
        /// 使读取器前进到下个结果
        /// </summary>
        /// <returns>返回读取的结果</returns>
        public override bool NextResult()
        {
            return dataReader.NextResult();
        }

        /// <summary>
        /// 使读取器前进到下个记录
        /// </summary>
        /// <returns>返回下一条记录</returns>
        public override bool Read()
        {
            return dataReader.Read();
        }

        /// <summary>
        /// 通过执行sql语句获取更改、插入或删除的行数
        /// </summary>
        public override int RecordsAffected
        {
            get { return dataReader.RecordsAffected; }
        }

        /// <summary>
        /// 获取dataReader中名称为传入值的数据
        /// </summary>
        /// <param name="name">名称</param>
        /// <returns>返回对象</returns>
        public override object this[string name]
        {
            get { return dataReader[name]; }
        }

        /// <summary>
        /// 获取dataReader中名称为传入序号的值
        /// </summary>
        /// <param name="ordinal">传入序号</param>
        /// <returns>返回对象</returns>
        public override object this[int ordinal]
        {
            get { return dataReader[ordinal] ;}
        }
    }
}
