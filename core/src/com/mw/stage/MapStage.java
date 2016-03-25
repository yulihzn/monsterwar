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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.mw.actor.GameMapTile;
import com.mw.actor.Player;
import com.mw.actor.TiledMapActor;
import com.mw.map.AStarMap;
import com.mw.map.AStarNode;
import com.mw.map.DungeonMap;
import com.mw.utils.Dungeon;

import java.util.ArrayList;
import java.util.List;

public class  MapStage extends Stage{
	private OrthographicCamera camera;
	private long roundTime = TimeUtils.nanoTime();
	private long roundSecond = 30000000;
	private boolean isMoving = false;

	private TiledMap tiledMap;
	private DungeonMap dungeonMap;
	private TiledMapRenderer renderer;
	private AssetManager assetManager;
	private TextureAtlas textureAtlas;

	private Player man;
	private AStarMap aStarMap;
	private List<AStarNode> path = new ArrayList<AStarNode>();
	private int indexAstarNode = 0;


	public MapStage(OrthographicCamera camera){
		this.camera = camera;
		textureAtlas = new TextureAtlas(Gdx.files.internal("tiles.pack"));
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
		Dungeon dungeon = new Dungeon();
		dungeon.createDungeon(DungeonMap.TILE_SIZE,DungeonMap.TILE_SIZE,5000);
		initAstarArray(dungeon.getDungeonArray());
		dungeonMap = new DungeonMap(dungeon.getDungeonArray());
		for(TiledMapTileSet tmts : dungeonMap.getTileSets()){
			for(TiledMapTile tmt :tmts){
				tmt.getTextureRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
			}
		}
		//获取渲染
		renderer = new OrthogonalTiledMapRenderer(dungeonMap,1f);
		//为每个tile加上actor和相应的监听
		for (MapLayer layer : dungeonMap.getLayers()) {
			TiledMapTileLayer tiledLayer = (TiledMapTileLayer)layer;
			createActorsForLayer(tiledLayer);
		}

		man = new Player(textureAtlas,"man",camera);
		man.setTilePosIndex(new GridPoint2(DungeonMap.TILE_SIZE/2,DungeonMap.TILE_SIZE/2));
		man.setPosition((DungeonMap.TILE_SIZE/2)<<5,(DungeonMap.TILE_SIZE/2)<<5);
		addActor(man);
	}
	private void createActorsForLayer(TiledMapTileLayer tiledLayer) {
		for (int x = 0; x < tiledLayer.getWidth(); x++) {
			for (int y = 0; y < tiledLayer.getHeight(); y++) {
				TiledMapTileLayer.Cell cell = tiledLayer.getCell(x, y);
				TiledMapActor actor;
				if(cell != null){
					actor = new TiledMapActor(dungeonMap, tiledLayer, cell,new GridPoint2(x,y));
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
		dungeonMap.dispose();
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
			movesLikeJagger();

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
	private void initAstarArray(int[][] array){
		aStarMap = new AStarMap(DungeonMap.TILE_SIZE,DungeonMap.TILE_SIZE);
		int[][] aStarData = new int[DungeonMap.TILE_SIZE][DungeonMap.TILE_SIZE];
		for (int i = 0; i < DungeonMap.TILE_SIZE; i++) {
			for (int j = 0; j < DungeonMap.TILE_SIZE; j++) {
				if(array[i][j] == Dungeon.tileStoneWall
						||array[i][j]== Dungeon.tileDirtWall
						||array[i][j]== Dungeon.tileUnused){
					aStarData[j][i] = 1;
				}else{
					aStarData[j][i] = 0;
				}
			}
		}
		aStarMap.loadData(aStarData,1,0);

	}

	private void movesLikeJagger() {

		if(isMoving){
//			camera.position.set(man.getX(),man.getY(), 0);
			indexAstarNode++;
			if(indexAstarNode > path.size()-1){
				indexAstarNode = 0;
				isMoving = false;
				return;
			}
			try{
				synchronized (path){
					AStarNode n = path.get(indexAstarNode);
					man.setTilePosIndex(new GridPoint2(n.getX(),n.getY()));
					man.setPosition(n.getX()<<5,n.getY()<<5);
					Gdx.app.log("man","x="+(n.getX()<<5)+", y="+(n.getY()<<5));
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	public class TiledMapClickListener extends ClickListener {

		private TiledMapActor actor;

		public TiledMapClickListener(TiledMapActor actor) {
			this.actor = actor;
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println(actor.getX()+","+actor.getY() +"value = "+actor.getCell().getTile().getId()+ " has been clicked.");
			if(null != actor.getCell()){
//				actor.getCell().setRotation(1);
			}
			aStarMap.setSource(new AStarNode(man.getTilePosIndex().x,man.getTilePosIndex().y));
			aStarMap.setTarget(new AStarNode(actor.getTilePosIndex().x,actor.getTilePosIndex().y));
			synchronized (path){
				path = aStarMap.find();
				indexAstarNode = 0;
				isMoving = true;
			}
		}
	}
}
