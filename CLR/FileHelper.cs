using System.IO;
using System.Text;
using Microsoft.SqlServer.Server;

namespace Common.Database.Interface
{
    public class FileHelper
    {
        [SqlFunction(IsDeterministic = true, IsPrecise = true)]
        public static string GetFileHexChar(string filePath, string outFile)
        {
            var fs = new FileStream(filePath, FileMode.Open, FileAccess.Read);
            var br = new BinaryReader(fs);
            var sw = new StreamWriter(outFile);
            var tempStr = new StringBuilder();
            var length = (int)fs.Length;
            while (length > 0)
            {
                byte tempByte = br.ReadByte();
                sw.Write("{0:x2}", tempByte);
                length--;
            }
            fs.Close();
            br.Close(); 
            sw.Close();
            return "";
        }
    }
}
