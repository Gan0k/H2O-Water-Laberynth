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
	public static boolean[] levels;

	private float  accum;

	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;

	private GameStateManager gsm; 

	static {
		levels = new boolean[10];
		for (int i = 0; i < 10; i++) levels[i] = false;
		levels[1] = levels[0] = true;
	}
	
	@Override
	public void create () {

		InputProcessor ip = new InputProcessor();
		Gdx.input.setInputProcessor(ip);
		Gdx.input.setCatchBackKey(true);

		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

		res = new Content();
		res.loadTexture("images/menu.png");
		res.loadTexture("images/smile.png");
		res.loadTexture("images/logo.png");
		res.loadTexture("images/hud.png");
		res.loadTexture("images/bgs.png");
		res.loadTexture("images/crystal.png");
		
		gsm = new GameStateManager(this);
		ip.setGSM(gsm);
	}

	@Override
	public void render () {
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
	}

	public void updateLevel() {
		gsm.setState(GameStateManager.LEVEL_SELECT);
	}

	public void dispose() {}
	public void resize(int w, int h) {}
	public void pause() {}
	public void resume() {}

	public SpriteBatch getSpriteBatch() { return sb; }
	public OrthographicCamera getCamera() { return cam; }
	public OrthographicCamera getHUDCamera() { return hudCam; }
}
