package com.mw.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MWGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private TextureAtlas atlas;
	private ImageButton ib_start;
	private Stage stage;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture(Gdx.files.internal("images/start_bg.png"));
		atlas = new TextureAtlas(Gdx.files.internal("images/buttons.pack"));
		TextureRegionDrawable imageUp = new TextureRegionDrawable(atlas.findRegion("button_start_normal"));
		TextureRegionDrawable imageDown = new TextureRegionDrawable(atlas.findRegion("button_start_pressed"));
		ib_start = new ImageButton(imageUp, imageDown);
		ib_start.setPosition(0, 0);
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		stage.addActor(ib_start);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		stage.act();
		stage.draw();
		batch.end();
	}
}
