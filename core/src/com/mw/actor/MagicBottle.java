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
import com.mw.utils.AssetManagerHelper;

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
    private String[]names = {"i0","i1","i2","i3","i4"};
    private int maxNum = 100;
    private int curNum = 0;
    private Image image;
    private LazyBitmapFont bitmapFont;
    private Label text;
    private Sprite sprite;
    private float bottle_h;
    private float bottle_w;
    private float bottle_y;
    private float bottle_x;
    private Texture texture;
    private Pixmap pixmap;

    public MagicBottle(int type,LazyBitmapFont bitmapFont) {
        this.bitmapFont = bitmapFont;
        this.type = type;
        textureAtlas = AssetManagerHelper.getInstance().getTextureAtlas("item");
        image = new Image(AssetManagerHelper.getInstance().findRegion(names[type]));
        text = new Label("",new Label.LabelStyle(bitmapFont,Color.WHITE));
        image.setScale(2);
        image.setPosition(0,0);
        add(image);
        add(text);
        sprite = new Sprite(AssetManagerHelper.getInstance().findRegion(names[4]));
        sprite.setScale(1.9f);
        sprite.setColor(colors[type]);
        bottle_h = sprite.getRegionHeight();
        bottle_w = sprite.getRegionWidth();
        bottle_y = sprite.getRegionY();
        bottle_x = sprite.getRegionX();
        pixmap = new Pixmap(sprite.getRegionWidth()*2, sprite.getRegionHeight()*2, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        // Create a texture to contain the pixmap
        texture = new Texture(pixmap.getWidth(), pixmap.getWidth(), Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        pixmap.setColor(Color.PINK);
        pixmap.fillRectangle(0,0,pixmap.getWidth(),pixmap.getHeight());
        texture.draw(pixmap,0,0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float x = getParent().getX()+getX();
        float y = getParent().getY()+getY();
        sprite.draw(batch);//画颜色
        batch.draw(texture,x,y);//画覆盖物
        super.draw(batch, parentAlpha);
    }

    public int getCurNum() {
        return curNum;
    }

    public void setCurNum(int curNum) {
        this.curNum = curNum;
        float h = pixmap.getHeight()-pixmap.getHeight()*curNum/maxNum-5;
        pixmap.setColor(new Color(0,0,0,0));
        pixmap.fillRectangle(0,0,pixmap.getWidth(),pixmap.getHeight());
        pixmap.setColor(Color.BLACK);
        pixmap.fillRectangle(0,0,pixmap.getWidth(),(int)(h>0?h:0));
        texture.draw(pixmap, 0, 0);
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
            float h = bottle_h*curNum/maxNum;
            float x = getParent().getX()+getX()+sprite.getRegionWidth()/2;
            float y = getParent().getY()+getY()+sprite.getRegionHeight()/2;
            sprite.setPosition(x,y);
        }
    }

    public void dispose(){
        pixmap.dispose();
        texture.dispose();
    }
}
