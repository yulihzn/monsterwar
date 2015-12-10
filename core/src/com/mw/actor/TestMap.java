package com.mw.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mw.utils.Dungeon;

import java.util.Random;

/**
 * Created by BanditCat on 2015/12/8.
 */
public class TestMap extends Actor {
    private OrthographicCamera cam;
    private static final int LAYERS = 1;
    private static final int WIDTH = 15;
    private static final int HEIGHT = 10;
    private Texture texture;
    private int[] layers = new int[LAYERS];
    private static final int TILES_PER_LAYER = WIDTH * HEIGHT;
    private static final int BLOCK_TILES = 25;
    private TextureAtlas textureAtlas;

    private Dungeon dungeon;
    private static final int ysize = 30,xsize = 30;
    private SpriteCache[] caches = new SpriteCache[LAYERS];
    private SpriteCache cache = new SpriteCache();
    private int cacheId = 0;

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
        setDungeon();

    }

    private void setDungeon(){
        dungeon = new Dungeon();
        dungeon.createDungeon(xsize,ysize,100);
        Gdx.app.log("", dungeon.showDungeon());
        int[][] array = dungeon.getDungeonArray();
        cache.beginCache();
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
                cache.add(textureAtlas.findRegion(name),x<<5,y<<5);
            }
        }
        cacheId = cache.endCache();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
//        for (int i = 0; i < LAYERS; i++) {
//            SpriteCache cache = caches[i];
//            cache.setProjectionMatrix(cam.combined);
//            cache.begin();
//            for (int j = 0; j < TILES_PER_LAYER; j += BLOCK_TILES) {
//                cache.draw(layers[i], j, BLOCK_TILES);
//            }
//            cache.end();
//        }
        cache.setProjectionMatrix(cam.combined);
        cache.begin();
        cache.draw(cacheId);
        cache.end();
    }
}
