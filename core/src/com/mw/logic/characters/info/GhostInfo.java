package com.mw.logic.characters.info;

/**
 * Created by BanditCat on 2016/7/26.
 */
public class GhostInfo extends CharacterInfo {
    public static final String NAME = "ghost";
    public static final String REGION_NAME = "p0002";

    public GhostInfo() {
        this.setName(NAME);
    }

    public GhostInfo(int h, int ap, int ar, int dp, int sp, int sr) {
        super(NAME,h, ap, ar, dp, sp,sr);
    }
}
