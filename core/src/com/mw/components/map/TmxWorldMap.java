package com.mw.components.map;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mw.game.MainGame;
import com.mw.components.map.model.Area;
import com.mw.components.map.model.WorldMapModel;

import java.io.File;
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

    private WorldMapModel mapModel = new WorldMapModel();
    private boolean hasInitMapModel = false;
    public TmxWorldMap(int width, int height) {
        super(width, height);
        String s = Gdx.files.getLocalStoragePath()+"save/";
        if(Gdx.app.getType().equals(Application.ApplicationType.Android)){
            s = MainGame.androidDir+"save/";
//            s=s.substring(s.indexOf("Android"));
            File dir = new File(s);
            dir.mkdirs();
        }
        name = s+"world.tmx";
        name=name.replaceAll("\\\\", "/");
        Gdx.app.log("namePath",name);
        initWorld();
    }


    @Override
    protected void initWorld() {
//        try {
//            tileMap = MapGenerator.getTmxLoader().load(name);
//        }catch (Exception e){
//            Gdx.app.log("searching...","not find the file.");
//        }
//        if(null != tileMap){
//            initMapModel();
//        return;
//        }
        if(MapGenerator.isExistsMap(name)){
            return;
        }
        mapModel = new MapEditor(MapEditor.SEED).create();
        int[][]arr = mapModel.getArr();
        if(arr == null){
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                Tile floor = getTileSets().get(0).getTile(SegType.WILD_DIRT.getValue());
                Tile block = getTileSets().get(0).getTile(arr[i][j]);
                Tile decorate = getTileSets().get(0).getTile(arr[i][j]);
                Tile shadow = getTileSets().get(0).getTile(SegType.SHADOW.getValue());
                floor.getProperties().setProperty("value",""+floor.getId());
                block.getProperties().setProperty("value",""+block.getId());
                decorate.getProperties().setProperty("value",""+decorate.getId());
                shadow.getProperties().setProperty("value",""+shadow.getId());
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
//            tileMap = MapGenerator.getTmxLoader().load(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //初始化数据，加载完tilemap执行
    protected void initMapModel() {
        if(hasInitMapModel){
            return;
        }
        hasInitMapModel =true;
        HashMap<String,Area> map = mapModel.getAreas();
        map.clear();
        //新建256个空的Area并放入map，把tilemap里的数组赋值
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
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

    @Override
    public TiledMap getTileMapReload() {
        super.getTileMapReload();
        initMapModel();
        return tileMap;
    }

    public WorldMapModel getMapModel() {
        return mapModel;
    }
}
