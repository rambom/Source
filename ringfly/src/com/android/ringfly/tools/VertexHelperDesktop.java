package com.android.ringfly.tools;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class VertexHelperDesktop {
	private final static int WINDOW_WIDTH=800;
	private final static int WINDOW_HEIGHT=480;
	public static void main(String[] args) {
		new JoglApplication(new VertexHelper(), "VertexHelper", WINDOW_WIDTH,
				WINDOW_HEIGHT, false);
	}

}
