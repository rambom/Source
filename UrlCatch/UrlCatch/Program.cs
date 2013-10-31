using System;
using System.Collections.Generic;
using System.Text;

namespace UrlCatch
{
    class Program
    {
        static void Main(string[] args)
        {
            for (; ; )
            {
                BrowserUtil browser = new BrowserUtil("www.baidu.com", "http://www.sina.com.cn");
                browser.RedirectUrl(new IEBrowser());
                browser = null;
                GC.Collect();
            }
        }
    }
}
