using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Reflection;
using System.Resources;
using Com.Dcjet.Plugin.Entity;
using EnvDTE;
using EnvDTE80;
using Microsoft.VisualStudio.CommandBars;

namespace Com.Dcjet.Plugin.Helper
{
    /// <summary>
    /// 
    /// </summary>
    internal class DTEHelper
    {
        private DTE2 _dte;
        private EnvDTE.AddIn _addin;

        public DTE2 Dte2
        {
            get { return _dte; }
        }

        public EnvDTE.AddIn Addin
        {
            get { return _addin; }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="dte">VS宿主</param>
        /// <param name="addin">插件实例</param>
        public DTEHelper(DTE2 dte, EnvDTE.AddIn addin)
        {
            this._dte = dte;
            this._addin = addin;
        }


        /// <summary>
        /// 返回特定区域菜单名称
        /// </summary>
        /// <param name="sourceName"></param>
        /// <returns></returns>
        public string GetCulturedMenuName(string sourceName)
        {
            try
            {
                string resourceName;
                ResourceManager resourceManager = new ResourceManager("SmartCode.CommandBar",
                                                                      Assembly.GetExecutingAssembly());
                CultureInfo cultureInfo = new CultureInfo(_dte.LocaleID);

                if (cultureInfo.TwoLetterISOLanguageName == "zh")
                {
                    System.Globalization.CultureInfo parentCultureInfo = cultureInfo.Parent;
                    resourceName = String.Concat(parentCultureInfo.Name, sourceName);
                }
                else
                {
                    resourceName = String.Concat(cultureInfo.TwoLetterISOLanguageName, sourceName);
                }

                return resourceManager.GetString(resourceName);
            }
            catch
            {
            }

            return sourceName;
        }

        /// <summary>
        /// 得到指定的命令栏菜单
        /// </summary>
        /// <param name="cmdBarName">命令工具栏名称</param>
        /// <returns></returns>
        public CommandBar GetCommandBarByName(string cmdBarName)
        {
            return ((CommandBars)_dte.CommandBars)[cmdBarName];
        }

        /// <summary>
        /// 注册命令
        /// </summary>
        /// <param name="cmdName">命令名,唯一,不能有空字符</param>
        /// <param name="buttonText">命令显示文字</param>
        /// <param name="toolTip">命令提示</param>
        /// <param name="useMsoButton">使用系统预定义图标</param>
        /// <param name="iconIndex">图标索引</param>
        /// <param name="position">添加命令位置</param>
        /// <param name="cmdStatus">按钮初始状态 </param>
        public EnvDTE.Command RegisterCommand(string cmdName, string buttonText, string toolTip,
                                              bool useMsoButton, int iconIndex, vsCommandStatus cmdStatus)
        {

            Commands2 commands = (Commands2)_dte.Commands;
            object[] contextGUIDS = new object[] { };

            // Add command
            EnvDTE.Command command = commands.AddNamedCommand2(_addin, cmdName, buttonText, toolTip,
                                                               useMsoButton, iconIndex, ref contextGUIDS,
                                                               (int)cmdStatus,
                                                               (int)vsCommandStyle.vsCommandStylePictAndText,
                                                               vsCommandControlType.vsCommandControlTypeButton);

            if (command == null)
            {
                throw new ArgumentNullException();
            }

            return command;
        }

        /// <summary>
        /// 添加按钮至菜单栏
        /// </summary>
        /// <param name="cmdBar">要添加按钮的命令栏</param>
        /// <param name="beforeIndex">插入的位置</param>
        /// <param name="caption">按钮显示名称</param>
        /// <param name="tooltip">按钮提示</param>
        /// <returns></returns>
        public CommandBarButton AddButtonToCmdBar(CommandBar cmdBar, int beforeIndex, string caption, string tooltip)
        {
            if (cmdBar.Controls.Count < beforeIndex)
                beforeIndex = cmdBar.Controls.Count;
            if (beforeIndex == 0)
                beforeIndex = 1;
            CommandBarButton button = cmdBar.Controls.Add(MsoControlType.msoControlButton,
                                                          Type.Missing, Type.Missing, beforeIndex, true) as
                                      CommandBarButton;
            button.Caption = caption;
            button.TooltipText = tooltip;

            return button;
        }

        /// <summary>
        /// 添加按钮至弹出式菜单栏
        /// </summary>
        /// <param name="popup">要添加按钮的命令栏</param>
        /// <param name="beforeIndex">插入的位置</param>
        /// <param name="caption">按钮显示名称</param>
        /// <param name="tooltip">按钮提示</param>
        /// <returns></returns>
        public CommandBarButton AddButtonToPopup(CommandBarPopup popup, int beforeIndex, string caption, string tooltip)
        {
            if (popup.Controls.Count < beforeIndex)
                beforeIndex = popup.Controls.Count;

            if (beforeIndex == 0)
                beforeIndex = 1;
            CommandBarButton button = popup.Controls.Add(MsoControlType.msoControlButton,
                                                         Type.Missing, Type.Missing, beforeIndex, true) as
                                      CommandBarButton;
            button.Caption = caption;
            button.TooltipText = tooltip;

            return button;
        }

        /// <summary>
        /// 获取选中文本
        /// </summary>
        /// <returns></returns>
        public string GetSelectedText()
        {
            TextSelection selectedText = _dte.ActiveDocument.Selection as TextSelection;
            return selectedText.Text;
        }

        /// <summary>
        /// 获取选中行
        /// </summary>
        /// <returns></returns>
        public string GetSelectedLines()
        {
            TextSelection selectedText = _dte.ActiveDocument.Selection as TextSelection;
            TextPoint topPoint = selectedText.TopPoint;
            EditPoint bottomPoint = selectedText.BottomPoint.CreateEditPoint();

            return bottomPoint.GetLines(topPoint.Line, bottomPoint.Line + 1);
        }

        /// <summary>
        /// 返回插件程序集路径
        /// </summary>
        /// <returns></returns>
        public string GetAddinAssemblyLocation()
        {
            Assembly asm = Assembly.GetEntryAssembly();
            return asm.Location;
        }

        /// <summary>
        /// 返回解决方案项目
        /// </summary>
        /// <param name="solution"></param>
        /// <returns></returns>
        public List<UIHierarchyItem> GetProjectNodes(Solution solution)
        {
            string solutionName = solution.Properties.Item("Name").Value.ToString();
            return GetProjectNodes(_dte.ToolWindows.SolutionExplorer.GetItem(solutionName).UIHierarchyItems);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="topLevelItems"></param>
        /// <returns></returns>
        private List<UIHierarchyItem> GetProjectNodes(UIHierarchyItems topLevelItems)
        {
            List<UIHierarchyItem> projects = new List<UIHierarchyItem>();
            foreach (UIHierarchyItem item in topLevelItems)
            {
                if (IsProjectNode(item))
                {
                    projects.Add(item);
                }
                else if (IsSolutionFolder(item))
                {
                    projects.AddRange(GetProjectNodesInSolutionFolder(item));
                }
            }

            return projects;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="item"></param>
        /// <returns></returns>
        private List<UIHierarchyItem> GetProjectNodesInSolutionFolder(UIHierarchyItem item)
        {
            List<UIHierarchyItem> projects = new List<UIHierarchyItem>();

            if (IsSolutionFolder(item))
            {
                foreach (UIHierarchyItem subItem in item.UIHierarchyItems)
                {
                    if (IsProjectNode(subItem))
                    {
                        projects.Add(subItem);
                    }
                }
            }

            return projects;
        }

        private bool IsSolutionFolder(UIHierarchyItem item)
        {
            return ((item.Object is Project) &&
                    ((item.Object as Project).Kind == ProjectKinds.vsProjectKindSolutionFolder));
        }

        private bool IsProjectNode(UIHierarchyItem item)
        {
            return IsDirectProjectNode(item) || IsProjectNodeInSolutionFolder(item);
        }

        private bool IsDirectProjectNode(UIHierarchyItem item)
        {
            return ((item.Object is Project) &&
                    ((item.Object as Project).Kind != ProjectKinds.vsProjectKindSolutionFolder));
        }

        private bool IsProjectNodeInSolutionFolder(UIHierarchyItem item)
        {
            return (item.Object is ProjectItem && ((ProjectItem)item.Object).Object is Project &&
                    ((Project)((ProjectItem)item.Object).Object).Kind != ProjectKinds.vsProjectKindSolutionFolder);
        }

        /// <summary>
        /// 选择当前行
        /// </summary>
        public void SelectLine()
        {
            try
            {
                // Retrieve document selection
                TextSelection sel = (TextSelection)_dte.ActiveWindow.Document.Selection;

                // Move to line
                sel.MoveToDisplayColumn(sel.CurrentLine, 1, false);

                // Select from start to end
                sel.StartOfLine(vsStartOfLineOptions.vsStartOfLineOptionsFirstText, false);
                sel.EndOfLine(true);

            }
            catch (ArgumentException)
            {
            }
        }

        /// <summary>
        /// 当前光标处是方法
        /// </summary>
        /// <returns></returns>
        public bool IsMethodOfCurrenActivePoint()
        {
            TextPoint textPoint = this.GetCurrentActivePoint();
            List<CodeDomCodeElement<CodeElement>> listElement = FetchCodeElementByPoint(_dte.ActiveDocument.ProjectItem.FileCodeModel, textPoint);
            CodeDomCodeElement<CodeElement> codeElement = FindPointElement(listElement, textPoint);
            if (null != codeElement && codeElement.CodeElement is CodeFunction)
            {
                if (textPoint.Line == codeElement.StartPoint.Line)
                {
                    //在方法的定义行
                    return true;
                }
            }
            return false;
        }

        /// <summary>
        /// 当前光标处是属性
        /// </summary>
        /// <returns></returns>
        public bool IsPropertyOfCurrenActivePoint()
        {
            TextPoint textPoint = this.GetCurrentActivePoint();
            List<CodeDomCodeElement<CodeElement>> listElement = FetchCodeElementByPoint(_dte.ActiveDocument.ProjectItem.FileCodeModel, textPoint);
            CodeDomCodeElement<CodeElement> codeElement = FindPointElement(listElement, textPoint);
            if (null != codeElement && codeElement.CodeElement is CodeProperty)
            {
                return true;
            }
            return false;
        }

        /// <summary>
        /// 返回当前光标位置
        /// </summary>
        /// <returns></returns>
        public TextPoint GetCurrentActivePoint()
        {
            TextSelection textSelection = _dte.ActiveWindow.Document.Selection as TextSelection;
            return textSelection.ActivePoint;

        }

        /// <summary>
        /// 返回光标处元素
        /// </summary>
        /// <param name="listElement">区域集合</param>
        /// <param name="cPoint">光标位置</param>
        /// <returns></returns>
        public CodeDomCodeElement<CodeElement> FindPointElement(List<CodeDomCodeElement<CodeElement>> listElement, TextPoint cPoint)
        {
            if (listElement.Count == 1)
            {
                return listElement[0];
            }

            foreach (CodeDomCodeElement<CodeElement> codeDomCodeElement in listElement)
            {
                if (codeDomCodeElement.StartPoint.Line >= cPoint.Line && codeDomCodeElement.EndPoint.Line >= cPoint.Line)
                    return codeDomCodeElement;
            }
            return null;
        }

        /// <summary>
        /// 返回光标处元素所属区域集合
        /// </summary>
        /// <returns></returns>
        public List<CodeDomCodeElement<CodeElement>> FetchCodeElementByPoint(FileCodeModel fileCodeModel, TextPoint textPoint)
        {
            List<CodeDomCodeElement<CodeElement>> codeElement = new List<CodeDomCodeElement<CodeElement>>();
            try
            {
                foreach (vsCMElement scope in Enum.GetValues(typeof(vsCMElement)))
                {
                    if (scope != vsCMElement.vsCMElementOther)
                    {
                        codeElement.Add(new CodeDomCodeElement<CodeElement>(fileCodeModel.CodeElementFromPoint(textPoint, scope)));

                    }
                }
            }
            catch
            {
            }
            return codeElement;
        }

        /// <summary>
        /// 取得当前选中项文件
        /// </summary>
        /// <param name="extensionFilter">按扩展名过滤</param>
        /// <returns></returns>
        public IList<ProjectItem> GetSelectedItem(string extensionFilter)
        {
            IList<ProjectItem> listItem = new List<ProjectItem>();

            foreach (SelectedItem seleItem in Dte2.SelectedItems)
            {

                foreach (var projectItem in GetProjectItem(seleItem.ProjectItem, extensionFilter))
                {
                    listItem.Add(projectItem);
                }
            }
            return listItem;
        }

        /// <summary>
        /// 获取项目所有子项
        /// </summary>
        /// <param name="item"></param>
        /// <param name="extensionFilter">按扩展名过滤</param>
        /// <returns></returns>
        public IList<ProjectItem> GetProjectItem(ProjectItem item, string extensionFilter)
        {
            IList<ProjectItem> listItem = new List<ProjectItem>();
            if (item.FileCodeModel == null && item.ProjectItems.Count > 0)
            {
                foreach (ProjectItem projectItem in item.ProjectItems)
                {
                    foreach (var s in GetProjectItem(projectItem, extensionFilter))
                    {
                        listItem.Add(s);
                    }
                }
            }
            else
            {
                if (item.Name.EndsWith(extensionFilter))
                {
                    listItem.Add(item);
                }
            }

            return listItem;
        }

        /// <summary>
        /// 获取文件中的所有类
        /// </summary>
        /// <param name="fileCodeModel"></param>
        /// <returns></returns>
        public IList<CodeClass> GetClassFromFile(FileCodeModel fileCodeModel)
        {
            IList<CodeClass> listClass = new List<CodeClass>();
            for (int i = fileCodeModel.CodeElements.Count; i > 0; i--)
            {
                CodeNamespace codeNamespace = fileCodeModel.CodeElements.Item(i) as CodeNamespace;
                if (null != codeNamespace)
                {
                    foreach (CodeElement ce in codeNamespace.Members)
                    {
                        CodeClass codeClass = ce as CodeClass;
                        if (null != codeClass)
                            listClass.Add(codeClass);
                    }
                    break;
                }
            }
            return listClass;
        }

        /// <summary>
        /// 克隆代码元素
        /// </summary>
        /// <param name="codeElement">要克隆的元素</param>
        /// <param name="codeClass">目标类</param>
        public void CloneCodeElement(CodeElement codeElement, CodeClass codeClass)
        {
            switch (codeElement.Kind)
            {
                case vsCMElement.vsCMElementFunction:
                    CloneFunction((CodeFunction)codeElement, codeClass);
                    break;
                default:
                    break;
            }

        }


        /// <summary>
        /// 克隆一个方法
        /// </summary>
        /// <param name="cloneFunction">要克隆的方法</param>
        /// <param name="codeClass">添加到类中</param>
        private void CloneFunction(CodeFunction cloneFunction, CodeClass codeClass)
        {
            CodeFunction codeFunction = codeClass.AddFunction(cloneFunction.Name, cloneFunction.FunctionKind,
                                                              cloneFunction.Type, -1, cloneFunction.Access, null);

            //添加参数
            for (int i = 1; i <= cloneFunction.Parameters.Count; i++)
            {
                CodeParameter2 parameter = cloneFunction.Parameters.Item(i) as CodeParameter2;
                CodeParameter2 cloneParameter = codeFunction.AddParameter(parameter.FullName, parameter.Type.AsFullName, i) as CodeParameter2;
                cloneParameter.DefaultValue = parameter.DefaultValue;
                cloneParameter.ParameterKind = parameter.ParameterKind;
            }

            //添加属性
            for (int i = 1; i <= cloneFunction.Attributes.Count; i++)
            {
                CodeAttribute attribute = cloneFunction.Attributes.Item(i) as CodeAttribute;
                codeFunction.AddAttribute(attribute.Name, attribute.Value, i);
            }

            //方法注释
            codeFunction.Comment = cloneFunction.Comment;
            //方法说明
            codeFunction.DocComment = cloneFunction.DocComment;
            //静态修饰
            codeFunction.IsShared = cloneFunction.IsShared;
            //抽象修饰
            codeFunction.MustImplement = cloneFunction.MustImplement;
            //重载修饰
            codeFunction.CanOverride = cloneFunction.CanOverride;
        }

        /// <summary>
        /// 删除代码元素
        /// </summary>
        /// <param name="codeElement">要删除的元素</param>
        /// <param name="codeClass">操作的类</param>
        public void DeleteCodeElement(CodeDomCodeElement<CodeElement> codeElement, CodeClass codeClass)
        {
            foreach (CodeElement ce in codeClass.Members)
            {
                if (ce.Kind == codeElement.Kind && ce.Name == codeElement.Name)
                {//仅比较类型，名称，不比较声明，修饰
                    try
                    {
                        codeClass.RemoveMember(ce);
                    }
                    catch
                    {
                    }
                }
            }
        }

        /// <summary>
        /// 重构元素
        /// </summary>
        /// <param name="oldElement">老元素</param>
        /// <param name="newElement">新元素</param>
        /// <param name="codeClass">操作的类</param>
        public void RefactorElement(CodeDomCodeElement<CodeElement> oldElement, CodeDomCodeElement<CodeElement> newElement, CodeClass codeClass)
        {
            if (newElement.Kind == oldElement.Kind)
            {
                foreach (CodeElement ce in codeClass.Members)
                {
                    if (ce.Kind == oldElement.Kind && ce.Name == oldElement.Name)
                    {
                        switch (oldElement.Kind)
                        {
                            case vsCMElement.vsCMElementFunction:
                                this.RefactorMethod((CodeFunction)ce, (CodeFunction)newElement.CodeElement);//重构方法
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }

        /// <summary>
        /// 重构方法
        /// </summary>
        /// <param name="replacedFunction"></param>
        /// <param name="newFunction"></param>
        private void RefactorMethod(CodeFunction replacedFunction, CodeFunction newFunction)
        {
            //清除方法参数
            while (replacedFunction.Parameters.Count > 0)
            {
                replacedFunction.RemoveParameter(replacedFunction.Parameters.Count);
            }

            //清除方法属性
            while (replacedFunction.Attributes.Count > 0)
            {
                ((CodeAttribute)replacedFunction.Attributes.Item(replacedFunction.Attributes.Count)).Delete();
            }

            //添加参数
            for (int i = 1; i <= newFunction.Parameters.Count; i++)
            {
                CodeParameter2 parameter = newFunction.Parameters.Item(i) as CodeParameter2;
                CodeParameter2 cloneParameter = replacedFunction.AddParameter(parameter.FullName, parameter.Type.AsFullName, i) as CodeParameter2;
                cloneParameter.DefaultValue = parameter.DefaultValue;
                cloneParameter.ParameterKind = parameter.ParameterKind;
            }

            //添加属性
            for (int i = 1; i <= newFunction.Attributes.Count; i++)
            {
                CodeAttribute2 attribute = newFunction.Attributes.Item(i) as CodeAttribute2;
                replacedFunction.AddAttribute(attribute.Name, attribute.Value, i);
            }

            //方法名
            replacedFunction.Name = newFunction.Name;
            //方法注释
            replacedFunction.Comment = newFunction.Comment;
            //方法说明
            replacedFunction.DocComment = newFunction.DocComment;
            //静态修饰
            replacedFunction.IsShared = newFunction.IsShared;
            //抽象修饰
            replacedFunction.MustImplement = newFunction.MustImplement;
            //重载修饰
            replacedFunction.CanOverride = newFunction.CanOverride;
        }
    }
}
