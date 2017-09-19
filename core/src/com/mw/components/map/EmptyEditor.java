package com.mw.components.map;

import com.mw.components.map.areasegment.AreaSegment;
import com.mw.components.map.model.Area;
import com.mw.components.map.model.MapInfoModel;

/**
 * Created by BanditCat on 2016/10/19.
 *
 *
 */

public class EmptyEditor {
    public static final int width = AreaEditor.WIDTH;
    public static final int height = AreaEditor.HEIGHT;
    public static final int segsize = AreaSegment.size;
    private MapInfoModel[][] map = new MapInfoModel[width][height];
    private Area area;

    public EmptyEditor(Area area) {
        this.area = area;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                MapInfoModel model = new MapInfoModel();
                model.setFloor(com.mw.components.map.AreaTile.F_DIRT_01);
                model.setBlock(com.mw.components.map.AreaTile.B_TRANS);
                model.setDecorate(com.mw.components.map.AreaTile.D_TRANS);
                model.setShadow(com.mw.components.map.AreaTile.S_SHADOW);
                map[i][j] = model;
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
