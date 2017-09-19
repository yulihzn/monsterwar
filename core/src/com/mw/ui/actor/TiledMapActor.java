package com.mw.ui.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by hezhengnan on 2015/12/8.
 */
public class TiledMapActor extends Actor {
    private TiledMap tiledMap;
    private GridPoint2 tilePosIndex = new GridPoint2(0,0);

    private TiledMapTileLayer tiledLayer;
    private TiledMapTileLayer.Cell cell;
    public TiledMapActor(TiledMap tiledMap, TiledMapTileLayer tiledLayer, TiledMapTileLayer.Cell cell,GridPoint2 tilePosIndex) {
        this.tiledMap = tiledMap;
        this.tiledLayer = tiledLayer;
        this.cell = cell;
        this.tilePosIndex = tilePosIndex;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void setTiledMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }

    public TiledMapTileLayer getTiledLayer() {
        return tiledLayer;
    }

    public void setTiledLayer(TiledMapTileLayer tiledLayer) {
        this.tiledLayer = tiledLayer;
    }

    public TiledMapTileLayer.Cell getCell() {
        return cell;
    }

    public void setCell(TiledMapTileLayer.Cell cell) {
        this.cell = cell;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public GridPoint2 getTilePosIndex() {
        return tilePosIndex;
    }

    public void setTilePosIndex(GridPoint2 tilePosIndex) {
        this.tilePosIndex = tilePosIndex;
    }

}
