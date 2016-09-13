package com.mw.factory;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.mw.actor.FoodActor;
import com.mw.actor.GhostActor;
import com.mw.actor.PlayerActor;
import com.mw.logic.characters.base.Player;
import com.mw.logic.characters.info.GhostInfo;
import com.mw.logic.characters.info.PlayerInfo;
import com.mw.logic.characters.npc.Ghost;
import com.mw.logic.item.base.Food;
import com.mw.logic.item.info.FoodInfo;
import com.mw.map.DungeonMap;
import com.mw.stage.MapStage;


/**
 * 角色生成器
 */
public class ItemFactory {
    private MapStage mapStage;
    private DungeonMap dungeonMap;
    private TextureAtlas textureAtlas;
    private OrthographicCamera camera;
    public ItemFactory(MapStage mapStage) {
        this.mapStage = mapStage;
        dungeonMap = mapStage.getDungeonMap();
        textureAtlas = mapStage.getTextureAtlas();
        camera = mapStage.getCamera();

    }

    public Food getFood(){
        Food food = new Food(dungeonMap);
        food.setInfo(new FoodInfo(1,0,0,0,0));
        FoodActor foodActor = new FoodActor(textureAtlas,FoodInfo.REGION_NAME,camera);
        foodActor.setPosition(-100,-100);
        food.setActor(foodActor);
        mapStage.addActor(foodActor);
        foodActor.setZIndex(3);
        return food;
    }

}
