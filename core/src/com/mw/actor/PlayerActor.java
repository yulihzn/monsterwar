package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mw.logic.characters.info.PlayerInfo;
import com.mw.map.DungeonMap;
import com.mw.utils.Dungeon;
import com.mw.utils.GameDataHelper;

/**
 * Created by BanditCat on 2015/12/24.
 */
public class PlayerActor extends CharacterActor {
    public static final int ACTION_DOWN = 1;
    public static final int ACTION_UP = 0;
    private PlayerActionListener playerActionListener;

    public void setPlayerActionListener(PlayerActionListener playerActionListener) {
        this.playerActionListener = playerActionListener;
    }

    public PlayerActor(TextureAtlas textureAtlas, String regionName, OrthographicCamera cam, DungeonMap dungeonMap) {
        super(textureAtlas, regionName, cam,dungeonMap);
    }

    @Override
    protected void moveLogic(int curPos) {
        super.moveLogic(curPos);
        GameDataHelper.getInstance().setCurrentStep(PlayerInfo.NAME,GameDataHelper.getInstance().getCurrentStep(PlayerInfo.NAME)+1);
        //上下
        final int x = path.get(curPos).getX();
        final int y = path.get(curPos).getY();
        if(path.size() == 1){
            if(getTilePosIndex().x == x&&getTilePosIndex().y==y){
                if(playerActionListener == null){
                    return;
                }
                if(dungeonMap.getDungeonArray()[x][y] == Dungeon.tileUpStairs){
                    playerActionListener.move(ACTION_UP,x,y);
                }else if(dungeonMap.getDungeonArray()[x][y] == Dungeon.tileDownStairs){
                    playerActionListener.move(ACTION_DOWN,x,y);
                }
            }
        }
    }

    public interface PlayerActionListener{
        void move(int action,int x,int y);
    }
}