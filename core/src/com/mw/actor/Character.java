package com.mw.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.TimeUtils;
import com.mw.map.AStarMap;
import com.mw.map.AStarNode;
import com.mw.map.DungeonMap;
import com.mw.stage.MapStage;
import com.mw.utils.Dungeon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BanditCat on 2016/6/7.
 */
public class Character extends GameMapTile {
    protected AStarMap aStarMap;
    protected List<AStarNode> path = new ArrayList<AStarNode>();//路径
    protected int indexAstarNode = 0;
    protected boolean isMoving = false;//是否移动
    protected long roundTime = TimeUtils.nanoTime();//回合时间用来和时间间隔对比
    protected boolean isFocus = false;//是否镜头跟随
    protected OrthographicCamera camera;
    protected DungeonMap dungeonMap;
    public Character(TextureAtlas textureAtlas, String regionName, OrthographicCamera cam,DungeonMap dungeonMap) {
        super(textureAtlas, regionName, cam);
        this.dungeonMap = dungeonMap;
        this.camera = cam;
        initAStarArray(dungeonMap.getDungeonArray());
    }

    /**
     * 初始化AStar数组，元素只有0,1,0代表可以通过1代表障碍
     * @param array
     */
    private void initAStarArray(int[][] array){
        int x = 0,y = 0;
        if(array != null && array.length>0){
            x = array[0].length;
            y = array.length;
        }
        aStarMap = new AStarMap(x,y);
        int[][] aStarData = new int[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
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
    private void moveInMap(){
        //每一个间隔移动一次
        if (TimeUtils.nanoTime() - roundTime >= MapStage.roundSecond) {
            roundTime = TimeUtils.nanoTime();
            if(isMoving){//是否正在移动
                indexAstarNode++;//下标+1
                //下标重置返回，停止移动
                if(indexAstarNode > path.size()-1){
                    indexAstarNode = 0;
                    isMoving = false;
                }
                if(indexAstarNode == 0){
                    return;
                }
                //按list设置每一步的位置
                try{
                    synchronized (path){
                        AStarNode n = path.get(indexAstarNode);
                        moveLogic(n.getX(),n.getY());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        if(isFocus){
            isFocus = false;
            camera.position.set(getX(),getY(), 0);
        }

    }
    //移动的逻辑
    protected void moveLogic(int x, int y) {
        //碰到门停下来，再次穿过打开
        if(dungeonMap.getDungeonArray()[x][y] == Dungeon.tileDoor){
            stopMoving();
            if(indexAstarNode == 1){
                dungeonMap.getDungeonArray()[x][y]=Dungeon.tileCorridor;
                dungeonMap.changeTileType(Dungeon.tileCorridor,x,y);
            }

        }else{
            setTilePosIndex(new GridPoint2(x,y));
        }
    }

    public void stopMoving(){
        isMoving = false;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }

    @Override
    public void act(float delta) {
        moveInMap();
        super.act(delta);
    }

    public void findWays(int endX, int endY){
        aStarMap.setSource(new AStarNode(getTilePosIndex().x,getTilePosIndex().y));
        aStarMap.setTarget(new AStarNode(endX,endY));
         synchronized (path){
            path = aStarMap.find();
            indexAstarNode = 0;
            isMoving = true;
        }
    }
}
