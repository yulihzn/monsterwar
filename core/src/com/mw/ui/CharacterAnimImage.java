package com.mw.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mw.avatar.main.Avatar;

/**
 * Created by yuli.he on 2017/7/31.
 */

public class CharacterAnimImage extends Image{
    private Avatar avatar;
    public CharacterAnimImage() {
        super();
        avatar = new Avatar();
        this.setDrawable(new TextureRegionDrawable(new TextureRegion(avatar.getTexture())));
        avatar.playLoop();
    }
    public void dispose(){
        avatar.stop();
        avatar.dispose();
    }
}
