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

public interface DataListener {
	/**
	 * 金币改变
	 * 
	 * @param gold
	 */
	void onGoldChanged(int gold);

	/**
	 * 魔力改变
	 * 
	 * @param lives
	 */
	void onMagicChanged(int magic);

	void onMetalChanged();

	void onWoodChanged();

	void onWaterChanged();

	void onFireChanged();

	void onEarthChanged();

	void onAllChanged();
	
	void onEyeChanged();

	void onFeiLeiChanged();

	void onFeiDanChanged();
}
