package com.mw.map.areasegment;

import com.badlogic.gdx.math.MathUtils;
import com.mw.map.AreaTile;
import com.mw.model.MapInfoModel;

import java.util.Random;

/**
 * Created by BanditCat on 2016/11/4.
 */

public class AreaSegmentStone extends AreaSegment {
    public AreaSegmentStone(int x0, int y0, int style) {
        super(x0, y0,style);
        setStyle();
        build();
    }

    @Override
    protected void setStyle() {
        switch (style){
            case 0:break;
        }
    }

    @Override
    protected void build() {
        map = new MapInfoModel[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                MapInfoModel model = new MapInfoModel();
                model.setFloor(AreaTile.F_DIRT_03);
                model.setBlock(AreaTile.B_TRANS);
                model.setDecorate(AreaTile.D_TRANS);
                model.setShadow(AreaTile.S_SHADOW);
                map[i][j] = model;
            }
        }
    }
}
