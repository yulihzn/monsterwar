package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mw.actor.ElementsInterFace;
import com.mw.actor.MapShadow;
import com.mw.actor.TiledMapActor;
import com.mw.factory.ItemFactory;
import com.mw.game.MainGame;
import com.mw.logic.Logic;
import com.mw.logic.characters.base.Monster;
import com.mw.logic.characters.base.Player;
import com.mw.logic.item.base.Item;
import com.mw.map.DungeonMap;
import com.mw.model.MapInfoModel;
import com.mw.screen.MainScreen;
import com.mw.utils.Dungeon;
import com.mw.profiles.GameFileHelper;
import com.mw.utils.KeyBoardController;
import com.mw.factory.CharacterFactory;
import com.mw.utils.LogicEventListener;


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

	private CharacterFactory characterFactory;
	private ItemFactory itemFactory;

//	private ElementsInterFace elementsInterFace;


	public MapStage(OrthographicCamera camera, MainScreen mainScreen){
		setViewport(new FitViewport(MainGame.worldWidth,MainGame.worldHeight,camera));
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
		level = GameFileHelper.getInstance().getCurrentLevel();
		dungeonMap = new DungeonMap();
		//获取渲染
		renderer = new OrthogonalTiledMapRenderer(dungeonMap,1f);
		//为每个tile加上actor和相应的监听
		createActorsForLayer((TiledMapTileLayer)dungeonMap.getLayers().get(DungeonMap.LAYER_SHADOW));
		characterFactory = new CharacterFactory(this);
		itemFactory = new ItemFactory(this);
		//添加角色
		man = characterFactory.getPlayer();
		Logic.getInstance().setPlayer(man);
		man.setPlayerActionListener(playerActionListener);
		adjustPlayerPos(-1);
		generateMonsters();
		generateItems();

		mapShadow = new MapShadow(camera,DungeonMap.TILE_SIZE_WIDTH<<5,DungeonMap.TILE_SIZE_HEIGHT<<5,dungeonMap);
		mapShadow.setPosition(0,0);
		addActor(mapShadow);
		mapShadow.setZIndex(300);
		mapShadow.getSightPosIndex().x = man.getActor().getTilePosIndex().x;
		mapShadow.getSightPosIndex().y = man.getActor().getTilePosIndex().y;
		mapShadow.updateLines();
//		elementsInterFace = new ElementsInterFace(camera,DungeonMap.TILE_SIZE_WIDTH<<5,DungeonMap.TILE_SIZE_HEIGHT<<5,dungeonMap);
//		elementsInterFace.setPosition(0,0);
//		addActor(elementsInterFace);
//		elementsInterFace.setZIndex(1000);
//		elementsInterFace.getSightPosIndex().x = man.getActor().getTilePosIndex().x;
//		elementsInterFace.getSightPosIndex().y = man.getActor().getTilePosIndex().y;
//		elementsInterFace.drawTile();

		Logic.getInstance().addGameEventListener(logicEventListener);

	}
	private LogicEventListener logicEventListener = new LogicEventListener() {
		@Override
		public void GameOver() {
			man.getActor().remove();
			man = characterFactory.getPlayer();
			Logic.getInstance().setPlayer(man);
			man.setPlayerActionListener(playerActionListener);
			generateNextStairs(0);
		}

		@Override
		public void reachTheEdge(int dir) {
			Logic.getInstance().sendLogMessage("地图到了边界加载新的地形");
			updateMap(dir);
		}
	};
	private void updateMap(int dir){
		//生成下一关或者上一关
		dungeonMap.upDateDungeon(dir);
		man.upDateAStarArray(dungeonMap);
//      ghost.getActor().upDateAStarArray(dungeonMap);
		//阴影重置
		mapShadow.reSet(dungeonMap);
		mapShadow.getSightPosIndex().y = man.getActor().getTilePosIndex().y;
		mapShadow.getSightPosIndex().x = man.getActor().getTilePosIndex().x;
		mapShadow.updateLines();
	}

	private void generateMonsters() {
		Logic.getInstance().getMonsterArray().clear();
		for (int i = 0; i < 0; i++) {
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
	private void generateItems(){
		Logic.getInstance().getItemArray().clear();
		for (int i = 0; i < 1; i++) {
			Logic.getInstance().getItemArray().add(itemFactory.getFood());
		}
		Array<MapInfoModel> availableTiles = new Array<MapInfoModel>();
		for (int i = 0; i < dungeonMap.getMapInfo().getMapArray().length; i++) {
			for (int j = 0; j < dungeonMap.getMapInfo().getMapArray()[0].length; j++) {
				if(!isBlock(i,j)){
					availableTiles.add(dungeonMap.getMapInfo().getMapArray()[i][j]);
				}
			}
		}
		for (Item food : Logic.getInstance().getItemArray()){
			food.getActor().setTilePosIndex(availableTiles.get(MathUtils.random(0,availableTiles.size-1)).getPos());
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
		mapShadow.getSightPosIndex().y = man.getActor().getTilePosIndex().y;
		mapShadow.getSightPosIndex().x = man.getActor().getTilePosIndex().x;
		mapShadow.updateLines();
		generateMonsters();
		generateItems();
	}
	//调整玩家位置让他不卡墙
	public void adjustPlayerPos(int action){
		int type = -1;
		if(GameFileHelper.getInstance().getCharacterPos(man.getActor().getRegionName()).x == -1){
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
				if(GameFileHelper.getInstance().getCharacterPos(man.getActor().getRegionName()).x == -1){
					man.getActor().setTilePosIndex(dungeonMap.getMapInfo().getUpstairsIndex());
				}else{
					man.getActor().setTilePosIndex(GameFileHelper.getInstance().getCharacterPos(man.getActor().getRegionName()));
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
				if(endY+1<DungeonMap.TILE_SIZE_HEIGHT&&!man.isMoving()){
					endY+=1;
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
				if(endX+1<DungeonMap.TILE_SIZE_WIDTH&&!man.isMoving()){
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
//		renderer.render();
		renderer.render(new int[]{0,1,2});
		//余下actor绘制
		super.draw();
//		renderer.render(new int[]{3});
	}

	@Override
	public void dispose() {
		super.dispose();
		//释放地图
		dungeonMap.dispose();
		mapShadow.dispose();
//		elementsInterFace.dispose();
		textureAtlas.dispose();
	}

	@Override
	public void act (float delta) {
		if(man.getActor().getTilePosIndex().x != mapShadow.getSightPosIndex().x
				||man.getActor().getTilePosIndex().y != mapShadow.getSightPosIndex().y){
			mapShadow.isChangedPos = true;
		}
		mapShadow.getSightPosIndex().y = man.getActor().getTilePosIndex().y;
		mapShadow.getSightPosIndex().x = man.getActor().getTilePosIndex().x;
//		elementsInterFace.getSightPosIndex().y = man.getActor().getTilePosIndex().y;
//		elementsInterFace.getSightPosIndex().x = man.getActor().getTilePosIndex().x;

		//同步摄像头
		this.getViewport().setCamera(camera);
		//地图绘制设置摄像头
		renderer.setView(camera);

		if (TimeUtils.nanoTime() - roundTime >= roundSecond) {
			roundTime = TimeUtils.nanoTime();
			if(man.isMoving()){
				mapShadow.updateLines();
//				elementsInterFace.drawTile();
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

}
