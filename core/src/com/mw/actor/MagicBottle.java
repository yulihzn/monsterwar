package com.mw.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mw.ui.LazyBitmapFont;

/**
 * Created by BanditCat on 2016/3/23 0023.
 */
public class MagicBottle extends Table {
    public static final int SWORD = 0;
    public static final int WAND = 1;
    public static final int CUP = 2;
    public static final int COIN = 3;
    private TextureAtlas textureAtlas;
    private int type = -1;
    private Color[]colors={Color.GREEN,Color.RED,Color.YELLOW,Color.GOLD};
    private String[]names = {"bottle0","bottle1","bottle2","bottle3","bottle4"};
    private int maxNum = 100;
    private int curNum = 0;
    private Image image;
    private LazyBitmapFont bitmapFont;
    private Label text;
    private Sprite sprite;

    public MagicBottle(int type,LazyBitmapFont bitmapFont) {
        this.bitmapFont = bitmapFont;
        this.type = type;
        textureAtlas = new TextureAtlas(Gdx.files.internal("tiles.pack"));
        image = new Image(textureAtlas.findRegion(names[type]));
        text = new Label("",new Label.LabelStyle(bitmapFont,Color.WHITE));
        image.setScale(2);
        image.setPosition(0,0);
        add(image);
        add(text);
        sprite = new Sprite(textureAtlas.findRegion(names[4]));
        sprite.setScale(2);
        sprite.setColor(colors[type]);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
        super.draw(batch, parentAlpha);
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
        text.setText(curNum+"/"+maxNum);
        text.setPosition(getWidth()/2-text.getGlyphLayout().width/2,getHeight()/2);
        if(getParent() != null){
            float h = sprite.getHeight()*curNum/maxNum;
            sprite.setBounds(getParent().getX()+getX()+sprite.getRegionWidth()/2,getParent().getY()+getY()+sprite.getRegionHeight()/2
                    ,sprite.getWidth(),10);
        }
    }

    public void dispose(){
        textureAtlas.dispose();
    }
}
