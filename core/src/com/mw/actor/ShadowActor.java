package com.mw.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;

/**
 * Created by BanditCat on 2016/10/31.
 */

public class ShadowActor extends Actor{
    private OrthographicCamera camera;
    private Pixmap pixmap;
    private Texture texture;
    private int width = 256;
    private int height = 256;
    private Color sightColor = new Color(0,0,0,0f);
    private Color fadeColor = new Color(0,0,0,0.5f);
    private int x0=-1,y0=-1;
    private int shadowRange = 7;

    public ShadowActor(OrthographicCamera camera) {
        this.camera = camera;
        pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        texture = new Texture(width, height, Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
//        pixmap.setColor(new Color(0,0,0,0.8f));
//        pixmap.fill();
//        pixmap.setColor(new Color(0,0,0,1f));
//        pixmap.fillRectangle(0,0,1,1);
        texture.draw(pixmap, 0, 0);
        ColorAction colorAction = new ColorAction();
        colorAction.setColor(fadeColor);
        colorAction.setEndColor(new Color(0,0,0,0.5f));
        colorAction.setDuration(1);
        addAction(colorAction);
        ColorAction colorAction1 = new ColorAction();
        colorAction1.setColor(sightColor);
        colorAction1.setEndColor(new Color(0,0,0,0f));
        colorAction1.setDuration(1);
        addAction(colorAction1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //混合模式
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(camera.combined);
        batch.draw(texture,getX(),getY());
        super.draw(batch, parentAlpha);
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
    public void drawShadow(int x,int y){
        Pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(new Color(0,0,0,1f));
        pixmap.fill();
        if(x0 !=-1||y0!=-1){
            pixmap.setColor(fadeColor);
            pixmap.fillCircle(x0,height-y0,shadowRange);
        }
        pixmap.setColor(sightColor);
        pixmap.fillCircle(x,height-y,(int)(shadowRange*(1-sightColor.a)));
        texture.draw(pixmap,0,0);
        x0=x;y0=y;
    }
}
