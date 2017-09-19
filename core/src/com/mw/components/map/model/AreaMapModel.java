package com.mw.components.map.model;

import com.mw.components.map.AreaEditor;

/**
 * Created by BanditCat on 2016/10/12.
 */

public class AreaMapModel {
    private MapInfoModel[][]arr = new MapInfoModel[AreaEditor.WIDTH][AreaEditor.HEIGHT];
    private Area area;

    public AreaMapModel() {
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public MapInfoModel[][] getArr() {
        return arr;
    }

    public void setArr(MapInfoModel[][] arr) {
        this.arr = arr;
    }
}
