package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.mw.map.AStarMap;
import com.mw.map.AStarNode;
import com.mw.map.DungeonMap;
import com.mw.model.MapInfoModel;
import com.mw.utils.Dungeon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BanditCat on 2016/6/7.
 */
public class CharacterActor extends GameMapTile {
    protected AStarMap aStarMap;
    protected List<AStarNode> path = new ArrayList<AStarNode>();//路径
    protected boolean isMoving = false;//是否移动
    protected boolean isAttack = false;//是否攻击
    protected long roundTime = TimeUtils.nanoTime();//回合时间用来和时间间隔对比
    protected boolean isFocus = false;//是否镜头跟随
    protected OrthographicCamera camera;
    protected DungeonMap dungeonMap;
    protected SequenceAction walkSequenceAction;
    public CharacterActor(TextureAtlas textureAtlas, String regionName, OrthographicCamera cam, DungeonMap dungeonMap) {
        super(textureAtlas, regionName, cam);
        this.dungeonMap = dungeonMap;
        this.camera = cam;
        walkSequenceAction = Actions.sequence();//行走序列动画
        initAStarArray(dungeonMap.getMapInfo().getMapArray());
    }

    public void upDateAStarArray(DungeonMap dungeonMap){
        this.dungeonMap = dungeonMap;
        initAStarArray(dungeonMap.getMapInfo().getMapArray());
    }

    /**
     * 初始化AStar数组，元素只有0,1,0代表可以通过1代表障碍
     * @param array
     */
    private void initAStarArray(MapInfoModel[][] array){
        int x = 0,y = 0;
        if(array != null && array.length>0){
            x = array[0].length;
            y = array.length;
        }
        aStarMap = new AStarMap(x,y);
        int[][] aStarData = new int[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if(array[i][j].getBlock() == Dungeon.tileStoneWall
                        ||array[i][j].getBlock()== Dungeon.tileDirtWall
                        ||array[i][j].getBlock()== Dungeon.tileUnused){
                    aStarData[j][i] = 1;
                }else{
                    aStarData[j][i] = 0;
                }
            }
        }
        aStarMap.loadData(aStarData,1,0);
    }
    //移动的逻辑
    protected void moveLogic(final int curPos) {
        if(curPos >= path.size() && curPos < 0){
            return;
        }
        final int x = path.get(curPos).getX();
        final int y = path.get(curPos).getY();
        //当列表的下一条是门的话
        if(curPos+1 < path.size()){
            final int nextX = path.get(curPos+1).getX();
            final int nextY = path.get(curPos+1).getY();
            //碰到门停下来，再次穿过打开
            if(dungeonMap.getMapInfo().getMapArray()[nextX][nextY].getBlock() == Dungeon.tileDoor){
                stopMoving();
                if(curPos==0||curPos==1){
                    dungeonMap.changeTileType(Dungeon.tileDoorOpen,nextX,nextY);
                }

            }
        }
    }

    public void stopMoving(){
        isMoving = false;
        walkSequenceAction.reset();
    }

    public boolean isAttack() {
        return isAttack;
    }

    public void setAttack(boolean attack) {
        isAttack = attack;
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
        super.act(delta);
        //镜头延迟跟随（待修改）
        if(isFocus){
            isFocus = false;
            camera.position.set(getX(),getY(), 0);
            final float disX = getX() - camera.position.x;
            final float disY = getY() - camera.position.y;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    camera.translate(disX/10,disY/10);
                }
            },2f,0.01f,10).run();
        }
    }

    public void findWays(int endX, int endY){
        aStarMap.setSource(new AStarNode(getTilePosIndex().x,getTilePosIndex().y));
        aStarMap.setTarget(new AStarNode(endX,endY));
         synchronized (path){
            path = aStarMap.find();
            isMoving = true;
        }
        walkSequenceAction.reset();
        //遍历节点添加移动动画
        for (int i = 0; i < path.size(); i++) {
            final int x = path.get(i).getX();
            final int y = path.get(i).getY();
            final int pos = i;
            MoveToAction action = Actions.moveTo(x<<5,y<<5,0.05f);
            //添加移动动画
            walkSequenceAction.addAction(action);
            //添加动画完成事件
            walkSequenceAction.addAction(Actions.run(new Runnable() {
                @Override
                public void run() {
                    setTilePosIndex(new GridPoint2(x,y));
                    moveLogic(pos);
                }
            }));
        }
        //移动结束
        walkSequenceAction.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                stopMoving();
            }
        }));
        addAction(walkSequenceAction);
    }

}
