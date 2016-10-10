package com.mw.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mw.game.MainGame;
import com.mw.logic.Logic;
import com.mw.map.MapEditor;
import com.mw.model.LogModel;

/**
 * Created by BanditCat on 2016/9/7.
 */
public class WorldMapTable extends Table {
    private FreeTypeFontGenerator generator;
    private LazyBitmapFont bitmapFont;
    private Texture texture;
    private ScrollPane scrollPane;
    private Color[]colors = {Color.LIGHT_GRAY,Color.YELLOW,Color.RED,Color.GREEN};


    public WorldMapTable() {
        init();
    }

    private void init() {
        int w = MainGame.worldWidth;
        int h = MainGame.worldHeight;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/font.ttf"));
        bitmapFont = new LazyBitmapFont(generator,8);

        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        texture = new Texture(w, h, Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        pixmap.setColor(new Color(0,0,0,1f));
        pixmap.fillRectangle(0,0,w,h);
        texture.draw(pixmap, 0, 0);
        TextureRegion textureRegion = new TextureRegion(texture,w,h);
        setBackground(new TextureRegionDrawable(textureRegion));
        setPosition(0,0);
        setWidth(w);
        setHeight(h);
        Table table = new Table();
        scrollPane = new ScrollPane(table);
        table.pad(5).defaults().expandX().space(5);
        add(scrollPane).expand().fill().colspan(5);
        row().space(5).padBottom(5);
        MapEditor mapEditor = new MapEditor();
        mapEditor.Create();
        String s = mapEditor.getArrayString();
        Label label = new Label(s,new Label.LabelStyle(bitmapFont, Color.WHITE));
        table.add(label).expandX().fillX();
    }
    public void dispose(){
        texture.dispose();
        bitmapFont.dispose();
        generator.dispose();

    }
}
