package com.mw.components.map.circle.section;

import com.mw.components.map.circle.elements.TileType;
import com.mw.components.map.circle.elements.Tiles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yuli.he on 2017/9/11.
 * 区域有一个二维数组
 */

public abstract class Section extends Rect implements Comparable<Section>{
    protected TileType[][] area;
    public int type;
    public int index;
    public List<SectionExit> exits;
    public TileType exitType;

    public Section(int left, int top, int right, int bottom, int type) {
        super(left, top, right, bottom);
        area = new TileType[width()][height()];
        this.type = type;
        exits = new ArrayList<SectionExit>();
        exitType = Tiles.tile().opendoor;
        updateArea();
    }

    public TileType[][] getArea() {
        return area;
    }

    public void setArea(TileType[][] area) {
        this.area = area;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public abstract void updateArea();

    public void updateArea(int x, int y, TileType tileType) {
        if(x>=left&&x<right&&y>=top&&y<bottom){
            area[x-left][y-top] = tileType;
        }
    }

    /**
     * 判断点是否在边界 返回 -1 则不是
     * @param x
     * @param y
     * @return 0:left 1:top 2:right 3:bottom
     */
    public int isSide(int x,int y){
        if(x==left)return 0;
        if(y==top)return 1;
        if(x==right-1)return 2;
        if(y==bottom-1)return 3;
        return -1;
    }

    /**
     * 是否是角落
     * @param x
     * @param y
     * @return
     */
    public boolean isCorner(int x,int y){
        return (x==left&&y==top)||(x==left&&y==bottom-1)||(x==right-1&&y==top)||(x==right-1&&y==bottom-1);
    }

    /**
     * 设置出口
     * @param x
     * @param y
     * @param dir 0:left 1:top 2:right 3:bottom
     */
    public void setExit(int x,int y,int dir){
        if(isCorner(x,y)){
            return;
        }
        exits.add(new SectionExit(x,y,dir));
        updateArea(x,y,exitType);
    }

    /**
     * 移除出口
     * @param x
     * @param y
     */
    public void removeExit(int x,int y){
        Iterator<SectionExit> it = exits.iterator();
        while(it.hasNext()){
            SectionExit se = it.next();
            if(se.x == x && se.y == y){
                it.remove();
                return;
            }
        }
    }


    public int getExitCount() {
        return exits.size();
    }

    public List<SectionExit> getExits() {
        return exits;
    }

    public TileType getExitType() {
        return exitType;
    }

    public void setExitType(TileType exitType) {
        this.exitType = exitType;
    }
}
