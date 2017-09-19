package com.mw.components.map.circle.section;



/**
 * Created by yuli.he on 2017/9/11.
 */

public class SectionUtils {
    public static final int REST = 0;
    public static final int REST1 = 1;
    public static final int DANGER = 2;
    public static final int PUZZLE = 3;
    public static final int TRAP = 4;
    public static final int TREASURE = 5;

    public static RestSection getRestSection(int left, int top, int right, int bottom) {
        return new RestSection(left, top, right, bottom, REST);
    }

    public static RestSection getRestSection(Section section) {
        return new RestSection(section.left, section.top, section.right, section.bottom, REST);
    }

    public static RestSection getRestSection1(Section section) {
        return new RestSection(section.left, section.top, section.right, section.bottom, REST);
    }

    public static RestSection getRestSection1(int left, int top, int right, int bottom) {
        return new RestSection(left, top, right, bottom, REST1);
    }
}
