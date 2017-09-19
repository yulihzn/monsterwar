package com.mw.components.map.areasegment;

import com.mw.components.map.model.MapInfoModel;

/**
 * Created by BanditCat on 2016/11/4.
 */

public class AreaSegmentCastleTower extends AreaSegment {
    private int dir = 0;
    private int floor = com.mw.components.map.AreaTile.F_ROAD_02;
    private int door = com.mw.components.map.AreaTile.B_DOOR_03;
    private int block = com.mw.components.map.AreaTile.B_WALL_04;
    private int decorate = com.mw.components.map.AreaTile.D_WALL_01;
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
                this.floor = com.mw.components.map.AreaTile.F_ROAD_01;
                this.door = com.mw.components.map.AreaTile.B_DOOR_03;
                this.block = com.mw.components.map.AreaTile.B_WALL_04;
                this.decorate = com.mw.components.map.AreaTile.D_WALL_01;
                break;
        }

    }

    @Override
    protected void build() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                MapInfoModel model = new MapInfoModel();
                model.setFloor(floor);
                model.setBlock(com.mw.components.map.AreaTile.B_TRANS);
                model.setDecorate(com.mw.components.map.AreaTile.D_TRANS);
                model.setShadow(com.mw.components.map.AreaTile.S_SHADOW);
                if(j==0||j==size-1){
                    model.setBlock(block);
                    if(i==size/2){
                        model.setBlock(door);
                    }
                }
                if(i==0||i==size-1){
                    model.setBlock(block);
                    if(j==size/2){
                        model.setBlock(door);
                    }
                }
                //底部装饰为墙
                if(j>=size-4){
                    model.setDecorate(decorate);
                }
                map[i][j] = model;
            }
        }
    }
}
