package com.android.ringfly.ringfly;

import com.android.ringfly.common.GameScreen;
import com.android.ringfly.ringfly.Cookie.StatEnum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;

public class PlayingScreen extends GameScreen<RingflyGame> {
	public final GameWorld gameWorld;
	private final StatusManager statusManager;
	private final StatusView statusView;
	private boolean wasBackPressed;
	private LevelConfig levelConfig;

	public PlayingScreen(RingflyGame game, LevelConfig levelConfig) {
		super(game);
		this.levelConfig = levelConfig;
		Cookie.setCurLevelConfig(levelConfig);
		gameWorld = new GameWorld(this.game, this, levelConfig);
		statusManager = new StatusManager();
		statusView = new StatusView(gameWorld);
		statusManager.addAchievementsListener(game);
		statusManager.addAchievementsListener(statusView);
		statusManager.addDataListener(statusView);
		gameWorld.addGameWorldListener(statusManager);
		gameWorld.addGameWorldListener(game);
		gameWorld.notifyApple();
		gameWorld.notifyDemon();
		gameWorld.notifyStone();
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
		GL10 gl = Gdx.app.getGraphics().getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		boolean isBackPressed = Gdx.input.isKeyPressed(Input.Keys.BACK);
		if (!wasBackPressed && isBackPressed) {
			while (Gdx.input.isKeyPressed(Input.Keys.BACK))
				;
			gameWorld.confirmActor.visible = true;
		}
		wasBackPressed = isBackPressed;
		gameWorld.update(gl, delta);
		statusManager.update(delta);
		statusView.render(delta);
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
		Cookie.setState(StatEnum.PLAYING);
		Gdx.app.log("PlayScreen", "show");
	}
}
