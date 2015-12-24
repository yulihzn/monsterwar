package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by BanditCat on 2015/12/24.
 */
public class Player extends CamImage {
    public Player(Texture texture, OrthographicCamera cam) {
        super(texture, cam);
    }
    public Player(TextureRegion texture, OrthographicCamera cam) {
        super(texture, cam);
    }
}
