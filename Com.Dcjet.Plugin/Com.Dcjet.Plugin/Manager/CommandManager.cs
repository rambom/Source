using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Com.Dcjet.Plugin.Command;
using Com.Dcjet.Plugin.Command.Impl;

namespace Com.Dcjet.Plugin.Manager
{
    /// <summary>
    /// 命令管理器
    /// </summary>
    internal class CommandManager
    {
        /// <summary>
        /// 获得所有需要注册的命令
        /// </summary>
        /// <returns></returns>
        public static IList<ICommand> GetAllCommands()
        {
            return new List<ICommand>(3)
                       {
                           new BuildDeclaration(),
                           new ChangeSignature(),
                           new DeleteItem()
                       };
        }
    }
}
