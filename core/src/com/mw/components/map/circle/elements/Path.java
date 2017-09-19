package com.mw.components.map.circle.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuli.he on 2017/9/11.
 * 路径
 */

public class Path {
    private TileType tileType;
    private List<Point> list;

    public List<Point> getList() {
        return list;
    }

    public void setList(List<Point> list) {
        this.list = list;
    }
    public Path(List<Point> list,TileType tileType) {
        this.list = list;
        this.tileType = tileType;
    }
    public Path(List<Point> list) {
        this.list = list;
        tileType = Tiles.tile().corridorfloor;
    }
    public Path(TileType tileType) {
        this.tileType = tileType;
        list = new ArrayList<Point>();
    }

    public Path() {
        tileType = Tiles.tile().corridorfloor;
        list = new ArrayList<Point>();
    }
    public int size(){
        return list==null?0:list.size();
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }
}
