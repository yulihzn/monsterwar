package com.mw.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by BanditCat on 2016/3/23 0023.
 */
public class MagicBottle extends Actor {
    public static final int SWORD = 0;
    public static final int WAND = 1;
    public static final int CUP = 2;
    public static final int COIN = 3;
    private Texture texture;
    private Pixmap pixmap;
    private int type = -1;
    private Color[]colors={Color.GREEN,Color.RED,Color.YELLOW,Color.GOLD};
    private int maxNum = 100;
    private int curNum = 0;

    public MagicBottle(int type) {
        this.type = type;
        int w = 512;
        int h = 512;
        pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        texture = new Texture(w, h, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        texture.draw(pixmap, 0, 0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //混合模式
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(texture,getX(),getY());
        super.draw(batch, parentAlpha);
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void invalidate(){
        pixmap.setColor(new Color(255,255,255,0.9f));
        pixmap.fillRectangle(0,0,(int)getWidth(),(int)getHeight());
        pixmap.setColor(colors[type]);
        float h = getHeight()*(float)curNum/(float)maxNum;
        pixmap.fillRectangle(0,0,(int)getWidth(),(int)h);
        texture.draw(pixmap,0,0);
    }

    public int getCurNum() {
        return curNum;
    }

    public void setCurNum(int curNum) {
        this.curNum = curNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void dispose(){
        pixmap.dispose();
        texture.dispose();
    }
}
