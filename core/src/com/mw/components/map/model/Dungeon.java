package com.mw.components.map.model;

import com.mw.components.map.MapEditor;

/**
 * Created by yuli.he on 2017/9/20.
 */

public class Dungeon extends Area {
    public Dungeon(int x0, int y0) {
        super(x0, y0);
        this.type = MapEditor.DUNGEON;
    }

}
