package com.mw.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mw.base.BaseScreen;
import com.mw.game.MainGame;

public class MainScreen extends BaseScreen implements Screen{
	private Stage stage = new Stage();
	private ImageButton ib_back;
	private TextureAtlas atlas;
	private static final float BACKPADDING = 10;

	public MainScreen(MainGame mainGame) {
		super(mainGame);
	}

	@Override
	public void show() {
		atlas = new TextureAtlas(Gdx.files.internal("images/buttons.pack"));
		TextureRegionDrawable imageUp = new TextureRegionDrawable(atlas.findRegion("button_back_normal"));
		TextureRegionDrawable imageDown = new TextureRegionDrawable(atlas.findRegion("button_back_pressed"));
		ib_back = new ImageButton(imageUp, imageDown);
		ib_back.setPosition(BACKPADDING, Gdx.graphics.getHeight()-BACKPADDING-ib_back.getWidth());
		stage = new Stage();
		ib_back.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				mainGame.setScreen(new TransferScreen(mainGame,new StartScreen(mainGame)));
				super.clicked(event, x, y);
			}

		});
		stage.addActor(ib_back);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
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
