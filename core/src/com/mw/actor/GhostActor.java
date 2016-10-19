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
    public GhostActor(Character character, String regionName, OrthographicCamera cam, DungeonMap dungeonMap) {
        super(character, regionName, cam, dungeonMap);
    }


}
