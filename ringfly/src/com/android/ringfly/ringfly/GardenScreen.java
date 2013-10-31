package com.android.ringfly.ringfly;

import com.android.ringfly.common.Assets;
import com.android.ringfly.common.CameraHelper;
import com.android.ringfly.common.CameraHelper.ViewportMode;
import com.android.ringfly.common.GameScreen;
import com.android.ringfly.common.MathTools;
import com.android.ringfly.common.TwoStateButton;
import com.android.ringfly.common.TwoStateButton.ButtonBoundShape;
import com.android.ringfly.configset.ConfigSet;
import com.android.ringfly.sprite.TaiJiDoorSprite;
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
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * 主菜单
 * 
 * @author fgshu
 * 
 */
public class GardenScreen extends GameScreen<RingflyGame> {
	private Stage stage;
	private SpriteBatch spriteBatch;
	private static float stageTime;
	private Action moveAction;
	private TwoStateButton btnStart;
	private Vector3 touchPoint;
	float i;
	private float curFrame = 1;
	private boolean wasTouched;
	private OrthographicCamera menuCam;
	private boolean wasBackPressed;
	private TaiJiDoorSprite doorSprite;
	private Label luckyNumLabel;
	private String winNums;

	public GardenScreen(RingflyGame game) {
		super(game);
		i = 0;
		menuCam = CameraHelper.createCamera2(ViewportMode.PIXEL_PERFECT,
				Assets.VIRTUAL_WIDTH, Assets.VIRTUAL_HEIGHT,
				Assets.pixelDensity);
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				false);
		btnStart = new TwoStateButton(Assets.btnGardenRegion,
				Assets.btnGardenDownRegion, ButtonBoundShape.RECTANGLE);

		btnStart.centerHorizontallyOn(Gdx.graphics.getWidth() / 2);
		btnStart.bottomOn(20f);

		spriteBatch = new SpriteBatch();

		float centerX = Gdx.graphics.getWidth() / 2
				- Assets.taiJiRegionMap.get("taiJiDoor").getRegionWidth() / 2;

		doorSprite = new TaiJiDoorSprite(centerX + 5, 100, game);

		luckyNumLabel = new Label("本级\n开运号码：" + winNums, Assets.uiSkin);
		luckyNumLabel.setAlignment(Align.BOTTOM | Align.CENTER);
		luckyNumLabel.setColor(Color.BLACK);
		luckyNumLabel.x = centerX + 28;
		luckyNumLabel.y = 170;

		Image imageBackground = new Image(Assets.gardenRegion);
		stage.addActor(imageBackground);
		stage.addActor(luckyNumLabel);

		// spriteBatch.setProjectionMatrix(menuCam.combined);

		// TODO Auto-generated constructor stub
	}

	public void setLuckyNumLabel(String text) {
		luckyNumLabel.setText("本级\n开运号码：" + text);
	}

	private void updateButtons(float delta) {
		// touchPoint = screenToViewport(Gdx.input.getX(), Gdx.input.getY());
		touchPoint = new Vector3(Gdx.input.getX(), Gdx.graphics.getHeight()
				- Gdx.input.getY(), 0);
		boolean justTouched = Gdx.input.justTouched();
		boolean isTouched = Gdx.input.isTouched();
		boolean justReleased = wasTouched && !isTouched;
		wasTouched = isTouched;
		btnStart.update(delta, justTouched, isTouched, justReleased,
				touchPoint.x, touchPoint.y);
		if (btnStart.wasPressed()) {
			doorSprite.play();
			Assets.playSound(Assets.Sounds.sound_door_open);
			game.settingDao.backLoop(false,
					Assets.Sounds.sound_loop_level_select);
		}
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
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		spriteBatch.begin();
		drawButtons();
		doorSprite.draw(spriteBatch);
		spriteBatch.end();
	}

	private void drawButtons() {
		btnStart.draw(spriteBatch);
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
	}

}
