package com.mw.ui.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mw.utils.AssetManagerHelper;

/**
 * Created by BanditCat on 2016/10/27.
 */

public class ShadowImage extends Image {

    public ShadowImage(TextureRegion region) {
        super(region);
        setOrigin(16,16);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = batch.getColor();
        super.draw(batch, parentAlpha);
        batch.setColor(color);
    }

    public void dispose(){

    }
}
