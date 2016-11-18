package com.mw.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.mw.actor.LoadingImage;
import com.mw.base.BaseScreen;
import com.mw.game.MainGame;
import com.mw.map.MapGenerator;
import com.mw.profiles.GameFileHelper;
import com.mw.ui.GameInfoLabel;
import com.mw.ui.LazyBitmapFont;
import com.mw.utils.AssetManagerHelper;

public class TransferScreen extends BaseScreen implements Screen {
	private LoadingImage image_loading;
	private TextureAtlas atlas;
	private Stage stage;
	private static final float DURATION = 0.1f;
	private Timer timer = new Timer();
	private int type = 0;

	private FreeTypeFontGenerator generator;
	private LazyBitmapFont bitmapFont;
	private GameInfoLabel infoLabel;
	private String msg = "Why did you hide that Tarot Deck in your music room?";
	private boolean isMapFinished = false;

	public TransferScreen(MainGame mainGame) {
		super(mainGame);
	}

	public TransferScreen(MainGame mainGame, int type) {
		super(mainGame);
		this.type = type;

		generator = new FreeTypeFontGenerator(Gdx.files.internal("data/font.ttf"));
		bitmapFont = new LazyBitmapFont(generator,24);
		infoLabel = new GameInfoLabel("",new Label.LabelStyle(bitmapFont, Color.WHITE));
		atlas = new TextureAtlas(
				Gdx.files.internal("images/settingimages.pack"));
		TextureRegion textureRegion = atlas.findRegion("loading");
//		textureRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		image_loading = new LoadingImage(textureRegion);
		image_loading.initLoadingAction();
		image_loading.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		stage = new Stage();
		stage.addActor(image_loading);
		stage.addActor(infoLabel);
		isMapFinished = true;
		//起线程去创建地图//在pc端报错No OpenGL context found in the current thread.
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				MapGenerator.getInstance().getTmxAreaMap(GameFileHelper.getInstance().getCurrentAreaName());
//				isMapFinished = true;
//			}
//		}).start();
	}


	@Override
	public void show() {

//		timer.scheduleTask(new Timer.Task() {
//
//			@Override
//			public void run() {
//				if(baseScreen != null){
//					mainGame.setScreen(baseScreen);
//				}
//			}
//		}, DURATION);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		infoLabel.setText(msg);
		infoLabel.setPosition(Gdx.graphics.getWidth()/2-infoLabel.getGlyphLayout().width/2,Gdx.graphics.getHeight()/2-infoLabel.getGlyphLayout().height-50);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		AssetManager assetManager = AssetManagerHelper.getInstance().getAssetManager();
		if(assetManager.update()){
			if(type == 1&&isMapFinished){
				//这里停顿0.01s防止读取太快
				stage.addAction(Actions.delay(0.01f,Actions.run(new Runnable() {
					@Override
					public void run() {
						MainScreen mainScreen = new MainScreen(mainGame);
						mainGame.setScreen(mainScreen);
					}
				})));

			}
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width,height,true);

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
	public void dispose() {
		atlas.dispose();
		stage.dispose();
		generator.dispose();
		bitmapFont.dispose();
	}

}
