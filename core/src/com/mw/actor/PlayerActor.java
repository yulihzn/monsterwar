package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mw.logic.Logic;
import com.mw.logic.characters.base.Character;
import com.mw.logic.characters.base.Monster;
import com.mw.logic.characters.info.PlayerInfo;
import com.mw.map.DungeonMap;
import com.mw.utils.Dungeon;
import com.mw.utils.GameDataHelper;

/**
 * Created by BanditCat on 2015/12/24.
 */
public class PlayerActor extends CharacterActor {


    public PlayerActor(Character character, TextureAtlas textureAtlas, String regionName, OrthographicCamera cam, DungeonMap dungeonMap) {
        super(character,textureAtlas, regionName, cam,dungeonMap);
    }


}
