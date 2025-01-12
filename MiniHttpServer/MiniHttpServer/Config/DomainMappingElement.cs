using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;

namespace MiniHttpServer.Config
{
    public class DomainMappingElement : ConfigurationElement
    {
        [ConfigurationProperty("domain", IsKey = true, IsRequired = true)]
        public string Domain
        {
            get { return (string)this["domain"]; }
            set { this["domain"] = value; }
        }

        [ConfigurationProperty("path", IsRequired = true)]
        public string Path
        {
            get { return (string)this["path"]; }
            set { this["path"] = value; }
        }
        [ConfigurationProperty("home", IsRequired = true)]
        public string Home
        {
            get { return (string)this["home"]; }
            set { this["home"] = value; }
        }
    }
}