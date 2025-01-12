using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;

namespace MiniHttpServer.Config
{
    [ConfigurationCollection(typeof(DomainMappingElement), AddItemName = "mapping")]
    public class DomainMappingCollection : ConfigurationElementCollection
    {
        protected override ConfigurationElement CreateNewElement()
        {
            return new DomainMappingElement();
        }

        protected override object GetElementKey(ConfigurationElement element)
        {
            return ((DomainMappingElement)element).Domain;
        }

        public DomainMappingElement this[string key]
        {
            get { return (DomainMappingElement)base.BaseGet(key); }
        }
    }
}