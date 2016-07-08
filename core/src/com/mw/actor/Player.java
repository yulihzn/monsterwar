package com.mw.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mw.map.DungeonMap;
import com.mw.utils.Dungeon;

/**
 * Created by BanditCat on 2015/12/24.
 */
public class Player extends Character{
    public static final int ACTION_DOWN = 1;
    public static final int ACTION_UP = 0;
    private PlayerActionListener playerActionListener;

    public void setPlayerActionListener(PlayerActionListener playerActionListener) {
        this.playerActionListener = playerActionListener;
    }

    public Player(TextureAtlas textureAtlas, String regionName, OrthographicCamera cam, DungeonMap dungeonMap) {
        super(textureAtlas, regionName, cam,dungeonMap);
    }

    @Override
    protected void moveLogic(int x, int y) {
        super.moveLogic(x, y);
        //上下
        if(path.size() == 1){
            if(getTilePosIndex().x == x&&getTilePosIndex().y==y){
                if(playerActionListener != null){
                    playerActionListener.move(ACTION_UP,x,y);
                }
                if(dungeonMap.getDungeonArray()[x][y] == Dungeon.tileUpStairs){
                }else if(dungeonMap.getDungeonArray()[x][y] == Dungeon.tileDownStairs){
                    if(playerActionListener != null){
                        playerActionListener.move(ACTION_DOWN,x,y);
                    }
                }
            }
        }
    }

    public interface PlayerActionListener{
        void move(int action,int x,int y);
    }
}
