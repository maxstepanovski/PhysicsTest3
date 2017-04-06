package com.mamabayamba.physicstest3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class MyGdxGame extends ApplicationAdapter {
	public static final float X_METERS = 40;
	public static float yMeters;
	public static float pixelsInMeters;
	public static float w, h;
	public static OrthographicCamera camera;
	ObjectFactory factory;
	GameLogicManager gameLogicManager;
	SpriteBatch batch;
	Box2DDebugRenderer renderer;
	World world;
	ParticleEffect pe;

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
		factory = new ObjectFactory(world);
		gameLogicManager = new GameLogicManager(world, camera);
		gameLogicManager.newGame();
		pe = new ParticleEffect();
		pe.load(Gdx.files.internal("fire.party"), Gdx.files.internal(""));
		pe.setPosition(0,0);
		pe.start();
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

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		////
		batch.begin();

		gameLogicManager.updateTextures();
		gameLogicManager.updateHUD();
		gameLogicManager.drawVisuals();
		pe.draw(batch, 1/60f);

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
		if(pe.isComplete()){
			pe.reset();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
