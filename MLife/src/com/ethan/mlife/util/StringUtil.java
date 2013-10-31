package com.ethan.mlife.util;

public class StringUtil {
	/**
	 * 字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(Object str) {
		if (null == str || str.toString().length() == 0
				|| "".equals(str.toString().trim()))
			return true;
		return false;
	}
}
