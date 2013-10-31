namespace Com.Dcjet.Plugin.Entity
{
    /// <summary>
    /// 命令执行结果
    /// </summary>
    internal class CommandResult
    {
        /// <summary>
        /// 错误代码
        /// </summary>
        public CommandExecStatus ErrCode { get; set; }
        /// <summary>
        /// 错误消息
        /// </summary>
        public string ErrMsg { get; set; }
    }
}
