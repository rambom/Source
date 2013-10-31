package com.android.ringfly.sprite;

import com.android.ringfly.common.Assets;
import com.android.ringfly.ringfly.Cookie;
import com.android.ringfly.ringfly.Cookie.StatEnum;
import com.android.ringfly.ringfly.LevelConfig;
import com.android.ringfly.ringfly.RingflyGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TaiJiDoorSprite extends Sprite {
	private float spriteWidth;
	private float spriteHeight;
	private float spriteRotate;
	private float spriteScale;
	private boolean play;
	private RingflyGame game;
	private float x, y;

	public TaiJiDoorSprite(float x, float y, RingflyGame game) {
		super();
		this.game = game;
		this.x = x;
		this.y = y;
		init();
		this.setRegion(Assets.taiJiRegionMap.get("taiJiDoor"));

	}

	private void init() {
		spriteRotate = 0;
		spriteScale = 1;
		this.play = false;
		this.spriteWidth = Assets.taiJiRegionMap.get("taiJiDoor")
				.getRegionWidth();
		this.spriteHeight = Assets.taiJiRegionMap.get("taiJiDoor")
				.getRegionHeight();
		this.setOrigin(spriteWidth * 0.5f, spriteHeight * 0.5f);
		this.setBounds(x, y, spriteWidth, spriteHeight);
		this.setScale(1);
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
		if (null != this && play) {
			this.setScale(spriteScale);
			this.setRotation(spriteRotate);
			float deltaTime = Gdx.graphics.getDeltaTime();
			spriteRotate += deltaTime * 1000;
			if (spriteScale >= deltaTime) {
				spriteScale -= deltaTime;
			} else {
				game.setScreen(game.getNewPlayingScreen(LevelConfig.values()[game.levelOrdinal]));
				game.settingDao.backLoop(true, Assets.Sounds.sound_loop_playing);
				Cookie.setState(StatEnum.PLAYING);
				init();
			}
		}
	}

	public void play() {
		this.play = true;
	}

}
