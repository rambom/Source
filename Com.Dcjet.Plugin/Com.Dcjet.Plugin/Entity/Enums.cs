using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Com.Dcjet.Plugin.Entity
{
    /// <summary>
    /// 命令状态
    /// </summary>
    enum CommandViewStatus
    {
        /// <summary>
        /// 启用
        /// </summary>
        Enabled,
        /// <summary>
        /// 停用
        /// </summary>
        Disabled,
        /// <summary>
        /// 显示
        /// </summary>
        Visible,
        /// <summary>
        /// 不显示
        /// </summary>
        Invisible
    }

    /// <summary>
    /// 命令执行状态
    /// </summary>
    enum CommandExecStatus
    {
        /// <summary>
        /// 执行成功
        /// </summary>
        Succeed,
        /// <summary>
        /// 执行失败
        /// </summary>
        Failed,
        /// <summary>
        /// 执行出错
        /// </summary>
        Error,
    }
}
