package com.mw.map;

import com.mw.map.areasegment.AreaSegmentCastleTower;
import com.mw.map.areasegment.AreaSegmentCastleWall;
import com.mw.map.areasegment.AreaSegmentCastleWater;
import com.mw.model.Area;
import com.mw.model.MapInfoModel;

/**
 * Created by BanditCat on 2016/10/19.
 *
 *
 */

public class CastleEditor {
    private MapInfoModel[][] map = new MapInfoModel[256][256];
    private Area area;
    public CastleEditor(Area area) {
        this.area = area;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                MapInfoModel model = new MapInfoModel();
                model.setFloor(AreaTile.F_DIRT_01);
                model.setBlock(AreaTile.B_TRANS);
                model.setDecorate(AreaTile.D_TRANS);
                model.setShadow(AreaTile.S_SHADOW);
                map[i][j] = model;
            }
        }
        for (int i = 0; i < 256; i+=16) {
            for (int j = 0; j < 256; j+=16) {
                if(i == 16||i== 256-32){
                    if(j==16||j==256-32){
                        com.mw.map.areasegment.AreaSegmentCastleTower tower = new com.mw.map.areasegment.AreaSegmentCastleTower(i,j, com.mw.map.areasegment.AreaSegmentCastleWall.DIR_TB,0);
                        addSegment(tower);
                    }else {
                        com.mw.map.areasegment.AreaSegmentCastleWall wall = new com.mw.map.areasegment.AreaSegmentCastleWall(i,j, com.mw.map.areasegment.AreaSegmentCastleWall.DIR_TB,0);
                        addSegment(wall);
                    }
                }
                if(j == 16||j== 256-32){
                    if(i==16||i==256-32){
                        com.mw.map.areasegment.AreaSegmentCastleTower tower = new AreaSegmentCastleTower(i,j, com.mw.map.areasegment.AreaSegmentCastleWall.DIR_LR,0);
                        addSegment(tower);
                    }else {
                        com.mw.map.areasegment.AreaSegmentCastleWall wall = new com.mw.map.areasegment.AreaSegmentCastleWall(i,j, AreaSegmentCastleWall.DIR_LR,0);
                        addSegment(wall);
                    }
                }
                if(i == 0||i== 256-16||j == 0||j== 256-16){
                    com.mw.map.areasegment.AreaSegmentCastleWater water = new AreaSegmentCastleWater(i,j,0);
                    addSegment(water);
                }
            }
        }
    }
    private void addSegment(com.mw.map.areasegment.AreaSegment segment){
        for (int i = 0; i < com.mw.map.areasegment.AreaSegment.HEIGHT; i++) {
            for (int j = 0; j < com.mw.map.areasegment.AreaSegment.WIDTH; j++) {
                map[i+segment.getX0()][j+segment.getY0()] = segment.getMap()[i][j];
            }
        }
    }

    public MapInfoModel[][] getMap() {
        return map;
    }
}
