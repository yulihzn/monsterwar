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
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.mw.actor.CamImage;
import com.mw.actor.Player;
import com.mw.actor.TestMap;
import com.mw.actor.TiledMapActor;
import com.mw.base.BaseScreen;
import com.mw.game.MainGame;
import com.mw.stage.MapStage;
import com.mw.utils.CameraController;
import com.mw.utils.Dungeon;
import com.mw.utils.OrthoCamController;

import java.util.Random;

public class MainScreen extends BaseScreen implements Screen{
	private MapStage mapStage;
	private Stage uiStage;
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
	private OrthographicCamera cam2;
	private OrthographicCamera cam3;
	private OrthoCamController camController;
	CameraController controller;
	GestureDetector gestureDetector;
	private long startTime = TimeUtils.nanoTime();

	private CamImage map02;
	private float backgroundScale = 1.0f;

	private TiledMapActor map01;
	private TestMap map03;
	private Player man;
	private TextureAtlas textureAtlas;
	private RandomXS128 randomXS128 = new RandomXS128();

	private float viewportWidth = 800;
	private float viewportHeight = 600;

	public MainScreen(MainGame mainGame) {
		super(mainGame);

		cam = new OrthographicCamera(viewportWidth, viewportHeight);
		cam.position.set(viewportWidth/2, viewportHeight/2, 0);
		camController = new OrthoCamController(cam);
		controller = new CameraController(cam);
		gestureDetector = new GestureDetector(20, 0.5f, 2, 0.15f, controller);
		textureAtlas = new TextureAtlas(Gdx.files.internal("tiles.pack"));

		map01 = new TiledMapActor(cam);
		map02 = new CamImage(new Texture(Gdx.files.internal("images/map02.jpg")),cam);
		map03 = new TestMap(cam);

		controller.setOnTouchListener(new CameraController.OnTouchListener() {
			@Override
			public void onTap(float x, float y) {
				elementTouch("man",x,y);
			}
		});

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
		uiStage = new Stage();
		mapStage = new MapStage(cam);
		ib_back.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				mainGame.setScreen(new TransferScreen(mainGame, new StartScreen(mainGame)));
				super.clicked(event, x, y);
			}

		});
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(mapStage);
		inputMultiplexer.addProcessor(uiStage);
		inputMultiplexer.addProcessor(gestureDetector);
		Gdx.input.setInputProcessor(inputMultiplexer);

		uiStage.addActor(ib_back);

		mapStage.addActor(map03);
		map03.setZIndex(1);
		map03.setDungeon();
		man = new Player(textureAtlas.findRegion("man"),cam);
		map03.setCreaturePos("man",1,1);
		man.setPosition(map03.getCreaturePos("man").x,map03.getCreaturePos("man").y);
		mapStage.addActor(man);
		man.setZIndex(2);
//		man.addListener(new ClickListener(){
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//				super.clicked(event, x, y);
//				map03.setCreaturePos("man",randomXS128.nextInt(TestMap.xsize),randomXS128.nextInt(TestMap.ysize));
//				man.setPosition(map03.getCreaturePos("man").x,map03.getCreaturePos("man").y);
//			}
//
//		});

	}
	private void elementTouch(String name,float x, float y) {
		boolean isTouched = false;
		float xx = (x+cam.position.x - viewportWidth/2)*cam.zoom;
		float yy = ((viewportHeight-y)+cam.position.y - viewportHeight/2)*cam.zoom;
		if(map03.getCreaturePos(name).x <= xx && map03.getCreaturePos(name).x + 32> xx
				&&map03.getCreaturePos(name).y <= yy && map03.getCreaturePos(name).y + 32> yy){
			isTouched = true;
		}
		Gdx.app.log("elementTouch","cam.position.x="+cam.position.x+"cam.position.y="+cam.position.y);
		Gdx.app.log("elementTouch","cam.zoom="+cam.zoom);
		Gdx.app.log("elementTouch","x="+x+"y="+(viewportHeight-y));
		Gdx.app.log("elementTouch","xx="+xx+"yy="+yy);
		Gdx.app.log("elementTouch","get.x="+map03.getCreaturePos(name).x+"get.y="+map03.getCreaturePos(name).y);
		if(isTouched){
			Gdx.app.log("elementTouch",name);
			map03.setCreaturePos("man",randomXS128.nextInt(TestMap.xsize),randomXS128.nextInt(TestMap.ysize));
			man.setPosition(map03.getCreaturePos("man").x,map03.getCreaturePos("man").y);
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//		controller.update();
		cam.update();
//		if(Math.abs(cam.zoom)>=5 && Math.abs(cam.zoom)<10){
//			map01.setVisible(false);
//			map02.setVisible(true);
//			map03.setVisible(false);
//		}else if(Math.abs(cam.zoom)>=10){
//			map01.setVisible(false);
//			map02.setVisible(false);
//			map03.setVisible(true);
//		}else if(Math.abs(cam.zoom)<5){
//			map01.setVisible(true);
//			map02.setVisible(false);
//			map03.setVisible(false);
//		}

		if (TimeUtils.nanoTime() - startTime >= 1000000000) {
//			Gdx.app.log("TileTest", "fps: " + Gdx.graphics.getFramesPerSecond());
			startTime = TimeUtils.nanoTime();
//			Gdx.app.log("cam.position", "x=" + cam.position.x + "y="+cam.position.y);
		}
		mapStage.act(Gdx.graphics.getDeltaTime());
		mapStage.draw();
		uiStage.act(Gdx.graphics.getDeltaTime());
		uiStage.draw();


		
	}

	@Override
	public void resize(int width, int height) {
		mapStage.getViewport().update(width,height,true);
		uiStage.getViewport().update(width,height,true);
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
