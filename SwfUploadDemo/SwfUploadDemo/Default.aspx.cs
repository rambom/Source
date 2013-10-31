using System;
using System.Collections;
using System.Configuration;
using System.Data;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using Microsoft.Win32;
using System.Net;
using System.Web.Services.Protocols;
using System.Runtime.Remoting;
using System.Runtime.Serialization.Formatters.Binary;



namespace SwfUploadDemo
{
    public partial class _Default : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            Session.Clear();
            HttpPostedFile postedFile = HttpContext.Current.Request.Files["FileData"];
            if (null != postedFile)
            {
                postedFile.SaveAs(System.IO.Path.Combine(Server.MapPath(@"~/UpLoadFiles/"), postedFile.FileName));
                Response.Write("<script>if(confirm('aa')) test();</script>");
            }

        }

    }
}
