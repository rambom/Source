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

package com.android.ringfly.ringfly;

import com.android.ringfly.actor.GradeUp;
import com.android.ringfly.actor.LevelUp;
import com.android.ringfly.common.Assets;
import com.android.ringfly.configset.ConfigSet;
import com.android.ringfly.ringfly.Cookie.StatEnum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class StatusView implements AchievementsListener, DataListener {

	public final GameWorld gameWorld;
	private final OrthographicCamera statusCam;
	private final SpriteBatch spriteBatch;
	private float now;
	// private Stage stage;

	private LevelUp levelUp;
	private GradeUp gradeUp;

	public StatusView(GameWorld world) {

		this.gameWorld = world;
		// statusCam = CameraHelper.createCamera2(ViewportMode.PIXEL_PERFECT,
		// VIRTUAL_WIDTH, VIRTUAL_HEIGHT, Assets.pixelDensity);
		statusCam = new OrthographicCamera(40, 24);
		statusCam.position.set(20, 12, 0);
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(statusCam.combined);

		// stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
		// false);

	}

	public void render(float delta) {
		Integer curLevel = Cookie.getCurLevelConfig().ordinal() + 1;
		Integer demonsDiedNum = ConfigSet.levelMap.get(curLevel)
				.getDemondienum().length();
		if (demonsDiedNum >= Setting.RUNBACK_DEMONS_NEED && !Cookie.runBack) {
			Cookie.runBack = true;
			Gdx.app.log(this.getClass().getName(), "left demons runback");
			this.gameWorld.leftDemonsRunBack();
		}

		// stage.act(Gdx.graphics.getDeltaTime());
		// stage.draw();
		now += delta;
		spriteBatch.begin();
		if (!Cookie.isPaused) {
			drawPauseButton();
		} else {
			// drawPaused();
		}
		spriteBatch.end();
	}

	private void drawPauseButton() {

	}

	@Override
	public void onAttained(Achievements achievement) {
		Gdx.app.log(achievement.getSummary(), achievement.getText());
		Cookie.achieved = true;
		int curLevel = Cookie.getCurLevelConfig().ordinal() + 1;
		int nextLevel = curLevel + 1;
		switch (achievement) {
		case LevelUp:
			Assets.playSound(Assets.Sounds.sound_child_cry);
			Assets.playSound(Assets.Sounds.sound_levelup_success);
			if (nextLevel > LevelConfig.values().length)
				break;
			ConfigSet.unlock(nextLevel);
			Cookie.setState(StatEnum.PAUSE);
			// levelUp = new LevelUp(gameWorld.getNotifier(), "levelUpSuccess",
			// true);
			// stage.addActor(levelUp);
			gameWorld.levelUp.visible = true;
			ConfigSet.levelMap.get(curLevel).setCoin(
					Cookie.gold - Cookie.oldGold);
			ConfigSet.levelMap.get(curLevel).setMagic(
					Cookie.magic - Cookie.oldMagic);
			break;
		case LevelFail:
			Assets.playSound(Assets.Sounds.sound_demon_laugh);
			Cookie.setState(StatEnum.PAUSE);
			// levelUp = new LevelUp(gameWorld.getNotifier(), "levelUpFail",
			// false);
			// stage.addActor(levelUp);
			gameWorld.levelFail.visible = true;
			break;
		case GradeUp:
			if (nextLevel > LevelConfig.values().length)
				break;
			ConfigSet.unlock(nextLevel);
			Cookie.setState(StatEnum.PAUSE);
			gameWorld.gradeUp.setGradeUp();
			gameWorld.gradeUp.visible = true;
			// gradeUp = new GradeUp(gameWorld.getNotifier(), "gardeUpSuccess");
			// stage.addActor(gradeUp);
			ConfigSet.levelMap.get(curLevel).setCoin(
					Cookie.gold - Cookie.oldGold);
			ConfigSet.levelMap.get(curLevel).setMagic(
					Cookie.magic - Cookie.oldMagic);
			break;
		}

	}

	@Override
	public void onGoldChanged(int gold) {
		gameWorld.goldLabel.setText("½ð±Ò:" + gold);

	}

	@Override
	public void onMagicChanged(int magic) {
		gameWorld.magicLabel.setText("Ä§Á¦:" + magic);

	}

	@Override
	public void onMetalChanged() {
		int num = Cookie.getRingsRemain()[0] - 1;
		Cookie.getRingsRemain()[0] = num;
		gameWorld.metalLabel.setText(num + "");
	}

	@Override
	public void onWoodChanged() {
		int num = Cookie.getRingsRemain()[1] - 1;
		Cookie.getRingsRemain()[1] = num;
		gameWorld.woodLabel.setText(num + "");
	}

	@Override
	public void onWaterChanged() {
		int num = Cookie.getRingsRemain()[2] - 1;
		Cookie.getRingsRemain()[2] = num;
		gameWorld.waterLabel.setText(num + "");
	}

	@Override
	public void onFireChanged() {
		int num = Cookie.getRingsRemain()[3] - 1;
		Cookie.getRingsRemain()[3] = num;
		gameWorld.fireLabel.setText(num + "");
	}

	@Override
	public void onEarthChanged() {
		int num = Cookie.getRingsRemain()[4] - 1;
		Cookie.getRingsRemain()[4] = num;
		gameWorld.earthLabel.setText(num + "");
	}

	@Override
	public void onAllChanged() {
		int num = Integer.parseInt(gameWorld.allLabel.getText().toString()) - 1;
		Cookie.getRingsRemain()[5] = num;
		gameWorld.allLabel.setText(num + "");
	}

	@Override
	public void onEyeChanged() {
		int num = Cookie.getRingsRemain()[6] - 1;
		Cookie.getRingsRemain()[6] = num;
		gameWorld.eyeLabel.setText(num + "");

	}

	@Override
	public void onFeiLeiChanged() {
		int num = Integer.parseInt(gameWorld.feileiLabel.getText().toString()) - 1;
		Cookie.getRingsRemain()[7] = num;
		gameWorld.feileiLabel.setText(num + "");
	}

	@Override
	public void onFeiDanChanged() {
		int num = Integer.parseInt(gameWorld.feidanLabel.getText().toString()) - 1;
		Cookie.getRingsRemain()[8] = num;
		gameWorld.feidanLabel.setText(num + "");
	}
}
