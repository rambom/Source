using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using System.Windows.Forms;

namespace DesUtil
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
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

        private void btnCrypt_Click(object sender, EventArgs e)
        {
            try
            {
                if (((Button)sender).Tag == "Decrypt")
                {
                    this.txtMing.Clear();
                    this.txtMing.Text = DesDecrypt(this.txtMi.Text, this.txtKey.Text);
                }
                else
                {
                    txtMi.Clear();
                    this.txtMi.Text = DesEncrypt(this.txtMing.Text, this.txtKey.Text);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
    }
}
