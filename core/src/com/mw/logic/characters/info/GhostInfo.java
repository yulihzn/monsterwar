package com.mw.logic.characters.info;

/**
 * Created by BanditCat on 2016/7/26.
 */
public class GhostInfo extends CharacterInfo {
    public static final String NAME = "ghost";

    public GhostInfo(int h, int ap, int ar, int dp, int sp) {
        super(h, ap, ar, dp, sp);
    }

    public GhostInfo() {
        setName(NAME);
    }
}
