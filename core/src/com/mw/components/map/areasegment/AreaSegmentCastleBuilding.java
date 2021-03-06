package com.mw.components.map.areasegment;

import com.mw.components.map.model.MapInfoModel;

import java.util.Random;

/**
 * Created by BanditCat on 2016/11/4.
 */

public class AreaSegmentCastleBuilding extends AreaSegment {
    private int floor = com.mw.components.map.AreaTile.F_DIRT_03;
    private int floor_house = com.mw.components.map.AreaTile.F_DIRT_02;
    private int door = com.mw.components.map.AreaTile.B_DOOR_04;
    private int block = com.mw.components.map.AreaTile.B_WALL_05;
    private int decorate = com.mw.components.map.AreaTile.B_WALL_05;
    private long seed = 0;
    private Random random;
    public AreaSegmentCastleBuilding(int x0, int y0,long seed, int style) {
        super(x0, y0,style);
        this.seed =seed;
        random = new Random(seed);
        setStyle();
        build();
    }
    //设置房屋类型
    @Override
    protected void setStyle() {
        switch (style){
            case 0:
                break;
        }

    }

    /**
     * 生成房子
     * 一个房子最小是5x5，最大是7x7
     *
     * @return
     */
    private int[][]getHouse(int cx,int cy){
        //0地板，1墙壁，2门
        int lengthx = random.nextInt(7-cx);
        lengthx = lengthx<5?5:lengthx;
        int lengthy = random.nextInt(7-cy);
        lengthy = lengthy<5?5:lengthy;
        int[][]arr = new int[lengthx][lengthy];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j]=0;
                if(i==0||i==arr.length-1){
                    arr[i][j]=1;
                }
                if(j==0||j==arr[0].length-1){
                    arr[i][j]=1;
                }
            }
        }
        int dir = random.nextInt(4);
        switch (dir){
            case 0:arr[0][random.nextInt(arr[0].length-2)+1]=2;break;
            case 1:arr[arr.length-1][random.nextInt(arr[0].length-2)+1]=2;break;
            case 2:arr[random.nextInt(arr.length-2)+1][0]=2;break;
            case 3:arr[random.nextInt(arr.length-2)+1][arr[0].length-1]=2;break;
        }
        return arr;
    }

    @Override
    protected void build() {
        int cx = random.nextInt(2);
        int cy = random.nextInt(2);
        int[][] house = getHouse(cx,cy);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                MapInfoModel model = new MapInfoModel(i,j);
                model.setFloor(floor);
                model.setBlock(com.mw.components.map.AreaTile.B_TRANS);
                model.setDecorate(com.mw.components.map.AreaTile.D_TRANS);
                model.setShadow(com.mw.components.map.AreaTile.S_SHADOW);
                map[i][j] = model;
            }
        }
        for (int i = 0; i < house.length; i++) {
            for (int j = 0; j < house[0].length; j++) {
                MapInfoModel model =  map[cx+i][cy+j];
                if(house[i][j]==0){
                    model.setFloor(floor_house);
                }else if(house[i][j]==1){
                    model.setBlock(block);
                }else if(house[i][j]==2){
                    model.setBlock(door);
                }

            }
        }
    }
}
