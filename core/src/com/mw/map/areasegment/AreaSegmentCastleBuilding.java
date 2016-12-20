package com.mw.map.areasegment;

import com.mw.map.AreaEditor;
import com.mw.map.AreaTile;
import com.mw.map.MapEditor;
import com.mw.model.MapInfoModel;

import java.util.Random;

/**
 * Created by BanditCat on 2016/11/4.
 */

public class AreaSegmentCastleBuilding extends AreaSegment {
    private int floor = AreaTile.F_DIRT_03;
    private int floor_house = AreaTile.F_DIRT_02;
    private int door = AreaTile.B_DOOR_04;
    private int block = AreaTile.B_WALL_05;
    private int decorate = AreaTile.B_WALL_05;
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
     * 一个房子最小是3x3，最大是16x16
     *
     * @return
     */
    private int[][]getHouse(int cx,int cy){
        //0地板，1墙壁，2门
        int lengthx = random.nextInt(16-cx);
        lengthx = lengthx<3?3:lengthx;
        int lengthy = random.nextInt(16-cy);
        lengthy = lengthy<3?3:lengthy;
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
        int cx = random.nextInt(14);
        int cy = random.nextInt(14);
        int[][] house = getHouse(cx,cy);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                MapInfoModel model = new MapInfoModel();
                model.setFloor(floor);
                model.setBlock(AreaTile.B_TRANS);
                model.setDecorate(AreaTile.D_TRANS);
                model.setShadow(AreaTile.S_SHADOW);
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
