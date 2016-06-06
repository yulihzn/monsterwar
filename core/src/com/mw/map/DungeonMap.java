package com.mw.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.mw.utils.Dungeon;

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

    public DungeonMap(int[][] dungeonArray) {
        this.dungeonArray = dungeonArray;
        this.width = dungeonArray.length;
        this.height = dungeonArray[0].length;
        this.tileLayer = new TiledMapTileLayer(width,height,TILE_SIZE,TILE_SIZE);
        this.creatureLayer = new TiledMapTileLayer(width,height,TILE_SIZE,TILE_SIZE);
        this.shadowLayer = new TiledMapTileLayer(width,height,TILE_SIZE,TILE_SIZE);
        initDungeon();
    }

    public DungeonMap(int width, int height) {
        this.height = height;
        this.width = width;
        this.tileLayer = new TiledMapTileLayer(width,height,TILE_SIZE,TILE_SIZE);
        this.creatureLayer = new TiledMapTileLayer(width,height,TILE_SIZE,TILE_SIZE);
        this.shadowLayer = new TiledMapTileLayer(width,height,TILE_SIZE,TILE_SIZE);
        dungeon = new Dungeon();
        dungeon.createDungeon(width,height,5000);
        this.dungeonArray = dungeon.getDungeonArray();
        initDungeon();
    }
    private void initDungeon(){
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
