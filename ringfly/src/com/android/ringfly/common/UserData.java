package com.android.ringfly.common;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class UserData {
	private String bodyName;
	private TextureRegion textureRegion;
	public boolean remove;
	private float w;
	private float h;
	public Nature nature;
	public boolean blnDraw;
	private Pixmap pixmap;
	private Texture point;
	private Vector2 lastPathPoint, curPathPont;
	public List<Vector2> pathPonts;
	private Sprite sprite;

	public UserData(String bodyName, TextureRegion textureRegion, Nature n) {
		this.bodyName = bodyName;
		this.textureRegion = textureRegion;
		this.remove = false;
		this.w = this.textureRegion.getRegionWidth();
		this.h = this.textureRegion.getRegionHeight();
		this.nature = n;
		this.blnDraw = false;

		pixmap = new Pixmap(8, 8, Format.RGBA8888);
		pixmap.setColor(0.5f, 0.5f, 0f, 1f);
		pixmap.drawCircle(4, 4, 2);
		point = new Texture(pixmap);
		lastPathPoint = new Vector2(0, 0);
		curPathPont = new Vector2(0, 0);
		pathPonts = new ArrayList<Vector2>();
		sprite = new Sprite(this.textureRegion);
	}

	public void draw(SpriteBatch spriteBatch, float x, float y) {
		if (blnDraw) {
			sprite.setPosition(x - w / 2, y - h / 2);
			sprite.draw(spriteBatch);
			if (this.bodyName.equals("feiDan3Ring")) {
				sprite.rotate(-1f);
			}
			// spriteBatch.draw(this.textureRegion, x - w / 2, y - h / 2);
			curPathPont.set(x, y);
			if (lastPathPoint.x == 0 && lastPathPoint.y == 0) {
				lastPathPoint.set(x, y);
			}
			if (MathTools.distanceCalc(lastPathPoint, curPathPont) > 20) {
				lastPathPoint.set(curPathPont);
				pathPonts.add(new Vector2(curPathPont.x, curPathPont.y));
			}
			if (null != this.pathPonts && this.pathPonts.size() > 0) {
				for (int i = 0; i < this.pathPonts.size(); i++) {
					spriteBatch.draw(point, this.pathPonts.get(i).x,
							this.pathPonts.get(i).y);
				}
			}
		}

	}

	public String getBodyName() {
		return bodyName;
	}

}
