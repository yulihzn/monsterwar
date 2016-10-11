package com.mw.map;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Random;

/**
 * Created by BanditCat on 2016/10/9.
 */

public class Village implements Comparable<Village>{
    private int x0,y0;
    private Random random;
    private int[][]arr = new int[16][16];
    private int cx,cy;//要塞
    private GridPoint2[] exits = new GridPoint2[4];//4个门,0,1,2,3,上下左右

    public Village(int x0, int y0, Random random) {
        this.random = random;
        this.x0 = x0;
        this.y0 = y0;
        init();
    }

    private void init() {
        //4-11的位置选择中心点
        cx = 4+random.nextInt(8);
        cy = 4+random.nextInt(8);
//        exits[0] = new GridPoint2(0,cy);
//        exits[1] = new GridPoint2(15,cy);
//        exits[2] = new GridPoint2(cx,0);
//        exits[3] = new GridPoint2(cx,15);
        exits[0] = new GridPoint2(-1,cy);
        exits[1] = new GridPoint2(15+1,cy);
        exits[2] = new GridPoint2(cx,-1);
        exits[3] = new GridPoint2(cx,15+1);
        int type = MapEditor.DIRT;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                type = MapEditor.DIRT;
                if(random.nextDouble()<0.15){
                    type = MapEditor.BUILDING2;
                }
                //主干道
                if(i==cx||j==cy) {
                    type = MapEditor.ROAD;
                }
                //要塞
                if(i==cx&&j==cy){
                    type = MapEditor.BUILDING;
                }
                arr[i][j]= type;
            }
        }
    }

    public GridPoint2[] getExits() {
        return exits;
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

    @Override
    public int compareTo(Village o) {
        return (int)(Math.sqrt(x0*x0+y0*y0)-Math.sqrt(o.getX0()*o.getX0()+o.getY0()*o.getY0()));
    }
}
