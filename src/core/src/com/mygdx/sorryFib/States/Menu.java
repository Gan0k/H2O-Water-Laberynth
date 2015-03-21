package com.mygdx.sorryFib;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import finnstr.libgdx.liquidfun.ParticleDebugRenderer;
import finnstr.libgdx.liquidfun.ColorParticleRenderer;
import finnstr.libgdx.liquidfun.ParticleDef.ParticleType;
import finnstr.libgdx.liquidfun.ParticleGroupDef;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

public class Menu extends GameState {
	
	private GameButton playButton;
	
	private World world;
	
	public Menu(GameStateManager gsm) {
		
		super(gsm);
		
		Texture tex = Game.res.getTexture("menu");
		//bg = new Background(new TextureRegion(tex), cam, 1f);
		//bg.setVector(-20, 0);
		
		tex = Game.res.getTexture("hud");
		playButton = new GameButton(new TextureRegion(tex, 0, 34, 58, 27), 160, 100, cam);
		
		cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);
		
		world = new World(new Vector2(0, -9.8f), true);
		
	}
	
	public void handleInput() {
		
		// mouse/touch input
		if(playButton.isClicked()) {
			//Game.res.getSound("crystal").play();
			gsm.setState(GameStateManager.LEVEL_SELECT);
		}
		
	}
	
	public void update(float dt) {
		
		handleInput();
		
		world.step(Gdx.graphics.getDeltaTime(), 10, 6, 0);
		
		playButton.update(dt);
		
	}
	
	public void render() {
		
		// clean screen
		Gdx.gl.glClearColor(0f,0f,0f,0.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sb.setProjectionMatrix(cam.combined);
		
		// draw button
		playButton.render(sb);
				
	}
	
	public void dispose() {
		// everything is in the resource manager com.neet.blockbunny.handlers.Content
	}
	
}