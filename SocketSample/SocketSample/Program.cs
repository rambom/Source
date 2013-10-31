using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Net.NetworkInformation;
using System.Net.Sockets;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;

namespace SocketSample
{
    class Program
    {
        /// <summary>
        /// 命令行语法匹配
        /// </summary>
        private const string Pattern = @"ip=(\S+) port=(\d{1,5}) file=([ \S]+)$";

        static int Main(string[] args)
        {
            int intReturn = 0;
            string strIp = "", strPort, strFile, strErrMsg = "";
            try
            {
                if (IsCommandRight(args, out strIp, out strPort, out strFile))
                {
                    if (!File.Exists(strFile))
                    {
                        throw new FileNotFoundException();
                    }
                    SocketClient client = new SocketClient(IPAddress.Parse(strIp), Convert.ToInt32(strPort));
                    client.Connect();
                    if (client.connected)
                    {
                        client.Send(File.ReadAllText(strFile), Encoding.UTF8, false, out strErrMsg);
                        client.Close();
                        Console.WriteLine(string.Format("{0} send returns:{1}", strIp, strErrMsg));

                        intReturn = (int)ConnectStatus.Success;
                    }
                    else
                    {
                        Console.WriteLine(string.Format("no answer,please confirm the {0} port be available...", strIp));
                        intReturn = (int)ConnectStatus.NoAnswer;
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(strIp + ":" + ex.Message);
                intReturn = (int)ConnectStatus.Error;
            }

            return intReturn;
        }

        /// <summary>
        /// 验证命令行格式
        /// </summary>
        /// <param name="args"></param>
        /// <param name="strIp"></param>
        /// <param name="strPort"></param>
        /// <param name="strFile"></param>
        /// <returns></returns>
        private static bool IsCommandRight(string[] args, out string strIp, out string strPort, out string strFile)
        {
            string strCommand = string.Join(" ", args);
            strIp = "";
            strPort = "";
            strFile = "";

            if (Regex.IsMatch(strCommand, Pattern, RegexOptions.IgnoreCase))
            {
                strIp = args[0].Split('=')[1];
                strPort = args[1].Split('=')[1];
                strFile = args[2].Split('=')[1];
            }
            else
            {
                Console.WriteLine(strCommand);
                Console.WriteLine("ip=host port=port file=fileName\n");
                Console.WriteLine("ip        目标主机地址\t");
                Console.WriteLine("port      目标主机端口\t");
                Console.WriteLine("fileName  要发送的文件\t");
                return false;
            }

            return true;
        }


        private enum ConnectStatus
        {
            Error = -3,
            PingFailed,
            NoAnswer,
            Success
        }
    }
}
