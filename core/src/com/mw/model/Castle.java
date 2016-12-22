package com.mw.model;

import com.badlogic.gdx.math.GridPoint2;
import com.mw.map.MapEditor;
import com.mw.map.SegType;

import java.util.Random;

/**
 * Created by BanditCat on 2016/10/9.
 */

public class Castle extends Area{
    private int cx,cy;//要塞
    private GridPoint2[] exits = new GridPoint2[4];//4个门,0,1,2,3,上下左右

    public Castle() {
        this.type = MapEditor.CASTLE;
    }

    public Castle(int x0, int y0) {
        super(x0, y0);
        this.type = MapEditor.CASTLE;
    }

    public Castle init() {
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
        int type = SegType.CASTLE_DIRT;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                type = SegType.CASTLE_DIRT;
                //城墙
                if(i==1||i==14||j==1||j==14){
                    type = SegType.CASTLE_WALL;
                }
                //护城河
                if(i==0||i==15||j==0||j==15){
                    type = SegType.CASTLE_GUARD_WATER;
                }
                //主干道
                if(i==cx||j==cy) {
                    type = SegType.CASTLE_AVENUE;
                }
                //要塞
                if(i==cx&&j==cy){
                    type = SegType.CASTLE_FORTRESSES;
                }
                //箭塔
                if(((i==cx+1||i==cx-1)&&type == SegType.CASTLE_WALL)
                        ||((j==cy+1||j==cy-1)&&type == SegType.CASTLE_WALL)){
                    type = SegType.CASTLE_BATTLEMENT;
                }
                if((i==1||i==14)&&(j==1||j==14)){
                    type = SegType.CASTLE_BATTLEMENT;
                }
                arr[i][j]= type;
            }
        }
        return this;
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
