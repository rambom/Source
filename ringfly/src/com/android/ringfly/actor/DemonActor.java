package com.android.ringfly.actor;

import java.util.List;

import com.android.ringfly.actor.AppleActor.AppleState;
import com.android.ringfly.common.Assets;
import com.android.ringfly.common.BodyFactory;
import com.android.ringfly.common.Nature;
import com.android.ringfly.common.Assets.Sounds;
import com.android.ringfly.configset.ConfigSet;
import com.android.ringfly.ringfly.Cookie;
import com.android.ringfly.ringfly.GameWorldListener;
import com.android.ringfly.ringfly.GameWorldNotifier;
import com.android.ringfly.ringfly.Setting;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.actions.Repeat;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;

public class DemonActor extends Actor implements DemonBehaviour,
		GameWorldListener {
	private Integer rank;
	private Animation hideRunAnim, showRunAnim, dieAnim, failHitAnim;
	private float stateTime;
	public Sprite sprite;
	public Body demonSphere;
	public DemonState state, lastState;
	private boolean blnDieOnce, blnfailHit, blnRunBack, blnTaiJiRun;
	private Body body;
	public boolean blnRemove;
	public boolean blnChange;
	public boolean blnBlinkOnce;
	public AppleActor appleInMouth;

	public Nature nature;
	public String luckyNum;
	public Integer magic;
	public Integer goldCoin;
	private BodyFactory bodyFactory;

	private final GameWorldNotifier notifier;
	private Vector2 hideBounds, showBounds, dieBounds, failHitBounds;
	private Action moveAction, blinkAction, totalAction;
	public boolean flip;
	public boolean isTaiJi;
	public boolean hungry;
	float sizePercent = 2f / 3f;

	public enum DemonState {
		stand, pause, hiderun, showrun, dienormal, diebyall, diebyfeilei, diebyfeidan, failHit, taijirun;
		public static DemonState getDemonState(String DemonState) {
			return valueOf(DemonState);
		}
	}

	public void initAnim(Nature nature) {
		switch (nature) {
		case METAL:
			this.showRunAnim = isTaiJi ? new Animation(
					Assets.DEMON_RUN_FRAME_DURATION, Assets.taiJiRunRegions)
					: new Animation(Assets.DEMON_RUN_FRAME_DURATION,
							Assets.metalRunRegions);
			this.dieAnim = new Animation(Assets.DEMON_DIE_FRAME_DURATION,
					Assets.fireFightMetalRegions);
			this.failHitAnim = new Animation(Assets.DEMON_HIT_FRAME_DURATION,
					Assets.failMetalRegions);
			break;
		case WOOD:
			this.showRunAnim = isTaiJi ? new Animation(
					Assets.DEMON_RUN_FRAME_DURATION, Assets.taiJiRunRegions)
					: new Animation(Assets.DEMON_RUN_FRAME_DURATION,
							Assets.woodRunRegions);// temp
			this.dieAnim = new Animation(Assets.DEMON_DIE_FRAME_DURATION,
					Assets.metalFightWoodRegions);
			this.failHitAnim = new Animation(Assets.DEMON_HIT_FRAME_DURATION,
					Assets.failWoodRegions);
			break;
		case WATER:
			this.showRunAnim = isTaiJi ? new Animation(
					Assets.DEMON_RUN_FRAME_DURATION, Assets.taiJiRunRegions)
					: new Animation(Assets.DEMON_RUN_FRAME_DURATION,
							Assets.waterRunRegions);// temp
			this.dieAnim = new Animation(Assets.DEMON_DIE_FRAME_DURATION,
					Assets.earthFightWaterRegions);
			this.failHitAnim = new Animation(Assets.DEMON_HIT_FRAME_DURATION,
					Assets.failWaterRegions);
			break;
		case FIRE:
			this.showRunAnim = isTaiJi ? new Animation(
					Assets.DEMON_RUN_FRAME_DURATION, Assets.taiJiRunRegions)
					: new Animation(Assets.DEMON_RUN_FRAME_DURATION,
							Assets.fireRunRegions);// temp
			this.dieAnim = new Animation(Assets.DEMON_DIE_FRAME_DURATION,
					Assets.waterFightFireRegions);
			this.failHitAnim = new Animation(Assets.DEMON_HIT_FRAME_DURATION,
					Assets.failFireRegions);
			break;
		case EARTH:
			this.showRunAnim = isTaiJi ? new Animation(
					Assets.DEMON_RUN_FRAME_DURATION, Assets.taiJiRunRegions)
					: new Animation(Assets.DEMON_RUN_FRAME_DURATION,
							Assets.earthRunRegions);// temp
			this.dieAnim = new Animation(Assets.DEMON_DIE_FRAME_DURATION,
					Assets.woodFightEarthRegions);
			this.failHitAnim = new Animation(Assets.DEMON_HIT_FRAME_DURATION,
					Assets.failEarthRegions);
			break;
		}

		if (null != hideRunAnim) {
			this.width = hideRunAnim.getKeyFrame(1, false).getRegionWidth();
			this.height = hideRunAnim.getKeyFrame(1, false).getRegionHeight();
			this.hideBounds = new Vector2(getBoundsOfAnim(hideRunAnim).x,
					getBoundsOfAnim(hideRunAnim).y);
		}
		if (null != showRunAnim) {
			this.showBounds = new Vector2(getBoundsOfAnim(showRunAnim).x,
					getBoundsOfAnim(showRunAnim).y);
		}
		if (null != dieAnim) {
			this.dieBounds = new Vector2(getBoundsOfAnim(dieAnim).x
					* sizePercent, getBoundsOfAnim(dieAnim).y * sizePercent);
		}
		if (null != failHitAnim) {
			this.failHitBounds = new Vector2(getBoundsOfAnim(failHitAnim).x
					* sizePercent, getBoundsOfAnim(failHitAnim).y * sizePercent);
		}
	}

	public DemonActor(GameWorldNotifier notifier, String luckyNum,
			Integer gold, Nature nature, Boolean isTaiJi, Integer rank) {
		super("demon" + luckyNum);
		hungry = true;
		bodyFactory = BodyFactory.GetInstance();
		this.luckyNum = luckyNum;
		this.magic = Integer.parseInt(luckyNum) * 10;
		this.goldCoin = gold;
		this.notifier = notifier;
		this.body = bodyFactory.getDemon(nature);
		this.appleInMouth = null;
		this.nature = nature;
		this.isTaiJi = isTaiJi;
		this.rank = rank;
		if (nature == Nature.POPO) {
			this.hideRunAnim = new Animation(Assets.DEMON_RUN_FRAME_DURATION,
					Assets.popoRegions);
		} else {
			this.hideRunAnim = new Animation(Assets.DEMON_RUN_FRAME_DURATION,
					Assets.hideRunRegions);
		}
		initAnim(nature);
		this.stateTime = 0;
		this.blnRemove = false;
		this.blnChange = false;
		this.blnRunBack = false;
		blnTaiJiRun = false;
		sprite = new Sprite();
		state = DemonState.stand;
		blnDieOnce = false;
		blnfailHit = false;
		blnBlinkOnce = false;
		flip = false;
		body.setUserData(this);
	}

	private Vector2 getBoundsOfAnim(Animation ani) {
		return new Vector2(ani.getKeyFrame(1, false).getRegionWidth(), ani
				.getKeyFrame(1, false).getRegionHeight());
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		if (null != this.body) {
			this.body.setTransform(this.x / 20, this.y / 20, 0);
		}
		if (null != this.appleInMouth) {
			if (this.appleInMouth.state == AppleState.die) {
				// keep going become taiJiyao
				// this.action(action)
				this.run(this.lastState == DemonState.hiderun ? false : true);
				this.blnBlinkOnce = false;
				this.appleInMouth = null;
			}
		}
		switch (this.state) {
		case stand:
		case pause:
			color.a = 1;
			drawPause(batch, 1);
			break;
		case hiderun:
			drawHide(batch, parentAlpha);
			break;
		case showrun:
			drawShow(batch, parentAlpha);
			break;
		case dienormal:
		case diebyall:
		case diebyfeidan:
		case diebyfeilei:
			if (null != this.dieAnim) {
				this.stateTime += Gdx.graphics.getDeltaTime();
				if (!blnDieOnce) {
					this.stateTime = 0;
					this.blnDieOnce = true;
				}
				sprite.setBounds(this.x, this.y, dieBounds.x, dieBounds.y);
				sprite.setRegion(this.dieAnim.getKeyFrame(stateTime, false));
				if (this.dieAnim.isAnimationFinished(stateTime)) {
					this.blnRemove = true;
					Integer curLevel = Cookie.getCurLevelConfig().ordinal() + 1;
					ConfigSet.levelMap.get(curLevel).setDemondienum(
							ConfigSet.levelMap.get(curLevel).getDemondienum()
									+ this.luckyNum);
					// Cookie.demonsDieNum.add(this.luckyNum);
				}
				sprite.flip(flip, false);
				sprite.draw(batch, color.a * parentAlpha);
			}
			break;
		case failHit:
			if (null != this.failHitAnim) {
				this.stateTime += Gdx.graphics.getDeltaTime();
				if (!blnfailHit) {
					this.stateTime = 0;
					this.blnfailHit = true;
				}
				sprite.setBounds(this.x, this.y, failHitBounds.x,
						failHitBounds.y);
				sprite.setRegion(this.failHitAnim.getKeyFrame(stateTime, false));
				if (this.failHitAnim.isAnimationFinished(stateTime)) {
					this.state = DemonState.hiderun;
					this.blnfailHit = false;
				}
				sprite.flip(flip, false);
				sprite.draw(batch, color.a * parentAlpha);
			}
			this.state = DemonState.showrun;
			break;
		}
	}

	private void drawPause(SpriteBatch batch, float parentAlpha) {
		if (this.lastState == DemonState.hiderun && null != this.hideRunAnim) {
			sprite.setBounds(this.x, this.y, hideBounds.x, hideBounds.y);
			sprite.setRegion(this.hideRunAnim.getKeyFrame(0, false));
			sprite.flip(flip, false);
			sprite.draw(batch, color.a * parentAlpha);
		} else if (this.lastState == DemonState.showrun
				&& null != this.showRunAnim) {
			sprite.setBounds(this.x, this.y, showBounds.x, showBounds.y);
			sprite.setRegion(this.showRunAnim.getKeyFrame(0, false));
			sprite.flip(flip, false);
			sprite.draw(batch, color.a * parentAlpha);
		}
	}

	private void drawHide(SpriteBatch batch, float parentAlpha) {
		if (null != this.hideRunAnim) {
			this.stateTime += Gdx.graphics.getDeltaTime();
			sprite.setBounds(this.x, this.y, hideBounds.x, hideBounds.y);
			sprite.setRegion(this.hideRunAnim.getKeyFrame(stateTime, true));
			sprite.flip(flip, false);
			sprite.draw(batch, color.a * parentAlpha);
		}
	}

	private void drawShow(SpriteBatch batch, float parentAlpha) {
		if (null != this.showRunAnim) {
			this.stateTime += Gdx.graphics.getDeltaTime();
			sprite.setBounds(this.x, this.y, showBounds.x, showBounds.y);
			sprite.setRegion(this.showRunAnim.getKeyFrame(stateTime, true));
			sprite.flip(flip, false);
			sprite.draw(batch, color.a * parentAlpha);
		}
	}

	@Override
	public Actor hit(float x, float y) {
		if (x > 0 && y > 0 && x < this.width && y < this.height)
			return this;
		else
			return null;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		// TODO Auto-generated method stub
		if (this.nature == Nature.POPO)
			return true;
		if (Setting.showDemon && this.state == DemonState.hiderun) {
			Gdx.app.log("show", "demon");
			Setting.showDemon = false;
			this.state = DemonState.showrun;
			this.lastState = this.state;
			notifier.onEyeUsed();
		} else {
			Gdx.app.log("DemonActor", "no need");
		}
		return true;
	}

	@Override
	public void hitFail() {
		Gdx.app.log("DemonActor", "you are wrong");
		this.state = DemonState.failHit;
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		if (this.nature != Nature.POPO) {
			Gdx.app.log("I am ", "die");
			this.state = DemonState.dienormal;
			this.body = null;
			if (null != this.appleInMouth) {
				// stop apple blink
				this.appleInMouth.saved();
			}
			notifier.onDemonDie(luckyNum, magic, goldCoin);
		} else {
			Assets.playSound(Sounds.sound_popo_break);
			Gdx.app.log(this.getClass().getName(), "demon run out from popo");
			this.nature = Cookie.getCurLevelConfig().getNatures()[this.rank];
			initAnim(nature);
			this.blnChange = true;
			this.state = DemonState.showrun;
		}
	}

	public void disappear() {
		// TODO Auto-generated method stub
		Gdx.app.log("I am ", "disappear");
		this.visible = false;
		this.state = DemonState.dienormal;
		this.body = null;
		if (null != this.appleInMouth) {
			// stop apple blink
			this.appleInMouth.saved();
		}
		Integer curLevel = Cookie.getCurLevelConfig().ordinal() + 1;
		ConfigSet.levelMap.get(curLevel).setDemoneacapenums(
				ConfigSet.levelMap.get(curLevel).getDemoneacapenums()
						+ this.luckyNum);
		// Cookie.demonDisappearNum.add(this.luckyNum);
		// notifier.onDemonDie(luckyNum, magic, goldCoin);
	}

	@Override
	public void run(boolean ifShow) {

		this.state = ifShow ? DemonState.showrun : DemonState.hiderun;

		moveAction = Sequence.$(MoveTo.$(flip ? Gdx.graphics.getWidth() + 100
				: -100, this.y, this.x / Assets.DEMON_RUN_SPEED));
		this.action(moveAction);
	}

	public void runBack() {
		flip = true;
		this.actions.clear();
		moveAction = Sequence.$(MoveTo.$(900, this.y, (800 - this.x)
				/ Assets.DEMON_RUN_SPEED / 2));
		this.action(moveAction);
	}

	public void cloudDetect(List<CloudActor> actors) {
		for (CloudActor actor : actors) {
			if (this.x - (actor.x + actor.width) < -1 && this.y == actor.y
					&& !blnRunBack) {
				Gdx.app.log("DemonActor", "I will disappear");
				this.actions.clear();
				// this.runBack();
				this.disappear();
				blnRunBack = true;
			}
		}
	}

	@Override
	public void eat(List<AppleActor> actors) {
		if (this.nature == Nature.POPO)
			return;
		for (AppleActor actor : actors) {
			if ((this.x - actor.x < 1) && !blnBlinkOnce
					&& actor.state == AppleState.normal) {
				this.lastState = this.state;
				this.actions.clear();
				this.blink(3);
				this.appleInMouth = actor;
				actor.die(this.y);
				blnBlinkOnce = true;
			}
		}
	}

	public void eat(AppleActor actor) {
		this.state = this.lastState;
		this.actions.clear();
		this.blink(3);
		this.appleInMouth = actor;
		actor.die(this.y);
	}

	@Override
	public void blink(int times) {
		blinkAction = Sequence.$(FadeOut.$(0.2f), FadeIn.$(1f));
		totalAction = Sequence.$(Repeat.$(blinkAction, times));
		totalAction.setCompletionListener(new OnActionCompleted() {
			@Override
			public void completed(Action arg0) {
				DemonActor.this.isTaiJi = true;
				DemonActor.this.hungry = false;
				switch (DemonActor.this.nature) {
				case METAL:
					DemonActor.this.showRunAnim = new Animation(
							Assets.DEMON_RUN_FRAME_DURATION,
							Assets.metalTaiJiRunRegions);
					break;
				case WOOD:
					DemonActor.this.showRunAnim = new Animation(
							Assets.DEMON_RUN_FRAME_DURATION,
							Assets.woodTaiJiRunRegions);
					break;
				case WATER:
					DemonActor.this.showRunAnim = new Animation(
							Assets.DEMON_RUN_FRAME_DURATION,
							Assets.waterTaiJiRunRegions);
					break;
				case FIRE:
					DemonActor.this.showRunAnim = new Animation(
							Assets.DEMON_RUN_FRAME_DURATION,
							Assets.fireTaiJiRunRegions);
					break;
				case EARTH:
					DemonActor.this.showRunAnim = new Animation(
							Assets.DEMON_RUN_FRAME_DURATION,
							Assets.earthTaiJiRunRegions);
					break;
				}
				DemonActor.this.showBounds = new Vector2(
						getBoundsOfAnim(showRunAnim).x,
						getBoundsOfAnim(showRunAnim).y);
				DemonActor.this.state = DemonState.showrun;
				DemonActor.this.lastState = DemonState.showrun;

			}
		});
		this.action(totalAction);
	}

	@Override
	public void pause() {
		this.lastState = this.state;
		this.actions.clear();
		this.state = DemonState.pause;
	}

	@Override
	public void goon() {
		this.state = this.lastState;
		switch (this.state) {
		case dienormal:
			break;
		case hiderun:
			this.run(false);
			break;
		case pause:
			break;
		case showrun:
			this.run(true);
			break;
		case stand:
			break;
		}
	}

	@Override
	public void onGameStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGamePause() {
		Gdx.app.log("I am " + this.name,
				"I am notified that the game is paused");
		if (this.state != DemonState.stand)
			this.pause();

	}

	@Override
	public void onGameReset() {

	}

	@Override
	public void onGameRuning() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContinue() {
		// TODO Auto-generated method stub
		if (this.state == DemonState.pause)
			this.goon();
	}

	@Override
	public void onDemonDie(String luckyNum, Integer magic, Integer gold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onShot(Nature nature) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLevelSelect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMainMenuSelect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSoundChange(Assets.Sounds sound) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContactStone(List<Vector2> contactPoints) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGradeUp(Boolean blnPass, Boolean win) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEyeUsed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGoldChanged(int gold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMagicChanged(int magic) {
		// TODO Auto-generated method stub

	}

}
