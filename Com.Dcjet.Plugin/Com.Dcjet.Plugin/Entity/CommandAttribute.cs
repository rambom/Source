using System;
using Microsoft.VisualStudio.CommandBars;

namespace Com.Dcjet.Plugin.Entity
{
    /// <summary>
    /// 命令属性
    /// </summary>
    [AttributeUsage(AttributeTargets.Class | AttributeTargets.Interface, AllowMultiple = false, Inherited = true)]
    internal class CommandAttribute : Attribute
    {
        /// <summary>
        /// default constructor
        /// </summary>
        public CommandAttribute()
        {

        }

        /// <summary>
        /// constructor 2
        /// </summary>
        /// <param name="key">唯一键值</param>
        /// <param name="controlType">控件类型</param>
        /// <param name="iconResource">图标资源索引</param>
        /// <param name="useMsoButton">使用msobutton</param>
        /// <param name="position">插入位置</param>
        /// <param name="tooltip">提示</param>
        /// <param name="caption">显示文字</param>
        /// <param name="initViewStatus">初始状态</param>
        public CommandAttribute(string key, MsoControlType controlType, int iconResource, bool useMsoButton, int position, string tooltip, string caption, CommandViewStatus initViewStatus)
        {
            Key = key;
            ControlType = controlType;
            IconResource = iconResource;
            UseMsoButton = useMsoButton;
            Position = position;
            Tooltip = tooltip;
            Caption = caption;
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
        /// 使用mso按钮
        /// </summary>
        public bool UseMsoButton { get; set; }
        /// <summary>
        /// 图标资源索引
        /// </summary>
        public int IconResource { get; set; }
        /// <summary>
        /// 控件类型
        /// </summary>
        public MsoControlType ControlType { get; set; }
        /// <summary>
        /// 命令初始状态
        /// </summary>
        public CommandViewStatus InitViewStatus { get; set; }
    }
}
