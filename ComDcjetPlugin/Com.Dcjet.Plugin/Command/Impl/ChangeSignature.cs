using System.Collections.Generic;
using Com.Dcjet.Plugin.Entity;
using EnvDTE;
using EnvDTE80;
using Microsoft.VisualStudio.CommandBars;

namespace Com.Dcjet.Plugin.Command.Impl
{
    /// <summary>
    /// 修改签名
    /// </summary>
    [Command(
            Key = "ChangeSignature",
            Caption = "Change Signature",
            Tooltip = "adjust signature by current members",
            Position = 2,
            UseMsoButton = true,
            IconResource = 59,
            ControlType = MsoControlType.msoControlButton,
            InitViewStatus = CommandViewStatus.Enabled
        )]
    internal class ChangeSignature : AbstractCmd
    {
        /// <summary>
        /// 当前光标位置
        /// </summary>
        private TextPoint _currentCodePoint;
        /// <summary>
        /// 当前光标文档
        /// </summary>
        private Document _currentCodeDocument;

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
                _currentCodePoint = DteHelper.GetCurrentActivePoint();
                _currentCodeDocument = DteHelper.Dte2.ActiveDocument;
                List<CodeDomCodeElement<CodeElement>> listElement = DteHelper.FetchCodeElementByPoint(_currentCodeDocument.ProjectItem.FileCodeModel, _currentCodePoint);
                this.CurrentCodeElement = DteHelper.FindPointElement(listElement, _currentCodePoint);
            }
            else if (vsWindowType.vsWindowTypeSolutionExplorer == DteHelper.Dte2.ActiveWindow.Type && CurrentCodeElement != null)
            {
                //取得所有选中文件
                IList<ProjectItem> listItem = DteHelper.GetSelectedItem(Constant.CSharpFileExtension);

                List<CodeDomCodeElement<CodeElement>> listElement = DteHelper.FetchCodeElementByPoint(_currentCodeDocument.ProjectItem.FileCodeModel, _currentCodePoint);
                CodeDomCodeElement<CodeElement> codeElement = DteHelper.FindPointElement(listElement, _currentCodePoint);

                foreach (var projectItem in listItem)
                {
                    foreach (var codeClass in DteHelper.GetClassFromFile(projectItem.FileCodeModel))
                    {//重构元素
                        DteHelper.RefactorElement(this.CurrentCodeElement, codeElement, codeClass);
                    }
                }
            }
            return result;
        }
    }
}
