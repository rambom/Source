@echo off
if exist send.log del send.log
for /l %%a in (193,1,235) do (
	SocketSample ip=192.168.10.%%a port=8883 file=gatherinfo.xml >> send.log;
)