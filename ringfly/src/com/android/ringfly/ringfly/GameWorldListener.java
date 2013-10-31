package com.android.ringfly.ringfly;

import java.util.List;

import com.android.ringfly.common.Assets;
import com.android.ringfly.common.Nature;
import com.badlogic.gdx.math.Vector2;

public interface GameWorldListener {
	/**
	 * 开始
	 */
	public void onGameStart();

	/**
	 * 暂停
	 */
	public void onGamePause();

	/**
	 * 重置
	 */
	public void onGameReset();

	/**
	 * 运行
	 */
	public void onGameRuning();

	/**
	 * 继续
	 */
	public void onContinue();

	/**
	 * 声音按钮
	 * 
	 */
	public void onSoundChange(Assets.Sounds sound);

	/**
	 * 命中
	 */
	public void onDemonDie(String luckyNum, Integer magic, Integer gold);

	/**
	 * 眼睛
	 */
	public void onEyeUsed();

	/**
	 * 发射
	 */
	public void onShot(Nature nature);

	/**
	 * 边侧菜单关卡选择
	 */
	public void onLevelSelect();

	/**
	 * 边侧菜单主菜单选择
	 */
	public void onMainMenuSelect();

	/**
	 * 
	 */
	public void onContactStone(List<Vector2> contactPoints);

	/**
	 * 升级
	 */
	public void onGradeUp(Boolean blnPass, Boolean win);

	public void onGoldChanged(int gold);

	public void onMagicChanged(int magic);

}
