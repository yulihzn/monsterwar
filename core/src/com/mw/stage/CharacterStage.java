package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mw.actor.MagicBottle;
import com.mw.game.MainGame;
import com.mw.screen.MainScreen;
import com.mw.ui.InventoryTable;
import com.mw.ui.LazyBitmapFont;
import com.mw.ui.PlayerAvatarTable;
import com.mw.ui.WorldMapTable;


/**
 * Created by BanditCat on 2016/7/13.
 */
public class CharacterStage extends Stage {
    private FreeTypeFontGenerator generator;
    private LazyBitmapFont bitmapFont;

    private Texture texture;
    private Table backGround;
    private InventoryTable inventoryTable;
    private PlayerAvatarTable playerAvatarTable;
    private WorldMapTable worldMapTable;
    public static final float camSize = 16;
    private OrthographicCamera camera;
    private boolean isVisible = false;

    private MainScreen mainScreen;

    private Array<MagicBottle> bottles = new Array<MagicBottle>();


    public CharacterStage(final MainScreen mainScreen) {
        setDebugUnderMouse(true);
        this.mainScreen = mainScreen;
        setViewport(new FitViewport(MainGame.worldWidth,MainGame.worldHeight));
        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/font.ttf"));
        bitmapFont = new LazyBitmapFont(generator,10);
        addBackGround();
    }

    private void addInventory() {
        inventoryTable = new InventoryTable();
        backGround.add(inventoryTable).top().right();
    }
    private void addAvatar() {
        playerAvatarTable = new PlayerAvatarTable();
        backGround.add(playerAvatarTable).center();
    }

    private void addBackGround(){
        int w = MainGame.worldWidth/10;
        int h = MainGame.worldHeight-MainGame.worldHeight/10;
        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        texture = new Texture(w, h, Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        pixmap.setColor(new Color(0,0,0,0.9f));
        pixmap.fillRectangle(0,0,w,h);
        texture.draw(pixmap, 0, 0);
        pixmap.dispose();
        backGround = new Table();
        backGround.setWidth(w);
        backGround.setHeight(h);
        backGround.setPosition(MainGame.worldWidth/10,MainGame.worldHeight);
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
        addAvatar();
        addInventory();
        camera = new OrthographicCamera(camSize,camSize*(h/w));
        camera.zoom = 1.0f;
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        worldMapTable = new WorldMapTable(camera);
        worldMapTable.setPosition(0,0);
//        addActor(worldMapTable);
    }

    private void addMagicBottle(){
        int w = MainGame.worldWidth/10;
        int h = w*2;
        bottles.clear();
        bottles.add(new MagicBottle(MagicBottle.SWORD,bitmapFont));
        bottles.add(new MagicBottle(MagicBottle.WAND,bitmapFont));
        bottles.add(new MagicBottle(MagicBottle.CUP,bitmapFont));
        bottles.add(new MagicBottle(MagicBottle.COIN,bitmapFont));
        backGround.left().bottom();
        for (int i = 0; i < bottles.size; i++) {
            MagicBottle m = bottles.get(i);
            m.setBounds(50*i,0,50,80);
            m.setCurNum(i*30);
            backGround.add(m).bottom().left();
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
                m.setCurNum(i*30);
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
        int w = MainGame.worldWidth-MainGame.worldHeight/10;
        int h = MainGame.worldHeight-MainGame.worldHeight/10;
        backGround.setWidth(w);
        backGround.setHeight(h);
        backGround.setPosition(MainGame.worldWidth/20,MainGame.worldHeight/20);


    }

    @Override
    public void dispose() {
        super.dispose();
        generator.dispose();
        bitmapFont.dispose();
        inventoryTable.dispose();
        playerAvatarTable.dispose();
        for (MagicBottle m:bottles){
            m.dispose();
        }
    }
    @Override
    public boolean scrolled(int amount) {
        Gdx.app.log("scrolled",""+amount);
        if(amount == -1){
            camera.zoom += 0.1f;
        }else if(amount == 1){
            camera.zoom -= 0.1f;
        }
        if(camera.zoom < 0.1f){
            camera.zoom = 0.1f;
        }
        if(camera.zoom > 20.0f){
            camera.zoom = 20.0f;
        }
        Gdx.app.log("zoom",""+camera.zoom);
        return super.scrolled(amount);
    }

    @Override
    public boolean keyDown(int keyCode) {
//        switch (KeyBoardController.getInstance().getKeyType(keyCode)){
//            case KeyBoardController.UP:
//                camera.translate(0,1,0);
//                break;
//            case KeyBoardController.DOWN:
//                camera.translate(0,-1,0);
//                break;
//            case KeyBoardController.LEFT:
//                camera.translate(-1,0,0);
//                break;
//            case KeyBoardController.RIGHT:
//                camera.translate(1,0,0);
//                break;
//            case KeyBoardController.SPACE:
//                camera.position.set(128,128,0);
//                break;
//
//        }
        return super.keyDown(keyCode);
    }

    @Override
    public OrthographicCamera getCamera() {
        return camera;
    }
}
