package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by hezhengnan on 2015/12/8.
 */
public class CamImage extends Image {
    private OrthographicCamera cam;

    public CamImage(Texture texture, OrthographicCamera cam) {
        super(texture);
        this.cam = cam;
        setHeight(texture.getHeight());
        setWidth(texture.getWidth());
    }
    public CamImage(TextureRegion texture, OrthographicCamera cam) {
        super(texture);
        this.cam = cam;
        setHeight(texture.getRegionHeight());
        setWidth(texture.getRegionWidth());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setProjectionMatrix(cam.combined);
        super.draw(batch, parentAlpha);
    }
}
