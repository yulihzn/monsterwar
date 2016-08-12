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

import java.util.HashMap;

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
    public static final int TILE_SIZE = 16;
    private TextureAtlas textureAtlas;
    private Dungeon dungeon;
    public static String LAYER_FLOOR = "LAYER_FLOOR";
    public static String LAYER_BLOCK = "LAYER_BLOCK";
    public static String LAYER_DECORATE = "LAYER_DECORATE";
    public static String LAYER_SHADOW = "LAYER_SHADOW";
    private int level = 0;

    public static final int MESSAGE_GENERATE_SUCCESS = 1;

    private int[][] shadowArray;
    private int[][] shadowClickArray;
    private TextureAtlas shadowTextureAtlas;
    private int[][]shadowIndex = {{0,4,8,12},{1,5,9,13},{2,6,10,14},{3,7,11,15}};

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
    public void initShadowArray(){
        shadowArray = new int[width+1][height+1];
        shadowClickArray = new int[width+1][height+1];
        for (int i = 0; i < shadowArray.length; i++) {
            for (int j = 0; j < shadowArray[0].length; j++) {
                shadowArray[i][j] = 0;
                shadowClickArray[i][j] = 0;
            }
        }
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
        //这里阴影层有16的偏移，所以要+1
        this.shadowLayer = new TiledMapTileLayer(width+1,height+1,32,32);

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
        shadowTextureAtlas = new TextureAtlas(Gdx.files.internal("images/shadows.pack"));
        initShadowArray();
        upDateTilesType();

    }

    /**
     * 更新数组贴图
     */
    public void upDateTilesType(){
        for(int x = 0; x < this.width;x++){
            for (int y = 0; y < this.height;y++){
                //障碍层
                int tileType = this.dungeonArray[x][y];
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                String name = getResName(dungeonArray[x][y]);
                DungeonTiledMapTile tiledMapTile = new DungeonTiledMapTile(textureAtlas.findRegion(name));
                tiledMapTile.setId(tileType);
                cell.setTile(tiledMapTile);
                this.blockLayer.setCell(x,y,cell);

                //地表层
                TiledMapTileLayer.Cell cellGround = new TiledMapTileLayer.Cell();
                String nameGround = getResName(Dungeon.tileDirtFloor);
                DungeonTiledMapTile tiledMapTile1 = new DungeonTiledMapTile(textureAtlas.findRegion(nameGround));
                tiledMapTile1.setId(Dungeon.tileDirtFloor);
                cellGround.setTile(tiledMapTile1);
                this.floorLayer.setCell(x,y,cellGround);


            }
            //阴影层比普通层多一层
            for (int i = 0; i < shadowArray.length; i++) {
                for (int j = 0; j < shadowArray[0].length; j++) {
                    //阴影层
                    TiledMapTileLayer.Cell cellShadow = new TiledMapTileLayer.Cell();
                    DungeonTiledMapTile tiledMapTile2 = new DungeonTiledMapTile(shadowTextureAtlas.findRegion(""+shadowArray[i][j]));
                    tiledMapTile2.setId(shadowArray[i][j]);
                    //点击位置为4个方块的左下，所以整体要向左下移动16个像素表示点击的是中间
                    tiledMapTile2.setOffsetX(-16);
                    tiledMapTile2.setOffsetY(-16);
                    cellShadow.setTile(tiledMapTile2);
                    this.shadowLayer.setCell(i,j,cellShadow);
                }
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
    //改变阴影方块
    private void changeShadowTileType(int value,int x,int y){
        if(x >= shadowArray.length||x < 0||y < 0||y >= shadowArray[0].length){
            return;
        }
        TiledMapTile tiledMapTile = this.shadowLayer.getCell(x,y).getTile();
        tiledMapTile.setId(tiledMapTile.getId()+value);
        if(tiledMapTile.getId()>15){
            tiledMapTile.setId(15);
        }
        tiledMapTile.setTextureRegion(shadowTextureAtlas.findRegion(tiledMapTile.getId()+""));
        shadowArray[x][y] = value;
    }
    public void changeShadow(int x,int y){
        if(shadowClickArray[x][y] == 1){
            return;
        }
        shadowClickArray[x][y]=1;
        int[] arr = {4,8,1,2};
		//左下开始
		this.changeShadowTileType(1,x,y);
        this.changeShadowTileType(2,x+1,y);
        this.changeShadowTileType(4,x,y+1);
        this.changeShadowTileType(8,x+1,y+1);

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

    @Override
    public void dispose() {
        super.dispose();
        shadowTextureAtlas.dispose();
        textureAtlas.dispose();
    }
}
