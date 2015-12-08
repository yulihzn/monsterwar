package com.mw.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by hezhengnan on 2015/12/8.
 */
public class TiledMapActor extends Actor {
    private TiledMap map;
    private TiledMapRenderer renderer;
    private AssetManager assetManager;
    private OrthographicCamera cam;

    public TiledMapActor(OrthographicCamera cam) {
        this.cam = cam;
        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class,new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load("images/tiles.tmx",TiledMap.class);
        assetManager.finishLoading();
        map = assetManager.get("images/tiles.tmx");
        for (TiledMapTileSet tmts :map.getTileSets()){
            for(TiledMapTile tmt : tmts){
                tmt.getTextureRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            }
        }
        renderer = new OrthogonalTiledMapRenderer(map,1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(this.isVisible()){
            renderer.setView(cam);
            renderer.render();
        }
    }

}
