package com.mw.components.map.areaeditor;

import com.mw.components.map.MapEditor;
import com.mw.components.map.model.Area;
import com.mw.components.map.model.AreaMapModel;
import com.mw.components.map.model.MapInfoModel;

/**
 * Created by BanditCat on 2016/10/21.
 */

public class AreaEditorManager {

    private Area area;
    public static final int WIDTH = 112;//7x16
    public static final int HEIGHT = 112;
    private MapInfoModel[][] arr = new MapInfoModel[WIDTH][HEIGHT];
    private AreaMapModel areaMapModel = new AreaMapModel();

    public AreaEditorManager(Area area) {
        this.area = area;
    }
    public AreaMapModel create(){
        areaMapModel.setArea(area);
        switch (area.getType()){
            case MapEditor.CASTLE:areaMapModel.setArr(new CastleEditor(area).getMap());break;
            case MapEditor.WILD:areaMapModel.setArr(new WildEditor(area).getMap());break;
            case MapEditor.VILLAGE:areaMapModel.setArr(new WildEditor(area).getMap());break;
            case MapEditor.DUNGEON:areaMapModel.setArr(new DungeonEditor(area).getMap());break;
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
