package com.android.ringfly.ringfly;

import java.util.ArrayList;
import java.util.List;

import com.android.ringfly.common.Assets.Season;
import com.badlogic.gdx.math.Vector2;

public class Cookie {
	public static int gold;
	public static int magic;
	private static StatEnum state = StatEnum.PLAYING;
	public static boolean isPaused = false;
	public static String strLuckyNums = "";
	// public static List<Integer> luckyNumList;
	// public static List<String> demonsDieNum;
	private static Season season = Season.spring;
	private static LevelConfig curLevelConfig;
	private static boolean feileiAble;
	private static boolean feidanAble;
	private static Integer[] ringsRemain;
	public static boolean outOfRings;
	public static boolean achieved;
	public static boolean runBack;
	// public static List<String> demonEscapeNum;
	// public static List<String> demonDisappearNum;
	public static List<Vector2> pathPontsKeep;
	public static int oldGold, oldMagic;

	// public static Integer demonEscapeNum = 0;

	public static void Init() {
		// demonsDieNum = new ArrayList<String>();
		// demonEscapeNum = new ArrayList<String>();
		// demonDisappearNum = new ArrayList<String>();
		pathPontsKeep = new ArrayList<Vector2>();
		// demonDisappearNum.clear();
		// demonsDieNum.clear();
		achieved = false;
		isPaused = false;
		feileiAble = false;
		feidanAble = false;
		outOfRings = false;
		runBack = false;
		// demonEscapeNum.clear();
	}

	// public static void genStrLuckyNums() {
	// // not in demonDieNum
	// String strLuckyNums = "";
	// List<Integer> luckyNumList = new ArrayList<Integer>();
	// for (int i = 0; i < 10; i++) {
	// boolean in = false;
	// for (int j = 0; j < demonsDieNum.size(); j++) {
	// if (i == Integer.parseInt(demonsDieNum.get(j))) {
	// in = true;
	// break;
	// }
	// }
	// if (!in) {
	// luckyNumList.add(new Integer(i));
	// }
	// }
	// for (int i = 0; i < luckyNumList.size(); i++) {
	// strLuckyNums += luckyNumList.get(i);
	// if (i != luckyNumList.size() - 1)
	// strLuckyNums += "/";
	// }
	// Cookie.strLuckyNums = strLuckyNums;
	// Cookie.luckyNumList = luckyNumList;
	// }

	public static boolean isFeileiAble() {
		return feileiAble;
	}

	public static void setFeileiAble(boolean feileiAble) {
		Cookie.feileiAble = feileiAble;
	}

	public static boolean isFeidanAble() {
		return feidanAble;
	}

	public static void setFeidanAble(boolean feidanAble) {
		Cookie.feidanAble = feidanAble;
	}

	public static StatEnum getState() {
		return state;
	}

	public static void setState(StatEnum varstate) {
		state = varstate;
	}

	public enum StatEnum {
		RESETTING, PLAYING, PAUSE, CONTINUE;
		public static StatEnum getStatEnum(String strStatEnum) {
			return valueOf(strStatEnum);
		}
	}

	public static LevelConfig getCurLevelConfig() {
		return curLevelConfig;
	}

	public static void setCurLevelConfig(LevelConfig curLevelConfig) {
		Cookie.curLevelConfig = curLevelConfig;
		Cookie.ringsRemain = curLevelConfig.getRingNums().clone();
	}

	public static Season getSeason() {
		return season;
	}

	public static void setSeason(Season season) {
		Cookie.season = season;
	}

	public static Integer[] getRingsRemain() {
		return ringsRemain;
	}

	public static void setRingsRemain(Integer[] ringsRemain) {
		Cookie.ringsRemain = ringsRemain;
	}

}
