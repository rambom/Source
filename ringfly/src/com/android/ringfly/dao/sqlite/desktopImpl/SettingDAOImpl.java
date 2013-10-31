package com.android.ringfly.dao.sqlite.desktopImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

import com.android.ringfly.common.Assets;
import com.android.ringfly.configset.ConfigSet;
import com.android.ringfly.dao.sqlite.SettingDAO;
import com.android.ringfly.entity.mapping.Chapter;
import com.android.ringfly.entity.mapping.Grade;
import com.android.ringfly.entity.mapping.Level;
import com.android.ringfly.entity.mapping.User;
import com.android.ringfly.ringfly.Setting;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SettingDAOImpl implements SettingDAO {

	String currentUserId = "0";

	public void loadSetting() {
		ConfigSet.userMap.put(currentUserId, getUser(currentUserId));
		ConfigSet.chapterMap.putAll(getChapter(currentUserId));
		ConfigSet.gradeMap.putAll(getGrade(currentUserId));
		ConfigSet.levelMap.putAll(getLevel(currentUserId));
	}

	public void saveSetting() {
		saveUser(ConfigSet.userMap.get(currentUserId));
		saveChapter(ConfigSet.chapterMap);
		saveGrade(ConfigSet.gradeMap);
		saveLevel(ConfigSet.levelMap);
	}

	private Connection getCon() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:user.sqlite");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	private void saveGrade(HashMap<Integer, Grade> gradeMap) {
		if (gradeMap == null)
			return;
		try {
			Connection conn = getCon();
			conn.setAutoCommit(false);
			Statement stat = conn.createStatement();
			String sqldelete = "delete from grade where userid='0'";
			String sqlInsert = "insert into grade values(?,?,?,?,?)";
			stat.execute(sqldelete);
			PreparedStatement prep = conn.prepareStatement(sqlInsert);
			Iterator<Integer> keySetIterator = gradeMap.keySet().iterator();
			while (keySetIterator.hasNext()) {
				Integer gradeid = keySetIterator.next();
				Grade grade = gradeMap.get(gradeid);
				prep.setString(1, grade.getUserId());
				prep.setInt(2, grade.getGradeId());
				prep.setString(3, grade.getLuckNums());
				prep.setBoolean(4, grade.getLock());
				prep.setInt(5, grade.getLevelCleared());
				prep.addBatch();
			}
			prep.executeBatch();
			conn.commit();
			Gdx.app.log("grade", "saved");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void saveChapter(HashMap<String, Chapter> chapter) {
		if (chapter == null)
			return;
		try {
			Connection conn = getCon();
			conn.setAutoCommit(false);
			Statement stat = conn.createStatement();
			String sqldelete = "delete from chapter where userid='0'";
			String sqlInsert = "insert into chapter values(?,?,?,?,?,?)";
			stat.execute(sqldelete);
			PreparedStatement prep = conn.prepareStatement(sqlInsert);
			Iterator<String> keySetIterator = chapter.keySet().iterator();
			while (keySetIterator.hasNext()) {
				String seasonname = keySetIterator.next();
				Chapter season = chapter.get(seasonname);
				prep.setString(1, season.getUserid());
				prep.setInt(2, season.getSeasonId());
				prep.setString(3, season.getSeason());
				prep.setBoolean(4, season.getLock());
				prep.setDouble(5, season.getScore());
				prep.setInt(6, season.getLevelCleared());
				prep.addBatch();
			}
			prep.executeBatch();
			conn.commit();
			Gdx.app.log("chapter", "saved");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private HashMap<Integer, Grade> getGrade(String currentUserId) {
		HashMap<Integer, Grade> gradeMap = new HashMap<Integer, Grade>();
		Grade grade;
		String sqlQuery = String.format("select * from grade where userid=%s",
				currentUserId);
		float size = 0;
		Connection conn = getCon();
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sqlQuery);
			while (rs.next()) {
				grade = new Grade();
				grade.setUserId(rs.getString("userid"));
				grade.setGradeId(rs.getInt("gradeid"));
				grade.setLuckNums(rs.getString("luckyNums"));
				grade.setLock(rs.getInt("lock") == 1 ? true : false);
				grade.setLevelCleared(rs.getInt("level_cleared"));
				gradeMap.put(grade.getGradeId(), grade);
				size++;
			}
			// 24小节默认设置
			if (size == 0) {
				for (int i = 0; i < 24; i++) {
					grade = new Grade();
					grade.setUserId(currentUserId);
					grade.setGradeId(i + 1);
					grade.setLuckNums("");
					grade.setLock(i == 0 ? false : true);
					grade.setLevelCleared(0);
					gradeMap.put(grade.getGradeId(), grade);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			return gradeMap;
		}
	}

	private HashMap<String, Chapter> getChapter(String currentUserId) {
		HashMap<String, Chapter> chapter = new HashMap<String, Chapter>();
		Chapter season;
		String sqlQuery = String.format(
				"select * from chapter where userid=%s", currentUserId);
		float size = 0;
		Connection conn = getCon();
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sqlQuery);
			while (rs.next()) {
				season = new Chapter();
				season.setUserid(rs.getString("userid"));
				season.setSeasonId(rs.getInt("season_id"));
				season.setSeason(rs.getString("season"));
				season.setLock(rs.getBoolean("lock"));
				season.setScore(rs.getDouble("score"));
				season.setLevelCleared(rs.getInt("level_cleared"));
				chapter.put(season.getSeason(), season);
				size++;
			}
			// 默认章节设置
			if (size == 0) {
				// 春夏秋冬
				for (int i = 0; i < 4; i++) {
					season = new Chapter();
					season.setUserid(currentUserId);
					season.setSeasonId(i);
					season.setLock(true);
					season.setScore(0d);
					season.setLevelCleared(0);
					switch (i) {
					case 0:// 春
						season.setSeason("spring");// 春默认解锁
						season.setLock(false);
						season.setLevelCleared(1); // 春默认解锁第一关
						break;
					case 1:// 夏
						season.setSeason("summer");
						break;
					case 2:// 秋
						season.setSeason("autumn");
						break;
					case 3:// 冬
						season.setSeason("winter");
						break;
					}
					chapter.put(season.getSeason(), season);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			return chapter;
		}
	}

	private void saveLevel(HashMap<Integer, Level> levelMap) {
		try {
			if (levelMap == null)
				return;
			Connection conn = getCon();
			conn.setAutoCommit(false);
			Statement stat = conn.createStatement();
			String sqldelete = "delete from level where userid='0'";
			String sqlInsert = "insert into level values(?,?,?,?,?,?,?,?,?,?)";
			stat.execute(sqldelete);
			PreparedStatement prep = conn.prepareStatement(sqlInsert);
			Iterator<Integer> keySetIterator = levelMap.keySet().iterator();
			while (keySetIterator.hasNext()) {
				Integer levelNum = keySetIterator.next();
				Level level = levelMap.get(levelNum);
				prep.setString(1, level.getUserid());
				prep.setInt(2, level.getSeasonId());
				prep.setInt(3, level.getLevel());
				prep.setBoolean(4, level.getLock());
				prep.setDouble(5, level.getStars());
				prep.setDouble(6, level.getMagic());
				prep.setDouble(7, level.getCoin());
				prep.setString(8, level.getDemoneacapenums());
				prep.setString(9, level.getDemondisappearnum());
				prep.setString(10, level.getDemondienum());
				prep.addBatch();
			}
			prep.executeBatch();
			conn.commit();
			Gdx.app.log("level cofig", "saved");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private HashMap<Integer, Level> getLevel(String currentUserId) {
		HashMap<Integer, Level> levelMap = new HashMap<Integer, Level>();
		Level level;
		String sqlQuery = String.format("select * from level where userid=%s",
				currentUserId);
		float size = 0;
		Connection conn = getCon();
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sqlQuery);
			while (rs.next()) {
				level = new Level();
				level.setUserid(rs.getString("userid"));
				level.setSeasonId(rs.getInt("season_id"));
				level.setLevel(rs.getInt("level"));
				level.setLock(rs.getBoolean("lock"));
				level.setStars(rs.getInt("stars"));
				level.setCoin(rs.getDouble("coin"));
				level.setMagic(rs.getDouble("magic"));
				level.setDemondienum(rs.getString("demondienum"));
				level.setDemondisappearnum(rs.getString("demondisappearnum"));
				level.setDemoneacapenums(rs.getString("demoneacapenums"));
				levelMap.put(level.getLevel(), level);
				size++;
			}
			// 默认关卡设置
			if (size == 0) {
				for (int i = 0; i < 72; i++) {
					level = new Level();
					level.setUserid(currentUserId);
					level.setSeasonId(i / 18 + 1);
					level.setLevel(i + 1);
					level.setLock(i == 0 ? false : true);
					level.setStars(0);
					level.setCoin(0);
					level.setMagic(0);
					level.setDemondienum("");
					level.setDemondisappearnum("");
					level.setDemoneacapenums("");
					levelMap.put(level.getLevel(), level);
					System.out.println(level.getUserid() + " "
							+ level.getSeasonId() + " " + level.getLevel()
							+ " " + level.getLock() + " " + level.getStars());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			return levelMap;
		}
	}

	private void saveUser(User user) {
		try {
			if (user == null)
				return;
			Connection conn = getCon();
			conn.setAutoCommit(false);
			Statement stat = conn.createStatement();
			String sqldelete = "delete from user where userid='0'";
			String sqlInsert = "insert into user values(?,?,?,?,?,?)";
			stat.execute(sqldelete);
			PreparedStatement prep = conn.prepareStatement(sqlInsert);
			prep.setString(1, user.getUserid());
			prep.setString(2, user.getUsername());
			prep.setString(3, user.getPassword());
			prep.setBoolean(4, user.getSound());
			prep.setDouble(5, user.getCoin());
			prep.setDouble(6, user.getMagic());
			prep.addBatch();
			prep.executeBatch();
			conn.commit();
			Gdx.app.log("user cofig", "saved");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private User getUser(String currentUserId) {
		User user = new User();
		Integer size = 0;

		try {
			Connection conn = getCon();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(true);
			String sqlQuery = String.format(
					"select * from user where userid=%s", currentUserId);
			ResultSet rs = stat.executeQuery(sqlQuery);
			while (rs.next()) {
				System.out.println("username = " + rs.getString("username"));
				System.out.println("password = " + rs.getString("password"));
				user.setUserid(currentUserId);
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setSound(rs.getBoolean("sound"));
				user.setCoin(rs.getDouble("coin"));
				user.setMagic(rs.getDouble("magic"));
				size++;
			}
			// 默认用户设置
			if (size == 0) {
				user.setUserid(currentUserId);
				user.setUsername("fgshu");
				user.setPassword("dcjet");
				user.setSound(true);
				user.setCoin(Setting.INIT_GOLD);
				user.setMagic(Setting.INIT_MAGIC);
				System.out.println("null");
			}

			rs.close();
			stat.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			return user;
		}
	}

	@Override
	public void backLoop(boolean on, Assets.Sounds sound) {
		if (on) {
			Assets.stopSound(sound);
			Assets.playSound(sound, true, 1);
		} else {
			Assets.stopSound(sound);
		}

	}

	@Override
	public void finishGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stretchSound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void scoreCountSound() {
		// TODO Auto-generated method stub

	}

}
