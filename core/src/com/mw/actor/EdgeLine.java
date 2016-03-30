package com.mw.actor;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by BanditCat on 2016/3/29.
 */
public class EdgeLine implements Comparable<EdgeLine> {
    private int id = -1;
    private boolean isNeedToDraw = true;

    public boolean isNeedToDraw() {
        return isNeedToDraw;
    }

    public void setNeedToDraw(boolean needToDraw) {
        isNeedToDraw = needToDraw;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private Vector2  start = new Vector2();// 起始点
    private Vector2 end = new Vector2();//结束点
    private float distance = 0;//距离
    private int prev = -1;//上一条id
    private int next = -1;//下一条id

    public EdgeLine() {
    }

    public EdgeLine(Vector2 start, Vector2 end, float distance, int prev, int next) {
        this.start = start;
        this.end = end;
        this.distance = distance;
        this.prev = prev;
        this.next = next;
    }

    public EdgeLine(Vector2 start, Vector2 end) {
        this.start = start;
        this.end = end;
    }

    public Vector2 getStart() {
        return start;
    }

    public Vector2 getEnd() {
        return end;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getPrev() {
        return prev;
    }

    public int getNext() {
        return next;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public void setNext(int next) {
        this.next = next;
    }

    @Override
    public int compareTo(EdgeLine o) {
        return (int)(this.getDistance() - o.getDistance());
    }
}
