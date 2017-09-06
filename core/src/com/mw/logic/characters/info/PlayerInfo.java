package com.mw.logic.characters.info;

/**
 * Created by BanditCat on 2016/7/25.
 */
public class PlayerInfo extends CharacterInfo {
    public static final String NAME = "man";
    public static final String REGION_NAME = "p0004";
    public PlayerInfo() {
        this.setName(NAME);
    }

    public PlayerInfo(int h, int ap, int ar, int dp, int sp,int sr) {
        super(NAME,h, ap, ar, dp, sp,sr);
    }

}
