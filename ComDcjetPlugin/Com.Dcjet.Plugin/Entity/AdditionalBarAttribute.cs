using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Com.Dcjet.Plugin.Entity
{
    /// <summary>
    /// 附加菜单属性
    /// </summary>
    [AttributeUsage(AttributeTargets.Class, Inherited = true, AllowMultiple = true)]
    internal class AdditionalBarAttribute : Attribute
    {
        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="barDescription"></param>
        /// <param name="vsCommandBar"></param>
        public AdditionalBarAttribute(string vsCommandBar, string barDescription)
        {
            VsCommandBar = vsCommandBar;
            BarDescription = barDescription;
        }

        /// <summary>
        /// DTE 命令栏名称
        /// </summary>
        public string VsCommandBar { get; set; }
        /// <summary>
        /// 命令栏描述
        /// </summary>
        public string BarDescription { get; set; }
    }
}
