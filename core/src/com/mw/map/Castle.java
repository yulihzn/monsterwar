package com.mw.map;

import java.util.Random;

/**
 * Created by BanditCat on 2016/10/9.
 */

public class Castle {
    private int x0,y0;
    private Random random;
    private int[][]arr = new int[16][16];
    private int cx,cy;//要塞

    public Castle(int x0, int y0, Random random) {
        this.random = random;
        this.x0 = x0;
        this.y0 = y0;
        init();
    }

    private void init() {
        //4-11的位置选择中心点
        cx = 4+random.nextInt(8);
        cy = 4+random.nextInt(8);
        int type = MapEditor.DIRT;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                type = MapEditor.DIRT;
                //城墙
                if(i==1||i==14||j==1||j==14){
                    type = MapEditor.WALL;
                }
                //护城河
                if(i==0||i==15||j==0||j==15){
                    type = MapEditor.WATER;
                }
                //主干道
                if(i==cx||j==cy) {
                    type = MapEditor.ROAD;
                }
                //要塞
                if(i==cx&&j==cy){
                    type = MapEditor.BUILDING;
                }
                //箭塔
                if(((i==cx+1||i==cx-1)&&type == MapEditor.WALL)
                        ||((j==cy+1||j==cy-1)&&type == MapEditor.WALL)){
                    type = MapEditor.BUILDING1;
                }
                if((i==1||i==14)&&(j==1||j==14)){
                    type = MapEditor.BUILDING1;
                }
                arr[i][j]= type;
            }
        }
    }

    public int[][] getArr() {
        return arr;
    }

    public int getCx() {
        return cx;
    }

    public int getCy() {
        return cy;
    }

    public int getX0() {
        return x0;
    }

    public int getY0() {
        return y0;
    }
}
