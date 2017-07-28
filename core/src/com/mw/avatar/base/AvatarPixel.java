package com.mw.avatar.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;

/**
 * Created by yuli.he on 2017/7/27.
 */

public class AvatarPixel {
    public GridPoint2 position;//坐标
    public Color color;//颜色
    public boolean isVisible = true;

    public AvatarPixel() {
    }

    public AvatarPixel(GridPoint2 position) {
        this.position = position;
        this.color = Color.BLACK;
    }

    public AvatarPixel(GridPoint2 position, Color color) {
        this.position = position;
        this.color = color;
    }

    public AvatarPixel(Color color) {
        this.color = color;
        this.position = new GridPoint2(0,0);
    }

    public GridPoint2 getPosition() {
        return position;
    }

    public void setPosition(GridPoint2 position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
