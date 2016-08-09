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
    private TiledMapTileLayer floorLayer;
    private TiledMapTileLayer blockLayer;
    private TiledMapTileLayer decorateLayer;
    private TiledMapTileLayer shadowLayer;
    private int width,height;
    private int[][] dungeonArray;
    public static final int TILE_SIZE = 32;
    private TextureAtlas textureAtlas;
    private Dungeon dungeon;
    public static String LAYER_FLOOR = "LAYER_FLOOR";
    public static String LAYER_BLOCK = "LAYER_BLOCK";
    public static String LAYER_DECORATE = "LAYER_DECORATE";
    public static String LAYER_SHADOW = "LAYER_SHADOW";

    private int level = 0;

    public static final int MESSAGE_GENERATE_SUCCESS = 1;

    public DungeonMap(int[][] dungeonArray,int level) {
        this.level = level;
        this.dungeonArray = dungeonArray;
        this.height = TILE_SIZE;
        this.width = TILE_SIZE;
        if(dungeonArray == null){
            dungeon = new Dungeon();
            dungeon.createDungeon(width,height,1000);
            this.dungeonArray = dungeon.getDungeonArray();
            GameDataHelper.getInstance().setCurrentLevel(level);
        }

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

        this.floorLayer = new TiledMapTileLayer(width,height,32,32);
        this.blockLayer = new TiledMapTileLayer(width,height,32,32);
        this.decorateLayer = new TiledMapTileLayer(width,height,32,32);
        this.shadowLayer = new TiledMapTileLayer(width,height,32,32);

        //去黑线
        for(TiledMapTileSet tmts : getTileSets()){
            for(TiledMapTile tmt :tmts){
                tmt.getTextureRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            }
        }
        MapLayers layers = this.getLayers();
        floorLayer.setName(LAYER_FLOOR);
        blockLayer.setName(LAYER_BLOCK);
        decorateLayer.setName(LAYER_DECORATE);
        shadowLayer.setName(LAYER_SHADOW);
        layers.add(floorLayer);
        layers.add(blockLayer);
        layers.add(decorateLayer);
        layers.add(shadowLayer);
        textureAtlas = new TextureAtlas(Gdx.files.internal("tiles.pack"));
        upDateTilesType();

    }

    /**
     * 更新数组贴图
     */
    public void upDateTilesType(){
        for(int x = 0; x < this.width;x++){
            for (int y = 0; y < this.height;y++){
                int tileType = this.dungeonArray[x][y];
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                String name = getResName(dungeonArray[x][y]);
                DungeonTiledMapTile tiledMapTile = new DungeonTiledMapTile(textureAtlas.findRegion(name));
                tiledMapTile.setId(tileType);
                cell.setTile(tiledMapTile);
                this.blockLayer.setCell(x,y,cell);

                TiledMapTileLayer.Cell cellGround = new TiledMapTileLayer.Cell();
                String nameGround = getResName(Dungeon.tileDirtFloor);
                DungeonTiledMapTile tiledMapTile1 = new DungeonTiledMapTile(textureAtlas.findRegion(nameGround));
                tiledMapTile1.setId(Dungeon.tileDirtFloor);
                cellGround.setTile(tiledMapTile1);
                this.floorLayer.setCell(x,y,cellGround);
            }
        }
    }
    public void changeTileType(int value,int x,int y){
        String name = getResName(value);
        if(name.equals("")){
            return;
        }
        this.blockLayer.getCell(x,y).getTile().setTextureRegion(textureAtlas.findRegion(name));
        dungeonArray[x][y] = value;
        GameDataHelper.getInstance().saveGameMap(dungeonArray,GameDataHelper.getInstance().getCurrentLevel());
    }
    private String getResName(int value){
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
            case Dungeon.tileDoorOpen:name="down"; break;
        }
        return name;
    }

    /**
     * 生成一层地牢，如果传入了数组直接读取，如果为null生成新的地牢
     * @param dungeonArray
     * @param level
     */
    public void generateNextDungeon(int[][] dungeonArray,int level){
        if(dungeonArray != null){
            this.dungeonArray = dungeonArray;
            if(onEventChangedListener != null){
                onEventChangedListener.onEventFinish(MESSAGE_GENERATE_SUCCESS);
            }
        }else{
            dungeon = new Dungeon();
            dungeon.createDungeon(width,height,5000);
            this.dungeonArray = dungeon.getDungeonArray();
            if(onEventChangedListener != null){
                onEventChangedListener.onEventFinish(MESSAGE_GENERATE_SUCCESS);
            }
        }
        upDateTilesType();
        GameDataHelper.getInstance().setCurrentLevel(level);
        GameDataHelper.getInstance().saveGameMap(this.dungeonArray,level);
    }

    public interface OnEventChangedListener{
        void onEventFinish(int type);
    }

    public void setOnEventChangedListener(OnEventChangedListener onEventChangedListener) {
        this.onEventChangedListener = onEventChangedListener;
    }

    private OnEventChangedListener onEventChangedListener;
}
