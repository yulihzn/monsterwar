package com.mw.components.map.model;

import com.mw.components.map.MapEditor;

import java.util.HashMap;

/**
 * Created by BanditCat on 2016/10/12.
 */

public class WorldMapModel {
    private int[][]arr = new int[MapEditor.WIDTH][MapEditor.HEIGHT];
    private HashMap<String, com.mw.components.map.model.Area> areas = new HashMap<String, com.mw.components.map.model.Area>();

    public WorldMapModel() {
    }

    public HashMap<String, com.mw.components.map.model.Area> getAreas() {
        return areas;
    }

    public void setAreas(HashMap<String, com.mw.components.map.model.Area> areas) {
        this.areas = areas;
    }

    public int[][] getArr() {
        return arr;
    }

    public void setArr(int[][] arr) {
        this.arr = arr;
    }

}
