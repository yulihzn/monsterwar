package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mw.logic.characters.base.Character;

/**
 * Created by BanditCat on 2015/12/24.
 */
public class PlayerActor extends CharacterActor {


    public PlayerActor(Character character, String regionName, OrthographicCamera cam) {
        super(character,regionName, cam);
    }


}
