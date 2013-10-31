package com.android.ringfly.ringfly;

import java.util.HashMap;
import java.util.Iterator;
import com.android.ringfly.common.Assets;
import com.android.ringfly.common.Assets.BtnGrade;
import com.android.ringfly.common.Assets.BtnLevel;
import com.android.ringfly.common.GameScreen;
import com.android.ringfly.common.TwoStateButton;
import com.android.ringfly.common.TwoStateButton.ButtonBoundShape;
import com.android.ringfly.configset.ConfigSet;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class LevelSelectScreen extends GameScreen<RingflyGame> {
	private SpriteBatch spriteBatch;
	private HashMap<String, TwoStateButton> btnsMap, btnGrade;
	private Vector3 touchPoint;
	private boolean wasTouched;
	private TextureRegion background;
	private boolean wasBackPressed;
	private static final float deltaX = 66f;
	private static final float deltaY = 75f;
	private static final float left_startX = 130f;
	private static final float right_startX = 400f;
	private static final float startY = 280f;

	public LevelSelectScreen(RingflyGame game) {
		super(game);
		spriteBatch = new SpriteBatch();
		background = new TextureRegion(Assets.backgroundRegionLevel);

	}

	private HashMap<String, TwoStateButton> createGrade() {
		HashMap<String, TwoStateButton> map = new HashMap<String, TwoStateButton>();
		int i = 0;
		for (BtnGrade btn : BtnGrade.values()) {
			TwoStateButton tb;
			int gradeid = Cookie.getSeason().ordinal() * 6 + btn.ordinal() + 1;
			if (ConfigSet.gradeMap.get(gradeid).getLock()) {
				tb = new TwoStateButton(Assets.gradeButtons.get(btn.name()
						+ "Locked"), ButtonBoundShape.CIRCLE);
				Gdx.app.log(gradeid + "", "Locked");
			} else {
				tb = new TwoStateButton(Assets.gradeButtons.get(btn.name()),
						ButtonBoundShape.CIRCLE);
			}
			tb.x = i % 2 == 0 ? 330 : 600;
			tb.y = startY - (i / 2) * deltaY;
			map.put(btn.name(), tb);
			i++;
		}
		return map;
	}

	private static HashMap<String, TwoStateButton> createBtnLevel() {
		HashMap<String, TwoStateButton> map = new HashMap<String, TwoStateButton>();
		int i = 0, j = 0, k = 0;
		for (BtnLevel btnRegion : BtnLevel.values()) {
			TwoStateButton tb = null;
			if (!ConfigSet.levelMap.get(
					Cookie.getSeason().ordinal() * 18 + k + 1).getLock()) {
				tb = new TwoStateButton(Assets.levelButtons.get("level"
						+ (i + 1)), ButtonBoundShape.CIRCLE);
			} else {
				tb = new TwoStateButton(Assets.levelButtons.get("levelLocked"),
						ButtonBoundShape.CIRCLE);
			}
			tb.x = j % 2 == 0 ? left_startX + i * deltaX : right_startX + i
					* deltaX;
			tb.y = startY - (j / 2) * deltaY;
			map.put(btnRegion.name(), tb);
			i++;
			k++;
			if (i == 3) {
				i = 0;
				j++;
			}
		}
		return map;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		boolean isBackPressed = Gdx.input.isKeyPressed(Input.Keys.BACK);
		if (!wasBackPressed && isBackPressed) {
			while (Gdx.input.isKeyPressed(Input.Keys.BACK))
				;
			game.setScreen(game.seasonSelectScreen);
		}
		wasBackPressed = isBackPressed;
		updateButtons(delta);
		spriteBatch.begin();
		spriteBatch.draw(background, 0, 0);
		drawButtons();
		// drawButtons();
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
				Integer ordinal = BtnLevel.getBtnLevel(btnName).ordinal();
				Integer curGrade = ordinal / 3 + 1;
				if (!ConfigSet.levelMap.get(ordinal + 1).getLock()
						|| Assets.GAME_ISDEBUG_MODE) {
					String winNum = "";
					if (ordinal % 3 == 0) {
						for (int i = 0; i < 3; i++) {
							winNum += MathUtils.random(0, 9);
						}
						ConfigSet.gradeMap.get(curGrade).setLuckNums(winNum);
					} else {
						winNum = ConfigSet.gradeMap.get(curGrade).getLuckNums();
					}
					((GardenScreen) game.gardenScreen).setLuckyNumLabel(winNum);

					game.setScreen(game.gardenScreen, ordinal);
				} else {
					Gdx.app.log(Cookie.getSeason() + btnName, "is locked");
				}
			}
		}
		keySetIterator = btnGrade.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			btnGrade.get(btnName).update(delta, justTouched, isTouched,
					justReleased, touchPoint.x, touchPoint.y);
		}
		keySetIterator = btnGrade.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String grade = keySetIterator.next();
			if (btnGrade.get(grade).wasPressed()) {
				int gradeid = Cookie.getSeason().ordinal() * 6
						+ BtnGrade.getBtnGrade(grade).ordinal() + 1;
				if (!ConfigSet.gradeMap.get(gradeid).getLock()) {
					Gdx.app.log(MathUtils.random(0, 999) + "" + grade, "Ö¥Âé¿ªÃÅ");
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
		keySetIterator = btnGrade.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			btnGrade.get(btnName).draw(spriteBatch);
		}
	}

	@Override
	public void resume() {
		Gdx.input.setCatchBackKey(true);
		btnsMap = createBtnLevel();
		btnGrade = createGrade();
		wasBackPressed = false;
		super.resume();
	}

	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
		btnsMap = createBtnLevel();
		btnGrade = createGrade();
		wasBackPressed = false;
		super.show();
	}

	@Override
	public void hide() {
		Gdx.input.setCatchBackKey(false);
		wasBackPressed = false;
	}

}
