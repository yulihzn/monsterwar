package com.mw.components.map;

import com.mw.utils.WildDungeon;

import java.io.IOException;

import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.io.TMXMapWriter;

/**
 * Created by BanditCat on 2016/10/14.
 */

public class TmxDungeonMap extends TmxMap {
    public TmxDungeonMap(int width, int height) {
        super(16, 16);
        name = "dungeon"+width+"_"+height;
        initWorld();
    }

    private int[][] getMapArray() {
        WildDungeon dungeon = new WildDungeon();
        dungeon.createDungeon(16,16,5000);
        int[][] worldArray = dungeon.getDungeonArray();
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
                Tile floor = getTileSets().get(0).getTile(SegType.WILD_DIRT.getValue());
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
            tileMap = MapGenerator.getTmxLoader().load(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
