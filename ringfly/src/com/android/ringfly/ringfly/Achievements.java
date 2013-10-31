package com.android.ringfly.ringfly;

public enum Achievements {
	LevelUp("LevelUp", "more than five demons killed"),

	LevelFail("LevelFail", "no rings remain or more than six demons escaped"),

	GradeUp("GradeUp", "gradeUpHaha");

	private final String summary;
	private final String text;
	private Boolean zhongJiang;

	public Boolean getZhongJiang() {
		return zhongJiang;
	}

	public void setZhongJiang(Boolean zhongJiang) {
		this.zhongJiang = zhongJiang;
	}

	private Achievements(String summary, String text, Boolean zhongJiang) {
		this.summary = summary;
		this.text = text;
		this.zhongJiang = zhongJiang;
	}

	private Achievements(String summary, String text) {
		this.summary = summary;
		this.text = text;
	}

	public String getSummary() {
		return summary;
	}

	public String getText() {
		return text;
	}

}
