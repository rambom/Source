using System;
using System.IO;
using System.Text.RegularExpressions;
using System.Xml;

namespace XmlParse
{
    internal class Program
    {
        /// <summary>
        /// 新增与更新语法
        /// </summary>
        private const string Pattern1 = @"-(i|u) -(p|n):(\S+) ([ \S]+) ([ \S]+) ([ \S]+)$";
        /// <summary>
        /// 节点删除语法
        /// </summary>
        private const string Pattern2 = @"-d -n ([ \S]+) ([ \S]+)$";
        /// <summary>
        /// 属性删除语法
        /// </summary>
        private const string Pattern3 = @"-d -p:(\S+) ([ \S]+) ([ \S]+)$";

        private static int Main(string[] args)
        {
            Console.WriteLine(System.AppDomain.CurrentDomain.BaseDirectory);
            Console.WriteLine(System.Environment.CurrentDirectory);
            Console.WriteLine(System.AppDomain.CurrentDomain.SetupInformation.ApplicationBase);
            Console.WriteLine(System.IO.Directory.GetCurrentDirectory());
            //xml File path
            string strXmlFile = null;

            //XPath value
            string strXPath = null;

            //settle values
            string strValue = null;

            string strErrorMsg = null;

            string strProperty = null;

            //操作类型
            OperateFlag operateFlag;
            //执行标识
            RunFlag runFlag;

            XmlDocument xmlDoc = null;

            XmlNodeList xmlNodeList = null;

            bool blnFlag = false;

            try
            {

                if (IsCommandRight(args, out strXmlFile, out strXPath, out strProperty, out strValue))
                {
                    runFlag = (RunFlag)char.Parse(args[0].Substring(1, 1).ToUpper());
                    operateFlag = (OperateFlag)char.Parse(args[1].Substring(1, 1).ToUpper());

                    Console.Write("\n");

                    if (XmlParse.ParseXmlFile(ref xmlDoc, strXmlFile, out strErrorMsg))
                    {
                        //格式化XPath
                        XmlParse.FormatXPath(ref strXPath);
                        //获取节点

                        if (XmlParse.FindXmlNodeWithXPath(xmlDoc, strXPath, out xmlNodeList, out strErrorMsg))
                        {

                            if (xmlNodeList.Count > 0)
                            {
                                foreach (XmlNode xmlNode in xmlNodeList)
                                {
                                    if (OperateFlag.Attribute.Equals(operateFlag))
                                    {
                                        //修改属性
                                        blnFlag = XmlParse.UpdateAttribute(xmlNode, strProperty, strValue, runFlag, out strErrorMsg);

                                    }
                                    else if (OperateFlag.Node.Equals(operateFlag))
                                    {
                                        //修改节点
                                        blnFlag = XmlParse.UpdateNode(xmlNode, strProperty, strValue, runFlag, out strErrorMsg);
                                    }

                                    if (!blnFlag)
                                        break;
                                }


                                if (blnFlag)
                                {
                                    xmlDoc.Save(strXmlFile);
                                    Console.WriteLine("Update Success!");
                                }
                                else
                                {
                                    Console.WriteLine(strErrorMsg);
                                    return -1;
                                }

                            }
                            else
                            {
                                Console.WriteLine("节点未找到!");
                                return -2;
                            }
                        }
                        else
                        {
                            Console.WriteLine(strErrorMsg);
                            return -3;
                        }

                    }
                    else
                    {
                        Console.Write(strErrorMsg);
                        return -4;
                    }
                }
                else
                {
                    return -5;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("System Error:\n" + ex.Message);
                return -6;
            }
            finally
            {
                xmlDoc = null;
            }

            return 0;
        }

        /// <summary>
        /// 验证命令行格式
        /// </summary>
        /// <param name="args"></param>
        /// <param name="property">属性名</param>
        /// <param name="newValue">新值</param>
        /// <param name="xmlFile">文件路径</param>
        /// <param name="xmlPath">XPath描述</param>
        /// <returns></returns>
        private static bool IsCommandRight(string[] args, out string xmlFile, out string xmlPath, out string property, out string newValue)
        {
            string strCommand = string.Join(" ", args);
            xmlFile = string.Empty;
            xmlPath = string.Empty;
            property = string.Empty;
            newValue = string.Empty;

            if (Regex.IsMatch(strCommand, Pattern1, RegexOptions.IgnoreCase) && args.Length == 5)
            {
                property = args[1].Substring(3);
                xmlFile = args[2];
                xmlPath = args[3];
                newValue = args[4];
            }
            else if (Regex.IsMatch(strCommand, Pattern2, RegexOptions.IgnoreCase) && args.Length == 4)
            {
                xmlFile = args[2];
                xmlPath = args[3];
            }
            else if (Regex.IsMatch(strCommand, Pattern3, RegexOptions.IgnoreCase) && args.Length == 4)
            {
                property = args[1].Substring(3);
                xmlFile = args[2];
                xmlPath = args[3];
            }
            else
            {
                Console.WriteLine(strCommand);
                Console.WriteLine("\nXmlParse (-[i|d|u]) (-[p|n]:value) filepath XPath NodeValue \n");
                Console.WriteLine("-[i|d|u]       i 插入 d 删除 u 更新\t");
                Console.WriteLine("-[p|n]         p 属性操作 n 节点操作\t");
                Console.WriteLine("value          属性或节点名,删除节点时不提供\t");
                Console.WriteLine("filepath       Xml文件路径\t");
                Console.WriteLine("XPath          标准的XPath\t");
                Console.WriteLine("NodeValue      新值,删除操作时不提供\t");
                return false;
            }
            return true;
        }

    }
}