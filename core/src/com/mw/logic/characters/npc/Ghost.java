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
    public void findWay(int x, int y) {
        super.findWay(x, y);
    }


    @Override
    public boolean hasEnemy(int x, int y) {
        boolean isEnemy=false;
        if(Logic.getInstance().getPlayer().getActor().getTilePosIndex().x==x
                &&Logic.getInstance().getPlayer().getActor().getTilePosIndex().y==y){
            isEnemy = true;
        }
        return isEnemy;
    }

    @Override
    public boolean hasUnit(int x, int y) {
        boolean isUnit=false;
        for (Monster monster : Logic.getInstance().getMonsterArray()){
            if(monster.getActor().getTilePosIndex().x==x&&monster.getActor().getTilePosIndex().y==y){
                isUnit = true;
            }
        }
        return isUnit;
    }

}
