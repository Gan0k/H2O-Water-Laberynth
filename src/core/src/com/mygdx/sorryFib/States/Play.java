package com.mygdx.sorryFib;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Play extends GameState {

	private World world;
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera b2dCam;


	public Play(GameStateManager gsm) {
		super(gsm);

		world = new World(new Vector2(0, -9.81f), true);
		b2dr = new Box2DDebugRenderer();
	
		containerBox();

		water();

		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / B2DVars.PPM, Game.V_HEIGHT / B2DVars.PPM);
	}

	public void handleInput() {

	}

	public void update(float dt) {
		world.step(dt,6,2,0);
	}

	public void render() {

		// clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// draw box2d world
		b2dr.render(world, b2dCam.combined);
		
	}

	public void dispose() {

	}

	private void containerBox() {
		// Up
		BodyDef bdef = new BodyDef();
		bdef.position.set(160/B2DVars.PPM, 180/B2DVars.PPM);
		bdef.type = BodyType.StaticBody;
		Body body = world.createBody(bdef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(100/B2DVars.PPM, 5/B2DVars.PPM);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);

		// Down
		bdef = new BodyDef();
		bdef.position.set(160/B2DVars.PPM, 80/B2DVars.PPM);
		bdef.type = BodyType.StaticBody;
		body = world.createBody(bdef);

		fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);

		// Left
		bdef = new BodyDef();
		bdef.position.set(60/B2DVars.PPM, 130/B2DVars.PPM);
		bdef.type = BodyType.StaticBody;
		body = world.createBody(bdef);

		fdef = new FixtureDef();
		fdef.shape = shape;
		shape.setAsBox(5/B2DVars.PPM, 50/B2DVars.PPM);
		body.createFixture(fdef);

		// Right
		bdef = new BodyDef();
		bdef.position.set(260/B2DVars.PPM, 130/B2DVars.PPM);
		bdef.type = BodyType.StaticBody;
		body = world.createBody(bdef);

		fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);
	}

	private void water() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(160 / B2DVars.PPM, 120 / B2DVars.PPM);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);

		CircleShape shape = new CircleShape();
		shape.setRadius(5.0f / B2DVars.PPM);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 2.5f;
		fdef.friction = 0.25f;
		fdef.restitution = 0.75f;
		body.createFixture(fdef);
		shape.dispose();
	}

}