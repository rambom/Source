using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace DeviceTest
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
                        //读取设备列表
            string result = String.Empty;
            // 深度遍历主控制器
            HostControllerInfo[] HostControllersCollection = USB.AllHostControllers;

            if (HostControllersCollection != null)
            {
                Int32 ControllerIndex = 1;
                foreach (HostControllerInfo item in HostControllersCollection)
                {   
                    // 创建主控制器节点
                    String PNPDeviceID = item.PNPDeviceID;
                    String HcdDriverKeyName = USB.GetHcdDriverKeyName(PNPDeviceID);
                    textBox1.Text += "HostController:" + ControllerIndex + "\r\n"+
                                     "  Name:" + item.Name + "\r\n"+
                                     "  PNPDeviceID:" + PNPDeviceID + "\r\n"+
                                     "  HcdDriverKeyName:" + HcdDriverKeyName + "\r\n";
                    ControllerIndex++;

                    // 创建根集线器节点
                    String RootHubPath = USB.GetUsbRootHubPath(PNPDeviceID);
                    textBox1.Text += "RootHub:" + RootHubPath+"\r\n\r\n";
                }
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            //启动，禁用设备
            String PNPDeviceID = textBox2.Text.TrimEnd();
            String HcdDriverKeyName = textBox3.Text.TrimEnd();
            bool IsOpen=checkBox1.Checked;

            DeviceHelper.SetDeviceEnabled(Guid.Parse(HcdDriverKeyName), PNPDeviceID, IsOpen);
        }
    }
}
