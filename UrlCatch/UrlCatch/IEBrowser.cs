using System;
using System.Collections.Generic;
using System.Text;

namespace UrlCatch
{
    internal class IEBrowser : WebBrowser
    {
        private const string formName = "IEFrame";

        #region WebBrowser 成员

        public IntPtr GetBrowserForm()
        {
            IntPtr intptr = WinFormAPI.FindWindow(formName, null);

            if (intptr != (IntPtr)0)
            {
                return intptr;
            }
            return (IntPtr)0;
        }

        public IntPtr GetBrowserUrlHandler(IntPtr phwnd)
        {
            IntPtr child = WinFormAPI.FindWindowEx(phwnd, 0, "WorkerW", null);
            child = WinFormAPI.FindWindowEx(child, 0, "ReBarWindow32", null);
            child = WinFormAPI.FindWindowEx(child, 0, "ComboBoxEx32", null);
            child = WinFormAPI.FindWindowEx(child, 0, "ComboBox", null);
            child = WinFormAPI.FindWindowEx(child, 0, "Edit", null);
            return child;
        }

        #endregion
    }
}
