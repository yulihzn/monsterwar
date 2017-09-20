package com.mw.components.map.areaeditor;

import com.mw.components.map.SegType;
import com.mw.components.map.areasegment.AreaSegmentGrass;
import com.mw.components.map.areasegment.AreaSegmentStone;
import com.mw.components.map.areasegment.AreaSegmentTree;
import com.mw.components.map.areasegment.AreaSegmentWater;
import com.mw.components.map.circle.elements.CircleDungeon;
import com.mw.components.map.model.Area;

/**
 * Created by BanditCat on 2016/10/19.
 *
 *
 */

public class DungeonEditor extends AreaEditor {
    private CircleDungeon dungeon;

    public DungeonEditor(Area area) {
        super(area);
    }

    @Override
    protected void initArea() {
        dungeon = new CircleDungeon(width,height);
        dungeon.createDungeon();

        for (int i = 0; i < width; i+=segSize) {
            for (int j = 0; j < height; j+=segSize) {
                int type = area.getArr()[i%segSize][j%segSize];
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
}
