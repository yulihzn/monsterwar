package com.mw.ui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mw.game.MainGame;
import com.mw.logic.characters.info.PlayerInfo;
import com.mw.ui.screen.MainScreen;
import com.mw.ui.widget.GameInfoLabel;
import com.mw.ui.widget.LazyBitmapFont;
import com.mw.ui.widget.LogMessageTable;
import com.mw.profiles.GameFileHelper;


/**
 * Created by BanditCat on 2016/7/13.
 */
public class UiStage extends Stage {
    private FreeTypeFontGenerator generator;
    private LazyBitmapFont bitmapFont;

    private ImageButton ib_back;
    private ImageButton ib_menu;
    private TextureAtlas atlas;
    private static final float BACK_PADDING = 10f;

    private int scrollWidth,scrollHeight;
    private boolean isScroll = false;

    private GameInfoLabel levelLabel;
    private GameInfoLabel stepLabel;
    private GameInfoLabel hpLabel;
    private GameInfoLabel apLabel;
    private GameInfoLabel dpLabel;

    private LogMessageTable logMessageTable;

    public UiStage(final MainScreen mainScreen) {
        setViewport(new FitViewport(MainGame.worldWidth,MainGame.worldHeight));
        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/font.ttf"));
        bitmapFont = new LazyBitmapFont(generator,16);

        levelLabel = new GameInfoLabel("",new Label.LabelStyle(bitmapFont,Color.WHITE));
        stepLabel = new GameInfoLabel("",new Label.LabelStyle(bitmapFont,Color.WHITE));
        hpLabel = new GameInfoLabel("",new Label.LabelStyle(bitmapFont,Color.WHITE));
        apLabel = new GameInfoLabel("",new Label.LabelStyle(bitmapFont,Color.WHITE));
        dpLabel = new GameInfoLabel("",new Label.LabelStyle(bitmapFont,Color.WHITE));

        atlas = new TextureAtlas(Gdx.files.internal("images/buttons.pack"));
        TextureRegionDrawable imageUp = new TextureRegionDrawable(atlas.findRegion("button_back_normal"));
        TextureRegionDrawable imageDown = new TextureRegionDrawable(atlas.findRegion("button_back_pressed"));
        ib_back = new ImageButton(imageUp, imageDown);
        ib_back.setPosition(BACK_PADDING, MainGame.worldHeight-BACK_PADDING-ib_back.getHeight());
        ib_back.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainScreen.getMainGame().setScreen(mainScreen.getMainGame().getStartScreen());
                super.clicked(event, x, y);
            }

        });
        ib_menu = new ImageButton(imageUp, imageDown);
        ib_menu.setPosition(MainGame.worldWidth-ib_menu.getWidth()-20,20);
        ib_menu.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainScreen.showCharacterStage();
                super.clicked(event, x, y);
            }

        });
        addActor(ib_back);
        addActor(ib_menu);
        logMessageTable = new LogMessageTable();
        addActor(logMessageTable);
        addActor(levelLabel);
        addActor(stepLabel);
        addActor(hpLabel);
        addActor(apLabel);
        addActor(dpLabel);
        logMessageTable.setVisible(false);

    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw() {
        super.draw();
        float w = MainGame.worldWidth;
        float h = MainGame.worldHeight;
        if(logMessageTable != null){
            logMessageTable.setWidth(w/2);
            logMessageTable.setHeight(100);
            logMessageTable.setPosition(0,0);
        }
        PlayerInfo playerInfo = GameFileHelper.getInstance().getPlayerInfo();
        String level = "关卡:"+ GameFileHelper.getInstance().getCurrentLevel();
        String step = "步数:"+ GameFileHelper.getInstance().getCurrentStep(PlayerInfo.NAME);
        String hp = "血量:"+playerInfo.getHealthPoint();
        String ap = "攻击:"+playerInfo.getAttackPoint();
        String dp = "防御:"+playerInfo.getDefensePoint();
        levelLabel.setText(level);
        levelLabel.setPosition(20,20+logMessageTable.getHeight());
        stepLabel.setText(step);
        stepLabel.setPosition(w-stepLabel.getGlyphLayout().width-20,20+logMessageTable.getHeight());

        hpLabel.setText(hp);
        hpLabel.setPosition(w-hpLabel.getGlyphLayout().width-20,h-hpLabel.getGlyphLayout().height-10);
        apLabel.setText(ap);
        apLabel.setPosition(w-hpLabel.getGlyphLayout().width-20,h-hpLabel.getGlyphLayout().height*2-15);
        dpLabel.setText(dp);
        dpLabel.setPosition(w-hpLabel.getGlyphLayout().width-20,h-hpLabel.getGlyphLayout().height*3-20);

        ib_menu.setPosition(w-ib_menu.getWidth()-20,20);

    }

    @Override
    public void dispose() {
        super.dispose();
        generator.dispose();
        bitmapFont.dispose();
        logMessageTable.dispose();
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if(MainGame.worldHeight-screenY < scrollHeight){
            isScroll = true;
        }else {
            isScroll = false;
        }
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return isScroll;
    }

}
