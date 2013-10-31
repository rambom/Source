window.onbeforeunload=function(){

if(swfu.getStats().files_queued > 0)
    return "文件尚未处理完成!";
else
    return;    
}

function swfUploadLoaded() {
	var btnSubmit = document.getElementById("btnImportFile");
	btnSubmit.onclick = doUploadFileSubmit;
}

// Called by the submit button to start the upload
function doUploadFileSubmit(e) {
	e = e || window.event;
	if (e.stopPropagation) {
		e.stopPropagation();
	}
	e.cancelBubble = true;
	
	try {
	    if (swfu.getStats().files_queued > 0)
	    {     
		    swfu.startUpload();		    
		}
		else
		    alert("请您先选择文件!");
	} catch (ex) {

	}
	return false;
}

function fileQueued(file) {
	try {
		var txtFileName = document.getElementById("txtFileName");
		txtFileName.value = file.name;
	} catch (e) {
	}
}


function fileQueueError(file, errorCode, message)  {
	try {
		// Handle this error separately because we don't want to create a FileProgress element for it.
		switch (errorCode) {
		case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
			alert("You have attempted to queue too many files.\n" + (message === 0 ? "You have reached the upload limit." : "You may select " + (message > 1 ? "up to " + message + " files." : "one file.")));
			return;
		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			alert("The file you selected is too big.");
			this.debug("Error Code: File too big, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			return;
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			alert("The file you selected is empty.  Please select another file.");
			this.debug("Error Code: Zero byte file, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			return;
		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
			alert("The file you choose is not an allowed file type.");
			this.debug("Error Code: Invalid File Type, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			return;
		default:
			alert("An error occurred in the upload. Try again later.");
			this.debug("Error Code: " + errorCode + ", File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			return;
		}
	} catch (e) {
	}
}

function fileDialogStart() {
	return false;
}

function fileDialogComplete(numFilesSelected, numFilesQueued) {
	try {
		if (numFilesQueued > 0) {
			//this.startUpload();
		}
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadProgress(file, bytesLoaded) {

	try {	    
	     this.setButtonDisabled(true); 
	     this.customSettings.upload_cancel = false; 
	        
         var percent = Math.ceil((bytesLoaded / file.size) * 100); 

         var progress = new FileProgress(file, this.customSettings.upload_target);

         var strPercent = file.name+"<span class='progressPercent'>"+"["+percent+"%]"+"</span>"
         
         progress.setProgress(percent);
         
         progress.setProgressPercent(strPercent);

         if (percent === 100) {             
              progress.setStatus("上传完毕,正在处理文件......");
              progress.toggleCancel(false, this);

         } 
        else {//显示上传信息   
              progress.setStatus("上传中,请梢后......‖已上传："+file.size+"/"+(bytesLoaded/(1024*1024)));

              progress.toggleCancel(true, this);

         }

     } catch (ex) {

         this.debug(ex);

     }

}

function uploadSuccess(file, serverData) {
	try {
	    this.setButtonDisabled(true);
	    document.write(serverData);
	} catch (e) {
	}
}

function uploadComplete(file) {
	try {
		/*  I want the next upload to continue automatically so I'll call startUpload here */
				
		if (this.getStats().files_queued > 0)
	    {
			    this.startUpload();
		}
		else if(this.customSettings.upload_cancel === true){
			var progress = new FileProgress(file,  this.customSettings.upload_target);
			progress.setCancelled();
			progress.setStatus("已取消上传.");
			progress.toggleCancel(false);
			
			var txtFileName = document.getElementById("txtFileName");
			txtFileName.value = "";
		}		
		else
		{
			var progress = new FileProgress(file,  this.customSettings.upload_target);
			progress.setComplete();
			progress.setStatus("文件上传成功.");
			progress.toggleCancel(false);						
			 
			var txtFileName = document.getElementById("txtFileName");
			txtFileName.value = "";
		}
		
		this.setButtonDisabled(false); 
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadError(file, errorCode, message) {
	try {
		if (errorCode === SWFUpload.UPLOAD_ERROR.FILE_CANCELLED) {
			this.customSettings.upload_cancel = true;		
			return;
		}
		
		var txtFileName = document.getElementById("txtFileName");
		txtFileName.value = "";
		
		// Handle this error separately because we don't want to create a FileProgress element for it.
		switch (errorCode) {
		case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
			alert("There was a configuration error.  You will not be able to upload a resume at this time.");
			this.debug("Error Code: No backend file, File name: " + file.name + ", Message: " + message);
			return;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
			alert("You may only upload 1 file.");
			this.debug("Error Code: Upload Limit Exceeded, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			return;
		case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
		case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
			break;
		default:
			alert("An error occurred in the upload. Try again later.");
			this.debug("Error Code: " + errorCode + ", File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			return;
		}

		file.id = "singlefile";	// This makes it so FileProgress only makes a single UI element, instead of one for each file
		var progress = new FileProgress(file, this.customSettings.progress_target);
		progress.setError();
		progress.toggleCancel(false);

		switch (errorCode) {
		case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
			progress.setStatus("Upload Error");
			this.debug("Error Code: HTTP Error, File name: " + file.name + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
			progress.setStatus("Upload Failed.");
			this.debug("Error Code: Upload Failed, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.IO_ERROR:
			progress.setStatus("Server (IO) Error");
			this.debug("Error Code: IO Error, File name: " + file.name + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
			progress.setStatus("Security Error");
			this.debug("Error Code: Security Error, File name: " + file.name + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
			progress.setStatus("Upload Cancelled");
			this.debug("Error Code: Upload Cancelled, File name: " + file.name + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
			progress.setStatus("Upload Stopped");
			this.debug("Error Code: Upload Stopped, File name: " + file.name + ", Message: " + message);
			break;
		}
	} catch (ex) {
	}
}



/*------------------------------------------------------------------------------------------------------------------------------------*/
function FileProgress(file, targetID) {
	this.fileProgressID = file.id;

	this.opacity = 100;
	this.height = 0;

	this.fileProgressWrapper = document.getElementById(this.fileProgressID);
	if (!this.fileProgressWrapper) {
		this.fileProgressWrapper = document.createElement("div");
		this.fileProgressWrapper.className = "progressWrapper";
		this.fileProgressWrapper.id = this.fileProgressID;

		this.fileProgressElement = document.createElement("div");
		this.fileProgressElement.className = "progressContainer";

		var progressCancel = document.createElement("a");
		progressCancel.className = "progressCancel";
		progressCancel.href = "#";
		progressCancel.style.visibility = "hidden";
		progressCancel.appendChild(document.createTextNode(""));

		var progressText = document.createElement("div");
		progressText.className = "progressName";
		progressText.appendChild(document.createTextNode(file.name));
		
		var progressBar = document.createElement("div");
		progressBar.className = "progressBarInProgress";

		var progressStatus = document.createElement("div");
		progressStatus.className = "progressBarStatus";
		progressStatus.innerHTML = "&nbsp;";

		this.fileProgressElement.appendChild(progressCancel);
		this.fileProgressElement.appendChild(progressText);
		this.fileProgressElement.appendChild(progressStatus);
		this.fileProgressElement.appendChild(progressBar);

		this.fileProgressWrapper.appendChild(this.fileProgressElement);

		document.getElementById(targetID).appendChild(this.fileProgressWrapper);
		fadeIn(this.fileProgressWrapper, 0);
	} else {
		this.fileProgressElement = this.fileProgressWrapper.firstChild;
		this.fileProgressElement.childNodes[1].innerHTML = file.name;
	}

	this.height = this.fileProgressWrapper.offsetHeight;

}
FileProgress.prototype.setProgress = function (percentage) {
	this.fileProgressElement.className = "progressContainer green";
	this.fileProgressElement.childNodes[3].className = "progressBarInProgress";
	this.fileProgressElement.childNodes[3].style.width = percentage + "%";
};
FileProgress.prototype.setComplete = function () {
	this.appear();
	this.fileProgressElement.className = "progressContainer blue";
	this.fileProgressElement.childNodes[3].className = "progressBarComplete";
	this.fileProgressElement.childNodes[3].style.width = "";

	var oSelf = this;
	setTimeout(function () {
		oSelf.disappear();
	}, 100000);
};
FileProgress.prototype.setError = function () {
	this.appear();
	this.fileProgressElement.className = "progressContainer red";
	this.fileProgressElement.childNodes[3].className = "progressBarError";
	this.fileProgressElement.childNodes[3].style.width = "";

	var oSelf = this;
	setTimeout(function () {
		oSelf.disappear();
	}, 10000);
};
FileProgress.prototype.setCancelled = function () {
	this.appear();
	this.fileProgressElement.className = "progressContainer";
	this.fileProgressElement.childNodes[3].className = "progressBarError";
	this.fileProgressElement.childNodes[3].style.width = "";

	var oSelf = this;
	setTimeout(function () {
		oSelf.disappear();
	}, 2000);
};

FileProgress.prototype.setStatus = function (status) {
	this.fileProgressElement.childNodes[2].innerHTML = status;
};

FileProgress.prototype.setProgressPercent = function (progressPercent) {
	this.fileProgressElement.childNodes[1].innerHTML = progressPercent;
};

// Show/Hide the cancel button
FileProgress.prototype.toggleCancel = function (show, swfUploadInstance) {
	this.fileProgressElement.childNodes[0].style.visibility = show ? "visible" : "hidden";
	if (swfUploadInstance) {
		var fileID = this.fileProgressID;
		this.fileProgressElement.childNodes[0].onclick = function () {
		    if(confirm("真的取消上传吗?"))
		    {
			    swfUploadInstance.cancelUpload(fileID,true);			    
			}    
			return false;
		};
	}
};

// Makes sure the FileProgress box is visible
FileProgress.prototype.appear = function () {
		if (this.fileProgressWrapper.filters) {
			try {
				this.fileProgressWrapper.filters.item("DXImageTransform.Microsoft.Alpha").opacity = 100;
			} catch (e) {
				// If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
				this.fileProgressWrapper.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=100)";
			}
		} else {
			this.fileProgressWrapper.style.opacity = 1;
		}
		
		this.fileProgressWrapper.style.height = "";
		this.height = this.fileProgressWrapper.offsetHeight;
		this.opacity = 100;
		this.fileProgressWrapper.style.display = "";

};

// Fades out and clips away the FileProgress box.
FileProgress.prototype.disappear = function () {

	var reduceOpacityBy = 15;
	var reduceHeightBy = 4;
	var rate = 30;	// 15 fps

	if (this.opacity > 0) {
		this.opacity -= reduceOpacityBy;
		if (this.opacity < 0) {
			this.opacity = 0;
		}

		if (this.fileProgressWrapper.filters) {
			try {
				this.fileProgressWrapper.filters.item("DXImageTransform.Microsoft.Alpha").opacity = this.opacity;
			} catch (e) {
				// If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
				this.fileProgressWrapper.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=" + this.opacity + ")";
			}
		} else {
			this.fileProgressWrapper.style.opacity = this.opacity / 100;
		}
	}

	if (this.height > 0) {
		this.height -= reduceHeightBy;
		if (this.height < 0) {
			this.height = 0;
		}

		this.fileProgressWrapper.style.height = this.height + "px";
	}

	if (this.height > 0 || this.opacity > 0) {
		var oSelf = this;
		setTimeout(function () {
			oSelf.disappear();
		}, rate);
	} else {
		this.fileProgressWrapper.style.display = "none";
	}
};

function addImage(src) {
	var newImg = document.createElement("img");
	newImg.style.margin = "5px";

	document.getElementById("thumbnails").appendChild(newImg);
	if (newImg.filters) {
		try {
			newImg.filters.item("DXImageTransform.Microsoft.Alpha").opacity = 0;
		} catch (e) {
			// If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
			newImg.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + 0 + ')';
		}
	} else {
		newImg.style.opacity = 0;
	}

	newImg.onload = function () {
		fadeIn(newImg, 0);
	};
	newImg.src = src;
}

function fadeIn(element, opacity) {
	var reduceOpacityBy = 5;
	var rate = 30;	// 15 fps


	if (opacity < 100) {
		opacity += reduceOpacityBy;
		if (opacity > 100) {
			opacity = 100;
		}

		if (element.filters) {
			try {
				element.filters.item("DXImageTransform.Microsoft.Alpha").opacity = opacity;
			} catch (e) {
				// If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
				element.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + opacity + ')';
			}
		} else {
			element.style.opacity = opacity / 100;
		}
	}

	if (opacity < 100) {
		setTimeout(function () {
			fadeIn(element, opacity);
		}, rate);
	}
}
/*---------------------------------------------------------------------------------------------------------------------------------------------*/