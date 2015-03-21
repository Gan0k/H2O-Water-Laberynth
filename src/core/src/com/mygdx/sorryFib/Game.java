package com.mygdx.sorryFib;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Game extends ApplicationAdapter {

	public static final String TITLE = "sorryFib";
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 240;
	public static final int SCALE = 2;

	public static Content res;

	private float  accum;

	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;

	private GameStateManager gsm; 
	
	@Override
	public void create () {

		Gdx.input.setInputProcessor(new InputProcessor());

		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

		res = new Content();
		res.loadTexture("images/menu.png");
		res.loadTexture("images/hud.png");
		res.loadTexture("images/bgs.png");
		
		gsm = new GameStateManager(this);
	}

	@Override
	public void render () {
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
	}

	public void dispose() {}
	public void resize(int w, int h) {}
	public void pause() {}
	public void resume() {}

	public SpriteBatch getSpriteBatch() { return sb; }
	public OrthographicCamera getCamera() { return cam; }
	public OrthographicCamera getHUDCamera() { return hudCam; }
}
