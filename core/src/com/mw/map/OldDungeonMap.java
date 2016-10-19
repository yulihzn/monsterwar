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
import com.mw.utils.AssetManagerHelper;
import com.mw.utils.Dungeon;

/**
 * Created by BanditCat on 2016/3/25.
 */
public class OldDungeonMap extends TiledMap {
    private TiledMapTileLayer floorLayer;
    private TiledMapTileLayer blockLayer;
    private TiledMapTileLayer decorateLayer;
    private TiledMapTileLayer shadowLayer;
    private int width,height;
    private MapInfo mapInfo;
    public static final int TILE_SIZE_WIDTH = 32;
    public static final int TILE_SIZE_HEIGHT = 16;
    private TextureAtlas textureAtlas;
    private Dungeon dungeon;
    public static String LAYER_FLOOR = "LAYER_FLOOR";
    public static String LAYER_BLOCK = "LAYER_BLOCK";
    public static String LAYER_DECORATE = "LAYER_DECORATE";
    public static String LAYER_SHADOW = "LAYER_SHADOW";
    private int level = 0;

    private TextureAtlas shadowTextureAtlas;
    //shadowIndex = {{0,4,8,12},{1,5,9,13},{2,6,10,14},{3,7,11,15}};

    public OldDungeonMap() {
        initMap();
        this.level = GameFileHelper.getInstance().getCurrentLevel();
        initDungeon(level);
    }

    /**
     * 初始化地图资源
     */
    private void initMap() {
        //因为阴影层多右上两条边有16的偏移，宽高要加1
        this.height = TILE_SIZE_HEIGHT+1;
        this.width = TILE_SIZE_WIDTH+1;
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
     * 生成一层地牢，如果传入对象，如果为null生成新的地牢
     * @param level
     */
    public void initDungeon(int level){
        mapInfo = GameFileHelper.getInstance().getGameMap(level);
        if(mapInfo == null){
            mapInfo = new MapInfo();
            dungeon = new Dungeon();
            dungeon.createDungeon(TILE_SIZE_WIDTH,TILE_SIZE_HEIGHT,5000);
            int[][] dungeonArray = dungeon.getDungeonArray();
            mapInfo.setMapArray(new MapInfoModel[width][height]);
            mapInfo.setLevel(level);
            for (int i = 0; i < mapInfo.getMapArray().length; i++) {
                for (int j = 0; j < mapInfo.getMapArray()[0].length; j++) {
                    MapInfoModel mim = new MapInfoModel();
                    //阴影要多两条边
                    if(i<dungeonArray.length&&j<dungeonArray[0].length){
                        mim.setBlock(dungeonArray[i][j]);
                        mim.setFloor(Dungeon.tileDirtFloor);
                        if(dungeonArray[i][j]==Dungeon.tileUpStairs){
                            mapInfo.setUpstairsIndex(new GridPoint2(i,j));
                        }
                        if(dungeonArray[i][j]==Dungeon.tileDownStairs){
                            mapInfo.setDownstairsIndex(new GridPoint2(i,j));
                        }
                    }else{
                        mim.setFloor(Dungeon.tileNothing);
                        mim.setBlock(Dungeon.tileNothing);
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
                DungeonTiledMapTile tiledMapTile = new DungeonTiledMapTile(AssetManagerHelper.getInstance().findRegion(getResName(tileType)));
                tiledMapTile.setId(tileType);
                cell.setTile(tiledMapTile);
                this.blockLayer.setCell(i,j,cell);

                //地表层
                TiledMapTileLayer.Cell cellGround = new TiledMapTileLayer.Cell();
                DungeonTiledMapTile tiledMapTile1 = new DungeonTiledMapTile(AssetManagerHelper.getInstance().findRegion(getResName(Dungeon.tileDirtFloor)));
                tiledMapTile1.setId(Dungeon.tileDirtFloor);
                cellGround.setTile(tiledMapTile1);
                this.floorLayer.setCell(i,j,cellGround);

                //阴影层
                TiledMapTileLayer.Cell cellShadow = new TiledMapTileLayer.Cell();
                DungeonTiledMapTile tiledMapTile2 = new DungeonTiledMapTile(AssetManagerHelper.getInstance().findRegion(""+mapInfo.getMapArray()[i][j].getShadow()));
                tiledMapTile2.setId(mapInfo.getMapArray()[i][j].getShadow());
                //点击位置为4个方块的左下，所以整体要向左下移动16个像素表示点击的是中间
                tiledMapTile2.setOffsetX(-16);
                tiledMapTile2.setOffsetY(-16);
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
        this.blockLayer.getCell(x,y).getTile().setTextureRegion(AssetManagerHelper.getInstance().findRegion(name));
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
        tiledMapTile.setTextureRegion(AssetManagerHelper.getInstance().findRegion(tiledMapTile.getId()+""));
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
            case Dungeon.tileUnused:name="stone01";break;
            case Dungeon.tileDirtWall:name="block01"; break;
            case Dungeon.tileDirtFloor:name="sand01"; break;
            case Dungeon.tileStoneWall:name="stone"; break;
            case Dungeon.tileCorridor:name="sand01"; break;
            case Dungeon.tileDoor:name="door"; break;
            case Dungeon.tileUpStairs:name="upstair"; break;
            case Dungeon.tileDownStairs:name="downstair"; break;
            case Dungeon.tileChest:name="cup01"; break;
            case Dungeon.tileDoorOpen:name="down"; break;
            case Dungeon.tileNothing:name="empty-original"; break;
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
