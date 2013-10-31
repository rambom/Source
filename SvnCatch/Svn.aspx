<%@ Page Language="C#" %>

<%@ Import Namespace="System.Collections.Generic" %>
<%@ Import Namespace="System.Collections.ObjectModel" %>
<%@ Import Namespace="System.IO" %>
<%@ Import Namespace="SharpSvn" %>
<%@ Import Namespace="SharpSvn.Security" %>
<html>
<head>
    <title>Svn Catcher</title>
    <style type="text/css">
        div
        {
            display: block;
        }
        body
        {
            font-size: 12px;
            color: #005EAC;
            font-family: 微软雅黑;
        }
        table
        {
            font-family: verdana,arial,sans-serif;
            font-size: 15;
            line-height: 1.7em;
            margin-top: 10px;
            width: 100%;
        }
        td
        {
            color: #00a;
            text-align: center;
            padding: 0.3em;
        }
        .head
        {
            background-color: #005EAC;
            color: #fff;
            font-size: 18px;
            font-weight: bold;
            padding: 10px;
        }
        span
        {
            color: Red;
            font-size: 13px;
            margin: 0px 0px 5px 0px;
        }
        .svnPathInput
        {
            border: 1px solid #CCC;
            border-radius: 3px;
            margin: 5px 0 3px;
            box-shadow: inset 2px 2px 10px 0px rgba(220, 220, 220, 1);
            padding: 5px;
            background: white;
            position: relative;
            display: block;
            width: 80%;
        }
        .svnPathInput textarea
        {
            overflow-x: hidden;
            overflow-y: auto;
            height: 256px;
            font-size: 14px;
            line-height: 18px;
            width: 100%;
            border: 0;
            resize: none;
        }
        #dvBtn span
        {
            border: 0;
            width: 70px;
            height: 28px;
            display: block;
            margin-right: 20px;
            float: left;
            background-image: url(image/bc.png);
            background-position: -70px 0;
            background-repeat: no-repeat;
            text-align: center;
            color: #666;
            font-size: 13px;
            letter-spacing: 5px;
            padding-top: 3px;
            cursor: pointer;
        }
    </style>
    <script language="javascript" type="text/javascript">
        function CheckInput(style) {
            var txtUser = document.getElementById('txtSvnUser');
            var txtPass = document.getElementById('txtSvnPwd');
            var txtPath = document.getElementById('txtPathList');

            if (txtUser.value.replace(/(^\s*)|(\s*$)/g, '') == '') {
                ChangeBackground(txtUser.id, 1, 0);
                return false;
            } else if (txtPass.value.replace(/(^\s*)|(\s*$)/g, '') == '') {
                ChangeBackground(txtPass.id, 1, 0);
                return false;
            } else if (txtPath.value.replace(/(^\s*)|(\s*$)/g, '') == '') {
                ChangeBackground(txtPath.id, 1, 0);
                return false;
            }
            var strMsg = "";

            if (style == "0")
                strMsg = "确认更新?";
            else if (style == "1")
                strMsg = "确认加锁?";
            else if (style == "2")
                strMsg = "确认解锁?";

            if (confirm(strMsg)) {
                var form = document.getElementById('form1');
                form.action = "svn.aspx?style=" + style;
                form.submit();
            }
        };

        function InputListener(obj) {
            var dvBtn = document.getElementById("dvBtn");
            var linkTag = dvBtn.getElementsByTagName("span");
            var bcPosition = "";
            var bcColor = "#000000";
            if (obj.value.replace(/(^\s*)|(\s*$)/g, '') == '') {
                bcPosition = "-70px 0";
                bcColor = "#666";
            } else {
                bcPosition = "-140px 0";
                bcColor = "#ffffff";
            }
            for (var i = 0; i < linkTag.length; i++) {
                linkTag[i].style.backgroundPosition = bcPosition;
                linkTag[i].style.color = bcColor;
            }
        }

        window.onload = function () {
            InputListener(document.getElementById("txtPathList"));
        };

        function ChangeBackground(obj, style, count) {
            count = parseInt(count) + 1;
            var txtObj = document.getElementById(obj);
            if (count < 5) {
                if (style == 1) {
                    txtObj.style.backgroundColor = "#FECCCB";
                    style = 0;
                } else {
                    txtObj.style.backgroundColor = "#FFFFFF";
                    style = 1;
                }
                setTimeout(function () {
                    ChangeBackground(obj, style, count);
                }, 250);
            }
        }
        
    </script>
    <script language="c#" runat="server">

        private string _svnUser;
        private string _svnPass;
        private SvnClient _svnClient;
        private SvnLockArgs lockArgs;
        private SvnUnlockArgs unlockArgs;
        private SvnUpdateArgs updateArgs;
        private StringBuilder _svnLog = new StringBuilder();
        protected void Page_Load(object sender, EventArgs e)
        {
            _svnLog.Length = 0;
            _svnUser = Request.Form["txtSvnUser"];
            _svnPass = Request.Form["txtSvnPwd"];
            string strPathList = Request.Form["txtPathList"];
            string strStyle = Request.QueryString["style"];
            //用户名,密码,Svn路径不能为空
            if (string.IsNullOrEmpty(_svnUser) || string.IsNullOrEmpty(_svnPass) || string.IsNullOrEmpty(strPathList) || string.IsNullOrEmpty(strStyle))
                return;
            _svnPass = _svnPass.Trim();
            _svnUser = _svnUser.Trim();
            strPathList = strPathList.Trim();
            strStyle = strStyle.Trim();
            SvnCommand command = (SvnCommand)Enum.Parse(typeof(SvnCommand), strStyle, true);
            string[] fileArray = Regex.Split(strPathList, "\r\n", RegexOptions.IgnoreCase);
            using (_svnClient = new SvnClient())
            {
                try
                {
                    _svnClient.Authentication.Clear();
                    _svnClient.Authentication.UserNamePasswordHandlers += new EventHandler<SvnUserNamePasswordEventArgs>(delegate(object o, SvnUserNamePasswordEventArgs args) { args.UserName = _svnUser; args.Password = _svnPass; });

                    lockArgs = new SvnLockArgs();
                    lockArgs.SvnError += new EventHandler<SvnErrorEventArgs>(SvnErrorHandler);

                    unlockArgs = new SvnUnlockArgs();
                    unlockArgs.SvnError += new EventHandler<SvnErrorEventArgs>(SvnErrorHandler);

                    updateArgs = new SvnUpdateArgs();
                    updateArgs.SvnError += new EventHandler<SvnErrorEventArgs>(SvnErrorHandler);

                    foreach (string str in fileArray)
                    {
                        string path = str.Trim();
                        if (!string.IsNullOrEmpty(path))
                        {
                            bool blnFlag = true;
                            _svnLog.Append(path);
                            switch (command)
                            {
                                case SvnCommand.update:
                                    blnFlag = _svnClient.Update(path, updateArgs);
				    if (!(File.Exists(path) || Directory.Exists(path)))
			 	    {
					_svnLog.Append("\t路径不存在");
					blnFlag = false;
				    }   
                                    break;
                                case SvnCommand.svnLock:
                                    if (Directory.Exists(path))
                                    {
                                        if (!SvnLock(path))
                                        {
                                            blnFlag = false;
                                        }
                                    }
				    else if (File.Exists(path))
                                    {
                                        blnFlag = _svnClient.Lock(path, lockArgs);
                                    }
				    else
				    {
					_svnLog.Append("\t路径不存在");
					blnFlag=false;
				    }
                                    break;
                                case SvnCommand.unlock:
                                    if (Directory.Exists(path))
                                    {
                                        if (!SvnUnlock(path))
                                        {
                                            blnFlag = false;
                                        }
                                    }
                                    else if (File.Exists(path))
                                    {
                                        blnFlag = _svnClient.Unlock(path, unlockArgs);
                                    }
				    else
				    {
					_svnLog.Append("\t路径不存在");
					blnFlag=false;
				    }
                                    break;
                            }
                            if (blnFlag)
                                _svnLog.Append("\t成功");
                            _svnLog.Append("\r\n");
                        }
                    }
                }
                catch (SvnAuthorizationException exception)
                {
                    Response.Write(string.Format(@"<span>[{0}]</span>", "用户名密码错误"));
                }
            }
        }

        private bool SvnLock(string path)
        {
            bool blnFlag = true;
            foreach (String strFile in Directory.GetFiles(path))
            {
                if (!_svnClient.Lock(strFile, lockArgs))
                {
                    blnFlag = false;
                }
            }

            foreach (string strDir in Directory.GetDirectories(path))
            {
                if (strDir.EndsWith("_svn") || strDir.EndsWith(".svn"))
                    continue;

                blnFlag = SvnLock(strDir) && blnFlag;

            }
            return blnFlag;
        }

        private bool SvnUnlock(string path)
        {
            bool blnFlag = true;
            foreach (String strFile in Directory.GetFiles(path))
            {
                if (!_svnClient.Unlock(strFile, unlockArgs))
                {
                    blnFlag = false;
                }
            }

            foreach (string strDir in Directory.GetDirectories(path))
            {
                if (strDir.EndsWith("_svn") || strDir.EndsWith(".svn"))
                    continue;

                blnFlag = SvnUnlock(strDir) && blnFlag;

            }
            return blnFlag;
        }

        private void SvnErrorHandler(object o, SvnErrorEventArgs args)
        {
            switch (args.Exception.SvnErrorCode)
            {
                case SvnErrorCode.SVN_ERR_FS_PATH_ALREADY_LOCKED:
                    //文件已锁定
                    args.Cancel = true;
                    _svnLog.Append("\t文件已被加锁");
                    break;
                case SvnErrorCode.SVN_ERR_CLIENT_MISSING_LOCK_TOKEN:
                    //文件未锁定
                    args.Cancel = true;
                    _svnLog.Append("\t文件未加锁");
                    break;
                case SvnErrorCode.SVN_ERR_ENTRY_NOT_FOUND:
                    //文件不存在
                    args.Cancel = true;
                    _svnLog.Append("\t非版本控制文件");
                    break;
                case SvnErrorCode.SVN_ERR_FS_OUT_OF_DATE:
                    args.Cancel = true;
                    _svnLog.Append("\t需更新版本");
                    break;
            }
        }

        enum SvnCommand
        {
            update = 0,
            svnLock = 1,
            unlock = 2
        }
    </script>
</head>
<body>
    <div class="head">
        Svn Catcher
    </div>
    <form id="form1" name="form1" method="post">
    <p>
        帐号(SVN用户名)<br />
        <input type="text" tabindex="1" name="txtSvnUser" id="txtSvnUser" value="<%=this._svnUser%>"
            maxlength="20" style="width: 150px" /><br />
        密码(区分大小写)<br />
        <input type="password" tabindex="2" name="txtSvnPwd" id="txtSvnPwd" value="<%=this._svnPass%>"
            maxlength="20" style="width: 150px" />
        <div>
            文件列表(目标svn目录路径)</div>
        <div class="svnPathInput">
            <textarea tabindex="3" title="SvnPath Input" id="txtPathList" name="txtPathList"
                node-type="textEl" style="word-wrap: break-word; line-height: 18px; outline: none;"
                range="0&amp;0" onkeyup="InputListener(this);"><%=this._svnLog %></textarea>
        </div>
        <br />
        <div id="dvBtn">
            <span title="更新" onclick="CheckInput(0);">更新</span><span title="加锁" onclick="CheckInput(1);">加锁</span><span
                title="解锁" onclick="CheckInput(2);">解锁</span>
        </div>
    </p>
    </form>
    <center>
        <div style="width: 100%; position: absolute; margin-top: 100px; left: 0px;">
            <hr style="color: #005EAC; height: 2px;" />
            <span style="color: blue">CopyRight © 2012 <a href="http://www.dcjet.com.cn">神州数码捷通科技有限公司
            </a></span>
            <br />
            <span style="color: blue">mail: <a href="mailto:qblong@dcjet.com.cn">qblong@dcjet.com.cn
            </a></span>
        </div>
    </center>
</body>
</html>
