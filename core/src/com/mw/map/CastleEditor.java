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
    private MapInfoModel[][] map = new MapInfoModel[256][256];
    private Area area;
    public CastleEditor(Area area) {
        this.area = area;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                MapInfoModel model = new MapInfoModel();
                model.setFloor(AreaTile.F_DIRT_01);
                model.setBlock(AreaTile.B_TRANS);
                model.setDecorate(AreaTile.D_TRANS);
                model.setShadow(AreaTile.S_SHADOW);
                map[i][j] = model;
            }
        }
        Random random = new Random(MapEditor.SEED);
        for (int i = 0; i < 256; i+=16) {
            for (int j = 0; j < 256; j+=16) {
                boolean isBuilding = true;
                if(i == 16||i== 256-32){
                    if(j==16||j==256-32||j==256/2){
                        AreaSegmentCastleTower tower = new AreaSegmentCastleTower(i,j, AreaSegmentCastleWall.DIR_TB,0);
                        addSegment(tower);
                    }else {
                        AreaSegmentCastleWall wall = new AreaSegmentCastleWall(i,j, AreaSegmentCastleWall.DIR_TB,0);
                        addSegment(wall);
                    }
                    isBuilding =false;
                }
                if(j == 16||j== 256-32){
                    if(i==16||i==256-32||i==256/2){
                        AreaSegmentCastleTower tower = new AreaSegmentCastleTower(i,j, AreaSegmentCastleWall.DIR_LR,0);
                        addSegment(tower);
                    }else {
                        AreaSegmentCastleWall wall = new AreaSegmentCastleWall(i,j, AreaSegmentCastleWall.DIR_LR,0);
                        addSegment(wall);
                    }
                    isBuilding = false;
                }
                if(i == 0||i== 256-16||j == 0||j== 256-16){
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
        for (int i = 0; i < AreaSegment.HEIGHT; i++) {
            for (int j = 0; j < AreaSegment.WIDTH; j++) {
                map[i+segment.getX0()][j+segment.getY0()] = segment.getMap()[i][j];
            }
        }
    }

    public MapInfoModel[][] getMap() {
        return map;
    }
}
