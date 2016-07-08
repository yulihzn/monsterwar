package com.mw.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.GridPoint2;

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
    public void saveGameMap(int[][] dungeonArray,int level){
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
        FileHandle file = Gdx.files.local(DIR_MAP+level+SUFFIXES_MAP);
        file.writeString(str,false);

    }
    public int[][] getGameMap(int level){
        FileHandle file = Gdx.files.local(DIR_MAP+level+SUFFIXES_MAP);
        if(file.exists()){
            try {
                String str = file.readString();
                String[] arr1 = str.split("#");
                int size_i=Integer.valueOf(arr1[arr1.length-1].split(",")[0]);
                int size_j=Integer.valueOf(arr1[arr1.length-1].split(",")[1]);
                int[][] dungeonArray = new int[size_i][size_j];
                for (int i = 0; i < arr1.length-1; i++) {
                    String[]arr2 = arr1[i].split(",");
                    for (int j = 0; j < arr2.length; j++) {
                        dungeonArray[i][j]= Integer.valueOf(arr2[j]);
                    }
                }
                return dungeonArray;
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
}
