package com.android.ringfly.actor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.android.ringfly.common.Assets;
import com.android.ringfly.common.Assets.BtnLevelUp;
import com.android.ringfly.common.CommonTools;
import com.android.ringfly.common.TwoStateButton;
import com.android.ringfly.common.TwoStateButton.ButtonBoundShape;
import com.android.ringfly.configset.ConfigSet;
import com.android.ringfly.dao.sqlite.SettingDAO;
import com.android.ringfly.ringfly.Cookie;
import com.android.ringfly.ringfly.GameWorldNotifier;
import com.android.ringfly.ringfly.Setting;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GradeUp extends Actor {
	private TextureRegion textureBackground;
	private Sprite background;
	private Action showAction, hideAction;
	public boolean isShow;
	private String curLevel;
	private HashMap<String, TwoStateButton> btnsMap;
	private float width, height, centerX, centerY;
	private Vector2 touchPoint;
	private boolean wasTouched;
	private Label levelLabel, congratulationLabel, goldLabel, magicLabel,
			luckyNumsLabel, winNumsLabel;
	private final GameWorldNotifier notifier;
	private boolean nextAble;
	private Pixmap pixmap;
	private Texture maskTexture;
	private Sprite maskSprite;
	private int gredeUpGoldTo, gradeUpMagicTo;
	private float lastTime, curTime;
	private SettingDAO settingDAO;

	public GradeUp(GameWorldNotifier notifier, String name, SettingDAO dao) {
		super(name);
		this.notifier = notifier;
		this.settingDAO = dao;
		textureBackground = Assets.backgroundRegionLevelUp;
		this.width = textureBackground.getRegionWidth();
		this.height = textureBackground.getRegionHeight();
		this.centerX = this.width / 2 - 55;
		this.centerY = this.height / 2 - 60;
		this.x = Gdx.graphics.getWidth() / 2 - this.width / 2;
		this.y = Gdx.graphics.getHeight() / 2 - this.height / 2;
		background = new Sprite(textureBackground);
		this.curLevel = "";
		Color color = Color.YELLOW;
		congratulationLabel = getLabel("本级中奖!", color, Assets.gradeUpSkin);
		goldLabel = getLabel("金币:" + Cookie.gold, color, Assets.uiSkin);
		magicLabel = getLabel("魔力:" + Cookie.magic, color, Assets.uiSkin);

		levelLabel = getLabel("", color, Assets.uiSkin);

		luckyNumsLabel = getLabel("", color, Assets.gradeUpSkin);
		winNumsLabel = getLabel("", color, Assets.gradeUpSkin);

		pixmap = new Pixmap(1024, 1024, Format.RGBA8888);
		pixmap.setColor(0f, 0f, 0f, 1f);
		// pixmap.drawRectangle(0, 0, 100, 100);
		pixmap.fill();
		maskTexture = new Texture(pixmap);
		maskSprite = new Sprite(maskTexture);
		this.visible = false;
	}

	public void setGradeUp() {
		Integer curGrade = Cookie.getCurLevelConfig().ordinal() / 3 + 1;
		Integer winGoldDelta = CommonTools.getCoin(
				ConfigSet.gradeMap.get(curGrade).getLuckNums(),
				ConfigSet.levelMap.get(curGrade * 3 - 2).getDemoneacapenums(),
				ConfigSet.levelMap.get(curGrade * 3 - 1).getDemoneacapenums(),
				ConfigSet.levelMap.get(curGrade * 3).getDemoneacapenums());
		if (winGoldDelta >= 0) {
			this.congratulationLabel.setText("本级中奖");
		} else {
			this.congratulationLabel.setText("本级未中奖");
		}
		luckyNumsLabel.setText("幸运数字:"
				+ ConfigSet.levelMap.get(curGrade * 3 - 2).getDemoneacapenums()
				+ "/"
				+ ConfigSet.levelMap.get(curGrade * 3 - 1).getDemoneacapenums()
				+ "/"
				+ ConfigSet.levelMap.get(curGrade * 3).getDemoneacapenums());
		winNumsLabel.setText("开运号码:"
				+ ConfigSet.gradeMap.get(curGrade).getLuckNums());

		Integer gradeUpGoldCost = ConfigSet.levelMap.get(curGrade * 3 - 2)
				.getDemoneacapenums().length()
				* ConfigSet.levelMap.get(curGrade * 3 - 1).getDemoneacapenums()
						.length()
				* ConfigSet.levelMap.get(curGrade * 3).getDemoneacapenums()
						.length() * 2;

		if (Cookie.magic >= Setting.GRADE_UP_MAGIC_COST
				&& Cookie.gold + winGoldDelta - gradeUpGoldCost >= 0) {
			nextAble = true;
			Assets.playSound(Assets.Sounds.sound_levelup_success);
		} else {
			nextAble = false;
			Assets.playSound(Assets.Sounds.sound_demon_laugh);
		}
		levelLabel.setText(nextAble ? "Success" : "Fail");
		if (btnsMap != null)
			btnsMap.clear();
		btnsMap = createSideMenuBtns();
		lastTime = 0;
		curTime = 0;
		gredeUpGoldTo = Cookie.gold + winGoldDelta - gradeUpGoldCost;
		gradeUpMagicTo = Cookie.magic - Setting.GRADE_UP_MAGIC_COST;
		setBtnPosition();
	}

	// private Boolean HasSameItem(List<Integer> list) {
	// for (int i = 0; i < list.size(); i++) {
	// for (int j = i + 1; j < list.size(); j++) {
	// if (list.get(i) == list.get(j))
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// private Boolean ArrContainAll(List<Integer> fater, List<Integer> child) {
	// return fater.containsAll(child);
	// // for (int i = 0; i < child.size(); i++) {
	// // Integer num = child.get(i);
	// // for (int j = 0; j < fater.size(); j++) {
	// // if (num == fater.get(j)) {
	// // break;
	// // } else if (j == fater.size() - 1) {
	// // return false;
	// // }
	// // }
	// // }
	// // return true;
	// }

	private Label getLabel(String value, Color tint, Skin skin) {
		Label label = new Label(value, skin);
		label.setWrap(false);
		label.setAlignment(Align.BOTTOM | Align.LEFT);
		label.setColor(tint);
		return label;
	}

	private HashMap<String, TwoStateButton> createSideMenuBtns() {
		HashMap<String, TwoStateButton> map = new HashMap<String, TwoStateButton>();
		for (BtnLevelUp btn : BtnLevelUp.values()) {
			if (btn == BtnLevelUp.levelUpNextLevel && !nextAble) {
				continue;
			}
			TwoStateButton tb;
			tb = new TwoStateButton(Assets.levelUpButtons.get(btn.name()),
					ButtonBoundShape.CIRCLE);
			map.put(btn.name(), tb);
		}
		return map;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		// sprite.draw(batch, 1f);
		if (!this.visible)
			return;
		if (this.x != -this.width) {
			curTime += Gdx.graphics.getDeltaTime();
			// Color oldColor = Assets.huaWenXiHeiFont.getColor();
			maskSprite.draw(batch, 0.5f);
			background.setPosition(x, y);
			background.draw(batch, color.a * parentAlpha);
			drawButtons(batch);
			levelLabel.draw(batch, color.a * parentAlpha);
			
			if (Cookie.gold < this.gredeUpGoldTo) {
				goldLabel.setText("金币:" + Cookie.gold++);
				this.settingDAO.scoreCountSound();
			}
			if (Cookie.gold > this.gredeUpGoldTo) {
				goldLabel.setText("金币:" + Cookie.gold--);
				this.settingDAO.scoreCountSound();
			}
			if (Cookie.magic < this.gradeUpMagicTo) {
				magicLabel.setText("魔力:" + Cookie.magic++);
				this.settingDAO.scoreCountSound();
			}
			if (Cookie.magic > this.gradeUpMagicTo) {
				magicLabel.setText("魔力:" + Cookie.magic--);
				this.settingDAO.scoreCountSound();
			}
			lastTime = curTime;

			goldLabel.draw(batch, color.a * parentAlpha);
			magicLabel.draw(batch, color.a * parentAlpha);
			winNumsLabel.draw(batch, color.a * parentAlpha);
			luckyNumsLabel.draw(batch, color.a * parentAlpha);
			congratulationLabel.draw(batch, color.a * parentAlpha);
			updateButtons();
		}
	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

	private void setBtnPosition() {
		Iterator<String> keySetIterator = btnsMap.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			switch (BtnLevelUp.getBtnLevelUp(btnName)) {
			case levelUpLevelSelect:
				btnsMap.get(btnName).x = this.x + centerX - 100;
				btnsMap.get(btnName).y = this.y + centerY;
				break;
			case levelUpNextLevel:
				btnsMap.get(btnName).x = this.x + centerX + 100;
				btnsMap.get(btnName).y = this.y + centerY;
				break;
			case levelUpReplay:
				btnsMap.get(btnName).x = nextAble ? this.x + centerX : this.x
						+ centerX + 100;
				btnsMap.get(btnName).y = nextAble ? this.y + centerY : this.y
						+ centerY;
				break;
			}
		}
	}

	private void updateButtons() {

		levelLabel.x = this.x + this.centerX;
		levelLabel.y = this.y + centerY + 180;

		goldLabel.x = this.x + this.centerX;
		goldLabel.y = this.y + centerY + 140;

		magicLabel.x = this.x + this.centerX;
		magicLabel.y = this.y + centerY + 100;

		winNumsLabel.x = this.x + this.centerX - winNumsLabel.width / 2;
		winNumsLabel.y = this.y + centerY - 20;

		luckyNumsLabel.x = this.x + this.centerX - magicLabel.width / 2;
		luckyNumsLabel.y = this.y + centerY - 50;

		congratulationLabel.x = this.x + this.centerX;
		congratulationLabel.y = this.y + centerY - 90;

		touchPoint = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight()
				- Gdx.input.getY());
		boolean justTouched = Gdx.input.justTouched();
		boolean isTouched = Gdx.input.isTouched();
		boolean justReleased = wasTouched && !isTouched;
		wasTouched = isTouched;
		Iterator<String> keySetIterator = btnsMap.keySet().iterator();
		setBtnPosition();

		if (Cookie.gold < this.gredeUpGoldTo
				|| Cookie.magic < this.gradeUpMagicTo)
			return;

		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			btnsMap.get(btnName).update(justTouched, isTouched, justReleased,
					touchPoint.x, touchPoint.y);
		}
		keySetIterator = btnsMap.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			if (btnsMap.get(btnName).wasPressed()) {
				Gdx.app.log(btnName, "wasPressed");
				this.hide();
				switch (BtnLevelUp.getBtnLevelUp(btnName)) {
				case levelUpLevelSelect:
					notifier.onLevelSelect();
					break;
				case levelUpNextLevel:
					notifier.onGameStart();
					break;
				case levelUpReplay:
					notifier.onGameReset();
					break;
				}
			}
		}
	}

	private void drawButtons(SpriteBatch batch) {
		Iterator<String> keySetIterator = btnsMap.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			btnsMap.get(btnName).draw(batch);
		}
	}

	public void hide() {
		this.actions.clear();
		if (true || this.x == 0 && this.isShow) {
			this.isShow = false;
			hideAction = FadeOut.$(0.2f);
			hideAction.setCompletionListener(new OnActionCompleted() {
				@Override
				public void completed(Action action) {
					// try {
					// this.finalize();
					// } catch (Throwable e) {
					// }
					GradeUp.this.visible = false;
				}
			});
			this.action(hideAction);
		}
	}

	public void show(String curLevel) {
		this.actions.clear();
		if (true || this.x == -textureBackground.getRegionWidth()
				&& !this.isShow) {
			this.isShow = true;
			this.curLevel = curLevel;
			// this.actions.remove();
			showAction = MoveTo.$(0, 0, 0.2f);
			this.action(showAction);
		}
	}
}
