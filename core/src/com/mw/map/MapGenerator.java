package com.mw.map;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.mw.model.Area;
import com.mw.model.AreaModel;
import com.mw.model.MapInfo;
import com.mw.model.MapInfoModel;
import com.mw.model.WorldMapModel;
import com.mw.profiles.GameFileHelper;
import com.mw.utils.Dungeon;
import com.mw.utils.WildDungeon;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Properties;

import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.core.TileSet;
import tiled.io.TMXMapWriter;
import tiled.util.BasicTileCutter;

/**
 * Created by BanditCat on 2016/9/27.
 * 小区域由16x16块组成
 * 大区域由16x16块小区域组成
 * 世界由16x16个大区域组成
 * 16x16x16x16=65536个小区域
 * 地图类型分为城堡，村落，野外，地牢
 * 根据塔罗牌大阿卡那和小阿卡那，其中22个城堡56个村落，剩下的178个是野外
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

    private MapEditor mapEditor = new MapEditor(0);

    private void init() {

    }

}
