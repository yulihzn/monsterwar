package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mw.map.DungeonMap;

/**
 * Created by BanditCat on 2015/12/24.
 */
public class Player extends Character{
    public Player(TextureAtlas textureAtlas, String regionName, OrthographicCamera cam,DungeonMap dungeonMap) {
        super(textureAtlas, regionName, cam,dungeonMap);
    }
}
