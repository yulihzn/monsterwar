package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.mw.logic.characters.base.Character;
import com.mw.map.DungeonMap;

/**
 * Created by BanditCat on 2016/6/7.
 */
public class CharacterActor extends GameMapTile {

    protected long roundTime = TimeUtils.nanoTime();//回合时间用来和时间间隔对比
    protected boolean isFocus = false;//是否镜头跟随
    protected OrthographicCamera camera;


    protected Character character;
    public CharacterActor(Character character,TextureAtlas textureAtlas, String regionName, OrthographicCamera cam, DungeonMap dungeonMap) {
        super(textureAtlas, regionName, cam);
        this.character = character;
        this.camera = cam;
    }





    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        //镜头延迟跟随（待修改）
        if(isFocus){
            isFocus = false;
            camera.position.set(getX(),getY(), 0);
            final float disX = getX() - camera.position.x;
            final float disY = getY() - camera.position.y;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    camera.translate(disX/10,disY/10);
                }
            },2f,0.01f,10).run();
        }
    }

}
