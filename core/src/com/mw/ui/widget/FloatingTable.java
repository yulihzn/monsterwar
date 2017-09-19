package com.mw.ui.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mw.utils.AssetManagerHelper;

/**
 * Created by BanditCat on 2016/9/20.
 */
public class FloatingTable extends Table {
    private FreeTypeFontGenerator generator;
    private LazyBitmapFont bitmapFont;
    private Texture texture;
    private TextureAtlas textureAtlas;
    private GameInfoLabel label;
    public FloatingTable() {
        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/font.ttf"));
        bitmapFont = new LazyBitmapFont(generator,14);
        textureAtlas = AssetManagerHelper.getInstance().getTextureAtlas("item");
        int w = 100;
        int h = 100;
        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        texture = new Texture(w, h, Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        pixmap.setColor(new Color(255,255,255,0.6f));
        pixmap.fillRectangle(0,0,w,h);
        texture.draw(pixmap, 0, 0);
        pixmap.dispose();
        setBackground(new TextureRegionDrawable(new TextureRegion(texture,w,h)));
        setPosition(0,0);
        setWidth(w);
        setHeight(h);
        Image image = new Image(AssetManagerHelper.getInstance().findRegion("i0001"));
        label = new GameInfoLabel("test",new Label.LabelStyle(bitmapFont,Color.BLACK));
        add(image).center();
        row();
        add(label).center();

    }
    public void show(float x,float y){
        setVisible(true);
        addAction(Actions.fadeIn(0.01f));
        setPosition(x,y);
        label.setText(x+" "+y);
        hide();
    }
    public void hide(){
        addAction(Actions.delay(1,Actions.sequence(Actions.fadeOut(0.5f),Actions.run(new Runnable() {
            @Override
            public void run() {
                setVisible(false);
            }
        }))));
    }
    public void dispose(){
        texture.dispose();
        bitmapFont.dispose();
    }
}
