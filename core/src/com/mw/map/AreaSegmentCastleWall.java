package com.mw.map;

import com.mw.model.MapInfoModel;

/**
 * Created by BanditCat on 2016/11/4.
 */

public class AreaSegmentCastleWall extends AreaSegment{
    public static final int DIR_LR = 0;//左右
    public static final int DIR_TB = 1;//上下
    private int dir = DIR_LR;
    public AreaSegmentCastleWall(int x0, int y0,int dir) {
        super(x0, y0);
        this.dir = dir;
        build();
    }

    @Override
    protected void build() {
        width=16;
        height=16;
        map = new MapInfoModel[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                MapInfoModel model = new MapInfoModel();
                model.setFloor(AreaTile.F_ROAD_01);
                model.setBlock(AreaTile.B_TRANS);
                model.setDecorate(AreaTile.D_TRANS);
                model.setShadow(AreaTile.S_SHADOW);
                if(dir == DIR_LR){
                    if(j==0||j==height-1){
                        model.setBlock(AreaTile.B_WALL_04);
                    }
                    if(j==height-2){
                        model.setDecorate(AreaTile.D_WALL_01);
                    }
                }else {
                    if(i==0||i==width-1){
                        model.setBlock(AreaTile.B_WALL_04);
                    }
                }
                map[i][j] = model;
            }
        }
    }
}
