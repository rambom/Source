// MPortServer.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include<locale.h>
#include <stdio.h>
#include <WINSOCK2.H>
#pragma comment(lib, "ws2_32.lib")
#define MAXCONN 100

int g_nAcceptCount = 0;

typedef struct _SOCKINFO
{
	int MapPort;
	char MapIp[50];
	SOCKET LisSocket;
}SOCKINFO,*PSOCKINFO;

typedef struct _SOCKS
{
	SOCKET MapSocket;
	SOCKET LisSocket;
}SOCKS,*PSOCKS;

bool InitSocket()
{
	WSADATA wsaData;
	if (WSAStartup(MAKEWORD(2,2), &wsaData))
	{
		printf("ERROR:WSAStartup Error.\r\n");
		return false;
	}
	return true;
}


bool SendData(SOCKET s, char *data, int len, bool sendFlag)
{
	char * p = data;
	int i = 0;
	int k = len;
	int ret = 0;
	
	if(len <= 0) return TRUE;
	
	if(sendFlag)
		printf("-----------------request data (bytes:%d)----------------\r",k);
	else		
		printf("-----------------reply data (bytes:%d)------------------\r",k);

	printf("%s \r\n",p);
	while(1)
	{
		ret = send(s,p,k,0);
		if(ret == 0 || ret == SOCKET_ERROR)
		{
			return FALSE;
		}
		i += ret;
		p += ret;
		k -= ret;
		if(i >= len) break;
	}	
	return TRUE;
}

DWORD WINAPI TranslateData(LPVOID pvParam)
{
	SOCKS *LisAndMapSocket = (SOCKS*)pvParam;

	FD_SET fdread;
	FD_ZERO(&fdread);

	FD_SET(LisAndMapSocket->LisSocket, &fdread);
	FD_SET(LisAndMapSocket->MapSocket, &fdread);
	
	char buff[8192];
	int nRecv,nSend;
	while(select(0, &fdread, NULL, NULL, NULL) != SOCKET_ERROR)
	{
		ZeroMemory(buff, sizeof(buff));
		if (FD_ISSET(LisAndMapSocket->LisSocket, &fdread))
		{
			nRecv = recv(LisAndMapSocket->LisSocket, buff, sizeof(buff), 0);
			nSend = SendData(LisAndMapSocket->MapSocket, buff, nRecv, true);
			if (nRecv == SOCKET_ERROR || nSend == false)
			{
				printf("ERROR:Translate listen socket data error.\r\n");
				break;
			}
		} 
		else if (FD_ISSET(LisAndMapSocket->MapSocket, &fdread))
		{
			nRecv = recv(LisAndMapSocket->MapSocket, buff, sizeof(buff), 0);
			nSend = SendData(LisAndMapSocket->LisSocket, buff, nRecv, false);
			if (nRecv == SOCKET_ERROR || nSend == false)
			{
				printf("ERROR:Translate map socket data error.\r\n");
				break;
			}
		}
		FD_ZERO(&fdread);
		FD_SET(LisAndMapSocket->LisSocket, &fdread);
		FD_SET(LisAndMapSocket->MapSocket, &fdread);
	}
	closesocket(LisAndMapSocket->MapSocket);
	closesocket(LisAndMapSocket->LisSocket);
	g_nAcceptCount--;
	return 0;
}

DWORD LisAccept(SOCKINFO si)
{
	DWORD dwResult = 0;
	SOCKS LisAndMapSocket;	//连接成功socket和监听socket
	SOCKINFO sockinfo = si;

	sockaddr_in sinmap;
	sinmap.sin_family = AF_INET;
	sinmap.sin_port = htons(sockinfo.MapPort);
	sinmap.sin_addr.S_un.S_addr = inet_addr(sockinfo.MapIp);
	//如果监听socket没有关闭，则一直accept
	while(sockinfo.LisSocket != INVALID_SOCKET)
	{
		SOCKET mapSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
		SOCKET sAccept = accept(sockinfo.LisSocket, NULL, NULL);
		if (sAccept == INVALID_SOCKET)
			break;
		LisAndMapSocket.LisSocket = sAccept;
		//如果客户端连接没有断开,则一直尝试连接要映射的端口
		do 
		{
			if (connect(mapSocket, (SOCKADDR*)&sinmap, sizeof(sinmap)) == SOCKET_ERROR)
			{
				printf("ERROR:Connect map computer error.\r\n");
				if (sAccept != INVALID_SOCKET)
				{
					//休息2秒，别太累了。
					Sleep(2000);
					continue;
				}
			} else {
				break;
			}
		} while (1);

		LisAndMapSocket.MapSocket = mapSocket;
		CreateThread(NULL, 0, TranslateData, (LPVOID)&LisAndMapSocket, 0, NULL);
		g_nAcceptCount++;
	}
	return dwResult;
}

bool ServiceMain(int MapPort, char* MapIp, int LisPort)
{
	InitSocket();
	SOCKINFO socks;
	//初始化sockaddr
	sockaddr_in sinlis;
	sinlis.sin_family = AF_INET;
	sinlis.sin_port = htons(LisPort);
	sinlis.sin_addr.S_un.S_addr = INADDR_ANY;
	//初始化Socket
	socks.LisSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

	//开始LisSocket操作
	if (bind(socks.LisSocket, (SOCKADDR*)&sinlis, sizeof(sinlis)) == INVALID_SOCKET)
	{
		printf("ERROR:Bind listen socket error.\r\n");
		return false;
	}
	if (listen(socks.LisSocket, MAXCONN) == INVALID_SOCKET)
	{
		printf("ERROR:Listen listen socket error.\r\n");
		return false;
	}
	
	socks.MapPort = MapPort;
	strcpy(socks.MapIp, MapIp);
	//开启MapSocket操作
	LisAccept(socks);
	return true;
}

int main(int argc, char* argv[])
{
	if(argc < 4)
	{
		printf("MPortServer [remote port] [remote ip] [local port] \r\n");
	}
	else
	{
		//映射端口 映射地址 本机监听端口
		ServiceMain(atoi(argv[1]), argv[2], atoi(argv[3]));
	}
	return 0;
}