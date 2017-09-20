package com.mw.components.map.areaeditor;

import com.mw.components.map.AreaTile;
import com.mw.components.map.SegType;
import com.mw.components.map.areasegment.AreaSegmentGrass;
import com.mw.components.map.areasegment.AreaSegmentStone;
import com.mw.components.map.areasegment.AreaSegmentTree;
import com.mw.components.map.areasegment.AreaSegmentWater;
import com.mw.components.map.circle.elements.CircleDungeon;
import com.mw.components.map.circle.elements.Tiles;
import com.mw.components.map.model.Area;
import com.mw.components.map.model.MapInfoModel;

/**
 * Created by BanditCat on 2016/10/19.
 */

public class DungeonEditor extends AreaEditor {
    private CircleDungeon dungeon;
    private int floor = com.mw.components.map.AreaTile.F_DIRT_03;
    private int door = com.mw.components.map.AreaTile.B_DOOR_04;
    private int block = com.mw.components.map.AreaTile.B_WALL_05;

    public DungeonEditor(Area area) {
        super(area);
    }

    @Override
    protected void initArea() {
        dungeon = new CircleDungeon(width, height);
        dungeon.createDungeon();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                MapInfoModel model = new MapInfoModel();
                model.setFloor(floor);
                model.setBlock(AreaTile.B_TRANS);
                int value = dungeon.getMaps()[i][j].getValue();
                if (value == Tiles.tile().roomwall.getValue()
                        || value == Tiles.tile().corridorwall.getValue()) {
                    model.setBlock(block);
                }
                if (value == Tiles.tile().opendoor.getValue()) {
                    model.setBlock(door);
                }
                model.setDecorate(AreaTile.D_TRANS);
                model.setShadow(AreaTile.S_SHADOW);
                map[i][j] = model;

            }
        }
    }
}
