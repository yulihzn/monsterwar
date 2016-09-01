package com.mw.logic.characters.base;

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
    public void doClick(MapStage mapStage, int x, int y){

    }
    protected void walk(){

    }
    protected void findWay(int x,int y){

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
        //当前单位的下一条和其余npc的下一条相同的话对比各个单位的速度，如果速度一致优先玩家然后是npc列表第一个
    }


    protected boolean hasEnemy(int x,int y) {
        return false;
    }
    protected boolean hasUnit(int x,int y){
        return false;
    }


    public void stopMoving(){
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
        final int x = path.get(curPos).getX();
        final int y = path.get(curPos).getY();
        final int nx = path.get(curPos+1).getX();
        final int ny = path.get(curPos+1).getY();
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
    public void findWays(int endX, int endY){
        aStarMap.setSource(new AStarNode(characterActor.getTilePosIndex().x,characterActor.getTilePosIndex().y));
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
                    characterActor.setTilePosIndex(new GridPoint2(x,y));
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
        characterActor.addAction(walkSequenceAction);
    }

}
