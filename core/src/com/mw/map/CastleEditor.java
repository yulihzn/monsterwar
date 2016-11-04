package com.mw.map;

import com.mw.model.Area;
import com.mw.model.MapInfoModel;

/**
 * Created by BanditCat on 2016/10/19.
 *
 *
 */

public class CastleEditor {
    private MapInfoModel[][] map = new MapInfoModel[256][256];

    public CastleEditor() {
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
                    AreaSegmentCastleWall wall = new AreaSegmentCastleWall(i,j,AreaSegmentCastleWall.DIR_TB);
                    addSegment(wall);
                }
                if(j == 16||j== 256-32){
                    AreaSegmentCastleWall wall = new AreaSegmentCastleWall(i,j,AreaSegmentCastleWall.DIR_LR);
                    addSegment(wall);
                }
                if(i == 0||i== 256-16||j == 0||j== 256-16){
                    AreaSegmentCastleWater water = new AreaSegmentCastleWater(i,j);
                    addSegment(water);
                }
            }
        }
    }
    private void addSegment(AreaSegment segment){
        for (int i = 0; i < segment.getHeight(); i++) {
            for (int j = 0; j < segment.getWidth(); j++) {
                map[i+segment.getX0()][j+segment.getY0()] = segment.getMap()[i][j];
            }
        }
    }

    public MapInfoModel[][] getMap() {
        return map;
    }
}
