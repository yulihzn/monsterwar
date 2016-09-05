package com.mw.logic.item.base;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mw.actor.GameMapTile;
import com.mw.actor.ItemActor;
import com.mw.logic.item.info.ItemInfo;
import com.mw.map.DungeonMap;

/**
 * Created by BanditCat on 2016/9/5.
 */
public abstract class Item {
    protected ItemActor actor;
    protected ItemInfo info;
    protected DungeonMap dungeonMap;

    public Item(DungeonMap dungeonMap) {
        this.dungeonMap = dungeonMap;
    }

    public ItemInfo getInfo() {
        return info;
    }

    public void setInfo(ItemInfo info) {
        this.info = info;
    }

    public ItemActor getActor() {
        return actor;
    }

    public void setActor(ItemActor actor) {
        this.actor = actor;
    }
}
