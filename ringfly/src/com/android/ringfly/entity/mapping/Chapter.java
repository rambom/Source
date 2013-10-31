package com.android.ringfly.entity.mapping;

public class Chapter {
	private String userid;
	private Integer seasonId;
	private String season;
	private boolean lock;
	private Double score;
	private Integer levelCleared;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Integer getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(Integer seasonId) {
		this.seasonId = seasonId;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public boolean getLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Integer getLevelCleared() {
		return levelCleared;
	}

	public void setLevelCleared(Integer levelCleared) {
		this.levelCleared = levelCleared;
	}

}
