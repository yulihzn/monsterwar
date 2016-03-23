package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by BanditCat on 2016/3/23 0023.
 */
public class GameMapTile extends Actor {
    private String regionName = "";
    private int tileIndex = 0;
    private GridPoint2 tilePosIndex = new GridPoint2(0,0);
    private TextureAtlas textureAtlas;
    private OrthographicCamera cam;

    public GameMapTile(TextureAtlas textureAtlas, String regionName,OrthographicCamera cam) {
        this.textureAtlas = textureAtlas;
        this.regionName = regionName;
        this.cam = cam;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setProjectionMatrix(cam.combined);
        batch.draw(textureAtlas.findRegion(regionName), getX(), getY());
        super.draw(batch, parentAlpha);
    }

    public GridPoint2 getTilePosIndex() {
        return tilePosIndex;
    }

    public void setTilePosIndex(GridPoint2 tilePosIndex) {
        this.tilePosIndex = tilePosIndex;
    }

    public int getTileIndex() {
        return tileIndex;
    }

    public void setTileIndex(int tileIndex) {
        this.tileIndex = tileIndex;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }


}
