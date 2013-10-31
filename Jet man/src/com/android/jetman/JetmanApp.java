package com.android.jetman;

import java.util.Stack;

import android.app.Activity;
import android.app.Application;

public class JetmanApp extends Application {

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
	}

	private static JetmanApp instance;
	private static JetmanApp context;
	private static Stack<Activity> stackActivity;

	public JetmanApp() {
	}

	public static JetmanApp getContext() {
		return context;
	}

	public static JetmanApp getInstance() {
		return null == instance ? instance = new JetmanApp() : instance;
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
}
