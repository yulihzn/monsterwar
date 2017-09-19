package com.mw.ui.widget;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;

/**
 * Created by BanditCat on 2016/9/21.
 */
public class FloatRectPixmap extends Pixmap{
    public FloatRectPixmap(byte[] encodedData, int offset, int len) {
        super(encodedData, offset, len);
    }

    public FloatRectPixmap(FileHandle file) {
        super(file);
    }

    public FloatRectPixmap(Gdx2DPixmap pixmap) {
        super(pixmap);
    }

    public FloatRectPixmap(int width, int height, Format format) {
        super(width, height, format);
    }

    public void fillRectangle(float x, float y, float width, float height) {
        super.fillRectangle((int)x, (int)y, (int)width, (int)height);
    }
}
