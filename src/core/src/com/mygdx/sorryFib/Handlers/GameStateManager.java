package com.mygdx.sorryFib;

import java.util.Stack;

public class GameStateManager {

	private Game game;

	private Stack<GameState> gameStates;

	public static final int MENU = 0;
	public static final int LEVEL_SELECT = 2;
	public static final int PLAY = 1;

	public GameStateManager(Game game) {
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(MENU);
	}

	public Game game() { return game; }

	public void update(float dt) {
		gameStates.peek().update(dt);
	}

	public void render() {
		gameStates.peek().render();
	}

	public void setState(int state) {
		popState();
		pushState(state);
	}

	public void pushState(int state) {
		gameStates.push(getState(state));
	}

	public void popState() {
		GameState g = gameStates.pop();
		g.dispose();
	}

	public Game getGame() { return game; }

	private GameState getState(int state) {
		if (state == PLAY) return new Play(this);
		if (state == MENU) return new Menu(this);
		if (state == LEVEL_SELECT) return new LevelSelect(this);
		return null;
	}

}