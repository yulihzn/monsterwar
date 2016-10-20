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
    private MapEditor mapEditor;
    public TmxWorldMap(int width, int height) {
        super(width, height);
        name = "save/world.tmx";
        mapEditor = new MapEditor(MapEditor.SEED);
        initWorld();
    }

    private int[][] getMapArray() {
        WorldMapModel mapModel = new MapEditor(MapEditor.SEED).create();
        int[][] worldArray = mapModel.getArr();
        return worldArray;
    }

    @Override
    protected void initWorld() {
        super.initWorld();
        int[][]arr = getMapArray();
        if(arr == null){
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                Tile floor = getTileSets().get(0).getTile(MapEditor.DIRT);
                Tile block = getTileSets().get(0).getTile(arr[i][j]);
                Tile decorate = getTileSets().get(0).getTile(arr[i][j]);
                Tile shadow = getTileSets().get(0).getTile(13);
                floor.getProperties().setProperty("element",""+arr[i][j]);
                block.getProperties().setProperty("element",""+arr[i][j]);
                shadow.getProperties().setProperty("visible","0");
                ((TileLayer)floorLayer).setTileAt(i,j,floor);
                ((TileLayer)blockLayer).setTileAt(i,j,block);
                ((TileLayer)decorateLayer).setTileAt(i,j,decorate);
                ((TileLayer)shadowLayer).setTileAt(i,j,shadow);
            }
        }
        try {
            TMXMapWriter mapWriter = new TMXMapWriter();
            mapWriter.writeMap(this,name);
            tileMap = new TmxMapLoader().load(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
