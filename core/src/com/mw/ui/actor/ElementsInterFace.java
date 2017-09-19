package com.mw.ui.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mw.components.map.DungeonMap;

/**
 * Created by BanditCat on 2016/8/26.
 */
public class ElementsInterFace extends Actor {
    private OrthographicCamera camera;
    private int width = 0,height = 0;
    private DungeonMap dungeonMap;
    private Pixmap pixmap;
    private Texture texture;
    private GridPoint2 sightPosIndex = new GridPoint2(0,0);
    private Color[] colors = {new Color(255,0,0,0.5f),new Color(0,255,0,0.5f),new Color(220,140,60,0.5f),new Color(0,0,255,0.5f)};

    public ElementsInterFace(OrthographicCamera camera, int width, int height, DungeonMap dungeonMap) {
        this.camera = camera;
        this.width = width;
        this.height = height;
        this.dungeonMap = dungeonMap;

        // Create an empty dynamic pixmap
        pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        // Create a texture to contain the pixmap
        texture = new Texture(width, height, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //混合模式
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(camera.combined);
//        batch.draw(texture,0,0);
        super.draw(batch, parentAlpha);
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void drawTile() {
        pixmap.setColor(new Color(0,0,0,0.0f));
        pixmap.fillRectangle(0,0,width,height);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                pixmap.setColor(colors[dungeonMap.getMapInfo().getMapArray()[sightPosIndex.x+i][sightPosIndex.y+j].getElement()]);
                pixmap.fillRectangle((sightPosIndex.x+i)*32,height-(sightPosIndex.y+j)*32-32,32,32);
            }
        }
        texture.draw(pixmap, 0, 0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public GridPoint2 getSightPosIndex() {
        return sightPosIndex;
    }

    public void setSightPosIndex(GridPoint2 sightPosIndex) {
        this.sightPosIndex = sightPosIndex;
    }
    public void dispose(){
        texture.dispose();
        pixmap.dispose();
    }

}
