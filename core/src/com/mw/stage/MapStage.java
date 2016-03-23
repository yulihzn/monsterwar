package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.mw.actor.GameMapTile;
import com.mw.actor.Player;
import com.mw.actor.TestMap;

public class  MapStage extends Stage{
	private TestMap map;
	private OrthographicCamera camera;
	private Player man;
	private TextureAtlas textureAtlas;

	private long roundTime = TimeUtils.nanoTime();
	private long roundSecond = 30000000;
	private boolean isMoving = true;

	public boolean isMoving() {
		return isMoving;
	}

	public void setIsMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public MapStage(OrthographicCamera camera){
		this.camera = camera;
		textureAtlas = new TextureAtlas(Gdx.files.internal("tiles.pack"));
		map = new TestMap(camera);
//		addActor(map);
		map.setZIndex(1);
		map.setDungeon();
		man = new Player(textureAtlas,"man",camera);
		man.setWidth(32);
		man.setHeight(32);
		man.setTilePosIndex(new GridPoint2(16, 16));
		map.setCreaturePos("man",16,16);
		man.setPosition(map.getCreaturePos("man").x,map.getCreaturePos("man").y);
		man.setZIndex(2);
		for (GameMapTile gameMapTile:
			 map.getTiles()) {
			gameMapTile.setZIndex(1);
			addActor(gameMapTile);
			TiledMapClickListener eventListener = new TiledMapClickListener(gameMapTile);
			gameMapTile.addListener(eventListener);
		}
		addActor(man);
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
	public void act (float delta) {
		super.act(delta);
		if (TimeUtils.nanoTime() - roundTime >= roundSecond) {
			roundTime = TimeUtils.nanoTime();
//			movesLikeJagger();

		}
	}
	@Override
	public boolean scrolled(int amount) {
		Gdx.app.log("scrolled",""+amount);
		if(amount == -1){
			camera.zoom += 0.1f;
		}else if(amount == 1){
			camera.zoom -= 0.1f;
		}
		if(camera.zoom < 0.1f){
			camera.zoom = 0.1f;
		}
		if(camera.zoom > 2.0f){
			camera.zoom = 2.0f;
		}
//		camera.zoom = (float)(Math.round(camera.zoom*10))/10;
		Gdx.app.log("zoom",""+camera.zoom);
		return super.scrolled(amount);
	}

	private void movesLikeJagger() {
		int x = (int)map.getCreaturePosIndex("man").x;
		int y = (int)map.getCreaturePosIndex("man").y;
		int flag_x = 1;
		int flag_y = 1;
		if(y >= TestMap.ysize){
			flag_y = -1;
		}
		if(y <= 0){
			flag_y = 1;
		}
		if(x >= TestMap.xsize){
			flag_x = -1;
			y+=flag_y;
		}
		if(x <= 0){
			flag_x = 1;
			y+=flag_y;
		}
		x = x + flag_x;
		map.setCreaturePos("man",x,y);
		man.setPosition(map.getCreaturePos("man").x,map.getCreaturePos("man").y);
		if(isMoving){
			camera.position.set(map.getCreaturePos("man").x,map.getCreaturePos("man").y, 0);
		}
	}

	public class TiledMapClickListener extends ClickListener {

		private GameMapTile gameMapTile;

		public TiledMapClickListener(GameMapTile gameMapTile) {
			this.gameMapTile = gameMapTile;
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			Gdx.app.log("","region:"+gameMapTile.getRegionName()+"("+gameMapTile.getTilePosIndex().x+","+gameMapTile.getTilePosIndex().y + ")");
			Gdx.app.log("","regionPos:"+"("+gameMapTile.getX()+","+gameMapTile.getY() + ")");
			map.setCreaturePos("man", gameMapTile.getTilePosIndex().x, gameMapTile.getTilePosIndex().y);
			man.setPosition(map.getCreaturePos("man").x,map.getCreaturePos("man").y);
			Gdx.app.log("","manPos:"+"("+man.getX()+","+man.getY() + ")");
		}
	}
}
