package com.android.ringfly.ringfly;

import com.android.ringfly.dao.sqlite.desktopImpl.SettingDAOImpl;
import com.badlogic.gdx.backends.jogl.JoglApplication;

public class RingflyDesktop {
	private final static int WINDOW_WIDTH = 800;
	private final static int WINDOW_HEIGHT = 480;
//	
//	private final static int WINDOW_WIDTH = 1024;
//	private final static int WINDOW_HEIGHT = 720;

	public static void main(String[] args) {
		new JoglApplication(new RingflyGame(new SettingDAOImpl()), "RingFly",
				WINDOW_WIDTH, WINDOW_HEIGHT, false);
//		new JoglApplication(new LabelTest(), "RingFly", WINDOW_WIDTH,
//				WINDOW_HEIGHT, false);
	}
}
