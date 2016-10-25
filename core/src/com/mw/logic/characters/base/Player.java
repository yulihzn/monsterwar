package com.mw.logic.characters.base;

import com.mw.logic.Logic;
import com.mw.logic.characters.info.CharacterInfo;
import com.mw.logic.characters.info.PlayerInfo;
import com.mw.logic.characters.npc.Ghost;
import com.mw.logic.item.base.Food;
import com.mw.logic.item.base.Item;
import com.mw.map.DungeonMap;
import com.mw.ui.LogMessageTable;
import com.mw.utils.Dungeon;
import com.mw.profiles.GameFileHelper;

/**
 * Created by BanditCat on 2016/7/25.
 */
public class Player extends Character {
    public static final int ACTION_DOWN = 1;
    public static final int ACTION_UP = 0;
    public Player() {
        super();
    }

    @Override
    public void findWay(int x, int y) {
        super.findWay(x, y);
    }

    @Override
    protected void attack(Character character) {
        super.attack(character);
    }

    @Override
    protected void moveFinish(int x, int y) {
        super.moveFinish(x, y);
        GameFileHelper.getInstance().setCurrentStep(PlayerInfo.NAME, GameFileHelper.getInstance().getCurrentStep(PlayerInfo.NAME)+1);

    }

    @Override
    protected void moveBegin(int x, int y) {
        super.moveBegin(x, y);
        Food food = null;
        for (Item item : Logic.getInstance().getItemArray()){
            if(item.getActor().getTilePosIndex().x==x&&item.getActor().getTilePosIndex().y==y){
                food = (Food)item;
                break;
            }
        }
        if(food != null){
            getItem(food);
        }
    }

    @Override
    protected void stayAround(int x, int y) {
        super.stayAround(x, y);
        if(playerActionListener != null){
//            if(dungeonMap.getMapInfo().getMapArray()[x][y].getBlock() == Dungeon.tileUpStairs){
//                playerActionListener.move(ACTION_UP,x,y);
//                Logic.getInstance().sendLogMessage("你费力地爬了上来", LogMessageTable.TYPE_NORMAL);
//            }else if(dungeonMap.getMapInfo().getMapArray()[x][y].getBlock() == Dungeon.tileDownStairs){
//                playerActionListener.move(ACTION_DOWN,x,y);
//                Logic.getInstance().sendLogMessage("不知道这底下是什么", LogMessageTable.TYPE_NORMAL);
//            }
        }
    }

    @Override
    protected boolean isReachTheEdge(int x, int y) {
        if(x<getInfo().getSightRange()||getInfo().getSightRange()+x>DungeonMap.TILE_SIZE_WIDTH
                ||y<getInfo().getSightRange()||getInfo().getSightRange()+y>DungeonMap.TILE_SIZE_HEIGHT){
            Logic.getInstance().sendLogMessage("那边不知道有什么，我先观望一下", LogMessageTable.TYPE_NORMAL);
            int curX = getActor().getTilePosIndex().x;
            int curY = getActor().getTilePosIndex().y;
            if(curX>x&&curY==y){
                Logic.getInstance().reachTheEdge(Logic.DIR_LEFT);
            }else if(curX<x&&curY==y){
                Logic.getInstance().reachTheEdge(Logic.DIR_RIGHT);
            }else if(curY<y&&curX==x){
                Logic.getInstance().reachTheEdge(Logic.DIR_TOP);
            }else if(curY>y&&curX==x){
                Logic.getInstance().reachTheEdge(Logic.DIR_BOTTOM);
            }else if(curY<y&&curX<x){
                Logic.getInstance().reachTheEdge(Logic.DIR_TOPRIGHT);
            }else if(curY<y&&curX>x){
                Logic.getInstance().reachTheEdge(Logic.DIR_TOPLEFT);
            }else if(curY>y&&curX>x){
                Logic.getInstance().reachTheEdge(Logic.DIR_BOTTOMLEFT);
            }else if(curY>y&&curX<x){
                Logic.getInstance().reachTheEdge(Logic.DIR_BOTTOMRIGHT);
            }
            return true;
        }
        return super.isReachTheEdge(x, y);
    }

    @Override
    public boolean hasEnemy(int x, int y) {
        boolean isEnemy=false;
        for (Monster monster : Logic.getInstance().getMonsterArray()){
            if(monster.getActor().getTilePosIndex().x==x&&monster.getActor().getTilePosIndex().y==y){
                isEnemy = true;
            }
        }
        return isEnemy;
    }

    @Override
    public boolean hasUnit(int x, int y) {
        return false;
    }

    @Override
    public void stopMoving() {
        super.stopMoving();
        if(path.size()-1 <= pathIndex){
        }else{
//            Logic.getInstance().beginRound(path.get(path.size()-1).getX(),path.get(path.size()-1).getY());
        }
    }

    @Override
    protected void attackUnit(int curPos) {
        super.attackUnit(curPos);
        Ghost ghost = null;
        for (Monster monster : Logic.getInstance().getMonsterArray()){
            if(monster.getActor().getTilePosIndex().x==path.get(curPos).getX()&&monster.getActor().getTilePosIndex().y==path.get(curPos).getY()){
                ghost = (Ghost)monster;
                break;
            }
        }
        if(ghost != null){
            attackCalculate(this,ghost);
        }
    }

    @Override
    protected void attackCalculate(Character attacker, Character defender) {
        super.attackCalculate(attacker, defender);
    }

    @Override
    protected void getItem(Item item) {
        super.getItem(item);
        Logic.getInstance().sendLogMessage("真好吃，生命恢复了"+item.getInfo().getHealthPoint()+"点",LogMessageTable.TYPE_GOOD);
        int hp = getInfo().getHealthPoint()+item.getInfo().getHealthPoint();
        this.getInfo().setHealthPoint(hp);
        this.setInfo(this.getInfo());
        item.getInfo().setHealthPoint(0);
    }

    @Override
    public void setInfo(CharacterInfo characterInfo) {
        super.setInfo(characterInfo);
        GameFileHelper.getInstance().setPlayerInfo((PlayerInfo) characterInfo);
    }

    private PlayerActionListener playerActionListener;

    public void setPlayerActionListener(PlayerActionListener playerActionListener) {
        this.playerActionListener = playerActionListener;
    }
    public interface PlayerActionListener{
        void move(int action,int x,int y);
    }
}
