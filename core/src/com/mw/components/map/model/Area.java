package com.mw.components.map.model;

import com.mw.components.map.MapEditor;

import java.util.Random;

/**
 * Created by BanditCat on 2016/10/12.
 */

public class Area {
    protected int x0,y0;
    protected Random random;
    protected int[][]arr = new int[16][16];
    protected String name;
    protected int type;

    public Area(int x0, int y0) {
        this.x0 = x0;
        this.y0 = y0;
        this.random = new Random(MapEditor.SEED);
        this.name = "area"+x0+"_"+y0;
    }

    public Area() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int[][] getArr() {
        return arr;
    }

    public void setArr(int[][] arr) {
        this.arr = arr;
    }

    public int getX0() {
        return x0;
    }

    public void setX0(int x0) {
        this.x0 = x0;
    }

    public int getY0() {
        return y0;
    }

    public void setY0(int y0) {
        this.y0 = y0;
    }
}
