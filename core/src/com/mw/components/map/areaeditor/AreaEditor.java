package com.mw.components.map.areaeditor;

import com.mw.components.map.MapEditor;
import com.mw.components.map.areasegment.AreaSegment;
import com.mw.components.map.model.Area;
import com.mw.components.map.model.MapInfoModel;

import java.util.Random;

/**
 * Created by yuli.he on 2017/9/19.
 */

public abstract class AreaEditor {
    public static final int width = AreaEditorManager.WIDTH;
    public static final int height = AreaEditorManager.HEIGHT;
    public static final int segSize = AreaSegment.SIZE;
    protected MapInfoModel[][] map = new MapInfoModel[width][height];
    protected Area area;
    protected Random random = new Random(MapEditor.SEED);

    public AreaEditor(Area area) {
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
        initArea();
    }
    protected void addSegment(AreaSegment segment){
        for (int i = 0; i < segSize; i++) {
            for (int j = 0; j < segSize; j++) {
                map[i+segment.getX0()][j+segment.getY0()] = segment.getMap()[i][j];
            }
        }
    }
    protected abstract void initArea();

    public MapInfoModel[][] getMap() {
        return map;
    }
}
