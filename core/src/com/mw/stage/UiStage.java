package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mw.logic.characters.info.PlayerInfo;
import com.mw.screen.MainScreen;
import com.mw.screen.StartScreen;
import com.mw.screen.TransferScreen;
import com.mw.utils.GameDataHelper;


/**
 * Created by BanditCat on 2016/7/13.
 */
public class UiStage extends Stage {
    private OrthographicCamera camera;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont bfLevel;
    private BitmapFont bfStep;
    private String str = "关卡:0123456789步数";

    private ImageButton ib_back;
    private TextureAtlas atlas;
    private static final float BACK_PADDING = 10f;

    public UiStage(OrthographicCamera camera, final MainScreen mainScreen) {
        this.camera = camera;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/font.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.characters = str;
        fontParameter.size = 20;
        bfLevel = generator.generateFont(fontParameter);
        bfStep = generator.generateFont(fontParameter);

        atlas = new TextureAtlas(Gdx.files.internal("images/buttons.pack"));
        TextureRegionDrawable imageUp = new TextureRegionDrawable(atlas.findRegion("button_back_normal"));
        TextureRegionDrawable imageDown = new TextureRegionDrawable(atlas.findRegion("button_back_pressed"));
        ib_back = new ImageButton(imageUp, imageDown);
        ib_back.setSize(Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight()/10);
        ib_back.setBounds(BACK_PADDING, Gdx.graphics.getHeight()-BACK_PADDING-ib_back.getWidth()
                ,Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight()/10);
        ib_back.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainScreen.getMainGame().setScreen(new TransferScreen(mainScreen.getMainGame(), new StartScreen(mainScreen.getMainGame())));
                super.clicked(event, x, y);
            }

        });
        addActor(ib_back);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw() {
        super.draw();
        getBatch().begin();
        bfLevel.draw(getBatch(),"关卡:"+GameDataHelper.getInstance().getCurrentLevel(),10,20+bfLevel.getCapHeight());
        bfStep.draw(getBatch(),"步数:"+GameDataHelper.getInstance().getCurrentStep(PlayerInfo.NAME),Gdx.graphics.getWidth()-bfStep.getRegion().getRegionWidth()-10,20+bfStep.getCapHeight());
        getBatch().end();

    }

    @Override
    public void dispose() {
        super.dispose();
        generator.dispose();
    }
}
