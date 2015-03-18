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
import com.badlogic.gdx.graphics.Color;

import finnstr.libgdx.liquidfun.ParticleDebugRenderer;
import finnstr.libgdx.liquidfun.ParticleDef.ParticleType;
import finnstr.libgdx.liquidfun.ParticleGroupDef;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

public class Play extends GameState {

	private World world;
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera b2dCam;

	private ParticleSystem mParticleSystem;
    private ParticleDebugRenderer mParticleDebugRenderer;

    private ParticleGroupDef mParticleGroupDef1;
    private ParticleGroupDef mParticleGroupDef2;

	public Play(GameStateManager gsm) {
		super(gsm);

		world = new World(new Vector2(0, -9.81f), true);
		createParticleStuff(Game.V_WIDTH / B2DVars.PPM, Game.V_HEIGHT / B2DVars.PPM);
	
		b2dr = new Box2DDebugRenderer();
		mParticleDebugRenderer = new ParticleDebugRenderer(new Color(0, 0, 1, 1), mParticleSystem.getParticleCount());

		containerBox();
	
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / B2DVars.PPM, Game.V_HEIGHT / B2DVars.PPM);
	}


	public void handleInput() {

	}

	public void update(float dt) {
		world.step(dt,6,2,0); // TODO: Add correct third parameter
	}

	public void render() {
        //First update our InputProcessor
        //this.inputUpdate(Gdx.graphics.getDeltaTime()); TODO: Create imput processor

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(Gdx.graphics.getDeltaTime(), 10, 6, mParticleSystem.calculateReasonableParticleIterations(Gdx.graphics.getDeltaTime()));

        //First render the particles and then the Box2D world
        mParticleDebugRenderer.render(mParticleSystem, B2DVars.PPM, b2dCam.combined);
        b2dr.render(world, b2dCam.combined);
    }

	public void dispose() {
        world.dispose();
        b2dr.dispose();
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

	private void createParticleStuff(float width, float height) {
        //First we create a new particlesystem and 
        //set the radius of each particle to 5 / 100 m (5 cm)
        ParticleSystemDef systemDef = new ParticleSystemDef();
        systemDef.radius = 1f / B2DVars.PPM;
        systemDef.dampingStrength = 0.2f;

        mParticleSystem = new ParticleSystem(world, systemDef);
        mParticleSystem.setParticleDensity(1.3f);

        //Create a new particlegroupdefinition and set some properties
        //For the flags you can set more than only one
        mParticleGroupDef1 = new ParticleGroupDef();
        mParticleGroupDef1.color.set(1f, 0, 0, 1);
        mParticleGroupDef1.flags.add(ParticleType.b2_waterParticle);
        mParticleGroupDef1.position.set(width / 2, height / 2);

        //Create a shape, give it to the definition and
        //create the particlegroup in the particlesystem.
        //This will return you a ParticleGroup instance, but
        //we don't need it here, so we drop that.
        //The shape defines where the particles are created exactly
        //and how much are created
        PolygonShape parShape = new PolygonShape();
        parShape.setAsBox(width * (20f / 100f) / 2f, width * (20f / 100f) / 2f);
        mParticleGroupDef1.shape = parShape;
        mParticleSystem.createParticleGroup(mParticleGroupDef1);

        //Here we create a new shape and we set a
        //linear velocity. This is used in createParticles1()
        //and createParticles2()
        CircleShape partShape = new CircleShape();
        partShape.setRadius(18.5f / B2DVars.PPM);
        mParticleGroupDef1.shape = partShape;
        mParticleGroupDef1.linearVelocity.set(new Vector2(0, -10f));
    }

}