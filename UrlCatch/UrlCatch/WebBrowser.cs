using System;
using System.Collections.Generic;
using System.Text;

namespace UrlCatch
{
    internal interface WebBrowser
    {
        IntPtr GetBrowserForm();
        IntPtr GetBrowserUrlHandler(IntPtr phwnd);
    }
}
