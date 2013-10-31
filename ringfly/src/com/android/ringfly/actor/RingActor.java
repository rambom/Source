package com.android.ringfly.actor;

import com.android.ringfly.common.Assets;
import com.android.ringfly.common.Assets.Sounds;
import com.android.ringfly.ringfly.GameWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class RingActor extends Actor {
	private Texture texture;
	private TextureRegion textureRegion;

	// private boolean dragAble;
	// private boolean touchUped;
	// private boolean touchHited;
	// private boolean touchDowned;
	private Body ringSphere;
	private boolean runOnce;
	public boolean useOnce;

	public RingActor(String name, TextureRegion textureRegion) {
		super(name);
		this.textureRegion = textureRegion;
		this.width = textureRegion.getRegionWidth();
		this.height = textureRegion.getRegionHeight();
		initState();
	}

	public RingActor(String name, Texture texture) {
		super(name);
		this.texture = texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
		initState();
	}

	private void initState() {
		this.visible = false;
		runOnce = false;
		useOnce = false;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		if (null != this.texture) {
			batch.draw(this.texture, this.x, this.y);
		} else if (null != this.textureRegion) {
			batch.draw(this.textureRegion, this.x, this.y);
		}
		this.touchable = true;
	}

	public void run(Vector2 startPoint, Vector2 endPoint, Vector2 bdfPoint) {
		if (runOnce)
			return;
		runOnce = true;
		GameWorld.arrow.visible = false;
		Gdx.app.log("waterRing", "go");
		CircleShape sphereShape = new CircleShape();
		sphereShape.setRadius(2);
		FixtureDef sphereFixture = new FixtureDef();
		sphereFixture.density = 1;
		sphereFixture.friction = 3;
		sphereFixture.restitution = 0.1f;
		sphereFixture.shape = sphereShape;

		BodyDef sphereBodyDef = new BodyDef();
		sphereBodyDef.type = BodyType.DynamicBody;

		sphereBodyDef.position.set(bdfPoint.x / 20, bdfPoint.y / 20);
		ringSphere = GameWorld.world.createBody(sphereBodyDef);
		ringSphere.setSleepingAllowed(true);
		ringSphere.createFixture(sphereFixture);

		float distanceX = startPoint.x - endPoint.x;
		float distanceY = startPoint.y - endPoint.y;
		float distance = (float) Math.sqrt(distanceX * distanceX + distanceY
				* distanceY);
		float birdAngle = (float) Math.atan2(distanceY, distanceX);
		ringSphere.setUserData(this);
		float vx = (float) (distance * Math.cos(birdAngle) / 4);
		float vy = (float) (distance * Math.sin(birdAngle) / 4);
		ringSphere.setLinearVelocity(vx, vy);
	}

	public boolean isSleeping() {
		return ringSphere.isAwake();
	}

	public TextureRegion getTextureRegion() {
		return textureRegion;
	}

	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

}
