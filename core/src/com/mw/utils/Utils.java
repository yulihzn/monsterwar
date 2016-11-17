package com.mw.utils;

import java.text.SimpleDateFormat;

/**
 * Created by BanditCat on 2016/11/17.
 */

public class Utils {
    public static String getMins(long time) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("mm:ss");
        String date = sDateFormat.format(new java.util.Date(time));
        return date;
    }
}
