using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;

namespace ThreadKnowledge
{
    internal class BackgroundThread
    {
        private Thread thread;

        public BackgroundThread(int count)
        {
            thread = new Thread(new ParameterizedThreadStart(this.Run));
            thread.IsBackground = true;
            thread.Name = typeof(BackgroundThread).Name;
            thread.Start(count);
        }

        /// <summary>
        /// 线程主方法
        /// </summary>
        /// <param name="obj"></param>
        private void Run(object obj)
        {
            if (obj is int)
            {
                int intCount = (int)obj;
                for (int i = 0; i < intCount; i++)
                {
                    Console.WriteLine("后台线程:{0}\n", i);
                    //停顿1秒
                    Thread.Sleep(1000);
                }
                Console.WriteLine("后台线程:({0})结束!", thread.Name);
            }
        }

        /// <summary>
        /// 停止线程
        /// </summary>
        internal void Stop()
        {
            thread.Join();
        }
    }
}
