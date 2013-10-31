using System;
using System.Collections.Generic;
using System.Text;
using System.Runtime.InteropServices;

namespace UrlCatch
{
    internal class WinFormAPI
    {
        public const int WM_GETTEXT = 0x000D;
        public const int WM_SETTEXT = 0x000C;
        public const int WM_KEYDOWN = 0x100;
        public const int VK_RETURN = 13;

        [DllImport("user32")]
        public static extern IntPtr FindWindow(string lpclassname, string lpwindowsname);

        [DllImport("user32")]
        public static extern IntPtr FindWindowEx(IntPtr phwnd, int hwnd, string lpclassname, string lpwindowsname);

        [DllImport("user32.dll", EntryPoint = "SendMessage")]
        public static extern int SendMessage(
            IntPtr hwnd,
            int wMsg,
            int wParam,
            StringBuilder lParam
        );

        [DllImport("user32.dll", EntryPoint = "SendMessage")]
        public static extern int SendMessage(
            IntPtr hwnd,
            int wMsg,
            int wParam,
            int lParam
        );

    }
}
