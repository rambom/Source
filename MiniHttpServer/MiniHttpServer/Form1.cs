using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Windows.Forms;
using System.Threading.Tasks;
using System.Configuration;
using MiniHttpServer.Config;
using System.Drawing;
using MiniHttpServer.Entity;

namespace MiniHttpServer
{
    public partial class Form1 : Form
    {
        private NotifyIcon trayIcon;
        private HttpListener listener;
        private IList<DomainConfig> domainConfig = new List<DomainConfig>(100);
        public Form1()
        {
            InitializeComponent();
            // 创建系统托盘图标
            trayIcon = new NotifyIcon
            {
                Text = "MiniHttpServer",
                Icon = new Icon(SystemIcons.Asterisk, 40, 40),
                BalloonTipIcon = ToolTipIcon.Info,
                Visible = true
            };
            this.SizeChanged += OnSizeChanged;
            this.trayIcon.Click += OnShow;
        }
        private void OnShow(object sender, EventArgs e)
        {
            this.Show();
            this.Activate();
            this.WindowState = FormWindowState.Normal;
        }
        protected override void OnFormClosing(FormClosingEventArgs e)
        {
            base.OnFormClosing(e);
            this.RemoveDomainsFromHosts();
            trayIcon.Visible = false;
            Application.Exit();
        }
        private void OnSizeChanged(object sender, EventArgs e)
        {
            if (this.WindowState == FormWindowState.Minimized)
            {
                this.Hide();
            }
        }
        /// <summary>
        /// 加载域名映射
        /// </summary>
        /// <returns></returns>
        private void LoadDomainMapping()
        {
            this.domainConfig.Clear();
            var domainConfig = new List<DomainConfig>(100);
            var domainMappingsSection = (DomainMappingSection)ConfigurationManager.GetSection("DomainMapping");
            if (domainMappingsSection != null)
            {
                foreach (DomainMappingElement mapping in domainMappingsSection.Mapping)
                {
                    this.domainConfig.Add(new DomainConfig()
                    {
                        Domain = mapping.Domain,
                        Path = mapping.Path,
                        Home = mapping.Home
                    });
                }
            }
        }
        /// <summary>
        /// 添加hosts域名映射
        /// </summary>
        private void AddDomainsToHosts()
        {
            string hostsFilePath = @"C:\Windows\System32\drivers\etc\hosts";
            List<string> hostsFileContent = File.ReadAllLines(hostsFilePath).ToList();

            foreach (var config in this.domainConfig)
            {
                string mapping = $"127.0.0.1 {config.Domain}";
                if (!hostsFileContent.Any(line => line.Trim() == mapping))
                {
                    hostsFileContent.Add(mapping);
                }
            }
            File.WriteAllLines(hostsFilePath, hostsFileContent);
        }
        /// <summary>
        /// 移除hosts域名映射
        /// </summary>\
        private void RemoveDomainsFromHosts()
        {
            string hostsFilePath = @"C:\Windows\System32\drivers\etc\hosts";
            List<string> hostsFileLine = File.ReadAllLines(hostsFilePath).ToList();
            List<string> newHostsFileLine = new List<string>(200);
            foreach (var hostLine in hostsFileLine)
            {
                bool exists = false;
                foreach (var config in this.domainConfig)
                {
                    if (hostLine.Trim() == $"127.0.0.1 {config.Domain}")
                    {
                        exists = true;
                        break;
                    }
                }
                if (!exists)
                {
                    newHostsFileLine.Add(hostLine);
                }
            }
            File.WriteAllLines(hostsFilePath, newHostsFileLine);
        }

        private async Task StartHttpListener()
        {
            await Task.Run(async () =>
            {
                while (listener.IsListening)
                {
                    var context = await listener.GetContextAsync();
                    var request = context.Request;
                    var response = context.Response;
                    try
                    {
                        var host = request.Headers["Host"];
                        var requestPath = request.Url.AbsolutePath.TrimStart('/');
                        var config = this.domainConfig.Where(e => e.Domain == host).FirstOrDefault();
                        if (config == null)
                        {
                            HandleNotFound(response);
                            continue;
                        }
                        var filePath = Path.Combine(config.Path, string.IsNullOrWhiteSpace(requestPath) ? config.Home : requestPath);
                        if (!File.Exists(filePath))
                        {
                            HandleNotFound(response);
                            continue;
                        }
                        byte[] fileContent = File.ReadAllBytes(filePath);
                        response.ContentLength64 = fileContent.Length;
                        response.ContentType = GetContentType(Path.GetExtension(filePath));

                        using (var output = response.OutputStream)
                        {
                            await output.WriteAsync(fileContent, 0, fileContent.Length);
                        }
                    }
                    catch (Exception ex)
                    {
                        MessageBox.Show($"错误：{ex.Message}\n");
                    }
                    finally
                    {
                        if (response != null)
                        {
                            response.Close();
                        }
                    }
                }
            });
        }
        private void HandleNotFound(HttpListenerResponse response)
        {
            response.StatusCode = (int)HttpStatusCode.NotFound;
            var notFoundMessage = "<html><body><h1>404 - Not Found</h1></body></html>";
            var notFoundContent = Encoding.UTF8.GetBytes(notFoundMessage);

            using (var output = response.OutputStream)
            {
                output.Write(notFoundContent, 0, notFoundContent.Length);
            }
        }

        private string GetContentType(string extension)
        {
            return extension switch
            {
                ".html" => "text/html",
                ".css" => "text/css",
                ".js" => "application/javascript",
                ".json" => "application/json",
                ".xml" => "application/xml",
                ".pdf" => "application/pdf",
                ".zip" => "application/zip",
                ".txt" => "text/plain",
                ".csv" => "text/csv",
                ".jpg" => "image/jpeg",
                ".jpeg" => "image/jpeg",
                ".png" => "image/png",
                ".gif" => "image/gif",
                _ => "application/octet-stream"
            };
        }

        private void btnStart_Click(object sender, EventArgs e)
        {
            try
            {
                this.LoadDomainMapping();
                this.AddDomainsToHosts();
                var port = ConfigurationManager.AppSettings["serverPort"];
                if (listener == null || !this.listener.IsListening)
                {
                    listener = new HttpListener();
                    listener.Prefixes.Add($"http://*:{(string.IsNullOrWhiteSpace(port) ? "80" : port)}/");
                    listener.Start();
                    this.StartHttpListener();
                    this.btnStart.Enabled = false;
                    this.btnStop.Enabled = true;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"启动错误：{ex}");
            }
        }

        private void btnStop_Click(object sender, EventArgs e)
        {
            this.RemoveDomainsFromHosts();
            if (listener != null && this.listener.IsListening)
            {
                listener.Stop();
                listener.Close();
                this.btnStart.Enabled = true;
                this.btnStop.Enabled = false;
            }
        }
    }
}