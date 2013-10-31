package com.android.ringfly.common;

public enum Nature {
	METAL, WOOD, WATER, FIRE, EARTH, ALL, FEILEI, FEIDAN, TAIJI, POPO;
	public static Nature getNature(String strNature) {
		return valueOf(strNature.toUpperCase());
	}

	public static boolean fight(Nature A, Nature B) {
		if (B == TAIJI)
			return false;
		if (B != POPO) {
			if (A == ALL || A == FEIDAN || A == FEILEI)
				return true;
			if (A == METAL && B == WOOD)
				return true;
			if (A == WOOD && B == EARTH)
				return true;
			if (A == EARTH && B == WATER)
				return true;
			if (A == WATER && B == FIRE)
				return true;
			if (A == FIRE && B == METAL)
				return true;
		} else {
			if (A == ALL)
				return true;
		}
		return false;
	}
}
