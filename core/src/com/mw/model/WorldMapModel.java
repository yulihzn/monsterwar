package com.mw.model;

import com.mw.map.MapEditor;

import java.util.HashMap;

/**
 * Created by BanditCat on 2016/10/12.
 */

public class WorldMapModel {
    private int[][]arr = new int[MapEditor.WIDTH][MapEditor.HEIGHT];
    private HashMap<String,Area> areas = new HashMap<String, Area>();

    public WorldMapModel() {
    }

    public HashMap<String, Area> getAreas() {
        return areas;
    }

    public void setAreas(HashMap<String, Area> areas) {
        this.areas = areas;
    }

    public int[][] getArr() {
        return arr;
    }

    public void setArr(int[][] arr) {
        this.arr = arr;
    }

}
