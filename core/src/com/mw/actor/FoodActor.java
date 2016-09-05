package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by BanditCat on 2016/9/5.
 */
public class FoodActor extends ItemActor {
    public FoodActor(TextureAtlas textureAtlas, String regionName, OrthographicCamera cam) {
        super(textureAtlas, regionName, cam);
    }
}
