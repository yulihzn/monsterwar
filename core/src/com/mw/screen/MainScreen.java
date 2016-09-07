package com.mw.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.mw.base.BaseScreen;
import com.mw.game.MainGame;
import com.mw.map.DungeonMap;
import com.mw.stage.MapStage;
import com.mw.stage.UiStage;
import com.mw.utils.CameraController;
import com.mw.utils.GameInputMultiplexer;

public class MainScreen extends BaseScreen implements Screen{
	private MapStage mapStage;
	private UiStage uiStage;

	private OrthographicCamera cam;
	CameraController controller;
	GestureDetector gestureDetector;
	private long startTime = TimeUtils.nanoTime();

	private float worldWidth = DungeonMap.TILE_SIZE*32;
	private float worldHeight = DungeonMap.TILE_SIZE*32;
	private float camSize = DungeonMap.TILE_SIZE*32;

	private GameInputMultiplexer inputMultiplexer;

	public GameInputMultiplexer getInputMultiplexer() {
		return inputMultiplexer;
	}

	private MainGame mainGame;

	public MainGame getMainGame() {
		return mainGame;
	}

	public MainScreen(MainGame mainGame) {
		super(mainGame);
		this.mainGame = mainGame;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(camSize, camSize*(h/w));
		cam.position.set(worldWidth / 2f, worldHeight / 2f, 0);
		cam.zoom = 1.5f;
		cam.update();
		controller = new CameraController(cam);
		gestureDetector = new GestureDetector(controller);
		controller.setOnTouchListener(new CameraController.OnTouchListener() {
			@Override
			public void onTap(float x, float y) {
				elementTouch("man",x,y);
			}
		});
		inputMultiplexer = new GameInputMultiplexer();
	}

	@Override
	public void show() {

		uiStage = new UiStage(this);
		mapStage = new MapStage(cam,this);
		mapStage.setDebugUnderMouse(true);
		inputMultiplexer.addProcessor(uiStage);
		inputMultiplexer.addProcessor(gestureDetector);
		inputMultiplexer.addProcessor(mapStage);
		Gdx.input.setInputProcessor(inputMultiplexer);

	}
	private void elementTouch(String name,float x, float y) {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		Gdx.gl.glEnable(GL20.GL_BLEND);
//		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		if (TimeUtils.nanoTime() - startTime >= 1000000000) {
//			Gdx.app.log("TileTest", "fps: " + Gdx.graphics.getFramesPerSecond());
			startTime = TimeUtils.nanoTime();
		}

		cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, worldWidth/cam.viewportWidth*2);
//		float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
//		float effectiveViewportHeight = cam.viewportHeight * cam.zoom;
//		cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, worldWidth - effectiveViewportWidth / 2f);
//		cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, worldtHeight - effectiveViewportHeight / 2f);
		cam.position.x = MathUtils.clamp(cam.position.x, 0, worldWidth);
		cam.position.y = MathUtils.clamp(cam.position.y, 0, worldHeight);
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		cam.viewportWidth = camSize;
		cam.viewportHeight = camSize*(h/w);
		cam.update();

		mapStage.act(Gdx.graphics.getDeltaTime());
		mapStage.draw();
		uiStage.act(Gdx.graphics.getDeltaTime());
		uiStage.draw();
	}



	@Override
	public void resize(int width, int height) {
		int ratio = height;
		if(width > height){
			ratio = width;
		}
//		cam.viewportWidth = camSize;
//		cam.viewportHeight = camSize * height/width;

		cam.update();
		mapStage.getViewport().update(width,height,true);
		uiStage.getViewport().update(width,height,true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose(){
		mapStage.dispose();
		uiStage.dispose();
	}

}
