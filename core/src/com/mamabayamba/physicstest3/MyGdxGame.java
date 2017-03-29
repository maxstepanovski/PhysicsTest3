package com.mamabayamba.physicstest3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	public static final float X_METERS = 40;
	public static float yMeters;
	public static float pixelsInMeters;
	public static float w, h;
	ObjectFactory factory;
	GameLogicManager gameLogicManager;
	OrthographicCamera camera;
	SpriteBatch batch;
	Box2DDebugRenderer renderer;
	World world;
	Stage stage;
	TextButton resetButton;

	private BitmapFont arialFont;
	private GlyphLayout layout;

	@Override
	public void create () {
		Box2D.init();
		batch = new SpriteBatch();
		world = new World(new Vector2(0, -10), true);
		world.setContactListener(new MyContactListener());
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

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		resetButton = factory.createButton("Reset", w-100, h-50);
		resetButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameLogicManager.endGame();
				gameLogicManager.newGame();
			}
		});
		stage.addActor(resetButton);
		arialFont = new BitmapFont(Gdx.files.internal("arial.fnt"));
		Label label = new Label("LOL", new Label.LabelStyle(arialFont, Color.CYAN));
		stage.addActor(label);
	}

	@Override
	public void render () {
		if(Gdx.input.isTouched() && !resetButton.isPressed()){
			gameLogicManager.handleInput();
		}
		gameLogicManager.checkBoarders();
		gameLogicManager.checkPlacedBlocks();
		gameLogicManager.spawnNewBlockIfNeeded();
		gameLogicManager.updateCamera();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		stage.draw();
		renderer.render(world, camera.combined);

		batch.end();
		world.step(1/60f, 6, 2);

		if(gameLogicManager.isNewGameNeeded()){
			gameLogicManager.endGame();
			gameLogicManager.newGame();
			gameLogicManager.setNewGameNeeded(false);
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public class MyContactListener implements ContactListener{

		@Override
		public void beginContact(Contact contact) {
			gameLogicManager.checkIfNewGameNeeded(contact);
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
}
