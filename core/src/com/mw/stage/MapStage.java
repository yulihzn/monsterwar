package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mw.actor.ElementsInterFace;
import com.mw.actor.MapShadow;
import com.mw.actor.TiledMapActor;
import com.mw.logic.Logic;
import com.mw.logic.characters.base.Monster;
import com.mw.logic.characters.base.Player;
import com.mw.logic.characters.info.GhostInfo;
import com.mw.logic.characters.npc.Ghost;
import com.mw.map.DungeonMap;
import com.mw.model.MapInfoModel;
import com.mw.screen.MainScreen;
import com.mw.utils.Dungeon;
import com.mw.utils.GameDataHelper;
import com.mw.utils.KeyBoardController;
import com.mw.factory.CharacterFactory;


public class  MapStage extends Stage{
	private OrthographicCamera camera;
	private long roundTime = TimeUtils.nanoTime();
	public static final long roundSecond = 10000000;

//	private AssetManager assetManager;
//	private TiledMap tiledMap;
	private DungeonMap dungeonMap;
	private TextureAtlas textureAtlas;
	private TiledMapRenderer renderer;

	private Player man;
	private MapShadow mapShadow;

	private int level = 0;

	private GameEventListener gameEventListener;

	private CharacterFactory characterFactory;

	private ElementsInterFace elementsInterFace;

	public void setGameEventListener(GameEventListener gameEventListener) {
		this.gameEventListener = gameEventListener;
	}

	public MapStage(OrthographicCamera camera, MainScreen mainScreen){
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
		level = GameDataHelper.getInstance().getCurrentLevel();
		dungeonMap = new DungeonMap();
		//获取渲染
		renderer = new OrthogonalTiledMapRenderer(dungeonMap,1f);
		//为每个tile加上actor和相应的监听
		for (MapLayer layer : dungeonMap.getLayers()) {
			TiledMapTileLayer tiledLayer = (TiledMapTileLayer)layer;
			createActorsForLayer(tiledLayer);
		}
		characterFactory = new CharacterFactory(this);
		//添加角色
		man = characterFactory.getPlayer();
		Logic.getInstance().setPlayer(man);
		man.setPlayerActionListener(playerActionListener);
		adjustPlayerPos(-1);
		generateMonsters();

		mapShadow = new MapShadow(camera,DungeonMap.TILE_SIZE<<5,DungeonMap.TILE_SIZE<<5,dungeonMap);
		mapShadow.setPosition(0,0);
		addActor(mapShadow);
		mapShadow.setZIndex(3);
		mapShadow.getSightPosIndex().x = man.getActor().getTilePosIndex().x;
		mapShadow.getSightPosIndex().y = man.getActor().getTilePosIndex().y;
		mapShadow.updateLines();
		elementsInterFace = new ElementsInterFace(camera,DungeonMap.TILE_SIZE<<5,DungeonMap.TILE_SIZE<<5,dungeonMap);
		elementsInterFace.setPosition(0,0);
		addActor(elementsInterFace);
		elementsInterFace.setZIndex(100);
		elementsInterFace.getSightPosIndex().x = man.getActor().getTilePosIndex().x;
		elementsInterFace.getSightPosIndex().y = man.getActor().getTilePosIndex().y;
		elementsInterFace.drawTile();

	}

	private void generateMonsters() {
		for (int i = 0; i < 3; i++) {
			Logic.getInstance().getMonsterArray().add(characterFactory.getGhost());
		}
		Array<MapInfoModel> availableTiles = new Array<MapInfoModel>();
		for (int i = 0; i < dungeonMap.getMapInfo().getMapArray().length; i++) {
			for (int j = 0; j < dungeonMap.getMapInfo().getMapArray()[0].length; j++) {
				if(!isBlock(i,j)){
					availableTiles.add(dungeonMap.getMapInfo().getMapArray()[i][j]);
				}
			}
		}
		for (Monster monster : Logic.getInstance().getMonsterArray()){
			monster.getActor().setTilePosIndex(availableTiles.get(MathUtils.random(0,availableTiles.size-1)).getPos());
		}
	}

	@Override
	public OrthographicCamera getCamera() {
		return camera;
	}

	public DungeonMap getDungeonMap() {
		return dungeonMap;
	}

	public TextureAtlas getTextureAtlas() {
		return textureAtlas;
	}

	private Player.PlayerActionListener playerActionListener = new Player.PlayerActionListener() {
		@Override
		public void move(int action, int x, int y) {
			switch (action){
				case Player.ACTION_DOWN:
					generateNextStairs(level+1);
					break;
				case Player.ACTION_UP:
					generateNextStairs(level-1);
					break;
			}
		}
	};

	public Player getMan() {
		return man;
	}

	/**
	 * 进入下一层
	 * @param nextLevel
     */
	private void generateNextStairs(final int nextLevel){
		int act = Player.ACTION_DOWN;
		if(nextLevel<level){
			act = Player.ACTION_UP;
		}
		final int action = act;
		//层数限制
		if(nextLevel < 0||nextLevel > 20){
			Gdx.app.log("tips","arrive limit!");
			return;
		}
		//生成下一关或者上一关
		dungeonMap.initDungeon(nextLevel);
		man.upDateAStarArray(dungeonMap);
//      ghost.getActor().upDateAStarArray(dungeonMap);
		level = nextLevel;
		//调整玩家位置
		adjustPlayerPos(action);
		man.getActor().setFocus(true);
		//阴影重置
		mapShadow.reSet(dungeonMap);
		mapShadow.updateLines();
	}
	//调整玩家位置让他不卡墙
	public void adjustPlayerPos(int action){
		int type = -1;
		if(GameDataHelper.getInstance().getCharacterPos(man.getActor().getRegionName()).x == -1){
			type = -2;
		}
		switch (action){
			case Player.ACTION_DOWN:
				type = Dungeon.tileUpStairs;
				man.getActor().setTilePosIndex(dungeonMap.getMapInfo().getUpstairsIndex());
				break;
			case Player.ACTION_UP:
				type = Dungeon.tileDownStairs;
				man.getActor().setTilePosIndex(dungeonMap.getMapInfo().getDownstairsIndex());
				break;
			default:
				if(GameDataHelper.getInstance().getCharacterPos(man.getActor().getRegionName()).x == -1){
					man.getActor().setTilePosIndex(dungeonMap.getMapInfo().getUpstairsIndex());
				}else{
					man.getActor().setTilePosIndex(GameDataHelper.getInstance().getCharacterPos(man.getActor().getRegionName()));
				}
				break;
		}
	}
	private boolean isBlock(int i,int j){
		int v = dungeonMap.getMapInfo().getMapArray()[i][j].getBlock();
		return !(v != Dungeon.tileStoneWall
				&&v!= Dungeon.tileDirtWall
				&&v!= Dungeon.tileUnused
		&&v!=Dungeon.tileNothing
		&&v!=Dungeon.tileDoor);
	}
	private void createActorsForLayer(TiledMapTileLayer tiledLayer) {
		for (int x = 0; x < tiledLayer.getWidth(); x++) {
			for (int y = 0; y < tiledLayer.getHeight(); y++) {
				TiledMapTileLayer.Cell cell = tiledLayer.getCell(x, y);
				TiledMapActor actor;
				if(cell != null){
					actor = new TiledMapActor(dungeonMap, tiledLayer, cell,new GridPoint2(x,y));
					addActor(actor);
					actor.setZIndex(0);
					actor.setBounds(x * tiledLayer.getTileWidth(), y * tiledLayer.getTileHeight(), tiledLayer.getTileWidth(),
							tiledLayer.getTileHeight());
					if(tiledLayer.getName().equals(DungeonMap.LAYER_SHADOW)){
						actor.setZIndex(3);
					}
					EventListener eventListener = new TiledMapClickListener(actor);
					actor.addListener(eventListener);

				}
			}
		}
	}

	@Override
	public boolean keyDown(int keyCode) {
		Gdx.app.log("keyDown","keyCode="+keyCode);
		int startX=man.getActor().getTilePosIndex().x;
		int startY=man.getActor().getTilePosIndex().y;
		int endX=startX;int endY=startY;
		switch (KeyBoardController.getInstance().getKeyType(keyCode)){
			case KeyBoardController.UP:
				if(endY+1<DungeonMap.TILE_SIZE&&!man.isMoving()){
					endY+=1;
//					man.getActor().setFocus(true);
//					detectedUnit(endX,endY);
					Logic.getInstance().beginRound(endX,endY);
				}
				break;
			case KeyBoardController.DOWN:
				if(endY-1>=0&&!man.isMoving()){
					endY-=1;
					Logic.getInstance().beginRound(endX,endY);
				}
				break;
			case KeyBoardController.LEFT:
				if(endX-1>=0&&!man.isMoving()){
					endX-=1;
					Logic.getInstance().beginRound(endX,endY);
				}
				break;
			case KeyBoardController.RIGHT:
				if(endX+1<DungeonMap.TILE_SIZE&&!man.isMoving()){
					endX+=1;
					Logic.getInstance().beginRound(endX,endY);
				}
				break;
			case KeyBoardController.SPACE:
				if(!man.isMoving()){
					Logic.getInstance().beginRound(endX,endY);
				}
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
		elementsInterFace.dispose();
	}

	@Override
	public void act (float delta) {
		if(man.getActor().getTilePosIndex().x != mapShadow.getSightPosIndex().x
				||man.getActor().getTilePosIndex().y != mapShadow.getSightPosIndex().y){
			mapShadow.isChangedPos = true;
		}
		mapShadow.getSightPosIndex().y = man.getActor().getTilePosIndex().y;
		mapShadow.getSightPosIndex().x = man.getActor().getTilePosIndex().x;
		elementsInterFace.getSightPosIndex().y = man.getActor().getTilePosIndex().y;
		elementsInterFace.getSightPosIndex().x = man.getActor().getTilePosIndex().x;

		//同步摄像头
		this.getViewport().setCamera(camera);
		//地图绘制设置摄像头
		renderer.setView(camera);

		if (TimeUtils.nanoTime() - roundTime >= roundSecond) {
			roundTime = TimeUtils.nanoTime();
			if(man.isMoving()){
				mapShadow.updateLines();
				elementsInterFace.drawTile();
			}
			man.update(delta);
			for (Monster monster : Logic.getInstance().getMonsterArray()){
				monster.update(delta);
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
			Logic.getInstance().beginRound(actor.getTilePosIndex().x,actor.getTilePosIndex().y);
		}
	}

	/**
	 * 检查该位置是否有指定方块物品或者角色
	 * @param x
	 * @param y
     */
	private void detectedUnit(int x, int y) {
		man.doClick(this,x,y);
		for (int i = 0; i < Logic.getInstance().getMonsterArray().size; i++) {
			Monster monster = Logic.getInstance().getMonsterArray().get(i);
			if(monster.getInfo().getName().equals(GhostInfo.NAME)){
				Ghost ghost = (Ghost) monster;
				ghost.doClick(this,man.getActor().getTilePosIndex().x,man.getActor().getTilePosIndex().y);
			}
		}
	}

	public interface GameEventListener{
		void onActionChanged(int action);
	}

}
