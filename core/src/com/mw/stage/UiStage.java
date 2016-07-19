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
    private BitmapFont bitmapFont;
    private String level = "关卡Level:0123456789";

    public UiStage(OrthographicCamera camera) {
        this.camera = camera;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/font.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.characters = level;
        fontParameter.size = 20;
        bitmapFont = generator.generateFont(fontParameter);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw() {
        super.draw();
        getBatch().begin();
        bitmapFont.draw(getBatch(),"关卡Level:"+GameDataHelper.getInstance().getCurrentLevel(),10,20+bitmapFont.getCapHeight());
        getBatch().end();

    }

    @Override
    public void dispose() {
        super.dispose();
        generator.dispose();
    }
}
