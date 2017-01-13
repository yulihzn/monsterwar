package com.mw.map;

import com.mw.map.areasegment.AreaSegment;
import com.mw.map.areasegment.AreaSegmentCastleBuilding;
import com.mw.map.areasegment.AreaSegmentCastleTower;
import com.mw.map.areasegment.AreaSegmentCastleWall;
import com.mw.map.areasegment.AreaSegmentCastleWater;
import com.mw.model.Area;
import com.mw.model.MapInfoModel;

import java.util.Random;

/**
 * Created by BanditCat on 2016/10/19.
 *
 *
 */

public class CastleEditor {
    public static final int width = AreaEditor.WIDTH;
    public static final int height = AreaEditor.HEIGHT;
    public static final int segsize = AreaSegment.size;
    private MapInfoModel[][] map = new MapInfoModel[width][height];
    private Area area;
    public CastleEditor(Area area) {
        this.area = area;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                MapInfoModel model = new MapInfoModel();
                model.setFloor(AreaTile.F_DIRT_01);
                model.setBlock(AreaTile.B_TRANS);
                model.setDecorate(AreaTile.D_TRANS);
                model.setShadow(AreaTile.S_SHADOW);
                map[i][j] = model;
            }
        }
        Random random = new Random(MapEditor.SEED);
        for (int i = 0; i < width; i+=segsize) {
            for (int j = 0; j < height; j+=segsize) {
                boolean isBuilding = true;
                if(i == segsize||i== width-segsize*2){
                    if(j==segsize||j==height-segsize*2||j==height/2){
                        AreaSegmentCastleTower tower = new AreaSegmentCastleTower(i,j, AreaSegmentCastleWall.DIR_TB,0);
                        addSegment(tower);
                    }else {
                        AreaSegmentCastleWall wall = new AreaSegmentCastleWall(i,j, AreaSegmentCastleWall.DIR_TB,0);
                        addSegment(wall);
                    }
                    isBuilding =false;
                }
                if(j == segsize||j== height-segsize*2){
                    if(i==segsize||i==width-segsize*2||i==width/2){
                        AreaSegmentCastleTower tower = new AreaSegmentCastleTower(i,j, AreaSegmentCastleWall.DIR_LR,0);
                        addSegment(tower);
                    }else {
                        AreaSegmentCastleWall wall = new AreaSegmentCastleWall(i,j, AreaSegmentCastleWall.DIR_LR,0);
                        addSegment(wall);
                    }
                    isBuilding = false;
                }
                if(i == 0||i== width-segsize||j == 0||j== width-segsize){
                    AreaSegmentCastleWater water = new AreaSegmentCastleWater(i,j,0);
                    addSegment(water);
                    isBuilding = false;
                }
                if(isBuilding){
                    AreaSegmentCastleBuilding building = new AreaSegmentCastleBuilding(i,j,random.nextLong(),0);
                    addSegment(building);
                }
            }
        }
    }
    private void addSegment(AreaSegment segment){
        for (int i = 0; i < segsize; i++) {
            for (int j = 0; j < segsize; j++) {
                map[i+segment.getX0()][j+segment.getY0()] = segment.getMap()[i][j];
            }
        }
    }

    public MapInfoModel[][] getMap() {
        return map;
    }
}
