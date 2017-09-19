package com.mw.ui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by BanditCat on 2016/7/14.
 */
public class GameInfoLabel extends Label {
    public GameInfoLabel(CharSequence text, Skin skin) {
        super(text, skin);
    }

    public GameInfoLabel(CharSequence text, Skin skin, String fontName, Color color) {
        super(text, skin, fontName, color);
    }

    public GameInfoLabel(CharSequence text, Skin skin, String fontName, String colorName) {
        super(text, skin, fontName, colorName);
    }

    public GameInfoLabel(CharSequence text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public GameInfoLabel(CharSequence text, LabelStyle style) {
        super(text, style);
    }
}
