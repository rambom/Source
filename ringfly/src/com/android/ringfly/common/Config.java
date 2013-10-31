package com.android.ringfly.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * ≈‰÷√∂¡»°¿‡
 * 
 * @author fgshu
 */
public class Config {

	private static final String PROPERTIES_FILE = "data/properties/sys.properties";
	private static Properties properties;

	private Config() {
	}

	private static Properties instance() {
		if (null == properties) {
			properties = new Properties();
			FileHandle fh = Gdx.files.internal(PROPERTIES_FILE);
			InputStream inStream = fh.read();
			try {
				properties.load(inStream);
				inStream.close();
			} catch (IOException e) {
				if (inStream != null) {
					try {
						inStream.close();
					} catch (IOException ex) {
					}
				}
			}
		}
		return properties;
	}

	public static int asInt(String name, int fallback) {
		String v = instance().getProperty(name);
		if (v == null)
			return fallback;
		return Integer.parseInt(v);
	}

	public static float asFloat(String name, float fallback) {
		String v = instance().getProperty(name);
		if (v == null)
			return fallback;
		return Float.parseFloat(v);
	}

	public static String asString(String name, String fallback) {
		String v = instance().getProperty(name);
		if (v == null)
			return fallback;
		return v;
	}
	
	public static boolean asBoolean(String name,boolean fallback){
		String v = instance().getProperty(name);
		if(v==null)
			return fallback;
		return Boolean.parseBoolean(v);
	}
}
