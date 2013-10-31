using System.Collections.Generic;
using Com.Dcjet.Plugin.Command;
using Com.Dcjet.Plugin.Command.Impl;
using Com.Dcjet.Plugin.Entity;
using Microsoft.VisualStudio.CommandBars;

namespace Com.Dcjet.Plugin.Menu.Impl
{
    /// <summary>
    /// SmartCode菜单
    /// </summary>
    [Menu(Key = "SmartCode",
           Caption = "Smart Code",
           ControlType = MsoControlType.msoControlPopup,
           Position = -1,
           Tooltip = ""
     )]
    [AdditionalBar("Code Window", "工作区代码窗口")]
    [AdditionalBar("Folder", "解决方案文件夹右键菜单")]
    [AdditionalBar("Item", "解决方案文件右键菜单")]
    internal class SmartCodeBar : IMenuBar
    {
        /// <summary>
        /// 菜单命令集合
        /// </summary>
        private readonly IList<ICommand> _commandList = new List<ICommand>()
                                                   {
                                                       new BuildDeclaration(), 
                                                       new ChangeSignature(),
                                                       new DeleteItem()
                                                   };

        /// <summary>
        /// 菜单命令集合
        /// </summary>
        public IList<ICommand> CommandList
        {
            get { return _commandList; }
        }
    }
}
