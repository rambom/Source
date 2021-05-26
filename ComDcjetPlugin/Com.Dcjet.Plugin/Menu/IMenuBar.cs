using System.Collections.Generic;
using Com.Dcjet.Plugin.Command;
using Com.Dcjet.Plugin.Entity;

namespace Com.Dcjet.Plugin.Menu
{
    internal interface IMenuBar
    {
        /// <summary>
        /// 菜单命令集合
        /// </summary>
        IList<ICommand> CommandList { get; }
    }
}
