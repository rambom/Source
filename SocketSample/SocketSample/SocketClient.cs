using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Net.NetworkInformation;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace SocketSample
{
    internal class SocketClient
    {
        #region private properties
        /// <summary>
        /// remote host
        /// </summary>
        private IPAddress ip;

        /// <summary>
        /// remote port
        /// </summary>
        private int port;

        /// <summary>
        /// connect timeout
        /// </summary>
        private int timeout;

        /// <summary>
        /// is connected
        /// </summary>
        private bool _connected;

        /// <summary>
        /// socket client
        /// </summary>
        private volatile TcpClient _client;

        #endregion

        #region constructor

        /// <summary>
        /// constructor 1 default timeout with equals 1 seconds
        /// </summary>
        /// <param name="ip">remote ip</param>
        /// <param name="port">remote port</param>
        public SocketClient(IPAddress ip, int port)
            : this(ip, port, 1000)
        {
        }

        /// <summary>
        /// constructor 2
        /// </summary>
        /// <param name="ip">remote ip</param>
        /// <param name="port">remote port</param>
        /// <param name="timeout">connect timeout</param>
        public SocketClient(IPAddress ip, int port, int timeout)
        {
            this.ip = ip;
            this.port = port;
            this.timeout = timeout;
            _connected = false;
        }

        #endregion

        #region private member

        /// <summary>
        /// async connect
        /// </summary>
        /// <param name="asyncResult"></param>
        private static void ConnectedCallBack(object asyncResult)
        {
            SocketConnector sc = (SocketConnector)asyncResult;
            try
            {
                TcpClient tc = new TcpClient(sc._ip, sc._port);
                sc.tcpClient = tc;
                sc.manualEvent.Set();
            }
            catch (Exception e)
            {
                sc.manualEvent.Set();
            }
        }

        #endregion

        #region public member

        /// <summary>
        /// current socket client
        /// </summary>
        public TcpClient client
        {
            get { return _client; }
        }

        /// <summary>
        /// socket connected flag
        /// </summary>
        public bool connected
        {
            get { return _connected; }
        }

        #endregion

        #region public Method

        /// <summary>
        /// if the socket has connected then return true else return false
        /// </summary>
        /// <returns></returns>
        public void Connect()
        {
            _connected = false;
            SocketConnector sc = new SocketConnector(ip, port);
            ThreadPool.QueueUserWorkItem(new WaitCallback(ConnectedCallBack), sc);
            if (sc.manualEvent.WaitOne(timeout, false))
            {
                if (sc.tcpClient != null)
                {
                    _client = sc.tcpClient;
                    _connected = true;
                }
            }
        }

        /// <summary>
        /// close the socket
        /// </summary>
        public void Close()
        {
            if (null != _client && _client.Connected)
            {
                _client.Client.Shutdown(SocketShutdown.Both);
                _client.Client.Close();
                _client.Close();
            }
        }

        /// <summary>
        /// send packets to host
        /// </summary>
        /// <param name="strContent">send character</param>
        /// <param name="contentEncode">encode of content</param> 
        /// <param name="autoClose">after send completed then close socket</param>
        /// <param name="errMsg">error message of send failed</param>
        public void Send(string strContent, Encoding contentEncode, bool autoClose, out string errMsg)
        {
            errMsg = null;
            try
            {
                if (null != _client && _client.Connected)
                {
                    _client.Client.Send(contentEncode.GetBytes(strContent));
                    if (autoClose)
                    {
                        Close();
                    }
                }
            }
            catch (Exception ex)
            {
                errMsg = ex.Message;
            }
        }

        /// <summary>
        /// check the host can receive ping packets
        /// </summary>
        /// <param name="ip">host ip</param>
        /// <returns></returns>
        public static bool IsCanPing(string ip)
        {
            Ping ping = new Ping();
            PingReply pingReply = ping.Send(ip, 1);
            if (null != pingReply && "TimedOut".Equals(pingReply.Status.ToString(), StringComparison.CurrentCultureIgnoreCase))
            {
                return false;
            }
            return true;
        }
        #endregion

        #region class

        /// <summary>
        /// internal socket connector
        /// </summary>
        private class SocketConnector
        {
            /// <summary>
            /// connect ip
            /// </summary>
            internal readonly string _ip;
            /// <summary>
            /// conntect port
            /// </summary>
            internal readonly int _port;
            /// <summary>
            /// thread manual notice event
            /// </summary>
            internal readonly ManualResetEvent manualEvent = new ManualResetEvent(false);
            /// <summary>
            /// socket client
            /// </summary>
            internal volatile TcpClient tcpClient;

            /// <summary>
            /// constructor
            /// </summary>
            /// <param name="ip">host</param>
            /// <param name="port">port</param>
            public SocketConnector(IPAddress ip, int port)
            {
                _ip = ip.ToString();
                _port = port;
            }
        }

        #endregion
    }
}
