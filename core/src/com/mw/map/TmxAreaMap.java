package com.mw.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mw.model.Area;
import com.mw.model.AreaMapModel;
import com.mw.model.MapInfoModel;

import java.io.IOException;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.io.TMXMapWriter;

/**
 * Created by BanditCat on 2016/10/14.
 */

public class TmxAreaMap extends TmxMap {
    public static final int block = 256;
    private AreaMapModel mapModel = new AreaMapModel();
    private Area area;
    public TmxAreaMap(Area area) {
        super(block, block);
        this.area = area;
        name = "save/area/"+area.getName()+".tmx";
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
        mapModel = new AreaEditor(area).create();
        MapInfoModel[][]arr = mapModel.getArr();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                MapInfoModel model = arr[i][j];
                Tile floor = getTileSets().get(1).getTile(model.getFloor());
                Tile block = getTileSets().get(2).getTile(model.getBlock());
                Tile decorate = getTileSets().get(3).getTile(model.getDecorate());
                Tile shadow = getTileSets().get(4).getTile(model.getShadow());
                floor.getProperties().setProperty("value",""+floor.getId());
                block.getProperties().setProperty("value",""+block.getId());
                decorate.getProperties().setProperty("value",""+decorate.getId());
                shadow.getProperties().setProperty("value",""+shadow.getId());
                floor.getProperties().setProperty("element",""+model.getFloor());
                block.getProperties().setProperty("element",""+model.getBlock());
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
    private void initMapModel() {
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                        mapModel.getArr()[i][j] = getNewMapInfoModel(getTileId(LAYER_FLOOR,i,j)
                                ,getTileId(LAYER_BLOCK,i,j)
                                ,getTileId(LAYER_DECORATE,i,j)
                                ,getTileId(LAYER_SHADOW,i,j));
            }
        }
    }
    private MapInfoModel getNewMapInfoModel(int floor,int block,int decorate,int shadow) {
        MapInfoModel model = new MapInfoModel();
        model.setFloor(floor);
        model.setBlock(block);
        model.setDecorate(decorate);
        model.setShadow(shadow);
        return model;
    }

    public AreaMapModel getMapModel() {
        return mapModel;
    }

    public void changeTileAlpha(String layer,int x,int y){
        if(x<0||x>=256||y<0||y>=256){
            return;
        }
        int decorate = mapModel.getArr()[x][y].getDecorate();
        if(decorate == AreaTile.D_WALL_01){
            tileMap.getLayers().get(layer).setOpacity(0.5f);
        }else{
            tileMap.getLayers().get(layer).setOpacity(1f);
        }
    }
}
