package com.mw.model;

import com.badlogic.gdx.utils.Array;
import com.mw.map.DungeonMap;

/**
 * Created by BanditCat on 2016/8/18.
 * 地图数组，阴影数组，
 */
public class MapInfo {
    private int[][] dungeonArray;
    private int[][] shadowArray;
    private int[][] shadowClickArray;
    private MapInfoModel[][] mapArray;
    private int level;

    public MapInfo(int level, int[][] dungeonArray, int[][] shadowArray,int[][] shadowClickArray) {
        this.level = level;
        this.dungeonArray = dungeonArray;
        this.shadowArray = shadowArray;
        this.shadowClickArray = shadowClickArray;
        mapArray = new MapInfoModel[DungeonMap.TILE_SIZE+1][DungeonMap.TILE_SIZE+1];
    }

    public MapInfoModel[][] getMapArray() {
        return mapArray;
    }

    public void setMapArray(MapInfoModel[][] mapArray) {
        this.mapArray = mapArray;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int[][] getDungeonArray() {
        return dungeonArray;
    }

    public void setDungeonArray(int[][] dungeonArray) {
        this.dungeonArray = dungeonArray;
    }

    public int[][] getShadowClickArray() {
        return shadowClickArray;
    }

    public void setShadowClickArray(int[][] shadowClickArray) {
        this.shadowClickArray = shadowClickArray;
    }

    public int[][] getShadowArray() {
        return shadowArray;
    }

    public void setShadowArray(int[][] shadowArray) {
        this.shadowArray = shadowArray;
    }
}
