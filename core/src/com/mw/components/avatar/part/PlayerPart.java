package com.mw.components.avatar.part;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuli.he on 2017/7/27.
 * 人物的身体
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
 */
public class PlayerPart {
    public int color_skin = 0xffffccff;
    public int color_eyes = 0x000000ff;
    public int color_lips = 0xff6666ff;
    public int color_armr = 0xffffccff;
    public int color_arml = 0xffffccff;
    public int color_legl = 0xffffccff;
    public int color_legr = 0xffffccff;
    public int color_body = 0xffffccff;
    private Map<Integer, Integer> colors = new HashMap<Integer, Integer>();
    private String name;//名字

    public PlayerPart() {
        init();
    }

    protected void init() {
        initColors();
    }

    private void initColors() {
        //给每一个点设置颜色
        for (int i = 1; i < 71; i++) {

            if (i >= 1 && i <= 25) {
                //head
                colors.put(i, color_skin);
            } else if (i >= 26 && i <= 30) {
                //right arm
                colors.put(i, color_armr);
            } else if (i >= 31 && i <= 35) {
                //body
                colors.put(i, color_body);
            } else if (i >= 36 && i <= 40) {
                //left arm
                colors.put(i, color_arml);
            } else if (i >= 41 && i <= 60) {
                //body
                colors.put(i, color_body);
            } else if (i >= 61) {
                //right leg
                if (i % 2 == 1) {
                    colors.put(i, color_legr);
                }
                //left leg
                else {
                    colors.put(i, color_legl);
                }
            }
            //eyes
            if (i == 12 || i == 14) {
                colors.put(i, color_eyes);
            }
            //mouth
            if (i == 23) {
                colors.put(i, color_lips);
            }
        }
    }

    public int getColor(int index) {
        Integer i = colors.get(index);
        if (i == null) {
            return color_skin;
        }
        return i;
    }


}
