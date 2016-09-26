package com.mw.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mw.game.MainGame;
import com.mw.model.LogModel;

/**
 * Created by BanditCat on 2016/9/20.
 */
public class InventoryTable extends Table {
    private Texture texture;
    private ScrollPane scrollPane;
    private Array<Array<Image>> images = new Array<Array<Image>>();
    private TextureAtlas textureAtlas;
    private int len = 0;
    private FloatingTable floatingTable;
    public InventoryTable() {
        textureAtlas = new TextureAtlas(Gdx.files.internal("tiles.pack"));
        int w = MainGame.worldWidth /3;
        int h = MainGame.worldHeight/3;
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
        Table table = new Table();
        scrollPane = new ScrollPane(table);
        table.pad(5).defaults().expandX().space(5);
        add(scrollPane).expand().fill().colspan(5);
        row().space(5).padBottom(5);
        images.add(new Array<Image>());
        len = w/32;
        for (int i = 0; i < 200 ; i++) {
            addItem();
        }
        floatingTable = new FloatingTable();
        table.addActor(floatingTable);
    }
    private void addItem(){
        final Table table = ((Table)scrollPane.getWidget());
        final Image image = new Image(textureAtlas.findRegion("bottle1"));
        image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                floatingTable.show(image.getX()+x,image.getY()+y);
            }
        });
        boolean needNew = false;
        for (Array<Image> array:images){
            needNew = true;
            if(array.size<len){
                array.add(image);
                table.add(image);
                needNew = false;
                break;
            }
        }
        if(needNew){
            Array<Image> array = new Array<Image>();
            array.add(image);
            table.row();
            table.add(image);
            images.add(array);
        }
    }
    public void dispose(){
        texture.dispose();
        textureAtlas.dispose();
        floatingTable.dispose();
    }
}
