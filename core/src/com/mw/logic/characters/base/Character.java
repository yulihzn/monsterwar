package com.mw.logic.characters.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.mw.actor.CharacterActor;
import com.mw.logic.characters.info.CharacterInfo;
import com.mw.map.AStarMap;
import com.mw.map.AStarNode;
import com.mw.map.DungeonMap;
import com.mw.model.MapInfoModel;
import com.mw.stage.MapStage;
import com.mw.utils.Dungeon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BanditCat on 2016/7/25.
 */
public abstract class Character implements Telegraph {
    protected StateMachine<Character,CharacterState> stateMachine;
    protected CharacterActor characterActor;
    protected CharacterInfo characterInfo;
    protected AStarMap aStarMap;
    protected List<AStarNode> path = new ArrayList<AStarNode>();//路径
    protected boolean isMoving = false;//是否移动
    protected boolean isAttack = false;//是否攻击
    protected DungeonMap dungeonMap;
    protected SequenceAction walkSequenceAction;
    protected boolean isFocus = false;//是否镜头跟随
    protected int pathIndex = 0;

    public Character(DungeonMap dungeonMap) {
        this.dungeonMap = dungeonMap;
        this.stateMachine = new DefaultStateMachine<Character, CharacterState>(this, CharacterState.IDLE, CharacterState.GLOBAL_STATE);
        walkSequenceAction = Actions.sequence();//行走序列动画
        initAStarArray(dungeonMap.getMapInfo().getMapArray());
    }

    public CharacterInfo getInfo() {
        return characterInfo;
    }

    public void setInfo(CharacterInfo characterInfo) {
        this.characterInfo = characterInfo;
    }

    public CharacterActor getActor() {
        return characterActor;
    }

    public void setActor(CharacterActor characterActor) {
        this.characterActor = characterActor;
    }

    public StateMachine<Character, CharacterState> getStateMachine() {
        return stateMachine;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return stateMachine.handleMessage(msg);
    }
    public void update (float delta) {
        stateMachine.update();
    }

    protected void attack(Character character){
    }
    public void walk(){
        if(!moveLogic(pathIndex)){return;}
        isMoving = true;
        final int x = path.get(pathIndex).getX();
        final int y = path.get(pathIndex).getY();
        characterActor.setTilePosIndexOnly(new GridPoint2(x,y));
        MoveToAction action = Actions.moveTo(x<<5,y<<5,0.05f);
        //添加移动动画
        walkSequenceAction.addAction(action);
        //添加动画完成事件
        walkSequenceAction.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                walkSequenceAction = Actions.sequence();//行走序列动画
                characterActor.removeAction(walkSequenceAction);
                characterActor.setTilePosIndex(new GridPoint2(x,y));
                moveFinish(x,y);
                stopMoving();
            }
        }));
        characterActor.addAction(walkSequenceAction);

    }
    protected void findWay(int x,int y){
        pathIndex = 0;
        aStarMap.setSource(new AStarNode(characterActor.getTilePosIndex().x,characterActor.getTilePosIndex().y));
        aStarMap.setTarget(new AStarNode(x,y));
        synchronized (path){
            path = aStarMap.find();
        }
        if(path.size()!=0){
            if(x!=getActor().getTilePosIndex().x||y!=getActor().getTilePosIndex().y){
                path.remove(0);
            }
        }
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

    public void upDateAStarArray(DungeonMap dungeonMap){
        this.dungeonMap = dungeonMap;
        initAStarArray(dungeonMap.getMapInfo().getMapArray());
    }

    //移动的逻辑,返回是否可以移动以及其它逻辑
    protected boolean moveLogic(final int curPos) {
        //如果到头了就不移动
        if(curPos >= path.size()){
            return false;
        }
        final int x = path.get(curPos).getX();
        final int y = path.get(curPos).getY();
        //当下一个点是门的话
        //碰到门停下来，改变门的状态，再次穿过打开
        if(hasObstacle(x,y)){
            return false;
        }
        //碰到npc停下来
        if(hasUnit(x,y)){
            return false;
        }
        if(hasEnemy(x,y)){
            attackUnit(curPos);
            return false;
        }
        moveBegin(x,y);
        return true;
        //当前单位的下一条和其余npc的下一条相同的话对比各个单位的速度，如果速度一致优先玩家然后是npc列表第一个
    }

    protected void moveBegin(int x, int y){

    }

    protected void moveFinish(int x, int y){

    }

    protected boolean hasObstacle(int x,int y){
        //当下一个点是门的话
        //碰到门停下来，改变门的状态，再次穿过打开
        if(dungeonMap.getMapInfo().getMapArray()[x][y].getBlock() == Dungeon.tileDoor){
            dungeonMap.changeTileType(Dungeon.tileDoorOpen,x,y);
            return true;
        }
        return false;
    }

    protected boolean hasEnemy(int x,int y){
        return false;
    }
    protected boolean hasUnit(int x,int y){
        return false;
    }


    protected void stopMoving(){
        isMoving = false;
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

    protected void attackUnit(int curPos){
        isAttack = true;
        final int x = characterActor.getTilePosIndex().x;
        final int y = characterActor.getTilePosIndex().y;
        final int nx = path.get(curPos).getX();
        final int ny = path.get(curPos).getY();
        MoveByAction action = Actions.moveBy((nx-x)*16,(ny-y)*16,0.05f);
        MoveToAction action1 = Actions.moveTo(x<<5,y<<5,0.05f);
        SequenceAction attackSeq = Actions.sequence(action,action1);
        attackSeq.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                isAttack = false;
            }
        }));
        characterActor.addAction(attackSeq);
    }

}
