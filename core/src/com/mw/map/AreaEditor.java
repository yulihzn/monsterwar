package com.mw.map;

import com.mw.model.Area;

import java.util.Random;

/**
 * Created by BanditCat on 2016/10/21.
 */

public class AreaEditor {
    private int[][] arr = new int[256][256];
    private Random random;
    private Area area;

    public AreaEditor(Area area) {
        this.area = area;
    }
}
