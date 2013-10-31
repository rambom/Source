package com.ethan.mlife;

import java.util.Stack;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.ethan.mlife.util.DatabaseHelper;
import com.ethan.mlife.util.HttpRequestClient;

public class MLifeApp extends Application {

	private static SQLiteOpenHelper sqliteHelper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this;
		sqliteHelper = new DatabaseHelper(this);
	}

	private static MLifeApp context;
	private static Stack<Activity> stackActivity;

	public static MLifeApp getContext() {
		return context;
	}

	/**
	 * 返回数据库操作对象
	 * 
	 * @return
	 */
	public static SQLiteOpenHelper getSqliteHelper() {
		return sqliteHelper;
	}

	/**
	 * 弹出activity
	 * 
	 * @param activity
	 */
	public void popActivity(Activity activity) {
		if (null != activity && !stackActivity.isEmpty()) {
			activity.finish();
			stackActivity.remove(activity);
			activity = null;
		}
	}

	/**
	 * 压入activity
	 * 
	 * @param activity
	 */
	public void pushActivity(Activity activity) {
		if (null == stackActivity)
			stackActivity = new Stack<Activity>();
		stackActivity.push(activity);
	}

	/**
	 * 返回指定Activity
	 * 
	 * @param cla
	 */
	public void returnToActivity(Class cla) {
		if (null != stackActivity) {
			while (!stackActivity.isEmpty()) {
				Activity activity = stackActivity.lastElement();
				if (activity.getClass().equals(cla))
					break;
				activity.finish();
				stackActivity.remove(activity);
			}
		}
	}

	/**
	 * 退出应用程序
	 */
	public void exitApplication() {
		if (null != stackActivity) {
			while (!stackActivity.isEmpty()) {
				Activity activity = stackActivity.lastElement();
				activity.finish();
				stackActivity.remove(activity);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		if (HttpRequestClient.initFlag) {
			HttpRequestClient.getHttpRequestClient()
					.shutdownHttpRequestClient();
		}
	}
}
