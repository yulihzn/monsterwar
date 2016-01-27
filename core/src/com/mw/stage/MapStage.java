package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class  MapStage extends Stage{
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
		if(amount == -1){
			camera.zoom += camera.zoom*0.05;
		}else if(amount == 1){
			camera.zoom -= camera.zoom*0.05;
		}
		Gdx.app.log("zoom",""+camera.zoom);
		return super.scrolled(amount);
	}
}
