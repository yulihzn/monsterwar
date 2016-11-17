package com.mw.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mw.screen.StartScreen;

public class MainGame extends Game {
	public static final int worldWidth = 1280;
	public static final int worldHeight = 720;
	public static String androidDir;

	@Override
	public void create() {
		setScreen(new StartScreen(this));
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

}
