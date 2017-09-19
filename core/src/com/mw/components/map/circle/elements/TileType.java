package com.mw.components.map.circle.elements;

/**
 * Created by yuli.he on 2017/9/11.
 */

public class TileType {

    public int value;
    public String name;
    public boolean isObstacle;

    public TileType(String name,int value,  boolean isObstacle) {
        this.name = name;
        this.value = value;
        this.isObstacle = isObstacle;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    public void setObstacle(boolean obstacle) {
        isObstacle = obstacle;
    }
}
