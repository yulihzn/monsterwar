package com.mw.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.HashMap;

/**
 * Created by BanditCat on 2016/10/12.
 */

public class WorldMapModel {
    private int[][]arr;
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
