package com.ethan.mlife.dao;

import com.ethan.mlife.dao.impl.SuZhouBusJsoupImpl;

public class MyBusFactory {
	private BusDaoFacade busDao;
	private String region;
	private static MyBusFactory context;

	private MyBusFactory() {
		this.busDao = new SuZhouBusJsoupImpl();
		this.region = "0512";
	}

	public static MyBusFactory getMyBus() {
		if (null == context) {
			context = new MyBusFactory();
		}
		return context;
	}

	private void updateBusDao() {
	}

	/**
	 * @return the busDao
	 * @throws Exception
	 */
	public BusDaoFacade getBusDao() {
		return busDao;
	}

	/**
	 * 设置公交城市区号
	 * 
	 * @param region
	 *            the region to set
	 */
	public synchronized void setRegion(String region) {
		this.region = region;
		updateBusDao();
	}

	/**
	 * 返回当前城市区号
	 * 
	 * @return
	 */
	public String getRegion() {
		return this.region;
	}
}
