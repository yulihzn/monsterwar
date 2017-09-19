package com.mw.logic.factory;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mw.ui.actor.FoodActor;
import com.mw.logic.item.base.Food;
import com.mw.logic.item.info.FoodInfo;
import com.mw.components.map.DungeonMap;
import com.mw.ui.stage.MapStage;


/**
 * 角色生成器
 */
public class ItemFactory {
    private Stage stage;
    private DungeonMap dungeonMap;
    private OrthographicCamera camera;
    public ItemFactory(Stage stage) {
        this.stage = stage;
        camera = ((MapStage)stage).getCamera();

    }

    public Food getFood(){
        Food food = new Food(dungeonMap);
        food.setInfo(new FoodInfo(1,0,0,0,0));
        FoodActor foodActor = new FoodActor(FoodInfo.REGION_NAME,camera);
        foodActor.setPosition(-100,-100);
        food.setActor(foodActor);
        stage.addActor(foodActor);
        foodActor.setZIndex(3);
        return food;
    }

}
