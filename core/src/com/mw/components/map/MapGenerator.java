package com.mw.components.map;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.mw.components.map.model.Area;
import com.mw.components.map.world.WorldEditor;
import com.mw.profiles.GameFileHelper;

/**
 * Created by BanditCat on 2016/9/27.
 * 小区域由7x7块组成
 * 大区域由16x16块小区域组成
 * 世界由16x16个大区域组成
 * 7x16x16x16=28672个小区域
 * 地图类型分为城堡，野外，地牢
 * 根据塔罗牌大阿卡那和小阿卡那，其中22个城堡56个村落，剩下的178个是野外
 *
 */

public class MapGenerator {
    private static MapGenerator mapGenerator;
    public static MapGenerator map()
    {
        if (mapGenerator ==null)
        {
            mapGenerator = new MapGenerator();
        }
        return mapGenerator;
    }
    private MapGenerator(){
    }
    private TmxAreaMap tmxAreaMap;
    private WorldEditor worldEditor;
    private String currentAreaName="";


    public void initWorld() {
        if(worldEditor == null){
            worldEditor = new WorldEditor(GameFileHelper.getInstance().getWorldSeed());
            worldEditor.create();
        }
    }
    public void getTmxAreaMap(final String name,OnMapGeneratorListener onMapGeneratorListener){
        this.setOnMapGeneratorListener(onMapGeneratorListener);
        if(name.equals(currentAreaName)&&tmxAreaMap != null){
            sendFinishMsg(tmxAreaMap);//加载不了
            return;
        }
        String[]astr = name.split("_");
        final int x = Integer.valueOf(astr[1]);
        final int y = Integer.valueOf(astr[2]);
        final int level = Integer.valueOf(astr[3]);
        if(astr.length != 4){
            sendFinishMsg(tmxAreaMap);//name格式错误
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Area area = new Area(x,y,level);
                area.setType(worldEditor.getWorldAreaType(x,y,level));
                if(tmxAreaMap != null && tmxAreaMap.getTileMap() != null){
                    tmxAreaMap = null;
                }
                tmxAreaMap = new TmxAreaMap(area);
                MapGenerator.this.currentAreaName = name;
                //ui线程通知
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        sendFinishMsg(tmxAreaMap);
                    }
                });
            }
        }).start();
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
//        L.i("origin",origin);
//        L.i("map",str);
        return arr;
    }
    public boolean isBlock(int x,int y){
        int block = tmxAreaMap.getMapModel().getArr()[x][y].getBlock();
        if(block == AreaTile.B_WALL_01
                ||block == AreaTile.B_WALL_02
                ||block == AreaTile.B_WALL_03
                ||block == AreaTile.B_WALL_04
                ||block == AreaTile.B_WALL_05){
            return true;
        }else{
            return false;
        }
    }
    public int getBlockValue(int x,int y){
        return tmxAreaMap.getMapModel().getArr()[x][y].getBlock();
    }
    public int getFloorValue(int x,int y){
        return tmxAreaMap.getMapModel().getArr()[x][y].getFloor();
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
    public static boolean isExistsMap(String name){
        if(Gdx.app.getType().equals(Application.ApplicationType.Android)){
            return new AbsoluteFileHandleResolver().resolve(name).exists();
        }
        return new InternalFileHandleResolver().resolve(name).exists();
    }
    public interface OnMapGeneratorListener{
        void finish(TmxAreaMap tmxAreaMap);
    }
    private OnMapGeneratorListener onMapGeneratorListener;

    public void setOnMapGeneratorListener(OnMapGeneratorListener onMapGeneratorListener) {
        this.onMapGeneratorListener = onMapGeneratorListener;
    }

    private void sendFinishMsg(final TmxAreaMap tmxAreaMap){

        if(onMapGeneratorListener != null){
            onMapGeneratorListener.finish(tmxAreaMap);
        }
    }
    public void dispose(){

        if(tmxAreaMap != null){
            if(tmxAreaMap.getTileMap()!=null){
                tmxAreaMap.getTileMap().dispose();
                tmxAreaMap=null;
            }
        }
    }
    public void putSpecialTile(String name,int x,int y){
        tmxAreaMap.getMapModel().putSpecialTile(name,x,y);
    }
    public GridPoint2 getSpecialTile(String name){
        return tmxAreaMap.getMapModel().getSpecialTile(name).getPos();
    }
}
