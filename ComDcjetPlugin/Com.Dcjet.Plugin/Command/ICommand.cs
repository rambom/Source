using Com.Dcjet.Plugin.Entity;
using Com.Dcjet.Plugin.Helper;
using EnvDTE;
using EnvDTE80;

namespace Com.Dcjet.Plugin.Command
{
    /// <summary>
    /// 命令顶级接口
    /// </summary>
    interface ICommand
    {
        /// <summary>
        /// 设置工具箱
        /// </summary>
        DTEHelper DteHelper { set; }
        /// <summary>
        /// 获得命令呈现状态
        /// </summary>
        /// <returns></returns>
        CommandViewStatus GetStatus();
        /// <summary>
        /// 执行命令
        /// </summary>
        /// <param name="para">自定义参数</param>
        /// <result></result>
        CommandResult Exec(object para);
    }
}
