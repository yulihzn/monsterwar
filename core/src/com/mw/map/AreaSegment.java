package com.mw.map;

import com.mw.model.MapInfoModel;

/**
 * Created by BanditCat on 2016/11/4.
 * 图块是地图的组成元素，大小是16x16或以下任意的图形
 */

public abstract class AreaSegment {
    protected int x0;//顶点x
    protected int y0;//顶点y
    protected int width;//宽度
    protected int height;//高度
    protected MapInfoModel[][] map;//数组(包含顶点信息)

    public AreaSegment(int x0, int y0) {
        this.x0 = x0;
        this.y0 = y0;
    }

    protected abstract void build();

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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public void reBuild(){
        build();
    }
}
