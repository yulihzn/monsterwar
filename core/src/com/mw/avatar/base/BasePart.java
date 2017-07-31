package com.mw.avatar.base;

import com.badlogic.gdx.utils.Array;

/**
 * Created by yuli.he on 2017/7/27.
 * 身体部位基类
 * 需要一个连接点，用来和其它部位连接
 * 需要一个列表存储部位包含的像素点
 * 根据32x32的图
 * <br>
 * 0  0  0  0  0  01 02 03 04 05 0  0  0  0  0 <br>
 * 0  0  0  0  0  06 07 08 09 10 0  0  0  0  0 <br>
 * 0  0  0  0  0  11 12 13 14 15 0  0  0  0  0 <br>
 * 0  0  0  0  0  16 17 18 19 20 0  0  0  0  0 <br>
 * 0  0  0  0  0  21 22 23 24 25 0  0  0  0  0 <br>
 * 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 <br>
 * 0  0  0  0  0  41 42 43 44 45 0  0  0  0  0 <br>
 * 0  0  0  0  0  46 47 48 49 50 0  0  0  0  0 <br>
 * 0  0  0  0  0  51 52 53 54 55 0  0  0  0  0 <br>
 * 0  0  0  0  0  56 57 58 59 60 0  0  0  0  0 <br>
 * 0  0  0  0  0  61 0  0  0  62 0  0  0  0  0 <br>
 * 0  0  0  0  0  63 0  0  0  64 0  0  0  0  0 <br>
 * 0  0  0  0  0  65 0  0  0  66 0  0  0  0  0 <br>
 * 0  0  0  0  0  67 0  0  0  68 0  0  0  0  0 <br>
 * 0  0  0  0  0  69 0  0  0  70 0  0  0  0  0 <br>
 *
 *
 */
public abstract class BasePart {
    protected String name;//名字

    public BasePart() {
        init();
    }

    protected abstract void init();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
