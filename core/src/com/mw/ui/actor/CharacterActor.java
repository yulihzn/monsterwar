package com.mw.ui.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.TimeUtils;
import com.mw.logic.characters.base.Character;

/**
 * Created by BanditCat on 2016/6/7.
 */
public class CharacterActor extends GameMapTile {

    protected long roundTime = TimeUtils.nanoTime();//回合时间用来和时间间隔对比
    protected boolean isFocus = false;//是否镜头跟随
    protected OrthographicCamera camera;


    protected Character character;
    public CharacterActor(Character character ,String regionName, OrthographicCamera cam) {
        super(regionName, cam);
        this.character = character;
        this.camera = cam;
    }





    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

}
