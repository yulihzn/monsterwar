package com.mw.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mw.utils.Dungeon;

import java.util.HashMap;

/**
 * Created by BanditCat on 2015/12/8.
 */
public class TestMap extends Actor {
    private OrthographicCamera cam;
    private TextureAtlas textureAtlas;
    private Dungeon dungeon;
    public static final int ysize = 32,xsize = 32;

    private String[][] names = new String[xsize][ysize];


    private HashMap<String,Vector2> creaturePos = new HashMap<String, Vector2>();

    public TestMap(OrthographicCamera cam) {
        this.cam = cam;
        textureAtlas = new TextureAtlas(Gdx.files.internal("tiles.pack"));
//        setDungeon();
        addListener(inputListener);


    }

    public void setDungeon(){
        setWidth((xsize-1)<<5);
        setHeight((ysize-1)<<5);
        dungeon = new Dungeon();
        dungeon.createDungeon(xsize,ysize,5000);
        Gdx.app.log("", dungeon.showDungeon());
        int[][] array = dungeon.getDungeonArray();
        names = new String[xsize][ysize];
        for (int y = 0; y < ysize; y++) {
            for (int x = 0; x < xsize; x++) {
                String name = "";
                switch(array[x][y]) {
                    case Dungeon.tileUnused:name="empty-original";break;
                    case Dungeon.tileDirtWall:name="block01-original"; break;
                    case Dungeon.tileDirtFloor:name="sand01-original"; break;
                    case Dungeon.tileStoneWall:name="stone-original"; break;
                    case Dungeon.tileCorridor:name="sand01-original"; break;
                    case Dungeon.tileDoor:name="sand01-original"; break;
                    case Dungeon.tileUpStairs:name="grass02-original"; break;
                    case Dungeon.tileDownStairs:name="grass02-original"; break;
                    case Dungeon.tileChest:name="cup02-original"; break;
                }
                names[x][y] = name;
            }
        }

    }

    /**
     * 设置生物位置
     * @param name 生物名字
     * @param x 数组坐标
     * @param y 数组坐标
     */
    public void setCreaturePos(String name,int x,int y){
        if(x >= xsize){
            x = xsize-1;
        }
        if(x < 0){
            x = 0;
        }
        if(y >= ysize){
            y = ysize-1;
        }
        if(y < 0){
            y = 0;
        }
        creaturePos.put(name,new Vector2(x<<5,y<<5));
    }
    public  Vector2 getCreaturePos(String name){
        return creaturePos.get(name);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        batch.end();
//        中间用来添加在batch之外的绘制，不推荐用
//        batch.begin();

        super.draw(batch, parentAlpha);
        batch.setProjectionMatrix(cam.combined);
        for (int y = 0; y < ysize; y++) {
            for (int x = 0; x < xsize; x++) {
                batch.draw(textureAtlas.findRegion(names[x][y]),x<<5,y<<5);
            }
        }
    }

    private InputListener inputListener = new InputListener(){
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            return super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            super.touchUp(event, x, y, pointer, button);
            Gdx.app.log("touchUp","x="+x+"y="+y);
            elementTouch("man",x,y);
        }
    };

    private void elementTouch(String name,float x, float y) {
        boolean isTouched = false;
        if(getCreaturePos(name).x <= x && getCreaturePos(name).x + 32> x
                &&getCreaturePos(name).y <= y && getCreaturePos(name).y + 32> y){
            isTouched = true;
        }
        Gdx.app.log("elementTouch","getCreaturePos(name).x="+getCreaturePos(name).x+"getCreaturePos(name).y="+getCreaturePos(name).y);
        if(isTouched){
            Gdx.app.log("elementTouch",name);
        }
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return super.hit(x, y, touchable);
    }
}
