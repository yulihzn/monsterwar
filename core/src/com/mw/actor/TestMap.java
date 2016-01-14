package com.mw.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mw.utils.Dungeon;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by BanditCat on 2015/12/8.
 */
public class TestMap extends Actor {
    private OrthographicCamera cam;
    private static final int LAYERS = 5;
    private static final int WIDTH = 15;
    private static final int HEIGHT = 10;
    private Texture texture;
    private int[] layers = new int[LAYERS];
    private static final int TILES_PER_LAYER = WIDTH * HEIGHT;
    private static final int BLOCK_TILES = 25;
    private TextureAtlas textureAtlas;

    private Dungeon dungeon;
    private static final int ysize = 20,xsize = 20;
    private SpriteCache[] caches = new SpriteCache[LAYERS];
    private SpriteCache cache = new SpriteCache();
    private SpriteCache cache1 = new SpriteCache();
    private int cacheId = 0;
    private int cacheId1 = 0;
    private SpriteCache[][] mapcaches = new SpriteCache[10][10];
    private int[][] mapcacheids = new int[10][10];

    private HashMap<String,Vector2> creaturePos = new HashMap<String, Vector2>();

    public TestMap(OrthographicCamera cam) {
        this.cam = cam;
        texture = new Texture(Gdx.files.internal("tiles.png"));
        textureAtlas = new TextureAtlas(Gdx.files.internal("tiles.pack"));
        Random rand = new Random();
        for (int i = 0; i < LAYERS; i++) {
            caches[i] = new SpriteCache();
            SpriteCache cache = caches[i];
            cache.beginCache();
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    int tileX = rand.nextInt(5);
                    int tileY = rand.nextInt(5);
                    cache.add(texture, x << 5, y << 5, 1 + tileX * 33, 1 + tileY * 33, 32, 32);
                }
            }
            layers[i] = cache.endCache();
        }
//        setDungeon();
        addListener(inputListener);


    }

    public void setDungeon(){
        dungeon = new Dungeon();
        dungeon.createDungeon(xsize,ysize,5000);
        Gdx.app.log("", dungeon.showDungeon());
        int[][] array = dungeon.getDungeonArray();
        for(int i = 0;i < 10;i++){
            for (int j = 0;j < 10;j++){
                mapcaches[i][j] = new SpriteCache();
                SpriteCache cache = mapcaches[i][j];
                cache.beginCache();
            }
        }
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
                mapcaches[x/10][y/10].add(textureAtlas.findRegion(name),x<<5,y<<5);
            }
        }
        for(int i = 0;i < 10;i++){
            for (int j = 0;j < 10;j++){
                SpriteCache cache = mapcaches[i][j];
                mapcacheids[i][j] = cache.endCache();
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
        creaturePos.put(name,new Vector2(x>>5,y>>5));
    }
    public  Vector2 getCreaturePos(String name){
        return creaturePos.get(name);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(int i = 0;i < 10;i++){
            for (int j = 0;j < 10;j++){
                SpriteCache cache = mapcaches[i][j];
                cache.setProjectionMatrix(cam.combined);
                cache.begin();
                cache.draw(mapcacheids[i][j]);
                cache.end();
            }
        }
        super.draw(batch, parentAlpha);
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
        }
    };

}
