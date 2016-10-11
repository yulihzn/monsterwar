package com.mw.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.mw.model.MapInfo;
import com.mw.model.MapInfoModel;
import com.mw.profiles.GameFileHelper;
import com.mw.utils.Dungeon;

/**
 * Created by BanditCat on 2016/3/25.
 */
public class WorldMap extends TiledMap {
    private TiledMapTileLayer floorLayer;
    private TiledMapTileLayer blockLayer;
    private TiledMapTileLayer decorateLayer;
    private TiledMapTileLayer shadowLayer;
    private int width,height;
    private MapInfo mapInfo;
    public static final int TILE_SIZE_WIDTH = 256;
    public static final int TILE_SIZE_HEIGHT = 256;
    private TextureAtlas textureAtlas;
    private MapEditor mapEditor;
    public static String LAYER_FLOOR = "LAYER_FLOOR";
    public static String LAYER_BLOCK = "LAYER_BLOCK";
    public static String LAYER_DECORATE = "LAYER_DECORATE";
    public static String LAYER_SHADOW = "LAYER_SHADOW";

    private TextureAtlas shadowTextureAtlas;

    public WorldMap() {
        initMap();
        initWorld(9999);
    }

    /**
     * 初始化地图资源
     */
    private void initMap() {
        this.height = TILE_SIZE_HEIGHT;
        this.width = TILE_SIZE_WIDTH;
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
        shadowTextureAtlas = new TextureAtlas(Gdx.files.internal("images/shadows.pack"));
    }

    /**
     * 生成大地图，如果传入对象为null生成新的大地图
     * @param level
     */
    public void initWorld(int level){
        mapInfo = GameFileHelper.getInstance().getGameMap(level);
        if(mapInfo == null){
            mapInfo = new MapInfo();
            mapEditor = new MapEditor();
            mapEditor.Create();

            int[][] worldArray = mapEditor.getArr();
            mapInfo.setMapArray(new MapInfoModel[width][height]);
            mapInfo.setLevel(level);
            for (int i = 0; i < mapInfo.getMapArray().length; i++) {
                for (int j = 0; j < mapInfo.getMapArray()[0].length; j++) {
                    MapInfoModel mim = new MapInfoModel();
                    //阴影要多两条边
                    mim.setBlock(worldArray[i][j]);
                    if(worldArray[i][j]==MapEditor.WATER){
                        mim.setFloor(MapEditor.WATER);
                    }else {
                        mim.setFloor(MapEditor.DIRT);
                    }
                    mim.setShadow(0);
                    mim.setShadowClick(0);
                    mim.setPos(new GridPoint2(i,j));
                    mim.setElement(MathUtils.random(3));
                    mapInfo.getMapArray()[i][j]=mim;
                }
            }
        }
        //保存地图
        GameFileHelper.getInstance().setCurrentLevel(level);
        GameFileHelper.getInstance().saveGameMap(mapInfo,level);
        upDateTilesType();

    }

    /**
     * 更新数组贴图
     */
    public void upDateTilesType(){
        for (int i = 0; i < mapInfo.getMapArray().length; i++) {
            for (int j = 0; j < mapInfo.getMapArray()[0].length; j++) {
                //障碍层
                int tileType = mapInfo.getMapArray()[i][j].getBlock();
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                DungeonTiledMapTile tiledMapTile = new DungeonTiledMapTile(textureAtlas.findRegion(getResName(tileType)));
                tiledMapTile.setId(tileType);
                cell.setTile(tiledMapTile);
                this.blockLayer.setCell(i,j,cell);

                //地表层
                TiledMapTileLayer.Cell cellGround = new TiledMapTileLayer.Cell();
                DungeonTiledMapTile tiledMapTile1 = new DungeonTiledMapTile(textureAtlas.findRegion(getResName(Dungeon.tileDirtFloor)));
                tiledMapTile1.setId(Dungeon.tileDirtFloor);
                cellGround.setTile(tiledMapTile1);
                this.floorLayer.setCell(i,j,cellGround);

                //阴影层
                TiledMapTileLayer.Cell cellShadow = new TiledMapTileLayer.Cell();
                DungeonTiledMapTile tiledMapTile2 = new DungeonTiledMapTile(shadowTextureAtlas.findRegion(""+mapInfo.getMapArray()[i][j].getShadow()));
                tiledMapTile2.setId(mapInfo.getMapArray()[i][j].getShadow());
                cellShadow.setTile(tiledMapTile2);
                this.shadowLayer.setCell(i,j,cellShadow);
            }
        }
    }
    public void changeTileType(int value,int x,int y){
        String name = getResName(value);
        if(name.equals("")){
            return;
        }
        this.blockLayer.getCell(x,y).getTile().setTextureRegion(textureAtlas.findRegion(name));
        mapInfo.getMapArray()[x][y].setBlock(value);
        GameFileHelper.getInstance().saveGameMap(mapInfo, GameFileHelper.getInstance().getCurrentLevel());
    }
    //改变阴影方块
    private void changeShadowTileType(int value,int x,int y){
        if(x >= mapInfo.getMapArray().length||x < 0||y < 0||y >= mapInfo.getMapArray()[0].length){
            return;
        }
        TiledMapTile tiledMapTile = this.shadowLayer.getCell(x,y).getTile();
        tiledMapTile.setId(tiledMapTile.getId()+value);
        if(tiledMapTile.getId()>15){
            tiledMapTile.setId(15);
        }
        tiledMapTile.setTextureRegion(shadowTextureAtlas.findRegion(tiledMapTile.getId()+""));
        mapInfo.getMapArray()[x][y].setShadow(tiledMapTile.getId());
    }
    public void changeShadow(int x,int y){
        if(mapInfo.getMapArray()[x][y].getShadowClick()== 1){
            return;
        }
        mapInfo.getMapArray()[x][y].setShadowClick(1);
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
            case MapEditor.DIRT:name="dirt";break;
            case MapEditor.GRASS:name="grass01";break;
            case MapEditor.TREE:name="tree";break;
            case MapEditor.WATER:name="water";break;
            case MapEditor.STONE:name="stone01";break;
            case MapEditor.WALL:name="block01";break;
            case MapEditor.DOOR:name="door";break;
            case MapEditor.ROAD:name="block02";break;
            case MapEditor.BUILDING:name="building";break;
            case MapEditor.BUILDING1:name="building";break;
            case MapEditor.BUILDING2:name="building";break;
            case MapEditor.GUARD_WATER:name="water";break;
            default:name = "";break;
        }
        return name;
    }

    public MapInfo getMapInfo() {
        return mapInfo;
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
