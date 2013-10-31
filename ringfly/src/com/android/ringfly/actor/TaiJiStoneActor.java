package com.android.ringfly.actor;

import java.util.List;

import com.android.ringfly.common.Assets;
import com.android.ringfly.common.Nature;
import com.android.ringfly.ringfly.Cookie;
import com.android.ringfly.ringfly.GameWorldListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TaiJiStoneActor extends Actor implements GameWorldListener {
	private Body body;
	private TextureRegion textureRegion;
	private List<Vector2> contactPoints;
	private float stateTime;
	private boolean flag;

	public TaiJiStoneActor(String name, Body body, TextureRegion textureRegion) {
		super(name);
		body.setUserData(this);
		this.body = body;
		this.textureRegion = textureRegion;
		flag = false;
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
		if (null != this.contactPoints && this.contactPoints.size() > 0) {
			if (!flag) {
				stateTime = 0;
				flag = true;
			}
			stateTime += Gdx.graphics.getDeltaTime();
			for (Vector2 point : contactPoints) {
				Gdx.app.log("I will", "draw a fire at" + point.x + "||"
						+ point.y);
				batch.draw(Assets.fireAnim.getKeyFrame(stateTime, false),
						point.x * Assets.pixelDensity - 30, point.y
								* Assets.pixelDensity - 30);
			}
			if (Assets.fireAnim.isAnimationFinished(stateTime)) {
				this.flag = false;
				this.contactPoints.clear();
			}
		}
	}

	@Override
	public Actor hit(float x, float y) {
		return null;
	}

	@Override
	public void onGameStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGamePause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameReset() {
		// TODO Auto-generated method stub

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
	public void onSoundChange(Assets.Sounds sound) {
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onMainMenuSelect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContactStone(List<Vector2> contactPoints) {
		this.contactPoints = contactPoints;
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
