package com.android.ringfly.tools;

import java.util.ArrayList;
import java.util.List;

import com.android.ringfly.common.Assets;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 多边形（Box2d复杂外观）描点工具 1.必须是凸多边形 2.顶点个数必须大于等于3小于等于8 3.从描绘的第三个点开始
 * 每个点必须在之前一边的右侧（即必须逆时针描点）
 * 
 * @author fgshu
 * 
 */
public class VertexHelper implements ApplicationListener {
	private SpriteBatch batch;
	private boolean justTouched;
	private boolean isTouched;
	private boolean justReleased;
	private boolean wasTouched;
	public World world;
	private Box2DDebugRenderer renderer;
	private Vector2 pos;

	TextureRegion myRegion;

	private OrthographicCamera camera;
	private Vector2 firstPoint, lastPoint, curPoint;
	private boolean doOnce;
	private List<Mesh> lines;
	private List<Vector2> allPoint;
	private final static float PTM_RATIO = 20;
	public Sprite sprite;
	private float sizePercent = 1;

	@Override
	public void create() {
		Assets.load();
		pos = new Vector2(50, 50);
		lines = new ArrayList<Mesh>();
		camera = new OrthographicCamera(40, 24);
		camera.position.set(20, 12, 0);
		firstPoint = new Vector2();
		lastPoint = new Vector2();
		curPoint = new Vector2();
		allPoint = new ArrayList<Vector2>();
		doOnce = true;
		batch = new SpriteBatch();
		myRegion = Assets.popoRegions.get(0);
		world = new World(new Vector2(0, -9.8f), true);
		renderer = new Box2DDebugRenderer();
		sprite = new Sprite();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		GL10 gl = Gdx.app.getGraphics().getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		this.justTouched = Gdx.input.justTouched();
		this.isTouched = Gdx.input.isTouched();
		this.justReleased = wasTouched && !isTouched;
		wasTouched = isTouched;
		float timeStep = Gdx.graphics.getDeltaTime();
		world.step(timeStep, Assets.VELOCITYITERATIONS,
				Assets.VELOCITYITERATIONS);
		renderer.render(world, camera.combined);
		if (this.justReleased) {
			// start
			if (Gdx.input.getX() > 700 && 480 - Gdx.input.getY() > 430
					&& allPoint.size() > 2) {
				String strVertices = "Vector2[] vertices = {";

				Vector2[] vertices = new Vector2[allPoint.size()];
				for (int i = 0; i < allPoint.size(); i++) {
					vertices[i] = new Vector2(allPoint.get(i).x / PTM_RATIO,
							allPoint.get(i).y / PTM_RATIO);
					if (i == lines.size() - 1) {
						strVertices += String
								.format("new Vector2(%sf/Assets.PTM_RATIO, %sf/Assets.PTM_RATIO)",
										allPoint.get(i).x - pos.x,
										allPoint.get(i).y - pos.y);
					} else {
						strVertices += String
								.format("new Vector2(%sf/Assets.PTM_RATIO, %sf/Assets.PTM_RATIO),",
										allPoint.get(i).x - pos.x,
										allPoint.get(i).y - pos.y);
					}
				}
				strVertices += "};";
				System.out.println(strVertices);
				PolygonShape pp = new PolygonShape(); // 定义多边形
				BodyDef bd = new BodyDef();
				bd.type = BodyDef.BodyType.DynamicBody;
				bd.position.set(0, 0);
				pp.set(vertices);// 设置凸多边形的定点坐标
				Body testBody = world.createBody(bd);
				testBody.createFixture(pp, 1f);
				return;
			}
			// reset
			if (Gdx.input.getX() > 700 && 480 - Gdx.input.getY() < 50) {
				allPoint.clear();
				doOnce = true;
				lines.clear();
			}
			if (doOnce) {
				if (Gdx.input.getX() > 700 && 480 - Gdx.input.getY() < 50) {
					return;
				}
				if (Gdx.input.getX() > 700 && 480 - Gdx.input.getY() > 430) {
					return;
				}
				firstPoint.set(Gdx.input.getX(), Gdx.graphics.getHeight()
						- Gdx.input.getY());
				lastPoint.set(firstPoint.x, firstPoint.y);
				doOnce = false;
			} else {
				lastPoint.set(curPoint.x, curPoint.y);
			}
			curPoint.set(Gdx.input.getX(),
					Gdx.graphics.getHeight() - Gdx.input.getY());
			allPoint.add(new Vector2(curPoint.x, curPoint.y));
			if (lastPoint.x != curPoint.x && lastPoint.y != curPoint.y) {
				Mesh lineMesh = new Mesh(true, 4, 4, new VertexAttribute(
						Usage.Position, 3, "line_position"));
				lineMesh.setVertices(new float[] { lastPoint.x / PTM_RATIO,
						lastPoint.y / PTM_RATIO, 0, curPoint.x / PTM_RATIO,
						curPoint.y / PTM_RATIO, 0 });
				lineMesh.setIndices(new short[] { 0, 1 });

				Mesh lineMesh2 = new Mesh(true, 4, 4, new VertexAttribute(
						Usage.Position, 3, "line_position"));
				lineMesh2.setVertices(new float[] { firstPoint.x / PTM_RATIO,
						firstPoint.y / PTM_RATIO, 0, curPoint.x / PTM_RATIO,
						curPoint.y / PTM_RATIO, 0 });
				lineMesh2.setIndices(new short[] { 0, 1 });
				if (lines.size() != 0) {
					lines.remove(lines.size() - 1);
				}
				lines.add(lineMesh);
				lines.add(lineMesh2);
			}
		}
		camera.update();
		camera.apply(gl);
		for (int i = 0; i < lines.size(); i++) {
			lines.get(i).render(GL11.GL_LINES, 0, 2);
		}

		sprite.setBounds(pos.x, pos.y, myRegion.getRegionWidth() * sizePercent,
				myRegion.getRegionHeight() * sizePercent);
		sprite.setRegion(myRegion);
		batch.begin();
		sprite.draw(batch);
		// batch.draw(myRegion, pos.x, pos.y);
		Assets.huaWenXiHeiFont
				.drawMultiLine(
						batch,
						"vertices nums should >=3 and <=8\n the point must be right to an edge\n from the third one",
						10, 480);
		Assets.huaWenXiHeiFont.draw(batch, "START", 700, 480);
		Assets.huaWenXiHeiFont.draw(batch, "RESET", 700, 50);
		batch.end();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
