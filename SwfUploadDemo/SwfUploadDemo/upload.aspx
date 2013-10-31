<%@ Page Language="C#" AutoEventWireup="true" Inherits="SwfUploadDemo.upload" %>

<script language="c#" runat="server">
    protected void Page_Load(object sender, EventArgs e)
    {
        HttpPostedFile postedFile = HttpContext.Current.Request.Files["FileData"];
        if (null != postedFile)
        {
            postedFile.SaveAs(System.IO.Path.Combine(Server.MapPath(@"~/UpLoadFiles/"), postedFile.FileName));
        }
    }
</script>

