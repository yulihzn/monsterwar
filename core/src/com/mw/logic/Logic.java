package com.mw.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mw.logic.characters.base.Monster;

/**
 * Created by BanditCat on 2016/8/10.
 */
public class Logic {
    private static Logic inStance = null;
    private Logic(){
        init();
    }

    private void init() {
    }

    public static Logic getInstance(){
        if(inStance == null){
            inStance = new Logic();
        }
        return inStance;
    }
    private Array<Monster> monsterArray = new Array<Monster>();

    public Array<Monster> getMonsterArray() {
        return monsterArray;
    }
}
