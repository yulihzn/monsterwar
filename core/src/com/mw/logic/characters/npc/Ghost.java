package com.mw.logic.characters.npc;

import com.mw.logic.Logic;
import com.mw.logic.characters.base.Monster;
import com.mw.map.DungeonMap;
import com.mw.stage.MapStage;

/**
 * Created by BanditCat on 2016/7/25.
 */
public class Ghost extends Monster {
    public Ghost(DungeonMap dungeonMap) {
        super(dungeonMap);
    }

    @Override
    public void doClick(MapStage mapStage, int x, int y) {
        super.doClick(mapStage, x, y);
        findWays(x,y);
    }
    @Override
    protected void moveLogic(int curPos) {
        super.moveLogic(curPos);
        if(curPos > 0){
            stopMoving();
        }
        //当列表的下一条是敌对npc且在攻击范围，攻击
        if(curPos+1 < path.size()){
            final int nextX = path.get(curPos+1).getX();
            final int nextY = path.get(curPos+1).getY();
            //碰到npc停下来
            if(hasUnit(nextX,nextY)){
                stopMoving();
            }
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
        if(Logic.getInstance().getPlayer().getActor().getTilePosIndex().x==x
                &&Logic.getInstance().getPlayer().getActor().getTilePosIndex().y==y){
            isEnemy = true;
        }
        return isEnemy;
    }

    @Override
    protected boolean hasUnit(int x, int y) {
        boolean isUnit=false;
        for (Monster monster : Logic.getInstance().getMonsterArray()){
            if(monster.getActor().getTilePosIndex().x==x&&monster.getActor().getTilePosIndex().y==y){
                isUnit = true;
            }
        }
        return isUnit;
    }
}
