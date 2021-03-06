package com.mygdx.sorryFib;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class Win extends GameState {

	private BitmapFont font = new BitmapFont();
	private Texture logo;
	
	public Win(GameStateManager gsm) {
		super(gsm);
		logo = Game.res.getTexture("smile");
	}
	
	public void handleInput() {}
	
	public void update(float dt) {}
	
	public void render() {

		Gdx.gl.glClearColor(0f,0f,0f,0.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		font.draw(sb, "You beat the level!", 100, 80);
        sb.draw(logo, 110, 90);

		sb.end();
	}
	
	public void dispose() {}

}