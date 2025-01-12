using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;

namespace MiniHttpServer.Config
{
    public class DomainMappingSection : ConfigurationSection
    {
        /*
         * 通过将属性名设置为空字符串并启用 IsDefaultCollection = true，可以直接定义集合元素，而无需显式属性名称，
         * 如指定属性名如：mapping，则在config中需先使用节点<mapping><mapping domain="a" path="b"/><mapping>来定义
         */
        [ConfigurationProperty("", IsDefaultCollection = true)]
        public DomainMappingCollection Mapping
        {
            get
            {
                return this[""] as DomainMappingCollection;
            }
        }
    }
}