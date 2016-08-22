package com.mw.model;

/**
 * Created by BanditCat on 2016/8/18.
 */
public class MapInfoModel {
    private int floor;//地板层
    private int block;//活动层
    private int decorate;//装饰层
    private int shadow;//阴影层
    private int shadowClick;//阴影层探索状态

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getDecorate() {
        return decorate;
    }

    public void setDecorate(int decorate) {
        this.decorate = decorate;
    }

    public int getShadow() {
        return shadow;
    }

    public void setShadow(int shadow) {
        this.shadow = shadow;
    }

    public int getShadowClick() {
        return shadowClick;
    }

    public void setShadowClick(int shadowClick) {
        this.shadowClick = shadowClick;
    }
}
