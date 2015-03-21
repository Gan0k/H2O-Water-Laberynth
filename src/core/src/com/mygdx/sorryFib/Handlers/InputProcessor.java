package com.mygdx.sorryFib;

import com.badlogic.gdx.InputAdapter;

public class InputProcessor extends InputAdapter {
	
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
}