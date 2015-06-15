package com.mw.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.mw.actor.LoadingImage;
import com.mw.game.MainGame;

public class TransferScreen implements Screen {
	private MainGame mainGame;
	private LoadingImage image_loading;
	private TextureAtlas atlas;
	private Stage stage;
	private static final float DURATION = 0.5f;
	private Timer timer = new Timer();

	public TransferScreen(MainGame mainGame) {
		super();
		this.mainGame = mainGame;
	}

	@Override
	public void show() {
		atlas = new TextureAtlas(
				Gdx.files.internal("images/settingimages.pack"));
		TextureRegion textureRegion = atlas.findRegion("loading");
//		textureRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		image_loading = new LoadingImage(textureRegion);
		image_loading.initLoadingAction();
		image_loading.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		
		stage = new Stage();
		stage.addActor(image_loading);
		timer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				mainGame.setScreen(new MainScreen(mainGame));
			}
		}, DURATION);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

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
	}

}
