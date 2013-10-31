package com.android.ringfly.common;

import java.util.ArrayList;
import java.util.List;

import com.android.ringfly.actor.CloudActor;
import com.android.ringfly.actor.DemonActor;
import com.android.ringfly.actor.TaiJiStoneActor;
import com.android.ringfly.actor.DemonActor.DemonState;
import com.android.ringfly.actor.RingActor;
import com.android.ringfly.common.Assets.Sounds;
import com.android.ringfly.ringfly.Cookie;
import com.android.ringfly.ringfly.GameWorldNotifier;
import com.android.ringfly.ringfly.PlayingScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MyContact<T> implements ContactListener {
	protected T fromscreen;
	private int MAX_IMPULSE = 10;
	private GameWorldNotifier notifier;

	public MyContact(T screen, GameWorldNotifier notifier) {
		this.fromscreen = screen;
		this.notifier = notifier;
	}

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// A created earlier than B
		if (impulse.getNormalImpulses()[0] > MAX_IMPULSE) {
			if (null != contact.getFixtureA().getBody().getUserData()
					&& null != contact.getFixtureB().getBody().getUserData()) {
				Object objectA = (contact.getFixtureA().getBody().getUserData());
				Object objectB = (contact.getFixtureB().getBody().getUserData());
				// Åö×²ÔªËØ¼ì²â
				// ring and taijistone
				if (objectA instanceof TaiJiStoneActor
						&& objectB instanceof UserData) {
					List<Vector2> contactPoints = new ArrayList<Vector2>();
					Gdx.app.log("MyContact", "show a fire");
					WorldManifold manifold = contact.getWorldManifold();
					int numContactPoints = manifold.getNumberOfContactPoints();
					System.out.println("numContactPoints is "
							+ numContactPoints);
					for (int j = 0; j < numContactPoints; j++) {
						Vector2 point = manifold.getPoints()[j];
						contactPoints.add(point);
						// Gdx.app.log((point.x * 20) + "", (point.y * 20) +
						// "");
					}
					notifier.onContactStone(contactPoints);
				}

				// ring and demon
				if (objectA instanceof DemonActor
						&& objectB instanceof UserData) {
					// Åö×²ÔªËØÊôÐÔ¼ì²â
					DemonActor demon = (DemonActor) objectA;
					UserData userData = (UserData) objectB;
					if (Nature
							.fight(userData.nature,
									(demon.isTaiJi && demon.nature != Nature.POPO) ? Nature.TAIJI
											: demon.nature)) {
						demon.die();
					} else {
						Assets.playSound(Sounds.sound_hit_demon_fail);
						if (demon.state == DemonState.hiderun
								&& demon.nature != Nature.POPO) {
							demon.hitFail();
						}
					}
					switch (userData.nature) {
					case METAL:
						break;
					case WOOD:
						break;
					case WATER:
						break;
					case FIRE:
						break;
					case EARTH:
						break;
					case ALL:
						break;
					case FEIDAN:
						Cookie.setFeidanAble(false);
						break;
					case FEILEI:
						Cookie.setFeileiAble(false);
						break;
					}
					userData.remove = true;
				}

			}
		}
	}
}
