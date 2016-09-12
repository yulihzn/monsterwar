package com.mw.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.mw.logic.characters.info.CharacterInfo;
import com.mw.logic.characters.info.PlayerInfo;
import com.mw.model.MapInfo;

/**
 * Created by BanditCat on 2016/7/4.
 */
public class GameDataHelper {
    public static final String DEFAULT_PROFILE = "default";
    private static GameDataHelper gameDataHelper;
    public static final String DIR_MAP = "/level/level_map_";
    public static final String SUFFIXES_MAP = ".map";
    public static final String SUFFIXES_GAME = ".sav";
    private ObjectMap<String,Object> objectMap = new ObjectMap<String, Object>();
    private Json json = new Json();



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
        String str = json.toJson(mapInfo);
        FileHandle file = Gdx.files.local(DIR_MAP+level+SUFFIXES_MAP);
        file.writeString(str,false);

    }
    public MapInfo getGameMap(int level){
        FileHandle file = Gdx.files.local(DIR_MAP+level+SUFFIXES_MAP);
        if(file.exists()){
            try {
                String str = file.readString();
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
    }
    public GridPoint2 getCharacterPos(String name){
        return new GridPoint2(preferences.getInteger(name+"x",-1),preferences.getInteger(name+"y",-1));
    }
    public void setCurrentLevel(int level){
        preferences.putInteger("level",level);
    }
    public int getCurrentLevel(){
        return preferences.getInteger("level",0);
    }

    public void setCurrentStep(String name,int step){
        preferences.putInteger(name+"step",step);
    }
    public int getCurrentStep(String name){
        return preferences.getInteger(name+"step",0);
    }

    public void setPlayerInfo(PlayerInfo characterInfo){
        Json json = new Json();
        String str = json.toJson(characterInfo);
        objectMap.put("info",str);
    }
    public PlayerInfo getPlayerInfo(){
        return json.fromJson(PlayerInfo.class,preferences.getString("info"));
    }

    public void putSaveObject(String key,Object object){
        objectMap.put(key,object);
    }
    public <T extends Object> T getSaveObject(String key, Class<T> type){
        T object = null;
        if( !objectMap.containsKey(key) ){
            return object;
        }
        object = (T)objectMap.get(key);
        return object;
    }

    public void saveProfile(){
        String text = json.toJson(objectMap);
        writeProfileToStorage(DEFAULT_PROFILE, text, true);
    }
    public void writeProfileToStorage(String profileName, String fileData, boolean overwrite){
        String fullFilename = profileName+SUFFIXES_GAME;

        boolean localFileExists = Gdx.files.local(fullFilename).exists();

        //If we cannot overwrite and the file exists, exit
        if( localFileExists && !overwrite ){
            return;
        }

        FileHandle file =  null;

        if( Gdx.files.isLocalStorageAvailable() ) {
            file = Gdx.files.local(fullFilename);
            String encodedString = fileData;
            file.writeString(encodedString, !overwrite);
        }
    }
}
