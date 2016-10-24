package com.mw.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mw.model.Area;
import com.mw.model.WorldMapModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.io.TMXMapWriter;

/**
 * Created by BanditCat on 2016/10/14.
 */

public class TmxWorldMap extends TmxMap {
    public static final int TILE_SIZE_WIDTH = 256;
    public static final int TILE_SIZE_HEIGHT = 256;
    private WorldMapModel mapModel = new WorldMapModel();
    public TmxWorldMap(int width, int height) {
        super(width, height);
        name = "save/world.tmx";
        initWorld();
    }


    @Override
    protected void initWorld() {
        try {
            tileMap = new TmxMapLoader().load(name);
        }catch (Exception e){
            Gdx.app.log("error","no find the file.");
        }
        if(null != tileMap){
            initMapModel();
            return;
        }
        mapModel = new MapEditor(MapEditor.SEED).create();
        int[][]arr = mapModel.getArr();
        if(arr == null){
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                Tile floor = getTileSets().get(0).getTile(MapEditor.DIRT);
                Tile block = getTileSets().get(0).getTile(arr[i][j]);
                Tile decorate = getTileSets().get(0).getTile(arr[i][j]);
                Tile shadow = getTileSets().get(0).getTile(MapEditor.SHADOW);
                floor.getProperties().setProperty("element",""+arr[i][j]);
                block.getProperties().setProperty("element",""+arr[i][j]);
                ((TileLayer)floorLayer).setTileAt(i,j,floor);
                ((TileLayer)blockLayer).setTileAt(i,j,block);
                ((TileLayer)decorateLayer).setTileAt(i,j,decorate);
                ((TileLayer)shadowLayer).setTileAt(i,j,shadow);
            }
        }
        HashMap<String,Area> map = mapModel.getAreas();
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,Area> entry = (Map.Entry<String,Area> )iterator.next();
            Area area = entry.getValue();
            getProperties().setProperty(area.getName()+"type",""+area.getType());
            }
        try {
            TMXMapWriter mapWriter = new TMXMapWriter();
            mapWriter.writeMap(this,name);
            tileMap = new TmxMapLoader().load(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMapModel() {

        HashMap<String,Area> map = mapModel.getAreas();
        map.clear();
        //新建256个空的Area并放入map，把tilemap里的数组赋值
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                if(i%16==0&&j%16==0){
                    Area area = new Area(i,j);
                    map.put(area.getName(),area);
                }
                mapModel.getArr()[i][j]=getTileId(LAYER_BLOCK,i,j);
            }
        }
        //遍历256个area并赋值
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,Area> entry = (Map.Entry<String,Area> )iterator.next();
            Area area = entry.getValue();
            int x0 = area.getX0();
            int y0 = area.getY0();
            int type = Integer.parseInt(String.valueOf(tileMap.getProperties().get(area.getName()+"type")));
            area.setType(type);
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    area.getArr()[i][j] = mapModel.getArr()[x0+i][y0+j];
                }
            }
        }

    }

    public WorldMapModel getMapModel() {
        return mapModel;
    }
}
