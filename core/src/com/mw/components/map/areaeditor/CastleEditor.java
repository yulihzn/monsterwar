package com.mw.components.map.areaeditor;

import com.mw.components.map.MapEditor;
import com.mw.components.map.areasegment.AreaSegment;
import com.mw.components.map.areasegment.AreaSegmentCastleBuilding;
import com.mw.components.map.model.Area;
import com.mw.components.map.model.MapInfoModel;

import java.util.Random;

/**
 * Created by BanditCat on 2016/10/19.
 *
 *
 */

public class CastleEditor extends AreaEditor {
    public CastleEditor(Area area) {
        super(area);
    }

    @Override
    protected void initArea() {
        for (int i = 0; i < width; i+=segSize) {
            for (int j = 0; j < height; j+=segSize) {
                boolean isBuilding = true;
                if(i == segSize||i== width-segSize*2){
                    if(j==segSize||j==height-segSize*2||j==height/2){
                        com.mw.components.map.areasegment.AreaSegmentCastleTower tower = new com.mw.components.map.areasegment.AreaSegmentCastleTower(i,j, com.mw.components.map.areasegment.AreaSegmentCastleWall.DIR_TB,0);
                        addSegment(tower);
                    }else {
                        com.mw.components.map.areasegment.AreaSegmentCastleWall wall = new com.mw.components.map.areasegment.AreaSegmentCastleWall(i,j, com.mw.components.map.areasegment.AreaSegmentCastleWall.DIR_TB,0);
                        addSegment(wall);
                    }
                    isBuilding =false;
                }
                if(j == segSize||j== height-segSize*2){
                    if(i==segSize||i==width-segSize*2||i==width/2){
                        com.mw.components.map.areasegment.AreaSegmentCastleTower tower = new com.mw.components.map.areasegment.AreaSegmentCastleTower(i,j, com.mw.components.map.areasegment.AreaSegmentCastleWall.DIR_LR,0);
                        addSegment(tower);
                    }else {
                        com.mw.components.map.areasegment.AreaSegmentCastleWall wall = new com.mw.components.map.areasegment.AreaSegmentCastleWall(i,j, com.mw.components.map.areasegment.AreaSegmentCastleWall.DIR_LR,0);
                        addSegment(wall);
                    }
                    isBuilding = false;
                }
                if(i == 0||i== width-segSize||j == 0||j== width-segSize){
                    com.mw.components.map.areasegment.AreaSegmentCastleWater water = new com.mw.components.map.areasegment.AreaSegmentCastleWater(i,j,0);
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
}
