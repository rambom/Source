using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Management;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Security.Cryptography;
using System.Xml;

namespace CrackUrtracker
{
    public partial class Form1 : Form
    {
        /// <summary>
        /// crypt key
        /// </summary>
        private readonly byte[][] _encryptionKeyBytes = new byte[2][]
                                                           {
                                                               new byte[] { 0x66, 0x37, 0x42, 130, 0x42, 0x20, 70, 0x36 }, 
                                                               new byte[] { 0x12, 0x34, 0x56, 120, 0x90, 0xab, 0xcd, 0xef }
                                                           };
        /// <summary>
        /// license tpl
        /// </summary>
        private readonly string _license = "URTracker1Licensed:633873134499389639:665429870358029639:神州数码捷通科技:100:{0}:00145E683E42";
        /// <summary>
        /// license file
        /// </summary>
        private readonly string[] licenseFile = { "licenses\\URTRACKER.aspx", "urtracker.lic" };

        /// <summary>
        /// sql connection config section name
        /// </summary>
        private const string sqlSectionName = "URTrackerConnectionString";

        public Form1()
        {
            InitializeComponent();
            InitializeControl();
        }

        /// <summary>
        /// 初始化控件
        /// </summary>
        private void InitializeControl()
        {
            this.cbVer.Items.Insert(0, "2.3.0");
            this.cbVer.Items.Insert(1, "3.2.0.517");
            this.cbVer.SelectedIndex = 0;
            this.cbVer.SelectedIndexChanged += new EventHandler(cbVer_SelectedIndexChanged);
            this.txtMachine.ReadOnly = true;
            this.txtMachine.Text = GetMachineNo();
        }

        /// <summary>
        /// 下拉框选择事件
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void cbVer_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (this.cbVer.SelectedIndex == 1)
            {
                this.txtMachine.ReadOnly = false;
            }
            else
            {
                this.txtMachine.ReadOnly = true;
            }
        }

        private void txtUrl_Click(object sender, EventArgs e)
        {
            FolderBrowserDialog fbd = new FolderBrowserDialog();
            if (fbd.ShowDialog() == DialogResult.OK)
            {
                txtUrl.Text = fbd.SelectedPath;
            }
            fbd.Dispose();
        }

        private void btnCrack_Click(object sender, EventArgs e)
        {
            try
            {
                if (Directory.Exists(txtUrl.Text))
                {
                    switch (this.cbVer.SelectedIndex)
                    {
                        case 0:
                            EncryptLicenseData(string.Format(_license, txtUsers.Text), _encryptionKeyBytes[0], Path.Combine(txtUrl.Text, licenseFile[0]));
                            break;
                        case 1:
                            RegAckMsg regAckMsg = new RegAckMsg { AllowedUserCount = Int32.Parse(txtUsers.Text) };
                            EncryptXmlData(regAckMsg.ToString(), txtMachine.Text, _encryptionKeyBytes[1], licenseFile[1]);
                            break;
                    }

                    MessageBox.Show("破解成功");
                }
                else
                {
                    MessageBox.Show("请选择正确的安装路径!");
                }
            }
            catch (Exception ex)
            {

                MessageBox.Show(ex.ToString());
            }
        }

        void EncryptLicenseData(string strLicense, byte[] btKey, string filePath)
        {
            DESCryptoServiceProvider provider = new DESCryptoServiceProvider();
            provider.Key = btKey;
            provider.IV = btKey;
            byte[] message = Encoding.UTF8.GetBytes(strLicense);
            FileStream fs = new FileStream(filePath, FileMode.Create, FileAccess.Write, FileShare.Write);
            CryptoStream cs = new CryptoStream(fs, provider.CreateEncryptor(), CryptoStreamMode.Write);
            cs.Write(message, 0, message.Length);
            cs.Flush();
            cs.Close();
            fs.Close();
        }

        void EncryptXmlData(string license, string machineId, byte[] btKey, string filePath)
        {
            DESCryptoServiceProvider des = new DESCryptoServiceProvider();

            byte[] inputByteArray = Encoding.UTF8.GetBytes(license);

            MemoryStream ms = new MemoryStream();
            CryptoStream cs = new CryptoStream(ms, des.CreateEncryptor(Encoding.UTF8.GetBytes(machineId.Substring(0, 8)), btKey), CryptoStreamMode.Write);
            cs.Write(inputByteArray, 0, inputByteArray.Length);
            cs.FlushFinalBlock();
            cs.Close();
            StreamWriter streamWriter = new StreamWriter(filePath, false);
            streamWriter.Write(Convert.ToBase64String(ms.ToArray()));
            ms.Close();
            streamWriter.Flush();
            streamWriter.Close();
            /*
            string strCommandText = "\r\nIF EXISTS(SELECT [ConfigItem] FROM [Common_SystemConfig] WHERE [ConfigItem] = @ConfigItem)\r\nBEGIN\r\n\tUPDATE [Common_SystemConfig] SET\r\n\t\t[ConfigValue] = @ConfigValue\r\n\tWHERE\r\n\t\t[ConfigItem] = @ConfigItem\r\nEND\r\nELSE\r\nBEGIN\r\n\tINSERT INTO [Common_SystemConfig] (\r\n\t\t[ConfigItem],\r\n\t\t[ConfigValue]\r\n\t) VALUES (\r\n\t\t@ConfigItem,\r\n\t\t@ConfigValue\r\n\t)\r\nEND\r\n";
            XmlDocument xmlDoc = new XmlDocument();
            xmlDoc.Load(Path.Combine(txtUrl.Text, "Web.Config"));
            string strConn = xmlDoc.SelectSingleNode(string.Format("configuration/connectionStrings/add[@name='{0}']", sqlSectionName)).Attributes["connectionString"].Value;
            
            SqlConnection sqlCon = new SqlConnection(strConn);
            SqlCommand sqlCmd = new SqlCommand(strCommandText, sqlCon);

            sqlCmd.Parameters.AddWithValue("@ConfigItem", "LocalRConfig");
            sqlCmd.Parameters.AddWithValue("@ConfigValue", strEncrypt);
            
            sqlCon.Open();
            sqlCmd.ExecuteNonQuery();
            sqlCon.Close();
             * */
        }

        /// <summary>
        /// 获取机器码
        /// </summary>
        /// <returns></returns>
        private string GetMachineNo()
        {
            string str = GetCPUSerialNo();
            string str2 = GetHDSerialNo();
            return (str.Substring(str.Length - 4) + str2.Substring(str2.Length - 4));
        }

        /// <summary>
        /// 获取硬盘序列号
        /// </summary>
        /// <returns></returns>
        private string GetHDSerialNo()
        {
            try
            {
                ManagementObject obj2 = new ManagementObject("win32_logicaldisk.deviceid=\"c:\"");
                obj2.Get();
                return obj2.GetPropertyValue("VolumeSerialNumber").ToString();
            }
            catch
            {
                return "FFFF";
            }
        }

        /// <summary>
        /// 获取CPU序列号
        /// </summary>
        /// <returns></returns>
        private string GetCPUSerialNo()
        {
            try
            {
                ManagementObjectCollection instances = new ManagementClass("Win32_Processor").GetInstances();
                string str = null;
                foreach (ManagementObject obj2 in instances)
                {
                    str = obj2.Properties["ProcessorId"].Value.ToString();
                    break;
                }
                return str;
            }
            catch
            {
                return "FFFF";
            }
        }

        /// <summary>
        /// Des加密
        /// </summary>
        /// <param name="pToEncrypt">加密字符串</param>
        /// <param name="key">密钥</param>
        /// <returns></returns>
        public string DesEncrypt(string strEncrypt, string key)
        {
            strEncrypt = Convert.ToBase64String(Encoding.UTF8.GetBytes(strEncrypt));

            DESCryptoServiceProvider des = new DESCryptoServiceProvider();

            byte[] inputByteArray = Encoding.UTF8.GetBytes(strEncrypt);
            des.Key = Encoding.UTF8.GetBytes(key);
            des.IV = des.Key;
            MemoryStream ms = new MemoryStream();
            CryptoStream cs = new CryptoStream(ms, des.CreateEncryptor(), CryptoStreamMode.Write);
            cs.Write(inputByteArray, 0, inputByteArray.Length);
            cs.FlushFinalBlock();
            return byteToHexString(ms.ToArray());
        }

        /// <summary>
        /// Des解密
        /// </summary>
        /// <param name="strEncrypt">解密字符串</param>
        /// <param name="key">密钥</param>
        /// <returns></returns>
        public string DesDecrypt(string strEncrypt, string key)
        {
            DESCryptoServiceProvider des = new DESCryptoServiceProvider();

            byte[] inputByteArray = stringToHex(strEncrypt);

            byte[] byteKey = Encoding.UTF8.GetBytes(key);
            MemoryStream ms = new MemoryStream();
            CryptoStream cs = new CryptoStream(ms, des.CreateDecryptor(byteKey, byteKey), CryptoStreamMode.Write);
            cs.Write(inputByteArray, 0, inputByteArray.Length);
            cs.FlushFinalBlock();

            return Encoding.UTF8.GetString(Convert.FromBase64String(Encoding.UTF8.GetString(ms.ToArray())));
        }

        /// <summary>
        /// 字节数组转化为4位16进制
        /// </summary>
        /// <param name="byteStr"></param>
        /// <returns></returns>
        private string byteToHexString(byte[] byteStr)
        {
            StringBuilder stringBuilder = new StringBuilder();

            foreach (byte b in byteStr)
            {
                stringBuilder.AppendFormat("{0:X4}", b);
            }
            return stringBuilder.ToString();
        }

        /// <summary>
        /// 16进制字符串转字节数组
        /// </summary>
        /// <param name="str"></param>
        /// <returns></returns>
        private byte[] stringToHex(string str)
        {
            byte[] byteArray = new byte[str.Length / 4];
            for (int i = 0; i < byteArray.Length; i++)
            {
                int x = Convert.ToInt32(str.Substring(i * 4, 4), 16);
                byteArray[i] = (byte)x;
            }
            return byteArray;
        }
    }
}
