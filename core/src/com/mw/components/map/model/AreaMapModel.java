package com.mw.components.map.model;

import com.badlogic.gdx.math.GridPoint2;
import com.mw.components.map.areaeditor.AreaEditorManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BanditCat on 2016/10/12.
 */

public class AreaMapModel {
    private MapInfoModel[][]arr = new MapInfoModel[AreaEditorManager.WIDTH][AreaEditorManager.HEIGHT];
    public static final String UPSTAIRS = "upstairs";
    public static final String DOWNSTAIRS = "downstairs";
    private Map<String,MapInfoModel> specialTiles = new HashMap<String, MapInfoModel>();
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

    public void putSpecialTile(String name,int x,int y){
        specialTiles.put(name,arr[x][y]);
    }
    public MapInfoModel getSpecialTile(String name){
        return specialTiles.get(name);
    }

}
