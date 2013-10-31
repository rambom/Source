package com.android.ringfly.sprite;

import com.android.ringfly.common.Assets;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TaiJIDownSprite extends Sprite {
	float spriteWidth;
	float spriteHeight;
	float spriteRotate = 0;

	public TaiJIDownSprite() {
		super();
		this.spriteWidth = Assets.taiJiRegionMap.get("taiJiDown")
				.getRegionWidth();
		this.spriteHeight = Assets.taiJiRegionMap.get("taiJiDown")
				.getRegionHeight();
		this.setRegion(Assets.taiJiRegionMap.get("taiJiDown"));
		this.setOrigin(spriteWidth * 0.5f, spriteHeight * 0.5f);
		this.setBounds(200, 200, spriteWidth, spriteHeight);
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
		if (null != this && spriteRotate <= 180) {
			this.setRotation(spriteRotate);
			spriteRotate += 0.1f;
		}
		//spriteBatch.draw(Assets.taiJiRegionMap.get("maskUp"), 200, 250);
	}

}
