package com.android.ringfly.configset;

import java.util.HashMap;

import com.android.ringfly.common.Assets.BtnLevel;
import com.android.ringfly.entity.mapping.Chapter;
import com.android.ringfly.entity.mapping.Grade;
import com.android.ringfly.entity.mapping.Level;
import com.android.ringfly.entity.mapping.User;
import com.badlogic.gdx.Gdx;

public class ConfigSet {
	public static HashMap<String, User> userMap = new HashMap<String, User>();
	public static HashMap<Integer, Level> levelMap = new HashMap<Integer, Level>();
	public static HashMap<Integer, Grade> gradeMap = new HashMap<Integer, Grade>();
	public static HashMap<String, Chapter> chapterMap = new HashMap<String, Chapter>();

	public static void unlock(int level) {
		ConfigSet.levelMap.get(level).setLock(false);
		Gdx.app.log("level" + level, "unlocked");
	}
}
