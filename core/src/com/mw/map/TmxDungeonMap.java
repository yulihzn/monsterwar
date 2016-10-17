package com.mw.map;

import com.mw.model.WorldMapModel;
import com.mw.utils.WildDungeon;

/**
 * Created by BanditCat on 2016/10/14.
 */

public class TmxDungeonMap extends TmxMap {
    public TmxDungeonMap(int width, int height) {
        super(16, 16);
        name = "dungeon"+width+"_"+height;
    }

    @Override
    protected int[][] getMapArray() {
        WildDungeon dungeon = new WildDungeon();
        dungeon.createDungeon(16,16,5000);
        int[][] worldArray = dungeon.getDungeonArray();
        return worldArray;
    }
}
