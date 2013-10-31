using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Xml;

namespace CastBase64Character
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void btnConvert_Click(object sender, EventArgs e)
        {
            try
            {
                string strPath = System.IO.Path.GetDirectoryName(this.txtFile.Text);
                if (System.IO.Directory.Exists(strPath))
                {
                    byte[] byteString = Convert.FromBase64String(System.IO.File.ReadAllText(this.txtFile.Text));
                    string strCast = System.Text.Encoding.UTF8.GetString(byteString);
                    XmlDocument xmlDoc = new XmlDocument();
                    xmlDoc.LoadXml(strCast);
                    xmlDoc.Save(System.IO.Path.Combine(strPath, System.IO.Path.GetFileNameWithoutExtension(this.txtFile.Text)) + ".xml");
                    MessageBox.Show("转换成功!");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void Form1_DragEnter(object sender, DragEventArgs e)
        {
            if (e.Data.GetDataPresent(DataFormats.FileDrop))
                e.Effect = DragDropEffects.Link;
            else
                e.Effect = DragDropEffects.None;
        }

        private void Form1_DragDrop(object sender, DragEventArgs e)
        {
            this.txtFile.Text = ((System.Array)e.Data.GetData(DataFormats.FileDrop)).GetValue(0).ToString();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                string strPath = System.IO.Path.GetDirectoryName(this.txtFile.Text);
                string strCast = Convert.ToBase64String(System.IO.File.ReadAllBytes(this.txtFile.Text));

                System.IO.File.WriteAllText(System.IO.Path.Combine(strPath, System.IO.Path.GetFileNameWithoutExtension(this.txtFile.Text)) + ".txt", strCast);
                MessageBox.Show("转换成功!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
    }
}
