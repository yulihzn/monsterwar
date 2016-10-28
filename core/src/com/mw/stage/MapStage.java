package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mw.actor.ShadowImage;
import com.mw.actor.TiledMapActor;
import com.mw.factory.ItemFactory;
import com.mw.game.MainGame;
import com.mw.logic.Logic;
import com.mw.logic.characters.base.Player;
import com.mw.map.AreaTile;
import com.mw.map.MapGenerator;
import com.mw.map.MapShadow;
import com.mw.map.TmxAreaMap;
import com.mw.map.TmxMap;
import com.mw.screen.MainScreen;
import com.mw.utils.AssetManagerHelper;
import com.mw.utils.CameraController;
import com.mw.utils.Dungeon;
import com.mw.profiles.GameFileHelper;
import com.mw.factory.CharacterFactory;
import com.mw.utils.LogicEventListener;

import tiled.core.TileLayer;


public class  MapStage extends Stage{
	private OrthographicCamera camera;
	private long roundTime = TimeUtils.nanoTime();
	public static final long roundSecond = 100000000;

	private TiledMap tiledMap;
	private TmxAreaMap tmxAreaMap;
	private TiledMapRenderer renderer;

	private Player man;

	private int level = 0;

	private CharacterFactory characterFactory;
	private ItemFactory itemFactory;

	CameraController controller;
	GestureDetector gestureDetector;
	private float worldWidth = 256;
	private float worldHeight = 256;
	private float camSize = 16;
	private MapShadow mapShadow;

	public MapStage(MainScreen mainScreen){
		float w = MainGame.worldWidth;
		float h = MainGame.worldHeight;
		camera = new OrthographicCamera(camSize, camSize*(h/w));
		camera.position.set(0f, 0f, 0);
		camera.zoom = 1f;
		camera.update();
		setViewport(new FitViewport(MainGame.worldWidth,MainGame.worldHeight,camera));
		controller = new CameraController(camera);
		gestureDetector = new GestureDetector(controller);
		//初始化地图
		level = GameFileHelper.getInstance().getCurrentLevel();
		tmxAreaMap = MapGenerator.getInstance().getTmxAreaMap("area0_0");
		tiledMap = tmxAreaMap.getTileMap();
//		tiledMap.getLayers().get(TmxMap.LAYER_SHADOW).setVisible(false);
		//获取渲染
		renderer = new OrthogonalTiledMapRenderer(tiledMap,1f/32f);
		characterFactory = new CharacterFactory(this);
		itemFactory = new ItemFactory(this);
		controller.setOnTouchListener(new CameraController.OnTouchListener() {
			@Override
			public void onTap(float x, float y) {
				elementTouch("",x,y);
			}
		});

		//添加角色
		man = characterFactory.getPlayer();
//		Logic.getInstance().setPlayer(man);
//		man.setPlayerActionListener(playerActionListener);
//		Logic.getInstance().addGameEventListener(logicEventListener);
//		MapGenerator.getInstance();
//		man.getActor().setPosition(16,14);
		mapShadow = new MapShadow(camera);
//		addActor(mapShadow);


	}

	@Override
	public boolean scrolled(int amount) {
		Gdx.app.log("scrolled",""+amount);
		if(amount == -1){
			camera.zoom += 0.1f;
		}else if(amount == 1){
			camera.zoom -= 0.1f;
		}

		Gdx.app.log("zoom",""+camera.zoom);
		return super.scrolled(amount);
	}

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}

	private void elementTouch(String name, float x, float y) {
		Vector3 vector3 = new Vector3(x,y,0);
		//转换屏幕坐标到camera坐标
		getViewport().unproject(vector3);
		int x0 = (int)vector3.x;
		int y0 = (int)vector3.y;
		Gdx.app.log("touchDownx0y0","x0="+x0+",y0="+y0);
		if(tmxAreaMap.getTileId(TmxMap.LAYER_SHADOW,x0,y0)!=AreaTile.S_SHADOW){
			Logic.getInstance().beginRound(x0,y0);
		}
	}

	public void updateCam(float delta,float Xtaget, float Ytarget) {

		//Creating a vector 3 which represents the target location myplayer)
		Vector3 target = new Vector3(Xtaget,Ytarget,0);
		//Change speed to your need
		final float speed=delta,ispeed=1.0f-speed;
		//The result is roughly: old_position*0.9 + target * 0.1
		Vector3 cameraPosition = camera.position;
		cameraPosition.scl(ispeed);
		target.scl(speed);
		cameraPosition.add(target);
		camera.position.set(cameraPosition);
	}

	private LogicEventListener logicEventListener = new LogicEventListener() {
		@Override
		public void GameOver() {
			man.getActor().remove();
			man = characterFactory.getPlayer();
			Logic.getInstance().setPlayer(man);
			man.setPlayerActionListener(playerActionListener);
//			generateNextStairs(0);
		}

		@Override
		public void reachTheEdge(int dir) {
			Logic.getInstance().sendLogMessage("地图到了边界加载新的地形");
			updateMap(dir);
		}
	};
	private void updateMap(int dir){
		//生成下一关或者上一关
	}

	@Override
	public OrthographicCamera getCamera() {
		return camera;
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
		level = nextLevel;
		//调整玩家位置
//		man.getActor().setFocus(true);
	}
	private boolean isBlock(int i,int j){
		int v = ((TileLayer)tmxAreaMap.getLayer(1)).getTileAt(i,j).getId();
		return !(v != Dungeon.tileStoneWall
				&&v!= Dungeon.tileDirtWall
				&&v!= Dungeon.tileUnused
		&&v!=Dungeon.tileNothing
		&&v!=Dungeon.tileDoor);
	}

	@Override
	public boolean keyDown(int keyCode) {
//		Gdx.app.log("keyDown","keyCode="+keyCode);
//		int startX=man.getActor().getTilePosIndex().x;
//		int startY=man.getActor().getTilePosIndex().y;
//		int endX=startX;int endY=startY;
//		switch (KeyBoardController.getInstance().getKeyType(keyCode)){
//			case KeyBoardController.UP:
//				if(endY+1<DungeonMap.TILE_SIZE_HEIGHT&&!man.isMoving()){
//					endY+=1;
//					Logic.getInstance().beginRound(endX,endY);
//				}
//				break;
//			case KeyBoardController.DOWN:
//				if(endY-1>=0&&!man.isMoving()){
//					endY-=1;
//					Logic.getInstance().beginRound(endX,endY);
//				}
//				break;
//			case KeyBoardController.LEFT:
//				if(endX-1>=0&&!man.isMoving()){
//					endX-=1;
//					Logic.getInstance().beginRound(endX,endY);
//				}
//				break;
//			case KeyBoardController.RIGHT:
//				if(endX+1<DungeonMap.TILE_SIZE_WIDTH&&!man.isMoving()){
//					endX+=1;
//					Logic.getInstance().beginRound(endX,endY);
//				}
//				break;
//			case KeyBoardController.SPACE:
//				if(!man.isMoving()){
//					Logic.getInstance().beginRound(endX,endY);
//				}
//				break;
//
//		}
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
		mapShadow.dispose();
	}

	@Override
	public void act (float delta) {

		if (TimeUtils.nanoTime() - roundTime >= roundSecond) {
			roundTime = TimeUtils.nanoTime();
//			if(man.isMoving()){
//			}
//			man.update(delta);
//			for (Monster monster : Logic.getInstance().getMonsterArray()){
//				monster.update(delta);
//			}
			roundTime = TimeUtils.nanoTime();
			if(Gdx.input.isKeyPressed(Input.Keys.UP)){
				if(!man.isMoving()){
					Logic.getInstance().beginRound(man.getActor().getTilePosIndex().x,man.getActor().getTilePosIndex().y+1);
				}
			}else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
				if(!man.isMoving()){
					Logic.getInstance().beginRound(man.getActor().getTilePosIndex().x,man.getActor().getTilePosIndex().y-1);
				}
			}else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
				if(!man.isMoving()){
					Logic.getInstance().beginRound(man.getActor().getTilePosIndex().x-1,man.getActor().getTilePosIndex().y);
				}
			}else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
				if(!man.isMoving()){
					Logic.getInstance().beginRound(man.getActor().getTilePosIndex().x+1,man.getActor().getTilePosIndex().y);
				}
			}else if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			}
			int x = man.getActor().getTilePosIndex().x;
			int y = man.getActor().getTilePosIndex().y;
			int size = 7;
			for (int i = -size; i < size; i++) {
				for (int j = -size; j < size; j++) {
					if(tmxAreaMap.getTileId(TmxMap.LAYER_SHADOW,x+i,y+j)!=AreaTile.S_TRANS){
						tmxAreaMap.changeTile(TmxMap.LAYER_SHADOW, AreaTile.S_TRANS,x+i,y+j);
					}
				}
			}
			mapShadow.setSightPosition(man.getActor().getTilePosIndex().x,man.getActor().getTilePosIndex().y);
		}
		updateCam(0.3f,man.getActor().getTilePosIndex().x,man.getActor().getTilePosIndex().y);
		float w = MainGame.worldWidth;
		float h = MainGame.worldHeight;
		camera.viewportWidth = 16;
		camera.viewportHeight = 16*(h/w);
		camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 16f);
		camera.position.x = MathUtils.clamp(camera.position.x, camera.viewportWidth/2, 256-camera.viewportWidth/2);
		camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight/2, 256-camera.viewportHeight/2);
//		camera.position.x = MathUtils.clamp(camera.position.x, 0, 256);
//		camera.position.y = MathUtils.clamp(camera.position.y, 0, 256);
		camera.update();
		//同步摄像头
		this.getViewport().setCamera(camera);
		//地图绘制设置摄像头
		renderer.setView(camera);
		super.act(delta);
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
			Logic.getInstance().sendLogMessage("x="+actor.getTilePosIndex().x+",y="+actor.getTilePosIndex().y);
		}
	}

}
