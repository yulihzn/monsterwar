package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mw.actor.MagicBottle;
import com.mw.screen.MainScreen;
import com.mw.ui.LazyBitmapFont;


/**
 * Created by BanditCat on 2016/7/13.
 */
public class CharacterStage extends Stage {
    private FreeTypeFontGenerator generator;
    private LazyBitmapFont bitmapFont;

    private Texture texture;
    private Table backGround;
    private boolean isVisible = false;

    private MainScreen mainScreen;
    private TextureAtlas textureAtlas;

    private Array<MagicBottle> bottles = new Array<MagicBottle>();


    public CharacterStage(final MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        setViewport(new ScreenViewport());
        setDebugAll(true);
        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/font.ttf"));
        bitmapFont = new LazyBitmapFont(generator,14);
        textureAtlas = new TextureAtlas(Gdx.files.internal("tiles.pack"));
        addBackGround();
    }
    private void addBackGround(){
        int w = Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/10;
        int h = Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/10;
        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        texture = new Texture(w, h, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        pixmap.setColor(new Color(0,0,0,0.9f));
        pixmap.fillRectangle(0,0,w,h);
        texture.draw(pixmap, 0, 0);
        backGround = new Table();
        backGround.setWidth(w);
        backGround.setHeight(h);
        backGround.setPosition(Gdx.graphics.getWidth()/10,Gdx.graphics.getWidth()/10);
        backGround.setVisible(isVisible);
        TextureRegion textureRegion = new TextureRegion(texture,w,h);
        backGround.setBackground(new TextureRegionDrawable(textureRegion));
        backGround.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
//                mainScreen.hideCharacterStage();
                super.clicked(event, x, y);
            }

        });
        addActor(backGround);
        addMagicBottle();
    }

    private void addMagicBottle(){
        int w = Gdx.graphics.getWidth()/10;
        int h = w*2;
        bottles.clear();
        bottles.add(new MagicBottle(MagicBottle.SWORD));
        bottles.add(new MagicBottle(MagicBottle.WAND));
        bottles.add(new MagicBottle(MagicBottle.CUP));
        bottles.add(new MagicBottle(MagicBottle.COIN));
        backGround.left().bottom();
        for (int i = 0; i < bottles.size; i++) {
            MagicBottle m = bottles.get(i);
            m.setBounds(50*i,0,50,80);
            m.setCurNum(i*10);
            m.invalidate();
            backGround.add(m);
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
        backGround.setVisible(visible);
        if(isVisible){
            for (int i = 0; i < bottles.size; i++) {
                MagicBottle m = bottles.get(i);
                m.setBounds(50*i,0,50,80);
                m.setCurNum(i*10);
                m.invalidate();
            }
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(screenX>backGround.getX()&&screenX<backGround.getX()+backGround.getWidth()
                &&screenY>backGround.getY()&&screenY<backGround.getY()+backGround.getHeight()){

        }else{
            mainScreen.hideCharacterStage();
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public void act(float delta) {
        if(!isVisible){
            return;
        }
        super.act(delta);
    }

    @Override
    public void draw() {
        if(!isVisible){
            return;
        }
        super.draw();
        int w = Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/10;
        int h = Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/10;
        backGround.setWidth(w);
        backGround.setHeight(h);
        backGround.setPosition(Gdx.graphics.getWidth()/20,Gdx.graphics.getHeight()/20);


    }

    @Override
    public void dispose() {
        super.dispose();
        generator.dispose();
        bitmapFont.dispose();
        textureAtlas.dispose();
        for (MagicBottle m:bottles){
            m.dispose();
        }
    }


}
