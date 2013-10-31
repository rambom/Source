package com.android.ringfly.ringfly;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.android.ringfly.common.Assets;
import com.android.ringfly.common.Assets.Sounds;
import com.android.ringfly.common.Nature;
import com.android.ringfly.configset.ConfigSet;
import com.android.ringfly.ringfly.Cookie.StatEnum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class StatusManager implements GameWorldListener {
	private List<String> luckyNums;
	private final AchievementsNotifier achievementsNotifier;
	private final EnumSet<Achievements> achieved;
	private final DataNotifier dataNotifier;

	public void setGold(int gold) {
		Cookie.gold = gold;
		dataNotifier.onGoldChanged(gold);
	}

	private void addGold(int delta) {
		setGold(Cookie.gold + delta);
	}

	private void addMagic(int delta) {
		setMagic(Cookie.magic + delta);
	}

	public void setMagic(int magic) {
		Cookie.magic = magic;
		dataNotifier.onMagicChanged(magic);
	}

	public StatusManager() {
		luckyNums = new ArrayList<String>();
		achievementsNotifier = new AchievementsNotifier();
		dataNotifier = new DataNotifier();
		achieved = EnumSet.noneOf(Achievements.class);
	}

	public void addDataListener(DataListener listener) {
		dataNotifier.addListener(listener);
	}

	public void addAchievementsListener(AchievementsListener listener) {
		achievementsNotifier.addListener(listener);
	}

	private void achievement(Achievements achievement) {
		if (!achieved.contains(achievement)) {
			achieved.add(achievement);
			achievementsNotifier.onAttained(achievement);
		}
	}

	public void update(float delta) {
		if (Cookie.achieved)
			return;
		Integer curLevel = Cookie.getCurLevelConfig().ordinal() + 1;
		Integer demonsDiedNum = ConfigSet.levelMap.get(curLevel)
				.getDemondienum().length();
		Integer demonsEscNum = ConfigSet.levelMap.get(curLevel)
				.getDemoneacapenums().length();
		if (Cookie.outOfRings || demonsDiedNum + demonsEscNum == 10) {
			if (demonsDiedNum >= 5) {
				// Cookie.genStrLuckyNums();
				ConfigSet.levelMap.get(curLevel).setStars(10 - demonsDiedNum);
				achievement(Achievements.LevelUp);
			} else {
				achievement(Achievements.LevelFail);
			}
		}
		if (demonsEscNum == 6) {
			achievement(Achievements.LevelFail);
		}

	}

	@Override
	public void onGameStart() {
		Cookie.setState(StatEnum.PLAYING);
		Cookie.isPaused = false;
	}

	@Override
	public void onGamePause() {
		// Gdx.app.log("pause", "pause");

	}

	@Override
	public void onGameReset() {
		Cookie.setState(StatEnum.PLAYING);
		Cookie.isPaused = false;
	}

	@Override
	public void onGameRuning() {
		Gdx.app.log("run", "run");
	}

	@Override
	public void onContinue() {
		Gdx.app.log("goon", "goon");
		Cookie.setState(StatEnum.CONTINUE);
		Cookie.isPaused = false;
	}

	@Override
	public void onDemonDie(String luckyNum, Integer magic, Integer gold) {
		Gdx.app.log("demon", "is shot by the right ring and die");
		Assets.playSound(Sounds.sound_hit_demon_success);
		this.luckyNums.add(luckyNum);
		addGold(gold);
		addMagic(magic);
	}

	@Override
	public void onShot(Nature nature) {
		Gdx.app.log("a Ring", "is used and minus gold");
		Assets.playSound(Assets.Sounds.sound_ringfly);
		int goldDelta = 0, magicDelta = 0;
		switch (nature) {
		case METAL:
			dataNotifier.onMetalChanged();
			break;
		case WOOD:
			dataNotifier.onWoodChanged();
			break;
		case WATER:
			dataNotifier.onWaterChanged();
			break;
		case FIRE:
			dataNotifier.onFireChanged();
			break;
		case EARTH:
			dataNotifier.onEarthChanged();
			break;
		case ALL:
			dataNotifier.onAllChanged();
			goldDelta -= Setting.ALL_GOLD_COST;
			magicDelta -= Setting.ALL_MAGIC_COST;
			break;
		case FEILEI:
			dataNotifier.onFeiLeiChanged();
			goldDelta -= Setting.FEILEI_GOLD_COST;
			magicDelta -= Setting.FEILEI_MAGIC_COST;
			Cookie.setFeileiAble(true);
			break;
		case FEIDAN:
			dataNotifier.onFeiDanChanged();
			goldDelta -= Setting.FEIDAN_GOLD_COST;
			magicDelta -= Setting.FEIDAN_MAGIC_COST;
			Cookie.setFeidanAble(true);
			break;
		}
		addGold(goldDelta);
		addMagic(magicDelta);
	}

	@Override
	public void onLevelSelect() {

	}

	@Override
	public void onMainMenuSelect() {

	}

	@Override
	public void onSoundChange(Assets.Sounds sound) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContactStone(List<Vector2> contactPoints) {
		Assets.playSound(Assets.Sounds.sound_ring_hit_stone);
	}

	@Override
	public void onGradeUp(Boolean blnPass, Boolean win) {
		if (blnPass) {
			Achievements.GradeUp.setZhongJiang(win);
			achievement(Achievements.GradeUp);
		}
	}

	@Override
	public void onEyeUsed() {
		dataNotifier.onEyeChanged();
	}

	@Override
	public void onGoldChanged(int gold) {
		dataNotifier.onGoldChanged(gold);

	}

	@Override
	public void onMagicChanged(int magic) {
		dataNotifier.onMagicChanged(magic);
	}
}
