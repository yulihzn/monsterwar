package com.mw.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.mw.actor.CamImage;
import com.mw.actor.TestMap;
import com.mw.base.BaseScreen;
import com.mw.game.MainGame;
import com.mw.stage.MapStage;
import com.mw.utils.CameraController;

import java.util.Random;

public class MainScreen extends BaseScreen implements Screen{
	private MapStage mapStage;
	private Stage uiStage;
	private ImageButton ib_back;
	private TextureAtlas atlas;
	private static final float BACKPADDING = 10f;

	private OrthographicCamera cam;
	CameraController controller;
	GestureDetector gestureDetector;
	private long startTime = TimeUtils.nanoTime();

	private float worldWidth = TestMap.xsize*32;
	private float worldtHeight = TestMap.ysize*32;
	private float camSize = 32*20;

	public MainScreen(MainGame mainGame) {
		super(mainGame);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(camSize, camSize*(w/h));
		cam.position.set(worldWidth / 2f, worldtHeight / 2f, 0);
		cam.update();
		controller = new CameraController(cam);
		gestureDetector = new GestureDetector(controller);
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
		ib_back.setSize(Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight()/10);
		ib_back.setBounds(BACKPADDING, Gdx.graphics.getHeight()-BACKPADDING-ib_back.getWidth()
		,Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight()/10);
		uiStage = new Stage();
		mapStage = new MapStage(cam);
//		mapStage.setDebugAll(true);
		ib_back.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				mainGame.setScreen(new TransferScreen(mainGame, new StartScreen(mainGame)));
				super.clicked(event, x, y);
			}

		});
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(gestureDetector);
		inputMultiplexer.addProcessor(uiStage);
		inputMultiplexer.addProcessor(mapStage);
		Gdx.input.setInputProcessor(inputMultiplexer);

		uiStage.addActor(ib_back);


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

		cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, worldWidth/cam.viewportWidth);
		float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
		float effectiveViewportHeight = cam.viewportHeight * cam.zoom;
		cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, worldWidth - effectiveViewportWidth / 2f);
		cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, worldWidth - effectiveViewportHeight / 2f);
		cam.update();

		mapStage.act(Gdx.graphics.getDeltaTime());
		mapStage.draw();
		uiStage.act(Gdx.graphics.getDeltaTime());
		uiStage.draw();
	}



	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = camSize;
		cam.viewportHeight = camSize * height/width;
		cam.update();
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
