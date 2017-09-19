package com.mw.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mw.components.map.MapGenerator;
import com.mw.ui.screen.MainScreen;
import com.mw.ui.screen.StartScreen;
import com.mw.ui.screen.TransferScreen;
import com.mw.utils.AssetManagerHelper;

public class MainGame extends Game {
	public static final int worldWidth = 1280;
	public static final int worldHeight = 720;
	public static String androidDir;

	private MainScreen mainScreen;
	private StartScreen startScreen;
	private TransferScreen transferScreen;

	public MainScreen getMainScreen() {
		return mainScreen;
	}

	public void setMainScreen(MainScreen mainScreen) {
		this.mainScreen = mainScreen;
	}

	public StartScreen getStartScreen() {
		return startScreen;
	}

	public void setStartScreen(StartScreen startScreen) {
		this.startScreen = startScreen;
	}

	public TransferScreen getTransferScreen() {
		return transferScreen;
	}

	public void setTransferScreen(TransferScreen transferScreen) {
		this.transferScreen = transferScreen;
	}
	public void loadingMap(){

    }

	@Override
	public void create() {
		AssetManagerHelper.getInstance().init();
		startScreen = new StartScreen(this);
		transferScreen = new TransferScreen(this,1);
		setScreen(startScreen);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		startScreen.dispose();
		if(mainScreen!=null){
			mainScreen.dispose();
		}
		transferScreen.dispose();
		AssetManagerHelper.getInstance().getAssetManager().clear();
		AssetManagerHelper.getInstance().getAssetManager().dispose();
		MapGenerator.map().dispose();
	}
}
