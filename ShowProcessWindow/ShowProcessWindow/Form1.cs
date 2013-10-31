using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Runtime.InteropServices;
using System.Text;
using System.Windows.Forms;

namespace ShowProcessWindow
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
            label1.Text = "进程ID可打开任务管理器获得,PID列即!";
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (listBox1.SelectedIndex == -1)
            {
                MessageBox.Show("未选择窗体句柄!");
            }
            else
            {
                IntPtr hwnd = (IntPtr)int.Parse(listBox1.SelectedItem.ToString());
                if (hwnd != IntPtr.Zero)
                    User32API.ShowWindow(hwnd, 1);
            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            if (listBox1.SelectedIndex == -1)
            {
                MessageBox.Show("未选择窗体句柄!");
            }
            else
            {
                IntPtr hwnd = (IntPtr)int.Parse(listBox1.SelectedItem.ToString());
                if (hwnd != IntPtr.Zero)
                    User32API.ShowWindow(hwnd, 0);
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            bool blnExist = false;
            if (!string.IsNullOrEmpty(this.textBox2.Text))
            {
                listBox1.Items.Clear();
                int intPid = int.Parse(textBox2.Text);
                foreach (Process p in Process.GetProcesses())
                {
                    if (p.Id.Equals(intPid))
                    {
                        SortedList hwndList = User32API.GetWindowHandle(intPid);
                        foreach (DictionaryEntry obj in hwndList)
                        {
                            listBox1.Items.Add(obj.Key);
                        }
                        blnExist = true;
                        return;
                    }
                }
                if (!blnExist)
                    MessageBox.Show("进程不存在!");
            }
        }

        private void textBox2_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (!Char.IsControl(e.KeyChar) && (e.KeyChar < 48 || e.KeyChar > 57))
                e.Handled = true;
        }
    }

    public class User32API
    {
        private static SortedList hwndList = null;
        public delegate bool Wndenumproc(IntPtr hwnd, uint lParam);

        static User32API()
        {
            if (hwndList == null)
            {
                hwndList = new SortedList();
            }
        }


        [DllImport("user32.dll", EntryPoint = "ShowWindow", SetLastError = true)]
        public static extern bool ShowWindow(IntPtr hWnd, uint nCmdShow);
        [DllImport("user32.DLL")]
        static extern int SendMessage(IntPtr hWnd, uint Msg, int wParam, int lParam);
        [DllImport("user32.DLL")]
        static extern IntPtr FindWindow(string lpszClass, string lpszWindow);
        [DllImport("user32.DLL")]
        static extern IntPtr FindWindowEx(IntPtr hwndParent, IntPtr hwndChildAfter, string lpszClass, string lpszWindow);

        [DllImport("user32.dll", EntryPoint = "EnumWindows", SetLastError = true)]
        public static extern bool EnumWindows(Wndenumproc lpEnumFunc, uint lParam);

        [DllImport("user32.dll", EntryPoint = "GetParent", SetLastError = true)]
        public static extern IntPtr GetParent(IntPtr hWnd);

        [DllImport("user32.dll", EntryPoint = "GetWindowThreadProcessId")]
        public static extern uint GetWindowThreadProcessId(IntPtr hWnd, ref uint lpdwProcessId);

        [DllImport("user32.dll", EntryPoint = "IsWindow")]
        public static extern bool IsWindow(IntPtr hWnd);

        [DllImport("kernel32.dll", EntryPoint = "SetLastError")]
        public static extern void SetLastError(uint dwErrCode);

        public static SortedList GetWindowHandle(int processId)
        {
            hwndList.Clear();
            uint uiPid = (uint)processId;

            bool bResult = EnumWindows(new Wndenumproc(EnumWindowsProc), uiPid);

            if (bResult && Marshal.GetLastWin32Error() == 0)
            {
                return hwndList;
            }

            return null;
        }

        private static bool EnumWindowsProc(IntPtr hwnd, uint lParam)
        {
            uint uiPid = 0;

            //if (GetParent(hwnd) == IntPtr.Zero)
            {
                GetWindowThreadProcessId(hwnd, ref uiPid);
                if (uiPid == lParam)
                {
                    SetLastError(0);
                    hwndList.Add((int)hwnd, hwnd);
                    //return false;
                }
            }
            return true;
        }
    }

}
