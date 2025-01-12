using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;

namespace MiniHttpServer.Entity
{
    public class DomainConfig
    {
        public string Domain { get; set; }
        public string Path { get; set; }
        public string Home { get; set; }
    }
}