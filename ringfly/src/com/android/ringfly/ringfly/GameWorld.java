package com.android.ringfly.ringfly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.android.ringfly.actor.AppleActor;
import com.android.ringfly.actor.AppleActor.AppleState;
import com.android.ringfly.actor.CloudActor;
import com.android.ringfly.actor.DemonActor;
import com.android.ringfly.actor.DemonActor.DemonState;
import com.android.ringfly.actor.ConfirmActor;
import com.android.ringfly.actor.GradeUp;
import com.android.ringfly.actor.HandActor;
import com.android.ringfly.actor.HelpActor;
import com.android.ringfly.actor.LevelUp;
import com.android.ringfly.actor.SideMenu;
import com.android.ringfly.actor.TaiJiStoneActor;
import com.android.ringfly.common.Assets;
import com.android.ringfly.common.Assets.BtnSelect;
import com.android.ringfly.common.BodyFactory;
import com.android.ringfly.common.Config;
import com.android.ringfly.common.MathTools;
import com.android.ringfly.common.MyContact;
import com.android.ringfly.common.Nature;
import com.android.ringfly.common.TwoStateButton;
import com.android.ringfly.common.TwoStateButton.ButtonBoundShape;
import com.android.ringfly.common.UserData;
import com.android.ringfly.configset.ConfigSet;
import com.android.ringfly.ringfly.Cookie.StatEnum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * 游戏世界
 * 
 * @author FGSHU
 * 
 */
public class GameWorld {
	RingflyGame game;
	private float now, stateTime;
	public static World world;
	private EdgeShape groundEdgeShape;
	private SpriteBatch spriteBatch;
	List<Body> ballList, rectList;
	BodyDef bd;
	private Stage stageBack, stageFore, stageConfirm;
	public static Image arrow;
	private boolean wasTouched;
	private Vector2 touchPoint, startPoint;
	public boolean nextShot;
	public int demonOccurTime;
	public float timeClock;
	public int demonI;
	private HandActor hand;
	private Image background;
	private HashMap<String, TwoStateButton> btnsMap;
	private Mesh mesh;
	private SideMenu sideMenu;
	private float timeStep;
	private List<DemonActor> demons;
	private List<AppleActor> apples;
	private List<TaiJiStoneActor> stones;
	private List<CloudActor> clouds;
	private static float demonWidth;
	private BodyFactory bodyFactory;
	protected OrthographicCamera camera;
	protected Box2DDebugRenderer renderer;
	private final GameWorldNotifier notifier;
	private final LevelConfig levelConfig;
	protected Label levelLabel, goldLabel, magicLabel, metalLabel, woodLabel,
			waterLabel, fireLabel, earthLabel, allLabel, eyeLabel, feileiLabel,
			feidanLabel;
	public boolean blnHelpOn;
	public GradeUp gradeUp;
	public LevelUp levelUp, levelFail;
	private Pixmap pixmap;
	private Texture point;

	private List<Integer> chosenList;

	public ConfirmActor confirmActor;

	public GameWorldNotifier getNotifier() {
		return notifier;
	}

	public GameWorld(RingflyGame game, PlayingScreen screen,
			LevelConfig levelConfig) {
		blnHelpOn = false;
		this.levelConfig = levelConfig;
		notifier = new GameWorldNotifier();
		this.game = game;
		InitStart();
		InitWorld(screen);
		InitStage();
		InitEnd();
		game.inputMultiplexer.addProcessor(stageFore);
		stageFore.setTouchFocus(hand, 0);
	}

	public void addGameWorldListener(GameWorldListener listener) {
		notifier.addListener(listener);
	}

	public void notifyApple() {
		if (null == apples || apples.size() == 0)
			return;
		for (AppleActor apple : apples) {
			if (apple.state != AppleState.die)
				notifier.addListener(apple);
		}
	}

	public void notifyDemon() {
		if (null == demons || demons.size() == 0)
			return;
		for (DemonActor demon : demons) {
			if (demon.state != DemonState.dienormal)
				notifier.addListener(demon);
		}
	}

	public void notifyStone() {
		if (null == stones || stones.size() == 0)
			return;
		for (TaiJiStoneActor stone : stones) {
			notifier.addListener(stone);
		}
	}

	private void InitStart() {
		camera = new OrthographicCamera(40, 24);
		camera.position.set(20, 12, 0);
		renderer = new Box2DDebugRenderer();
		wasTouched = false;
		nextShot = true;
		demonOccurTime = 0;
		timeClock = 0;
		spriteBatch = new SpriteBatch();
		touchPoint = new Vector2();
		startPoint = Setting.startPoint;
		btnsMap = createBtnRing(BtnSelect.btnMetal, BtnSelect.btnWood,
				BtnSelect.btnEarth, BtnSelect.btnWater, BtnSelect.btnFire,
				BtnSelect.btnAll, BtnSelect.btnEye, BtnSelect.btnFeiLei,
				BtnSelect.btnFeiDan, BtnSelect.btnPause, BtnSelect.btnReset);

		pixmap = new Pixmap(8, 8, Format.RGBA8888);
		pixmap.setColor(0.5f, 0.5f, 0f, 1f);
		pixmap.drawCircle(4, 4, 2);
		point = new Texture(pixmap);
	}

	private void InitEnd() {
		if (mesh == null) {
			mesh = new Mesh(true, 3, 3, new VertexAttribute(Usage.Position, 3,
					"a_position"), new VertexAttribute(Usage.ColorPacked, 4,
					"a_color"), new VertexAttribute(Usage.TextureCoordinates,
					2, "a_texCoords"));
			mesh.setIndices(new short[] { 0, 1, 2 });
		}

	}

	private Label getLabel(String value, float x, float y, Color tint) {
		Label label = new Label(value, Assets.uiSkin);
		label.setWrap(false);
		label.setAlignment(Align.BOTTOM | Align.LEFT);
		label.x = x;
		label.y = y;
		label.setColor(tint);
		stageFore.addActor(label);
		return label;
	}

	private void InitWorld(PlayingScreen screen) {
		world = new World(new Vector2(0, Assets.GRAVITY), true); // 一般标准重力场g约等于9.8m/s^2
		// "-"表示方向向下
		world.setContactListener(new MyContact<PlayingScreen>(screen, notifier));
		world.setAutoClearForces(true);

		bodyFactory = BodyFactory.GetInstance(world);

		bodyFactory.getEdge(new Vector2(0, 4.3f), new Vector2(3.5f, 4.3f));
		bodyFactory.getEdge(new Vector2(3.5f, 4.3f), new Vector2(3.5f, 4.2f));
		bodyFactory.getEdge(new Vector2(1.8f, 3.9f), new Vector2(3.6f, 0));

	}

	private Boolean InChosenList(Integer id) {
		if (chosenList == null || chosenList.size() == 0)
			return false;
		else
			for (Integer i : chosenList) {
				if (id.equals(i))
					return true;
			}
		return false;
	}

	private boolean inArray(Integer[] container, Integer num) {
		for (Integer i : container) {
			if (i == num)
				return true;
		}
		return false;
	}

	private void InitStage() {
		stageBack = new Stage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), false);
		stageFore = new Stage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), false);
		stageConfirm = new Stage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), false);

		background = new Image(Assets.backgroundRegion01);
		background.x = 0;
		background.y = 0;
		stageBack.addActor(background);

		confirmActor = new ConfirmActor("confirmActor", game.settingDao);
		confirmActor.visible = false;
		stageConfirm.addActor(confirmActor);

		chosenList = new ArrayList<Integer>();

		hand = new HandActor(notifier, "hand", Assets.hand, world,
				game.settingDao);

		hand.visible = false;
		stageFore.addActor(hand);

		demonWidth = 36.0f / Assets.PTM_RATIO;

		// get demons
		List<String> digitals = MathTools.getShuffle(0, 9);
		demons = new ArrayList<DemonActor>();

		for (int i = 0; i < this.levelConfig.getTaiJiNum(); i++) {
			chosenList.add(new Integer(digitals.get(i)));
		}
		Integer[] popoList = this.levelConfig.getIsPopo();
		for (int i = 0; i < 10; i++) {
			Nature nature;
			if (inArray(popoList, i)) {
				nature = Nature.POPO;
			} else {
				nature = this.levelConfig.getNatures()[i];
			}
			DemonActor demon = new DemonActor(notifier, digitals.get(i),
					this.levelConfig.getGoldCoin()[i], nature, InChosenList(i),
					i);
			if (InChosenList(i)) {
				Gdx.app.log("chose", i + "");
			}

			demon.x = 800;
			demon.y = this.levelConfig.getDemonsPosition()[i];
			stageFore.addActor(demon);
			demons.add(demon);
		}

		// demon.action(Sequence.$(MoveTo.$(0, 300, 10f)));

		// get apples
		apples = new ArrayList<AppleActor>();
		// for (int i = 0; i < 4; i++) {
		// AppleActor apple = new AppleActor(i + "", Assets.apple);
		// apple.x = 300 + 80 * i;
		// apple.y = 70;
		// apple.locationXY = new Vector2(apple.x, apple.y);
		// stageFore.addActor(apple);
		// apples.add(apple);
		// }

		for (int i = 0; i < this.levelConfig.getApplePos().length; i++) {
			AppleActor apple = new AppleActor(i + "", Assets.apple,
					this.notifier);
			apple.x = this.levelConfig.getApplePos()[i].x;
			apple.y = this.levelConfig.getApplePos()[i].y;
			apple.locationXY = new Vector2(apple.x, apple.y);
			stageFore.addActor(apple);
			apples.add(apple);
		}

		// get stones
		stones = new ArrayList<TaiJiStoneActor>();

		for (int i = 0; i < this.levelConfig.getStonePos().length; i++) {
			TaiJiStoneActor stone = new TaiJiStoneActor("stone",
					bodyFactory.getStone(), Assets.taiJiStone);
			stone.x = this.levelConfig.getStonePos()[i].x;
			stone.y = this.levelConfig.getStonePos()[i].y;
			stageFore.addActor(stone);
			stones.add(stone);
		}

		// get clouds
		clouds = new ArrayList<CloudActor>();
		for (int i = 0; i < this.levelConfig.getCloudsPos().length; i++) {
			CloudActor cloud = new CloudActor("clound", bodyFactory.getCloud(),
					Assets.cloud);
			cloud.x = this.levelConfig.getCloudsPos()[i].x;
			cloud.y = this.levelConfig.getCloudsPos()[i].y;
			stageFore.addActor(cloud);
			clouds.add(cloud);
		}

		levelLabel = getLabel("Level-"
				+ (Cookie.getCurLevelConfig().ordinal() + 1), 210, 440,
				Color.DARK_GRAY);

		goldLabel = getLabel("金币:" + Cookie.gold, 400, 440, Color.DARK_GRAY);
		magicLabel = getLabel("魔力:" + Cookie.magic, 600, 440, Color.DARK_GRAY);

		float lY = 40;
		float startX = 45;
		float deltaX = 80f;
		Color tint = Color.RED;

		metalLabel = getLabel(Cookie.getCurLevelConfig().getRingNums()[0] + "",
				startX, lY, tint);
		woodLabel = getLabel(Cookie.getCurLevelConfig().getRingNums()[1] + "",
				startX + deltaX, lY, tint);
		earthLabel = getLabel(Cookie.getCurLevelConfig().getRingNums()[4] + "",
				startX + 2 * deltaX, lY, tint);
		waterLabel = getLabel(Cookie.getCurLevelConfig().getRingNums()[2] + "",
				startX + 3 * deltaX, lY, tint);
		fireLabel = getLabel(Cookie.getCurLevelConfig().getRingNums()[3] + "",
				startX + 4 * deltaX, lY, tint);
		int indexNow = 4;
		if (this.levelConfig.getVisibility() == null
				|| this.levelConfig.getVisibility()[0]) {
			indexNow++;
			allLabel = getLabel(Cookie.getCurLevelConfig().getRingNums()[5]
					+ "", startX + indexNow * deltaX, lY, tint);
		}
		if (this.levelConfig.getVisibility() == null
				|| this.levelConfig.getVisibility()[1]) {
			indexNow++;
			eyeLabel = getLabel(Cookie.getCurLevelConfig().getRingNums()[6]
					+ "", startX + indexNow * deltaX + 20, lY, tint);
		}
		if (this.levelConfig.getVisibility() == null
				|| this.levelConfig.getVisibility()[2]) {
			indexNow++;
			feileiLabel = getLabel(Cookie.getCurLevelConfig().getRingNums()[7]
					+ "", startX + indexNow * deltaX + 20, lY, tint);
		}
		if (this.levelConfig.getVisibility() == null
				|| this.levelConfig.getVisibility()[3]) {
			indexNow++;
			feidanLabel = getLabel(Cookie.getCurLevelConfig().getRingNums()[8]
					+ "", startX + indexNow * deltaX + 20, lY, tint);
		}
		sideMenu = new SideMenu(notifier, "sideMenu");
		stageFore.addActor(sideMenu);

		if (this.levelConfig.ordinal() == 0) {
			blnHelpOn = true;
			HelpActor helpActor = new HelpActor("help", this);
			stageFore.addActor(helpActor);
		}
		this.levelUp = new LevelUp(getNotifier(), "levelUpSuccess", true);
		stageFore.addActor(levelUp);

		this.levelFail = new LevelUp(getNotifier(), "levelUpFail", false);
		stageFore.addActor(levelFail);

		this.gradeUp = new GradeUp(getNotifier(), "gardeUpSuccess",
				game.settingDao);
		stageFore.addActor(gradeUp);

	}

	private HashMap<String, TwoStateButton> createBtnRing(
			BtnSelect... btnRegions) {
		HashMap<String, TwoStateButton> map = new HashMap<String, TwoStateButton>();
		int i = 0;
		for (BtnSelect btnRegion : btnRegions) {
			TwoStateButton tb = null;
			switch (btnRegion) {
			case btnAll:
				if (this.levelConfig.getVisibility() != null
						&& !this.levelConfig.getVisibility()[0])
					continue;
				else {
					tb = getBtn(btnRegion.name());
					tb.x = 10 + 80 * i;
					tb.y = 4;
				}
				break;
			case btnEye:
				if (this.levelConfig.getVisibility() != null
						&& !this.levelConfig.getVisibility()[1])
					continue;
				else {
					tb = getBtn(btnRegion.name());
					tb.x = 10 + 80 * i;
					tb.y = 4;
				}
				break;
			case btnFeiLei:
				if (this.levelConfig.getVisibility() != null
						&& !this.levelConfig.getVisibility()[2])
					continue;
				else {
					tb = getBtn(btnRegion.name());
					tb.x = 10 + 80 * i;
					tb.y = 4;
				}
				break;
			case btnFeiDan:
				if (this.levelConfig.getVisibility() != null
						&& !this.levelConfig.getVisibility()[3])
					continue;
				else {
					tb = getBtn(btnRegion.name());
					tb.x = 10 + 80 * i;
					tb.y = 4;
				}
				break;
			case btnPause:
				tb = new TwoStateButton(Assets.stageButtons.get(btnRegion
						.name()), ButtonBoundShape.CIRCLE);
				tb.x = 20;
				tb.y = 420;
				break;
			case btnReset:
				tb = new TwoStateButton(Assets.stageButtons.get(btnRegion
						.name()), ButtonBoundShape.CIRCLE);
				tb.x = 20 + 70;
				tb.y = 420;
				break;
			default:
				tb = getBtn(btnRegion.name());
				tb.x = 10 + 80 * i;
				tb.y = 4;
				break;
			}
			map.put(btnRegion.name(), tb);
			i++;
		}
		return map;
	}

	private TwoStateButton getBtn(String btnName) {
		return new TwoStateButton(Assets.stageButtons.get(btnName),
				Assets.stageButtons.get(btnName + "Down"),
				ButtonBoundShape.CIRCLE);
	}

	public void update(GL10 gl, float delta) {
		// timeClock += delta;
		camera.update();
		camera.apply(gl);

		if (!Cookie.isPaused) {
			now += delta;
			stateTime += delta;
			switch (Cookie.getState()) {
			case RESETTING:
				break;
			case PLAYING:
				if (!this.hand.touchable)
					this.hand.touchable = true;
				timeStep = Gdx.graphics.getDeltaTime();
				if (!blnHelpOn) {
					updateButtons(delta);
				}
				break;

			case CONTINUE:
				notifier.onContinue();
				Cookie.setState(StatEnum.PLAYING);
				break;
			case PAUSE:
				this.hand.touchable = false;
				timeStep = 0;
				notifier.onGamePause();
				Cookie.isPaused = true;
				break;
			}
		}

		timeClock += timeStep;
		world.step(timeStep, Assets.VELOCITYITERATIONS,
				Assets.VELOCITYITERATIONS);
		stageBack.act(Gdx.graphics.getDeltaTime());
		stageBack.draw();
		drawMesh();
		spriteBatch.begin();
		drawButtons();
		drawBodyTexture();
		updateWorld();
		updateStage();
		drawPathPoints();
		spriteBatch.end();
		if (Config.asBoolean("world.renderif", true))
			renderer.render(world, camera.combined);
		stageFore.act(Gdx.graphics.getDeltaTime());
		stageFore.draw();

		stageConfirm.act(Gdx.graphics.getDeltaTime());
		stageConfirm.draw();

		if (!blnHelpOn) {
			demonsAct();
		}

	}

	private void drawPathPoints() {
		if (null != Cookie.pathPontsKeep && Cookie.pathPontsKeep.size() > 0) {
			for (int i = 0; i < Cookie.pathPontsKeep.size(); i++) {
				spriteBatch.draw(point, Cookie.pathPontsKeep.get(i).x,
						Cookie.pathPontsKeep.get(i).y);
			}
		}
	}

	public void leftDemonsRunBack() {

		for (DemonActor actor : demons) {
			if (actor.state == DemonState.diebyall
					|| actor.state == DemonState.diebyfeidan
					|| actor.state == DemonState.diebyfeilei
					|| actor.state == DemonState.dienormal) {
				continue;
			}
			if (actor.state == DemonState.stand) {
				actor.disappear();
			}
			actor.runBack();
		}
	}

	private void demonsAct() {
		for (DemonActor actor : demons) {
			if (actor.hungry) {
				actor.eat(apples);
			}
			actor.cloudDetect(clouds);
		}
		if (timeClock > demonOccurTime && demonI < demons.size()) {
			timeClock = 0;
			DemonActor demonActor = demons.get(demonI);
			demonActor.run(false);
			demonI++;
			demonOccurTime = (int) MathUtils.random(Assets.DEMON_OCCUR_BREAK,
					Assets.DEMON_OCCUR_BREAK + 2);
		}
	}

	// 更新物理世界
	private void updateWorld() {
		if (world.getBodyCount() <= 0)
			return;
		Iterator<Body> bodys = world.getBodies();
		while (null != bodys && bodys.hasNext()) {
			Body currentBody = bodys.next();
			if (currentBody == null)
				continue;
			// update demons
			if (null != currentBody.getUserData()
					&& currentBody.getUserData() instanceof DemonActor) {
				DemonActor demon = (DemonActor) currentBody.getUserData();
				if (demon.state == DemonState.dienormal) {
					currentBody.setUserData(null);
					// currentBody.getFixtureList().clear();
					currentBody.destroyFixture(currentBody.getFixtureList()
							.get(0));
					// world.destroyBody(currentBody);
					break;
				}
				if (demon.blnChange) {
					currentBody.setUserData(null);
					currentBody.destroyFixture(currentBody.getFixtureList()
							.get(0));
					currentBody.createFixture(bodyFactory
							.getDemonFixture(demon.nature));
					currentBody.setUserData(demon);
				}
				if (demonIsOutOfWorldBoundary(currentBody.getPosition(),
						demon.flip)) {
					Integer curLevel = Cookie.getCurLevelConfig().ordinal() + 1;
					ConfigSet.levelMap.get(curLevel).setDemoneacapenums(
							ConfigSet.levelMap.get(curLevel)
									.getDemoneacapenums() + demon.luckyNum);
					// Cookie.demonEscapeNum.add(demon.luckyNum);
					Gdx.app.log(ConfigSet.levelMap.get(curLevel)
							.getDemoneacapenums().length()
							+ "", "demonEscapeNum");

					currentBody.setUserData(null);
					// currentBody.getFixtureList().clear();
					currentBody.destroyFixture(currentBody.getFixtureList()
							.get(0));
					// world.destroyBody(currentBody);
					break;
				}

			}
			// update rings
			if (null != currentBody.getUserData()
					&& currentBody.getUserData() instanceof UserData) {
				// Gdx.app.log(currentBody.getPosition().x + "",
				// currentBody.getPosition().y + "");
				UserData userData = (UserData) currentBody.getUserData();
				if (userData.remove == true
						|| ringIsOutOfWorldBoundary(currentBody.getPosition())) {
					Cookie.setFeidanAble(false);
					Cookie.setFeidanAble(false);
					int ringsSum = 0;
					for (int i = 0; i < Cookie.getRingsRemain().length; i++) {
						if (i != 6) {
							ringsSum += Cookie.getRingsRemain()[i];
						}
					}
					// for (int num : Cookie.getRingsRemain()) {
					// ringsSum += num;
					// }
					if (ringsSum == 0)
						Cookie.outOfRings = true;
					for (Vector2 point : userData.pathPonts) {
						Cookie.pathPontsKeep.add(new Vector2(point.x, point.y));
					}
					currentBody.setUserData(null);
					// currentBody.getFixtureList().clear();
					currentBody.destroyFixture(currentBody.getFixtureList()
							.get(0));
					// world.destroyBody(currentBody);
					break;
				}
			}
		}
	}

	private boolean demonIsOutOfWorldBoundary(Vector2 demonPos, boolean flip) {
		return demonPos.x < -demonWidth
				|| (flip == true && demonPos.x > Assets.VIRTUAL_WIDTH);

	}

	private boolean ringIsOutOfWorldBoundary(Vector2 ringPos) {
		return ringPos.x > Assets.VIRTUAL_WIDTH + Setting.RING_RADIUS
				|| ringPos.x < -Setting.RING_RADIUS
				|| ringPos.y < -Setting.RING_RADIUS;
		// || ringPos.y > Assets.VIRTUAL_HEIGHT + ringRadius;
	}

	// 更新舞台
	private void updateStage() {
		Iterator<Actor> actors = stageFore.getActors().iterator();
		while (actors.hasNext()) {
			Actor actor = actors.next();
			if (null != actor.name && actor.name.contains("demon")) {
				if (actor.getClass() == DemonActor.class) {
					if (((DemonActor) actor).blnRemove) {
						stageFore.removeActor(actor);
						return;
					}
				}
			}
		}
	}

	/**
	 * 刚体贴图
	 */
	private void drawBodyTexture() {
		Iterator<Body> bodys = world.getBodies();
		while (null != bodys && bodys.hasNext()) {
			Body currentBody = bodys.next();
			if (null != currentBody.getUserData()
					&& currentBody.getUserData().getClass()
							.equals(UserData.class)) {
				((UserData) currentBody.getUserData()).draw(spriteBatch,
						currentBody.getPosition().x * 20,
						currentBody.getPosition().y * 20);
			}
		}
	}

	/**
	 * 按钮绘制
	 */
	private void drawButtons() {
		// spriteBatch.draw(Assets.stageButtons.get("btnBackground"), 0, 0);
		Iterator<String> keySetIterator = btnsMap.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			// switch (BtnSelect.getBtnChoice(btnName)) {
			// case btnAll:
			// if (this.levelConfig.getVisibility() != null
			// && !this.levelConfig.getVisibility()[0])
			// continue;
			// break;
			// case btnEye:
			// if (this.levelConfig.getVisibility() != null
			// && !this.levelConfig.getVisibility()[1])
			// continue;
			// break;
			// case btnFeiLei:
			// if (this.levelConfig.getVisibility() != null
			// && !this.levelConfig.getVisibility()[2])
			// continue;
			// break;
			// case btnFeiDan:
			// if (this.levelConfig.getVisibility() != null
			// && !this.levelConfig.getVisibility()[3])
			// continue;
			// break;
			// }
			btnsMap.get(btnName).draw(spriteBatch);
		}
		for (int i = 0; i < 4; i++) {
			spriteBatch.draw(Assets.fightArrow, 62 + i * 80, 21);
		}
		spriteBatch.draw(Assets.fightArrowLong, 32, 0);

	}

	private void updateButtons(float delta) {
		// touchPoint = screenToViewport(Gdx.input.getX(), Gdx.input.getY());
		touchPoint = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight()
				- Gdx.input.getY());
		boolean justTouched = Gdx.input.justTouched();
		boolean isTouched = Gdx.input.isTouched();
		boolean justReleased = wasTouched && !isTouched;
		wasTouched = isTouched;
		hand.update(delta, justTouched, isTouched, justReleased, touchPoint.x,
				touchPoint.y);
		Iterator<String> keySetIterator = btnsMap.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			btnsMap.get(btnName).update(delta, justTouched, isTouched,
					justReleased, touchPoint.x, touchPoint.y);
		}
		keySetIterator = btnsMap.keySet().iterator();
		while (keySetIterator.hasNext()) {
			String btnName = keySetIterator.next();
			if (btnsMap.get(btnName).wasPressed()) {
				// hand.InitStat();
				Gdx.app.log(btnName, "wasPressed");
				switch (BtnSelect.getBtnChoice(btnName)) {
				case btnMetal:
					if (Cookie.getRingsRemain()[0] > 0) {
						setHand(new UserData("metal", new TextureRegion(
								Assets.rings.get("metalRing")), Nature.METAL),
								Assets.metalHandAnim);
					} else {
						blinkLabel(metalLabel);
						Gdx.app.log("GameWorld", "no such ring remained");
					}
					break;
				case btnWood:
					if (Cookie.getRingsRemain()[1] > 0) {
						setHand(new UserData("wood",
								Assets.rings.get("woodRing"), Nature.WOOD),
								Assets.woodHandAnim);
					} else {
						blinkLabel(woodLabel);
						Gdx.app.log("GameWorld", "no such ring remained");
					}
					break;
				case btnWater:
					if (Cookie.getRingsRemain()[2] > 0) {
						setHand(new UserData("water",
								Assets.rings.get("waterRing"), Nature.WATER),
								Assets.waterHandAnim);
					} else {
						blinkLabel(waterLabel);
						Gdx.app.log("GameWorld", "no such ring remained");
					}
					break;
				case btnFire:
					if (Cookie.getRingsRemain()[3] > 0) {
						setHand(new UserData("fire",
								Assets.rings.get("fireRing"), Nature.FIRE),
								Assets.fireHandAnim);
					} else {
						blinkLabel(fireLabel);
						Gdx.app.log("GameWorld", "no such ring remained");
					}
					break;
				case btnEarth:
					if (Cookie.getRingsRemain()[4] > 0) {
						setHand(new UserData("earth",
								Assets.rings.get("earthRing"), Nature.EARTH),
								Assets.earthHandAnim);
					} else {
						blinkLabel(earthLabel);
						Gdx.app.log("GameWorld", "no such ring remained");
					}
					break;
				case btnPause:
					Cookie.setState(StatEnum.PAUSE);
					sideMenu.show("1-1");
					break;
				case btnReset:
					notifier.onGameReset();
					break;
				case btnAll:
					if (Cookie.getRingsRemain()[5] < 1) {
						blinkLabel(allLabel);
						return;
					}
					if (Cookie.gold >= Setting.ALL_GOLD_COST
							&& Cookie.magic >= Setting.ALL_MAGIC_COST) {
						setHand(new UserData("allRing",
								Assets.rings.get("allRing"), Nature.ALL),
								Assets.allHandAnim);
					} else {
						magicGoldWarn(Setting.ALL_GOLD_COST,
								Setting.ALL_MAGIC_COST);
						Gdx.app.log("GameWorld", "gold or magic not enough");
					}
					break;
				case btnEye:
					if (Cookie.getRingsRemain()[6] < 1) {
						blinkLabel(eyeLabel);
						return;
					}
					if (Cookie.gold >= Setting.EYE_GOLD_COST
							&& Cookie.magic >= Setting.EYE_MAGIC_COST) {
						Setting.showDemon = true;
					} else {
						magicGoldWarn(Setting.EYE_GOLD_COST,
								Setting.EYE_MAGIC_COST);
						Gdx.app.log("GameWorld", "gold or magic not enough");
					}
					break;
				case btnFeiLei:
					if (Cookie.getRingsRemain()[7] < 1) {
						blinkLabel(feileiLabel);
						return;
					}
					if (Cookie.gold >= Setting.FEILEI_GOLD_COST
							&& Cookie.magic >= Setting.FEILEI_MAGIC_COST) {
						setHand(new UserData("feiLeiRing",
								Assets.rings.get("feiLeiRing"), Nature.FEILEI),
								Assets.metalHandAnim);
					} else {
						magicGoldWarn(Setting.FEILEI_GOLD_COST,
								Setting.FEILEI_MAGIC_COST);
						Gdx.app.log("GameWorld", "gold or magic not enough");
					}
					break;
				case btnFeiDan:
					if (Cookie.getRingsRemain()[8] < 1) {
						blinkLabel(feidanLabel);
						return;
					}
					if (Cookie.gold >= Setting.FEIDAN_GOLD_COST
							&& Cookie.magic >= Setting.FEIDAN_MAGIC_COST) {
						setHand(new UserData("feiDan3Ring", Assets.rings
								.get("feiDan3Ring"), Nature.FEIDAN),
								Assets.metalHandAnim);
					} else {
						magicGoldWarn(Setting.FEIDAN_GOLD_COST,
								Setting.FEIDAN_MAGIC_COST);
						Gdx.app.log("GameWorld", "gold or magic not enough");
					}
					break;
				}
			}
		}
	}

	private void magicGoldWarn(int goldCost, int magicCost) {
		if (Cookie.gold < goldCost) {
			blinkLabel(goldLabel);
		}
		if (Cookie.magic < magicCost) {
			blinkLabel(magicLabel);
		}
	}

	private void blinkLabel(Label label) {
		Action action = Sequence.$(FadeOut.$(0.05f), FadeIn.$(0.05f));
		label.action(action);
	}

	private void setHand(UserData userData, Animation handAnim) {
		hand.InitStat();
		hand.setHandAnim(handAnim);
		hand.visible = true;
		hand.setBodyInHandUserData(userData);

		// if (null == hand.getBodyInHandUserData()) {
		// // hand.setBodyInHand(bodyFactory.getRing(BodyType.StaticBody,
		// // userData, ringRadius, startPoint.x, startPoint.y));
		// //
		// hand.setBodyInHandUserData(userData);
		//
		// }
		// else if (hand.getBodyInHandUserData().nature != userData.nature) {
		// // world.destroyBody(hand.getBodyInHand());
		// // hand.setBodyInHand(bodyFactory.getRing(BodyType.StaticBody,
		// // userData, ringRadius, startPoint.x, startPoint.y));
		//
		// hand.setBodyInHand(bodyFactory.getRing(BodyType.StaticBody,
		// userData, ringRadius, startPoint.x, startPoint.y));
		// }
	}

	private void drawMesh() {
		if (null != hand.getBodyInHandUserData()) {
			float ptCenterX = hand.ringX;
			float ptCenterY = hand.ringY;
			if (MathTools.distanceCalc(startPoint, new Vector2(ptCenterX,
					ptCenterY)) > Setting.RING_RADIUS * Assets.PTM_RATIO + 10) {
				List<Vector2> qiedian = MathTools.CalcQieDian(new Vector2(
						ptCenterX, ptCenterY), Setting.RING_RADIUS
						* Assets.PTM_RATIO, startPoint, 50);
				if (qiedian.get(0).x > 0 && qiedian.get(1).x > 0) {
					mesh.setVertices(new float[] { qiedian.get(0).x,
							qiedian.get(0).y, 0,
							Color.toFloatBits(240, 133, 25, 255), 0, 1,
							qiedian.get(1).x, qiedian.get(1).y, 0,
							Color.toFloatBits(240, 133, 25, 255), 1, 1,
							startPoint.x, startPoint.y, 0,
							Color.toFloatBits(250, 250, 100, 255), 0.5f, 0 });
					mesh.render(GL10.GL_TRIANGLES, 0, 3);

					qiedian = MathTools.CalcQieDian(new Vector2(ptCenterX,
							ptCenterY), Setting.RING_RADIUS * Assets.PTM_RATIO,
							startPoint, 80);
					Mesh lineMesh = new Mesh(
							true,
							4,
							4,
							new VertexAttribute(Usage.Position, 3,
									"line_position"),
							new VertexAttribute(Usage.ColorPacked, 4, "a_color"));
					lineMesh.setVertices(new float[] { startPoint.x,
							startPoint.y, 0, Color.toFloatBits(255, 0, 0, 255),
							qiedian.get(0).x, qiedian.get(0).y, 0,
							Color.toFloatBits(255, 0, 0, 255) });
					lineMesh.setIndices(new short[] { 0, 1 });
					lineMesh.render(GL10.GL_LINES, 0, 2);

					Mesh lineMesh2 = new Mesh(
							true,
							4,
							4,
							new VertexAttribute(Usage.Position, 3,
									"line_position"),
							new VertexAttribute(Usage.ColorPacked, 4, "a_color"));
					lineMesh2.setVertices(new float[] { startPoint.x,
							startPoint.y, 0, Color.toFloatBits(255, 0, 0, 255),
							qiedian.get(1).x, qiedian.get(1).y, 0,
							Color.toFloatBits(255, 0, 0, 255) });
					lineMesh2.setIndices(new short[] { 0, 1 });
					lineMesh2.render(GL10.GL_LINES, 0, 2);
				}
			}
		}
	}
}
