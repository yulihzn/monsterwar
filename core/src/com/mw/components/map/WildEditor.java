package com.mw.components.map;

import com.mw.components.map.areasegment.AreaSegment;
import com.mw.components.map.areasegment.AreaSegmentGrass;
import com.mw.components.map.areasegment.AreaSegmentStone;
import com.mw.components.map.areasegment.AreaSegmentTree;
import com.mw.components.map.areasegment.AreaSegmentWater;
import com.mw.components.map.model.Area;
import com.mw.components.map.model.MapInfoModel;

/**
 * Created by BanditCat on 2016/10/19.
 *
 *
 */

public class WildEditor {
    public static final int width = AreaEditor.WIDTH;
    public static final int height = AreaEditor.HEIGHT;
    public static final int segsize = AreaSegment.size;
    private MapInfoModel[][] map = new MapInfoModel[width][height];
    private Area area;

    public WildEditor(Area area) {
        this.area = area;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                MapInfoModel model = new MapInfoModel();
                model.setFloor(com.mw.components.map.AreaTile.F_DIRT_01);
                model.setBlock(com.mw.components.map.AreaTile.B_TRANS);
                model.setDecorate(com.mw.components.map.AreaTile.D_TRANS);
                model.setShadow(com.mw.components.map.AreaTile.S_SHADOW);
                map[i][j] = model;
            }
        }
        for (int i = 0; i < width; i+=segsize) {
            for (int j = 0; j < height; j+=segsize) {
                int type = area.getArr()[i%segsize][j%segsize];
                if(type == com.mw.components.map.SegType.WILD_WATER.getValue()){
                    AreaSegmentWater water = new AreaSegmentWater(i,j,0);
                    addSegment(water);
                }
                if(type == com.mw.components.map.SegType.WILD_GRASS.getValue()){
                    AreaSegmentGrass grass = new AreaSegmentGrass(i,j,0);
                    addSegment(grass);
                }
                if(type == com.mw.components.map.SegType.WILD_TREE.getValue()){
                    AreaSegmentTree tree = new AreaSegmentTree(i,j,0);
                    addSegment(tree);
                }
                if(type == com.mw.components.map.SegType.WILD_STONE.getValue()){
                    AreaSegmentStone stone = new AreaSegmentStone(i,j,0);
                    addSegment(stone);
                }

            }
        }
    }
    private void addSegment(AreaSegment segment){
        for (int i = 0; i < segsize; i++) {
            for (int j = 0; j < segsize; j++) {
                map[i+segment.getX0()][j+segment.getY0()] = segment.getMap()[i][j];
            }
        }
    }

    public MapInfoModel[][] getMap() {
        return map;
    }
}
