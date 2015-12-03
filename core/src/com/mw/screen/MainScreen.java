package com.mw.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.mw.base.BaseScreen;
import com.mw.game.MainGame;
import com.mw.utils.CameraController;
import com.mw.utils.OrthoCamController;

import java.util.Random;

public class MainScreen extends BaseScreen implements Screen{
	private Stage stage = new Stage();
	private ImageButton ib_back;
	private TextureAtlas atlas;
	private static final float BACKPADDING = 10f;

	private static final int LAYERS = 5;
	private static final int BLOCK_TILES = 25;
	private static final int WIDTH = 15;
	private static final int HEIGHT = 10;
	private static final int TILES_PER_LAYER = WIDTH * HEIGHT;
	private SpriteCache[] caches = new SpriteCache[LAYERS];
	private Texture texture;
	private int[] layers = new int[LAYERS];
	private OrthographicCamera cam;
	private OrthoCamController camController;
	CameraController controller;
	GestureDetector gestureDetector;
	private long startTime = TimeUtils.nanoTime();

	public MainScreen(MainGame mainGame) {
		super(mainGame);

		cam = new OrthographicCamera(480, 320);
		cam.position.set(WIDTH * 32 / 2, HEIGHT * 32 / 2, 0);
		camController = new OrthoCamController(cam);
		controller = new CameraController(cam);
		gestureDetector = new GestureDetector(20, 0.5f, 2, 0.15f, controller);

		texture = new Texture(Gdx.files.internal("tiles.png"));

		Random rand = new Random();
		for (int i = 0; i < LAYERS; i++) {
			caches[i] = new SpriteCache();
			SpriteCache cache = caches[i];
			cache.beginCache();
			for (int y = 0; y < HEIGHT; y++) {
				for (int x = 0; x < WIDTH; x++) {
					int tileX = rand.nextInt(5);
					int tileY = rand.nextInt(5);
					cache.add(texture, x << 5, y << 5, 1 + tileX * 33, 1 + tileY * 33, 32, 32);
				}
			}
			layers[i] = cache.endCache();
		}

	}

	@Override
	public void show() {
		atlas = new TextureAtlas(Gdx.files.internal("images/buttons.pack"));
		TextureRegionDrawable imageUp = new TextureRegionDrawable(atlas.findRegion("button_back_normal"));
		TextureRegionDrawable imageDown = new TextureRegionDrawable(atlas.findRegion("button_back_pressed"));
		ib_back = new ImageButton(imageUp, imageDown);
		ib_back.setWidth(Gdx.graphics.getWidth()/10);
		ib_back.setHeight(Gdx.graphics.getHeight()/10);
		ib_back.setPosition(BACKPADDING, Gdx.graphics.getHeight()-BACKPADDING-ib_back.getWidth());
		stage = new Stage();
		ib_back.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				mainGame.setScreen(new TransferScreen(mainGame, new StartScreen(mainGame)));
				super.clicked(event, x, y);
			}

		});
		stage.addActor(ib_back);
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
//		inputMultiplexer.addProcessor(camController);
		inputMultiplexer.addProcessor(gestureDetector);
		Gdx.input.setInputProcessor(inputMultiplexer);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		controller.update();
		cam.update();


		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		for (int i = 0; i < LAYERS; i++) {
			SpriteCache cache = caches[i];
			cache.setProjectionMatrix(cam.combined);
			cache.begin();
			for (int j = 0; j < TILES_PER_LAYER; j += BLOCK_TILES) {
				cache.draw(layers[i], j, BLOCK_TILES);
			}
			cache.end();
		}

		if (TimeUtils.nanoTime() - startTime >= 1000000000) {
			Gdx.app.log("TileTest", "fps: " + Gdx.graphics.getFramesPerSecond());
			startTime = TimeUtils.nanoTime();
		}
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width,height,true);
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
