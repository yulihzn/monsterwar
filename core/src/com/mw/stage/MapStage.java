package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MapStage extends Stage{
	private OrthographicCamera camera;
	public MapStage(OrthographicCamera camera){
		this.camera = camera;
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		super.draw();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	@Override
	public boolean scrolled(int amount) {
		Gdx.app.log("scrolled",""+amount);
		camera.zoom += amount*0.25;
		if(camera.zoom == 0.0f){
			camera.zoom = 0.1f;
		}
		Gdx.app.log("zoom",""+camera.zoom);
		return super.scrolled(amount);
	}
}
