package com.android.junit;

import android.test.AndroidTestCase;

import com.ethan.mlife.dao.BusDaoFacade;
import com.ethan.mlife.dao.impl.SuZhouBusJsoupImpl;
import com.ethan.mlife.entity.Line;

public class TestHttpConnection extends AndroidTestCase {
	String url = "http://www.szjt.gov.cn/apts/APTSLine.aspx?LineGuid=5b056a72-5eae-4f82-89bf-7f0a71668194&LineInfo=1(火车站)";

	public void testHttp() {
		BusDaoFacade busDao = new SuZhouBusJsoupImpl();
		Line line = new Line();
		line.setUrlLink("http://www.szjt.gov.cn/apts/APTSLine.aspx?LineGuid=5b056a72-5eae-4f82-89bf-7f0a71668194");
		line.setLineNo("1");
		for (Line station : busDao.getBusLine(line)) {
			System.out.println(station);
		}
		/*
		 * System.out.println("httpRequestDao"); busDao = new
		 * SuZhouBusRegularImpl(); for (Station station :
		 * busDao.getLineStation(line)) { System.out.println(station); }
		 */

	}
}
