package com.android.ringfly.actor;

import java.util.HashMap;
import java.util.Iterator;

import com.android.ringfly.common.Assets;
import com.android.ringfly.common.Assets.BtnLevelUp;
import com.android.ringfly.common.TwoStateButton;
import com.android.ringfly.common.TwoStateButton.ButtonBoundShape;
import com.android.ringfly.configset.ConfigSet;
import com.android.ringfly.ringfly.Cookie;
import com.android.ringfly.ringfly.GameWorldNotifier;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;

public class LevelUp extends Actor {
	private TextureRegion textureBackground;
	private Sprite background;
	private Action showAction, hideAction;
	public boolean isShow;
	private String curLevel;
	private HashMap<String, TwoStateButton> btnsMap;
	private float width, height, centerX, centerY;
	private Vector2 touchPoint;
	private boolean wasTouched;

	private final GameWorldNotifier notifier;
	private boolean nextAble;
	TextureRegion region;
	private Sprite faceLaugh, faceCry, fontPass, fontFail;

	public LevelUp(GameWorldNotifier notifier, String name, boolean nextAble) {
		super(name);
		this.notifier = notifier;
		this.nextAble = nextAble;
		textureBackground = Assets.levelRegions.get("levelFailBackground");
		this.width = textureBackground.getRegionWidth();
		this.height = textureBackground.getRegionHeight();
		this.centerX = this.width / 2 - 55;
		this.centerY = this.height / 2 - 60;
		this.x = Gdx.graphics.getWidth() / 2 - this.width / 2;
		this.y = Gdx.graphics.getHeight() / 2 - this.height / 2;
		background = new Sprite(textureBackground);
		this.curLevel = "";
		btnsMap = createSideMenuBtns();
		setBtnPosition();
		faceCry = new Sprite(Assets.levelRegions.get("levelFailFace"));
		faceLaugh = new Sprite(Assets.levelRegions.get("levelPassFace"));
		fontPass = new Sprite(Assets.levelRegions.get("levelPassFont"));
		fontFail = new Sprite(Assets.levelRegions.get("levelFailFont"));
		float bottomUp = Gdx.graphics.getHeight() * 0.3f;
		faceLaugh.setPosition(this.x + centerX, bottomUp);
		faceCry.setPosition(this.x + centerX, bottomUp);

		fontPass.setPosition(this.x + centerX - 66, bottomUp + 200);
		fontFail.setPosition(this.x + centerX - 66, bottomUp + 200);

		this.visible = false;
	}

	private HashMap<String, TwoStateButton> createSideMenuBtns() {
		HashMap<String, TwoStateButton> map = new HashMap<String, TwoStateButton>();
		for (BtnLevelUp btn : BtnLevelUp.values()) {
			if (btn == BtnLevelUp.levelUpNextLevel && !nextAble) {
				continue;
			}
			TwoStateButton tb;
			tb = new TwoStateButton(Assets.levelUpButtons.get(btn.name()),
					ButtonBoundShape.CIRCLE);
			map.put(btn.name(), tb);
		}
		return map;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (!this.visible)
			return;
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		// sprite.draw(batch, 1f);

		if (this.x != -this.width) {
			// Color oldColor = Assets.huaWenXiHeiFont.getColor();
			background.setPosition(x, y);
			background.draw(batch, color.a * parentAlpha);
			drawButtons(batch);
			if (nextAble) {
				fontPass.draw(batch, color.a * parentAlpha);
				faceLaugh.draw(batch, color.a * parentAlpha);
			} else {
				fontFail.draw(batch, color.a * parentAlpha);
				faceCry.draw(batch, color.a * parentAlpha);
			}
			updateButtons();
		}
	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

	private void setBtnPosition() {
		Iterator<String> keySetIterator = btnsMap.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			float bottomUp = Gdx.graphics.getHeight() * 0.1f;
			switch (BtnLevelUp.getBtnLevelUp(btnName)) {
			case levelUpLevelSelect:
				btnsMap.get(btnName).x = this.x + centerX - 100;
				btnsMap.get(btnName).y = this.y + bottomUp;
				break;
			case levelUpNextLevel:
				btnsMap.get(btnName).x = this.x + centerX + 100;
				btnsMap.get(btnName).y = this.y + bottomUp;
				break;
			case levelUpReplay:
				btnsMap.get(btnName).x = nextAble ? this.x + centerX : this.x
						+ centerX + 100;
				btnsMap.get(btnName).y = this.y + bottomUp;
				break;
			}
		}
	}

	private void updateButtons() {
		touchPoint = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight()
				- Gdx.input.getY());
		boolean justTouched = Gdx.input.justTouched();
		boolean isTouched = Gdx.input.isTouched();
		boolean justReleased = wasTouched && !isTouched;
		wasTouched = isTouched;
		setBtnPosition();
		Iterator<String> keySetIterator = btnsMap.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			btnsMap.get(btnName).update(justTouched, isTouched, justReleased,
					touchPoint.x, touchPoint.y);
		}
		keySetIterator = btnsMap.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			if (btnsMap.get(btnName).wasPressed()) {
				Gdx.app.log(btnName, "wasPressed");
				this.hide();
				switch (BtnLevelUp.getBtnLevelUp(btnName)) {
				case levelUpLevelSelect:
					notifier.onLevelSelect();
					break;
				case levelUpNextLevel:
					// judege garadeup
					Integer curLevel = Cookie.getCurLevelConfig().ordinal() + 1;
					if (curLevel % 3 == 0) {
						// judge gradeup
						this.hide();
						notifier.onGradeUp(true, false);
					} else {
						notifier.onGameStart();
					}
					break;
				case levelUpReplay:
					notifier.onGameReset();
					break;
				}
			}
		}
	}

	private void drawButtons(SpriteBatch batch) {
		Iterator<String> keySetIterator = btnsMap.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			btnsMap.get(btnName).draw(batch);
		}
	}

	public void hide() {
		this.actions.clear();
		if (true || this.x == 0 && this.isShow) {
			this.isShow = false;
			// this.actions.remove();
			hideAction = FadeOut.$(0.2f);
			hideAction.setCompletionListener(new OnActionCompleted() {
				@Override
				public void completed(Action action) {
					// try {
					// this.finalize();
					// } catch (Throwable e) {
					// }
					LevelUp.this.visible = false;
				}
			});
			this.action(hideAction);
		}
	}

	public void show(String curLevel) {
		this.actions.clear();
		if (true || this.x == -textureBackground.getRegionWidth()
				&& !this.isShow) {
			this.isShow = true;
			this.curLevel = curLevel;
			// this.actions.remove();
			showAction = MoveTo.$(0, 0, 0.2f);
			this.action(showAction);
		}
	}
}
