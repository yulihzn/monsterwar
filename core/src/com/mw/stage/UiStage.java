package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mw.utils.GameDataHelper;


/**
 * Created by BanditCat on 2016/7/13.
 */
public class UiStage extends Stage {
    private OrthographicCamera camera;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private Label label_level;

    public UiStage(OrthographicCamera camera) {
        this.camera = camera;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/pixelFont.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 40;
        BitmapFont bitmapFont = generator.generateFont(fontParameter);
        Label.LabelStyle labelStyle = new Label.LabelStyle(bitmapFont, Color.WHITE);
        label_level = new Label("Level:",labelStyle);
        label_level.setName("levelLabel");
        label_level.setPosition(10,20);
        addActor(label_level);

    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw() {
        super.draw();
        Label label = (Label) getRoot().findActor("levelLabel");
        label.setText("Level:" + GameDataHelper.getInstance().getCurrentLevel());
        // 更新X值以保证显示位置正确性
        label.setX(label.getWidth());
    }

    @Override
    public void dispose() {
        super.dispose();
        generator.dispose();
    }
}
