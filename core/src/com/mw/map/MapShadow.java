package com.mw.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mw.actor.ShadowActor;
import com.mw.actor.ShadowImage;
import com.mw.utils.AssetManagerHelper;

/**
 * Created by BanditCat on 2016/10/28.
 */

public class MapShadow extends Group {
    private OrthographicCamera camera;
    private ShadowImage sight;
    private ShadowImage cover;

    public MapShadow(OrthographicCamera camera) {
        this.camera = camera;
        sight = new ShadowImage(AssetManagerHelper.getInstance().findRegion("shadow","s0003"));
        cover = new ShadowImage(AssetManagerHelper.getInstance().findRegion("shadow","s0005"));
        sight.setZIndex(299);
        sight.setScale(2f);
//        addActor(sight);
        cover.setScale(8);
        cover.setZIndex(298);
//        addActor(cover);
//        sight.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(5),Actions.fadeOut(5))));
//        cover.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(5),Actions.fadeOut(5))));
        //32个像素扩大了8倍到256，左下角为原点，右上移动128个单位再减去本身宽度的一半16
        cover.setPosition(128-16,128-16);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setProjectionMatrix(camera.combined);
        super.draw(batch, parentAlpha);
    }
    public void dispose(){
        sight.dispose();
        cover.dispose();
    }
    public void setSightPosition(float x,float y){
        sight.setPosition(x-32,y-32);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
