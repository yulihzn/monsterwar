package com.mw.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by BanditCat on 2016/9/20.
 */
public class PlayerAvatarTable extends Table {
    private Texture texture;
    private TextureAtlas textureAtlas;
    public PlayerAvatarTable() {
        textureAtlas = new TextureAtlas(Gdx.files.internal("tiles.pack"));
        int w = Gdx.graphics.getWidth()/4;
        int h = Gdx.graphics.getHeight()/4;
        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        texture = new Texture(w, h, Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        pixmap.setColor(new Color(255,255,255,0.2f));
        pixmap.fillRectangle(0,0,w,h);
        texture.draw(pixmap, 0, 0);
        pixmap.dispose();
        setBackground(new TextureRegionDrawable(new TextureRegion(texture,w,h)));
        setPosition(0,0);
        setWidth(w);
        setHeight(h);
        Image image = new Image(textureAtlas.findRegion("man"));
        image.setScale(5);
        image.setPosition(0,0);
        add(image);
    }
    public void dispose(){
        texture.dispose();
        textureAtlas.dispose();
    }
}
