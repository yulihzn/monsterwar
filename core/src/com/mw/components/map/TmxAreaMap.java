package com.mw.components.map;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.GridPoint2;
import com.mw.components.map.areaeditor.AreaEditorManager;
import com.mw.components.map.circle.elements.Tiles;
import com.mw.game.MainGame;
import com.mw.components.map.model.Area;
import com.mw.components.map.model.AreaMapModel;
import com.mw.components.map.model.MapInfoModel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.io.TMXMapWriter;

/**
 * Created by BanditCat on 2016/10/14.
 */

public class TmxAreaMap extends TmxMap {
    private AreaMapModel mapModel = new AreaMapModel();
    private Area area;

    public TmxAreaMap(Area area) {
        super(AreaEditorManager.WIDTH, AreaEditorManager.HEIGHT);
        this.area = area;
        String s = Gdx.files.getLocalStoragePath()+"save/area/";
        if(Gdx.app.getType().equals(Application.ApplicationType.Android)){
            s = MainGame.androidDir+"save/area/";
        }
        File dir = new File(s);
        dir.mkdirs();
        name = s+area.getName()+".tmx";
        name=name.replaceAll("\\\\", "/");
        Gdx.app.log("namePath",name);
        initWorld();
    }

    @Override
    protected void initWorld() {
        if(MapGenerator.isExistsMap(name)){
            return;
        }
        mapModel = new AreaEditorManager(area).create();

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initMapModel() {
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                        mapModel.getArr()[i][j] = getNewMapInfoModel(i,j,getTileId(LAYER_FLOOR,i,j)
                                ,getTileId(LAYER_BLOCK,i,j)
                                ,getTileId(LAYER_DECORATE,i,j)
                                ,getTileId(LAYER_SHADOW,i,j));
                if (mapModel.getArr()[i][j].getBlock() == AreaTile.B_UPSTAIRS_01) {
                    mapModel.putSpecialTile(AreaMapModel.UPSTAIRS,i,j);
                }
                if (mapModel.getArr()[i][j].getBlock() == AreaTile.B_DOWNSTAIRS_01) {
                    mapModel.putSpecialTile(AreaMapModel.DOWNSTAIRS,i,j);
                }
            }
        }
    }
    private MapInfoModel getNewMapInfoModel(int x,int y,int floor,int block,int decorate,int shadow) {
        MapInfoModel model = new MapInfoModel(x,y);
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
        if(x<0||x>=getWidth()||y<0||y>=getHeight()){
            return;
        }
        int decorate = mapModel.getArr()[x][y].getDecorate();
        if(decorate == AreaTile.D_WALL_01){
            tileMap.getLayers().get(layer).setOpacity(0.2f);
        }else{
            tileMap.getLayers().get(layer).setOpacity(1f);
        }
    }
    @Override
    public TiledMap getTileMapReload() {
        super.getTileMapReload();
        initMapModel();
        return tileMap;
    }

}
