package com.mw.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * Created by BanditCat on 2016/9/18.
 */
public class CharacterWindow extends Window {
    public CharacterWindow(String title, Skin skin) {
        super(title, skin);
    }

    public CharacterWindow(String title, Skin skin, String styleName) {
        super(title, skin, styleName);
    }

    public CharacterWindow(String title, WindowStyle style) {
        super(title, style);
    }
}
