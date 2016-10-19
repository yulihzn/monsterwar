package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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
import com.mw.map.MapGenerator;
import com.mw.map.TmxAreaMap;
import com.mw.screen.MainScreen;
import com.mw.utils.Dungeon;
import com.mw.profiles.GameFileHelper;
import com.mw.utils.KeyBoardController;
import com.mw.factory.CharacterFactory;
import com.mw.utils.LogicEventListener;

import tiled.core.TileLayer;


public class  MapStage extends Stage{
	private OrthographicCamera camera;
	private long roundTime = TimeUtils.nanoTime();
	public static final long roundSecond = 10000000;

	private TiledMap tiledMap;
	private TmxAreaMap tmxAreaMap;
	private TiledMapRenderer renderer;

	private Player man;

	private int level = 0;

	private CharacterFactory characterFactory;
	private ItemFactory itemFactory;

	public MapStage(OrthographicCamera camera, MainScreen mainScreen){
		setViewport(new FitViewport(MainGame.worldWidth,MainGame.worldHeight,camera));
		this.camera = camera;
		//初始化地图
		level = GameFileHelper.getInstance().getCurrentLevel();
		tmxAreaMap = new TmxAreaMap(0,0);
		tiledMap = tmxAreaMap.getTileMap();
		//获取渲染
		renderer = new OrthogonalTiledMapRenderer(tiledMap,1f);
		characterFactory = new CharacterFactory(this);
		itemFactory = new ItemFactory(this);
		//添加角色
		man = characterFactory.getPlayer();
		Logic.getInstance().setPlayer(man);
		man.setPlayerActionListener(playerActionListener);
		Logic.getInstance().addGameEventListener(logicEventListener);
		MapGenerator.getInstance();

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
		man.getActor().setFocus(true);
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
		//同步摄像头
		this.getViewport().setCamera(camera);
		//地图绘制设置摄像头
		renderer.setView(camera);

		if (TimeUtils.nanoTime() - roundTime >= roundSecond) {
			roundTime = TimeUtils.nanoTime();
			if(man.isMoving()){
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
			Logic.getInstance().sendLogMessage("x="+actor.getTilePosIndex().x+",y="+actor.getTilePosIndex().y);
		}
	}

}
