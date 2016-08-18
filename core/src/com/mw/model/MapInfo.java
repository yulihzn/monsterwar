package com.mw.model;

/**
 * Created by BanditCat on 2016/8/18.
 * 地图数组，阴影数组，
 */
public class MapInfo {
    private int[][] dungeonArray;
    private int[][] shadowArray;
    private int[][] shadowClickArray;
    private int level;

    public MapInfo(int level, int[][] dungeonArray, int[][] shadowArray,int[][] shadowClickArray) {
        this.level = level;
        this.dungeonArray = dungeonArray;
        this.shadowArray = shadowArray;
        this.shadowClickArray = shadowClickArray;
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
