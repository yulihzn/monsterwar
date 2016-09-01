package com.mw.logic.characters.base;

import com.mw.logic.Logic;
import com.mw.logic.characters.info.PlayerInfo;
import com.mw.map.AStarNode;
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
    public void doClick(MapStage mapStage, int x, int y) {
        super.doClick(mapStage, x, y);
        findWays(x,y);
    }

    @Override
    public void findWay(int x, int y) {
        super.findWay(x, y);
        aStarMap.setSource(new AStarNode(characterActor.getTilePosIndex().x,characterActor.getTilePosIndex().y));
        aStarMap.setTarget(new AStarNode(x,y));
        synchronized (path){
            path = aStarMap.find();
        }
    }

    @Override
    public void walk() {
        super.walk();
    }

    @Override
    protected void moveLogic(int curPos) {
        super.moveLogic(curPos);
        GameDataHelper.getInstance().setCurrentStep(PlayerInfo.NAME,GameDataHelper.getInstance().getCurrentStep(PlayerInfo.NAME)+1);
        //上下
        final int x = path.get(curPos).getX();
        final int y = path.get(curPos).getY();
        if(path.size() == 1){
            if(getActor().getTilePosIndex().x == x&&getActor().getTilePosIndex().y==y){
                if(playerActionListener == null){
                    return;
                }
                if(dungeonMap.getMapInfo().getMapArray()[x][y].getBlock() == Dungeon.tileUpStairs){
                    playerActionListener.move(ACTION_UP,x,y);
                }else if(dungeonMap.getMapInfo().getMapArray()[x][y].getBlock() == Dungeon.tileDownStairs){
                    playerActionListener.move(ACTION_DOWN,x,y);
                }
            }
        }
        //当列表的下一条是敌对npc且在攻击范围，攻击
        if(curPos+1 < path.size()){
            final int nextX = path.get(curPos+1).getX();
            final int nextY = path.get(curPos+1).getY();
            //碰到npc停下来
            if(hasEnemy(nextX,nextY)){
                stopMoving();
                if(curPos==0||curPos==1){
                    attackUnit(curPos);
                }
            }
        }
    }

    @Override
    protected boolean hasEnemy(int x, int y) {
        boolean isEnemy=false;
        for (Monster monster : Logic.getInstance().getMonsterArray()){
            if(monster.getActor().getTilePosIndex().x==x&&monster.getActor().getTilePosIndex().y==y){
                isEnemy = true;
            }
        }
        return isEnemy;
    }

    @Override
    public void stopMoving() {
        super.stopMoving();
        GameDataHelper.getInstance().saveGameMap(dungeonMap.getMapInfo(),GameDataHelper.getInstance().getCurrentLevel());
    }

    private PlayerActionListener playerActionListener;

    public void setPlayerActionListener(PlayerActionListener playerActionListener) {
        this.playerActionListener = playerActionListener;
    }
    public interface PlayerActionListener{
        void move(int action,int x,int y);
    }
}
