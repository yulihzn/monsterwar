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
    private TmxAreaMap tmxAreaMap;
    private TmxWorldMap tmxWorldMap;

    private void init() {
        tmxWorldMap = new TmxWorldMap(256,256);
    }
    public  TmxWorldMap getTmxWorldMap(){
        return tmxWorldMap;
    }
    public TmxAreaMap getTmxAreaMap(String name){
        Area area = tmxWorldMap.getMapModel().getAreas().get(name);
        tmxAreaMap = new TmxAreaMap(area);
        return tmxAreaMap;
    }

    public TmxAreaMap getTmxAreaMap() {
        return tmxAreaMap;
    }

    /**
     * 传入左上角坐标和数组长度获取对应astar
     * @param x0
     * @param y0
     * @param length
     * @return
     */
    public int[][] getAStarArray(int x0,int y0,int length){
        int[][] arr = new int[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int block;
                if(x0+i>=0&&x0+i<256&&y0+j>=0&&y0+j<256){
                    block = tmxAreaMap.getMapModel().getArr()[x0+i][y0+j].getBlock();
                    if(block == AreaTile.B_WALL_01
                            ||block==AreaTile.B_WALL_02
                            ||block==AreaTile.B_STONE_01){
                        arr[i][j]=1;
                    }else{
                        arr[i][j]=0;
                    }
                }else {
                    arr[i][j]=1;
                }
            }
        }
        return arr;
    }
}
