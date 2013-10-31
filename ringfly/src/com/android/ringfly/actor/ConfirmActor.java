package com.android.ringfly.actor;

import java.util.ArrayList;
import java.util.List;

import com.android.ringfly.common.Assets;
import com.android.ringfly.common.TwoStateButton;
import com.android.ringfly.common.TwoStateButton.ButtonBoundShape;
import com.android.ringfly.dao.sqlite.SettingDAO;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ConfirmActor extends Actor {
	private TextureRegion helpRegion1;
	private List<Sprite> helpSpriteList;
	private Action showAction, hideAction;
	public boolean isShow;
	private TwoStateButton btnCancel, btnConfirm;
	private float width, height, centerX, centerY;
	private Vector2 touchPoint;
	private boolean wasTouched;
	Pixmap pixmap;
	Texture maskTexture;
	TextureRegion region;
	Sprite maskSprite;
	Integer curId;
	SettingDAO settingDAO;

	public ConfirmActor(String name, SettingDAO dao) {
		super(name);
		this.settingDAO = dao;
		helpRegion1 = Assets.exitConfirmRegionMap.get("exitConfirmDialog");
		this.width = helpRegion1.getRegionWidth();
		this.height = helpRegion1.getRegionHeight();
		this.centerX = this.width / 2 - 55;
		this.centerY = this.height / 2 - 60;
		this.x = Gdx.graphics.getWidth() / 2 - this.width / 2;
		this.y = Gdx.graphics.getHeight() / 2 - this.height / 2;
		helpSpriteList = new ArrayList<Sprite>();
		helpSpriteList.add(new Sprite(helpRegion1));
		btnCancel = new TwoStateButton(
				Assets.exitConfirmRegionMap.get("cancel"),
				ButtonBoundShape.CIRCLE);
		btnConfirm = new TwoStateButton(
				Assets.exitConfirmRegionMap.get("confirm"),
				ButtonBoundShape.CIRCLE);

		pixmap = new Pixmap(1024, 1024, Format.RGBA8888);
		pixmap.setColor(0f, 0f, 0f, 1f);
		// pixmap.drawRectangle(0, 0, 100, 100);
		pixmap.fill();
		maskTexture = new Texture(pixmap);
		maskSprite = new Sprite(maskTexture);
		curId = 0;
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
		btnCancel.update(justTouched, isTouched, justReleased, touchPoint.x,
				touchPoint.y);
		btnConfirm.update(justTouched, isTouched, justReleased, touchPoint.x,
				touchPoint.y);
		btnCancel.x = this.x + centerX - 150;
		btnCancel.y = this.y + centerY - 120;
		btnConfirm.x = this.x + centerX + 150;
		btnConfirm.y = this.y + centerY - 120;
		if (btnConfirm.wasPressed()) {
			this.visible = false;
			this.settingDAO.finishGame();

		}
		if (btnCancel.wasPressed()) {
			this.visible = false;
		}

	}

	private void drawButtons(SpriteBatch batch) {
		btnCancel.draw(batch);
		btnConfirm.draw(batch);
	}

	// public void hide() {
	// this.actions.clear();
	// if (true || this.x == 0 && this.isShow) {
	// this.isShow = false;
	// // this.actions.remove();
	// hideAction = FadeOut.$(0.2f);
	// hideAction.setCompletionListener(new OnActionCompleted() {
	// @Override
	// public void completed(Action action) {
	// try {
	// this.finalize();
	// } catch (Throwable e) {
	// }
	// }
	// });
	// this.action(hideAction);
	// }
	// }
	//
	// public void show(String curLevel) {
	// this.actions.clear();
	// if (true || this.x == -helpRegion1.getRegionWidth() && !this.isShow) {
	// this.isShow = true;
	// // this.actions.remove();
	// showAction = MoveTo.$(0, 0, 0.2f);
	// this.action(showAction);
	// }
	// }
}
