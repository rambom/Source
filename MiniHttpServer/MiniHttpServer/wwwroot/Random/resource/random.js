String.prototype.trim=function(){return this.replace(/^\s*/,"").replace(/\s*$/,"");}
var RecaptchaOptions={theme:'white'};function htmlEntityDecode(str){var ta=document.createElement("textarea");ta.innerHTML=str.replace(/</g,"&lt;").replace(/>/g,"&gt;");return ta.value;}
function getCheckedValue(radioObj){if(!radioObj)
return "";var radioLength=radioObj.length;if(radioLength==undefined)
if(radioObj.checked)
return radioObj.value;else
return "";for(var i=0;i<radioLength;i++){if(radioObj[i].checked){return radioObj[i].value;}}
return "";}
function setCheckedValue(radioObj,newValue){if(!radioObj)
return;var radioLength=radioObj.length;if(radioLength==undefined){radioObj.checked=(radioObj.value==newValue.toString());return;}
for(var i=0;i<radioLength;i++){radioObj[i].checked=false;if(radioObj[i].value==newValue.toString()){radioObj[i].checked=true;}}}
function openPopup(url){popupWin=window.open(url,'target01_window','status,scrollbars,resizable,dependent,width=500,height=600')}
function setReload(obj){var newUrl;newUrl=location.protocol+"//"+location.host;if(location.port!=""){newUrl=newURL+":"+location.port;}
newUrl=newUrl+location.pathname;if(location.hash!=""){newUrl=newURL+":"+location.hash;}
if(obj.value!=""){newUrl=newUrl+"?reload="+obj.value;}
location.replace(newUrl);}
function pad(number,length){var str=''+number;while(str.length<length)
str='0'+str;return str;}
function trimString(s){return s.replace(/\s+/g,"").replace(/,/g,"");}
function Comma(SS){var T="",S=String(SS),L=S.length-1,C,j;for(j=0;j<=L;j++){T+=(C=S.charAt(j));if((j<L)&&((L-j)%3==0)&&(C!="-")){T+=",";}}
return T;}
function asMoney(d,s)
{if(isNaN(d)){return("unknown");}else{var parts=d.toFixed(2).split(".");return((parts[0]<0?"-":"")+s+Comma(Math.abs(parts[0]))+"."+parts[1]);}}
function handleEnter(field,event){var keyCode=event.keyCode?event.keyCode:event.which?event.which:event.charCode;if(keyCode==13){var i;for(i=0;i<field.form.elements.length;i++)
if(field==field.form.elements[i])
break;i=(i+1)%field.form.elements.length;field.form.elements[i].focus();return false;}else return true;}
function updateGeneratorForm(){var myrnd=document.getElementsByName("rnd");if(myrnd.length!=3){alert("Problem: myrnd.length was "+myrnd.length+", not 3");}
var mydate=document.getElementById("date");var myid=document.getElementById("id");if(myrnd[0].checked){mydate.disabled=true;myid.disabled=true;}else if(myrnd[1].checked){mydate.disabled=false;myid.disabled=true;}else if(myrnd[2].checked){mydate.disabled=true;myid.disabled=false;}}
function submitGeneratorForm(){var myrnd0=document.getElementsByName("rnd")[0];var myrnd1=document.getElementsByName("rnd")[1];var myrnd2=document.getElementsByName("rnd")[2];var mydate=document.getElementById("date");var myid=document.getElementById("id");myrnd0.value="new";myrnd1.value="date."+mydate.options[mydate.selectedIndex].value.trim();myrnd2.value="id."+myid.value.trim();}
function createCookie(name,value,path,days){if(!path)path='/';if(days){var date=new Date();date.setTime(date.getTime()+(days*24*60*60*1000));var expires="; expires="+date.toGMTString();}
else var expires="";document.cookie=name+"="+encodeURIComponent(value)+expires+";domain=.random.org;path="+path;}
function readCookie(name){var nameEQ=name+"=";var ca=document.cookie.split(';');for(var i=0;i<ca.length;i++){var c=ca[i];while(c.charAt(0)==' ')c=c.substring(1,c.length);if(c.indexOf(nameEQ)==0)return decodeURIComponent(c.substring(nameEQ.length,c.length));}
return null;}
function eraseCookie(name,path){createCookie(name,"",path,-1);}