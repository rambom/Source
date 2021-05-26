using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace Com.Dcjet.Plugin.Entity
{
    /// <summary>
    /// 公共常量
    /// </summary>
    internal class Constant
    {
        /// <summary>
        /// 匹配菜单名称正则表达式
        /// </summary>
        public const string MenuBarPattern = @"\[[\w ]+\]";
        /// <summary>
        /// csharp文件扩展名
        /// </summary>
        public const string CSharpFileExtension = ".cs";
    }
}
