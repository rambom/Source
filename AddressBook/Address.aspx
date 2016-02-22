<html>
<head>
    <title>Dcjet Address Book</title>
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=2.0" />
    <style type="text/css">body{font-size:12px;color:#005EAC;font-family:微软雅黑;}table{font-family:verdana,arial,sans-serif;font-size:15;line-height:1.7em;margin-top:10px;width:100%;}td{cursor:pointer;color:#00a;text-align:center;padding:0.3em;}.head{background-color:#005EAC;color:#fff;font-size:18px;font-weight:bold;padding:10px;}span{color:Red;font-size:13px;margin:0px 0px 5px 0px;}</style>
    <script language="javascript" type="text/javascript">
        function CheckInput() {var strWhere = document.getElementById('hidDownload').value = '0'; document.getElementById('txtWhere').value.replace(/(^\s*)|(\s*$)/g, ''); var strUser = document.getElementById('txtSvnUser').value.replace(/(^\s*)|(\s*$)/g, ''); var strPass = document.getElementById('txtSvnPwd').value.replace(/(^\s*)|(\s*$)/g, ''); if (strUser == '' || strPass == '' || strWhere == '') return false; else return true; }
	</script>
    <script language="c#" runat="server">
        private System.Data.DataRow[] dataRows;
        private string strSvnUser;
        private string strSvnPwd;
        private string strWhere;
        private string updateTime;
        protected void Page_Load(object sender, EventArgs e)
        {
            strSvnUser = Request.Form["txtSvnUser"];
            strSvnPwd = Request.Form["txtSvnPwd"];
			strWhere=Request.Form["txtWhere"];
            
            //用户名,密码,查询条件不能为空
			if(string.IsNullOrEmpty(strSvnUser)||string.IsNullOrEmpty(strSvnPwd)||string.IsNullOrEmpty(strWhere))
			return;
			
            strSvnUser = strSvnUser.Trim();
            strSvnPwd = strSvnPwd.Trim();
            strWhere = strWhere.Trim();
            string strErrMsg;

            string strUrl = @"http://svnjet.dcjet.com.cn:8088/svn/jet/98%20%E5%85%AC%E5%8F%B8%E9%80%9A%E8%AE%AF%E5%BD%95/%E6%8D%B7%E9%80%9A%E5%85%AC%E5%8F%B8%E9%80%9A%E8%AE%AF%E5%BD%95.xls";

            //addressBook file path
            string strFilePath = Server.MapPath("~/resource/DcjetAddressBook.xls");
            int intFlag = this.DownloadSvnFile(strSvnUser, strSvnPwd, strUrl, strFilePath, out strErrMsg);

            if(1 == intFlag)
            {
                Response.Write(string.Format("<span>[{0}]</span>",strErrMsg));
            }
            else if(System.IO.File.Exists(strFilePath))
            {
                updateTime = string.Format("<span style=\"color:#ccc;display:block;width:220px;margin-top:3px;border-top:dotted 1px\">update by [{0}]</span>",System.IO.File.GetLastWriteTime(strFilePath));
                Aspose.Cells.Workbook workbook = new Aspose.Cells.Workbook();

                if (strFilePath.EndsWith(".xlsx"))
                    workbook.Open(System.IO.File.OpenRead(strFilePath), Aspose.Cells.FileFormatType.Excel2007Xlsx);
                else
                    workbook.Open(strFilePath);

                //导入Excel
                Aspose.Cells.Worksheet worksheet = workbook.Worksheets[0];
                System.Data.DataTable dataTable = worksheet.Cells.ExportDataTable(1, 6, worksheet.Cells.MaxDataRow, 6);
                //去除Excel列头
                dataTable.Rows.RemoveAt(0);
                //按姓名,固定电话,手机,海关小号,邮件地址进行模糊查询
                dataRows =
                    dataTable.Select(
                        string.Format(
                            "Column1 <> '' and (Column1 like '%{0}%' or Column3 like '%{0}%' or Column4 like '%{0}%' or Column5 like '%{0}%' or Column6 like '%{0}%')",
                            strWhere));
            }
            else
            {
                Response.Write(string.Format("[<span>更新通讯录失败，{0}]</span>",strErrMsg));
            }
        }

        private int DownloadSvnFile(string user,string password,string url, string filename, out string errMsg)
        {
            //returns value
            int intFlag = 0;
            errMsg = string.Empty;

            try
            {
                System.Net.HttpWebRequest request = (System.Net.HttpWebRequest)System.Net.WebRequest.Create(url);
                request.AllowAutoRedirect = false;
                request.Method = "GET";
                request.ContentType = "application/octet-stream";
                request.Accept = "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-ms-application, application/x-ms-xbap, application/vnd.ms-xpsdocument, application/xaml+xml, */*";
                request.UserAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; CIBA; InfoPath.2; .NET CLR 1.0.3705; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR 1.1.4322)";

                /*
                request.Headers["Accept-Language"] = "zh-cn";
                request.Headers["Accept-Charset"] = "gb2312";
                request.Headers["Content-Language"] = "zh-cn";
                */

                //create login valid
                string strCredentials = Convert.ToBase64String(Encoding.ASCII.GetBytes(string.Format("{0}:{1}", user, password)));
                //add authorization
                request.Headers.Add("Authorization", "Basic " + strCredentials);

                //url response
                System.Net.HttpWebResponse response = request.GetResponse() as System.Net.HttpWebResponse;

                /*
                Response.Write(Encoding.UTF8.GetString(Encoding.GetEncoding("iso-8859-1").GetBytes(response.Headers["ETag"])));
                
                Response.Write(response.Headers["Last-Modified"]);
                */

                System.IO.Stream stream = response.GetResponseStream();

                System.IO.FileStream fs = new System.IO.FileStream(filename, System.IO.FileMode.Create, System.IO.FileAccess.Write);

                byte[] buffer = new byte[1024];
                int intSize = 0;
                while ((intSize = stream.Read(buffer, 0, buffer.Length)) > 0)
                {
                    fs.Write(buffer, 0, intSize);
                }

                fs.Close();
                stream.Close();
                response.Close();
                request.Abort();
            }
            catch(System.Net.WebException ex)
            {
                if(System.Net.HttpStatusCode.Unauthorized.Equals(((System.Net.HttpWebResponse)ex.Response).StatusCode))
                {
                    errMsg = "用户名密码错误!";
                    intFlag = 1;
                }
                else
                {
                    errMsg = ex.Message;
                    intFlag = 2;
                }
            }
            catch (Exception ex)
            {
                errMsg = ex.Message;
                intFlag = 2;
            }
            return intFlag;
        }

    </script>
</head>
<body><div class="head">Address Book Of Dcjet<%=updateTime %></div>
    <form id="form1" name="form1" method="post" action="address.aspx">
    <p>
        帐号(SVN用户名)<br />
        <input type="text" name="txtSvnUser" id="txtSvnUser" value="<%=this.strSvnUser%>"
            maxlength="20" style="width: 150px" /><br />
        密码(区分大小写)<br />
        <input type="password" name="txtSvnPwd" id="txtSvnPwd" value="<%=this.strSvnPwd%>"
            maxlength="20" style="width: 150px" /><br />
        模糊查询(姓名/手机号/固定电话/海关小号/邮件地址)<br />
        <input type="text" name="txtWhere" id="txtWhere" maxlength="20" value="<%=this.strWhere%>"
            style="width: 150px" /><br />
        <input type="submit" value="查询" onclick="return CheckInput();" style="margin: 5px 0 0 0" /></p>
    </form>
    <%
			if(this.dataRows!=null)
            {
                StringBuilder builder = new StringBuilder();
                bool blnAlternation = false;
                Response.Write("<table>");
                foreach (System.Data.DataRow row in this.dataRows)
                {
                    builder.Length = 0;
                    foreach (object item in row.ItemArray)
                    {
                        string strValue = item.ToString().Trim();
                        {
                            if (item.ToString().IndexOf('@') >= 0)
                                builder.AppendFormat("<td><a href='mailto:{1}'>{1}</a></td>", row[0],strValue);
                            else if(Regex.IsMatch(strValue,@"^\d+$"))
								builder.AppendFormat("<td>{1}</td>", row[0],strValue);
							else
                                builder.AppendFormat("<td>{0}</td>", strValue);
                        }
                    }
                    if(blnAlternation)
                    {
                        Response.Write(string.Format("<tr style='background-color:#dddddd'>{0}</tr>", builder));
                        blnAlternation = false;
                    }
                    else
                    {
                        Response.Write(string.Format("<tr style='background-color:#fdf5e6'>{0}</tr>", builder));
                        blnAlternation = true;
                    }
                }
                Response.Write("</table>");
            }
    %>
    <hr style="color:#005EAC;height: 2px;" /><center><span style="color:blue">CopyRight © 2011 <a href="http://www.dcjet.com.cn">神州数码捷通科技有限公司 </a></span><br /><span style="color:blue">mail: <a href="mailto:qblong@dcjet.com.cn">qblong@dcjet.com.cn </a></span></center></body></html>
