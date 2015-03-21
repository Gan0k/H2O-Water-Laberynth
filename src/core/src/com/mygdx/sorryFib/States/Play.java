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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;

import finnstr.libgdx.liquidfun.ParticleDebugRenderer;
import finnstr.libgdx.liquidfun.ColorParticleRenderer;
import finnstr.libgdx.liquidfun.ParticleDef.ParticleType;
import finnstr.libgdx.liquidfun.ParticleGroupDef;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

public class Play extends GameState {

	public static int level = 1;

	private World world;
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera b2dCam;

	private TiledMap tileMap;
	private int tileMapWidth;
	private int tileMapHeight;
	private int tileSize;
	private OrthogonalTiledMapRenderer tmRenderer;
	private ParticleSystem mParticleSystem;
    private ParticleDebugRenderer mParticleDebugRenderer;
    private ColorParticleRenderer mColorParticleRenderer;

    private ParticleGroupDef mParticleGroupDef1;

	public Play(GameStateManager gsm) {
		super(gsm);

		world = new World(new Vector2(0, -9.81f), true);
		createParticleStuff(Game.V_WIDTH / B2DVars.PPM, Game.V_HEIGHT / B2DVars.PPM);
	
		b2dr = new Box2DDebugRenderer();
		mParticleDebugRenderer = new ParticleDebugRenderer(new Color(0, 0, 1, 1), mParticleSystem.getParticleCount());
		mColorParticleRenderer = new ColorParticleRenderer(mParticleSystem.getParticleCount());
	
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / B2DVars.PPM, Game.V_HEIGHT / B2DVars.PPM);

		// create walls
		createMargin();
		createWalls();
		//cam.setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);
	}


	public void handleInput() {

	}

	private void createMargin() {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.StaticBody;
		bdef.position.set(0,0);
		ChainShape cs = new ChainShape();
		Vector2[] v = new Vector2[2];
		v[0] = new Vector2(0,0);
		v[1] = new Vector2(0, Game.V_HEIGHT / B2DVars.PPM);
		cs.createChain(v);
		FixtureDef fd = new FixtureDef();
		fd.friction = 0;
		fd.shape = cs;
		world.createBody(bdef).createFixture(fd);
		v = new Vector2[2];
		v[0] = new Vector2(0,0);
		v[1] = new Vector2(Game.V_WIDTH / B2DVars.PPM,0);
		cs.createChain(v);
		fd = new FixtureDef();
		fd.friction = 0;
		fd.shape = cs;
		world.createBody(bdef).createFixture(fd);
		v = new Vector2[2];
		v[0] = new Vector2(Game.V_WIDTH / B2DVars.PPM,Game.V_HEIGHT / B2DVars.PPM);
		v[1] = new Vector2(Game.V_WIDTH / B2DVars.PPM,0);
		cs.createChain(v);
		fd = new FixtureDef();
		fd.friction = 0;
		fd.shape = cs;
		world.createBody(bdef).createFixture(fd);
		v = new Vector2[2];
		v[0] = new Vector2(0, Game.V_HEIGHT / B2DVars.PPM);
		v[1] = new Vector2(Game.V_WIDTH / B2DVars.PPM,Game.V_HEIGHT / B2DVars.PPM);
		cs.createChain(v);
		fd = new FixtureDef();
		fd.friction = 0;
		fd.shape = cs;
		world.createBody(bdef).createFixture(fd);
		cs.dispose();
	}

	private void createWalls() {
		
		// load tile map and map renderer
		try {
			tileMap = new TmxMapLoader().load("maps/level" + level + ".tmx");
		}
		catch(Exception e) {
			System.out.println("Cannot find file: maps/level" + level + ".tmx");
			Gdx.app.exit();
		}
		tileMapWidth = Integer.parseInt((tileMap.getProperties().get("width").toString()));
		tileMapHeight = Integer.parseInt((tileMap.getProperties().get("height").toString()));
		tileSize = Integer.parseInt((tileMap.getProperties().get("tilewidth").toString()));
		tmRenderer = new OrthogonalTiledMapRenderer(tileMap);
		
		// read each of the "red" "green" and "blue" layers
		TiledMapTileLayer layer;
		layer = (TiledMapTileLayer) tileMap.getLayers().get("layer");
		createBlocks(layer);
	}

	private void createBlocks(TiledMapTileLayer layer) {
		
		// tile size
		float ts = layer.getTileWidth();
		
		// go through all cells in layer
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				
				// get cell
				Cell cell = layer.getCell(col, row);
				
				// check that there is a cell
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				// create body from cell
				BodyDef bdef = new BodyDef();
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5f) * ts / B2DVars.PPM, (row + 0.5f) * ts / B2DVars.PPM);
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-ts / 2 / B2DVars.PPM, -ts / 2 / B2DVars.PPM);
				v[1] = new Vector2(-ts / 2 / B2DVars.PPM, ts / 2 / B2DVars.PPM);
				v[2] = new Vector2(ts / 2 / B2DVars.PPM, ts / 2 / B2DVars.PPM);
				v[3] = new Vector2(ts / 2 / B2DVars.PPM, -ts / 2 / B2DVars.PPM);
				v[4] = new Vector2(-ts / 2 / B2DVars.PPM, -ts / 2 / B2DVars.PPM);
				cs.createChain(v);
				FixtureDef fd = new FixtureDef();
				fd.friction = 0;
				fd.shape = cs;
				world.createBody(bdef).createFixture(fd);
				cs.dispose();
				
			}
		}
		
	}

	public void update(float dt) {
		float x = Gdx.input.getAccelerometerX();
		x *= 9.81/10;
		float y = Gdx.input.getAccelerometerY();
		y *= 9.81/10;
		world.setGravity(new Vector2(y,-x));
		world.step(Gdx.graphics.getDeltaTime(), 10, 6, mParticleSystem.calculateReasonableParticleIterations(Gdx.graphics.getDeltaTime()));
	}

	public void render() {
        //First update our InputProcessor
        //this.inputUpdate(Gdx.graphics.getDeltaTime()); TODO: Create imput processor

        Gdx.gl.glClearColor(0f,0f,0f,0.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //First render the particles and then the Box2D world
        //mParticleDebugRenderer.render(mParticleSystem, B2DVars.PPM, b2dCam.combined);
        mColorParticleRenderer.render(mParticleSystem, B2DVars.PPM, b2dCam.combined);
        //b2dr.render(world, b2dCam.combined);

        // draw map
        tmRenderer.setView(cam);
		tmRenderer.render();
    }

	public void dispose() {
        world.dispose();
        b2dr.dispose();
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
        mParticleGroupDef1.color.set(0.44313725490196076f, 0.6862745098039216f, 0.7294117647058823f, 0.9f);
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