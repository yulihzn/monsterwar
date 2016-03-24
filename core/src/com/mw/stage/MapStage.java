package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.mw.actor.GameMapTile;
import com.mw.actor.Player;
import com.mw.actor.TestMap;
import com.mw.actor.TiledMapActor;

public class  MapStage extends Stage{
	private OrthographicCamera camera;
	private long roundTime = TimeUtils.nanoTime();
	private long roundSecond = 30000000;
	private boolean isMoving = true;

	private TiledMap tiledMap;
	private TiledMapRenderer renderer;
	private AssetManager assetManager;

	public boolean isMoving() {
		return isMoving;
	}

	public void setIsMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public MapStage(OrthographicCamera camera){
		this.camera = camera;
		//加载地图资源
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class,new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load("images/tiles.tmx",TiledMap.class);
		assetManager.finishLoading();
		tiledMap = assetManager.get("images/tiles.tmx");
		//去黑线
		for (TiledMapTileSet tmts :tiledMap.getTileSets()){
			for(TiledMapTile tmt : tmts){
				tmt.getTextureRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
			}
		}
		//获取渲染
		renderer = new OrthogonalTiledMapRenderer(tiledMap,1f);
		//为每个tile加上actor和相应的监听
		for (MapLayer layer : tiledMap.getLayers()) {
			TiledMapTileLayer tiledLayer = (TiledMapTileLayer)layer;
			createActorsForLayer(tiledLayer);
		}
	}
	private void createActorsForLayer(TiledMapTileLayer tiledLayer) {
		for (int x = 0; x < tiledLayer.getWidth(); x++) {
			for (int y = 0; y < tiledLayer.getHeight(); y++) {
				TiledMapTileLayer.Cell cell = tiledLayer.getCell(x, y);
				TiledMapActor actor;
				if(cell != null){
					actor = new TiledMapActor(tiledMap, tiledLayer, cell);
					actor.setBounds(x * tiledLayer.getTileWidth(), y * tiledLayer.getTileHeight(), tiledLayer.getTileWidth(),
							tiledLayer.getTileHeight());
					addActor(actor);
					EventListener eventListener = new TiledMapClickListener(actor);
					actor.addListener(eventListener);
				}
			}
		}
	}

	@Override
	public void draw() {
		super.draw();
	}

	@Override
	public void dispose() {
		super.dispose();
		//释放地图
		tiledMap.dispose();
	}

	@Override
	public void act (float delta) {
		//同步摄像头
		this.getViewport().setCamera(camera);
		//地图绘制设置摄像头
		renderer.setView(camera);
		//地图绘制
		renderer.render();
		if (TimeUtils.nanoTime() - roundTime >= roundSecond) {
			roundTime = TimeUtils.nanoTime();
//			movesLikeJagger();

		}
		super.act(delta);
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
		Gdx.app.log("zoom",""+camera.zoom);
		return super.scrolled(amount);
	}

//	private void movesLikeJagger() {
//		int x = (int)map.getCreaturePosIndex("man").x;
//		int y = (int)map.getCreaturePosIndex("man").y;
//		int flag_x = 1;
//		int flag_y = 1;
//		if(y >= TestMap.ysize){
//			flag_y = -1;
//		}
//		if(y <= 0){
//			flag_y = 1;
//		}
//		if(x >= TestMap.xsize){
//			flag_x = -1;
//			y+=flag_y;
//		}
//		if(x <= 0){
//			flag_x = 1;
//			y+=flag_y;
//		}
//		x = x + flag_x;
//		map.setCreaturePos("man",x,y);
//		man.setPosition(map.getCreaturePos("man").x,map.getCreaturePos("man").y);
//		if(isMoving){
//			camera.position.set(map.getCreaturePos("man").x,map.getCreaturePos("man").y, 0);
//		}
//	}

	public class TiledMapClickListener extends ClickListener {

		private TiledMapActor actor;

		public TiledMapClickListener(TiledMapActor actor) {
			this.actor = actor;
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println(actor.getX()+","+actor.getY() + " has been clicked.");
			if(null != actor.getCell()){
				actor.getCell().setRotation(1);
			}
		}
	}
}
