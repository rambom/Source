package com.android.ringfly.actor;

import java.util.HashMap;
import java.util.Iterator;

import com.android.ringfly.common.Assets;
import com.android.ringfly.common.Assets.BtnSideMenu;
import com.android.ringfly.common.TwoStateButton;
import com.android.ringfly.common.TwoStateButton.ButtonBoundShape;
import com.android.ringfly.configset.ConfigSet;
import com.android.ringfly.ringfly.GameWorldNotifier;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;

public class SideMenu extends Actor {
	private TextureRegion textureBackground;
	private Sprite background;
	private Action showAction, hideAction;
	public boolean isShow;
	private String curLevel;
	private HashMap<String, TwoStateButton> btnsMap;
	private TwoStateButton btnInfoSpec;
	private float width, height;
	private Vector2 touchPoint;
	private boolean wasTouched;
	private final GameWorldNotifier notifier;

	public SideMenu(GameWorldNotifier notifier, String name) {
		super(name);
		this.notifier = notifier;
		this.isShow = false;
		textureBackground = Assets.backgroundRegionSideMenu;
		this.width = textureBackground.getRegionWidth();
		this.height = textureBackground.getRegionHeight();
		this.x = -width;
		this.y = 0;
		background = new Sprite(textureBackground);
		this.curLevel = "";
		btnsMap = createSideMenuBtns();
		setBtnPosition();
	}

	private HashMap<String, TwoStateButton> createSideMenuBtns() {
		HashMap<String, TwoStateButton> map = new HashMap<String, TwoStateButton>();
		for (BtnSideMenu btn : BtnSideMenu.values()) {
			TwoStateButton tb;
			tb = new TwoStateButton(Assets.sideMenuButtons.get(btn.name()),
					ButtonBoundShape.CIRCLE);
			map.put(btn.name(), tb);
		}
		btnInfoSpec = new TwoStateButton(Assets.mainMenuButtons.get("info"),
				ButtonBoundShape.CIRCLE);
		btnInfoSpec.visible = false;
		return map;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		if (this.x != -this.width) {
			Color oldColor = Assets.huaWenXiHeiFont.getColor();
			background.setPosition(x, y);
			background.draw(batch, 1f);
			drawButtons(batch);
			updateButtons();
			// Assets.huaWenXiHeiFont.setColor(Color.RED);
			// Assets.huaWenXiHeiFont.setScale(2f, 2f);// better not this way
			// Assets.huaWenXiHeiFont.drawWrapped(batch, curLevel, this.x,
			// this.y,
			// textureBackground.getRegionWidth(), HAlignment.CENTER);
			// Assets.huaWenXiHeiFont.setColor(oldColor);
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
			switch (BtnSideMenu.getBtnSideMenu(btnName)) {
			case sideMenuGoOn:
				btnsMap.get(btnName).x = this.x + 140;
				btnsMap.get(btnName).y = 200;
				break;
			case sideMenuHelp:
				btnsMap.get(btnName).x = this.x + this.width / 2 + 15;
				btnsMap.get(btnName).y = 50;
				break;
			case sideMenuLevelSelect:
				btnsMap.get(btnName).x = this.x + this.width / 2 - 50;
				btnsMap.get(btnName).y = 130;
				break;
			case sideMenuMainMenu:
				btnsMap.get(btnName).x = this.x + this.width / 2 - 50;
				btnsMap.get(btnName).y = 240;
				break;
			case sideMenuReset:
				btnsMap.get(btnName).x = this.x + this.width / 2 - 50;
				btnsMap.get(btnName).y = 350;
				break;
			case sideMenuSoundOn:
			case sideMenuSoundOff:
				btnsMap.get(btnName).x = this.x + this.width / 2 - 75;
				btnsMap.get(btnName).y = 50;
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

		if (btnInfoSpec.visible) {
			btnInfoSpec.update(justTouched, isTouched, justReleased,
					touchPoint.x, touchPoint.y);
			if (btnInfoSpec.wasPressed()) {
				btnInfoSpec.visible = false;
			}
			return;
		}

		Iterator<String> keySetIterator = btnsMap.keySet().iterator();
		setBtnPosition();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			btnsMap.get(btnName).update(justTouched, isTouched, justReleased,
					touchPoint.x, touchPoint.y);
		}
		keySetIterator = btnsMap.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			if (btnsMap.get(btnName).wasPressed()) {
				// hand.InitStat();
				Gdx.app.log(btnName, "wasPressed");
				switch (BtnSideMenu.getBtnSideMenu(btnName)) {
				case sideMenuGoOn:
					this.hide();
					notifier.onContinue();
					break;
				case sideMenuHelp:
					btnInfoSpec.visible = true;
					break;
				case sideMenuLevelSelect:
					notifier.onLevelSelect();
					break;
				case sideMenuMainMenu:
					notifier.onMainMenuSelect();
					break;
				case sideMenuReset:
					notifier.onGameReset();
					break;
				case sideMenuSoundOn:
					notifier.onSoundChange(Assets.Sounds.sound_loop_playing);
					// ConfigSet.userMap.get("0").setSound(
					// !ConfigSet.userMap.get("0").getSound());
					// if (!ConfigSet.userMap.get("0").getSound()) {
					// Assets.stopSound(Assets.soundMap
					// .get(Assets.Sounds.backloop));
					// } else {
					// Assets.playSound(
					// Assets.soundMap.get(Assets.Sounds.backloop),
					// true, 1);
					// }
					break;
				}
			}
		}
	}

	private void drawButtons(SpriteBatch batch) {
		Iterator<String> keySetIterator = btnsMap.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			switch (BtnSideMenu.getBtnSideMenu(btnName)) {
			case sideMenuGoOn:
			case sideMenuHelp:
			case sideMenuLevelSelect:
			case sideMenuMainMenu:
			case sideMenuReset:
				btnsMap.get(btnName).draw(batch);
				break;
			case sideMenuSoundOn:
				if (ConfigSet.userMap.get("0").getSound())
					btnsMap.get(btnName).draw(batch);
				break;
			case sideMenuSoundOff:
				if (!ConfigSet.userMap.get("0").getSound())
					btnsMap.get(btnName).draw(batch);
				break;
			}
			if (btnInfoSpec.visible)
				btnInfoSpec.draw(batch);
		}
	}

	public void hide() {
		this.actions.clear();
		if (this.x == 0 && this.isShow) {
			this.isShow = false;
			// this.actions.remove();
			hideAction = MoveTo.$(-width, 0, 0.2f);
			this.action(hideAction);
		}
	}

	public void show(String curLevel) {
		this.actions.clear();
		if (this.x == -textureBackground.getRegionWidth() && !this.isShow) {
			this.isShow = true;
			this.curLevel = curLevel;
			// this.actions.remove();
			showAction = MoveTo.$(0, 0, 0.2f);
			this.action(showAction);
		}
	}
}
