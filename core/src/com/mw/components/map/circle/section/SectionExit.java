package com.mw.components.map.circle.section;

/**
 * Created by yuli.he on 2017/9/15.
 * 出口
 */

public class SectionExit {
    public int x;
    public int y;
    public int dir;

    public SectionExit(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }
}
