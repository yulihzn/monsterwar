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
        int[][] dungeonArray = mapInfo.getDungeonArray();
        Json json = new Json();
        String ss = json.toJson(mapInfo);
        String str = "";
        for (int i = 0; i < dungeonArray.length; i++) {
            for (int j = 0; j < dungeonArray[0].length; j++) {
                str += dungeonArray[i][j];
                //每一条数组结尾是#，到了最后一条把数组长度添加上去
                if(j == dungeonArray[0].length - 1){
                    str+="#";
                    if(i == dungeonArray.length - 1){
                        str+=dungeonArray.length+","+dungeonArray[0].length;
                    }
                }else{
                    str+=",";
                }
            }
        }
        str+="$";
        int[][] shadowArray = mapInfo.getShadowArray();
        for (int i = 0; i < shadowArray.length; i++) {
            for (int j = 0; j < shadowArray[0].length; j++) {
                str += shadowArray[i][j];
                //每一条数组结尾是#，到了最后一条把数组长度添加上去
                if(j == shadowArray[0].length - 1){
                    str+="#";
                    if(i == shadowArray.length - 1){
                        str+=shadowArray.length+","+shadowArray[0].length;
                    }
                }else{
                    str+=",";
                }
            }
        }
        str+="$";
        int[][] shadowClickArray = mapInfo.getShadowClickArray();
        for (int i = 0; i < shadowClickArray.length; i++) {
            for (int j = 0; j < shadowClickArray[0].length; j++) {
                str += shadowClickArray[i][j];
                //每一条数组结尾是#，到了最后一条把数组长度添加上去
                if(j == shadowClickArray[0].length - 1){
                    str+="#";
                    if(i == shadowClickArray.length - 1){
                        str+=shadowClickArray.length+","+shadowClickArray[0].length;
                    }
                }else{
                    str+=",";
                }
            }
        }
        FileHandle file = Gdx.files.local(DIR_MAP+level+SUFFIXES_MAP);
        file.writeString(str,false);

    }
    public MapInfo getGameMap(int level){
        FileHandle file = Gdx.files.local(DIR_MAP+level+SUFFIXES_MAP);
        if(file.exists()){
            try {
                String str = file.readString();
                String[]arr = str.split("$");
                String[] arr1 = arr[0].split("#");
                int size_i=Integer.valueOf(arr1[arr1.length-1].split(",")[0]);
                int size_j=Integer.valueOf(arr1[arr1.length-1].split(",")[1]);
                int[][] dungeonArray = new int[size_i][size_j];
                for (int i = 0; i < arr1.length-1; i++) {
                    String[]arr2 = arr1[i].split(",");
                    for (int j = 0; j < arr2.length; j++) {
                        dungeonArray[i][j]= Integer.valueOf(arr2[j]);
                    }
                }
                String[] arr_shadow = arr[0].split("#");
                int size_i_s=Integer.valueOf(arr_shadow[arr_shadow.length-1].split(",")[0]);
                int size_j_s=Integer.valueOf(arr_shadow[arr_shadow.length-1].split(",")[1]);
                int[][] shadowArray = new int[size_i_s][size_j_s];
                for (int i = 0; i < arr_shadow.length-1; i++) {
                    String[]arr_s = arr_shadow[i].split(",");
                    for (int j = 0; j < arr_s.length; j++) {
                        shadowArray[i][j]= Integer.valueOf(arr_s[j]);
                    }
                }
                String[] arr_shadow_click = arr[0].split("#");
                int size_i_sc=Integer.valueOf(arr_shadow_click[arr_shadow_click.length-1].split(",")[0]);
                int size_j_sc=Integer.valueOf(arr_shadow_click[arr_shadow_click.length-1].split(",")[1]);
                int[][] shadowClickArray = new int[size_i_sc][size_j_sc];
                for (int i = 0; i < arr_shadow_click.length-1; i++) {
                    String[]arr_sc = arr_shadow_click[i].split(",");
                    for (int j = 0; j < arr_sc.length; j++) {
                        shadowClickArray[i][j]= Integer.valueOf(arr_sc[j]);
                    }
                }
                return new MapInfo(level,dungeonArray,shadowArray,shadowClickArray);
            }catch (Exception e){
                e.printStackTrace();
                Gdx.app.log("Error","地图读取出错...");
            }

        }
        return new MapInfo(level,null,null,null);
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
