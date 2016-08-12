package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.mw.actor.MapShadow;
import com.mw.actor.PlayerActor;
import com.mw.actor.TiledMapActor;
import com.mw.logic.Logic;
import com.mw.logic.characters.base.Player;
import com.mw.map.DungeonMap;
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
		dungeonMap = new DungeonMap(GameDataHelper.getInstance().getGameMap(level),level);
		//获取渲染
		renderer = new OrthogonalTiledMapRenderer(dungeonMap,1f);
		//为每个tile加上actor和相应的监听
		for (MapLayer layer : dungeonMap.getLayers()) {
			TiledMapTileLayer tiledLayer = (TiledMapTileLayer)layer;
			createActorsForLayer(tiledLayer);
		}
		characterFactory = new CharacterFactory(this);
		//添加角色
		generateMonsters();
		man = characterFactory.getPlayer();
		((PlayerActor)man.getActor()).setPlayerActionListener(playerActionListener);
		adjustPlayerPos(-1);

		mapShadow = new MapShadow(camera,DungeonMap.TILE_SIZE<<5,DungeonMap.TILE_SIZE<<5,dungeonMap);
		mapShadow.setPosition(0,0);
		addActor(mapShadow);
		mapShadow.setZIndex(3);
		mapShadow.getSightPosIndex().x = man.getActor().getTilePosIndex().x;
		mapShadow.getSightPosIndex().y = man.getActor().getTilePosIndex().y;
		mapShadow.updateLines();

	}

	private void generateMonsters() {
		for (int i = 0; i < 10; i++) {
			Logic.getInstance().getMonsterArray().add(characterFactory.getGhost());
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

	private PlayerActor.PlayerActionListener playerActionListener = new PlayerActor.PlayerActionListener() {
		@Override
		public void move(int action, int x, int y) {
			switch (action){
				case PlayerActor.ACTION_DOWN:
					generateNextStairs(level+1);
					break;
				case PlayerActor.ACTION_UP:
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
		int act = PlayerActor.ACTION_DOWN;
		if(nextLevel<level){
			act = PlayerActor.ACTION_UP;
		}
		final int action = act;
		//层数限制
		if(nextLevel < 0||nextLevel > 20){
			Gdx.app.log("tips","arrive limit!");
			return;
		}
		//生成下一关或者上一关
		dungeonMap.setOnEventChangedListener(new DungeonMap.OnEventChangedListener() {
			@Override
			public void onEventFinish(int type) {
				switch (type){
					case DungeonMap.MESSAGE_GENERATE_SUCCESS:
						man.getActor().upDateAStarArray(dungeonMap);
//						ghost.getActor().upDateAStarArray(dungeonMap);
						level = nextLevel;
						//调整玩家位置
						adjustPlayerPos(action);
						man.getActor().setFocus(true);
						//阴影重置
						mapShadow.reSet(dungeonMap);
						mapShadow.updateLines();
						break;
				}
			}
		});
		dungeonMap.generateNextDungeon(GameDataHelper.getInstance().getGameMap(nextLevel),nextLevel);

	}
	//调整玩家位置让他不卡墙
	public void adjustPlayerPos(int action){
		int type = -1;
		if(GameDataHelper.getInstance().getCharacterPos(man.getActor().getRegionName()).x == -1){
			type = -2;
		}
		switch (action){
			case PlayerActor.ACTION_DOWN:
				type = Dungeon.tileUpStairs;
				break;
			case PlayerActor.ACTION_UP:
				type = Dungeon.tileDownStairs;
				break;
			default:
				if(type == -2){
					type = Dungeon.tileUpStairs;
				}
				break;
		}
		//地图上面的特殊位置要存储起来，例如上下楼等
		if(type != -1){
			for (int i = 0; i < dungeonMap.getDungeonArray().length; i++) {
				for (int j = 0; j < dungeonMap.getDungeonArray()[0].length; j++) {
					if(dungeonMap.getDungeonArray()[i][j]==type){
						man.getActor().setTilePosIndex(new GridPoint2(i,j));
						i = dungeonMap.getDungeonArray().length;
						break;
					}
				}
			}
		}else{
			man.getActor().setTilePosIndex(GameDataHelper.getInstance().getCharacterPos(man.getActor().getRegionName()));
		}
//		for(int i = -1;i < 2;i++){
//			for(int j = -1;j < 2;j++){
//				if(!isBlock(ghost.getActor().getTilePosIndex().x+i,ghost.getActor().getTilePosIndex().y+j)){
//					if(!(ghost.getActor().getTilePosIndex().x+i == man.getActor().getTilePosIndex().x
//							&&ghost.getActor().getTilePosIndex().y+j == man.getActor().getTilePosIndex().y))
////						ghost.getActor().findWays(ghost.getActor().getTilePosIndex().x+i,ghost.getActor().getTilePosIndex().y+j);
//					i = 2;
//					break;
//				}
//			}
//		}
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
				if(endY+1<DungeonMap.TILE_SIZE){
					endY+=1;
				}
				man.getActor().setFocus(true);
				man.getActor().findWays(endX,endY);
				break;
			case KeyBoardController.DOWN:
				if(endY-1>=0){
					endY-=1;
				}
				man.getActor().setFocus(true);
				man.getActor().findWays(endX,endY);
				break;
			case KeyBoardController.LEFT:
				if(endX-1>=0){
					endX-=1;
				}
				man.getActor().setFocus(true);
				man.getActor().findWays(endX,endY);
				break;
			case KeyBoardController.RIGHT:
				if(endX+1<DungeonMap.TILE_SIZE){
					endX+=1;
				}
				man.getActor().setFocus(true);
				man.getActor().findWays(endX,endY);
				break;
			case KeyBoardController.SPACE:
				man.getActor().findWays(endX,endY);
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
		if(man.getActor().getTilePosIndex().x != mapShadow.getSightPosIndex().x
				||man.getActor().getTilePosIndex().y != mapShadow.getSightPosIndex().y){
			mapShadow.isChangedPos = true;
		}
		mapShadow.getSightPosIndex().x = man.getActor().getTilePosIndex().x;
		mapShadow.getSightPosIndex().y = man.getActor().getTilePosIndex().y;
		//同步摄像头
		this.getViewport().setCamera(camera);
		//地图绘制设置摄像头
		renderer.setView(camera);

		if (TimeUtils.nanoTime() - roundTime >= roundSecond) {
			roundTime = TimeUtils.nanoTime();
			if(man.getActor().isMoving()){
				mapShadow.updateLines();
			}
			man.update(delta);
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
			detectedUnit(actor.getTilePosIndex().x,actor.getTilePosIndex().y);
//			ghost.findWays(man.getTilePosIndex().x,man.getTilePosIndex().y);
		}
	}

	/**
	 * 检查该位置是否有指定方块物品或者角色
	 * @param x
	 * @param y
     */
	private void detectedUnit(int x, int y) {
		man.getActor().findWays(x,y);
//		int[] arr = {4,8,1,2};
//		//左下开始
//		dungeonMap.changeShadowTileType(1,x,y);
//		dungeonMap.changeShadowTileType(2,x+1,y);
//		dungeonMap.changeShadowTileType(4,x,y+1);
//		dungeonMap.changeShadowTileType(8,x+1,y+1);
	}

	public interface GameEventListener{
		void onActionChanged(int action);
	}

}
