package com.mw.components.map.areaeditor;

import com.mw.components.map.AreaTile;
import com.mw.components.map.circle.elements.CircleDungeon;
import com.mw.components.map.circle.elements.Tiles;
import com.mw.components.map.model.Area;
import com.mw.components.map.model.MapInfoModel;

import tiled.core.Tile;

/**
 * Created by BanditCat on 2016/10/19.
 */

public class DungeonEditor extends AreaEditor {
    private CircleDungeon dungeon;
    private static int floor = AreaTile.F_DIRT_03;
    private static int door = AreaTile.B_DOOR_04;
    private static int block = AreaTile.B_WALL_05;

    public DungeonEditor(Area area) {
        super(area);
    }

    @Override
    protected void initArea() {
        dungeon = new CircleDungeon(width/2, height/2);
        dungeon.createDungeon();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                MapInfoModel model = new MapInfoModel();
                model.setFloor(floor);
                model.setBlock(AreaTile.B_SHADOW);
                int x = i-width/4;
                int y = j-height/4;
                if(x >= 0 && y >= 0 && x < width/2 && y < height/2){
                    int value = dungeon.getMaps()[x][y].getValue();
                    if (value == Tiles.tile().roomwall.getValue()
                            || value == Tiles.tile().corridorwall.getValue()
                            || value == Tiles.tile().roomcorner.getValue()) {
                        model.setBlock(block);
                    }
                    if (value == Tiles.tile().opendoor.getValue()) {
                        model.setBlock(door);
                    }
                    if (value == Tiles.tile().roomfloor.getValue()
                            ||value == Tiles.tile().corridorfloor.getValue()) {
                        model.setBlock(AreaTile.B_TRANS);
                    }
                }

                model.setDecorate(AreaTile.D_TRANS);
                model.setShadow(AreaTile.S_SHADOW);
                map[i][j] = model;

            }
        }
    }
}
