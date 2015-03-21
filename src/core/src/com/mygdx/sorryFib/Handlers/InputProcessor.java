package com.mygdx.sorryFib;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class InputProcessor extends InputAdapter {

	private GameStateManager gsm;

	public InputProcessor(GameStateManager gsm) {
		this.gsm = gsm;
	}
	
	public boolean mouseMoved(int x, int y) {
		Input.x = x;
		Input.y = y;
		return true;
	}
	
	public boolean touchDragged(int x, int y, int pointer) {
		Input.x = x;
		Input.y = y;
		Input.down = true;
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer, int button) {
		Input.x = x;
		Input.y = y;
		Input.down = true;
		return true;
	}
	
	public boolean touchUp(int x, int y, int pointer, int button) {
		Input.x = x;
		Input.y = y;
		Input.down = false;
		return true;
	}

	public boolean keyDown(int keycode) {
        if(keycode == Keys.BACK){
           gsm.setState(GameStateManager.MENU);
        }
        return false;
   }
}