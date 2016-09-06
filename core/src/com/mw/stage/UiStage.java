package com.mw.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
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
    private BitmapFont bfHp;
    private BitmapFont bfAp;
    private BitmapFont bfDp;
    private String str = "关卡:-0123456789步数攻击血量防御abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private ImageButton ib_back;
    private TextureAtlas atlas;
    private static final float BACK_PADDING = 10f;

    private Texture texture;
    private int scrollWidth,scrollHeight;
    private boolean isScroll = false;

    public UiStage(OrthographicCamera camera, final MainScreen mainScreen) {
        this.camera = camera;
        setDebugAll(true);
        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/font.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.characters = str;
        fontParameter.size = 20;
        bfLevel = generator.generateFont(fontParameter);
        bfStep = generator.generateFont(fontParameter);

        bfAp = generator.generateFont(fontParameter);
        bfDp = generator.generateFont(fontParameter);
        bfHp = generator.generateFont(fontParameter);

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
        addScrollPane();
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
        PlayerInfo playerInfo = GameDataHelper.getInstance().getPlayerInfo();
        bfHp.draw(getBatch(),"血量:"+playerInfo.getHealthPoint(),Gdx.graphics.getWidth()-bfStep.getRegion().getRegionWidth()-10,Gdx.graphics.getHeight()-20-bfLevel.getCapHeight());
        bfAp.draw(getBatch(),"攻击:"+playerInfo.getAttackPoint(),Gdx.graphics.getWidth()-bfStep.getRegion().getRegionWidth()-10,Gdx.graphics.getHeight()-40-bfLevel.getCapHeight());
        bfDp.draw(getBatch(),"防御:"+playerInfo.getDefensePoint(),Gdx.graphics.getWidth()-bfStep.getRegion().getRegionWidth()-10,Gdx.graphics.getHeight()-60-bfLevel.getCapHeight());
        getBatch().end();

    }

    @Override
    public void dispose() {
        super.dispose();
        generator.dispose();
        texture.dispose();
    }

    private Table container;
    private void addScrollPane(){
        scrollWidth = Gdx.graphics.getWidth();
        scrollHeight = Gdx.graphics.getHeight()/4;
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
        container.setBackground(new TextureRegionDrawable(textureRegion));
        container.setPosition(0,0);
        container.setWidth(Gdx.graphics.getWidth());
        container.setHeight(Gdx.graphics.getHeight()/4);
        addActor(container);
        Table table = new Table();
        final ScrollPane scrollPane = new ScrollPane(table);
        table.pad(10).defaults().expandX().space(4);
        for (int i = 0; i < 100; i++) {
            table.row();
            table.add(new Label(i+"12345678",new Label.LabelStyle(bfLevel, Color.WHITE))).expandX().fillX();
        }
        container.add(scrollPane).expand().fill().colspan(4);
        container.row().space(10).padBottom(10);

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
}
