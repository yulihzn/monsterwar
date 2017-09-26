package com.mw.components.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.mw.logic.Logic;
import com.mw.components.map.model.MapInfo;
import com.mw.components.map.model.MapInfoModel;
import com.mw.utils.AssetManagerHelper;
import com.mw.utils.Dungeon;
import com.mw.profiles.GameFileHelper;
import com.mw.utils.WildDungeon;

/**
 * Created by BanditCat on 2016/3/25.
 */
public class DungeonMap extends TiledMap {
    private TiledMapTileLayer floorLayer;
    private TiledMapTileLayer blockLayer;
    private TiledMapTileLayer decorateLayer;
    private TiledMapTileLayer shadowLayer;
    private int width,height;
    private MapInfo mapInfo;
    public static final int TILE_SIZE_WIDTH = 16;
    public static final int TILE_SIZE_HEIGHT = 16;
    private TextureAtlas textureAtlas;
    private WildDungeon dungeon;
    public static String LAYER_FLOOR = "LAYER_FLOOR";
    public static String LAYER_BLOCK = "LAYER_BLOCK";
    public static String LAYER_DECORATE = "LAYER_DECORATE";
    public static String LAYER_SHADOW = "LAYER_SHADOW";
    private int level = 0;

    private TextureAtlas shadowTextureAtlas;
    //shadowIndex = {{0,4,8,12},{1,5,9,13},{2,6,10,14},{3,7,11,15}};

    public DungeonMap() {
        initMap();
        this.level = GameFileHelper.getInstance().getCurrentLevel();
        initDungeon(level);
    }

    /**
     * 初始化地图资源
     */
    private void initMap() {
        //因为阴影层多右上两条边有16的偏移，宽高要加1//这里暂时先不加这个阴影
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
        textureAtlas = AssetManagerHelper.getInstance().getTextureAtlas("wall");
        shadowTextureAtlas = AssetManagerHelper.getInstance().getTextureAtlas("cover");
    }

    /**
     * 生成一层地牢，如果传入对象，如果为null生成新的地牢
     * @param level
     */
    public void initDungeon(int level){
        mapInfo = GameFileHelper.getInstance().getGameMap(level);
        if(mapInfo == null){
            mapInfo = new MapInfo();
            dungeon = new WildDungeon();
            dungeon.createDungeon(TILE_SIZE_WIDTH,TILE_SIZE_HEIGHT,5000);
            int[][] dungeonArray = dungeon.getDungeonArray();
            mapInfo.setMapArray(new MapInfoModel[width][height]);
            mapInfo.setLevel(level);
            for (int i = 0; i < mapInfo.getMapArray().length; i++) {
                for (int j = 0; j < mapInfo.getMapArray()[0].length; j++) {
                    MapInfoModel mim = new MapInfoModel(i,j);
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
                        mim.setBlock(Dungeon.tileChest);
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
        initTilesType();

    }
    //更新新一列地图
    public void upDateDungeon(int dir){
        MapInfoModel[][]maps = mapInfo.getMapArray();
        MapInfoModel[][]tempmaps= new MapInfoModel[maps.length][maps[0].length];
        //这里的坐标系是自然坐标系，左下角为圆点，i是x，j是y
        for (int i = 0; i < tempmaps.length; i++) {
            for (int j = 0; j < tempmaps[0].length; j++) {
                switch (dir){
                    case Logic.DIR_BOTTOM:
                        if(j == 0){//第一排获取新的元素
                            tempmaps[i][j] = getNewMapInfoModel(i,j,Dungeon.tileDirtFloor);
                        }else if(j > 0){//第二排从原数组的第一排开始复制
                            tempmaps[i][j]=maps[i][j-1];
                        }
                        break;
                    case Logic.DIR_TOP:
                        if(j == tempmaps[0].length-1){//倒数第一排获取新的元素
                            tempmaps[i][j] = getNewMapInfoModel(i,j,Dungeon.tileDirtFloor);
                        }else if(j<tempmaps[0].length-1){//第一排从原数组的第二排开始复制
                            tempmaps[i][j]=maps[i][j+1];
                        }
                        break;
                    case Logic.DIR_LEFT:
                        if(i==0){//第一列获取新的元素
                            tempmaps[i][j]=getNewMapInfoModel(i,j,Dungeon.tileDirtFloor);
                        }else if(i>0){//第二列从原数组的第一列开始复制
                            tempmaps[i][j]=maps[i-1][j];
                        }
                        break;
                    case Logic.DIR_RIGHT:
                        if(i == tempmaps.length-1){//倒数第一列获取新的元素
                            tempmaps[i][j] = getNewMapInfoModel(i,j,Dungeon.tileDirtFloor);
                        }else if(i<tempmaps.length-1){//第一列从原数组的第二列开始复制
                            tempmaps[i][j]=maps[i+1][j];
                        }
                        break;
                    case Logic.DIR_BOTTOMLEFT:
                        if(j == 0||i==0){//第一排第一列获取新的元素
                            tempmaps[i][j] = getNewMapInfoModel(i,j,Dungeon.tileDirtFloor);
                        }else if(j > 0&&i>0){
                            tempmaps[i][j]=maps[i-1][j-1];
                        }
                        break;
                    case Logic.DIR_BOTTOMRIGHT:
                        if(j == 0||i == tempmaps.length-1){//第一排最后一列获取新的元素
                            tempmaps[i][j] = getNewMapInfoModel(i,j,Dungeon.tileDirtFloor);
                        }else if(j > 0&&i<tempmaps.length-1){
                            tempmaps[i][j]=maps[i+1][j-1];
                        }
                        break;
                    case Logic.DIR_TOPLEFT:
                        if(j == tempmaps[0].length-1||i==0){//最后一排第一列获取新的元素
                            tempmaps[i][j] = getNewMapInfoModel(i,j,Dungeon.tileDirtFloor);
                        }else if(j<tempmaps[0].length-1&&i>0){
                            tempmaps[i][j]=maps[i-1][j+1];
                        }
                        break;
                    case Logic.DIR_TOPRIGHT:
                        if(j == tempmaps[0].length-1||i == tempmaps.length-1){//最后一排最后一列获取新的元素
                            tempmaps[i][j] = getNewMapInfoModel(i,j,Dungeon.tileDirtFloor);
                        }else if(j<tempmaps[0].length-1&&i<tempmaps.length-1){
                            tempmaps[i][j]=maps[i+1][j+1];
                        }
                        break;

                }

            }
        }
        mapInfo.setMapArray(tempmaps);
        upDateTilesType();
    }
    private MapInfoModel getNewMapInfoModel(int x,int y,int block){
        MapInfoModel mim = new MapInfoModel(x,y);
        //阴影要多两条边
        if(x<DungeonMap.TILE_SIZE_WIDTH&&y<DungeonMap.TILE_SIZE_HEIGHT){
            mim.setBlock(block);
            mim.setFloor(Dungeon.tileDirtFloor);
            if(block==Dungeon.tileUpStairs){
                mapInfo.setUpstairsIndex(new GridPoint2(x,y));
            }
            if(block==Dungeon.tileDownStairs){
                mapInfo.setDownstairsIndex(new GridPoint2(x,y));
            }
        }else{
            mim.setFloor(Dungeon.tileNothing);
            mim.setBlock(Dungeon.tileChest);
        }

        mim.setShadow(0);
        mim.setShadowClick(0);
        mim.setPos(new GridPoint2(x,y));
        mim.setElement(MathUtils.random(3));
        return mim;
    }

    /**
     * 刷新数组贴图
     */
    public void upDateTilesType(){
        for (int i = 0; i < mapInfo.getMapArray().length; i++) {
            for (int j = 0; j < mapInfo.getMapArray()[0].length; j++) {
                //障碍层
                int tileType = mapInfo.getMapArray()[i][j].getBlock();
                blockLayer.getCell(i,j).getTile().setId(tileType);
                blockLayer.getCell(i,j).getTile().setTextureRegion(AssetManagerHelper.getInstance().findRegion(getResName(tileType)));

                //地表层
                floorLayer.getCell(i,j).getTile().setId(Dungeon.tileDirtFloor);
                floorLayer.getCell(i,j).getTile().setTextureRegion(AssetManagerHelper.getInstance().findRegion(getResName(Dungeon.tileDirtFloor)));

                //阴影层
                shadowLayer.getCell(i,j).getTile().setTextureRegion(AssetManagerHelper.getInstance().findRegion(""+mapInfo.getMapArray()[i][j].getShadow()));
                shadowLayer.getCell(i,j).getTile().setId(mapInfo.getMapArray()[i][j].getShadow());
            }
        }
    }
    /**
     * 建立数组贴图
     */
    public void initTilesType(){
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
            case Dungeon.tileUnused:name="w2";break;
            case Dungeon.tileDirtWall:name="w1"; break;
            case Dungeon.tileDirtFloor:name="f4"; break;
            case Dungeon.tileStoneWall:name="w0"; break;
            case Dungeon.tileCorridor:name="f4"; break;
            case Dungeon.tileDoor:name="b0"; break;
            case Dungeon.tileUpStairs:name="b4"; break;
            case Dungeon.tileDownStairs:name="b3"; break;
            case Dungeon.tileChest:name="i5"; break;
            case Dungeon.tileDoorOpen:name="b1"; break;
            case Dungeon.tileNothing:name="c0"; break;
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
    }
}
