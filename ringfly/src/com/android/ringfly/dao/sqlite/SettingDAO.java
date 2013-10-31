package com.android.ringfly.dao.sqlite;

import com.android.ringfly.common.Assets;

public interface SettingDAO {
	// 加载设置
	public void loadSetting();

	// 保存设置
	public void saveSetting();

	// 循环播放背景音乐
	public void backLoop(boolean on, Assets.Sounds sound);

	// 推出游戏
	public void finishGame();

	// 拉升
	public void stretchSound();

	public void scoreCountSound();

}
