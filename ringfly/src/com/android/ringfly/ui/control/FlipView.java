package com.android.ringfly.ui.control;

import java.util.ArrayList;
import java.util.List;

import com.android.ringfly.common.Assets;
import com.android.ringfly.common.MathTools;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;

/**
 * 
 * @author fgshu
 * 
 */
public class FlipView extends Actor {

	private int size;
	private int lastSelectedIndex;
	private int selectedIndex;
	private int currentIndex;
	private TextureRegion backGround;
	private List<TextureRegion> textureRegions;
	private ENUM_FLIP_DIRECTION flipDirection;
	private float lastX, lastY;
	private Vector2 downPoint;
	private float dragTime;
	private float flexibleX, flexibleY;
	private TextureRegion[] pointViews;
	private boolean numberNavigationVisible;
	private boolean pointNavigationVisible;
	private Vector2 touchPoint0, touchPoint1, touchPoint2, touchPoint3;
	private boolean touch0, touch1, touch2, touch3;
	private float curDistance01, lastDistance01;
	private boolean multiTouching;
	private String strFlag;

	public FlipView(ENUM_FLIP_DIRECTION dir) {
		super();
		this.flipDirection = dir;
		this.selectedIndex = 0;
		this.dragTime = 0;
	}

	public FlipView(String name, ENUM_FLIP_DIRECTION dir) {
		super(name);
		this.flipDirection = dir;
		this.selectedIndex = 0;
	}

	public FlipView(ENUM_FLIP_DIRECTION dir, List<TextureRegion> textureRegions) {
		super();
		this.flipDirection = dir;
		this.textureRegions = textureRegions;
		if (textureRegions != null && textureRegions.size() > 0) {
			this.width = textureRegions.get(0).getRegionWidth();
			this.height = textureRegions.get(0).getRegionHeight();
		}
		this.size = textureRegions.size();
		this.selectedIndex = 0;
	}

	public FlipView(String name, ENUM_FLIP_DIRECTION dir,
			List<TextureRegion> textureRegions) {
		super(name);
		this.flipDirection = dir;
		this.textureRegions = textureRegions;
		if (textureRegions != null && textureRegions.size() > 0) {
			this.width = textureRegions.get(0).getRegionWidth();
			this.height = textureRegions.get(0).getRegionHeight();
		}
		this.size = textureRegions.size();
		this.selectedIndex = 0;
	}

	public FlipView(String name, ENUM_FLIP_DIRECTION dir,
			TextureRegion... textureRegions) {
		super(name);
		this.flipDirection = dir;
		this.textureRegions = new ArrayList<TextureRegion>();
		for (TextureRegion t : textureRegions) {
			this.textureRegions.add(t);
		}
		if (textureRegions != null && textureRegions.length > 0) {
			this.width = textureRegions[0].getRegionWidth();
			this.height = textureRegions[0].getRegionHeight();
		}
		this.size = textureRegions.length;
		pointViews = new TextureRegion[this.size];
		this.selectedIndex = 0;
		select(selectedIndex);
		this.dragTime = 0;
		this.flexibleX = 0;
		this.flexibleY = 0;
		this.numberNavigationVisible = true;
		this.pointNavigationVisible = true;

		touchPoint0 = new Vector2();
		touchPoint1 = new Vector2();
		touchPoint2 = new Vector2();
		touchPoint3 = new Vector2();
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		this.dragTime += Gdx.graphics.getDeltaTime();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		if (this.backGround != null)
			batch.draw(this.backGround, 0, 0);

		for (int i = 0; i < this.size; i++) {
			batch.draw(this.textureRegions.get(i), this.x + this.width * i,
					this.y);
		}

		for (int i = 0; i < this.size; i++) {
			if (pointNavigationVisible) {
				batch.draw(pointViews[i], i * 20 + 100, 30);
			}
			if (numberNavigationVisible && i == this.currentIndex)
				Assets.huaWenXiHeiFont.draw(batch, (i + 1) + "", i * 20 + 100,
						30);
		}
		if (this.strFlag != null) {
			Assets.huaWenXiHeiFont.draw(batch, strFlag, 400, 30);
		}

	}

	private void select(int index) {
		if (this.x != -Gdx.graphics.getWidth() * this.selectedIndex) {
			this.x = -Gdx.graphics.getWidth() * this.selectedIndex;
		}
		for (int i = 0; i < pointViews.length; i++) {
			if (i == index)
				pointViews[i] = new TextureRegion(
						Assets.pageIndicatorFocusedRegion);
			else
				pointViews[i] = new TextureRegion(Assets.pageIndicatorRegion);
		}
		this.currentIndex = this.selectedIndex;
	}

	@Override
	public Actor hit(float x, float y) {
		return this;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		switch (pointer) {
		case 0:
			this.touch0 = true;
			this.touchPoint0.set(x, y);
			break;
		case 1:
			this.touch1 = true;
			this.touchPoint1.set(x, y);
			break;
		case 2:
			this.touch2 = true;
			this.touchPoint2.set(x, y);
			break;
		case 3:
			this.touch3 = true;
			this.touchPoint3.set(x, y);
			break;
		default:
			break;
		}
		this.dragTime = 0.0001f;
		lastSelectedIndex = this.selectedIndex;
		downPoint = new Vector2(x, y);
		this.lastX = this.x;
		this.lastY = this.y;
		Gdx.app.log("" + MathUtils.roundPositive(0.5f), "");
		return true;

	}

	@Override
	public void touchUp(float x, float y, int pointer) {
		Gdx.app.log("deltaX" + (this.x - this.lastX), "deltaTime:"
				+ this.dragTime);
		// Gdx.app.log(this.getClass().getName(), "touchUp");
		switch (pointer) {
		case 0:
			this.touch0 = false;
			this.multiTouching = false;
			break;
		case 1:
			this.touch1 = false;
			this.multiTouching = false;
			break;
		case 2:
			this.touch2 = false;
			break;
		case 3:
			this.touch3 = false;
			break;
		default:
			break;
		}

		float deltaS = this.x - this.lastX;
		float speed = Math.abs(deltaS / this.dragTime);
		Gdx.app.log(speed + "", "");
		float div = this.x / Gdx.graphics.getWidth();
		int kk = MathUtils.round(div);
		this.selectedIndex = -kk;
		if (this.selectedIndex == this.lastSelectedIndex
				&& Math.abs(deltaS) > 10 && speed > 600) {
			if (deltaS < 0)
				this.selectedIndex++;
			else
				this.selectedIndex--;
		}
		if (this.selectedIndex < 0)
			this.selectedIndex = 0;
		if (this.selectedIndex > this.size - 1)
			this.selectedIndex = this.size - 1;
		float toX = -Gdx.graphics.getWidth() * this.selectedIndex;
		float deltaX = Math.abs(this.x - toX);
		Action ajustX = MoveTo.$(toX, this.y, deltaX / 1000);
		ajustX.setCompletionListener(new OnActionCompleted() {
			public void completed(Action action) {
				select(selectedIndex);
			}
		});
		this.action(ajustX);

	}

	@Override
	public void touchDragged(float x, float y, int pointer) {
		// Gdx.app.log(this.getClass().getName(), "touchDragged" + x + ";" + y);
		// to flip in horizontal
		// we can detect multi-touch using pointer
		Gdx.app.log("pointer", pointer + "");
		switch (pointer) {
		case 0:
			this.touchPoint0.set(x, y);
			break;
		case 1:
			this.touchPoint1.set(x, y);
			break;
		case 2:
			this.touchPoint2.set(x, y);
			break;
		case 3:
			this.touchPoint3.set(x, y);
			break;
		default:
			break;
		}
		if (touch0 && touch1) {
			if (!this.multiTouching) {
				this.multiTouching = true;
				lastDistance01 = (float) MathTools.distanceCalc(touchPoint0,
						touchPoint1);
			}
			curDistance01 = (float) MathTools.distanceCalc(touchPoint0,
					touchPoint1);
			float deltaDis = curDistance01 - lastDistance01;
			if (deltaDis > 10) {
				Gdx.app.log("zome in", "");
				lastDistance01 = curDistance01;
				strFlag = "zome in";
			} else if (deltaDis < -10) {
				Gdx.app.log("zoom out", "");
				lastDistance01 = curDistance01;
				strFlag = "zome out";
			}
			Gdx.app.log("distance", curDistance01 + "");
		} else {
			this.multiTouching = false;
			strFlag = "normal";
		}

		if (this.flipDirection == ENUM_FLIP_DIRECTION.Horizontal) {
			if (Math.abs(x - downPoint.x) >= 1) {
				float toX = x + this.x - downPoint.x;
				if (toX > flexibleX)
					toX = flexibleX;
				if (toX < -Gdx.graphics.getWidth() * (this.size - 1)
						- flexibleX)
					toX = -Gdx.graphics.getWidth() * (this.size - 1)
							- flexibleX;
				this.x = toX;
			}
		}
		// to flip in vertical
		else if (this.flipDirection == ENUM_FLIP_DIRECTION.Vertical) {

		}
	}

	/* Getters and Setters */
	public int getSize() {
		return size;
	}

	public List<TextureRegion> getTextureRegions() {
		return textureRegions;
	}

	public void setTextureRegions(List<TextureRegion> textureRegions) {
		this.textureRegions = textureRegions;
		this.size = textureRegions.size();
	}

	public ENUM_FLIP_DIRECTION getFlipDirection() {
		return flipDirection;
	}

	public void setFlipDirection(ENUM_FLIP_DIRECTION flipDirection) {
		this.flipDirection = flipDirection;
	}

	public TextureRegion getBackGround() {
		return backGround;
	}

	public void setBackGround(TextureRegion backGround) {
		this.backGround = backGround;
	}

	public float getFlexibleX() {
		return flexibleX;
	}

	public void setFlexibleX(float flexibleX) {
		this.flexibleX = flexibleX;
	}

	public float getFlexibleY() {
		return flexibleY;
	}

	public void setFlexibleY(float flexibleY) {
		this.flexibleY = flexibleY;
	}

	public boolean isNumberNavigationVisible() {
		return numberNavigationVisible;
	}

	public void setNumberNavigationVisible(boolean numberNavigationVisible) {
		this.numberNavigationVisible = numberNavigationVisible;
	}

	public boolean isPointNavigationVisible() {
		return pointNavigationVisible;
	}

	public void setPointNavigationVisible(boolean pointNavigationVisible) {
		this.pointNavigationVisible = pointNavigationVisible;
	}

	public enum ENUM_FLIP_DIRECTION {
		Horizontal, Vertical;
	}

}
