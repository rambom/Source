using System;
using System.Collections.Generic;
using System.Text;
using System.Net.Sockets;
using System.IO;

namespace CrackEmail
{
    /**/
    /// <summary>
    /// Pop3 的摘要说明。
    /// </summary>
    public class MyMail
    {
        private string _host = null;
        private int _port = 110;
        private TcpClient _client = null;
        private NetworkStream _netStream = null;
        private StreamReader _streamReader = null;
        private string _statusMsg = null;


        #region Constructor

        public MyMail()
        {
        }


        public MyMail(string host)
        {
            _host = host;
        }

        #endregion

        #region private member


        private void SendCommandToNetStream(ref NetworkStream netStream, String command)
        {
            string strToSend = command + "\r\n";
            byte[] arrayToSend = Encoding.ASCII.GetBytes(strToSend.ToCharArray());
            netStream.Write(arrayToSend, 0, arrayToSend.Length);
        }

        private bool CheckCorrect(string message)
        {
            if (message.IndexOf("+OK") == -1)
                return false;
            else
                return true;
        }

        private string ExecuteCommand(string command)
        {
            string strMessage = null;

            try
            {
                //发送命令
                SendCommandToNetStream(ref _netStream, command);

                strMessage = _streamReader.ReadLine();

                //命令执行是否成功
                if (CheckCorrect(strMessage))
                    return strMessage;
                else
                    return "Error";
            }
            catch (IOException exc)
            {
                throw new Exception(exc.ToString());
            }
        }


        private string User(string user)
        {
            return ExecuteCommand("USER " + user);
        }

        private string Pass(string password)
        {
            return ExecuteCommand("PASS " + password);
        }


        private void Quit()
        {
            SendCommandToNetStream(ref _netStream, "QUIT");
        }

        #endregion

        #region public member

        public bool Connect()
        {
            if (_host == null)
                throw new Exception("请提供SMTP主机名称或IP地址！");

            try
            {
                _client = new TcpClient(_host, _port);
                _netStream = _client.GetStream();
                _streamReader = new StreamReader(_client.GetStream());

                string strMessage = _streamReader.ReadLine();
                if (CheckCorrect(strMessage) == true)
                    return true;
                else
                    return false;
            }
            catch (SocketException exc)
            {
                throw new Exception(exc.Message);
            }
            catch (NullReferenceException exc)
            {
                throw new Exception(exc.Message);
            }
        }


        public int ValidateLogin(string user, string password)
        {
            if (User(user).Equals("Error"))
                return 1;

            if (Pass(password).Equals("Error"))
                return 2;

            return 0;

        }

        public void DisConnect()
        {
            try
            {
                Quit();
                if (_streamReader != null)
                    _streamReader.Close();
                if (_netStream != null)
                    _netStream.Close();
                if (_client != null)
                    _client.Close();
            }
            catch (SocketException exc)
            {
                throw new Exception(exc.ToString());
            }
        }

        #endregion


    }
}
