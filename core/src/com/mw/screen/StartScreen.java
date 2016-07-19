package com.mw.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mw.actor.ParticleEffectActor;
import com.mw.base.BaseScreen;
import com.mw.game.MainGame;

public class StartScreen extends BaseScreen implements Screen{
	private Stage stage;
	private TextureAtlas atlas;
	private ImageButton ib_start;
	private Image background;
	private float backgroundScale = 1.0f;
	private ParticleEffect particleEffect;
	private ParticleEffectActor particleEffectActor;


	public StartScreen(MainGame mainGame) {
		super(mainGame);
	}

	@Override
	public void show() {
		atlas = new TextureAtlas(Gdx.files.internal("images/buttons.pack"));
		TextureRegionDrawable imageUp = new TextureRegionDrawable(atlas.findRegion("button_start_normal"));
		TextureRegionDrawable imageDown = new TextureRegionDrawable(atlas.findRegion("button_start_pressed"));
		ib_start = new ImageButton(imageUp, imageDown);
		background = new Image(new Texture(Gdx.files.internal("images/start_bg.png")));
		backgroundScale = Gdx.graphics.getHeight()/background.getHeight();
		background.setWidth(background.getWidth() * backgroundScale);
		background.setHeight(Gdx.graphics.getHeight());

		ib_start.setWidth(ib_start.getWidth() * backgroundScale);
		ib_start.setHeight(ib_start.getHeight() * backgroundScale);
		ib_start.setPosition(Gdx.graphics.getWidth() / 2 - ib_start.getWidth() / 2, ib_start.getWidth());
		background.setPosition(Gdx.graphics.getWidth()/2-background.getWidth()/2,0);
		stage = new Stage();
		ib_start.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				mainGame.setScreen(new TransferScreen(mainGame,new MainScreen(mainGame)));
				super.clicked(event, x, y);
			}
			
		});
		//test
//		mainGame.setScreen(new TransferScreen(mainGame,new MainScreen(mainGame)));
		stage.addActor(background);
		stage.addActor(ib_start);
		Gdx.input.setInputProcessor(stage);

		particleEffect = new ParticleEffect();
		particleEffect.load(Gdx.files.internal("data/fire.p"),Gdx.files.internal("images"));
		particleEffectActor = new ParticleEffectActor(particleEffect);
		particleEffectActor.scaleBy(2.0f,2.0f);
		particleEffectActor.setPposition(Gdx.graphics.getWidth()/2 + 330*backgroundScale, 140*backgroundScale);
		particleEffectActor.getEffect().scaleEffect(2.0f);
		stage.addActor(particleEffectActor);



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

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

}
