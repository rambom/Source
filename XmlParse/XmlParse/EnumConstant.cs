namespace XmlParse
{
    public enum RunFlag
    {
        /// <summary>
        /// 插入
        /// </summary>
        Insert = 'I',
        
        /// <summary>
        /// 删除
        /// </summary>
        Erase = 'D',

        /// <summary>
        /// 更新
        /// </summary>
        Update = 'U'
    }

    public enum OperateFlag
    {
        /// <summary>
        /// 属性操作
        /// </summary>
        Attribute = 'P',

        /// <summary>
        /// 节点操作
        /// </summary>
        Node = 'N'
    }
}