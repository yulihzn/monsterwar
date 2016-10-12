package com.mw.model;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.mw.map.DungeonMap;

/**
 * Created by BanditCat on 2016/8/18.
 * 地图数组，阴影数组，
 */
public class MapInfo {
    private MapInfoModel[][] mapArray;
    private int level;
    private int type;//类型
    private GridPoint2 upstairsIndex;
    private GridPoint2 downstairsIndex;
    private ObjectMap<String,GridPoint2>indexList = new ObjectMap<String, GridPoint2>();

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

    public GridPoint2 getDownstairsIndex() {
        return downstairsIndex;
    }

    public void setDownstairsIndex(GridPoint2 downstairsIndex) {
        this.downstairsIndex = downstairsIndex;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GridPoint2 getUpstairsIndex() {
        return upstairsIndex;
    }

    public void setUpstairsIndex(GridPoint2 upstairsIndex) {
        this.upstairsIndex = upstairsIndex;
    }
}
