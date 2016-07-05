package com.mw.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.mw.utils.Dungeon;
import com.mw.utils.GameDataHelper;

/**
 * Created by BanditCat on 2016/3/25.
 */
public class DungeonMap extends TiledMap {
    private TiledMapTileLayer tileLayer;
    private TiledMapTileLayer creatureLayer;
    private TiledMapTileLayer shadowLayer;
    private int width,height;
    private int[][] dungeonArray;
    public static final int TILE_SIZE = 32;
    private TextureAtlas textureAtlas;
    private Dungeon dungeon;
    public static String LAYER_FLOOR = "LAYER_FLOOR";
    public static String LAYER_BLOCK = "LAYER_BLOCK";
    public static String LAYER_DECORATE = "LAYER_DECORATE";
    public static String LAYER_CREATURE = "LAYER_CREATURE";
    public static String LAYER_SHADOW = "LAYER_SHADOW";

    private int level = 0;

    public DungeonMap(int[][] dungeonArray,int level) {
        this.level = level;
        this.dungeonArray = dungeonArray;
        this.height = TILE_SIZE;
        this.width = TILE_SIZE;
        if(dungeonArray == null){
            dungeon = new Dungeon();
            dungeon.createDungeon(width,height,5000);
            this.dungeonArray = dungeon.getDungeonArray();
            GameDataHelper.getInstance().setCurrentLevel(level);
        }

        initDungeon();
    }

    public DungeonMap() {
        this.height = TILE_SIZE;
        this.width = TILE_SIZE;
        dungeon = new Dungeon();
        dungeon.createDungeon(width,height,5000);
        this.dungeonArray = dungeon.getDungeonArray();
        initDungeon();
    }

    public int[][] getDungeonArray() {
        return dungeonArray;
    }

    public void setDungeonArray(int[][] dungeonArray) {
        this.dungeonArray = dungeonArray;
    }

    private void initDungeon(){
        //保存地图
        GameDataHelper.getInstance().saveGameMap(dungeonArray,level);

        this.tileLayer = new TiledMapTileLayer(width,height,TILE_SIZE,TILE_SIZE);
        this.creatureLayer = new TiledMapTileLayer(width,height,TILE_SIZE,TILE_SIZE);
        this.shadowLayer = new TiledMapTileLayer(width,height,TILE_SIZE,TILE_SIZE);

        //去黑线
        for(TiledMapTileSet tmts : getTileSets()){
            for(TiledMapTile tmt :tmts){
                tmt.getTextureRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            }
        }
        MapLayers layers = this.getLayers();
        tileLayer.setName(LAYER_FLOOR);
        creatureLayer.setName(LAYER_CREATURE);
        shadowLayer.setName(LAYER_SHADOW);
        layers.add(tileLayer);
        layers.add(creatureLayer);
        layers.add(shadowLayer);
        textureAtlas = new TextureAtlas(Gdx.files.internal("tiles.pack"));
        for(int x = 0; x < this.width;x++){
            for (int y = 0; y < this.height;y++){
                int tileType = this.dungeonArray[x][y];
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                String name = "";
                switch(dungeonArray[x][y]) {
//                    case Dungeon.tileUnused:name="empty-original";break;
                    case Dungeon.tileUnused:name="block01-original";break;
                    case Dungeon.tileDirtWall:name="block01-original"; break;
                    case Dungeon.tileDirtFloor:name="block02"; break;
                    case Dungeon.tileStoneWall:name="stone-original"; break;
                    case Dungeon.tileCorridor:name="block02"; break;
                    case Dungeon.tileDoor:name="door"; break;
                    case Dungeon.tileUpStairs:name="upstair"; break;
                    case Dungeon.tileDownStairs:name="downstair"; break;
                    case Dungeon.tileChest:name="cup02-original"; break;
                }
                DungeonTiledMapTile tiledMapTile = new DungeonTiledMapTile(textureAtlas.findRegion(name));
                tiledMapTile.setId(tileType);
                cell.setTile(tiledMapTile);
                this.tileLayer.setCell(x,y,cell);
            }
        }

    }
    public void changeTileType(int value,int x,int y){
        String name = "";
        switch(value) {
            case Dungeon.tileUnused:name="block01-original";break;
            case Dungeon.tileDirtWall:name="block01-original"; break;
            case Dungeon.tileDirtFloor:name="block02"; break;
            case Dungeon.tileStoneWall:name="stone-original"; break;
            case Dungeon.tileCorridor:name="block02"; break;
            case Dungeon.tileDoor:name="door"; break;
            case Dungeon.tileUpStairs:name="upstair"; break;
            case Dungeon.tileDownStairs:name="downstair"; break;
            case Dungeon.tileChest:name="cup02-original"; break;
        }
        if(name.equals("")){
            return;
        }
        this.tileLayer.getCell(x,y).getTile().setTextureRegion(textureAtlas.findRegion(name));
        dungeonArray[x][y] = value;
        GameDataHelper.getInstance().saveGameMap(dungeonArray,level);
    }
    public void addCreature(String name,int x,int y){
        if(x < 0){
            x = 0;
        }
        if(x >= TILE_SIZE){
            x = TILE_SIZE -1;
        }
        if(y < 0){
            y = 0;
        }
        if(y >= TILE_SIZE){
            y = TILE_SIZE -1;
        }
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        DungeonTiledMapTile tiledMapTile = new DungeonTiledMapTile(textureAtlas.findRegion(name));
        tiledMapTile.setId(1001);
        cell.setTile(tiledMapTile);
        this.creatureLayer.setCell(x,y,cell);


    }
}
