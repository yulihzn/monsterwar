package com.mw.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.mw.ui.actor.LoadingImage;
import com.mw.game.MainGame;
import com.mw.components.map.MapGenerator;
import com.mw.profiles.GameFileHelper;
import com.mw.ui.widget.GameInfoLabel;
import com.mw.ui.widget.LazyBitmapFont;
import com.mw.utils.AssetManagerHelper;

/**
 * 关卡加载过渡界面，用来处理耗时任务
 */
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
	private float time=0;
	private boolean isWorldFinished = false;

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
		MapGenerator.map().setOnMapGeneratorListener(new MapGenerator.OnMapGeneratorListener() {
			@Override
			public void begin(String msg, int type) {
			}
			@Override
			public void generating(String msg, int type) {
			}
			@Override
			public void finish(String msg, int type) {
				if (type==MapGenerator.WORLD){
					//世界创建完毕需要用ui线程加载世界（可以放进assetmanager里）
					// 初始化数据（可以在线程里跑，由于这里循环比较多，最好只执行一次）再加载地区
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							AssetManagerHelper.getInstance().loadTiledMap(MapGenerator.map().getTmxWorldMap().getName());

						}
					});
				}
			}
		});

	}


	@Override
	public void show() {
		time = 0;
		isMapFinished = false;
		isWorldFinished = false;

        //起线程去创建地图
		new Thread(new Runnable() {
			@Override
			public void run() {
				MapGenerator.map().initWorld();
			}
		}).start();

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		infoLabel.setText(msg);
		infoLabel.setPosition(Gdx.graphics.getWidth()/2-infoLabel.getGlyphLayout().width/2,Gdx.graphics.getHeight()/2-infoLabel.getGlyphLayout().height-50);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		time+=delta;
		AssetManager assetManager = AssetManagerHelper.getInstance().getAssetManager();
		if(assetManager.update()){
			if(type == 1&&isMapFinished){
				if(time>1){
					if(mainGame.getMainScreen() == null){
						mainGame.setMainScreen(new com.mw.ui.screen.MainScreen(mainGame));
					}
					mainGame.setScreen(mainGame.getMainScreen());
				}

			}
		}
		if(MapGenerator.map().getTmxWorldMap()!=null&&assetManager.isLoaded(MapGenerator.map().getTmxWorldMap().getName())&&!isWorldFinished){
			isWorldFinished = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					MapGenerator.map().getTmxWorldMap().getTileMapReload();
					AssetManagerHelper.getInstance().loadTiledMap(MapGenerator.map()
							.getTmxAreaMap(GameFileHelper.getInstance().getCurrentAreaName()).getName());
					isMapFinished = true;
				}
			}).start();
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
