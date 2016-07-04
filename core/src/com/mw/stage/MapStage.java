package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.TimeUtils;
import com.mw.actor.Character;
import com.mw.actor.MapShadow;
import com.mw.actor.Player;
import com.mw.actor.TiledMapActor;
import com.mw.map.AStarMap;
import com.mw.map.AStarNode;
import com.mw.map.DungeonMap;
import com.mw.utils.Dungeon;
import com.mw.utils.GameDataHelper;
import com.mw.utils.KeyBoardController;

import java.util.ArrayList;
import java.util.List;

public class  MapStage extends Stage{
	private OrthographicCamera camera;
	private long roundTime = TimeUtils.nanoTime();
	public static final long roundSecond = 20000000;

//	private AssetManager assetManager;
//	private TiledMap tiledMap;
	private DungeonMap dungeonMap;
	private TextureAtlas textureAtlas;
	private TiledMapRenderer renderer;

	private Player man;
	private Character ghost;
	private MapShadow mapShadow;

//	private int[][]dungeonArray;


	public MapStage(OrthographicCamera camera){
		this.camera = camera;
		textureAtlas = new TextureAtlas(Gdx.files.internal("tiles.pack"));
/*		//加载地图资源
//		assetManager = new AssetManager();
//		assetManager.setLoader(TiledMap.class,new TmxMapLoader(new InternalFileHandleResolver()));
//		assetManager.load("images/tiles.tmx",TiledMap.class);
//		assetManager.finishLoading();
//		TiledMap tiledMap = assetManager.get("images/tiles.tmx");
//		//去黑线
//		for (TiledMapTileSet tmts :tiledMap.getTileSets()){
//			for(TiledMapTile tmt : tmts){
//				tmt.getTextureRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
//			}
//		}
//		Dungeon dungeon = new Dungeon();
//		dungeon.createDungeon(DungeonMap.TILE_SIZE,DungeonMap.TILE_SIZE,5000);
//		dungeonArray =dungeon.getDungeonArray();
*/
		//初始化地图
		dungeonMap = new DungeonMap();
		//获取渲染
		renderer = new OrthogonalTiledMapRenderer(dungeonMap,1f);
		//为每个tile加上actor和相应的监听
		for (MapLayer layer : dungeonMap.getLayers()) {
			TiledMapTileLayer tiledLayer = (TiledMapTileLayer)layer;
			createActorsForLayer(tiledLayer);
		}
		//添加角色
		ghost = new Character(textureAtlas,"ghost",camera,dungeonMap);
		ghost.setTilePosIndex(new GridPoint2(DungeonMap.TILE_SIZE/2,DungeonMap.TILE_SIZE/2));
		ghost.setZIndex(1);
		addActor(ghost);

		man = new Player(textureAtlas,"man",camera,dungeonMap);
		man.setTilePosIndex(new GridPoint2(DungeonMap.TILE_SIZE/2,DungeonMap.TILE_SIZE/2));
		man.setZIndex(2);
		addActor(man);



		mapShadow = new MapShadow(camera,DungeonMap.TILE_SIZE<<5,DungeonMap.TILE_SIZE<<5,dungeonMap.getDungeonArray());
		mapShadow.setPosition(0,0);
		mapShadow.setZIndex(3);
		addActor(mapShadow);
		adjustPlayerPos();

	}
	//调整玩家位置让他不卡墙
	private void adjustPlayerPos(){
		boolean a = false;
		boolean b = false;
		for(int i = -1;i < 2;i++){
			for(int j = -1;j < 2;j++){
				if(!isBlock(man.getTilePosIndex().x+i,man.getTilePosIndex().y+j)){
					man.findWays(man.getTilePosIndex().x+i,man.getTilePosIndex().y+j);
					i = 2;
					break;
				}
			}
		}
		for(int i = -1;i < 2;i++){
			for(int j = -1;j < 2;j++){
				if(!isBlock(ghost.getTilePosIndex().x+i,ghost.getTilePosIndex().y+j)){
					if(!(ghost.getTilePosIndex().x+i == man.getTilePosIndex().x
							&&ghost.getTilePosIndex().y+j == man.getTilePosIndex().y))
						ghost.findWays(ghost.getTilePosIndex().x+i,ghost.getTilePosIndex().y+j);
					i = 2;
					break;
				}
			}
		}

	}
	private boolean isBlock(int i,int j){
		int v = dungeonMap.getDungeonArray()[i][j];
		return !(v != Dungeon.tileStoneWall
				&&v!= Dungeon.tileDirtWall
				&&v!= Dungeon.tileUnused);
	}
	private void createActorsForLayer(TiledMapTileLayer tiledLayer) {
		for (int x = 0; x < tiledLayer.getWidth(); x++) {
			for (int y = 0; y < tiledLayer.getHeight(); y++) {
				TiledMapTileLayer.Cell cell = tiledLayer.getCell(x, y);
				TiledMapActor actor;
				if(cell != null){
					actor = new TiledMapActor(dungeonMap, tiledLayer, cell,new GridPoint2(x,y));
					actor.setZIndex(0);
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
	public boolean keyDown(int keyCode) {
		Gdx.app.log("keyDown","keyCode="+keyCode);
		int startX=man.getTilePosIndex().x;
		int startY=man.getTilePosIndex().y;
		int endX=startX;int endY=startY;
		switch (KeyBoardController.getInstance().getKeyType(keyCode)){
			case KeyBoardController.UP:
				if(endY+1<DungeonMap.TILE_SIZE){
					endY+=1;
				}
				man.setFocus(true);
				man.findWays(endX,endY);
				break;
			case KeyBoardController.DOWN:
				if(endY-1>=0){
					endY-=1;
				}
				man.setFocus(true);
				man.findWays(endX,endY);
				break;
			case KeyBoardController.LEFT:
				if(endX-1>=0){
					endX-=1;
				}
				man.setFocus(true);
				man.findWays(endX,endY);
				break;
			case KeyBoardController.RIGHT:
				if(endX+1<DungeonMap.TILE_SIZE){
					endX+=1;
				}
				man.setFocus(true);
				man.findWays(endX,endY);
				break;

		}
		return super.keyDown(keyCode);
	}

	@Override
	public void draw() {
		//地图绘制
		renderer.render();
		//余下actor绘制
		super.draw();
	}

	@Override
	public void dispose() {
		super.dispose();
		//释放地图
		dungeonMap.dispose();
		mapShadow.dispose();
	}

	@Override
	public void act (float delta) {
		if(man.getTilePosIndex().x != mapShadow.getSightPosIndex().x
				||man.getTilePosIndex().y != mapShadow.getSightPosIndex().y){
			mapShadow.isChangedPos = true;
		}
		mapShadow.getSightPosIndex().x = man.getTilePosIndex().x;
		mapShadow.getSightPosIndex().y = man.getTilePosIndex().y;
		//同步摄像头
		this.getViewport().setCamera(camera);
		//地图绘制设置摄像头
		renderer.setView(camera);

		if (TimeUtils.nanoTime() - roundTime >= roundSecond) {
			if(man.isMoving()){
				mapShadow.updateLines();
			}

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

	public class TiledMapClickListener extends ClickListener {

		private TiledMapActor actor;

		public TiledMapClickListener(TiledMapActor actor) {
			this.actor = actor;
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println(actor.getX()+","+actor.getY() +"value = "+actor.getCell().getTile().getId()+ " has been clicked.");
			man.findWays(actor.getTilePosIndex().x,actor.getTilePosIndex().y);
			ghost.findWays(man.getTilePosIndex().x,man.getTilePosIndex().y);
		}
	}

}
