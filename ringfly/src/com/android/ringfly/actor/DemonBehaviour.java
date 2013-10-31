package com.android.ringfly.actor;

import java.util.List;

public interface DemonBehaviour {
	// 死亡
	public void die();
	
	
	public void hitFail();

	// 行走
	public void run(boolean ifShow);

	// 吸人参果
	public void eat(List<AppleActor> actors);
	
	//闪烁
	public void blink(int times);
	
	//暂停
	public void pause();
	
	//继续
	public void goon();
	
}
