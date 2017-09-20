package com.mw.components.map.model;

import com.mw.components.map.MapEditor;

import java.util.Random;

/**
 * Created by BanditCat on 2016/10/12.
 */

public class Wild extends com.mw.components.map.model.Area {
    public Wild() {
    }

    public Wild(int x0, int y0, Random random) {
        super(x0, y0,0);
        this.type = MapEditor.WILD;
    }
}
