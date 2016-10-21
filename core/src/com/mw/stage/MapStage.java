package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mw.actor.TiledMapActor;
import com.mw.factory.ItemFactory;
import com.mw.game.MainGame;
import com.mw.logic.Logic;
import com.mw.logic.characters.base.Monster;
import com.mw.logic.characters.base.Player;
import com.mw.map.DungeonMap;
import com.mw.map.MapEditor;
import com.mw.map.MapGenerator;
import com.mw.map.TmxAreaMap;
import com.mw.map.TmxMap;
import com.mw.map.TmxWorldMap;
import com.mw.screen.MainScreen;
import com.mw.utils.CameraController;
import com.mw.utils.Dungeon;
import com.mw.profiles.GameFileHelper;
import com.mw.utils.KeyBoardController;
import com.mw.factory.CharacterFactory;
import com.mw.utils.LogicEventListener;

import tiled.core.TileLayer;


public class  MapStage extends Stage{
	private OrthographicCamera camera;
	private long roundTime = TimeUtils.nanoTime();
	public static final long roundSecond = 100000000;

	private TiledMap tiledMap;
	private TmxWorldMap tmxAreaMap;
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

	public MapStage(MainScreen mainScreen){
		float w = MainGame.worldWidth;
		float h = MainGame.worldHeight;
		camera = new OrthographicCamera(camSize, camSize*(h/w));
		camera.position.set(worldWidth / 2f, worldHeight / 2f, 0);
		camera.zoom = 1f;
		camera.update();
		setViewport(new FitViewport(MainGame.worldWidth,MainGame.worldHeight,camera));
		controller = new CameraController(camera);
		gestureDetector = new GestureDetector(controller);
		controller.setOnTouchListener(new CameraController.OnTouchListener() {
			@Override
			public void onTap(float x, float y) {
				elementTouch("man",x,y);
			}
		});
		//初始化地图
		level = GameFileHelper.getInstance().getCurrentLevel();
		tmxAreaMap = new TmxWorldMap(256,256);
		tiledMap = tmxAreaMap.getTileMap();
		//获取渲染
		renderer = new OrthogonalTiledMapRenderer(tiledMap,1f/32f);
		characterFactory = new CharacterFactory(this);
		itemFactory = new ItemFactory(this);
		//添加角色
//		man = characterFactory.getPlayer();
//		Logic.getInstance().setPlayer(man);
//		man.setPlayerActionListener(playerActionListener);
//		Logic.getInstance().addGameEventListener(logicEventListener);
//		MapGenerator.getInstance();
		camera.position.set(0,0,0);
//		man.getActor().setPosition(400,300);

	}

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}

	private void elementTouch(String name, float x, float y) {
		Gdx.app.log("camera","x="+camera.position.x+",y="+camera.position.y);
		//自然坐标系,x-right,y-up
		//800/16=50 600/12=50
		//原点8,6 对应0,0
		//缩小到16x12的窗口
		float h = MainGame.worldHeight;
		int x0=((int)x)/50,y0=((int)(h-y))/50;
		x0+=camera.position.x-8;
		y0+=camera.position.y-6;
		Gdx.app.log("x0y0","x0="+x0+",y0="+y0);
		tmxAreaMap.changeTile(TmxMap.LAYER_SHADOW, MapEditor.TRANS,x0,y0);
	}

	public TmxWorldMap getTmxAreaMap() {
		return tmxAreaMap;
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
		renderer.render();
//		renderer.render(new int[]{0,1,2});
		//余下actor绘制
		super.draw();
//		renderer.render(new int[]{3});
	}

	@Override
	public void dispose() {
		super.dispose();
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
				camera.translate(0,1);
			}else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
				camera.translate(0,-1);
			}else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
				camera.translate(-1,0);
			}else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
				camera.translate(1,0);
			}else if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
				camera.position.set(0,0,0);
			}
		}
		float w = MainGame.worldWidth;
		float h = MainGame.worldHeight;
		camera.viewportWidth = 16;
		camera.viewportHeight = 16*(h/w);
		camera.zoom = MathUtils.clamp(camera.zoom, 1f, 1f);
		camera.position.x = MathUtils.clamp(camera.position.x, camera.viewportWidth/2, 256-camera.viewportWidth/2);
		camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight/2, 256-camera.viewportHeight/2);
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
