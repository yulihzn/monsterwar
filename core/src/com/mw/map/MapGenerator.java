package com.mw.map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mw.model.MapInfo;
import com.mw.profiles.GameFileHelper;
import com.mw.utils.WildDungeon;

/**
 * Created by BanditCat on 2016/9/27.
 * 小区域由16x16块组成
 * 大区域由?x?块小区域组成
 *
 */

public class MapGenerator {
    private static MapGenerator mapGenerator;
    public static MapGenerator getInstance()
    {
        if (mapGenerator ==null)
        {
            mapGenerator = new MapGenerator();
        }
        return mapGenerator;
    }
    private MapGenerator(){
        init();
    }

    private WildDungeon dungeon;
    private Array<MapInfo> maps = new Array<MapInfo>();

    private void init() {
        dungeon = new WildDungeon();
    }
}
