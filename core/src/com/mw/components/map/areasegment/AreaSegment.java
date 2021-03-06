package com.mw.components.map.areasegment;

import com.mw.components.map.model.MapInfoModel;

/**
 * Created by BanditCat on 2016/11/4.
 * 图块是大区域16等分的小区域，大小是7x7任意的区域
 */

public abstract class AreaSegment {
    protected int x0;//顶点x
    protected int y0;//顶点y
    protected int style;//类型
    public static final int SIZE = 7;
    protected MapInfoModel[][] map = new MapInfoModel[SIZE][SIZE];//数组(包含顶点信息)

    public AreaSegment(int x0, int y0,int style) {
        this.x0 = x0;
        this.y0 = y0;
        this.style = style;
    }

    protected abstract void build();
    protected abstract void setStyle();

    public MapInfoModel[][] getMap() {
        return map;
    }

    public int getX0() {
        return x0;
    }

    public void setX0(int x0) {
        this.x0 = x0;
    }

    public int getY0() {
        return y0;
    }

    public void setY0(int y0) {
        this.y0 = y0;
    }

    public void reBuild(){
        build();
    }
}
