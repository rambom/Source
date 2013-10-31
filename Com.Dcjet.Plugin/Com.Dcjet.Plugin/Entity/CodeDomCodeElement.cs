using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using EnvDTE;

namespace Com.Dcjet.Plugin.Entity
{
    /// <summary>
    /// 简单克隆的CodeElement元素
    /// </summary>
    internal class CodeDomCodeElement<T> : CodeElement where T : CodeElement
    {
        private CodeElements _children;
        private CodeElements _collection;
        private DTE _dte;
        private TextPoint _endPoint;
        private string _fullName;
        private vsCMInfoLocation _infoLocation;
        private bool _isCodeType;
        private vsCMElement _kind;
        private string _language;
        private string _name;
        private ProjectItem _projectItem;
        private TextPoint _startPoint;
        /// <summary>
        /// 原对象
        /// </summary>
        private T _codeElement;

        public CodeDomCodeElement(T codeElement)
        {
            this._children = codeElement.Children;
            this._collection = codeElement.Collection;
            this._dte = codeElement.DTE;
            this._endPoint = codeElement.EndPoint;
            this._fullName = codeElement.FullName;
            this._infoLocation = codeElement.InfoLocation;
            this._isCodeType = codeElement.IsCodeType;
            this._kind = codeElement.Kind;
            this._language = codeElement.Language;
            this._name = codeElement.Name;
            this._projectItem = codeElement.ProjectItem;
            this._startPoint = codeElement.StartPoint;
            this._codeElement = codeElement;
        }

        /// <summary>
        /// prototype of codelement
        /// </summary>
        public T CodeElement
        {
            get { return this._codeElement; }
        }

        public TextPoint GetEndPoint(vsCMPart Part = vsCMPart.vsCMPartWholeWithAttributes)
        {
            return this._codeElement.GetEndPoint(Part);
        }

        public TextPoint GetStartPoint(vsCMPart Part = vsCMPart.vsCMPartWholeWithAttributes)
        {
            return this._codeElement.GetStartPoint(Part);
        }

        public object get_Extender(string ExtenderName)
        {
            return this._codeElement.get_Extender(ExtenderName);
        }

        public CodeElements Children
        {
            get { return this._children; }
        }

        public CodeElements Collection
        {
            get { return this._collection; }
        }

        public DTE DTE
        {
            get { return this._dte; }
        }

        public TextPoint EndPoint
        {
            get { return this._endPoint; }
        }

        public string ExtenderCATID
        {
            get { return this._codeElement.ExtenderCATID; }
        }

        public object ExtenderNames
        {
            get { return this._codeElement.ExtenderNames; }
        }

        public string FullName
        {
            get { return this._fullName; }
        }

        public vsCMInfoLocation InfoLocation
        {
            get { return this._infoLocation; }
        }

        public bool IsCodeType
        {
            get { return this._isCodeType; }
        }

        public vsCMElement Kind
        {
            get { return this._kind; }
        }

        public string Language
        {
            get { return this._language; }
        }

        public string Name
        {
            get
            {
                return this._name;
            }
            set { this._name = value; }
        }

        public ProjectItem ProjectItem
        {
            get { return this._projectItem; }
        }

        public TextPoint StartPoint
        {
            get { return this._startPoint; }
        }
    }
}
