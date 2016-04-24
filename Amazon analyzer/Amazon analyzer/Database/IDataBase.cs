using System;
using System.Data;
using System.Collections.Generic;
using System.Data.Common;

namespace Amazon_analyzer.Database
{
    /// <summary>
    /// 数据访问接口
    /// </summary>
    public interface IDataBase : IDisposable
    {
        /// <summary>
        /// 获取参数化绑定前缀
        /// </summary>
        /// <returns></returns>
        string GetDbPrefix();
        /// <summary>
        /// 获取数据库拼接字符 SQL+ ORACLE||
        /// </summary>
        /// <returns></returns>
        string GetDbJoinString();

        /// <summary>
        /// 事务处理
        /// </summary>
        void OpenTrans();

        /// <summary>
        /// 事务提交
        /// </summary>
        void CommitTrans();

        /// <summary>
        /// 事务回滚
        /// </summary>
        void RollbackTrans();

        /// <summary>
        /// 关闭
        /// </summary>
        void CloseConn();

        /// <summary>
        /// 执行非查询的SQL语句
        /// </summary>
        /// <param name="strSql">SQL语句</param>
        /// <param name="arrParam">参数对象</param>
        /// <returns>受影响的行数</returns>
        int ExecuteNoQuery(string strSql, List<DataParameter> arrParam);

        /// <summary>
        /// 执行查询语句返回DataTable
        /// </summary>
        /// <param name="strSql">SQL语句</param>
        /// <param name="arrParam">参数集合</param>
        /// <returns></returns>
        DataTable ExecuteDataTable(string strSql, List<DataParameter> arrParam);

        /// <summary>
        /// 执行查询语句返回DataSet
        /// </summary>
        /// <param name="strSql">SQL语句</param>
        /// <param name="arrParam">参数集合</param>
        /// <returns>DataSet</returns>
        DataSet ExecuteDataSet(string strSql, List<DataParameter> arrParam);

        /// <summary>
        /// 执行查询语句，获取第一行第一列的值
        /// </summary>
        /// <param name="strSql">SQL查询语句</param>
        /// <param name="arrParam">参数对象</param>
        /// <returns>查询结果</returns>
        dynamic ExecuteScalar(string strSql, List<DataParameter> arrParam);

        /// <summary>
        /// 执行SQL语句返回DataReader
        /// </summary>
        /// <param name="strSql">SQL查询语句</param>
        /// <param name="arrParam">参数对象</param>
        /// <returns>查询结果</returns>
        DbDataReader ExecuteDataReader(string strSql, List<DataParameter> arrParam);

        /// <summary>
        /// 将datatable数据集导入具体哪几行到数据库
        /// </summary>
        /// <param name="dtImport">数据集</param>
        /// <param name="strSql">sql查询语句</param>
        /// <param name="intStartRow">起始行</param>
        void WriteDataTableToDB(DataTable dtImport, string strSql, int intStartRow);

        /// <summary>
        /// 以Adapter方式导入Datatable映射的列到数据库表
        /// </summary>
        /// <param name="dataTable">要导入的dataTable</param>
        /// <param name="tableName">目标表名</param>
        /// <param name="columnMapping">列映射关系(第1列为源列,第2列为目标列)</param>
        /// <returns>成功返回success，否则为返回错误消息</returns>
        string ImportDatatableToDB(DataTable dataTable, string tableName, DataRow[] columnMapping);

        /// <summary>
        /// 快速导入dataTable映射的列至数据库表(目标列名区分大小写)
        /// </summary>
        /// <param name="dataTable">要导入的dataTable</param>
        /// <param name="tableName">目标表名</param>
        /// <param name="columnMapping">列映射关系(第1列为源列,第2列为目标列)</param>
        /// <returns>成功返回success，否则为返回错误消息</returns>
        string FastImportDatatableToDB(DataTable dataTable, string tableName, DataRow[] columnMapping);

        /// <summary>
        /// 快速导入dataTable映射的列名称至数据库表(目标列名区分大小写)
        /// </summary>
        /// <param name="dataTable">要导入的dataTable</param>
        /// <param name="tableName">目标表名</param>
        /// <param name="arrStrTarGetColumns">目标列名</param>
        /// <returns>成功返回success，否则为返回错误消息</returns>
        string FastImportDatatableToDB(DataTable dataTable, string tableName, string[] arrStrTarGetColumns);

        /// <summary>
        /// 快速导入dataTable映射的列名称至数据库表(目标列名区分大小写)
        /// </summary>
        /// <param name="dataTable">要导入的dataTable</param>
        /// <param name="tableName">目标表名</param>
        /// <param name="arrStrTarGetColumns">目标列名</param>
        /// <returns>成功返回success，否则为返回错误消息</returns>
        string FastImportDatatableToDBNotClose(DataTable dataTable, string tableName, string[] arrStrTarGetColumns);
       
        /// <summary>
        /// 快速DataReader映射的列至数据库表，并设置最大行数和每次提交行数，计算执行的时间
        /// </summary>
        /// <param name="strConnectionString">目的数据库连接字符串</param>
        /// <param name="intTimeOut">导入最大超时时间</param>
        /// <param name="dataReader">要导入的DataReader</param>
        /// <param name="strTableName">目标表名</param>
        /// <param name="columnMapping">列映射关系(目标列,源列)</param>
        /// <param name="intBatchSize">每次导入多少条后提交</param>
        /// <param name="intCommitRowCount">导入成功行数</param>
        /// <param name="spentTime">消耗时间</param>
        /// <param name="strErrMsg">错误消息</param>
        /// <returns>执行结果</returns>
        bool FastImportDataReaderToDb(string strConnectionString, int intBatchSize, int intTimeOut, IDataReader dataReader,
                                      string strTableName,
                                      Dictionary<string, string> columnMapping, out int intCommitRowCount, out long spentTime, out string strErrMsg);

        /// <summary>
        /// 快速导入从第几行开始的文本文件映射的列至数据库表。并设置最大行数和每次提交行数，计算执行的时间。
        /// </summary>
        /// <param name="strConnectionString">目的数据库连接字符串</param>
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
        /// <returns>执行结果</returns>
        bool FastImportTextToDb(string strConnectionString, int intBatchSize, int intTimeOut, string strTxtFile,
                                string strHead, string strDelimited, string strTableName,
                                Dictionary<string, string> columnMapping, Dictionary<string, object> extraMapping, uint intStartRow, string strRowNumberColumn,
                                out int intCommitRowCount, out long lSpentTime, out string strErrMsg);

        /// <summary>
        /// 执行存储过程返回DataSet
        /// </summary>
        /// <param name="proName">存储过程</param>
        /// <param name="arrParam">参数数组</param>
        /// <returns>DataSet</returns>
        DataSet ExecuteDataSetSP(string proName, List<DataParameter> arrParam);

        /// <summary>
        /// 执行存储过程返回Scalar
        /// </summary>
        /// <param name="proName">存储过程</param>
        /// <param name="arrParam">参数数组</param>
        /// <returns>Scalar</returns>
        dynamic ExecuteScalarSP(string proName, List<DataParameter> arrParam);

        /// <summary>
        /// 执行存储过程返回输出参数
        /// </summary>
        /// <param name="proName">存储过程</param>
        /// <param name="arrParam">参数数组</param>
        /// <returns>输出参数的值</returns>
        string ExecuteStringSP(string proName, List<DataParameter> arrParam);

        /// <summary>
        /// 执行存储过程无返回值
        /// </summary>
        /// <param name="proName">存储过程</param>
        /// <param name="arrParam">参数数组</param>
        void ExecuteNoQuerySP(string proName, List<DataParameter> arrParam);

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
        /// <returns>返回分页结果集</returns>
        DataTable ExecuteDataTablePaged(string pagedTableName, string pagedSelectColumns, string pagedPrimaryKey, string whereCondition, List<DataParameter> arrParam, string pagedOrderColumns, int lowIndex, int topIndex);
        /// <summary>
        /// 查询获取分页总记录数
        /// </summary>
        /// <param name="pagedTableName">表名</param>
        /// <param name="whereCondition">条件</param>
        /// <param name="arrParam">条件参数</param>
        /// <returns>返回分页总记录数</returns>
        DataTable ExecuteDataTablePaged(string pagedTableName, string whereCondition, List<DataParameter> arrParam);
    }
}
