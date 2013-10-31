package com.android.ringfly.actor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CloudActor extends Actor {
	private Body body;
	private TextureRegion textureRegion;

	public CloudActor(String name, Body body, TextureRegion textureRegion) {
		super(name);
		this.body = body;
		this.textureRegion = textureRegion;
		this.width = textureRegion.getRegionWidth();
		this.height = textureRegion.getRegionHeight();
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		if (null != this.body) {
			this.body.setTransform(this.x / 20, this.y / 20, 0);
		}
		if (null != this.textureRegion) {
			batch.draw(this.textureRegion, x, y);
		}
	}

	@Override
	public Actor hit(float x, float y) {
		return null;
	}

}
