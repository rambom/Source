package com.android.ringfly.ringfly;

import java.util.List;

import com.android.ringfly.common.Assets;
import com.android.ringfly.common.BodyFactory;
import com.android.ringfly.common.Nature;
import com.android.ringfly.common.Assets.Sounds;
import com.android.ringfly.configset.ConfigSet;
import com.android.ringfly.dao.sqlite.SettingDAO;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;

public class RingflyGame extends Game implements AchievementsListener,
		GameWorldListener {
	Screen mainMenuScreen;
	Screen playingScreen;
	Screen seasonSelectScreen;
	Screen levelSelectScreen;
	Screen gardenScreen;
	InputMultiplexer inputMultiplexer;
	public SettingDAO settingDao;
	private static RingflyGame instance;
	public int levelOrdinal;

	public void setScreen(Screen screen, int levelOrdinal) {
		// TODO Auto-generated method stub
		super.setScreen(screen);
		this.levelOrdinal = levelOrdinal;
	}

	public static RingflyGame getInstance() {
		return instance;
	}

	AchievementsListener achievementsListener;

	public RingflyGame(SettingDAO dao) {
		this.settingDao = dao;
	}

	@Override
	// 游戏入口点加在资源，创建场景，显示主菜单场景
	public void create() {
		// TODO Auto-generated method stub
		settingDao.loadSetting();
		Assets.load();
		// Cookie.gold = (int) ConfigSet.userMap.get("0").getCoin();
		// Cookie.magic = (int) ConfigSet.userMap.get("0").getMagic();
		settingDao.backLoop(true, Sounds.sound_loop_enter);
		// Assets.playSound(Assets.soundMap.get(Assets.Sounds.soundEnterLoop),
		// true, 1);
		inputMultiplexer = new InputMultiplexer();
		mainMenuScreen = new MainMenuScreen(this);
		// playingScreen = new PlayingScreen(this, null);
		seasonSelectScreen = new SeasonSelectScreen(this);
		levelSelectScreen = new LevelSelectScreen(this);
		gardenScreen = new GardenScreen(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
		setScreen(mainMenuScreen);
		instance = this;
	}

	public Screen getNewPlayingScreen(LevelConfig levelConfig) {
		Cookie.Init();
		calculateScore(levelConfig);
		inputMultiplexer = new InputMultiplexer();
		this.playingScreen = new PlayingScreen(this, levelConfig);
		Gdx.input.setInputProcessor(inputMultiplexer);
		return this.playingScreen;
	}

	private void calculateScore(LevelConfig levelConfig) {
		Integer ordinal = levelConfig.ordinal();

		Integer curGrade = ordinal / 3 + 1;
		String winNums = ConfigSet.gradeMap.get(curGrade).getLuckNums();
		char[] luckyNums = winNums.toCharArray();
		Integer sumWinNums = 0;
		for (char c : luckyNums) {
			String s = c + "";
			sumWinNums += Integer.parseInt(s);
		}
		Integer goldDelta = sumWinNums * 100;
		Integer magicDelta = 0;
		// goldDelta -= curGrade != 1 ? Setting.GRADE_UP_GOLD_COST : 0;
		// magicDelta -= curGrade != 1 ? Setting.GRADE_UP_MAGIC_COST : 0;
		Cookie.gold = Setting.INIT_GOLD + goldDelta;
		Cookie.magic = Setting.INIT_MAGIC + magicDelta;
		int curLevel = levelConfig.ordinal() + 1;
		ConfigSet.levelMap.get(curLevel).Init();
		ConfigSet.levelMap.get(curLevel).setLock(false);
		for (int i = 1; i < curLevel; i++) {
			Cookie.gold += (int) ConfigSet.levelMap.get(i).getCoin();
			Cookie.magic += (int) ConfigSet.levelMap.get(i).getMagic();
		}
		Cookie.oldGold = Cookie.gold;
		Cookie.oldMagic = Cookie.magic;
		// set the levels after
		for (int i = curLevel + 1; i < ConfigSet.levelMap.size(); i++) {
			ConfigSet.levelMap.get(i).Init();
		}

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		this.settingDao.saveSetting();
		settingDao.backLoop(false, Assets.Sounds.sound_loop_enter);
		BodyFactory.removeInstance();
		try {
			this.finalize();
		} catch (Throwable e) {
		}
	}

	@Override
	public void onAttained(Achievements achievement) {
		if (achievementsListener != null) {
			achievementsListener.onAttained(achievement);
		}
	}

	public void setAchievementsListener(AchievementsListener listener) {
		this.achievementsListener = listener;
	}

	@Override
	public void onGameStart() {
		BodyFactory.removeInstance();
		int nextOrdinal = Cookie.getCurLevelConfig().ordinal() + 1;
		setScreen(getNewPlayingScreen(LevelConfig.values()[nextOrdinal]));
	}

	@Override
	public void onGamePause() {

	}

	@Override
	public void onGameReset() {
		Gdx.app.log("game", "Reset");
		BodyFactory.removeInstance();
		setScreen(getNewPlayingScreen(Cookie.getCurLevelConfig()));
	}

	@Override
	public void onGameRuning() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContinue() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDemonDie(String luckyNum, Integer magic, Integer gold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onShot(Nature nature) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLevelSelect() {
		settingDao.backLoop(false, Assets.Sounds.sound_loop_playing);
		settingDao.backLoop(true, Assets.Sounds.sound_loop_level_select);
		BodyFactory.removeInstance();
		setScreen(levelSelectScreen);
	}

	@Override
	public void onMainMenuSelect() {
		settingDao.backLoop(false, Assets.Sounds.sound_loop_playing);
		settingDao.backLoop(true, Assets.Sounds.sound_loop_enter);
		BodyFactory.removeInstance();
		setScreen(mainMenuScreen);
	}

	@Override
	public void onSoundChange(Assets.Sounds sound) {
		ConfigSet.userMap.get("0").setSound(
				!ConfigSet.userMap.get("0").getSound());
		if (ConfigSet.userMap.get("0").getSound()) {
			settingDao.backLoop(true, sound);
		} else {
			settingDao.backLoop(false, sound);
		}
	}

	@Override
	public void onContactStone(List<Vector2> contactPoints) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGradeUp(Boolean blnPass, Boolean win) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEyeUsed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGoldChanged(int gold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMagicChanged(int magic) {
		// TODO Auto-generated method stub

	}
}
