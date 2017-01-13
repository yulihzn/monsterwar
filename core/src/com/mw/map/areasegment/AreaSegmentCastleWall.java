package com.mw.map.areasegment;

import com.mw.map.*;
import com.mw.model.MapInfoModel;

import java.util.Random;

/**
 * Created by BanditCat on 2016/11/4.
 */

public class AreaSegmentCastleWall extends AreaSegment {
    public static final int DIR_LR = 0;//左右
    public static final int DIR_TB = 1;//上下
    private int dir = DIR_LR;
    private int floor = AreaTile.F_ROAD_01;
    private int floor1 = AreaTile.F_ROAD_01;
    private int floor2 = AreaTile.F_ROAD_01;
    private int block = AreaTile.B_WALL_04;
    private int decorate = AreaTile.D_WALL_01;
    public AreaSegmentCastleWall(int x0, int y0,int dir,int style) {
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
                this.floor1 = AreaTile.F_ROAD_01;
                this.floor2 = AreaTile.F_ROAD_01;
                this.block = AreaTile.B_WALL_04;
                this.decorate = AreaTile.D_WALL_01;
                break;
        }

    }

    @Override
    protected void build() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                MapInfoModel model = new MapInfoModel();
                model.setFloor(floor);
                Random r = new Random(MapEditor.SEED);
                if(r.nextDouble()<0.2){
                    model.setFloor(floor1);
                }
                if(r.nextDouble()<0.1){
                    model.setFloor(floor2);
                }
                model.setBlock(AreaTile.B_TRANS);
                model.setDecorate(AreaTile.D_TRANS);
                model.setShadow(AreaTile.S_SHADOW);
                if(dir == DIR_LR){
                    //左右方向，上下围墙
                    if(j==0||j==size-1){
                        model.setBlock(block);
                    }
                    //底部第二排装饰为墙
                    if(j==size-2){
                        model.setDecorate(decorate);
                    }
                }else {
                    //上下方向，左右围墙
                    if(i==0||i==size-1){
                        model.setBlock(block);
                    }
                }
                map[i][j] = model;
            }
        }
    }
}
