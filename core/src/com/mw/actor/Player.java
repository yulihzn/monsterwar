package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by BanditCat on 2015/12/24.
 */
public class Player extends GameMapTile{
    public Player(TextureAtlas textureAtlas, String regionName, OrthographicCamera cam) {
        super(textureAtlas, regionName, cam);
    }
}
