using System.Collections.Generic;
using Com.Dcjet.Plugin.Entity;
using EnvDTE;
using EnvDTE80;
using Microsoft.VisualStudio.CommandBars;

namespace Com.Dcjet.Plugin.Command.Impl
{
    /// <summary>
    /// 删除声明命令
    /// </summary>
    [Command(
            Key = "DeleteItem",
            Caption = "Delete Item",
            Tooltip = "delete current item",
            Position = 3,
            UseMsoButton = true,
            IconResource = 59,
            ControlType = MsoControlType.msoControlButton,
            InitViewStatus = CommandViewStatus.Enabled
        )]
    internal class DeleteItem : AbstractCmd
    {
        /// <summary>
        /// 执行命令
        /// </summary>
        /// <param name="para">自定义参数</param>
        /// <result></result>
        public override CommandResult Process(object para)
        {
            CommandResult result = new CommandResult();
            result.ErrCode = CommandExecStatus.Succeed;
            result.ErrMsg = para.ToString();
            if (vsWindowType.vsWindowTypeDocument == DteHelper.Dte2.ActiveWindow.Type)
            {
                List<CodeDomCodeElement<CodeElement>> listElement = DteHelper.FetchCodeElementByPoint(DteHelper.Dte2.ActiveDocument.ProjectItem.FileCodeModel, DteHelper.GetCurrentActivePoint());
                this.CurrentCodeElement = DteHelper.FindPointElement(listElement, DteHelper.GetCurrentActivePoint());
                this.CurrentCodeElement.ProjectItem.FileCodeModel.Remove(this.CurrentCodeElement.CodeElement);
            }
            else if (vsWindowType.vsWindowTypeSolutionExplorer == DteHelper.Dte2.ActiveWindow.Type && CurrentCodeElement != null)
            {
                //取得所有选中文件
                IList<ProjectItem> listItem = DteHelper.GetSelectedItem(Constant.CSharpFileExtension);

                foreach (var projectItem in listItem)
                {
                    foreach (var codeClass in DteHelper.GetClassFromFile(projectItem.FileCodeModel))
                    {//删除所有元素
                        DteHelper.DeleteCodeElement(this.CurrentCodeElement, codeClass);
                    }
                }
            }
            return result;
        }
    }
}
