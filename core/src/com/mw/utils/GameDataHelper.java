package com.mw.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

/**
 * Created by BanditCat on 2016/7/4.
 */
public class GameDataHelper {
    private static GameDataHelper gameDataHelper;

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
    public void saveGameMap(int[][] dungeonArray,int level){
        Json json = new Json();
        for (int i = 0; i < dungeonArray.length; i++) {
            for (int j = 0; j < dungeonArray[0].length; j++) {
            }
        }
        FileHandle file = Gdx.files.local("/level/level_map_"+level+".map");

    }
    public int[][] getGameMap(int level){
        return null;
    }
}
