using System;
using System.Net;
using System.Net.Sockets;

namespace WinTunnel
{
	/// <summary>
	/// Summary description for ProxyDataTask.
	/// </summary>
	public class ProxySwapDataTask: ITask
	{
		ProxyConnection m_conn;
		static Logger logger;

		public ProxySwapDataTask(ProxyConnection conn)
		{
			m_conn = conn;
			logger = Logger.getInstance();
		}
		#region ITask Members

		public void run()
		{
			//validate that both the client side and server side sockets are ok.  If so, do read/write
			if (m_conn.clientSocket == null || m_conn.serverSocket == null)
			{
				logger.error("[{0}] ProxyConnection#{1}--Either client socket or server socket is null.",
							 m_conn.serviceName, m_conn.connNumber);
				m_conn.Release();
				return;
			}

			if (m_conn.clientSocket.Connected && m_conn.serverSocket.Connected)
			{
				//Read data from the client socket
				m_conn.clientSocket.BeginReceive( m_conn.clientReadBuffer, 0, ProxyConnection.BUFFER_SIZE, 0,
					new AsyncCallback(clientReadCallBack), m_conn);
			
				//Read data from the server socket
				m_conn.serverSocket.BeginReceive( m_conn.serverReadBuffer, 0, ProxyConnection.BUFFER_SIZE, 0,
					new AsyncCallback(serverReadCallBack), m_conn);
			}
			else
			{
				logger.error("[{0}] ProxyConnection#{1}: Either the client or server socket got disconnected.", 
					m_conn.serviceName, m_conn.connNumber );
				m_conn.Release();
			}
			m_conn = null;
		}

		public String getName()
		{
			return "ProxySwapDataTask[(#" + m_conn.connNumber + ") "+ m_conn.serverEP.ToString() + " <===> " + m_conn.clientSocket.RemoteEndPoint.ToString() + "]"; 
		}

		#endregion

		private static void clientReadCallBack(IAsyncResult ar)
		{
			ProxyConnection conn = (ProxyConnection) ar.AsyncState;

			int numBytes =0;
			try
			{
				numBytes = conn.clientSocket.EndReceive(ar);
		
				if (numBytes > 0) //write to the server side socket
				{						
					//copy the bytes to the server send buffer and call send
					Array.Copy(conn.clientReadBuffer, 0, conn.serverSendBuffer, 0, numBytes);
					conn.serverNumBytes += numBytes;
					conn.serverSocket.BeginSend( conn.serverSendBuffer, 0, numBytes, 0,
						new AsyncCallback(serverSendCallBack), conn);
				}
				else
				{
					logger.error("[{0}] ProxyConnection#{1}: Detected Client Socket disconnect via read.", 
						conn.serviceName, conn.connNumber );
					conn.Release();
				}
			}	
			catch (SocketException se)
			{
				if (!conn.isShutdown)
				{
					logger.error("[{0}] ProxyConnection#{1}: Socket Error occurred when reading data from the client socket.  Error Code is: {2}.", 
						conn.serviceName, conn.connNumber, se.ErrorCode );
					conn.Release();
				}
			}
			catch (Exception e)
			{
				if (!conn.isShutdown)
				{
					logger.error("[{0}] ProxyConnection#{1}:  Error occurred when reading data from the client socket.  The error is: {2}.", 
						conn.serviceName, conn.connNumber, e );
					conn.Release();
				}
			}
			finally
			{
				conn = null;
			}
		}

		private static void serverReadCallBack(IAsyncResult ar)
		{
			ProxyConnection conn = (ProxyConnection) ar.AsyncState;
			int numBytes = 0;

			try
			{	
				numBytes = conn.serverSocket.EndReceive(ar);
		
				if (numBytes > 0) //write to the client side socket
				{						
					//copy the bytes to the client send buffer and call send
					Array.Copy(conn.serverReadBuffer, 0, conn.clientSendBuffer, 0, numBytes);
					conn.clientNumBytes += numBytes;
					conn.clientSocket.BeginSend( conn.clientSendBuffer, 0, numBytes, 0,
						new AsyncCallback(clientSendCallBack), conn);
				}
				else
				{
					//Server must have disconnected the socket
					logger.error("[{0}] ProxyConnection#{1}: Detected Server Socket disconnect via read.", 
						conn.serviceName, conn.connNumber );
					conn.Release();
				}
			}
			catch (SocketException se)
			{
				if (!conn.isShutdown)
				{
					logger.error("[{0}] ProxyConnection#{1}: Socket Error occurred when reading data from the server socket.  Error Code is: {2}.", 
						conn.serviceName, conn.connNumber, se.ErrorCode );
					conn.Release();
				}
			}
			catch (Exception e)
			{
				if (!conn.isShutdown)
				{
					logger.error("[{0}] ProxyConnection#{1}:  Error occurred when reading data from the server socket.  The error is: {2}.", 
						conn.serviceName, conn.connNumber, e );
					conn.Release();
				}
			}
			finally
			{
				conn = null;
			}
		}

		private static void clientSendCallBack(IAsyncResult ar)
		{
			ProxyConnection conn = (ProxyConnection) ar.AsyncState;
			try
			{
				int numBytes = conn.clientSocket.EndSend(ar);
		
				if (numBytes == conn.clientNumBytes) //read from the server side socket
				{	
					conn.clientNumBytes=0;			
					conn.serverSocket.BeginReceive( conn.serverReadBuffer, 0, ProxyConnection.BUFFER_SIZE, 0,
						new AsyncCallback(serverReadCallBack), conn);
				}
				else
				{
					conn.clientNumBytes -= numBytes;
				}
			}
			catch (SocketException se)
			{
				if (!conn.isShutdown)
				{
					logger.error("[{0}] ProxyConnection#{1}: Socket Error occurred when writing data to the client socket.  Error Code is: {2}.", 
						conn.serviceName, conn.connNumber, se.ErrorCode );
					conn.Release();
				}
			}
			catch (Exception e)
			{
				if (!conn.isShutdown)
				{
					logger.error("[{0}] ProxyConnection#{1}:  Error occurred when writing data to the client socket.  The error is: {2}.", 
						conn.serviceName,conn.connNumber, e );
					conn.Release();
				}
			}
			finally
			{
				conn = null;
			}
		}

		private static void serverSendCallBack(IAsyncResult ar)
		{
			ProxyConnection conn = (ProxyConnection) ar.AsyncState;
			try
			{
				int numBytes = conn.serverSocket.EndSend(ar);
		
				if (numBytes == conn.serverNumBytes) //finished sending the data, now read from client socket again
				{	
					conn.serverNumBytes =0;
					conn.clientSocket.BeginReceive( conn.clientReadBuffer, 0, ProxyConnection.BUFFER_SIZE, 0,
						new AsyncCallback(clientReadCallBack), conn);
				}
				else
				{
					conn.serverNumBytes -= numBytes;
				}
			}	
			catch (SocketException se)
			{
				if (!conn.isShutdown)
				{
					logger.error("[{0}] ProxyConnection#{1}: Socket Error occurred when writing data to the server socket.  Error Code is: {2}.", 
						conn.serviceName, conn.connNumber, se.ErrorCode );
					conn.Release();
				}
			}
			catch (Exception e)
			{
				if (!conn.isShutdown)
				{
					logger.error( "[{0}] ProxyConnection#{1}, conn#{2}:  Error occurred when writing data to the server socket.  The error is: {2}.",
						conn.serviceName, conn.connNumber, e );
					conn.Release();
				}
			}
			finally
			{
				conn = null;
			}
		}
	}
}

