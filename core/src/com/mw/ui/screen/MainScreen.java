package com.mw.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mw.game.MainGame;
import com.mw.ui.stage.CharacterStage;
import com.mw.ui.stage.MapStage;
import com.mw.ui.stage.UiStage;
import com.mw.utils.GameInputMultiplexer;

public class MainScreen extends BaseScreen{
	private MapStage mapStage;
	private UiStage uiStage;
	private CharacterStage characterStage;

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
		inputMultiplexer = new GameInputMultiplexer();
		uiStage = new UiStage(this);
		mapStage = new MapStage(this);
		mapStage.setDebugUnderMouse(true);
		characterStage = new CharacterStage(this);
	}

	@Override
	public void show() {
		mapStage.initMap();
		hideCharacterStage();

	}
	public void showCharacterStage(){
		inputMultiplexer.clear();
		inputMultiplexer.addProcessor(characterStage);
		Gdx.input.setInputProcessor(inputMultiplexer);
		characterStage.setVisible(true);
	}
	public void hideCharacterStage(){
		inputMultiplexer.clear();
		inputMultiplexer.addProcessor(uiStage);
		inputMultiplexer.addProcessor(mapStage.getGestureDetector());
		inputMultiplexer.addProcessor(mapStage);
		Gdx.input.setInputProcessor(inputMultiplexer);
		characterStage.setVisible(false);
	}
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		Gdx.gl.glEnable(GL20.GL_BLEND);
//		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		mapStage.act(Gdx.graphics.getDeltaTime());
		mapStage.draw();
		uiStage.act(Gdx.graphics.getDeltaTime());
		uiStage.draw();
		characterStage.act(Gdx.graphics.getDeltaTime());
		characterStage.draw();
	}

	public CharacterStage getCharacterStage() {
		return characterStage;
	}

	@Override
	public void resize(int width, int height) {

		characterStage.getViewport().update(width,height,true);
		mapStage.getViewport().update(width,height,false);
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
		characterStage.dispose();
		mapStage.dispose();
		uiStage.dispose();
	}

}