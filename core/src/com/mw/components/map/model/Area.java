package com.mw.components.map.model;

import com.mw.components.map.MapEditor;
import com.mw.components.map.world.WorldAreaType;

import java.util.Random;

/**
 * Created by BanditCat on 2016/10/12.
 */

public class Area {
    protected int x0, y0;
    protected Random random;
    protected int[][] arr = new int[16][16];
    protected String name;//名字是area+坐标+level：area0_0_0
    protected WorldAreaType type;
    protected int level = 0;//层级默认为0,向下为123456...
    public static final String AREA = "area_";

    public Area(int x0, int y0, int level) {
        this.x0 = x0;
        this.y0 = y0;
        this.level = level;
        this.random = new Random(MapEditor.SEED);
        this.name = "area_" + x0 + "_" + y0 + "_" + level;
    }

    public Area() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorldAreaType getType() {
        return type;
    }

    public void setType(WorldAreaType type) {
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static String getAreaName(int x, int y, int level) {
        return "AREA" + x + "_" + y + "_" + level;
    }

    public static String getChangedAreaName(String name, int level) {
        String[] str = name.split("_");
        if (str.length != 4) {
            return name;
        }
        int x = Integer.valueOf(str[1]);
        int y = Integer.valueOf(str[2]);
        return AREA + x + "_" + y + "_" + level;
    }

    public static String getChangedAreaName(String name, int x, int y) {
        String[] str = name.split("_");
        if (str.length != 4) {
            return name;
        }
        int level = Integer.valueOf(str[3]);
        return AREA + x + "_" + y + "_" + level;
    }

    public static int getAreaLevel(String name) {
        String[] str = name.split("_");
        if (str.length != 4) {
            return 0;
        }
        int level = Integer.valueOf(str[3]);
        return level;
    }
}
