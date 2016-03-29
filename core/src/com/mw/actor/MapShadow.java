package com.mw.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by BanditCat on 2016/3/29.
 */
public class MapShadow extends Actor{
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private GridPoint2 sightPosIndex = new GridPoint2(0,0);
    private int width = 0,height = 0;
    private int sightRadius = 4;

    public MapShadow(OrthographicCamera camera,int width,int height) {
        this.camera = camera;
        this.height = height;
        this.width = width;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setProjectionMatrix(camera.combined);
        //混合模式
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        //画视野
        int sightX = 32*getSightPosIndex().x-32*sightRadius;
        int sightY = 32*getSightPosIndex().y-32*sightRadius;
        int sightWidth = 32*(sightRadius*2+1);
        int sightHeight = 32*(sightRadius*2+1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0,1,0,0.1f));
        shapeRenderer.rect(sightX,sightY,sightWidth,sightHeight);
        //画阴影
        shapeRenderer.setColor(new Color(0,0,0,0.75f));
        shapeRenderer.rect(0,0,sightX,height);
        shapeRenderer.rect(sightX,0,sightWidth,sightY);
        shapeRenderer.rect(sightX+sightWidth,0,width-sightX-sightWidth,height);
        shapeRenderer.rect(sightX,sightY+sightHeight,sightWidth,height-sightY-sightHeight);
        shapeRenderer.end();

        //画线
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public GridPoint2 getSightPosIndex() {
        return sightPosIndex;
    }

    public void dispose(){
        shapeRenderer.dispose();
    }
}
