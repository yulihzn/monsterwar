package com.mw.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mw.screen.StartScreen;

public class MainGame extends Game {

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
