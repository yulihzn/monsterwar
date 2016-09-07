package com.mw.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mw.logic.Logic;

/**
 * Created by BanditCat on 2016/9/7.
 */
public class LogMessageTable extends Table {
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont bitmapFont;
    private Texture texture;
    private String str="test";
    public LogMessageTable() {
        init();
    }

    private void init() {
        int w = Gdx.graphics.getWidth()/2;
        int h = 200;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/font.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.characters = str;
        fontParameter.size = 16;
        bitmapFont = generator.generateFont(fontParameter);

        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        // Create a texture to contain the pixmap
        texture = new Texture(w, h, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        pixmap.setColor(new Color(0,0,0,0.8f));
        pixmap.fillRectangle(0,0,w,h);
        texture.draw(pixmap, 0, 0);
        TextureRegion textureRegion = new TextureRegion(texture,w,h);
        setBackground(new TextureRegionDrawable(textureRegion));
        setPosition(0,0);
        setWidth(w);
        setHeight(h);
        Table table = new Table();
        final ScrollPane scrollPane = new ScrollPane(table);
        table.pad(5).defaults().expandX().space(5);
        for (int i = 0; i < 10; i++) {
            table.row();
            Label label = new Label(i+"12345678",new Label.LabelStyle(bitmapFont, Color.WHITE));
            label.setText(label.getText()+"防御123456781234防御1234567812345防御1234567812345防御1234567812345防御1234567812345防御1234567812345防御12345678123455");
            label.setWidth(w);
            label.setWrap(true);
            table.add(label).expandX().fillX();
        }
        add(scrollPane).expand().fill().colspan(5);
        row().space(5).padBottom(5);
        addAction(Actions.fadeOut(0));
        setVisible(false);
        Logic.getInstance().setLogMessageListener(logMessageListener);

    }
    private Logic.LogMessageListener logMessageListener = new Logic.LogMessageListener() {

        @Override
        public void sendMessage(String msg) {
            setVisible(true);
            addAction(Actions.sequence(Actions.fadeIn(0.1f),Actions.delay(1,Actions.fadeOut(3f)),Actions.run(new Runnable() {
                @Override
                public void run() {
                    setVisible(false);
                }
            })));
        }
    };
    public void dispose(){
        texture.dispose();
        bitmapFont.dispose();
        generator.dispose();

    }
}
