package com.android.ringfly.actor;

import java.util.ArrayList;
import java.util.List;
import com.android.ringfly.common.Assets;
import com.android.ringfly.common.BodyFactory;
import com.android.ringfly.common.Config;
import com.android.ringfly.common.MathTools;
import com.android.ringfly.common.Nature;
import com.android.ringfly.common.PointInTester;
import com.android.ringfly.common.UserData;
import com.android.ringfly.dao.sqlite.SettingDAO;
import com.android.ringfly.ringfly.Cookie;
import com.android.ringfly.ringfly.GameWorld;
import com.android.ringfly.ringfly.GameWorldNotifier;
import com.android.ringfly.ringfly.Setting;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class HandActor extends Actor {
	private TextureRegion textureRegion;
	private Body bodyInHand, bodyShot;
	private TextureRegion ringRegion;
	private Vector2 startPoint, realsePoint, bdfPoint, lastPathPoint,
			curPathPont;
	public List<Vector2> pathPonts;
	private Body fakeRingBody, testBody;
	private Pixmap pixmap;
	private Texture point;
	private BodyFactory worldFactory;
	public float stateTime;
	private boolean blnThrow;
	private final GameWorldNotifier notifier;
	private Animation handAnim;
	private boolean wasPressed;
	private boolean activated;
	private boolean down;
	private List<Body> feileiList, feidanList;
	private BodyFactory bodyFactory;
	private float timeClock;
	private float lastX, lastY;
	public float ringX, ringY;
	private UserData bodyInHandUserData;
	private boolean blnStep;
	public World fakeWorld;
	protected Box2DDebugRenderer renderer;
	private boolean blnTouchDraged;

	SettingDAO settingDAO;

	private float lastDisX, lastDisY;

	public void setHandAnim(Animation handAnim) {
		this.handAnim = handAnim;
	}

	public HandActor(GameWorldNotifier notifier, String name,
			TextureRegion textureRegion, World world, SettingDAO dao) {
		super(name);

		fakeWorld = new World(new Vector2(0, Assets.GRAVITY), true); // 一般标准重力场g约等于9.8m/s^2
		// "-"表示方向向下
		fakeWorld.setAutoClearForces(true);
		this.settingDAO = dao;
		this.wasPressed = false;
		blnStep = false;
		blnTouchDraged = false;
		this.notifier = notifier;
		this.textureRegion = textureRegion;
		this.width = textureRegion.getRegionWidth();
		this.height = textureRegion.getRegionHeight();
		this.touchable = true;
		this.feidanList = new ArrayList<Body>();
		this.feileiList = new ArrayList<Body>();
		timeClock = 0;
		lastX = 0;
		lastY = 0;
		InitStat();
		bodyFactory = BodyFactory.GetInstance(world);
		bodyInHand = null;
		bodyShot = null;
		realsePoint = new Vector2();
		lastPathPoint = new Vector2();
		curPathPont = new Vector2();
		startPoint = new Vector2(Setting.startPoint.x / Assets.pixelDensity,
				Setting.startPoint.y / Assets.pixelDensity);

		pixmap = new Pixmap(8, 8, Format.RGBA8888);
		pixmap.setColor(0.5f, 0.2f, 0f, 1f);
		pixmap.drawCircle(4, 4, 2);
		point = new Texture(pixmap);
		this.worldFactory = BodyFactory.GetInstance(GameWorld.world);
		pathPonts = new ArrayList<Vector2>();
		fakeRingBody = getFakeRing(BodyType.StaticBody, Setting.RING_RADIUS,
				(this.ringX), (this.ringY));

		// testBody = worldFactory.getRing(BodyType.StaticBody, null,
		// Setting.RING_RADIUS, (this.x - Setting.handDelta.x),
		// (this.y - Setting.handDelta.y));

		renderer = new Box2DDebugRenderer();
	}

	private Body getFakeRing(BodyType type, float ringRadius, float posX,
			float posY) {
		CircleShape sphereShape = new CircleShape();
		sphereShape.setRadius(ringRadius);
		FixtureDef sphereFixture = new FixtureDef();
		sphereFixture.filter.groupIndex = -2;
		sphereFixture.density = 1;
		sphereFixture.friction = 3;
		sphereFixture.restitution = 0.1f;
		sphereFixture.shape = sphereShape;
		BodyDef sphereBodyDef = new BodyDef();
		sphereBodyDef.type = type;
		sphereBodyDef.position.set(posX / Assets.pixelDensity, posY
				/ Assets.pixelDensity);
		Body ringSphere = fakeWorld.createBody(sphereBodyDef);
		ringSphere.setSleepingAllowed(true);
		ringSphere.createFixture(sphereFixture);
		return ringSphere;
	}

	public void update(float delta, boolean justTouched, boolean isTouched,
			boolean justReleased, float x, float y) {
		wasPressed = false;
		Rectangle r = new Rectangle(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		if (justTouched && PointInTester.pointInRectangle(r, x, y)) {
			activated = true;
			down = true;
		} else if (isTouched) {
			down = activated && PointInTester.pointInRectangle(r, x, y);
		} else if (justReleased) {
			wasPressed = activated && PointInTester.pointInRectangle(r, x, y);
			activated = false;
			down = false;
		} else {
			activated = false;
		}
	}

	public void InitStat() {
		this.x = Setting.startPoint.x + Setting.handDelta.x;
		this.y = Setting.startPoint.y + Setting.handDelta.y;
		this.blnThrow = false;
		this.stateTime = 0;
	}

	public List<Body> getFeileiList() {
		return feileiList;
	}

	public void setFeileiList(List<Body> feileiList) {
		this.feileiList = feileiList;
	}

	public List<Body> getFeidanList() {
		return feidanList;
	}

	public void setFeidanList(List<Body> feidanList) {
		this.feidanList = feidanList;
	}

	public Body getBodyInHand() {
		return bodyInHand;
	}

	public void setBodyInHand(Body bodyInHand) {
		this.bodyInHand = bodyInHand;
		startPoint = new Vector2(this.bodyInHand.getPosition());
	}

	public UserData getBodyInHandUserData() {
		return bodyInHandUserData;
	}

	public void setBodyInHandUserData(UserData userData) {
		this.bodyInHandUserData = userData;
		this.ringX = 0;
		this.ringY = 0;
		blnStep = true;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		float deltaTime = Gdx.graphics.getDeltaTime();
		stateTime += deltaTime;
		timeClock += deltaTime;
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		// batch.draw(this.textureRegion, this.x, this.y);
		if (blnThrow) {
			batch.draw(Assets.hand, this.x, this.y);
		} else {
			batch.draw(this.handAnim.getKeyFrame(stateTime, false), this.x,
					this.y);
		}
		if (null != this.pathPonts && this.pathPonts.size() > 0) {
			for (int i = 0; i < this.pathPonts.size(); i++) {
				batch.draw(point,
						this.pathPonts.get(i).x * Assets.pixelDensity,
						this.pathPonts.get(i).y * Assets.pixelDensity);
			}
		}

		if (Cookie.isFeidanAble() && this.down) {
			Cookie.setFeidanAble(false);
			for (int i = 0; i < 3; i++) {
				UserData data = new UserData("feiDanRing",
						Assets.rings.get("feiDanRing"), Nature.FEIDAN);
				data.blnDraw = true;
				Body body = bodyFactory.getRing(BodyType.DynamicBody, data, 1,
						this.bodyShot.getPosition().x * Assets.pixelDensity,
						80 + 150 + 90 * i);
				body.setLinearVelocity(25, 1);
				this.feidanList.add(body);
			}
			Gdx.app.log("飞弹", "解体" + this.bodyShot.getPosition().x + "|"
					+ this.bodyShot.getPosition().y);
			((UserData) this.bodyShot.getUserData()).remove = true;

		}
		if (Cookie.isFeileiAble() && this.down) {
			Cookie.setFeileiAble(false);
			for (int i = 0; i < 3; i++) {
				UserData data = new UserData("feiLeiRing",
						Assets.rings.get("feiLeiRing"), Nature.FEILEI);
				data.blnDraw = true;
				Body body = bodyFactory.getRing(BodyType.DynamicBody, data, 1,
						this.bodyShot.getPosition().x * Assets.pixelDensity,
						80 + 150 + 90 * i);
				body.setLinearVelocity(25, 1);
				this.feidanList.add(body);
			}
			Gdx.app.log("飞雷", "解体" + this.bodyShot.getPosition().x + "|"
					+ this.bodyShot.getPosition().y);
			((UserData) this.bodyShot.getUserData()).remove = true;
		}
	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		if (x > 0 && y > 0 && x < this.width && y < this.height)
			return this;
		else
			return null;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		// TODO Auto-generated method stub
		// Gdx.app.log("startPont" + (this.x + x), (this.y + y) + "");
		if (x > 0 && y > 0 && x < this.width && y < this.height)
			return true;
		else
			return false;
	}

	@Override
	public void touchUp(float x, float y, int pointer) {
		// TODO Auto-generated method stub
		blnStep = false;
		// if (this.fakeRingBody != null) {
		// this.fakeRingBody.setTransform(-Assets.pixelDensity,
		// -Assets.pixelDensity, 0);
		// }
		if (this.bodyInHandUserData != null) {
			this.bodyInHand = bodyFactory.getRing(BodyType.DynamicBody,
					this.bodyInHandUserData, Setting.RING_RADIUS,
					(this.x - Setting.handDelta.x),
					(this.y - Setting.handDelta.y));
		}
		if (null != bodyInHand) {
			this.bodyInHand.setType(BodyType.DynamicBody);
			realsePoint.set(this.bodyInHand.getPosition());
			float distanceX = (startPoint.x - realsePoint.x)
					* Assets.pixelDensity;
			float distanceY = (startPoint.y - realsePoint.y)
					* Assets.pixelDensity;
			float distance = (float) Math.sqrt(distanceX * distanceX
					+ distanceY * distanceY);
			float angle = (float) Math.atan2(distanceY, distanceX);
			float vx = (float) (distance * Math.cos(angle) / Assets.RING_SPEED_DIVISOR);
			float vy = (float) (distance * Math.sin(angle) / Assets.RING_SPEED_DIVISOR);
			this.bodyInHand.setLinearVelocity(vx, vy);
			Cookie.pathPontsKeep.clear();
			((UserData) bodyInHand.getUserData()).blnDraw = true;

			Nature nature = ((UserData) (this.bodyInHand.getUserData())).nature;
			notifier.onShot(nature);
			bodyShot = bodyInHand;
			bodyInHand = null;
			bodyInHandUserData = null;
			blnThrow = true;
		}
		this.pathPonts.clear();

	}

	@Override
	public void touchDragged(float x, float y, int pointer) {
		this.x = this.x + x - this.width / 2;
		this.y = this.y + y - this.height / 2;

		float distanceX = this.x - Setting.handDelta.x - Setting.startPoint.x;
		float distanceY = this.y - Setting.handDelta.y - Setting.startPoint.y;

		if (this.visible == true
				&& distanceX * distanceX + distanceY * distanceY > 30
				&& !blnTouchDraged) {
			blnTouchDraged = true;
		}
		if (distanceX * distanceX + distanceY * distanceY > Setting.handRangeDis
				* Setting.handRangeDis) {
			double birdAngle = Math.atan2(distanceY, distanceX);
			this.x = (float) (Setting.startPoint.x + Setting.handRangeDis
					* Math.cos(birdAngle) + Setting.handDelta.x);
			this.y = (float) (Setting.startPoint.y + Setting.handRangeDis
					* Math.sin(birdAngle) + Setting.handDelta.y);

		}

		if (null != this.bodyInHandUserData) {
			// this.bodyInHand.setTransform((this.x - Setting.handDelta.x) / 20,
			// (this.y - Setting.handDelta.y) / 20, 0);
			this.ringX = (this.x - Setting.handDelta.x);
			this.ringY = (this.y - Setting.handDelta.y);
		}

		if (null != this.bodyInHandUserData) {
			if (timeClock > Assets.PATHLINE_RENDERTIME) {
				if (Math.abs(this.x - this.lastX) > Assets.PATHLINE_SHAKE
						|| Math.abs(this.y - this.lastY) > Assets.PATHLINE_SHAKE) {
					this.lastX = this.x;
					this.lastY = this.y;
					drawPathLine();
					timeClock = 0;
				}
			}
		}
		if ((Math.abs(this.x - this.lastDisX) > 15 || Math.abs(this.y
				- this.lastDisY) > 15)
				&& blnTouchDraged && this.visible == true) {
			this.settingDAO.stretchSound();
			lastDisX = this.x;
			lastDisY = this.y;
		}
	}

	private void drawPathLine() {
		simulateRun();
		pathPonts.clear();
		lastPathPoint.set(this.fakeRingBody.getPosition().x,
				this.fakeRingBody.getPosition().y);
		while (pathPonts.size() < Assets.FAKE_POIN_TNUM && blnStep) {
			fakeWorld.step(Gdx.graphics.getDeltaTime(), 6, 3);
			curPathPont.set(this.fakeRingBody.getPosition().x,
					this.fakeRingBody.getPosition().y);
			if (MathTools.distanceCalc(lastPathPoint, curPathPont) > 1) {
				lastPathPoint.set(curPathPont);
				pathPonts.add(new Vector2(lastPathPoint));
			}
		}
		// fakeRingBody.setType(BodyType.StaticBody);
	}

	public void simulateRun() {
		fakeRingBody.setTransform((this.x - Setting.handDelta.x)
				/ Assets.pixelDensity, (this.y - Setting.handDelta.y)
				/ Assets.pixelDensity, 0);
		if (fakeRingBody.getType() != BodyType.DynamicBody)
			fakeRingBody.setType(BodyType.DynamicBody);
		realsePoint.set(fakeRingBody.getPosition());
		float distanceX = (startPoint.x - realsePoint.x) * Assets.pixelDensity;
		float distanceY = (startPoint.y - realsePoint.y) * Assets.pixelDensity;
		float distance = (float) Math.sqrt(distanceX * distanceX + distanceY
				* distanceY);
		float angle = (float) Math.atan2(distanceY, distanceX);
		float vx = (float) (distance * Math.cos(angle) / Assets.RING_SPEED_DIVISOR);
		float vy = (float) (distance * Math.sin(angle) / Assets.RING_SPEED_DIVISOR);
		fakeRingBody.setLinearVelocity(vx, vy);
	}
}
