using System;
using System.Collections.Generic;
using Microsoft.VisualStudio.CommandBars;

namespace Com.Dcjet.Plugin.Entity
{
    /// <summary>
    /// 菜单属性
    /// </summary>
    [AttributeUsage(AttributeTargets.Class, AllowMultiple = false, Inherited = true)]
    internal class MenuAttribute : Attribute
    {
        /// <summary>
        /// default constructor
        /// </summary>
        public MenuAttribute()
        {
        }

        /// <summary>
        /// constructor 2
        /// </summary>
        /// <param name="caption"></param>
        /// <param name="controlType"></param>
        /// <param name="key"></param>
        /// <param name="position"></param>
        /// <param name="tooltip"></param>
        public MenuAttribute(string caption, MsoControlType controlType, string key, int position, string tooltip)
        {
            Caption = caption;
            ControlType = controlType;
            Key = key;
            Position = position;
            Tooltip = tooltip;
        }


        /// <summary>
        /// 内部唯一编码
        /// </summary>
        public string Key { get; set; }
        /// <summary>
        /// 显示文字
        /// </summary>
        public string Caption { get; set; }
        /// <summary>
        /// 提示描述
        /// </summary>
        public string Tooltip { get; set; }
        /// <summary>
        /// 显示位置
        /// </summary>
        public int Position { get; set; }
        /// <summary>
        /// 控件类型
        /// </summary>
        public MsoControlType ControlType { get; set; }
    }
}
