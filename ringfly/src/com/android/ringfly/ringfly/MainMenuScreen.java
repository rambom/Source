package com.android.ringfly.ringfly;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import com.android.ringfly.actor.ConfirmActor;
import com.android.ringfly.common.Assets;
import com.android.ringfly.common.CameraHelper;
import com.android.ringfly.common.CameraHelper.ViewportMode;
import com.android.ringfly.common.CommonTools;
import com.android.ringfly.common.GameScreen;
import com.android.ringfly.common.TwoStateButton;
import com.android.ringfly.common.TwoStateButton.ButtonBoundShape;
import com.android.ringfly.configset.ConfigSet;
import com.android.ringfly.ui.control.FlipView;
import com.android.ringfly.ui.control.FlipView.ENUM_FLIP_DIRECTION;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.actions.Parallel;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * 主菜单
 * 
 * @author fgshu
 * 
 */
public class MainMenuScreen extends GameScreen<RingflyGame> {
	private Stage stage;
	private SpriteBatch spriteBatch;
	private static float stageTime;
	private Action moveAction;
	private TwoStateButton btnStart, btnInfo, btnInfoSpec, btnSoundOn,
			btnSoundOff;
	private Vector3 touchPoint;
	Image image;
	float i;
	private float curFrame = 1;
	private boolean wasTouched;
	private OrthographicCamera menuCam;
	private boolean wasBackPressed;
	private Label labelInfo;

	public MainMenuScreen(RingflyGame game) {
		super(game);
		i = 0;
		menuCam = CameraHelper.createCamera2(ViewportMode.PIXEL_PERFECT,
				Assets.VIRTUAL_WIDTH, Assets.VIRTUAL_HEIGHT,
				Assets.pixelDensity);
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				false);
		btnStart = new TwoStateButton(Assets.mainMenuButtons.get("btnStartUp"),
				Assets.mainMenuButtons.get("btnStartDown"),
				ButtonBoundShape.RECTANGLE);
		btnInfoSpec = new TwoStateButton(Assets.mainMenuButtons.get("info"),
				ButtonBoundShape.CIRCLE);
		btnInfoSpec.visible = false;
		btnInfo = new TwoStateButton(Assets.mainMenuButtons.get("btnInfo"),
				ButtonBoundShape.CIRCLE);
		btnSoundOn = new TwoStateButton(
				Assets.mainMenuButtons.get("btnSoundOn"),
				ButtonBoundShape.CIRCLE);
		btnSoundOff = new TwoStateButton(
				Assets.mainMenuButtons.get("btnSoundOff"),
				ButtonBoundShape.CIRCLE);
		btnStart.centerHorizontallyOn(Gdx.graphics.getWidth() / 2);
		btnStart.bottomOn(152f);

		btnInfo.leftOn(650f);
		btnInfo.bottomOn(10f);
		btnSoundOn.leftOn(720f);
		btnSoundOn.bottomOn(10f);
		btnSoundOff.leftOn(720f);
		btnSoundOff.bottomOn(10f);

		spriteBatch = new SpriteBatch();
		// spriteBatch.setProjectionMatrix(menuCam.combined);

		image = new Image(Assets.rings.get("MissileRing"));
		image.x = 10;
		image.y = 0;
		stage.addActor(image);

		labelInfo = CommonTools.getLabel("试用过期", Color.RED);
		labelInfo.x = 338;
		labelInfo.y = 120;
		stage.addActor(labelInfo);
		labelInfo.visible = false;

		confirmActor = new ConfirmActor("confirmActor", game.settingDao);
		stage.addActor(confirmActor);
		confirmActor.visible = false;
		// TODO Auto-generated constructor stub
		// test code
		// FlipView flipView = new FlipView("flip",
		// ENUM_FLIP_DIRECTION.Horizontal, Assets.backgroundRegion00,
		// Assets.backgroundRegion01, Assets.backgroundRegion00,
		// Assets.backgroundRegion01);
		// flipView.setBackGround(Assets.backgroundRegion01);
		// flipView.setFlexibleX(10);
		// stage.addActor(flipView);
		game.inputMultiplexer.addProcessor(stage);
		//stage.setTouchFocus(flipView, 0);
	}

	private void updateButtons(float delta) {
		if (confirmActor.visible == true)
			return;
		// touchPoint = screenToViewport(Gdx.input.getX(), Gdx.input.getY());
		touchPoint = new Vector3(Gdx.input.getX(), Gdx.graphics.getHeight()
				- Gdx.input.getY(), 0);
		boolean justTouched = Gdx.input.justTouched();
		boolean isTouched = Gdx.input.isTouched();
		boolean justReleased = wasTouched && !isTouched;
		wasTouched = isTouched;
		if (btnInfoSpec.visible) {
			btnInfoSpec.update(delta, justTouched, isTouched, justReleased,
					touchPoint.x, touchPoint.y);
			if (btnInfoSpec.wasPressed()) {
				btnInfoSpec.visible = false;
			}
			return;
		}
		btnStart.update(delta, justTouched, isTouched, justReleased,
				touchPoint.x, touchPoint.y);
		btnInfo.update(delta, justTouched, isTouched, justReleased,
				touchPoint.x, touchPoint.y);
		btnSoundOn.update(delta, justTouched, isTouched, justReleased,
				touchPoint.x, touchPoint.y);

		if (btnStart.wasPressed()) {
			if (CheckExpiryDate())
				return;
			game.setScreen(game.seasonSelectScreen);
			game.settingDao.backLoop(false, Assets.Sounds.sound_loop_enter);
			game.settingDao.backLoop(true,
					Assets.Sounds.sound_loop_level_select);
		}
		if (btnSoundOn.wasPressed()) {
			game.onSoundChange(Assets.Sounds.sound_loop_enter);
		}
		if (btnInfo.wasPressed()) {
			// Gdx.app.log("info", "pressed");
			// game.settingDao.saveSetting();
			btnInfoSpec.visible = true;
		}
	}

	private Boolean CheckExpiryDate() {
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		DateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
		Date outDate = null;
		try {
			outDate = df.parse(Assets.GAME_EXPIRY_DATE);
		} catch (ParseException e) {
		}
		if (curDate.after(outDate)) {
			Gdx.app.log("out of date", "pelease charge");
			labelInfo.visible = true;
			return true;
		}
		return false;
	}

	@Override
	public void hide() {
		Gdx.input.setCatchBackKey(false);
		wasBackPressed = false;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		updateButtons(delta);

		boolean isBackPressed = Gdx.input.isKeyPressed(Input.Keys.BACK);
		if (!wasBackPressed && isBackPressed) {
			while (Gdx.input.isKeyPressed(Input.Keys.BACK))
				;
			confirmActor.visible = true;
		}
		wasBackPressed = isBackPressed;

		stageTime += Gdx.graphics.getDeltaTime();
		if (stageTime > curFrame) {
			curFrame += MathUtils.random(0, 0.02f);
			i += MathUtils.random(0.5f, 1f);
		}
		super.render(delta);
		GL10 gl = Gdx.app.getGraphics().getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		spriteBatch.draw(Assets.backgroundRegion00, 0, 0);
		drawButtons();
		spriteBatch.end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

	}

	private void drawButtons() {
		btnStart.draw(spriteBatch);
		btnInfo.draw(spriteBatch);
		if (ConfigSet.userMap.get("0").getSound()) {
			btnSoundOn.draw(spriteBatch);
		} else {
			btnSoundOff.draw(spriteBatch);
		}
		if (btnInfoSpec.visible)
			btnInfoSpec.draw(spriteBatch);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
	}

	@Override
	public void resume() {
		Gdx.input.setCatchBackKey(true);
		wasBackPressed = false;
	}

	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
		wasBackPressed = false;
		moveAction = Sequence.$(MoveTo.$(10, 600, 5f));
		image.action(Parallel.$(moveAction));
	}

}
