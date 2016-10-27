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
    public static final int block = 256;
    private MapInfoModel[][] arr = new MapInfoModel[block][block];
    private AreaMapModel areaMapModel = new AreaMapModel();

    public AreaEditor(Area area) {
        this.area = area;
        random = new Random(MapEditor.SEED);
    }
    public AreaMapModel create(){
        for (int i = 0; i < block; i++) {
            for (int j = 0; j < block; j++) {
                MapInfoModel model = getNewMapInfoModel(AreaTile.F_DIRT_01,AreaTile.B_TRANS,AreaTile.D_TRANS,AreaTile.S_SHADOW);
                model.setPos(new GridPoint2(i,j));
                model.setBlock(random.nextDouble()<0.3?AreaTile.B_WALL_01:AreaTile.B_TRANS);
                arr[i][j] = model;
            }
        }
        areaMapModel.setArea(area);
        areaMapModel.setArr(arr);
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
