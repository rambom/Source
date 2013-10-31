using System;
using Com.Dcjet.Plugin.Entity;
using Com.Dcjet.Plugin.Helper;
using EnvDTE;
using EnvDTE80;

namespace Com.Dcjet.Plugin.Command
{
    /// <summary>
    /// 赋予smartCode命令默认行为
    /// </summary>
    internal abstract class AbstractCmd : ICommand
    {
        /// <summary>
        /// 当前节点元素
        /// </summary>
        protected CodeDomCodeElement<CodeElement> CurrentCodeElement;
        /// <summary>
        /// Dte工具箱
        /// </summary>
        private DTEHelper _dteHelper;

        public DTEHelper DteHelper
        {
            get { return this._dteHelper; }
            set { this._dteHelper = value; }
        }

        /// <summary>
        /// 获得命令呈现状态
        /// </summary>
        /// <param name="dteHelper"> </param>
        /// <returns></returns>
        public virtual CommandViewStatus GetStatus()
        {
            CommandViewStatus viewStatus = CommandViewStatus.Disabled;

            if (null != _dteHelper.Dte2.ActiveWindow && vsWindowType.vsWindowTypeDocument == _dteHelper.Dte2.ActiveWindow.Type
                && _dteHelper.Dte2.ActiveWindow.Document.FullName.EndsWith(Constant.CSharpFileExtension))//csharp文档窗口
            {
                //当前光标处元素是方法
                viewStatus = CommandViewStatus.Enabled;
            }
            else if (null != _dteHelper.Dte2.ActiveWindow && vsWindowType.vsWindowTypeSolutionExplorer == _dteHelper.Dte2.ActiveWindow.Type && null != CurrentCodeElement)
            {//解决方案窗口
                viewStatus = CommandViewStatus.Enabled;
            }
            return viewStatus;
        }

        /// <summary>
        /// 执行命令
        /// </summary>
        /// <param name="para">自定义参数</param>
        /// <result></result>
        public CommandResult Exec(object para)
        {
            CommandResult cmdResult = new CommandResult();
            cmdResult.ErrCode = CommandExecStatus.Succeed;
            cmdResult.ErrMsg = "";

            try
            {
                BeginExec(para);
                cmdResult = Process(para);
                AfterExec(para);
            }
            catch (Exception ex)
            {
                cmdResult.ErrCode = CommandExecStatus.Error;
                cmdResult.ErrMsg = ex.ToString();
            }
            return cmdResult;
        }

        /// <summary>
        /// 执行前事件
        /// </summary>
        /// <param name="para"></param>
        private void BeginExec(object para)
        {

        }

        /// <summary>
        /// 执行后事件
        /// </summary>
        /// <param name="para"></param>
        private void AfterExec(object para)
        {

        }

        /// <summary>
        /// 调用命令
        /// </summary>
        /// <param name="para"></param>
        /// <returns></returns>
        public abstract CommandResult Process(object para);
    }
}
