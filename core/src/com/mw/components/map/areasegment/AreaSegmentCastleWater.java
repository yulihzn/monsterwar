package com.mw.components.map.areasegment;

import com.mw.components.map.model.MapInfoModel;

/**
 * Created by BanditCat on 2016/11/4.
 */

public class AreaSegmentCastleWater extends AreaSegment {
    public AreaSegmentCastleWater(int x0, int y0,int style) {
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
                model.setFloor(com.mw.components.map.AreaTile.F_WATER_01);
                model.setBlock(com.mw.components.map.AreaTile.B_TRANS);
                model.setDecorate(com.mw.components.map.AreaTile.D_TRANS);
                model.setShadow(com.mw.components.map.AreaTile.S_SHADOW);
                map[i][j] = model;
            }
        }
    }
}
