using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Xml.Serialization;

namespace CrackUrtracker
{
    public class RegAckMsg
    {
        public RegAckMsg()
        {
            this.CustomerName = "苏州海关技术处";
            this.CheckResult = 0;
            this.CheckMessage = string.Empty;
            this.AllowedUserCount = 100;
            this.FunctionLevel = 2;
            this.ExpireDate = DateTime.Now.AddYears(100);
            this.NextCheckTime = DateTime.Now.AddYears(99);
        }

        private static RegAckMsg Deserialize(string xmlData)
        {
            XmlSerializer serializer = new XmlSerializer(typeof(RegAckMsg));
            StringReader textReader = new StringReader(xmlData);
            object obj2 = serializer.Deserialize(textReader);
            textReader.Close();
            return (RegAckMsg)obj2;
        }

        public static RegAckMsg RevertObject(string str)
        {
            return Deserialize(Encoding.UTF8.GetString(Convert.FromBase64String(str)));
        }

        public string Serialize()
        {
            XmlSerializer serializer = new XmlSerializer(typeof(RegAckMsg));
            StringBuilder sb = new StringBuilder();
            StringWriter writer = new StringWriter(sb);
            serializer.Serialize((TextWriter)writer, this);
            return sb.ToString();
        }

        public override string ToString()
        {
            return Convert.ToBase64String(Encoding.UTF8.GetBytes(this.Serialize()));
        }

        public int AllowedUserCount { get; set; }

        public string CheckMessage { get; set; }

        public int CheckResult { get; set; }

        public string CustomerName { get; set; }

        public DateTime ExpireDate { get; set; }

        public int FunctionLevel { get; set; }

        public DateTime NextCheckTime { get; set; }
    }
}
