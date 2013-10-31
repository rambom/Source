package com.android.ringfly.actor;

import java.util.List;

import com.android.ringfly.common.Assets;
import com.android.ringfly.common.Nature;
import com.android.ringfly.ringfly.Cookie;
import com.android.ringfly.ringfly.GameWorldListener;
import com.android.ringfly.ringfly.GameWorldNotifier;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.actions.Parallel;
import com.badlogic.gdx.scenes.scene2d.actions.Repeat;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;

public class AppleActor extends Actor implements AppleBehaviour,
		GameWorldListener {
	private TextureRegion textureRegion;
	public AppleState state;
	public Vector2 locationXY;
	GameWorldNotifier worldNotifier;

	public enum AppleState {
		normal, blink, die;
		public static AppleState getAppleState(String AppleState) {
			return valueOf(AppleState);
		}
	}

	public AppleActor(String name, TextureRegion textureRegion,
			GameWorldNotifier notifier) {
		super(name);
		this.worldNotifier = notifier;
		this.state = AppleState.normal;
		this.textureRegion = textureRegion;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		if (null != this.textureRegion) {
			batch.draw(textureRegion, x, y);
		}
	}

	@Override
	public Actor hit(float x, float y) {
		return null;
	}

	@Override
	public void die(float demonY) {
		this.state = AppleState.blink;
		this.actions.clear();
		Action fade = Sequence.$(FadeOut.$(0.2f), FadeIn.$(1f));
		Action up = MoveTo.$(this.x, demonY, (demonY - this.y) / 30);
		Action blink = Repeat.$(fade, 3);
		this.action(Parallel.$(blink, up));
		blink.setCompletionListener(new blinkActionListener());
	}

	@Override
	public void saved() {
		this.actions.clear();// 停止闪烁
		this.state = AppleState.normal;
		this.color.a = 1;
		Action down = MoveTo.$(this.x, this.locationXY.y, 0.1f);
		this.action(down);
	}

	class blinkActionListener implements OnActionCompleted {
		@Override
		public void completed(Action action) {
			Gdx.app.log("I am", "eated");
			AppleActor.this.state = AppleActor.AppleState.die;
			AppleActor.this.visible = false;// 简单处理 优化时可移除舞台
			worldNotifier.onGoldChanged(Cookie.gold - 100);
			worldNotifier.onMagicChanged(Cookie.magic - 10);
		}
	}

	@Override
	public void onGameStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGamePause() {
		if (this.state == AppleState.blink)
			this.actions.clear();
		this.state = AppleState.normal;
		Gdx.app.log("I am apple" + this.name,
				"I am notified that the game is paused");
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
		Gdx.app.log("I am apple" + this.name,
				"I am notified that the game is continued");

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
	public void onSoundChange(Assets.Sounds sound) {
		// TODO Auto-generated method stub

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
