package com.mw.components.map.circle.utils;


import com.mw.components.map.circle.elements.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuli.he on 2017/9/11.
 */

public class MathUtils {
    public static List<Point> getLinePoints(Point start, Point end) {
        List<Point> list = new ArrayList<Point>();
        int x0 = start.x;
        int y0 = start.y;
        int x1 = end.x;
        int y1 = end.y;

        int x = x0;
        int y = y0;

        int w = x1 - x0;
        int h = y1 - y0;

        int dx1 = w < 0 ? -1 : (w > 0 ? 1 : 0);
        int dy1 = h < 0 ? -1 : (h > 0 ? 1 : 0);

        int dx2 = w < 0 ? -1 : (w > 0 ? 1 : 0);
        int dy2 = 0;

        int fastStep = Math.abs(w);
        int slowStep = Math.abs(h);
        if (fastStep <= slowStep) {
            fastStep = Math.abs(h);
            slowStep = Math.abs(w);

            dx2 = 0;
            dy2 = h < 0 ? -1 : (h > 0 ? 1 : 0);
        }
        int numerator = fastStep >> 1;

        for (int i = 0; i <= fastStep; i++) {
            list.add(new Point(x, y));
            numerator += slowStep;
            if (numerator >= fastStep) {
                numerator -= fastStep;
                x += dx1;
                y += dy1;
            } else {
                x += dx2;
                y += dy2;
            }
        }
        return list;
    }

    public static List<Point> getCirclePoints(int x0, int y0, int r) {
        List<Point> list = new ArrayList<Point>();
        int x, y;
        x = 0;
        y = r;
        int d = 1 - r;
        //起点(0,R),下一点中点(1,R-0.5),d=1*1+(R-0.5)*(R-0.5)-R*R=1.25-R,d只参与整数运算，所以小数部分可省略
        // y>x即第一象限的第1区八分圆
        while (y >= x) {
            list.add(new Point(x + x0, y + y0));
            list.add(new Point(y + x0, x + y0));
            list.add(new Point(-x + x0, y + y0));
            list.add(new Point(-y + x0, x + y0));

            list.add(new Point(-x + x0, -y + y0));
            list.add(new Point(-y + x0, -x + y0));
            list.add(new Point(x + x0, -y + y0));
            list.add(new Point(y + x0, -x + y0));
            if (d < 0) {
                d = d + 2 * x + 3;
            } else {
                d = d + 2 * (x - y) + 5;
                y--;
            }
            x++;
        }


        return list;
    }

    /**
     * 获取适合地牢的圆
     * @param x0
     * @param y0
     * @param r
     * @return
     */
    public static List<Point> getCircleDungeonPoints(int x0, int y0, int r) {
        List<Point> list = new ArrayList<Point>();
        List<List<Point>> eightlist = new ArrayList<List<Point>>();
        for (int i = 0; i < 8; i++) {
            eightlist.add(new ArrayList<Point>());
        }
        int x, y;
        x = 0;
        y = r;
        int d = 1 - r;
        //起点(0,R),下一点中点(1,R-0.5),d=1*1+(R-0.5)*(R-0.5)-R*R=1.25-R,d只参与整数运算，所以小数部分可省略
        // y>x即第一象限的第1区八分圆
        while (y >= x) {
            addEightPoint(eightlist,x0,y0,x,y);
            if (d < 0) {
                d = d + 2 * x + 3;
            } else {
                d = d + 2 * (x - y) + 5;
                y--;
                addEightPoint(eightlist,x0,y0,x,y);
            }
            x++;
        }
        for (int i = 0; i < eightlist.size(); i++) {
            list.addAll(eightlist.get(i));
        }
        return list;
    }
    private static void addEightPoint(List<List<Point>> eightlist,int x0,int y0,int x,int y){
        //右边负半圆靠近y轴的圆弧为第一个圆弧(因为代码里面坐标轴正方向向下),逆时针添加其余圆弧
        eightlist.get(0).add(new Point(x + x0, y + y0));//1
        eightlist.get(1).add(0,new Point(y + x0, x + y0));//2
        eightlist.get(2).add(new Point(y + x0, -x + y0));//3
        eightlist.get(3).add(0,new Point(x + x0, -y + y0));//4
        eightlist.get(4).add(new Point(-x + x0, -y + y0));//5
        eightlist.get(5).add(0,new Point(-y + x0, -x + y0));//6
        eightlist.get(6).add(new Point(-y + x0, x + y0));//7
        eightlist.get(7).add(0,new Point(-x + x0, y + y0));//8


    }


}
