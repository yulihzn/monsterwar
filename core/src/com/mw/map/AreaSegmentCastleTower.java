package com.mw.map;

import com.mw.model.MapInfoModel;

import java.util.Random;

/**
 * Created by BanditCat on 2016/11/4.
 */

public class AreaSegmentCastleTower extends AreaSegment{
    private int dir = 0;
    private int floor = AreaTile.F_ROAD_02;
    private int door = AreaTile.B_DOOR_03;
    private int block = AreaTile.B_WALL_04;
    private int decorate = AreaTile.D_WALL_01;
    public AreaSegmentCastleTower(int x0, int y0, int dir, int style) {
        super(x0, y0,style);
        this.dir = dir;
        setStyle();
        build();
    }
    //设置城墙的类型
    @Override
    protected void setStyle() {
        switch (style){
            case 0:
                this.floor = AreaTile.F_ROAD_01;
                this.door = AreaTile.B_DOOR_03;
                this.block = AreaTile.B_WALL_04;
                this.decorate = AreaTile.D_WALL_01;
                break;
        }

    }

    @Override
    protected void build() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                MapInfoModel model = new MapInfoModel();
                model.setFloor(floor);
                model.setBlock(AreaTile.B_TRANS);
                model.setDecorate(AreaTile.D_TRANS);
                model.setShadow(AreaTile.S_SHADOW);
                if(j==0||j==HEIGHT-1){
                    model.setBlock(block);
                    if(i==7||i==8){
                        model.setBlock(door);
                    }
                }
                if(i==0||i==WIDTH-1){
                    model.setBlock(block);
                    if(j==7||j==8){
                        model.setBlock(door);
                    }
                }
                //底部装饰为墙
                if(j>=HEIGHT-4){
                    model.setDecorate(decorate);
                }
                map[i][j] = model;
            }
        }
    }
}
