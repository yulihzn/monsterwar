package com.mw.components.map.model;

import com.badlogic.gdx.math.GridPoint2;
import com.mw.components.map.SegType;
import com.mw.components.map.world.WorldAreaType;

/**
 * Created by BanditCat on 2016/10/9.
 */

public class Castle extends Area{
    private int cx,cy;//要塞
    private GridPoint2[] exits = new GridPoint2[4];//4个门,0,1,2,3,上下左右

    public Castle() {
        this.type = WorldAreaType.CASTLE_00_FOOL;
    }

    public Castle(int x0, int y0) {
        super(x0, y0,0);
        this.type = WorldAreaType.CASTLE_00_FOOL;
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
        int type = SegType.CASTLE_DIRT.getValue();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                type = SegType.CASTLE_DIRT.getValue();
                //城墙
                if(i==1||i==14||j==1||j==14){
                    type = SegType.CASTLE_WALL.getValue();
                }
                //护城河
                if(i==0||i==15||j==0||j==15){
                    type = SegType.CASTLE_GUARD_WATER.getValue();
                }
                //主干道
                if(i==cx||j==cy) {
                    type = SegType.CASTLE_AVENUE.getValue();
                }
                //要塞
                if(i==cx&&j==cy){
                    type = SegType.CASTLE_FORTRESSES.getValue();
                }
                //箭塔
                if(((i==cx+1||i==cx-1)&&type == SegType.CASTLE_WALL.getValue())
                        ||((j==cy+1||j==cy-1)&&type == SegType.CASTLE_WALL.getValue())){
                    type = SegType.CASTLE_BATTLEMENT.getValue();
                }
                if((i==1||i==14)&&(j==1||j==14)){
                    type = SegType.CASTLE_BATTLEMENT.getValue();
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
