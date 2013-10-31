<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Default.aspx.cs" Inherits="SwfUploadDemo._Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head id="Head1" runat="server">
    <title>SWFUpload Application Demo (ASP.Net 2.0)</title>
    <link href="css/default.css" rel="stylesheet" type="text/css" />

    <script type="text/javascript" src="js/swfupload.js"></script>

    <script type="text/javascript" src="js/handlers.js" charset="gb2312"></script>

    <script type="text/javascript">
        var swfu;
        window.onload = function() {
            var uploadpage = "default.aspx";

            swfu = new SWFUpload({
                // Backend Settings
                swfupload_loaded_handler: swfUploadLoaded,
                upload_url: uploadpage,
                post_params: {
                    "ASPSESSID": "<%=Session.SessionID %>"
                },

                // File Upload Settings
                file_size_limit: "1024 MB",
                file_types: "*.*",
                file_types_description: "All Files",
                file_upload_limit: "0",    // Zero means unlimited

                // Event Handler Settings - these functions as defined in Handlers.js
                //  The handlers are not part of SWFUpload but are part of my website and control how
                //  my website reacts to the SWFUpload events.
                file_queued_handler: fileQueued,
                file_queue_error_handler: fileQueueError,

                file_dialog_start_handler: fileDialogStart,
                file_dialog_complete_handler: fileDialogComplete,

                upload_progress_handler: uploadProgress,
                upload_error_handler: uploadError,
                upload_success_handler: uploadSuccess,
                upload_complete_handler: uploadComplete,

                // Button settings
                button_image_url: "images/XPButtonUploadText_61x22.png",
                button_placeholder_id: "spanButtonPlaceholder",
                button_width: 61,
                button_height: 22,
                //button_action : SWFUpload.BUTTON_ACTION.SELECT_FILE,	
                button_text: "浏 览",
                //button_text_style : '.button { font-family: Helvetica, Arial, sans-serif; font-size: 14pt;} .buttonSmall { font-size: 10pt;}',
                //button_text_top_padding: 1,
                button_text_left_padding: 15,

                // Flash Settings
                flash_url: "js/swfupload.swf", // Relative to this file

                custom_settings: {
                    upload_target: "divFileProgressContainer",
                    upload_cancel: false
                },

                // Debug Settings
                debug: false
            });
        }
        function test() {
            alert('您好！'); 
        }
    </script>
</head>
<body>
    <form id="form1" runat="server" enctype="multipart/form-data" method="post">
    <div id="content">
        <div class="fieldset">
            <span class="legend">文件上传</span>
            <table style="vertical-align: top;">
                <tr>
                    <td>
                        <label for="txtFileName">
                            文件:</label>
                    </td>
                    <td>
                        <div>
                            <div>
                                <input type="text" id="txtFileName" disabled="true" style="border: solid 1px; background-color: #FFFFFF;" />
                                <span id="spanButtonPlaceholder"></span>
                            </div>
                            <div class="flash" id="divFileProgressContainer">
                            </div>
                            <textarea id="txtContent"></textarea>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                    </td>
                    <td>
                        <input type="submit" value="上传" id="btnImportFile" />
                    </td>
                </tr>
            </table>
        </div>
    </div>
    </form>
</body>
</html>
