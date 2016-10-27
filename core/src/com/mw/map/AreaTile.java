package com.mw.map;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by BanditCat on 2016/10/24.
 */

public class AreaTile {
    public static final int F_TRANS = 0;
    public static final int F_SHADOW = 1;
    public static final int F_GRASS_01 = 2;
    public static final int F_ROAD_01 = 3;
    public static final int F_DIRT_01 = 4;
    public static final int F_GRASS_02 = 5;
    public static final int F_WATER_01 = 6;
    public static final int F_DIRT_02 = 7;

    public static final int B_TRANS = 0;
    public static final int B_SHADOW = 1;
    public static final int B_DOOR_01 = 2;
    public static final int B_DOOR_02 = 3;
    public static final int B_DOWNSTAIRS_01 = 4;
    public static final int B_UPSTAIRS_01 = 5;
    public static final int B_WALL_01 = 6;
    public static final int B_WALL_02 = 7;
    public static final int B_STONE_01 = 8;

    public static final int D_TRANS = 0;
    public static final int D_SHADOW = 1;
    public static final int D_GRASS_01 = 2;

    public static final int S_TRANS = 0;
    public static final int S_SHADOW = 1;
    public static final int S_SHADOW_HALF = 2;

    private static int getRandomTile(int rangeInclusive){
        return MathUtils.random(rangeInclusive);
    }
    public static int getRandomFloor(){
        return getRandomTile(7);
    }
    public static int getRandomBlock(){
        return getRandomTile(8);
    }
    public static int getRandomDecorate(){
        return getRandomTile(2);
    }
    public static int getRandomShadow(){
        return getRandomTile(1);
    }
}
