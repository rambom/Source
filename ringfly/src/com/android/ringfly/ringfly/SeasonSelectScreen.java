package com.android.ringfly.ringfly;

import java.util.HashMap;
import java.util.Iterator;

import com.android.ringfly.common.Assets;
import com.android.ringfly.common.Assets.BtnSeason;
import com.android.ringfly.common.Assets.Season;
import com.android.ringfly.common.GameScreen;
import com.android.ringfly.common.TwoStateButton;
import com.android.ringfly.common.TwoStateButton.ButtonBoundShape;
import com.android.ringfly.configset.ConfigSet;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class SeasonSelectScreen extends GameScreen<RingflyGame> {
	private SpriteBatch spriteBatch;
	private HashMap<String, TwoStateButton> btnsMap;
	private Vector3 touchPoint;
	private boolean wasTouched;
	private TextureRegion background;
	private boolean wasBackPressed;

	public SeasonSelectScreen(RingflyGame game) {
		super(game);
		spriteBatch = new SpriteBatch();
		background = new TextureRegion(Assets.backgroundRegionSeason);
		btnsMap = createBtnSeason();
	}

	private static HashMap<String, TwoStateButton> createBtnSeason() {
		HashMap<String, TwoStateButton> map = new HashMap<String, TwoStateButton>();
		for (BtnSeason btnRegion : BtnSeason.values()) {
			int ordinal = btnRegion.ordinal();
			TwoStateButton tb;
			if (ConfigSet.chapterMap.get(Season.values()[ordinal].name())
					.getLock()) {
				tb = new TwoStateButton(Assets.seasonButtons.get(btnRegion
						.name() + "Locked"), ButtonBoundShape.RECTANGLE);
				Gdx.app.log(btnRegion.name(), "draw a lock");
			} else {
				tb = new TwoStateButton(Assets.seasonButtons.get(btnRegion
						.name()), ButtonBoundShape.RECTANGLE);
			}
			float deltaX = 2;
			float posY = 200;
			float centerX = Gdx.graphics.getWidth() / 2;
			float btnWidth = Assets.seasonButtons.get(btnRegion.name())
					.getRegionWidth();

			switch (btnRegion) {
			case btnSpring:
				tb.x = centerX - 2 * btnWidth - 3 * deltaX;
				break;
			case btnAutumn:
				tb.x = centerX + deltaX;
				break;
			case btnSummer:
				tb.x = centerX - btnWidth - deltaX;
				break;
			case btnWinter:
				tb.x = centerX + btnWidth + 3 * deltaX;
				break;
			}
			tb.y = posY;
			map.put(btnRegion.name(), tb);
		}
		return map;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		boolean isBackPressed = Gdx.input.isKeyPressed(Input.Keys.BACK);
		if (!wasBackPressed && isBackPressed) {
			Gdx.app.log("back was", "pressed");
			while (Gdx.input.isKeyPressed(Input.Keys.BACK))
				;
			game.setScreen(game.mainMenuScreen);
		}
		wasBackPressed = isBackPressed;
		updateButtons(delta);
		GL10 gl = Gdx.app.getGraphics().getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		spriteBatch.draw(background, 0, 0);
		drawButtons();
		spriteBatch.end();

	}

	private void updateButtons(float delta) {
		// touchPoint = screenToViewport(Gdx.input.getX(), Gdx.input.getY());
		touchPoint = new Vector3(Gdx.input.getX(), Gdx.graphics.getHeight()
				- Gdx.input.getY(), 0);
		boolean justTouched = Gdx.input.justTouched();
		boolean isTouched = Gdx.input.isTouched();
		boolean justReleased = wasTouched && !isTouched;
		wasTouched = isTouched;
		Iterator<String> keySetIterator = btnsMap.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			btnsMap.get(btnName).update(delta, justTouched, isTouched,
					justReleased, touchPoint.x, touchPoint.y);
		}
		keySetIterator = btnsMap.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			if (btnsMap.get(btnName).wasPressed()) {
				Gdx.app.log(btnName, "wasPressed");
				switch (BtnSeason.getBtnSeason(btnName)) {
				case btnSpring:
					if (!ConfigSet.chapterMap.get("spring").getLock()) {
						game.setScreen(game.levelSelectScreen);
						Cookie.setSeason(Season.spring);
					} else
						Gdx.app.log("it's", "locked");
					break;
				case btnSummer:
					if (!ConfigSet.chapterMap.get("summer").getLock()) {
						game.setScreen(game.levelSelectScreen);
						Cookie.setSeason(Season.summer);
					} else
						Gdx.app.log("it's", "locked");
					break;
				case btnAutumn:
					if (!ConfigSet.chapterMap.get("autumn").getLock()) {
						game.setScreen(game.levelSelectScreen);
						Cookie.setSeason(Season.autumn);
					} else
						Gdx.app.log("it's", "locked");
					break;
				case btnWinter:
					if (!ConfigSet.chapterMap.get("winter").getLock()) {
						game.setScreen(game.levelSelectScreen);
						Cookie.setSeason(Season.winter);
					} else
						Gdx.app.log("it's", "locked");
					break;
				}
			}
		}
	}

	private void drawButtons() {
		Iterator<String> keySetIterator = btnsMap.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			btnsMap.get(btnName).draw(spriteBatch);
		}
	}

	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
		wasBackPressed = false;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	@Override
	public void hide() {
		Gdx.input.setCatchBackKey(false);
		wasBackPressed = false;
		wasTouched = false;
		super.hide();
	}

	@Override
	public void resume() {
		Gdx.input.setCatchBackKey(true);
		wasBackPressed = false;
		super.resume();
	}

}
