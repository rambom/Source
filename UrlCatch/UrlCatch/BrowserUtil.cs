using System;
using System.Collections.Generic;
using System.Text;
using System.Runtime.InteropServices;

namespace UrlCatch
{
    internal class BrowserUtil
    {

        private string orginUrl = string.Empty;
        private string replaUrl = string.Empty;

        public BrowserUtil(string url, string replaceUrl)
        {
            orginUrl = url;
            replaUrl = replaceUrl;
        }

        public void RedirectUrl(WebBrowser webBrowser)
        {
            IntPtr intptr = webBrowser.GetBrowserUrlHandler(webBrowser.GetBrowserForm());
            StringBuilder strBuffer = new StringBuilder(1024);
            int num = WinFormAPI.SendMessage(intptr, WinFormAPI.WM_GETTEXT, 1024, strBuffer);
            string strURL = strBuffer.ToString().Trim('/').ToLower();

            if (strURL.IndexOf(orginUrl) != -1)
            {
                strBuffer.Remove(0, strBuffer.Length);
                strBuffer.Append(replaUrl);
                WinFormAPI.SendMessage(intptr, WinFormAPI.WM_SETTEXT, 1024, strBuffer);
                WinFormAPI.SendMessage(intptr, WinFormAPI.WM_KEYDOWN, WinFormAPI.VK_RETURN, 0);
            }
        }
    }
}
