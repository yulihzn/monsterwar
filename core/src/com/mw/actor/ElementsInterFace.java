package com.mw.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mw.map.DungeonMap;
import com.mw.utils.GameDataHelper;

/**
 * Created by BanditCat on 2016/8/26.
 */
public class ElementsInterFace extends Actor {
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private int width = 0,height = 0;
    private DungeonMap dungeonMap;
    private Pixmap pixmap;
    private Texture texture;
    private GridPoint2 sightPosIndex = new GridPoint2(0,0);

    public ElementsInterFace(OrthographicCamera camera, int width, int height, DungeonMap dungeonMap) {
        this.camera = camera;
        this.shapeRenderer = new ShapeRenderer();
        this.width = width;
        this.height = height;
        this.dungeonMap = dungeonMap;

        // Create an empty dynamic pixmap
        pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        // Create a texture to contain the pixmap
        texture = new Texture(width, height, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        pixmap.setColor(new Color(0,255,0,0.1f));
        pixmap.fillRectangle(sightPosIndex.x*32,height-sightPosIndex.y*32,32,32);
        texture.draw(pixmap, 0, 0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //混合模式
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(camera.combined);
        drawTile(batch);
        super.draw(batch, parentAlpha);
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawTile(Batch batch) {
        batch.draw(texture,0,0);
    }

    public GridPoint2 getSightPosIndex() {
        return sightPosIndex;
    }

    public void setSightPosIndex(GridPoint2 sightPosIndex) {
        this.sightPosIndex = sightPosIndex;
    }


}
