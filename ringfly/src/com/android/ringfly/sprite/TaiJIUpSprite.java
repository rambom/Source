package com.android.ringfly.sprite;

import com.android.ringfly.common.Assets;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TaiJIUpSprite extends Sprite {
	float spriteWidth;
	float spriteHeight;
	float spriteRotate = 0;
	boolean flag = true;

	public TaiJIUpSprite(float x, float y) {
		super();
		this.spriteWidth = Assets.taiJiRegionMap.get("taiJiUp")
				.getRegionWidth();
		this.spriteHeight = Assets.taiJiRegionMap.get("taiJiUp")
				.getRegionHeight();
		this.setRegion(Assets.taiJiRegionMap.get("taiJiUp"));
		this.setOrigin(spriteWidth * 0.5f, spriteHeight * 0.5f);
		this.setBounds(x, y, spriteWidth, spriteHeight);
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
		if (flag) {
			flag = false;
		}
		if (null != this && spriteRotate <= 180) {
			this.setRotation(spriteRotate);
			spriteRotate += 0.1f;
		}
		// spriteBatch.draw(Assets.taiJiRegionMap.get("maskDown"), 200, 200);
	}

}
