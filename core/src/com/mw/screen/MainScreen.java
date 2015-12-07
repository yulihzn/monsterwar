package com.mw.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TideMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.mw.base.BaseScreen;
import com.mw.game.MainGame;
import com.mw.stage.MapStage;
import com.mw.utils.CameraController;
import com.mw.utils.OrthoCamController;

import java.util.Random;

public class MainScreen extends BaseScreen implements Screen{
	private MapStage stage;
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

	private Image map02;
	private float backgroundScale = 1.0f;

	private TiledMap map;
	private TiledMapRenderer renderer;
	private AssetManager assetManager;

	public MainScreen(MainGame mainGame) {
		super(mainGame);

		cam = new OrthographicCamera(480, 320);
		cam.position.set(WIDTH * 32 / 2, HEIGHT * 32 / 2, 0);
		camController = new OrthoCamController(cam);
		controller = new CameraController(cam);
		gestureDetector = new GestureDetector(20, 0.5f, 2, 0.15f, controller);

		texture = new Texture(Gdx.files.internal("tiles.png"));
		map02 = new Image(new Texture(Gdx.files.internal("images/map02.jpg")));
		backgroundScale = Gdx.graphics.getHeight()/map02.getHeight();
//		map02.setWidth(map02.getWidth() * backgroundScale);
//		map02.setHeight(Gdx.graphics.getHeight());
		map02.setPosition(Gdx.graphics.getWidth()/2-map02.getWidth()/2,0);

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

		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class,new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load("images/tiles.tmx",TiledMap.class);
		assetManager.finishLoading();
		map = assetManager.get("images/tiles.tmx");
		renderer = new OrthogonalTiledMapRenderer(map,1f/32f);

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
		stage = new MapStage(cam);
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
		stage.addActor(map02);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		controller.update();
		cam.update();
		if(Math.abs(cam.zoom)>5){
			map02.setVisible(true);
		}else{
			map02.setVisible(false);
		}



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

		renderer.setView(cam);
		renderer.render();

		
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
