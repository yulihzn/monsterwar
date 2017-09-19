package com.mw.components.map.circle.section;


import com.mw.components.map.circle.elements.Tiles;

/**
 * Created by yuli.he on 2017/9/11.
 */

public class RestSection extends Section {
    public RestSection(int left, int top, int right, int bottom, int type) {
        super(left, top, right, bottom, type);

    }

    @Override
    public void updateArea() {
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                area[i][j] = Tiles.tile().roomfloor;
                if(i==0||j==0||i==width()-1||j==height()-1){
                    area[i][j] = Tiles.tile().roomwall;
                    if(isCorner(i+left,j+top)){
                        area[i][j] = Tiles.tile().roomcorner;
                    }
                }

            }
        }
    }

    @Override
    public int compareTo(Section o) {
        return this.getType()-o.getType();
    }


}
