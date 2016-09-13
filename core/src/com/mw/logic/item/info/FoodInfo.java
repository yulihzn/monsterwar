package com.mw.logic.item.info;

/**
 * Created by BanditCat on 2016/9/5.
 */
public class FoodInfo extends ItemInfo {
    public static final String NAME = "food";
    public static final String REGION_NAME = "food1";


    public FoodInfo() {
        this.setName(NAME);
    }

    public FoodInfo(int h, int ap, int ar, int dp, int sp) {
        super(NAME, h, ap, ar, dp, sp);
    }
}
