package com.mw.map.areasegment;

import com.mw.map.AreaTile;
import com.mw.model.MapInfoModel;

/**
 * Created by BanditCat on 2016/11/4.
 */

public class AreaSegmentWater extends AreaSegment {
    public AreaSegmentWater(int x0, int y0, int style) {
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
        map = new MapInfoModel[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                MapInfoModel model = new MapInfoModel();
                model.setFloor(AreaTile.F_WATER_02);
                model.setBlock(AreaTile.B_TRANS);
                model.setDecorate(AreaTile.D_TRANS);
                model.setShadow(AreaTile.S_SHADOW);
                map[i][j] = model;
            }
        }
    }
}
