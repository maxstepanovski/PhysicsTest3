package com.mamabayamba.physicstest3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class MyGdxGame extends ApplicationAdapter implements ContactListener {
	public static final float X_METERS = 40;
	public static float yMeters;
	public static float pixelsInMeters;
	public static float w, h;
	public static OrthographicCamera camera;
	ObjectFactory factory;
	GameLogicManager gameLogicManager;
    EffectManager effectManager;
	SpriteBatch batch;
	Box2DDebugRenderer renderer;
	World world;

	@Override
	public void create () {
		Box2D.init();
		batch = new SpriteBatch();
		world = new World(new Vector2(0, -10), true);
		renderer = new Box2DDebugRenderer();
		//
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		pixelsInMeters = w / X_METERS;
		yMeters = h / pixelsInMeters;
		Gdx.app.log("happy: ", X_METERS+"; "+yMeters);
		camera = new OrthographicCamera(w/ pixelsInMeters, (h/ pixelsInMeters));
		camera.position.set(camera.viewportWidth/2.0f, camera.viewportHeight/2.0f, 0);
		camera.update();
		factory = new ObjectFactory(world, camera);
		gameLogicManager = new GameLogicManager(world, camera);
        effectManager = new EffectManager();
        world.setContactListener(this);
		gameLogicManager.newGame();

	}

	@Override
	public void render () {
		if(Gdx.input.isTouched()){
			gameLogicManager.handleInput();
		}
		gameLogicManager.checkBoarders();
		gameLogicManager.checkIfAllBlocksPlaced();
		gameLogicManager.checkIfNewBlockNeeded();
		gameLogicManager.updateCamera();

		Gdx.gl.glClearColor(0,0,0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		////
		batch.begin();

        gameLogicManager.drawTextures(batch);
        effectManager.drawEffects(batch);

		batch.end();
		////
		world.step(1/60f, 6, 2);
		gameLogicManager.checkIfNewGameNeeded();
		if(gameLogicManager.isNewGameNeeded()){
			gameLogicManager.endGame();
			gameLogicManager.newGame();
			gameLogicManager.setNewGameNeeded(false);
		}
//		renderer.render(world, camera.combined);


	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
	
    @Override
    public void beginContact(Contact contact) {
        effectManager.addDustEffectToPool(contact, camera);
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
