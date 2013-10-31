package com.android.junit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import junit.framework.TestCase;

public class TestCustomize extends TestCase {
	String url = "http://www.szjt.gov.cn/apts/APTSLine.aspx?LineGuid=5b056a72-5eae-4f82-89bf-7f0a71668194&LineInfo=1(火车站)";

	public void test() {
		List<Line> list = new ArrayList<Line>();
		list.add(new Line(null, null, null, null, null, null, "0", null, null,
				null));
		list.add(new Line(null, null, null, null, null, null, "2", null, null,
				null));
		list.add(new Line(null, null, null, null, null, null, "1", null, null,
				null));
		list.add(new Line(null, null, null, null, null, null, "3", null, null,
				null));
		list.add(new Line(null, null, null, null, null, null, "待发", null, null,
				null));
		list.add(new Line(null, null, null, null, null, null, "9", null, null,
				null));
		list.add(new Line(null, null, null, null, null, null, "13", null, null,
				null));

		// 按站距排序
		Collections.sort(list, new Comparator<Line>() {
			public int compare(Line object1, Line object2) {
				Integer intSpacing1;
				Integer intSpacing2;
				try {
					intSpacing1 = Integer.valueOf(object1.getSpacing());
				} catch (Exception ex) {
					return 1;
				}

				try {
					intSpacing2 = Integer.valueOf(object2.getSpacing());
				} catch (Exception ex) {
					return -1;
				}

				// 按点击数排序
				return intSpacing1.compareTo(intSpacing2);
			}
		});

		System.out.println(list.toString());
	}
}
