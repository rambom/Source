package com.android.ringfly.actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.android.ringfly.common.Assets;
import com.android.ringfly.common.Assets.BtnLevelUp;
import com.android.ringfly.common.TwoStateButton;
import com.android.ringfly.common.TwoStateButton.ButtonBoundShape;
import com.android.ringfly.ringfly.Cookie;
import com.android.ringfly.ringfly.GameWorld;
import com.android.ringfly.ringfly.GameWorldNotifier;
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

public class HelpActor extends Actor {
	private TextureRegion helpRegion1, helpRegion2, helpRegion3;
	private List<Sprite> helpSpriteList;
	private Action showAction, hideAction;
	public boolean isShow;
	private String curLevel;
	private TwoStateButton btnNext;
	private float width, height, centerX, centerY;
	private Vector2 touchPoint;
	private boolean wasTouched;
	Pixmap pixmap;
	Texture maskTexture;
	TextureRegion region;
	Sprite maskSprite;
	Integer curId;
	GameWorld gameworld;

	public HelpActor(String name, GameWorld gameworld) {
		super(name);
		helpRegion1 = Assets.helpRegionMap.get("help1");
		helpRegion2 = Assets.helpRegionMap.get("help2");
		helpRegion3 = Assets.helpRegionMap.get("help3");
		this.width = helpRegion1.getRegionWidth();
		this.height = helpRegion1.getRegionHeight();
		this.centerX = this.width / 2 - 55;
		this.centerY = this.height / 2 - 60;
		this.gameworld = gameworld;
		this.x = Gdx.graphics.getWidth() / 2 - this.width / 2;
		this.y = Gdx.graphics.getHeight() / 2 - this.height / 2;
		helpSpriteList = new ArrayList<Sprite>();
		helpSpriteList.add(new Sprite(helpRegion1));
		helpSpriteList.add(new Sprite(helpRegion2));
		helpSpriteList.add(new Sprite(helpRegion3));
		this.curLevel = "";
		btnNext = new TwoStateButton(Assets.helpRegionMap.get("click"),
				ButtonBoundShape.CIRCLE);

		Color color = Color.YELLOW;

		pixmap = new Pixmap(1024, 1024, Format.RGBA8888);
		pixmap.setColor(0f, 0f, 0f, 1f);
		// pixmap.drawRectangle(0, 0, 100, 100);
		pixmap.fill();
		maskTexture = new Texture(pixmap);
		maskSprite = new Sprite(maskTexture);
		curId = 0;
	}

	private Label getLabel(String value, Color tint) {
		Label label = new Label(value, Assets.uiSkin);
		label.setWrap(false);
		label.setAlignment(Align.BOTTOM | Align.LEFT);
		label.setColor(tint);
		return label;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		// sprite.draw(batch, 1f);
		if (this.x != -this.width) {
			// Color oldColor = Assets.huaWenXiHeiFont.getColor();
			maskSprite.draw(batch, 0.5f);
			Sprite helpSprite = helpSpriteList.get(curId);
			helpSprite.setPosition(x, y);
			helpSprite.draw(batch, color.a * parentAlpha);
			drawButtons(batch);
			updateButtons();
		}
	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

	private void updateButtons() {
		touchPoint = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight()
				- Gdx.input.getY());
		boolean justTouched = Gdx.input.justTouched();
		boolean isTouched = Gdx.input.isTouched();
		boolean justReleased = wasTouched && !isTouched;
		wasTouched = isTouched;
		btnNext.update(justTouched, isTouched, justReleased, touchPoint.x,
				touchPoint.y);
		btnNext.x = this.x + centerX + 230;
		btnNext.y = this.y + centerY - 100;
		if (btnNext.wasPressed()) {
			Gdx.app.log("btnNext", "was pressed");
			if (curId < helpSpriteList.size() - 1) {
				curId++;
			} else {
				// goon
				this.visible = false;
				gameworld.blnHelpOn = false;
			}
		}

	}

	private void drawButtons(SpriteBatch batch) {
		btnNext.draw(batch);
	}

	public void hide() {
		this.actions.clear();
		if (true || this.x == 0 && this.isShow) {
			this.isShow = false;
			// this.actions.remove();
			hideAction = FadeOut.$(0.2f);
			hideAction.setCompletionListener(new OnActionCompleted() {
				@Override
				public void completed(Action action) {
					try {
						this.finalize();
					} catch (Throwable e) {
					}
				}
			});
			this.action(hideAction);
		}
	}

	public void show(String curLevel) {
		this.actions.clear();
		if (true || this.x == -helpRegion1.getRegionWidth() && !this.isShow) {
			this.isShow = true;
			this.curLevel = curLevel;
			// this.actions.remove();
			showAction = MoveTo.$(0, 0, 0.2f);
			this.action(showAction);
		}
	}
}
