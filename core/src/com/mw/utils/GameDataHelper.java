package com.mw.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Json;
import com.mw.model.MapInfo;

/**
 * Created by BanditCat on 2016/7/4.
 */
public class GameDataHelper {
    public static final String PREFERENCES_NAME = "MW";
    private static GameDataHelper gameDataHelper;
    public static final String DIR_MAP = "/level/level_map_";
    public static final String SUFFIXES_MAP = ".map";

    private Preferences preferences = Gdx.app.getPreferences(PREFERENCES_NAME);

    public static GameDataHelper getInstance()
    {
        if (gameDataHelper==null)
        {
            gameDataHelper = new GameDataHelper();
        }
        return gameDataHelper;
    }
    private void GameDataHelper(){
    }
    public void saveGameMap(MapInfo mapInfo, int level){
        Json json = new Json();
        String str = json.toJson(mapInfo);
        FileHandle file = Gdx.files.local(DIR_MAP+level+SUFFIXES_MAP);
        file.writeString(str,false);

    }
    public MapInfo getGameMap(int level){
        FileHandle file = Gdx.files.local(DIR_MAP+level+SUFFIXES_MAP);
        if(file.exists()){
            try {
                String str = file.readString();
                Json json = new Json();
                return json.fromJson(MapInfo.class,str);
            }catch (Exception e){
                e.printStackTrace();
                Gdx.app.log("Error","地图读取出错...");
            }

        }
        return null;
    }

    /**
     * 玩家信息：层数，位置
     */
    public void setCharacterPos(String name,int x,int y){
        preferences.putInteger(name+"x",x);
        preferences.putInteger(name+"y",y);
        preferences.flush();
    }
    public GridPoint2 getCharacterPos(String name){
        return new GridPoint2(preferences.getInteger(name+"x",-1),preferences.getInteger(name+"y",-1));
    }
    public void setCurrentLevel(int level){
        preferences.putInteger("level",level);
        preferences.flush();
    }
    public int getCurrentLevel(){
        return preferences.getInteger("level",0);
    }

    public void setCurrentStep(String name,int step){
        preferences.putInteger(name+"step",step);
        preferences.flush();
    }
    public int getCurrentStep(String name){
        return preferences.getInteger(name+"step",0);
    }
}
