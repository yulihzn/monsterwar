package com.mw.components.map.world;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.mw.components.map.PerlinNoise2D;

import java.util.Random;

/**
 * Created by yuli.he on 2017/9/22.
 * 22个大阿卡纳城镇
 */

public class WorldEditor {
    public static final int WIDTH = 16;
    public static final int HEIGHT = 16;
    private WorldAreaType[][] arr = new WorldAreaType[WIDTH][HEIGHT];
    private Random random;
    private PerlinNoise2D perlin = new PerlinNoise2D();
    private double threshold = .14, threshold2 = -.14;
    double x0 = 0, y0 = 0, dx = 20, dy = 20;
    int w = WIDTH, h = HEIGHT, d = 2;
    private Array<WorldAreaType> castles = new Array<WorldAreaType>();
    public WorldEditor(long seed) {
        random = new Random(seed);
    }
    public void create(){
        //噪声建立地形
        dx = 20+random.nextInt(20);
        dy = 20+random.nextInt(20);
        x0 = random.nextInt(9999);
        y0 = random.nextInt(9999);
        for(int i = 0;i < w;i++){
            for(int j = 0;j < h;j++){
                arr[i][j] = WorldAreaType.WILD_DIRT;
                double x = dx * i / w + x0,
                        y = dy * j / h + y0;
                double p = perlin.perlinNoise(x, y);
                if (p > threshold){
                    arr[i][j] = WorldAreaType.WILD_FOREST;
                }else if (p < threshold2){
                    arr[i][j] = WorldAreaType.WILD_WATER;
                }
                double pr = random.nextDouble();
                if(pr>=0&&pr<0.20&&arr[i][j]!= WorldAreaType.WILD_WATER){
                    arr[i][j]= WorldAreaType.WILD_ROCK;
                }
                if(pr>=0.20&&pr<0.25&&arr[i][j]!= WorldAreaType.WILD_WATER){
                    arr[i][j]= WorldAreaType.WILD_GRASS;
                }
            }
        }
        for (int i = 0; i < 22; i++) {
            castles.add(WorldAreaType.CASTLE_00_FOOL);
            castles.add(WorldAreaType.CASTLE_01_MAGICIAN);
            castles.add(WorldAreaType.CASTLE_02_PRIESTESS);
            castles.add(WorldAreaType.CASTLE_03_EMPRESS);
            castles.add(WorldAreaType.CASTLE_04_EMPEROR);
            castles.add(WorldAreaType.CASTLE_05_HIEROPHANT);
            castles.add(WorldAreaType.CASTLE_06_LOVERS);
            castles.add(WorldAreaType.CASTLE_07_CHARIOT);
            castles.add(WorldAreaType.CASTLE_08_STRENGTH);
            castles.add(WorldAreaType.CASTLE_09_HERMIT);
            castles.add(WorldAreaType.CASTLE_10_WHEEL);
            castles.add(WorldAreaType.CASTLE_11_JUSTICE);
            castles.add(WorldAreaType.CASTLE_12_HANGED);
            castles.add(WorldAreaType.CASTLE_13_DEATH);
            castles.add(WorldAreaType.CASTLE_14_TEMPERANCE);
            castles.add(WorldAreaType.CASTLE_15_DEVIL);
            castles.add(WorldAreaType.CASTLE_16_TOWER);
            castles.add(WorldAreaType.CASTLE_17_STAR);
            castles.add(WorldAreaType.CASTLE_18_MOON);
            castles.add(WorldAreaType.CASTLE_19_SUN);
            castles.add(WorldAreaType.CASTLE_20_JUDGMENT);
            castles.add(WorldAreaType.CASTLE_21_WORLD);
        }
        //存储256个点遍历列表生成对应大区域
        Array<GridPoint2> indexs = new Array<GridPoint2>();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                indexs.add(new GridPoint2(i,j));
            }
        }
        //添加22个城堡
        for (int i = 0; i < 22; i++) {
            GridPoint2 g = indexs.get(random.nextInt(indexs.size));
            arr[g.x][g.y]= castles.get(i);
            indexs.removeValue(g,false);
        }
    }

    public String getArrayString(){
        StringBuilder stringBuilder = new StringBuilder();
        String[][] strs = new String[WIDTH][HEIGHT];
        for(int i = 0;i < WIDTH;i++){
            for(int j = 0;j < HEIGHT;j++){
                strs[i][j] = arr[i][j].toString();
                stringBuilder.append(strs[i][j]);
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public WorldAreaType[][] getArr() {
        return arr;
    }
    public WorldAreaType getWorldAreaType(int x,int y,int level){
        if(level>0){
            //地牢
            return WorldAreaType.DUNGEON;
        }else{
            //地表
            return arr[x][y];
        }
    }
}
