package com.mygdx.sorryFib;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LevelSelect extends GameState {
	
	private TextureRegion reg;
	
	private GameButton[][] buttons;
	
	public LevelSelect(GameStateManager gsm) {
		
		super(gsm);
		
		TextureRegion buttonReg = new TextureRegion(Game.res.getTexture("hud"), 0, 0, 32, 32);
		buttons = new GameButton[2][5];
		for(int row = 0; row < buttons.length; row++) {
			for(int col = 0; col < buttons[0].length; col++) {
				buttons[row][col] = new GameButton(buttonReg, Game.V_WIDTH/7 + col * Game.V_WIDTH/6,  Game.V_HEIGHT*4/6 + -row * Game.V_HEIGHT/3, 
					cam, 1.5f*buttonReg.getRegionWidth(), 1.5f*buttonReg.getRegionHeight());
				buttons[row][col].setText(row * buttons[0].length + col + 1 + "");
			}
		}
		
		cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);
		
	}
	
	public void handleInput() {
	}
	
	public void update(float dt) {
		
		handleInput();
		
		for(int row = 0; row < buttons.length; row++) {
			for(int col = 0; col < buttons[0].length; col++) {
				buttons[row][col].update(dt);
				if(buttons[row][col].isClicked()) {
					Play.level = row * buttons[0].length + col + 1;
					//Game.res.getSound("levelselect").play();
					gsm.setState(GameStateManager.PLAY);
				}
			}
		}
		
	}
	
	public void render() {
		
		// clean screen
		Gdx.gl.glClearColor(0f,0f,0f,0.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sb.setProjectionMatrix(cam.combined);
		
		for(int row = 0; row < buttons.length; row++) {
			for(int col = 0; col < buttons[0].length; col++) {
				buttons[row][col].render2(sb);
			}
		}
		
	}
	
	public void dispose() {
		// everything is in the resource manager com.neet.blockbunny.handlers.Content
	}
	
}