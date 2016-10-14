package com.mw.map;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.mw.model.Area;
import com.mw.model.AreaModel;
import com.mw.model.MapInfo;
import com.mw.model.MapInfoModel;
import com.mw.model.WorldMapModel;
import com.mw.profiles.GameFileHelper;
import com.mw.utils.Dungeon;
import com.mw.utils.WildDungeon;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Properties;

import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.core.TileSet;
import tiled.io.TMXMapWriter;
import tiled.util.BasicTileCutter;

/**
 * Created by BanditCat on 2016/9/27.
 * 小区域由16x16块组成
 * 大区域由16x16块小区域组成
 * 世界由16x16个大区域组成
 * 16x16x16x16=65536个小区域
 * 地图类型分为城堡，村落，野外，地牢
 * 根据塔罗牌大阿卡那和小阿卡那，其中22个城堡56个村落，剩下的178个是野外
 *
 *
 * 加载大区域到列表，一共包含256个小区域
 * 加载当前需要显示的区域，该区域可能是2-4个区域拼合而成（斜线）
 *
 */

public class MapGenerator {
    private static MapGenerator mapGenerator;
    public static MapGenerator getInstance()
    {
        if (mapGenerator ==null)
        {
            mapGenerator = new MapGenerator();
        }
        return mapGenerator;
    }
    private MapGenerator(){
        init();
    }

    private WildDungeon dungeon;
    private MapInfo[][] maps = new MapInfo[9][9];
    private MapEditor mapEditor = new MapEditor();

    private WorldMapModel worldMapModel;
    private AreaModel areaModel;

    private void init() {
        dungeon = new WildDungeon();
        worldMapModel = GameFileHelper.getInstance().getWorldMap(GameFileHelper.DEFAULT_PROFILE);
        if(worldMapModel == null){
            worldMapModel = mapEditor.create();
            GameFileHelper.getInstance().saveWorldMap(GameFileHelper.DEFAULT_PROFILE,worldMapModel);
        }
        GridPoint2 areaPos = GameFileHelper.getInstance().getAreaPos(GameFileHelper.DEFAULT_PROFILE);
        Area area = worldMapModel.getAreas().get("area"+areaPos.x+"_"+areaPos.y);
        areaModel = GameFileHelper.getInstance().getAreaMap(GameFileHelper.DEFAULT_PROFILE,area);
        if(areaModel == null){
//            initArea(area);
        }
//        initTmx();

    }

    private void initTmx() {
        TMXMapWriter mapWriter = new TMXMapWriter();
        String name = "world.tmx";
        Map map = new Map(256,256);
        map.setTileWidth(32);
        map.setTileHeight(32);
        TileSet tileSet = new TileSet();
        tileSet.setTilesetImageFilename("tiles.png");
        try {
            tileSet.importTileBitmap("tiles.png",new BasicTileCutter(32,32,0,0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.addTileset(tileSet);
        MapLayer mapLayer = new TileLayer(256,256);
        mapLayer.setName("floor");
        map.addLayer(mapLayer);
        int[][]arr = worldMapModel.getArr();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                Tile tile = tileSet.getTile(1);
                Properties p = new Properties();
                p.setProperty("testkey","testvalue");
                tile.setProperties(p);
                ((TileLayer)mapLayer).setTileAt(i,j,tile);
            }
        }
        try {
            mapWriter.writeMap(map,name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //初始化区域
    private void initArea(Area area) {
        areaModel = new AreaModel();
        areaModel.setArea(area);
        for (int i = 0; i < areaModel.getMapInfos().length; i++) {
            for (int j = 0; j < areaModel.getMapInfos()[0].length; j++) {
                areaModel.getMapInfos()[i][j]=getNewMapInfo();
            }
        }
        GameFileHelper.getInstance().saveAreaMap(GameFileHelper.DEFAULT_PROFILE,areaModel);
    }

    private MapInfo getNewMapInfo(){
        MapInfo mapInfo = new MapInfo();
        dungeon.createDungeon(DungeonMap.TILE_SIZE_WIDTH,DungeonMap.TILE_SIZE_HEIGHT,5000);
        int[][] dungeonArray = dungeon.getDungeonArray();
        mapInfo.setMapArray(new MapInfoModel[DungeonMap.TILE_SIZE_WIDTH][DungeonMap.TILE_SIZE_WIDTH]);
        for (int i = 0; i < mapInfo.getMapArray().length; i++) {
            for (int j = 0; j < mapInfo.getMapArray()[0].length; j++) {
                MapInfoModel mim = new MapInfoModel();
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
        return mapInfo;
    }
}
