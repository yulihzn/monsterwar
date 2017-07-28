package com.mw.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mw.avatar.Avatar;
import com.mw.game.MainGame;

/**
 * Created by BanditCat on 2016/9/20.
 */
public class PlayerAvatarTable extends Table {
    private Texture texture;
    private Avatar avatar;
    public PlayerAvatarTable() {
        avatar = new Avatar();
        int w = MainGame.worldWidth /4;
        int h = MainGame.worldHeight/4;
        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        texture = new Texture(w, h, Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        pixmap.setColor(new Color(255,255,255,0.2f));
        pixmap.fillRectangle(0,0,w,h);
        texture.draw(pixmap, 0, 0);
        pixmap.dispose();
        setBackground(new TextureRegionDrawable(new TextureRegion(texture,w,h)));
        setPosition(0,0);
        setWidth(w);
        setHeight(h);
        final Image image = new Image(avatar.getTexture());
        image.setScale(10);
        image.setPosition(0,0);
        bottom().left().add(image);

        image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SequenceAction sequenceAction = Actions.sequence();
                for (int i = 0; i < 15; i++) {
                    sequenceAction.addAction(Actions.delay(0.1f,Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            avatar.upDateAvatar();
                        }
                    })));
                }
                image.addAction(sequenceAction);
            }
        });
    }
    public void dispose(){
        texture.dispose();
        avatar.dispose();
    }
}
