package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mw.map.DungeonMap;

/**
 * Created by BanditCat on 2016/8/26.
 */
public class GhostActor extends CharacterActor {
    public GhostActor(TextureAtlas textureAtlas, String regionName, OrthographicCamera cam, DungeonMap dungeonMap) {
        super(textureAtlas, regionName, cam, dungeonMap);
    }

    @Override
    protected void moveLogic(int curPos) {
        super.moveLogic(curPos);
        if(curPos > 2){
            stopMoving();
        }
        if(curPos+1 < path.size()){
        }
    }
}
