package com.mw.logic.characters.base;

import com.mw.logic.Logic;
import com.mw.logic.characters.info.PlayerInfo;
import com.mw.map.DungeonMap;
import com.mw.stage.MapStage;
import com.mw.utils.Dungeon;
import com.mw.utils.GameDataHelper;

/**
 * Created by BanditCat on 2016/7/25.
 */
public class Player extends Character {
    public static final int ACTION_DOWN = 1;
    public static final int ACTION_UP = 0;
    public Player(DungeonMap dungeonMap) {
        super(dungeonMap);
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
        GameDataHelper.getInstance().setCurrentStep(PlayerInfo.NAME,GameDataHelper.getInstance().getCurrentStep(PlayerInfo.NAME)+1);
        if(getActor().getTilePosIndex().x == x&&getActor().getTilePosIndex().y==y){
            if(playerActionListener != null){
                if(dungeonMap.getMapInfo().getMapArray()[x][y].getBlock() == Dungeon.tileUpStairs){
                    playerActionListener.move(ACTION_UP,x,y);
                }else if(dungeonMap.getMapInfo().getMapArray()[x][y].getBlock() == Dungeon.tileDownStairs){
                    playerActionListener.move(ACTION_DOWN,x,y);
                }
            }
        }
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
            GameDataHelper.getInstance().saveGameMap(dungeonMap.getMapInfo(),GameDataHelper.getInstance().getCurrentLevel());
        }else{
            Logic.getInstance().beginRound(path.get(path.size()-1).getX(),path.get(path.size()-1).getY());
        }
    }

    private PlayerActionListener playerActionListener;

    public void setPlayerActionListener(PlayerActionListener playerActionListener) {
        this.playerActionListener = playerActionListener;
    }
    public interface PlayerActionListener{
        void move(int action,int x,int y);
    }
}
