package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mw.logic.characters.base.Character;
import com.mw.map.DungeonMap;

/**
 * Created by BanditCat on 2015/12/24.
 */
public class PlayerActor extends CharacterActor {


    public PlayerActor(Character character, String regionName, OrthographicCamera cam, DungeonMap dungeonMap) {
        super(character,regionName, cam,dungeonMap);
    }


}
