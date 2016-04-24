using System;
using System.Data;

namespace Amazon_analyzer.Database
{
    /// <summary>
    /// 参数化数据对象类
    /// </summary>
    [Serializable]
    public class DataParameter : IDataParameter
    {
        /// <summary>
        /// 参数化数据对象
        /// </summary>
        /// <param name="paramName">参数名称</param>
        /// <param name="paramType">参数类型</param>
        /// <param name="paramSize">最大值</param>
        /// <param name="paramValue">实际值</param>
        public DataParameter(string paramName, DbType paramType, int paramSize, object paramValue)
        {
            this.ParameterName = paramName;
            this.DbType = paramType;
            this.Size = paramSize;
            this.Value = paramValue ?? DBNull.Value;
        }

        /// <summary>
        /// 最大值
        /// </summary>
        public int Size
        {
            get;
            set;
        }
        /// <summary>
        /// 类型
        /// </summary>
        public DbType DbType
        {
            get;
            set;
        }
        /// <summary>
        /// 描述
        /// </summary>
        public ParameterDirection Direction
        {
            get;
            set;
        }
        /// <summary>
        /// 是否可空
        /// </summary>
        public bool IsNullable
        {
            get;
            set;
        }
        /// <summary>
        /// 参数名
        /// </summary>
        public string ParameterName
        {
            get;
            set;
        }
        /// <summary>
        /// 源列
        /// </summary>
        public string SourceColumn
        {
            get;
            set;
        }
        /// <summary>
        /// 源版本
        /// </summary>
        public DataRowVersion SourceVersion
        {
            get;
            set;
        }
        /// <summary>
        /// 实际值
        /// </summary>
        public object Value
        {
            get;
            set;
        }

        /// <summary>
        /// 重写格式化为字符串
        /// </summary>
        /// <returns>返回字符串</returns>
		public override string ToString()
		{
			return string.Format("P_Name:{0},P_Value:{1},RealType:{2},DbType:{3},Direction:{4}", this.ParameterName, this.Value, this.Value.GetType().ToString(), this.DbType.ToString(), this.Direction.ToString());
		}
    }
}
