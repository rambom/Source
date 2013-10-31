package com.android.ringfly.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TwoStateButton {
	private boolean wasPressed;
	public float x;
	public float y;
	private float w;
	private float h;
	private boolean activated;
	private boolean down;
	private TextureRegion upface, downface;
	private ButtonBoundShape boundshape;
	private Vector2[] polygonVertices;
	public boolean visible;

	public enum ButtonBoundShape {
		CIRCLE, RECTANGLE, POLYGON;
	}

	public TwoStateButton(TextureRegion upface, TextureRegion downface,
			ButtonBoundShape boundshape) {
		this.wasPressed = false;
		this.activated = false;
		this.down = false;
		this.upface = upface;
		this.downface = downface;
		this.w = upface.getRegionWidth();
		this.h = upface.getRegionHeight();
		this.boundshape = boundshape;
		this.visible = true;
	}

	public TwoStateButton(TextureRegion face, ButtonBoundShape boundshape) {
		this.wasPressed = false;
		this.activated = false;
		this.down = false;
		this.upface = face;
		this.downface = face;
		this.w = face.getRegionWidth();
		this.h = face.getRegionHeight();
		this.boundshape = boundshape;
	}

	public void setBoundshape(ButtonBoundShape boundshape) {
		this.boundshape = boundshape;
	}

	public void setPolygonVertices(Vector2... polygonVertices) {
		for (int i = 0; i < polygonVertices.length; i++) {
			polygonVertices[i].x += this.x;
			polygonVertices[i].y += this.y;
		}
		this.polygonVertices = polygonVertices;
	}

	public void setWidth(float width) {
		w = width;
	}

	public void setHeight(float height) {
		h = height;
	}

	public void update(float delta, boolean justTouched, boolean isTouched,
			boolean justReleased, float x, float y) {
		wasPressed = false;
		if (justTouched && inBounds(x, y)) {
			activated = true;
			down = true;
		} else if (isTouched) {
			down = activated && inBounds(x, y);
		} else if (justReleased) {
			wasPressed = activated && inBounds(x, y);
			activated = false;
			down = false;
		} else {
			activated = false;
		}
	}

	public void update(boolean justTouched, boolean isTouched,
			boolean justReleased, float x, float y) {
		wasPressed = false;
		if (justTouched && inBounds(x, y)) {
			activated = true;
			down = true;
		} else if (isTouched) {
			down = activated && inBounds(x, y);
		} else if (justReleased) {
			wasPressed = activated && inBounds(x, y);
			activated = false;
			down = false;
		} else {
			activated = false;
		}
	}

	private boolean inBounds(float x, float y) {
		if (this.boundshape == ButtonBoundShape.RECTANGLE) {
			Rectangle r = new Rectangle(this.x, this.y, this.w, this.h);
			return PointInTester.pointInRectangle(r, x, y);
		} else if (this.boundshape == ButtonBoundShape.CIRCLE) {
			Circle c = new Circle(this.x + this.w / 2, this.y + this.h / 2,
					this.w / 2);
			return PointInTester.pointInCircle(c, x, y);
		} else if (this.boundshape == ButtonBoundShape.POLYGON) {
			return PointInTester.pointInPolygon(new Vector2(x, y),
					this.polygonVertices);
		}
		return false;
	}

	public void draw(SpriteBatch spriteBatch) {
		if (down) {
			spriteBatch.draw(downface, x, y);
		} else {
			spriteBatch.draw(upface, x, y);
		}
	}

	public boolean wasPressed() {
		return this.wasPressed;
	}

	public void rightOn(float right) {
		x = right - w;
	}

	public void leftOn(float left) {
		x = left;
	}

	public void centerHorizontallyOn(float centerX) {
		x = centerX - w / 2;
	}

	public void bottomOn(float bottom) {
		y = bottom;
	}

	public void topOn(float top) {
		y = top - h;
	}

	public void centerVerticallyOn(float centerY) {
		y = centerY - h / 2;
	}
}
