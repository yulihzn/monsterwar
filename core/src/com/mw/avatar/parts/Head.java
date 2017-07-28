package com.mw.avatar.parts;

import com.badlogic.gdx.utils.Array;
import com.mw.avatar.base.AvatarPixel;
import com.mw.avatar.base.BasePart;

/**
 * Created by yuli.he on 2017/7/27.
 */
public class Head extends BasePart {

    @Override
    protected void init() {
        setName("head");
        setIndex(0);
        connectPixel.getPosition().x = 0;
        connectPixel.getPosition().y = 0;
    }
}
