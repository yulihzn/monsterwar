package com.mw.map;

import com.mw.model.MapInfoModel;

/**
 * Created by BanditCat on 2016/11/4.
 */

public class AreaSegmentCastleWater extends AreaSegment{
    public AreaSegmentCastleWater(int x0, int y0) {
        super(x0, y0);
        build();
    }

    @Override
    protected void build() {
        width=16;
        height=16;
        map = new MapInfoModel[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                MapInfoModel model = new MapInfoModel();
                model.setFloor(AreaTile.F_WATER_01);
                model.setBlock(AreaTile.B_TRANS);
                model.setDecorate(AreaTile.D_TRANS);
                model.setShadow(AreaTile.S_SHADOW);
                map[i][j] = model;
            }
        }
    }
}
