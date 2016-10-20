package com.mw.model;

import com.badlogic.gdx.math.GridPoint2;
import com.mw.map.MapEditor;

import java.util.Random;

/**
 * Created by BanditCat on 2016/10/9.
 */

public class Village extends Area{
    private int cx,cy;//要塞
    private GridPoint2[] exits = new GridPoint2[4];//4个门,0,1,2,3,上下左右

    public Village() {
    }

    public Village(int x0, int y0, Random random) {
        super(x0, y0, random);
        this.type = MapEditor.VILLAGE;
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

    public int getCx() {
        return cx;
    }

    public int getCy() {
        return cy;
    }

}