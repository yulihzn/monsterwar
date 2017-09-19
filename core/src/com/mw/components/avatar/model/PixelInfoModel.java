package com.mw.components.avatar.model;

import com.badlogic.gdx.utils.Array;

/**
 * Created by yuli.he on 2017/7/28.
 */

public class PixelInfoModel {
    private int  index;
    private String tag;
    private Array<PixelIndexModel> list = new Array<PixelIndexModel>();

    public Array<PixelIndexModel> getList() {
        return list;
    }

    public void setList(Array<PixelIndexModel> list) {
        this.list = list;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
