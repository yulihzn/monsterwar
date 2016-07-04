package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mw.map.DungeonMap;
import com.mw.utils.Dungeon;

/**
 * Created by BanditCat on 2015/12/24.
 */
public class Player extends Character{
    public Player(TextureAtlas textureAtlas, String regionName, OrthographicCamera cam,DungeonMap dungeonMap) {
        super(textureAtlas, regionName, cam,dungeonMap);
    }

    @Override
    protected void moveLogic(int x, int y) {
        super.moveLogic(x, y);
        //上下
        if(dungeonMap.getDungeonArray()[x][y] == Dungeon.tileUpStairs){
        }else if(dungeonMap.getDungeonArray()[x][y] == Dungeon.tileDownStairs){

        }
    }
}
