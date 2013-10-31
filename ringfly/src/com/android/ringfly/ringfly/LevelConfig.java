package com.android.ringfly.ringfly;

import com.android.ringfly.common.Nature;
import com.badlogic.gdx.math.Vector2;

import static com.android.ringfly.common.Constant.*;

public enum LevelConfig {

	LEVEL1(new float[] { M, M, M, M, M, M, M, M, M, M }, new Nature[] {
			Nature.WOOD, Nature.WOOD, Nature.WOOD, Nature.WOOD, Nature.WOOD,
			Nature.WOOD, Nature.WOOD, Nature.WOOD, Nature.WOOD, Nature.WOOD },
			new Vector2[] { new Vector2(50, M) }, new Vector2[] { new Vector2(
					500, 430) }, new Vector2[] { new Vector2(300, 30) },
			new Integer[] { 200, 0, 500, 0, 0, 0, 0, 0, 1000, 0 }, new Integer[] {
					10, 0, 0, 0, 0, 0, 0, 0, 0 }, new Boolean[] { false, false,
					false, false }, 1, new Integer[] {}),

	LEVEL2(new float[] { U, M, U, M, U, M, U, M, U, M }, new Nature[] {
			Nature.WOOD, Nature.WOOD, Nature.WOOD, Nature.WOOD, Nature.WOOD,
			Nature.WOOD, Nature.WOOD, Nature.WOOD, Nature.WOOD, Nature.WOOD },
			new Vector2[] { new Vector2(20, M), new Vector2(100, U) },
			new Vector2[] { new Vector2(300, 430), new Vector2(500, 430) },
			new Vector2[] { new Vector2(300, 30), new Vector2(400, 40) },
			new Integer[] { 0, 0, 200, 0, 0, 0, 1000, 0, 0, 0 }, new Integer[] {
					10, 0, 0, 0, 0, 0, 0, 0, 0 }, new Boolean[] { false, false,
					false, false }, 1, new Integer[] {}),

	LEVEL3(new float[] { U, M, D, U, M, D, U, M, D, M }, new Nature[] {
			Nature.WOOD, Nature.WOOD, Nature.WOOD, Nature.WOOD, Nature.WOOD,
			Nature.WOOD, Nature.WOOD, Nature.WOOD, Nature.WOOD, Nature.WOOD },
			new Vector2[] { new Vector2(100, M), new Vector2(50, U),
					new Vector2(300, D) }, new Vector2[] {
					new Vector2(300, 430), new Vector2(500, 430),
					new Vector2(400, 330) }, new Vector2[] {
					new Vector2(300, 30), new Vector2(400, 40),
					new Vector2(500, 35) }, new Integer[] { 0, 500, 0, 0, 200, 0,
					0, 500, 0, 0 }, new Integer[] { 10, 0, 0, 0, 0, 0, 0, 0, 0 },
			new Boolean[] { false, false, false, false }, 1, new Integer[] {}),

	LEVEL4(new float[] { M, M, M, M, M, M, M, M, M, M },
			new Nature[] { Nature.WOOD, Nature.EARTH, Nature.WOOD,
					Nature.EARTH, Nature.WOOD, Nature.EARTH, Nature.WOOD,
					Nature.EARTH, Nature.WOOD, Nature.EARTH },
			new Vector2[] { new Vector2(50, M) }, new Vector2[] { new Vector2(
					500, 430) }, new Vector2[] { new Vector2(300, 30) },
			new Integer[] { 0, 200, 0, 0, 0, 500, 0, 1000, 0, 0 }, new Integer[] {
					5, 5, 0, 0, 0, 1, 1, 0, 0 }, new Boolean[] { true, true,
					false, false }, 1, new Integer[] { 0 }),

	LEVEL5(new float[] { M, U, M, M, U, M, M, U, M, U },
			new Nature[] { Nature.WOOD, Nature.EARTH, Nature.WOOD,
					Nature.EARTH, Nature.WOOD, Nature.EARTH, Nature.WOOD,
					Nature.EARTH, Nature.WOOD, Nature.EARTH }, new Vector2[] {
					new Vector2(20, M), new Vector2(100, U) }, new Vector2[] {
					new Vector2(300, 430), new Vector2(500, 430) },
			new Vector2[] { new Vector2(300, 30), new Vector2(400, 40) },
			new Integer[] { 0, 500, 0, 200, 0, 0, 1000, 0, 0, 0 }, new Integer[] {
					5, 5, 0, 0, 0, 1, 1, 0, 0 }, new Boolean[] { true, true,
					false, false }, 1, new Integer[] { 3 }),

	LEVEL6(new float[] { U, M, D, M, U, D, U, D, U, M },
			new Nature[] { Nature.WOOD, Nature.EARTH, Nature.WOOD,
					Nature.EARTH, Nature.WOOD, Nature.EARTH, Nature.WOOD,
					Nature.EARTH, Nature.WOOD, Nature.EARTH }, new Vector2[] {
					new Vector2(100, M), new Vector2(50, U),
					new Vector2(300, D) }, new Vector2[] {
					new Vector2(300, 430), new Vector2(500, 430),
					new Vector2(400, 330) }, new Vector2[] { new Vector2(300,
					30) }, new Integer[] { 0, 0, 0, 0, 200, 0, 1000, 0, 500, 0 },
			new Integer[] { 5, 5, 0, 0, 0, 1, 1, 0, 0 }, new Boolean[] { true,
					true, false, false }, 1, new Integer[] { 6 }),

	LEVEL7(new float[] { M, M, M, M, M, M, M, M, M, M },
			new Nature[] { Nature.WOOD, Nature.WATER, Nature.WOOD,
					Nature.EARTH, Nature.EARTH, Nature.EARTH, Nature.WOOD,
					Nature.WATER, Nature.WATER, Nature.WOOD },
			new Vector2[] { new Vector2(50, M) }, new Vector2[] { new Vector2(
					500, 430) }, new Vector2[] { new Vector2(300, 30) },
			new Integer[] { 0, 200, 0, 500, 0, 0, 0, 1000, 0, 0 }, new Integer[] {
					4, 3, 0, 0, 3, 2, 1, 0, 0 }, new Boolean[] { true, true,
					false, false }, 1, new Integer[] { 0, 3 }),

	LEVEL8(new float[] { U, M, U, M, U, M, U, M, U, M },
			new Nature[] { Nature.WOOD, Nature.WATER, Nature.WOOD,
					Nature.EARTH, Nature.EARTH, Nature.EARTH, Nature.WOOD,
					Nature.WATER, Nature.WATER, Nature.WOOD }, new Vector2[] {
					new Vector2(20, M), new Vector2(100, U) }, new Vector2[] {
					new Vector2(300, 430), new Vector2(500, 430) },
			new Vector2[] { new Vector2(300, 30), new Vector2(400, 40) },
			new Integer[] { 0, 0, 0, 200, 0, 500, 0, 0, 1000, 0 }, new Integer[] {
					4, 3, 0, 0, 3, 2, 1, 0, 0 }, new Boolean[] { true, true,
					false, false }, 1, new Integer[] { 4, 8 }),

	LEVEL9(new float[] { M, D, M, U, D, M, U, D, M, U },
			new Nature[] { Nature.WOOD, Nature.WATER, Nature.WOOD,
					Nature.EARTH, Nature.EARTH, Nature.EARTH, Nature.WOOD,
					Nature.WATER, Nature.WATER, Nature.WOOD }, new Vector2[] {
					new Vector2(100, M), new Vector2(50, U),
					new Vector2(300, D) }, new Vector2[] {
					new Vector2(300, 430), new Vector2(500, 430),
					new Vector2(400, 330) }, new Vector2[] {
					new Vector2(300, 30), new Vector2(400, 40),
					new Vector2(500, 35) }, new Integer[] { 0, 500, 0, 0, 200, 0,
					0, 0, 1000, 0 }, new Integer[] { 4, 3, 0, 0, 3, 2, 1, 0, 0 },
			new Boolean[] { true, true, false, false }, 1,
			new Integer[] { 1, 7 }),

	LEVEL10(new float[] { M, M, M, M, M, M, M, M, M, M },
			new Nature[] { Nature.WOOD, Nature.WOOD, Nature.WOOD, Nature.EARTH,
					Nature.EARTH, Nature.EARTH, Nature.WATER, Nature.WATER,
					Nature.FIRE, Nature.FIRE }, new Vector2[] { new Vector2(50,
					M) }, new Vector2[] { new Vector2(500, 430) },
			new Vector2[] { new Vector2(300, 30) }, new Integer[] { 200, 500, 0,
					0, 0, 0, 1000, 0, 0, 0 }, new Integer[] { 3, 3, 2, 0, 2, 3, 1,
					0, 0 }, new Boolean[] { true, true, false, false }, 1,
			new Integer[] { 1, 3, 5 }),

	LEVEL11(new float[] { U, M, U, M, U, M, U, M, U, M},
			new Nature[] { Nature.WOOD, Nature.WOOD, Nature.WOOD, Nature.EARTH,
					Nature.EARTH, Nature.EARTH, Nature.WATER, Nature.WATER,
					Nature.FIRE, Nature.FIRE }, new Vector2[] {
					new Vector2(20, M), new Vector2(100, U) }, new Vector2[] {
					new Vector2(300, 430), new Vector2(500, 430) },
			new Vector2[] { new Vector2(300, 30), new Vector2(400, 40) },
			new Integer[] { 0, 500, 0, 200, 0, 0, 1000, 0, 0, 0 }, new Integer[] {
					3, 3, 2, 0, 2, 3, 1, 0, 0 }, new Boolean[] { true, true,
					false, false }, 1, new Integer[] { 2, 4, 6 }),

	LEVEL12(new float[] { U, M, D, M, U, M, D, M, U, M },
			new Nature[] { Nature.WOOD, Nature.WOOD, Nature.WOOD, Nature.EARTH,
					Nature.EARTH, Nature.EARTH, Nature.WATER, Nature.WATER,
					Nature.FIRE, Nature.FIRE }, new Vector2[] {
					new Vector2(100, M), new Vector2(50, U),
					new Vector2(300, D) }, new Vector2[] {
					new Vector2(300, 430), new Vector2(500, 430),
					new Vector2(400, 330) }, new Vector2[] {
					new Vector2(300, 30), new Vector2(400, 40),
					new Vector2(500, 35) }, new Integer[] { 0, 0, 500, 0, 200, 0,
					0, 0, 1000, 0 }, new Integer[] { 3, 3, 2, 0, 2, 3, 1, 0, 0 },
			new Boolean[] { true, true, false, false }, 1, new Integer[] { 3,
					5, 9 }),

	LEVEL13(new float[] { M, M, M, M, M, M, M, M, M, M },
			new Nature[] { Nature.METAL, Nature.METAL, Nature.WOOD,
					Nature.WOOD, Nature.WATER, Nature.WATER, Nature.FIRE,
					Nature.FIRE, Nature.EARTH, Nature.EARTH },
			new Vector2[] { new Vector2(50, M) }, new Vector2[] { new Vector2(
					500, 430) }, new Vector2[] { new Vector2(300, 30) },
			new Integer[] { 0, 1000, 0, 200, 0, 0, 0, 0, 500, 0 }, new Integer[] {
					2, 2, 2, 2, 2, 4, 1, 1, 0 }, new Boolean[] { true, true,
					true, false }, 1, new Integer[] { 2, 4, 5, 7 }),

	LEVEL14(new float[] { U, M, U, M, U, M, U, M, U, M},
			new Nature[] { Nature.METAL, Nature.METAL, Nature.WOOD,
					Nature.WOOD, Nature.WATER, Nature.WATER, Nature.FIRE,
					Nature.FIRE, Nature.EARTH, Nature.EARTH }, new Vector2[] {
					new Vector2(20, M), new Vector2(100, U) }, new Vector2[] {
					new Vector2(300, 430), new Vector2(500, 430) },
			new Vector2[] { new Vector2(300, 30) }, new Integer[] { 0, 0, 0,
					200, 500, 0, 0, 1000, 0, 0 }, new Integer[] { 2, 2, 2, 2, 2, 4,
					1, 0, 1 }, new Boolean[] { true, true, false, true }, 1,
			new Integer[] { 0, 1, 3, 8 }),

	LEVEL15(new float[] { U, U, D, U, U, D, U, M, U, M },
			new Nature[] { Nature.METAL, Nature.METAL, Nature.WOOD,
					Nature.WOOD, Nature.WATER, Nature.WATER, Nature.FIRE,
					Nature.FIRE, Nature.EARTH, Nature.EARTH }, new Vector2[] {
					new Vector2(100, M), new Vector2(50, U),
					new Vector2(300, D) }, new Vector2[] {
					new Vector2(300, 430), new Vector2(500, 430),
					new Vector2(400, 330) }, new Vector2[] {
					new Vector2(300, 30), new Vector2(400, 40),
					new Vector2(500, 35) }, new Integer[] { 0, 500, 0, 200, 0, 0,
					0, 0, 1000, 0 }, new Integer[] { 2, 2, 2, 2, 2, 4, 1, 1, 1 },
			new Boolean[] { true, true, true, true }, 1, new Integer[] { 2, 4,
					6, 8 }),

	LEVEL16(new float[] { M, M, M, M, M, M, M, M, M, M },
			new Nature[] { Nature.METAL, Nature.METAL, Nature.WOOD,
					Nature.WOOD, Nature.WATER, Nature.WATER, Nature.FIRE,
					Nature.FIRE, Nature.EARTH, Nature.EARTH },
			new Vector2[] { new Vector2(50, M) }, new Vector2[] { new Vector2(
					500, 430) }, new Vector2[] { new Vector2(300, 30) },
			new Integer[] { 0, 500, 0, 200, 0, 0, 0, 0, 1000, 0 }, new Integer[] {
					2, 2, 2, 2, 2, 5, 1, 0, 0 }, new Boolean[] { true, true,
					false, false }, 1, new Integer[] { 1, 3, 5, 7, 9 }),

	LEVEL17(new float[] { U, M, U, M, U, M, U, M, U, M },
			new Nature[] { Nature.METAL, Nature.METAL, Nature.WOOD,
					Nature.WOOD, Nature.WATER, Nature.WATER, Nature.FIRE,
					Nature.FIRE, Nature.EARTH, Nature.EARTH }, new Vector2[] {
					new Vector2(20, M), new Vector2(100, U) }, new Vector2[] {
					new Vector2(300, 430), new Vector2(500, 430) },
			new Vector2[] { new Vector2(300, 30), new Vector2(400, 40) },
			new Integer[] { 0, 500, 0, 0,1000, 0, 200, 0, 0, 0 }, new Integer[] {
					2, 2, 2, 2, 2, 5, 1, 0, 0 }, new Boolean[] { true, true,
					false, false }, 1, new Integer[] { 0, 2, 4, 6, 8 }),

	LEVEL18(new float[] { D, M, D, M, U, D, U, M, U, M },
			new Nature[] { Nature.METAL, Nature.METAL, Nature.WOOD,
					Nature.WOOD, Nature.WATER, Nature.WATER, Nature.FIRE,
					Nature.FIRE, Nature.EARTH, Nature.EARTH }, new Vector2[] {
					new Vector2(100, M), new Vector2(50, U),
					new Vector2(300, D) }, new Vector2[] {
					new Vector2(300, 430), new Vector2(500, 430),
					new Vector2(400, 330) }, new Vector2[] {
					new Vector2(300, 30), new Vector2(400, 40),
					new Vector2(500, 35) }, new Integer[] { 200, 0, 500, 0, 0, 0,
					0, 1000, 0, 0 }, new Integer[] { 2, 2, 2, 2, 2, 5, 1, 0, 0 },
			new Boolean[] { true, true, false, false }, 1, new Integer[] { 0,
					1, 3, 7, 8 });

	private float[] demonsPosition;
	private Nature[] natures;
	private Vector2[] cloudsPos;
	private Vector2[] stonePos;
	private Vector2[] applePos;
	private Integer[] goldCoin;
	private Integer[] ringNums;
	private Boolean[] visibility;
	private Integer TaiJiNum;
	private Integer[] isPopo;

	public float[] getDemonsPosition() {
		return demonsPosition;
	}

	public Nature[] getNatures() {
		return natures;
	}

	public Vector2[] getCloudsPos() {
		return cloudsPos;
	}

	public Vector2[] getStonePos() {
		return stonePos;
	}

	public Vector2[] getApplePos() {
		return applePos;
	}

	public Integer[] getGoldCoin() {
		return goldCoin;
	}

	public Integer[] getRingNums() {
		return ringNums;
	}

	public Boolean[] getVisibility() {
		return visibility;
	}

	public Integer getTaiJiNum() {
		return TaiJiNum;
	}

	public Integer[] getIsPopo() {
		return isPopo;
	}

	private LevelConfig(float[] demonsPosition, Nature[] natures,
			Vector2[] cloudsPos, Vector2[] stonePos, Vector2[] applePos,
			Integer[] goldCoin, Integer[] ringNums, Boolean[] visibility,
			Integer taiJiNum, Integer[] isPopo) {
		this.demonsPosition = demonsPosition;
		this.natures = natures;
		this.cloudsPos = cloudsPos;
		this.stonePos = stonePos;
		this.applePos = applePos;
		this.goldCoin = goldCoin;
		this.ringNums = ringNums;
		this.visibility = visibility;
		TaiJiNum = taiJiNum;
		this.isPopo = isPopo;
	}
}
