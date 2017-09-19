package com.mw.components.map.model;

/**
 * Created by BanditCat on 2016/10/12.
 */

public class AreaModel {
    private Area area;
    private MapInfo[][] mapInfos = new MapInfo[16][16];

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public MapInfo[][] getMapInfos() {
        return mapInfos;
    }

    public void setMapInfos(MapInfo[][] mapInfos) {
        this.mapInfos = mapInfos;
    }
}
