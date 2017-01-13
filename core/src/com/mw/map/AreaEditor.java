package com.mw.map;

import com.badlogic.gdx.math.GridPoint2;
import com.mw.model.Area;
import com.mw.model.AreaMapModel;
import com.mw.model.MapInfoModel;

import java.util.Random;

/**
 * Created by BanditCat on 2016/10/21.
 */

public class AreaEditor {

    private Random random;
    private Area area;
    public static final int WIDTH = 112;
    public static final int HEIGHT = 112;
    private MapInfoModel[][] arr = new MapInfoModel[WIDTH][HEIGHT];
    private AreaMapModel areaMapModel = new AreaMapModel();

    public AreaEditor(Area area) {
        this.area = area;
        random = new Random(MapEditor.SEED);
    }
    public AreaMapModel create(){
//        for (int i = 0; i < block; i++) {
//            for (int j = 0; j < block; j++) {
//                MapInfoModel model = getNewMapInfoModel(AreaTile.F_DIRT_01,AreaTile.B_TRANS,AreaTile.D_TRANS,AreaTile.S_SHADOW);
//                model.setPos(new GridPoint2(i,j));
//                model.setBlock(random.nextDouble()<0.3?AreaTile.B_WALL_01:AreaTile.B_TRANS);
//                if(i==1&&j==1){
//                    model.setBlock(AreaTile.B_DOWNSTAIRS_01);
//                }
//                arr[i][j] = model;
//            }
//        }
        areaMapModel.setArea(area);
        switch (area.getType()){
            case MapEditor.CASTLE:areaMapModel.setArr(new CastleEditor(area).getMap());break;
            case MapEditor.WILD:areaMapModel.setArr(new WildEditor(area).getMap());break;
            case MapEditor.VILLAGE:areaMapModel.setArr(new WildEditor(area).getMap());break;
            default:areaMapModel.setArr(new EmptyEditor(area).getMap());break;
        }
        return areaMapModel;
    }

    private MapInfoModel getNewMapInfoModel(int floor,int block,int decorate,int shadow) {
        MapInfoModel model = new MapInfoModel();
        model.setFloor(floor);
        model.setBlock(block);
        model.setDecorate(decorate);
        model.setShadow(shadow);
        return model;
    }

    public MapInfoModel[][] getArr() {
        return arr;
    }

    public void setArr(MapInfoModel[][] arr) {
        this.arr = arr;
    }
}
