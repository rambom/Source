using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace CSV_Split
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
            this.Focus(); ActiveControl = null;
        }

        private void textBox2_TextChanged(object sender, EventArgs e)
        {
            if (double.TryParse(this.txtRowCount.Text.Replace(",", ""), out double number))
            {
                this.txtRowCount.Text = string.Format("{0:N0}", number); // N0 表示按千位分隔显示，没有小数部分
                this.txtRowCount.SelectionStart = this.txtRowCount.Text.Length; // 将光标移到末尾
            }
        }

        private void txtFilePath_Enter(object sender, EventArgs e)
        {
            // 清除全选状态，让文本框不会自动全选文本
            this.txtFilePath.SelectionStart = this.txtFilePath.Text.Length;
            this.txtFilePath.SelectionLength = 0;
        }

        private void txtFilePath_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            this.openFileDialog1.Filter = "CSV Files (*.csv)|*.csv";
            if (openFileDialog1.ShowDialog() == DialogResult.OK)
            {
                this.txtFilePath.Text = openFileDialog1.FileName;
            }
        }

        private void btnDoing_Click(object sender, EventArgs e)
        {
            string inputFilePath = this.txtFilePath.Text;
            int linesPerFile = int.Parse(this.txtRowCount.Text.Replace(",", ""));
            this.SplitCSVFile(inputFilePath, linesPerFile);
            MessageBox.Show("CSV splitting complete.");
        }
        private void SplitCSVFile(string inputFilePath, int linesPerFile)
        {
            FileInfo file = new FileInfo(inputFilePath);
            using (StreamReader reader = new StreamReader(inputFilePath))
            {
                int fileNumber = 1;
                int currentLine = 0;

                string header = reader.ReadLine(); // 读取列名
                while (!reader.EndOfStream)
                {
                    string outputFileName = $"{file.Name}_{fileNumber}.csv";
                    string outputFilePath = Path.Combine(file.DirectoryName, outputFileName);
                    using (StreamWriter writer = new StreamWriter(outputFilePath))
                    {
                        writer.WriteLine(header); // 写入列名

                        for (int i = 0; i < linesPerFile && !reader.EndOfStream; i++)
                        {
                            writer.WriteLine(reader.ReadLine());
                            currentLine++;
                        }
                    }

                    fileNumber++;
                }
            }
        }
    }
}
