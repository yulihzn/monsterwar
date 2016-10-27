package com.mw.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mw.utils.AssetManagerHelper;

/**
 * Created by BanditCat on 2016/10/27.
 */

public class AreaShadow extends Actor {
    private OrthographicCamera camera;
    private Pixmap pixmap;
    private Texture texture;
    public AreaShadow(OrthographicCamera camera) {
        int width = 320;
        int height = 320;
        this.camera = camera;
        pixmap = new Pixmap(width,height, Pixmap.Format.RGB888);
        texture = new Texture(width,height, Pixmap.Format.RGB888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        pixmap.setColor(new Color(255,255,255,0.5f));
        pixmap.fillRectangle(0,0,width,height);
//        pixmap.setColor(new Color(0,0,0,0f));
//        pixmap.drawRectangle(0,0,5,5);
        texture.draw(pixmap, 0, 0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //混合模式
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(camera.combined);
        batch.draw(texture, getX(), getY(),10,10);
        super.draw(batch, parentAlpha);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        Gdx.gl.glColorMask(false, false, false, true);
        int dst = batch.getBlendDstFunc();
        int src = batch.getBlendSrcFunc();
    }

    public void dispose(){

    }
}
