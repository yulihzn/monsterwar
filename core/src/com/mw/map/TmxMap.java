package com.mw.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

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

public class TmxMap extends Map {
    protected String name = "";
    protected MapLayer floorLayer;
    protected MapLayer blockLayer;
    protected MapLayer decorateLayer;
    protected MapLayer shadowLayer;
    public static String LAYER_FLOOR = "LAYER_FLOOR";
    public static String LAYER_BLOCK = "LAYER_BLOCK";
    public static String LAYER_DECORATE = "LAYER_DECORATE";
    public static String LAYER_SHADOW = "LAYER_SHADOW";
    protected TiledMap tileMap;
    private String[]packNames = {"world","floor","block","decorate","shadow"};
    public TmxMap(int width, int height) {
        super(width, height);
        initMap();
    }

    protected void initWorld() {
        try {
            tileMap = new TmxMapLoader().load(name);
            return;
        }catch (Exception e){
            Gdx.app.log("error","no find the file.");
        }

    }

    private void initMap() {
        setTileHeight(32);
        setTileWidth(32);
        floorLayer = new TileLayer(getWidth(),getHeight());
        blockLayer = new TileLayer(getWidth(),getHeight());
        decorateLayer = new TileLayer(getWidth(),getHeight());
        shadowLayer = new TileLayer(getWidth(),getHeight());
        floorLayer.setName(LAYER_FLOOR);
        blockLayer.setName(LAYER_BLOCK);
        decorateLayer.setName(LAYER_DECORATE);
        shadowLayer.setName(LAYER_SHADOW);
        getLayers().add(floorLayer);
        getLayers().add(blockLayer);
        getLayers().add(decorateLayer);
        getLayers().add(shadowLayer);

        for (int i = 0; i < packNames.length; i++) {
            addTileset(getTileSet("images/tile/"+packNames[i]+".png"));
        }
    }
    private TileSet getTileSet(String fileName){
        TileSet tileSet = new TileSet();
        tileSet.setTilesetImageFilename(fileName);
        try {
            tileSet.importTileBitmap(fileName,new BasicTileCutter(32,32,0,0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tileSet;
    }
    public void saveTiledMap(){
        if(tileMap == null){
            return;
        }
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                //tileMap是y-up,map是y-down
                TiledMapTile floor1 = ((TiledMapTileLayer)(tileMap.getLayers().get(LAYER_FLOOR))).getCell(i,getHeight()-j-1).getTile();
                TiledMapTile block1 = ((TiledMapTileLayer)(tileMap.getLayers().get(LAYER_BLOCK))).getCell(i,getHeight()-j-1).getTile();
                TiledMapTile decorate1 = ((TiledMapTileLayer)(tileMap.getLayers().get(LAYER_DECORATE))).getCell(i,getHeight()-j-1).getTile();
                TiledMapTile shadow1 = ((TiledMapTileLayer)(tileMap.getLayers().get(LAYER_SHADOW))).getCell(i,getHeight()-j-1).getTile();
                Tile floor = TiledMapTile2Tile(floor1);
                Tile block = TiledMapTile2Tile(block1);
                Tile decorate = TiledMapTile2Tile(decorate1);
                Tile shadow = TiledMapTile2Tile(shadow1);
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
    private Tile TiledMapTile2Tile(TiledMapTile tiledMapTile){
        //tileset的下标是从1开始的tilemap的id比tile的id多1
        Tile tile = getTileSets().get(0).getTile(tiledMapTile.getId()-1);
        Iterator<String> iterator = tiledMapTile.getProperties().getKeys();
        while (iterator.hasNext()){
            String key = iterator.next();
            tile.getProperties().setProperty(key,tiledMapTile.getProperties().get(key).toString());
        }
        return tile;
    }
    public void changeTile(String layer,int type,int x,int y){
        if(x<0||x>=256||y<0||y>=256){
            return;
        }
        TiledMapTileLayer.Cell cell = ((TiledMapTileLayer)(tileMap.getLayers().get(layer))).getCell(x,y);
        TiledMapTile tile = new StaticTiledMapTile(cell.getTile().getTextureRegion());
        tile.setId(type);
        tile.getProperties().putAll(cell.getTile().getProperties());
        //tileset的下标是从1开始的
        tile.setTextureRegion(tileMap.getTileSets().getTile(type+1).getTextureRegion());
        cell.setTile(tile);
    }
    public int getTileId(String layer,int x,int y){
        if(x<0||x>=256||y<0||y>=256){
            return -1;
        }
        TiledMapTileLayer.Cell cell = ((TiledMapTileLayer)(tileMap.getLayers().get(layer))).getCell(x,y);
        return Integer.valueOf(String.valueOf(cell.getTile().getProperties().get("value")));
    }


    public TiledMap getTileMap() {
        return tileMap;
    }
}
