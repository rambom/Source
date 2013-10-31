using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Xml;

namespace XmlParse
{
    public class XmlParse
    {
        private const string Namespace = "QB";

        private const string NamespacePrefix = "//" + Namespace + ":";

        /// <summary>
        /// 解析加载XML文件
        /// </summary>
        /// <param name="xmlDoc"></param>
        /// <param name="xmlFile"></param>
        /// <param name="errorMessage"></param>
        /// <returns></returns>
        public static bool ParseXmlFile(ref XmlDocument xmlDoc, string xmlFile, out string errorMessage)
        {
            bool blnFlag = false;
            errorMessage = string.Empty;

            try
            {
                if (!File.Exists(xmlFile))
                {
                    errorMessage = string.Format("{0},文件不存在!", xmlFile);
                }
                else
                {
                    xmlDoc = new XmlDocument();
                    xmlDoc.Load(xmlFile);
                    blnFlag = true;
                }
            }
            catch (Exception)
            {
                errorMessage = "加载文件失败,Xml文件不正确!";
            }

            return blnFlag;
        }

        /// <summary>
        /// 通过XPath查询节点
        /// </summary>
        /// <param name="xmlDoc">xmlDoc上下文</param>
        /// <param name="xPath">xPath路径</param>
        /// <returns></returns>
        public static bool FindXmlNodeWithXPath(XmlDocument xmlDoc, string xPath, out XmlNodeList xmlNodeList, out string errorMsg)
        {
            bool blnFlag = false;
            xmlNodeList = null;
            errorMsg = string.Empty;
            try
            {
                XmlNamespaceManager xmlNamespaceManager = new XmlNamespaceManager(xmlDoc.NameTable);
                xmlNamespaceManager.AddNamespace(Namespace, xmlDoc.DocumentElement.NamespaceURI);
                xmlNodeList = xmlDoc.SelectNodes(xPath, xmlNamespaceManager);
                blnFlag = true;
            }
            catch (Exception)
            {
                errorMsg = "XPath表达式有误:\n" + xPath;
                blnFlag = false;
            }

            return blnFlag;
        }

        /// <summary>
        /// 格式化XPath字符串
        /// </summary>
        /// <param name="xPath">xPath路径</param>
        /// <returns></returns>
        public static void FormatXPath(ref string xPath)
        {
            while (xPath.StartsWith("/"))
            {
                xPath = xPath.Substring(1);
            }

            //命名空间转换
            xPath = xPath.Replace("/", NamespacePrefix);

            xPath = NamespacePrefix + xPath;
        }

        /// <summary>
        /// 更新节点
        /// </summary>
        /// <param name="xmlDoc"></param>
        /// <param name="xmlNode"></param>
        /// <param name="node"></param>
        /// <param name="value"></param>
        /// <param name="runFlag"></param>
        /// <param name="errorMessage"></param>
        /// <returns></returns>
        public static bool UpdateNode(XmlNode xmlNode, string node, string value, RunFlag runFlag, out string errorMessage)
        {
            bool blnFlag = true;
            errorMessage = string.Empty;
            try
            {

                if (RunFlag.Insert.Equals(runFlag))
                {
                    XmlElement xmlElement = xmlNode.OwnerDocument.CreateElement(node, xmlNode.NamespaceURI);
                    if (!string.IsNullOrEmpty(value.Trim()))
                        xmlElement.InnerText = value;
                    xmlNode.AppendChild(xmlElement);
                }
                else if (RunFlag.Erase.Equals(runFlag))
                {
                    xmlNode.ParentNode.RemoveChild(xmlNode);
                }
                else if (RunFlag.Update.Equals(runFlag))
                {
                    XmlNamespaceManager xmlNamespaceManager = new XmlNamespaceManager(xmlNode.OwnerDocument.NameTable);
                    xmlNamespaceManager.AddNamespace(Namespace, xmlNode.NamespaceURI);

                    XmlNodeList xmlNodeList = xmlNode.SelectNodes(Namespace + ":" + node, xmlNamespaceManager);

                    if (xmlNodeList.Count > 0)
                    {
                        foreach (XmlNode xmlNodeChild in xmlNodeList)
                        {
                            switch (runFlag)
                            {
                                case RunFlag.Update:
                                    xmlNodeChild.InnerText = value;
                                    break;
                            }
                        }
                    }
                    else
                    {
                        blnFlag = false;
                        errorMessage = "节点不存在:" + node;
                    }
                }
            }
            catch (Exception ex)
            {
                errorMessage = ex.ToString();
                blnFlag = false;
            }
            return blnFlag;
        }

        /// <summary>
        /// 更新属性
        /// </summary>
        /// <param name="xmlNode"></param>
        /// <param name="attribute"></param>
        /// <param name="value"></param>
        /// <param name="runFlag"></param>
        /// <param name="errorMessage"></param>
        /// <returns></returns>
        public static bool UpdateAttribute(XmlNode xmlNode, string attribute, string value, RunFlag runFlag, out string errorMessage)
        {
            bool blnFlag = true;
            errorMessage = string.Empty;

            try
            {
                if (RunFlag.Insert.Equals(runFlag))
                {
                    XmlAttribute xmlAttribute = xmlNode.OwnerDocument.CreateAttribute(attribute);
                    xmlAttribute.Value = value;
                    xmlNode.Attributes.Append(xmlAttribute);
                }
                else if (null != xmlNode.Attributes[attribute])
                {
                    switch (runFlag)
                    {
                        case RunFlag.Update:
                            xmlNode.Attributes[attribute].Value = value;
                            break;
                        case RunFlag.Erase:
                            xmlNode.Attributes.RemoveNamedItem(attribute);
                            break;
                    }
                }
                else
                {
                    blnFlag = false;
                    errorMessage = "属性不存在:" + attribute;
                }

            }
            catch (Exception ex)
            {
                errorMessage = ex.StackTrace;
                blnFlag = false;
            }
            return blnFlag;
        }
    }
}
