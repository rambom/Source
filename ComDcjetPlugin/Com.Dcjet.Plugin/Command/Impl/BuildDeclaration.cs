using System;
using System.CodeDom;
using System.Collections.Generic;
using Com.Dcjet.Plugin.Entity;
using EnvDTE;
using Microsoft.VisualStudio.CommandBars;

namespace Com.Dcjet.Plugin.Command.Impl
{
    /// <summary>
    /// 生成声明
    /// </summary>
    [Command(
            Key = "BuildDeclaration",
            Caption = "Build Declaration",
            Tooltip = "copy current members to declare",
            Position = 1,
            UseMsoButton = true,
            IconResource = 59,
            ControlType = MsoControlType.msoControlButton,
            InitViewStatus = CommandViewStatus.Enabled
        )]
    internal class BuildDeclaration : AbstractCmd
    {
        /// <summary>
        /// 调用命令
        /// </summary>
        /// <param name="para"></param>
        /// <returns></returns>
        public override CommandResult Process(object para)
        {
            CommandResult result = new CommandResult();
            result.ErrCode = CommandExecStatus.Succeed;
            if (vsWindowType.vsWindowTypeDocument == DteHelper.Dte2.ActiveWindow.Type)
            {
                List<CodeDomCodeElement<CodeElement>> listElement = DteHelper.FetchCodeElementByPoint(DteHelper.Dte2.ActiveDocument.ProjectItem.FileCodeModel, DteHelper.GetCurrentActivePoint());
                this.CurrentCodeElement = DteHelper.FindPointElement(listElement, DteHelper.GetCurrentActivePoint());
            }
            else if (vsWindowType.vsWindowTypeSolutionExplorer == DteHelper.Dte2.ActiveWindow.Type && CurrentCodeElement != null)
            {
                //取得所有选中文件
                IList<ProjectItem> listItem = DteHelper.GetSelectedItem(Constant.CSharpFileExtension);

                foreach (var projectItem in listItem)
                {
                    foreach (var codeClass in DteHelper.GetClassFromFile(projectItem.FileCodeModel))
                    {//克隆元素
                        DteHelper.CloneCodeElement(CurrentCodeElement.CodeElement, codeClass);
                    }
                }
            }
            return result;
        }
    }
}
