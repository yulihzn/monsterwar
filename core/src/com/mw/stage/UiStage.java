package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mw.logic.Logic;
import com.mw.logic.characters.info.PlayerInfo;
import com.mw.screen.MainScreen;
import com.mw.screen.StartScreen;
import com.mw.screen.TransferScreen;
import com.mw.ui.GameInfoLabel;
import com.mw.ui.LogMessageTable;
import com.mw.utils.GameDataHelper;


/**
 * Created by BanditCat on 2016/7/13.
 */
public class UiStage extends Stage {
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont bitmapFont;
    private String str = "关卡:-0123456789步数攻击血量防御abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private ImageButton ib_back;
    private TextureAtlas atlas;
    private static final float BACK_PADDING = 10f;

    private Texture texture;
    private int scrollWidth,scrollHeight;
    private boolean isScroll = false;

    private GameInfoLabel levelLabel;
    private GameInfoLabel stepLabel;
    private GameInfoLabel hpLabel;
    private GameInfoLabel apLabel;
    private GameInfoLabel dpLabel;

    private LogMessageTable logMessageTable;

    public UiStage(final MainScreen mainScreen) {
        setViewport(new ScreenViewport());
        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/font.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.characters = str;
        fontParameter.size = 16;
        bitmapFont = generator.generateFont(fontParameter);

        levelLabel = new GameInfoLabel("",new Label.LabelStyle(bitmapFont,Color.WHITE));
        stepLabel = new GameInfoLabel("",new Label.LabelStyle(bitmapFont,Color.WHITE));
        hpLabel = new GameInfoLabel("",new Label.LabelStyle(bitmapFont,Color.WHITE));
        apLabel = new GameInfoLabel("",new Label.LabelStyle(bitmapFont,Color.WHITE));
        dpLabel = new GameInfoLabel("",new Label.LabelStyle(bitmapFont,Color.WHITE));


        atlas = new TextureAtlas(Gdx.files.internal("images/buttons.pack"));
        TextureRegionDrawable imageUp = new TextureRegionDrawable(atlas.findRegion("button_back_normal"));
        TextureRegionDrawable imageDown = new TextureRegionDrawable(atlas.findRegion("button_back_pressed"));
        ib_back = new ImageButton(imageUp, imageDown);
        ib_back.setPosition(BACK_PADDING, Gdx.graphics.getHeight()-BACK_PADDING-ib_back.getHeight());
        ib_back.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainScreen.getMainGame().setScreen(new TransferScreen(mainScreen.getMainGame(), new StartScreen(mainScreen.getMainGame())));
                super.clicked(event, x, y);
            }

        });
        addActor(ib_back);
        logMessageTable = new LogMessageTable();
//        addScrollPane();
        addActor(levelLabel);
        addActor(stepLabel);
        addActor(hpLabel);
        addActor(apLabel);
        addActor(dpLabel);

    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw() {
        super.draw();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        if(logMessageTable != null){
            logMessageTable.setWidth(w/2);
            logMessageTable.setHeight(100);
            logMessageTable.setPosition(0,0);
        }
        PlayerInfo playerInfo = GameDataHelper.getInstance().getPlayerInfo();
        String level = "关卡:"+GameDataHelper.getInstance().getCurrentLevel();
        String step = "步数:"+GameDataHelper.getInstance().getCurrentStep(PlayerInfo.NAME);
        String hp = "血量:"+playerInfo.getHealthPoint();
        String ap = "攻击:"+playerInfo.getAttackPoint();
        String dp = "防御:"+playerInfo.getDefensePoint();
        levelLabel.setText(level);
        levelLabel.setPosition(20,20);
        if(logMessageTable !=null){
            levelLabel.setPosition(20,20+logMessageTable.getHeight());
        }
        stepLabel.setText(step);
        stepLabel.setPosition(w-stepLabel.getGlyphLayout().width-20,20);
        if(logMessageTable !=null){
            stepLabel.setPosition(w-stepLabel.getGlyphLayout().width-20,20+logMessageTable.getHeight());
        }

        hpLabel.setText(hp);
        hpLabel.setPosition(w-hpLabel.getGlyphLayout().width-20,h-hpLabel.getGlyphLayout().height-10);
        apLabel.setText(ap);
        apLabel.setPosition(w-hpLabel.getGlyphLayout().width-20,h-hpLabel.getGlyphLayout().height*2-15);
        dpLabel.setText(dp);
        dpLabel.setPosition(w-hpLabel.getGlyphLayout().width-20,h-hpLabel.getGlyphLayout().height*3-20);

    }

    @Override
    public void dispose() {
        super.dispose();
        generator.dispose();
        texture.dispose();
        bitmapFont.dispose();
    }

    private Table container;
    private void addScrollPane(){
        scrollWidth = Gdx.graphics.getWidth()/2;
        scrollHeight = 100;
        Pixmap pixmap = new Pixmap(scrollWidth, scrollHeight, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        // Create a texture to contain the pixmap
        texture = new Texture(scrollWidth, scrollHeight, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        pixmap.setColor(new Color(0,0,0,0.8f));
        pixmap.fillRectangle(0,0,scrollWidth,scrollHeight);
        texture.draw(pixmap, 0, 0);
        TextureRegion textureRegion = new TextureRegion(texture,scrollWidth,scrollHeight);

        container = new Table();
        addActor(container);
        container.setBackground(new TextureRegionDrawable(textureRegion));
        container.setPosition(0,0);
        container.setWidth(scrollWidth);
        container.setHeight(scrollHeight);
        Table table = new Table();
        final ScrollPane scrollPane = new ScrollPane(table);
        table.pad(5).defaults().expandX().space(5);
        for (int i = 0; i < 10; i++) {
            table.row();
            Label label = new Label(i+"12345678",new Label.LabelStyle(bitmapFont, Color.WHITE));
            label.setText(label.getText()+"防御123456781234防御1234567812345防御1234567812345防御1234567812345防御1234567812345防御1234567812345防御12345678123455");
            label.setWidth(scrollWidth);
            label.setWrap(true);
            table.add(label).expandX().fillX();
        }
        container.add(scrollPane).expand().fill().colspan(5);
        container.row().space(5).padBottom(5);
        container.addAction(Actions.fadeOut(0));
        container.setVisible(false);

    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if(Gdx.graphics.getHeight()-screenY < scrollHeight){
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

    private Logic.LogMessageListener logMessageListener = new Logic.LogMessageListener() {

        @Override
        public void sendMessage(String msg) {
            container.setVisible(true);
            container.addAction(Actions.sequence(Actions.fadeIn(0.1f),Actions.delay(1,Actions.fadeOut(3f)),Actions.run(new Runnable() {
                @Override
                public void run() {
                    container.setVisible(false);
                }
            })));
        }
    };
}
