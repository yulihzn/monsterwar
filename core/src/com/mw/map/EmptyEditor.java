package com.mw.map;

import com.mw.map.areasegment.AreaSegmentGrass;
import com.mw.map.areasegment.AreaSegmentStone;
import com.mw.map.areasegment.AreaSegmentTree;
import com.mw.map.areasegment.AreaSegmentWater;
import com.mw.model.Area;
import com.mw.model.MapInfoModel;

/**
 * Created by BanditCat on 2016/10/19.
 *
 *
 */

public class EmptyEditor {
    private MapInfoModel[][] map = new MapInfoModel[256][256];
    private Area area;

    public EmptyEditor(Area area) {
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
