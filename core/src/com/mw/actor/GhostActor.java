package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mw.logic.Logic;
import com.mw.logic.characters.base.Character;
import com.mw.logic.characters.base.Monster;
import com.mw.map.DungeonMap;

/**
 * Created by BanditCat on 2016/8/26.
 */
public class GhostActor extends CharacterActor {
    public GhostActor(Character character,TextureAtlas textureAtlas, String regionName, OrthographicCamera cam, DungeonMap dungeonMap) {
        super(character,textureAtlas, regionName, cam, dungeonMap);
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
