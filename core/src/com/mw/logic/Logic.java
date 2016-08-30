package com.mw.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mw.logic.characters.base.Monster;
import com.mw.logic.characters.base.Player;

/**
 * Created by BanditCat on 2016/8/10.
 */
public class Logic {
    /**
     * 玩家进行攻击移动使用休息等操作算作一回合的开始
     * 每行动一次算作一回合，其余单位也行动一次
     * 比如移动，当点击目的地的时候，其余单位也确定各自的目的地或者其他行为，玩家移动一格其他单位也行动一回合，各种逻辑处理完毕，最后再执行动画
     * 由静止的动画转变成相应的动画
     *
     */

    private static Logic inStance = null;
    private Logic(){
        init();
    }

    private void init() {
    }

    public static Logic getInstance(){
        if(inStance == null){
            inStance = new Logic();
        }
        return inStance;
    }
    private Array<Monster> monsterArray = new Array<Monster>();

    public Array<Monster> getMonsterArray() {
        return monsterArray;
    }

    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
