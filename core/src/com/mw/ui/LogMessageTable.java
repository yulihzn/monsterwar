package com.mw.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mw.logic.Logic;
import com.mw.model.LogModel;

/**
 * Created by BanditCat on 2016/9/7.
 */
public class LogMessageTable extends Table {
    private FreeTypeFontGenerator generator;
    private LazyBitmapFont bitmapFont;
    private Texture texture;
    private ScrollPane scrollPane;
    private Array<LogModel> msgList = new Array<LogModel>();
    public final static int TYPE_NORMAL = 0;//普通类型
    public final static int TYPE_WARNING = 1;//警告类型
    public final static int TYPE_BAD = 2;//危险类型
    public final static int TYPE_GOOD = 3;//增益类型
    private Color[]colors = {Color.LIGHT_GRAY,Color.YELLOW,Color.RED,Color.GREEN};


    public LogMessageTable() {
        init();
    }

    private void init() {
        int w = Gdx.graphics.getWidth()/2;
        int h = 20;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/font.ttf"));
        bitmapFont = new LazyBitmapFont(generator,14);

        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
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
        scrollPane = new ScrollPane(table);
        table.pad(5).defaults().expandX().space(5);
        add(scrollPane).expand().fill().colspan(5);
        row().space(5).padBottom(5);
//        addAction(Actions.fadeOut(0));
//        setVisible(false);
        Logic.getInstance().setLogMessageListener(logMessageListener);

    }

    /**
     * 添加一条消息
     * @param msg
     */
    private void addMsg(String msg,int type){
        //列表添加消息
        msgList.insert(0,new LogModel(msg,type,msgList.size));
        //新建一个label
        Table table = ((Table)scrollPane.getWidget());
        Label label = new Label("",new Label.LabelStyle(bitmapFont, Color.WHITE));
        int w = Gdx.graphics.getWidth()/2;
        label.setWidth(w);
        label.setWrap(true);
        table.row();
        table.add(label).expandX().fillX();
        //重新赋值所有的label
        for (int i = 0; i < table.getCells().size; i++) {
            Cell cell = table.getCells().get(i);
            cell.getActor().setColor(colors[msgList.get(i).getType()]);
            ((Label)cell.getActor()).setText(msgList.get(i).getMsg());
        }

    }
    private Logic.LogMessageListener logMessageListener = new Logic.LogMessageListener() {

        @Override
        public void sendMessage(String msg,int type) {
            addMsg(msg,type);
//            setVisible(true);
//            addAction(Actions.sequence(Actions.fadeIn(0.1f),Actions.delay(1,Actions.fadeOut(3f)),Actions.run(new Runnable() {
//                @Override
//                public void run() {
//                    setVisible(false);
//                }
//            })));
        }
    };
    public void dispose(){
        texture.dispose();
        bitmapFont.dispose();
        generator.dispose();

    }
}
