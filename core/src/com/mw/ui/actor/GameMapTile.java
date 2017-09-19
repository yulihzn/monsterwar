package com.mw.ui.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mw.profiles.GameFileHelper;
import com.mw.utils.AssetManagerHelper;

/**
 * Created by BanditCat on 2016/3/23 0023.
 */
public class GameMapTile extends Actor {
    private String regionName = "";
    private int tileIndex = 0;
    private GridPoint2 tilePosIndex = new GridPoint2(0,0);
    private OrthographicCamera cam;
    private TextureRegion textureRegion;

    public GameMapTile(String regionName,OrthographicCamera cam) {
        this.regionName = regionName;
        this.cam = cam;
        textureRegion = AssetManagerHelper.getInstance().findRegion(regionName);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setProjectionMatrix(cam.combined);
        //镜头是1/32所以region的宽高是1x1而不是32x32

        batch.draw(textureRegion, getX(), getY(),1,1);
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
        this.tilePosIndex = tilePosIndex;//设置下标
        setPosition(tilePosIndex.x,tilePosIndex.y);//设置actor位置
        GameFileHelper.getInstance().setCharacterPos(regionName,tilePosIndex.x,tilePosIndex.y);
    }
    public void setTilePosIndexOnly(GridPoint2 tilePosIndex) {
        this.tilePosIndex = tilePosIndex;//设置下标
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
        textureRegion = AssetManagerHelper.getInstance().findRegion(regionName);
    }

}
