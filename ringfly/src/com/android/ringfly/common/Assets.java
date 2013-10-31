/*
 * Copyright 2011 Rod Hyde (rod@badlydrawngames.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.android.ringfly.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.ringfly.configset.ConfigSet;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.FloatArray;

public class Assets {
	// definition of constant
	public final static int VELOCITYITERATIONS = Config.asInt(
			"velocityIterations", 6);
	public final static int POSITIONITERATIONS = Config.asInt(
			"positionIterations", 2);

	public static final float DEMON_RUN_FRAME_DURATION = Config.asFloat(
			"demon.RunFrameDuration", 0.1f);
	public static final float DEMON_DIE_FRAME_DURATION = Config.asFloat(
			"demon.DieFrameDuration", 0.1f);

	public static final float DEMON_HIT_FRAME_DURATION = Config.asFloat(
			"demon.HitFrameDuration", 0.1f);

	public static final float DEMON_RUN_SPEED = Config.asFloat(
			"demon.RunSpeed", 20f);

	public static final float DEMON_OCCUR_BREAK = Config.asFloat(
			"demon.DemonOccurBreak", 8f);

	public static final float PATHLINE_RENDERTIME = Config.asFloat(
			"pathLine.RenderTime", 8f);

	public static final float FAKE_POIN_TNUM = Config.asFloat("pathLine.Num",
			20);

	public static final float RING_SPEED_DIVISOR = Config.asFloat(
			"ring.SpeedDivisor", 5);

	public static final float GRAVITY = Config.asFloat("world.gravity", -10f);

	public static final float PATHLINE_SHAKE = Config.asFloat("pathLine.Shake",
			1f);

	public static final float PTM_RATIO = Config.asFloat("PTM_RATIO", 20f);

	public static final Boolean GAME_ISDEBUG_MODE = Config.asBoolean(
			"game.isDebug", false);

	public static final String GAMENAME = "圈圈飞";

	public static final String GAME_EXPIRY_DATE = Config.asString(
			"game.Expiry.Date", "20121223");

	// definition of TextureAtlas
	private static TextureAtlas ringAtlas;

	// definition of TextureRegion
	public static TextureRegion backgroundRegion01;
	public static TextureRegion backgroundRegion00;
	public static TextureRegion backgroundRegionSeason;
	public static TextureRegion backgroundRegionLevel;
	public static TextureRegion backgroundRegionLevelUp;
	public static TextureRegion apple;
	public static TextureRegion cloud;
	public static TextureRegion taiJiStone;
	public static TextureRegion backgroundRegionSideMenu;
	public static TextureRegion fightArrow, fightArrowLong;
	public static List<TextureRegion> demonRunRegions, demonDieRegions,
			demonEatRegions, mHandRegions, metalHandRegions, woodHandRegions,
			waterHandRegions, fireHandRegions, earthHandRegions,
			allHandRegions, earthDemonRunRegions;

	public static List<TextureRegion> earthFightWaterRegions,
			woodFightEarthRegions, waterFightFireRegions,
			fireFightMetalRegions, metalFightWoodRegions;

	public static List<TextureRegion> failMetalRegions, failWoodRegions,
			failWaterRegions, failFireRegions, failEarthRegions;

	public static List<TextureRegion> hideRunRegions, metalRunRegions,
			woodRunRegions, waterRunRegions, fireRunRegions, earthRunRegions,
			taiJiRunRegions, metalTaiJiRunRegions, woodTaiJiRunRegions,
			waterTaiJiRunRegions, fireTaiJiRunRegions, earthTaiJiRunRegions,
			allFightTaiJiDieRegions, fireRegions, popoRegions;

	public static HashMap<String, TextureRegion> rings;
	public static HashMap<String, TextureRegion> taiJiRegionMap, helpRegionMap,
			exitConfirmRegionMap;

	public static TextureRegion gardenRegion, btnGardenRegion,
			btnGardenDownRegion;

	public static TextureRegion pageIndicatorFocusedRegion,
			pageIndicatorRegion;

	public static HashMap<String, TextureRegion> mainMenuButtons, stageButtons,
			seasonButtons, levelButtons, sideMenuButtons, levelUpButtons,
			gradeButtons, levelRegions;
	public static TextureRegion hand;
	public static TextureRegion textPolygonRegion;

	// definition of Sound

	public static HashMap<Sounds, Sound> soundMap;

	// definition of Animation
	public static Animation demonRun, earthDemonRun, demonDie, demonEat,
			metalHandAnim, woodHandAnim, waterHandAnim, fireHandAnim,
			earthHandAnim, allHandAnim, fireAnim;

	public static Animation earthFightWaterAnim, woodFightEarthAnim,
			waterFightFireAnim, fireFightMetalAnim, metalFightWoodAnim;

	public static Animation failMetalAnim, failWoodAnim, failWaterAnim,
			failFireAnim, failEarthAnim;

	public static Animation hideRunAnim, metalRunAnim, taiJiRunAnim,
			allFightTaiJiDieAnim, popoAnim;

	// definition of BitmapFont
	public static BitmapFont huaWenXiHeiFont;

	public static final float VIRTUAL_WIDTH = 40.0f;
	public static final float VIRTUAL_HEIGHT = 24.0f;

	public static float pixelDensity;

	public static Skin uiSkin, gradeUpSkin;

	public enum Sounds {
		sound_score_count_loop, sound_streched, sound_loop_playing, sound_popo_break, sound_hit_demon_success, sound_child_cry, sound_demon_laugh, sound_door_open, sound_loop_enter, sound_hit_demon_fail, sound_loop_level_select, sound_levelup_success, sound_ring_hit_stone, sound_ringfly;
	}

	public enum BtnLevel {
		level1, level2, level3, level4, level5, level6, level7, level8, level9, level10, level11, level12, level13, level14, level15, level16, level17, level18;
		public static BtnLevel getBtnLevel(String btnLevel) {
			return valueOf(btnLevel);
		}
	}

	public enum BtnGrade {
		grade1, grade2, grade3, grade4, grade5, grade6;
		public static BtnGrade getBtnGrade(String btnGrade) {
			return valueOf(btnGrade);
		}
	}

	public enum Season {
		spring, summer, autumn, winter;
	}

	public enum BtnSeason {
		btnSpring, btnSummer, btnAutumn, btnWinter;
		public static BtnSeason getBtnSeason(String btnSeason) {
			return valueOf(btnSeason);
		}
	}

	public enum BtnSelect {
		btnMetal, btnWood, btnWater, btnFire, btnEarth, btnAll, btnEye, btnFeiLei, btnFeiDan, btnPause, btnReset;
		public static BtnSelect getBtnChoice(String btnRingSelect) {
			return valueOf(btnRingSelect);
		}
	}

	public enum BtnSideMenu {
		sideMenuGoOn, sideMenuHelp, sideMenuLevelSelect, sideMenuMainMenu, sideMenuReset, sideMenuSoundOn, sideMenuSoundOff;
		public static BtnSideMenu getBtnSideMenu(String btnBtnSideMenu) {
			return valueOf(btnBtnSideMenu);
		}
	}

	public enum BtnLevelUp {
		levelUpLevelSelect, levelUpReplay, levelUpNextLevel;
		public static BtnLevelUp getBtnLevelUp(String btnLevelUp) {
			return valueOf(btnLevelUp);
		}
	}

	public static void load() {
		pixelDensity = calculatePixelDensity();
		String textureDir = "data/textures/" + (int) pixelDensity;
		loadTextures(textureDir);
		createAnimations();
		loadFonts();
		loadSounds();
		uiSkin = new Skin(Gdx.files.internal("data/skin/uiskin.json"),
				Gdx.files.internal("data/skin/uiskin.png"));
		gradeUpSkin = new Skin(
				Gdx.files.internal("data/skin/gradeupskin.json"),
				Gdx.files.internal("data/skin/gradeupskin.png"));
		uiSkin.getTexture().setFilter(TextureFilter.Nearest,
				TextureFilter.Nearest);
		gradeUpSkin.getTexture().setFilter(TextureFilter.Nearest,
				TextureFilter.Nearest);
	}

	private static void loadFonts() {
		huaWenXiHeiFont = new BitmapFont(
				Gdx.files.internal("data/fonts/32/huiwenxihei32.fnt"), false);
		// huaWenXiHeiFont.setScale(1.0f / pixelDensity);
	}

	private static void loadTextures(String textureDir) {
		loadTextureAtlas(textureDir);

		textPolygonRegion = new TextureRegion(
				ringAtlas.findRegion("testPolygon"));

		pageIndicatorFocusedRegion = new TextureRegion(
				ringAtlas.findRegion("pageIndicatorFocused"));
		pageIndicatorRegion = new TextureRegion(ringAtlas.findRegion("pageIndicator"));
		
		gardenRegion = new TextureRegion(ringAtlas.findRegion("garden"));
		btnGardenRegion = new TextureRegion(ringAtlas.findRegion("btnGarden"));
		btnGardenDownRegion = new TextureRegion(
				ringAtlas.findRegion("btnGardenDown"));

		backgroundRegion01 = new TextureRegion(
				ringAtlas.findRegion("background01"));
		backgroundRegion00 = new TextureRegion(
				ringAtlas.findRegion("background00"));
		backgroundRegionSeason = new TextureRegion(
				ringAtlas.findRegion("seasonselect"));
		backgroundRegionLevel = new TextureRegion(
				ringAtlas.findRegion("levelselect"));
		backgroundRegionLevelUp = new TextureRegion(
				ringAtlas.findRegion("levelUpBackground"));

		backgroundRegionSideMenu = new TextureRegion(
				ringAtlas.findRegion("sideMenuBackground"));

		apple = new TextureRegion(ringAtlas.findRegion("renShenGuo"));
		cloud = new TextureRegion(ringAtlas.findRegion("cloud"));
		taiJiStone = new TextureRegion(ringAtlas.findRegion("TaiJiStone"));
		fightArrow = new TextureRegion(ringAtlas.findRegion("fightArrow"));
		fightArrowLong = new TextureRegion(
				ringAtlas.findRegion("fightArrowLong"));

		hand = new TextureRegion(ringAtlas.findRegion("hand"));
		mHandRegions = getRegionList(ringAtlas, "metalHandF1", "metalHandF2");
		metalHandRegions = getRegionList(ringAtlas, "metalHandF1",
				"metalHandF2");
		woodHandRegions = getRegionList(ringAtlas, "woodHandF1", "woodHandF2");
		waterHandRegions = getRegionList(ringAtlas, "waterHandF1",
				"waterHandF2");
		fireHandRegions = getRegionList(ringAtlas, "fireHandF1", "fireHandF2");
		earthHandRegions = getRegionList(ringAtlas, "earthHandF1",
				"earthHandF2");
		allHandRegions = getRegionList(ringAtlas, "allHandF1", "allHandF2");
		demonRunRegions = getRegionList(ringAtlas, "demonRunF1", "demonRunF2",
				"demonRunF3", "demonRunF4", "demonRunF5", "demonRunF6",
				"demonRunF7");

		earthDemonRunRegions = getRegionList(ringAtlas, "earthDemonRunF1",
				"earthDemonRunF2", "earthDemonRunF3", "earthDemonRunF4",
				"earthDemonRunF5", "earthDemonRunF6", "earthDemonRunF7");

		demonDieRegions = getRegionList(ringAtlas, "demonDieF1", "demonDieF2",
				"demonDieF3", "demonDieF4", "demonDieF5");

		earthFightWaterRegions = getRegionList(ringAtlas, "earthFightWaterF1",
				"earthFightWaterF2", "earthFightWaterF3");
		woodFightEarthRegions = getRegionList(ringAtlas, "woodFightEarthF1",
				"woodFightEarthF2", "woodFightEarthF3");
		waterFightFireRegions = getRegionList(ringAtlas, "waterFightFireF1",
				"waterFightFireF2", "waterFightFireF3");
		fireFightMetalRegions = getRegionList(ringAtlas, "fireFightMetalF1",
				"fireFightMetalF2", "fireFightMetalF3");
		metalFightWoodRegions = getRegionList(ringAtlas, "metalFightWoodF1",
				"metalFightWoodF2", "metalFightWoodF3");

		failMetalRegions = getRegionList(ringAtlas, "failMetalF1",
				"failMetalF2");
		failWoodRegions = getRegionList(ringAtlas, "failWoodF1", "failWoodF2");
		failWaterRegions = getRegionList(ringAtlas, "failWaterF1",
				"failWaterF2");
		failFireRegions = getRegionList(ringAtlas, "failFireF1", "failFireF2");
		failEarthRegions = getRegionList(ringAtlas, "failEarthF1",
				"failEarthF2");

		hideRunRegions = getRegionList(ringAtlas, "hideRunF1", "hideRunF2",
				"hideRunF3", "hideRunF4", "hideRunF5");
		metalRunRegions = getRegionList(ringAtlas, "metalRunF1", "metalRunF2",
				"metalRunF3", "metalRunF4", "metalRunF5");
		woodRunRegions = getRegionList(ringAtlas, "woodRunF1", "woodRunF2",
				"woodRunF3", "woodRunF4", "woodRunF5");
		waterRunRegions = getRegionList(ringAtlas, "waterRunF1", "waterRunF2",
				"waterRunF3", "waterRunF4", "waterRunF5");
		fireRunRegions = getRegionList(ringAtlas, "fireRunF1", "fireRunF2",
				"fireRunF3", "fireRunF4", "fireRunF5");
		earthRunRegions = getRegionList(ringAtlas, "earthRunF1", "earthRunF2",
				"earthRunF3", "earthRunF4", "earthRunF5");

		taiJiRunRegions = getRegionList(ringAtlas, "taiJiRunF1", "taiJiRunF2");

		metalTaiJiRunRegions = getRegionList(ringAtlas, "metalTaiJiRunF1");
		woodTaiJiRunRegions = getRegionList(ringAtlas, "woodTaiJiRunF1");
		waterTaiJiRunRegions = getRegionList(ringAtlas, "waterTaiJiRunF1");
		fireTaiJiRunRegions = getRegionList(ringAtlas, "fireTaiJiRunF1");
		earthTaiJiRunRegions = getRegionList(ringAtlas, "earthTaiJiRunF1");

		allFightTaiJiDieRegions = getRegionList(ringAtlas,
				"allFightTaiJiDieF1", "allFightTaiJiDieF2",
				"allFightTaiJiDieF3");

		demonEatRegions = getRegionList(ringAtlas, "demonDieF1", "demonDieF2",
				"demonDieF3", "demonDieF4", "demonDieF5");
		rings = getRegionHashMap(ringAtlas, "metalRing", "woodRing",
				"waterRing", "fireRing", "earthRing", "MissileRing",
				"AllPowerfulRing", "feileiRingSmall", "allRing", "feiLeiRing",
				"feiDanRing", "feiDan3Ring");

		taiJiRegionMap = getRegionHashMap(ringAtlas, "taiJiUp", "taiJiDown",
				"maskUp", "maskDown", "taiJiDoor");

		helpRegionMap = getRegionHashMap(ringAtlas, "help1", "help2", "help3",
				"click");

		exitConfirmRegionMap = getRegionHashMap(ringAtlas, "exitConfirmDialog",
				"cancel", "confirm");

		mainMenuButtons = getRegionHashMap(ringAtlas, "btnStartDown",
				"btnStartUp", "btnInfo", "btnSoundOn", "btnSoundOff", "info");

		stageButtons = getRegionHashMap(ringAtlas, "btnBackground", "btnMetal",
				"btnWood", "btnWater", "btnFire", "btnEarth", "btnAll",
				"btnEye", "btnFeiLei", "btnFeiDan", "btnMetalDown",
				"btnWoodDown", "btnWaterDown", "btnFireDown", "btnEarthDown",
				"btnAllDown", "btnEyeDown", "btnFeiLeiDown", "btnFeiDanDown",
				"btnPause", "btnReset");

		seasonButtons = getRegionHashMap(ringAtlas, "btnSpring", "btnSummer",
				"btnAutumn", "btnWinter", "btnSummerLocked", "btnAutumnLocked",
				"btnWinterLocked");

		levelButtons = getRegionHashMap(ringAtlas, "level1", "level2",
				"level3", "levelLocked");

		sideMenuButtons = getRegionHashMap(ringAtlas, "sideMenuGoOn",
				"sideMenuHelp", "sideMenuLevelSelect", "sideMenuMainMenu",
				"sideMenuReset", "sideMenuSoundOn", "sideMenuSoundOff");
		levelUpButtons = getRegionHashMap(ringAtlas, "levelUpLevelSelect",
				"levelUpReplay", "levelUpNextLevel");

		levelRegions = getRegionHashMap(ringAtlas, "levelFailBackground",
				"levelFailFace", "levelFailFont", "levelPassFace",
				"levelPassFont");

		gradeButtons = getRegionHashMap(ringAtlas, "grade1", "grade2",
				"grade3", "grade4", "grade5", "grade6", "grade2Locked",
				"grade3Locked", "grade4Locked", "grade5Locked", "grade6Locked");

		fireRegions = getRegionList(ringAtlas, "fireF1", "fireF2", "fireF3");

		popoRegions = getRegionList(ringAtlas, "popo");

	}

	/**
	 * 计算最佳像素密度
	 * 
	 * @return
	 */
	private static float calculatePixelDensity() {
		FileHandle textureDir = Gdx.files.internal("data/textures");
		FileHandle[] availableDensities = textureDir.list();
		FloatArray densities = new FloatArray();
		for (int i = 0; i < availableDensities.length; i++) {
			try {
				float density = Float.parseFloat(availableDensities[i].name());
				densities.add(density);
			} catch (NumberFormatException ex) {
				// Ignore anything non-numeric, such as ".svn" folders.
			}
		}
		densities.shrink(); // Remove empty slots to get rid of zeroes.
		densities.sort(); // Now the lowest density comes first.
		return CameraHelper.bestDensity(VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
				densities.items);
	}

	/**
	 * 通过TextureAtlas和regionNames数组获得List<TextureRegion>
	 * 
	 * @param atlas
	 * @param regionNames
	 * @return
	 */
	private static List<TextureRegion> getRegionList(TextureAtlas atlas,
			String... regionNames) {
		List<TextureRegion> list = new ArrayList<TextureRegion>();
		for (String name : regionNames) {
			list.add(new TextureRegion(atlas.findRegion(name)));
		}
		return list;
	}

	/**
	 * 通过TextureAtlas和regionNames数组获得HashMap<String, TextureRegion>
	 * 
	 * @param atlas
	 * @param regionNames
	 * @return
	 */
	private static HashMap<String, TextureRegion> getRegionHashMap(
			TextureAtlas atlas, String... regionNames) {
		HashMap<String, TextureRegion> map = new HashMap<String, TextureRegion>();
		for (String name : regionNames) {
			map.put(name, new TextureRegion(atlas.findRegion(name)));
		}
		return map;
	}

	private static void loadTextureAtlas(String textureDir) {
		ringAtlas = loadATextureAtlas(textureDir + "/pack");
	}

	private static TextureAtlas loadATextureAtlas(String filename) {
		return new TextureAtlas(Gdx.files.internal(filename));
	}

	private static void loadSounds() {
		soundMap = loadSoundMap();
	}

	private static HashMap<Sounds, Sound> loadSoundMap() {
		HashMap<Sounds, Sound> soundMap = new HashMap<Assets.Sounds, Sound>();
		for (Sounds sound : Sounds.values()) {
			soundMap.put(
					sound,
					Gdx.audio.newSound(Gdx.files.internal("data/sounds/"
							+ sound.name() + ".ogg")));
		}
		return soundMap;
	}

	private static Sound loadSound(String filename) {
		return Gdx.audio
				.newSound(Gdx.files.internal("data/sounds/" + filename));
	}

	public static void playSound(Sounds sound, Boolean loop, float volume) {
		Sound soundSrc = Assets.soundMap.get(sound);
		if (ConfigSet.userMap.get("0").getSound()) {
			if (loop)
				soundSrc.loop();
			else
				soundSrc.play(volume);
		}
	}

	public static void playSound(Sounds sound) {
		Sound soundSrc = Assets.soundMap.get(sound);
		if (ConfigSet.userMap.get("0").getSound()) {
			soundSrc.play(1);
		}
	}

	public static void stopSound(Sounds sound) {
		Sound soundSrc = Assets.soundMap.get(sound);
		soundSrc.stop();
	}

	private static void createAnimations() {
		demonRun = new Animation(DEMON_RUN_FRAME_DURATION, demonRunRegions);
		demonDie = new Animation(DEMON_DIE_FRAME_DURATION, demonDieRegions);

		earthFightWaterAnim = new Animation(DEMON_RUN_FRAME_DURATION,
				earthFightWaterRegions);
		woodFightEarthAnim = new Animation(DEMON_RUN_FRAME_DURATION,
				woodFightEarthRegions);
		waterFightFireAnim = new Animation(DEMON_RUN_FRAME_DURATION,
				waterFightFireRegions);
		fireFightMetalAnim = new Animation(DEMON_RUN_FRAME_DURATION,
				fireFightMetalRegions);
		metalFightWoodAnim = new Animation(DEMON_RUN_FRAME_DURATION,
				metalFightWoodRegions);

		failMetalAnim = new Animation(DEMON_RUN_FRAME_DURATION,
				failMetalRegions);
		failWoodAnim = new Animation(DEMON_RUN_FRAME_DURATION, failWoodRegions);
		failWaterAnim = new Animation(DEMON_RUN_FRAME_DURATION,
				failWaterRegions);
		failFireAnim = new Animation(DEMON_RUN_FRAME_DURATION, failFireRegions);
		failEarthAnim = new Animation(DEMON_RUN_FRAME_DURATION,
				failEarthRegions);

		popoAnim = new Animation(DEMON_RUN_FRAME_DURATION, popoRegions);
		hideRunAnim = new Animation(DEMON_RUN_FRAME_DURATION, hideRunRegions);
		metalRunAnim = new Animation(DEMON_RUN_FRAME_DURATION, metalRunRegions);
		taiJiRunAnim = new Animation(DEMON_RUN_FRAME_DURATION, taiJiRunRegions);
		allFightTaiJiDieAnim = new Animation(DEMON_RUN_FRAME_DURATION,
				allFightTaiJiDieRegions);

		metalHandAnim = new Animation(0.2f, metalHandRegions);
		woodHandAnim = new Animation(0.2f, woodHandRegions);
		waterHandAnim = new Animation(0.2f, waterHandRegions);
		fireHandAnim = new Animation(0.2f, fireHandRegions);
		earthHandAnim = new Animation(0.2f, earthHandRegions);
		allHandAnim = new Animation(0.2f, allHandRegions);

		earthDemonRun = new Animation(DEMON_RUN_FRAME_DURATION,
				earthDemonRunRegions);

		fireAnim = new Animation(DEMON_RUN_FRAME_DURATION, fireRegions);
	}
}
