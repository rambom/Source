package com.android.ringfly.common;

import com.android.ringfly.actor.ConfirmActor;
import com.badlogic.gdx.Screen;

public class GameScreen<T> implements Screen {
	protected T game;
	protected ConfirmActor confirmActor;

	public GameScreen(T game) {
		this.game = game;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void render(float delta) {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
	}

	@Override
	public void show() {
	}
}
