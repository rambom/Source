package com.android.ringfly.common;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class BodyFactory {
	private World world;
	private static BodyFactory instance;

	private BodyFactory(World myWorld) {
		this.world = myWorld;
	}

	public static BodyFactory GetInstance(World myWorld) {
		if (null == instance)
			instance = new BodyFactory(myWorld);
		return instance;
	}

	public static BodyFactory GetInstance() {
		return instance;
	}

	public static void removeInstance() {
		try {
			instance.finalize();
		} catch (Throwable e) {
		}
		instance = null;
	}

	// 创建圈圈刚体
	public Body getRing(BodyType type, UserData data, float ringRadius,
			float posX, float posY) {
		CircleShape sphereShape = new CircleShape();
		sphereShape.setRadius(ringRadius);
		FixtureDef sphereFixture = new FixtureDef();
		sphereFixture.filter.groupIndex = -1;
		sphereFixture.density = 1;
		sphereFixture.friction = 3;
		sphereFixture.restitution = 0.1f;
		sphereFixture.shape = sphereShape;
		BodyDef sphereBodyDef = new BodyDef();
		sphereBodyDef.type = type;
		sphereBodyDef.position.set(posX / 20, posY / 20);
		Body ringSphere = world.createBody(sphereBodyDef);
		ringSphere.setSleepingAllowed(true);
		ringSphere.createFixture(sphereFixture);
		if (null != data) {
			ringSphere.setUserData(data);
		}
		return ringSphere;
	}

	// 创建云刚体
	public Body getCloud() {
		PolygonShape pp = new PolygonShape();
		BodyDef bd = new BodyDef();
		bd.type = BodyDef.BodyType.StaticBody;
		Vector2[] vertices = {
				new Vector2(40.0f / Assets.PTM_RATIO, 45.0f / Assets.PTM_RATIO),
				new Vector2(-4.0f / Assets.PTM_RATIO, 26.0f / Assets.PTM_RATIO),
				new Vector2(16.0f / Assets.PTM_RATIO, 7.0f / Assets.PTM_RATIO),
				new Vector2(40.0f / Assets.PTM_RATIO, 0.0f / Assets.PTM_RATIO),
				new Vector2(76.0f / Assets.PTM_RATIO, 15.0f / Assets.PTM_RATIO),
				new Vector2(77.0f / Assets.PTM_RATIO, 26.0f / Assets.PTM_RATIO) };
		pp.set(vertices);
		Body body = world.createBody(bd);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = pp;
		fixtureDef.filter.groupIndex = -2;
		fixtureDef.density = 1f;
		body.createFixture(fixtureDef);

		return body;
	}

	// 创建石头刚体
	public Body getStone() {
		PolygonShape pp = new PolygonShape();
		BodyDef bd = new BodyDef();
		bd.type = BodyDef.BodyType.StaticBody;
		Vector2 v = new Vector2(
				(float) Assets.taiJiStone.getRegionWidth() / 40,
				(float) Assets.taiJiStone.getRegionHeight() / 40);
		pp.setAsBox(v.x, v.y, v, 0);
		Body body = world.createBody(bd);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = pp;
		fixtureDef.density = 1f;
		fixtureDef.restitution = 0.8f;
		fixtureDef.friction = 0.8f;
		fixtureDef.filter.groupIndex = -2;
		body.createFixture(fixtureDef);
		return body;
	}

	// 创建妖怪刚体
	public FixtureDef getDemonFixture(Nature nature) {
		PolygonShape pp = new PolygonShape();
		Vector2[] vertices = null;
		if (nature == Nature.POPO) {
			vertices = new Vector2[] {
					new Vector2(5.0f / Assets.PTM_RATIO,
							68.0f / Assets.PTM_RATIO),
					new Vector2(0f / Assets.PTM_RATIO,
							47.0f / Assets.PTM_RATIO),
					new Vector2(10.0f / Assets.PTM_RATIO,
							0.0f / Assets.PTM_RATIO),
					new Vector2(22.0f / Assets.PTM_RATIO,
							0.0f / Assets.PTM_RATIO),
					new Vector2(24.0f / Assets.PTM_RATIO,
							1.0f / Assets.PTM_RATIO),
					new Vector2(39.0f / Assets.PTM_RATIO,
							53.0f / Assets.PTM_RATIO),
					new Vector2(31.0f / Assets.PTM_RATIO,
							67.0f / Assets.PTM_RATIO) };
		} else {
			vertices = new Vector2[] {
					new Vector2(6.0f / Assets.PTM_RATIO,
							55.0f / Assets.PTM_RATIO),
					new Vector2(0.0f / Assets.PTM_RATIO,
							36.0f / Assets.PTM_RATIO),
					new Vector2(0.0f / Assets.PTM_RATIO,
							5.0f / Assets.PTM_RATIO),
					new Vector2(9.0f / Assets.PTM_RATIO,
							0.0f / Assets.PTM_RATIO),
					new Vector2(24.0f / Assets.PTM_RATIO,
							9.0f / Assets.PTM_RATIO),
					new Vector2(21.0f / Assets.PTM_RATIO,
							41.0f / Assets.PTM_RATIO),
					new Vector2(12.0f / Assets.PTM_RATIO,
							56.0f / Assets.PTM_RATIO) };
		}
		pp.set(vertices);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.filter.groupIndex = -2;
		fixtureDef.density = 1f;
		fixtureDef.shape = pp;
		return fixtureDef;
	}

	public Body getDemon(Nature nature) {
		BodyDef bd = new BodyDef();
		bd.type = BodyDef.BodyType.StaticBody;
		bd.position.set(0, 0);
		Body body = world.createBody(bd);
		body.createFixture(getDemonFixture(nature));
		return body;
	}

	public Body getEdge(Vector2 v1, Vector2 v2) {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0, 0);
		groundBodyDef.linearDamping = 0.1f;
		Body groundBody = world.createBody(groundBodyDef);
		EdgeShape groundEdgeShape = new EdgeShape();

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.filter.groupIndex = -2;
		fixtureDef.shape = groundEdgeShape;
		fixtureDef.density = 1f;

		groundEdgeShape.set(v1, v2);
		groundBody.createFixture(fixtureDef);
		groundBody.setSleepingAllowed(true);
		return groundBody;
	}

}
