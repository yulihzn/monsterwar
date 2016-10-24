package com.mw.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by BanditCat on 2016/10/19.
 */

public class AssetManagerHelper {
    private static AssetManagerHelper instance;
    public static AssetManagerHelper getInstance()
    {
        if (instance ==null)
        {
            instance = new AssetManagerHelper();
        }
        return instance;
    }
    private AssetManagerHelper(){
        init();
    }

    private AssetManager assetManager;
    private String[]packNames = {"block","shadow","decorate","floor","item","player","world"};

    public AssetManager getAssetManager() {
        return assetManager;
    }

    private void init() {
        assetManager = new AssetManager();
        for (int i = 0; i < packNames.length; i++) {
            assetManager.load("images/tile/"+packNames[i]+".pack",TextureAtlas.class);
        }
    }
    public TextureAtlas getTextureAtlas(String packName){
        return assetManager.get("images/tile/"+packName+".pack",TextureAtlas.class);
    }
    public TextureAtlas.AtlasRegion findRegion (String packName,String name) {
        return assetManager.get("images/tile/"+packName+".pack",TextureAtlas.class).findRegion(name);
    }
    public TextureAtlas.AtlasRegion findRegion (String name) {
        for (int i = 0; i < packNames.length; i++) {
           TextureAtlas.AtlasRegion region = assetManager.get("images/tile/"+packNames[i]+".pack",TextureAtlas.class).findRegion(name);
            if(region != null){
                return region;
            }
        }

        return null;
    }
}
