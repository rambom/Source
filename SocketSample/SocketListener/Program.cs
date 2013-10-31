using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Text.RegularExpressions;

namespace SocketListener
{
    class Program
    {
        /// <summary>
        /// 命令行语法匹配
        /// </summary>
        private const string Pattern = @"\d{1,5}";

        static void Main(string[] args)
        {
            try
            {
                string strPort, strIp;
                int i = 0;
                if (IsCommandRight(args, out strPort))
                {
                    Socket objListener = null;
                    IPEndPoint localEP = new IPEndPoint(IPAddress.Any, Convert.ToInt32(strPort));
                    objListener = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                    try
                    {
                        objListener.ExclusiveAddressUse = false;
                        objListener.Bind(localEP);
                        objListener.Listen(100);
                        while (true)
                        {
                            Socket socket = objListener.Accept();
                            byte[] buffer = new byte[10240];
                            int count = socket.Receive(buffer);
                            i++;
                            Console.Write(Encoding.UTF8.GetString(buffer, 0, count));
                            Console.WriteLine(string.Format("接收次数:{0}\t接收时间:{1}", i, DateTime.Now));
                            socket.Shutdown(SocketShutdown.Both);
                            socket.Disconnect(true);
                            socket.Close();
                        }
                    }
                    catch (ArgumentNullException exception)
                    {
                        throw exception;
                    }
                    catch (ObjectDisposedException exception2)
                    {
                        throw exception2;
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
        }

        /// <summary>
        /// 验证命令行格式
        /// </summary>
        /// <param name="args"></param>
        /// <param name="strIp"></param>
        /// <param name="strPort"></param>
        /// <returns></returns>
        private static bool IsCommandRight(string[] args, out string strPort)
        {
            string strCommand = string.Join(" ", args);
            strPort = "";

            if (Regex.IsMatch(strCommand, Pattern, RegexOptions.IgnoreCase))
            {
                strPort = args[0];
            }
            else
            {
                Console.WriteLine(strCommand);
                Console.WriteLine("SocketListener port");
                Console.WriteLine("port      监听端口\t");
                return false;
            }

            return true;
        }
    }
}
