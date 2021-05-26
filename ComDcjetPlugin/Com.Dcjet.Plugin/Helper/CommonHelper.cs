using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Com.Dcjet.Plugin.Entity;
using EnvDTE;

namespace Com.Dcjet.Plugin.Helper
{
    /// <summary>
    /// 工具类
    /// </summary>
    internal class CommonHelper
    {
        /// <summary>
        /// 转换命令状态
        /// </summary>
        /// <param name="cmdViewStatus"></param>
        /// <returns></returns>
        public static vsCommandStatus Convert2VsCmdStatus(CommandViewStatus cmdViewStatus)
        {
            vsCommandStatus status = vsCommandStatus.vsCommandStatusSupported; ;

            switch (cmdViewStatus)
            {
                case CommandViewStatus.Enabled:
                    status |= vsCommandStatus.vsCommandStatusEnabled;
                    break;
                case CommandViewStatus.Disabled:
                    status = vsCommandStatus.vsCommandStatusSupported;
                    break;
                case CommandViewStatus.Visible:
                    status |= vsCommandStatus.vsCommandStatusEnabled;
                    break;
                case CommandViewStatus.Invisible:
                    status |= vsCommandStatus.vsCommandStatusInvisible;
                    break;
                default:
                    status = vsCommandStatus.vsCommandStatusInvisible;
                    break;
            }
            return status;
        }
    }
}
