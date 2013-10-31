/*
 * Copyright 2011 Rod Hyde (rod@badlydrawngames.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.android.ringfly.ringfly;

import java.util.List;

import com.android.ringfly.common.Assets;
import com.android.ringfly.common.Nature;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GameWorldNotifier implements GameWorldListener {

	private final Array<GameWorldListener> listeners;

	public GameWorldNotifier() {
		listeners = new Array<GameWorldListener>();
	}

	public void addListener(GameWorldListener listener) {
		listeners.add(listener);
	}

	@Override
	public void onGameStart() {
		for (GameWorldListener listener : listeners) {
			listener.onGameStart();
		}
	}

	@Override
	public void onGamePause() {
		for (GameWorldListener listener : listeners) {
			listener.onGamePause();
		}
	}

	@Override
	public void onGameReset() {
		for (GameWorldListener listener : listeners) {
			listener.onGameReset();
		}
	}

	@Override
	public void onGameRuning() {
		for (GameWorldListener listener : listeners) {
			listener.onGameRuning();
		}

	}

	@Override
	public void onDemonDie(String luckyNum, Integer magic, Integer gold) {
		for (GameWorldListener listener : listeners) {
			listener.onDemonDie(luckyNum, magic, gold);
		}
	}

	@Override
	public void onShot(Nature nature) {
		for (GameWorldListener listener : listeners) {
			listener.onShot(nature);
		}
	}

	@Override
	public void onContinue() {
		for (GameWorldListener listener : listeners) {
			listener.onContinue();
		}

	}

	@Override
	public void onLevelSelect() {
		for (GameWorldListener listener : listeners) {
			listener.onLevelSelect();
		}

	}

	@Override
	public void onMainMenuSelect() {
		for (GameWorldListener listener : listeners) {
			listener.onMainMenuSelect();
		}

	}

	@Override
	public void onSoundChange(Assets.Sounds sound) {
		for (GameWorldListener listener : listeners) {
			listener.onSoundChange(sound);
		}
	}

	@Override
	public void onContactStone(List<Vector2> contactPoints) {
		for (GameWorldListener listener : listeners) {
			listener.onContactStone(contactPoints);
		}

	}

	@Override
	public void onGradeUp(Boolean blnPass, Boolean win) {
		for (GameWorldListener listener : listeners) {
			listener.onGradeUp(blnPass, win);
		}

	}

	@Override
	public void onEyeUsed() {
		for (GameWorldListener listener : listeners) {
			listener.onEyeUsed();
		}
	}

	@Override
	public void onGoldChanged(int gold) {
		for (GameWorldListener listener : listeners) {
			listener.onGoldChanged(gold);
		}
	}

	@Override
	public void onMagicChanged(int magic) {
		for (GameWorldListener listener : listeners) {
			listener.onMagicChanged(magic);
		}

	}
}
