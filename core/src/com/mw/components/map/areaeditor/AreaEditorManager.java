package com.mw.components.map.areaeditor;

import com.badlogic.gdx.math.GridPoint2;
import com.mw.components.map.MapEditor;
import com.mw.components.map.model.Area;
import com.mw.components.map.model.AreaMapModel;
import com.mw.components.map.model.MapInfoModel;

import java.util.HashMap;

/**
 * Created by BanditCat on 2016/10/21.
 */

public class AreaEditorManager {

    private Area area;
    public static final int WIDTH = 112;//7x16
    public static final int HEIGHT = 112;
    private AreaEditor areaEditor;
    public AreaEditorManager(Area area) {
        this.area = area;
    }
    public AreaMapModel create(){

        switch (area.getType()){
            case CASTLE_00_FOOL:areaEditor = new CastleEditor(area);break;
            case WILD_DIRT:
            case WILD_FOREST:
            case WILD_WATER:
            case WILD_GRASS:areaEditor = new WildEditor(area);break;
            case DUNGEON:areaEditor = new DungeonEditor(area);break;
            default:areaEditor = new EmptyEditor(area);break;
        }

        return areaEditor.getAreaMapModel();
    }
}
