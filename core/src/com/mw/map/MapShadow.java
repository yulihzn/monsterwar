package com.mw.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
        sight = new ShadowImage(AssetManagerHelper.getInstance().findRegion("shadow","s0004"));
        cover = new ShadowImage(AssetManagerHelper.getInstance().findRegion("shadow","s0005"));
        sight.setZIndex(298);
        addActor(sight);
        cover.setScale(8);
        cover.setZIndex(299);
        addActor(cover);
        cover.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(5),Actions.fadeOut(5))));
        //32个像素扩大了8倍到256，左下角为原点，右上移动128个单位再减去本身宽度的一半16
        cover.setPosition(128-16,128-16);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_ONE,GL20.GL_ZERO);
        batch.setProjectionMatrix(camera.combined);
        super.draw(batch, parentAlpha);
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
    public void dispose(){
        sight.dispose();
        cover.dispose();
    }
    public void setSightPosition(float x,float y){
        sight.setPosition(x-16,y-16);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
