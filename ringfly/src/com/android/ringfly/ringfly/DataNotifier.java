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

import com.badlogic.gdx.utils.Array;

public class DataNotifier implements DataListener {

	private Array<DataListener> listeners;

	public DataNotifier() {
		listeners = new Array<DataListener>();
	}

	public void addListener(DataListener listener) {
		listeners.add(listener);
	}

	@Override
	public void onGoldChanged(int gold) {
		for (DataListener listener : listeners) {
			listener.onGoldChanged(gold);
		}

	}

	@Override
	public void onMagicChanged(int magic) {
		for (DataListener listener : listeners) {
			listener.onMagicChanged(magic);
		}

	}

	@Override
	public void onMetalChanged() {
		for (DataListener listener : listeners) {
			listener.onMetalChanged();
		}
	}

	@Override
	public void onWoodChanged() {
		for (DataListener listener : listeners) {
			listener.onWoodChanged();
		}

	}

	@Override
	public void onWaterChanged() {
		for (DataListener listener : listeners) {
			listener.onWaterChanged();
		}
	}

	@Override
	public void onFireChanged() {
		for (DataListener listener : listeners) {
			listener.onFireChanged();
		}

	}

	@Override
	public void onEarthChanged() {
		for (DataListener listener : listeners) {
			listener.onEarthChanged();
		}

	}

	@Override
	public void onAllChanged() {
		for (DataListener listener : listeners) {
			listener.onAllChanged();
		}
	}

	@Override
	public void onFeiLeiChanged() {
		for (DataListener listener : listeners) {
			listener.onFeiLeiChanged();
		}
	}

	@Override
	public void onFeiDanChanged() {
		for (DataListener listener : listeners) {
			listener.onFeiDanChanged();
		}
	}

	@Override
	public void onEyeChanged() {
		for (DataListener listener : listeners) {
			listener.onEyeChanged();
		}
	}
}
