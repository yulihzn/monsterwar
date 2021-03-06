package com.mw.profiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.mw.logic.characters.info.PlayerInfo;
import com.mw.components.map.model.MapInfo;

/**
 * Created by BanditCat on 2016/7/4.
 */
public class GameFileHelper {
    public static final String DEFAULT_PROFILE = "default";
    private static GameFileHelper gameFileHelper;
    public static final String DIR_MAP = "/level/level_map_";
    public static final String DIR_AREA = "/save/area/";
    public static final String DIR_SAVE = "/save/";
    public static final String SUFFIXES_MAP = ".map";
    public static final String SUFFIXES_GAME = ".sav";
    private ObjectMap<String,Object> objectMap = new ObjectMap<String, Object>();
    private boolean isNewProfile=false;
    private Json json;

    public boolean isNewProfile() {
        return isNewProfile;
    }

    public void setNewProfile(boolean newProfile) {
        isNewProfile = newProfile;
    }

    public static GameFileHelper getInstance()
    {
        if (gameFileHelper ==null)
        {
            gameFileHelper = new GameFileHelper();
        }
        return gameFileHelper;
    }
    private GameFileHelper(){
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
    }
    public void saveGameMap(MapInfo mapInfo, int level){
        String str = json.prettyPrint(mapInfo);
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
        putSaveObject(GameFileStr.CURPOS+name,new GridPoint2(x,y));
    }
    public GridPoint2 getCharacterPos(String name){
        GridPoint2 gridPoint2 = getSaveObject(GameFileStr.CURPOS+name,GridPoint2.class);
        if(null == gridPoint2){
            return new GridPoint2(-1,-1);
        }
        return gridPoint2;
    }
    public void setAreaPos(String name,int x,int y){
        putSaveObject(GameFileStr.AREAPOS+name,new GridPoint2(x,y));
    }
    public GridPoint2 getAreaPos(String name){
        GridPoint2 gridPoint2 = getSaveObject(GameFileStr.AREAPOS+name,GridPoint2.class);
        if(null == gridPoint2){
            return new GridPoint2(0,0);
        }
        return gridPoint2;
    }
    public void setCurrentLevel(int level){
        putSaveObject(GameFileStr.LEVEL,level);
    }
    public int getCurrentLevel(){
        Integer level = getSaveObject(GameFileStr.LEVEL,Integer.class);
        if(null == level){
            level = 0;
        }
        return level;
    }
    public void setCurrentAreaName(String name){
        putSaveObject(GameFileStr.AREA,name);
    }
    public String getCurrentAreaName(){
        String name = getSaveObject(GameFileStr.AREA,String.class);
        if(null == name){
            name = "area_0_0_1";
        }
        return name;
    }

    public long getWorldSeed(){
        Long seed = getSaveObject(GameFileStr.WORLD_SEED,Long.class);
        if(null == seed){
            seed = System.currentTimeMillis();
            putSaveObject(GameFileStr.WORLD_SEED,seed);
        }
        return seed;
    }

    public void setCurrentStep(String name,int step){
        putSaveObject(GameFileStr.STEP+name,step);
    }
    public int getCurrentStep(String name){
        Integer step = getSaveObject(GameFileStr.STEP+name,Integer.class);
        if(null == step){
            step = 0;
        }
        return step;
    }

    public void setPlayerInfo(PlayerInfo characterInfo){
        putSaveObject(GameFileStr.INFO,characterInfo);
    }
    public PlayerInfo getPlayerInfo(){
        PlayerInfo playerInfo = getSaveObject(GameFileStr.INFO,PlayerInfo.class);
        if(null == playerInfo){
            return new PlayerInfo();
        }
        return playerInfo;
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

    public void saveProfile(String profileName){
        String text = json.prettyPrint(objectMap);
        writeProfileToStorage(DIR_SAVE+profileName+SUFFIXES_GAME, text, true);
    }
    public void loadProfile(String profileName){
        if(isNewProfile ){
            saveProfile(profileName);
        }

        String fullProfileFileName = DIR_SAVE+profileName+SUFFIXES_GAME;
        boolean doesProfileFileExist = Gdx.files.local(fullProfileFileName).exists();

        if( !doesProfileFileExist ){
            //System.out.println("File doesn't exist!");
            return;
        }

        FileHandle encodedFile = Gdx.files.local(fullProfileFileName);
        String s = encodedFile.readString();


        objectMap = json.fromJson(ObjectMap.class,s);
        isNewProfile = false;
    }
    public void writeProfileToStorage(String profileName, String fileData, boolean overwrite){
        String fullFilename = profileName;

        boolean localFileExists = Gdx.files.local(fullFilename).exists();

        //If we cannot overwrite and the file exists, exit
        if( localFileExists && !overwrite ){
            return;
        }

        if( Gdx.files.isLocalStorageAvailable() ) {
            FileHandle file = Gdx.files.local(fullFilename);
            String encodedString = fileData;
            file.writeString(encodedString, !overwrite);
        }
    }
}
