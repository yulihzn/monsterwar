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

public class WildEditor {
    private MapInfoModel[][] map = new MapInfoModel[256][256];
    private Area area;

    public WildEditor(Area area) {
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
                int type = area.getArr()[i%16][j%16];
                if(type == SegType.WILD_WATER.getValue()){
                    AreaSegmentWater water = new AreaSegmentWater(i,j,0);
                    addSegment(water);
                }
                if(type == SegType.WILD_GRASS.getValue()){
                    AreaSegmentGrass grass = new AreaSegmentGrass(i,j,0);
                    addSegment(grass);
                }
                if(type == SegType.WILD_TREE.getValue()){
                    AreaSegmentTree tree = new AreaSegmentTree(i,j,0);
                    addSegment(tree);
                }
                if(type == SegType.WILD_STONE.getValue()){
                    AreaSegmentStone stone = new AreaSegmentStone(i,j,0);
                    addSegment(stone);
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
