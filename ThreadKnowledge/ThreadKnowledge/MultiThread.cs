using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.Threading;

namespace ThreadKnowledge
{
    public class MultiThread
    {
        private Thread thread1;
        private Thread thread2;
        private event EventHandler StopThread;
        private object lockObj = new object();
        private bool blnExit = false;
        private int count = 0;

        public MultiThread()
        {
            thread1 = new Thread(new ThreadStart(Run));
            thread2 = new Thread(new ThreadStart(Run));
            thread1.Name = "线程1";
            thread2.Name = "线程2";
            StopThread += new EventHandler(Thread_Clear);
            thread1.Start();
            thread2.Start();
        }

        private void Run()
        {
            while (true)
            {
                Console.WriteLine("{0}:{1}:等待", Thread.CurrentThread.Name, count);
                lock (lockObj)
                {
                    Console.WriteLine("{0}:{1}:执行", Thread.CurrentThread.Name, count);
                    if (blnExit)
                    {
                        StopThread(this, new EventArgs());
                    }
                    count++;
                    if (count > 3)
                        blnExit = true;
                    Console.WriteLine("{0}:{1}:完毕\n", Thread.CurrentThread.Name, count);
                }
                Thread.Sleep(1000);
            }
        }

        void Thread_Clear(object sender, EventArgs e)
        {
            Console.WriteLine("停止所有线程。");
            thread1.Abort();
            thread2.Abort();
        }
    }
}
