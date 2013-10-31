using System;
using System.Collections;
using System.Threading;

namespace WinTunnel
{
	/// <summary>
	/// Summary description for ThreadPool.
	/// This is a singleton class.
	/// </summary>
	public class ThreadPool
	{
		private bool continueRun = false ;

		private ArrayList ThreadList = ArrayList.Synchronized( new ArrayList());

		private Logger logger;

		private Queue taskQueue = Queue.Synchronized( new Queue() );
		
		private static ThreadPool m_pool;
		private ThreadPool() //private constructor--to prevent instantiation
		{
			logger = Logger.getInstance();
			logger.debug("ThreadPool object instantiated.");
		}

		public static ThreadPool getInstance()
		{
			if (m_pool == null)
			{
				m_pool = new ThreadPool();
			}
			return m_pool;
		}
		
		public void initialize(int count) //start and launch all the threads
		{
			// Start threads to handle Client Task
			logger.debug("ThreadPool initializing with {0} threads.", count);
			continueRun = true;
			for ( int i = 0 ; i < count ; i++) 
			{
				Thread t = new Thread( new ThreadStart(threadFunc) );
				t.Name = "Worker" + i;
				t.Start();
				ThreadList.Add(t);
			}
		}
    
		private void threadFunc()  
		{
			try
			{
				logger.info("Thread is starting...");
				//Starting of each thread
				while ( continueRun ) 
				{	
					//look in the TaskQueue for work to do
					while ( taskQueue.Count > 0)
					{
						ITask task = (ITask) taskQueue.Dequeue();
						if (task != null)
						{
							logger.debug("Thread is processing {0} ...", task.getName() );
							task.run();
						}
					}
					
					lock(m_pool)
					{
						Monitor.Wait(m_pool);
					}
				}
			}
			catch (Exception e)
			{
				logger.error("Thread has encountered exception: {0} ", e.StackTrace);
			}
			finally
			{
				ThreadList.Remove(Thread.CurrentThread);
				logger.info("Thread is terminating ...");
			}
		}
		
    
		public  void Stop()  //Shutdown each thread
		{
			continueRun = false ; 
			logger.info("ThreadPool: Stopping {0} threads...", ThreadList.Count);
			logger.info("Task Queue currently contains {0} tasks.", taskQueue.Count);
			lock(m_pool)
			{
				Monitor.PulseAll(m_pool); //wake up all the thread
			}
			logger.info("Signalled all threads to exit.");
			while (ThreadList.Count > 0) Thread.Sleep(1000);
		}  

		public bool addTask(ITask newTask)
		{
			//add a task to the Queue
			taskQueue.Enqueue(newTask);
			lock(m_pool)
			{
				Monitor.Pulse(m_pool); //wake up thread to process this task
			}
			return true;
		}
	} 
}
