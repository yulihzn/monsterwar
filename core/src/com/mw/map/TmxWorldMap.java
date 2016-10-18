package com.mw.map;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.mw.model.MapInfo;
import com.mw.model.MapInfoModel;
import com.mw.model.WorldMapModel;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

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

public class TmxWorldMap extends TmxMap {
    public static final int TILE_SIZE_WIDTH = 256;
    public static final int TILE_SIZE_HEIGHT = 256;
    public TmxWorldMap(int width, int height) {
        super(width, height);
    }

    @Override
    protected int[][] getMapArray() {
        WorldMapModel mapModel = new MapEditor().create();
        int[][] worldArray = mapModel.getArr();
        return worldArray;
    }

    @Override
    protected void loadMap() {
        name = "save/world.tmx";
        super.loadMap();
    }
}
