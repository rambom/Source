using System;
using System.Collections.Generic;
using System.Net.NetworkInformation;
using System.Runtime.InteropServices;
using System.Text;
using Microsoft.Win32;

namespace Hooyee.Utils
{
    class NetCard
    {
        static void Main(string[] args)
        {
            Console.WriteLine(GetMacAddr());
            Console.ReadLine();
        }

        public static string GetMacAddr()
        {
            StringBuilder sbMac = new StringBuilder();
            foreach (var netcard in NetworkInterface.GetAllNetworkInterfaces())
            {
                string strTmp = string.Empty;
                strTmp = GetPhysicalAddr(netcard.Id);
                if (!string.IsNullOrEmpty(strTmp))
                {
                    sbMac.AppendFormat("||{0}", strTmp);
                }
            }
            ;
            return sbMac.Remove(0, 2).ToString();
        }

        private static string GetPhysicalAddr(string cardId)
        {
            string macAddress = string.Empty;
            uint device = 0;
            try
            {
                string driveName = "\\\\.\\" + cardId;
                device = Win32Api.CreateFile(driveName,
                                         Win32Api.GENERIC_READ | Win32Api.GENERIC_WRITE,
                                         Win32Api.FILE_SHARE_READ | Win32Api.FILE_SHARE_WRITE,
                                         0, Win32Api.OPEN_EXISTING, 0, 0);
                if (device != Win32Api.INVALID_HANDLE_VALUE)
                {
                    byte[] outBuff = new byte[6];
                    uint bytRv = 0;
                    int intBuff = Win32Api.PERMANENT_ADDRESS;

                    if (0 != Win32Api.DeviceIoControl(device, Win32Api.IOCTL_NDIS_QUERY_GLOBAL_STATS,
                                        ref intBuff, 4, outBuff, 6, ref bytRv, 0))
                    {
                        string temp = string.Empty;
                        foreach (byte b in outBuff)
                        {
                            temp = Convert.ToString(b, 16).PadLeft(2, '0');
                            macAddress += "-" + temp;
                            temp = string.Empty;
                        }
                        if (!string.IsNullOrEmpty(macAddress))
                            macAddress = macAddress.TrimStart('-');
                    }
                }
            }
            finally
            {
                if (device != 0)
                {
                    Win32Api.CloseHandle(device);
                }
            }

            return macAddress;
        }
    }

    public class Win32Api
    {
        public const uint GENERIC_READ = 0x80000000;
        public const uint GENERIC_WRITE = 0x40000000;
        public const uint FILE_SHARE_READ = 0x00000001;
        public const uint FILE_SHARE_WRITE = 0x00000002;
        public const uint OPEN_EXISTING = 3;
        public const uint INVALID_HANDLE_VALUE = 0xffffffff;
        public const uint IOCTL_NDIS_QUERY_GLOBAL_STATS = 0x00170002;
        public const int PERMANENT_ADDRESS = 0x01010101;

        [DllImport("kernel32.dll")]
        public static extern int CloseHandle(uint hObject);

        [DllImport("kernel32.dll")]
        public static extern int DeviceIoControl(uint hDevice,
                                                  uint dwIoControlCode,
                                                  ref int lpInBuffer,
                                                  int nInBufferSize,
                                                  byte[] lpOutBuffer,
                                                  int nOutBufferSize,
                                                  ref uint lpbytesReturned,
                                                  int lpOverlapped);

        [DllImport("kernel32.dll")]
        public static extern uint CreateFile(string lpFileName,
                                              uint dwDesiredAccess,
                                              uint dwShareMode,
                                              int lpSecurityAttributes,
                                              uint dwCreationDisposition,
                                              uint dwFlagsAndAttributes,
                                              int hTemplateFile);
    }
}
