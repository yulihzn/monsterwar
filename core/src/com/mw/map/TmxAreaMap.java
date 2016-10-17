package com.mw.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.mw.model.WorldMapModel;

import java.io.IOException;
import java.util.Iterator;

import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.core.TileSet;
import tiled.io.TMXMapWriter;
import tiled.util.BasicTileCutter;

/**
 * Created by BanditCat on 2016/10/14.
 */

public class TmxAreaMap extends TmxMap {
    public TmxAreaMap(int width, int height) {
        super(256, 256);
        name = "area"+width+"_"+height;


    }

    @Override
    protected int[][] getMapArray() {
        WorldMapModel mapModel = new MapEditor().create();
        int[][] worldArray = mapModel.getArr();
        return worldArray;
    }
}
