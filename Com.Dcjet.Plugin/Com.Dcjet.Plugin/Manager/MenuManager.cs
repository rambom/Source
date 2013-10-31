using System.Collections.Generic;
using Com.Dcjet.Plugin.Menu;
using Com.Dcjet.Plugin.Menu.Impl;

namespace Com.Dcjet.Plugin.Manager
{
    /// <summary>
    /// 菜单管理器
    /// </summary>
    internal class MenuManager
    {
        /// <summary>
        /// 返回要创建的菜单
        /// </summary>
        /// <returns></returns>
        public static IList<IMenuBar> GetAddinMenu()
        {
            return new List<IMenuBar>()
                       {
                           new SmartCodeBar()
                       };
        }
    }
}
