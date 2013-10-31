using System;
using System.Collections.Generic;
using System.Text;

namespace ThreadKnowledge
{
    class Program
    {
        static void Main(string[] args)
        {
            //new ForegroundThread(2);//创建前台线程
            //BackgroundThread backThread = new BackgroundThread(3); //创建后台线程

            //backThread.Stop();//阻塞主线程,直到backThread执行完毕

            new MultiThread();//线程同步
            GC.Collect();
        }
    }
}
