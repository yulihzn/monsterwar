package com.mw.components.map.areasegment;

import com.mw.components.map.AreaTile;
import com.mw.components.map.model.MapInfoModel;

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
        map = new MapInfoModel[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                MapInfoModel model = new MapInfoModel(i,j);
                model.setFloor(AreaTile.F_DIRT_03);
                model.setBlock(AreaTile.B_TRANS);
                model.setDecorate(AreaTile.D_TRANS);
                model.setShadow(AreaTile.S_SHADOW);
                map[i][j] = model;
            }
        }
    }
}
