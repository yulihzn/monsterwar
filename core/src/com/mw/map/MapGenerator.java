package com.mw.map;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mw.model.Area;
import com.mw.utils.Utils;

/**
 * Created by BanditCat on 2016/9/27.
 * 小区域由16x16块组成
 * 大区域由16x16块小区域组成
 * 世界由16x16个大区域组成
 * 16x16x16x16=65536个小区域
 * 地图类型分为城堡，村落，野外，地牢
 * 根据塔罗牌大阿卡那和小阿卡那，其中22个城堡56个村落，剩下的178个是野外
 *
 */

public class MapGenerator {
    private static MapGenerator mapGenerator;
    public static MapGenerator getInstance()
    {
        if (mapGenerator ==null)
        {
            mapGenerator = new MapGenerator();
        }
        return mapGenerator;
    }
    private MapGenerator(){
        init();
    }
    private TmxAreaMap tmxAreaMap;
    private TmxWorldMap tmxWorldMap;

    private void init() {
        long time =System.currentTimeMillis();
        Gdx.app.log("world", Utils.getMins(time));
        tmxWorldMap = new TmxWorldMap(256,256);
        long tt = System.currentTimeMillis();
        Gdx.app.log("world", Utils.getMins(tt));
        Gdx.app.log("sec", Utils.getMins(tt-time));
    }
    public  TmxWorldMap getTmxWorldMap(){
        return tmxWorldMap;
    }
    public TmxAreaMap getTmxAreaMap(String name){
        Area area = tmxWorldMap.getMapModel().getAreas().get(name);
        long time =System.currentTimeMillis();
        Gdx.app.log(name, Utils.getMins(time));
        tmxAreaMap = new TmxAreaMap(area);
        long tt = System.currentTimeMillis();
        Gdx.app.log(name, Utils.getMins(tt));
        Gdx.app.log(name, Utils.getMins(tt-time));
        return tmxAreaMap;
    }

    public TmxAreaMap getTmxAreaMap() {
        return tmxAreaMap;
    }

    /**
     * 传入左上角坐标和数组长度获取对应astar
     * @return
     */
    public int[][] getAStarArray(){
        int length = tmxAreaMap.getMapModel().getArr().length;
        int[][] arr = new int[length][length];
//        String str = "\n";
//        String origin = "\n";
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int block;
                if(i>=0&&i<length&&j>=0&&j<length){
                    if(isBlock(i,j)){
                        arr[i][j]=1;
                    }else {
                        arr[i][j]=0;
                    }
//                    origin+=""+block;
                }else {
                    arr[i][j]=1;
                }
//                str+=""+arr[i][j];
            }
//            str+="\n";
//            origin+="\n";
        }
//        Gdx.app.log("origin",origin);
//        Gdx.app.log("map",str);
        return arr;
    }
    public boolean isBlock(int x,int y){
        int block = tmxAreaMap.getMapModel().getArr()[x][y].getBlock();
        if(block != AreaTile.B_TRANS){
            return true;
        }else{
            return false;
        }
    }
    //是否是不可去的位置
    public boolean isForbidden(int x,int y){
        int length = tmxAreaMap.getMapModel().getArr().length;
        int floor = tmxAreaMap.getMapModel().getArr()[x][y].getFloor();
        if(x>=0&&x<length&&y>=0&&y<length){
            if(floor == AreaTile.F_WATER_01){
                return true;
            }else {
                return false;
            }
        }else {
            return true;
        }
    }
    public static  TmxMapLoader getTmxLoader(){
        //如果是Android读取绝对路径
        if(Gdx.app.getType().equals(Application.ApplicationType.Android)){
            return new TmxMapLoader(new AbsoluteFileHandleResolver());
        }
        return new TmxMapLoader();
    }
}
