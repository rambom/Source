@echo   off

REM /************预处理部分************/

set pwdLogFileName=pwdLog.txt

set exeLogFileName=exeLog.txt

set exeListFileName=exeList.txt

set pwdListFileName=pwdList.txt

setlocal enabledelayedexpansion

REM /************预处理结束************/


title 局域网管理工具(小比尔制作:171882044)
color 8e
:begin
  cls
  echo ╭───────────────────────────╮
  echo │　　　　　　　　　　　　　　　　　　　　　　　　　　　│
  echo │　　　　　　       局域网辅助菜单　              　   │
  echo │　　********************************************　　　│
  echo │　　　　　　 　     　　                              │
  echo │　　　　　　　　　 1:手工批量修改           　　　  　│
  echo │　　　　　　       2:文件批量修改                     │
  echo │　　　　　　　　　 3:查看修改结果                     │
  echo │　　　　　　       4:手工批量执行                     │
　echo │            　　   5:文件批量执行                     │
  echo │　　　　　　 　    6:查看执行结果                     │
  echo │　　　　　　　　　 0:退出                             │
  echo │　　　　　　       　　　　　　　　　　　　　　　　 　│
  echo │　　　　　　　　　　　　　　　　　　　　　　　　　　　│
  echo ╰───────────────────────────╯
  echo.
  set choice=
  set /p choice=请选择(0~1):
  if /i "%choice%"=="1" goto changPwd
  if /i "%choice%"=="2" goto pwdListDetail
  if /i "%choice%"=="3" goto showPwdFile
  if /i "%choice%"=="4" goto execFile
  if /i "%choice%"=="5" goto exeListDetail
  if /i "%choice%"=="6" goto showExecFile
  if /i "%choice%"=="0" goto end
  echo.
  goto begin


  :changPwd
   set /p beginAddress=起始地址(如:192.168.0.1):
   set /p endAddress=结束地址(如:254):
   set /p rootAdmin=有权登陆用户:
   set /p rootPwd=有权登陆密码:
   set /p newUser=要修改的用户:
   set /p newPwd=要修改的密码:
   goto pwdDetail


  :pwdDetail 
   cls
   ECHO 修改密码网段: %beginAddress% ~ %endAddress%
   ECHO 有权登陆用户：%rootAdmin%
   ECHO 有权登陆密码：%rootPwd%
   ECHO 要修改的用户：%newUser%
   ECHO 要修改的密码：%newPwd%
   set /p choice=确定(Y/N):
   if /i "%choice%"=="Y" goto pwdOkay
   if /i "%choice%"=="N" goto begin
   echo.
   goto pwdDetail


  :pwdOkay
   cls
   ECHO 天这么热先吃个雪糕吧,俺正在为您批量修改密码...
   ECHO OFF
   if exist "%pwdLogFileName%" del %pwdLogFileName%  
   for /F "tokens=1,2,3,4 delims=. " %%e in ('echo %beginAddress%') do set beginAddress=%%e.%%f.%%g&& set beginAdd=%%h 
   for /L %%e in (%beginAdd%,1,%endAddress%) do ( 
   cls 
   echo.    
   echo 正在修改的Ip:%beginAddress%.%%e
   echo Ip:%beginAddress%.%%e:>>%pwdLogFileName%
   pwd \\%beginAddress%.%%e -u %rootAdmin% -p %rootPwd% %newUser% %newPwd%>>%pwdLogFileName%
   if ERRORLEVEL 0 (echo 修改成功!>>%pwdLogFileName%) else (echo Ip连接失败或登陆的用户名密码错误或要更改的用户名不存在!>>%pwdLogFileName%)
   echo *****************************************************************>>%pwdLogFileName%
   )
   echo.
   echo.
   echo 报告,执行完毕!
   echo.
   pause    
   goto begin  
 

  :showPwdFile
   echo.
   if not exist "%pwdLogFileName%" (echo 很抱歉,文件不存在!) else (explorer %pwdLogFileName%)
   echo.
   pause  
   goto begin


  :execFile
   set /p beginAddress=起始地址(如:192.168.0.1):
   set /p endAddress=结束地址(如:254):
   set /p rootAdmin=有权登陆用户:
   set /p rootPwd=有权登陆密码:
   set /p filePath=要上传并执行的文件路径(如:c:\abc.exe):
   if not exist "%filePath%" echo 文件不存在! & pause & goto begin
   goto execDetail
   

   :execDetail
    cls
    ECHO 执行文件网段: %beginAddress% ~ %endAddress%
    ECHO 有权登陆用户：%rootAdmin%
    ECHO 有权登陆密码：%rootPwd%
    ECHO 本地文件路径：%filePath%
    set /p choice=确定(Y/N):
    if /i "%choice%"=="Y" goto execOkay
    if /i "%choice%"=="N" goto begin
    echo.
    goto execDetail


  :execOkay
   cls
   ECHO 坐下来喝怀咖啡吧,正在上传并执行文件...
   if /i exist "%exeLogFileName%" del %exeLogFileName%
   for /F "tokens=1,2,3,4 delims=. " %%e in ('echo %beginAddress%') do set beginAddress=%%e.%%f.%%g&& set beginAdd=%%h 
   for /L %%e in (%beginAdd%,1,%endAddress%) do (cls
   echo 正在执行的Ip:%beginAddress%.%%e 
   echo Ip:%beginAddress%.%%e:>>%exeLogFileName%   
   call :subexe %beginAddress%.%%e %rootAdmin% %rootPwd% %filePath% %exeLogFileName%
   echo *****************************************************************>>%exeLogFileName%
   )
   echo.
   echo.
   echo 哦也,搞定了!
   echo.
   pause    
   goto begin  



  :showExecFile
   echo.
   if not exist "%exeLogFileName%" (echo 很抱歉,文件不存在!) else (explorer %exeLogFileName%)
   echo.
   pause
   goto begin

  
  :pwdListDetail
   set /p pwdListFileName=请输入文件路径(如:c:\pwdList.txt):
   if not exist "%pwdListFileName%" echo 文件不存在! & pause & goto begin
   cls
   for /F "eol=* tokens=*" %%e in (%pwdListFileName%) do echo %%e
   set /p choice=确定(Y/N):
   if /i "%choice%"=="Y" goto pwdListOkay
   if /i "%choice%"=="N" goto begin


  :pwdListOkay
   if exist "%pwdLogFileName%" del %pwdLogFileName%
   for /F "eol=* tokens=1,2,3,4,5 delims=,| " %%e in (%pwdListFileName%) do (cls
   echo.    
   echo 正在修改的Ip:%%e
   echo Ip:%%e:>>%pwdLogFileName%
   pwd \\%%e -u %%f -p %%g %%h %%i>>%pwdLogFileName%
   if ERRORLEVEL 0 (echo 修改成功!>>%pwdLogFileName%) else (echo Ip连接失败或登陆的用户名密码错误或要更改的用户名不存在!>>%pwdLogFileName%)
   echo *****************************************************************>>%pwdLogFileName%
   )
   echo.
   echo.
   echo 全部执行完毕!
   echo.
   pause    
   goto begin


  :exeListDetail
   set /p exeListFileName=请输入文件路径(如:c:\exeList.txt):
   if not exist "%exeListFileName%" echo 文件不存在! & pause & goto begin
   cls
   for /F "eol=* tokens=*" %%e in (%exeListFileName%) do echo %%e
   set /p choice=确定(Y/N):
   if /i "%choice%"=="Y" goto exeListOkay
   if /i "%choice%"=="N" goto begin


  :exeListOkay
   cls
   ECHO 坐下来喝怀咖啡吧,正在上传并执行文件...
   if /i exist "%exeLogFileName%" del %exeLogFileName%
   for /F "eol=* tokens=1,2,3,4 delims=,| " %%e in (%exeListFileName%) do (cls
   echo 正在执行的Ip:%%e 
   echo Ip:%%e:>>%exeLogFileName%   
   call :subexe %%e %%f %%g %%h %exeLogFileName%
   echo *****************************************************************>>%exeLogFileName%
   )
   echo.
   echo.
   echo 哦也,搞定了!
   echo.
   pause    
   goto begin  


  
  :end 
   exit  

  :subexe
  pexe \\%1 -u %2 -p %3 -c -f -d "%4"
  if %ERRORLEVEL%==-1 (echo 执行的文件不存在.>>%5) else (if %ERRORLEVEL%==53 (echo 连接到IP失败.>>%5) else (if %ERRORLEVEL%==997 (echo 目标IP未开放admin$共享.>>%5) else (if %ERRORLEVEL%==1326 (echo 用户名或密码错误.>>%5) else (echo 执行成功,进程ID为%ERRORLEVEL%.>>%5)))) 
  goto :eof 