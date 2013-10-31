package com.android.ringfly.entity.mapping;

public class Grade {
	private String userId;
	private Integer gradeId;
	private String luckNums;
	private boolean lock;
	private Integer levelCleared;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public String getLuckNums() {
		return luckNums;
	}

	public void setLuckNums(String luckNums) {
		this.luckNums = luckNums;
	}

	public boolean getLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public Integer getLevelCleared() {
		return levelCleared;
	}

	public void setLevelCleared(Integer levelCleared) {
		this.levelCleared = levelCleared;
	}
}
