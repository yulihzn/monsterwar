package com.mw.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mw.logic.characters.base.Monster;
import com.mw.logic.characters.base.Player;
import com.mw.logic.characters.info.GhostInfo;
import com.mw.logic.characters.npc.Ghost;
import com.mw.logic.item.base.Food;
import com.mw.logic.item.base.Item;
import com.mw.logic.item.info.FoodInfo;

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

    private Array<Item>itemArray = new Array<Item>();

    public Array<Item> getItemArray() {
        return itemArray;
    }

    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void beginRound(int x,int y){
        //点击地图位置
        //玩家确定移动位置生成路径path,path是从0开始,当前A*的path是包含当前位置的，只有点击本身才应该保留当前位置
        //其它npc确定移动位置生成路径path
        //玩家按移动速度移动到路径的相对应的格数，不足移动速度的取path的最大值，speed=1，pathIndex=0;speed=2,pathIndex=1;
        //玩家这一轮移动如果路径上有触发事件移动终止，保留之前点击的位置
        //触发事件有：npc进入视野，踩到陷阱，其余各种状态开始或者结束例如饥饿，中毒
        //如果移动的位置有其他障碍物，如npc或者大石头墙壁等则停止移动，
        //其余生物按添加顺序执行操作
        //在逻辑上面移动结束，执行动画
        /**
         * 每走一格算一个回合，当玩家做出操作，例如移动时，点击地板，则通知开始一个回合，玩家走一格
         * ，其余生物各自走一格，回合结束，继续监听有没有下一个回合，如果玩家没有到指定的地点，
         * 也没有遇到让目的中止情况，继续走下一个回合。。然后循环下去
         */
        player.findWay(x,y);
        player.walk();
        checkOthers();
        for (int i = 0; i < Logic.getInstance().getMonsterArray().size; i++) {
            Monster monster = Logic.getInstance().getMonsterArray().get(i);
            if(monster.getInfo().getName().equals(GhostInfo.NAME)){
                Ghost ghost = (Ghost) monster;
                ghost.findWay(player.getActor().getTilePosIndex().x,player.getActor().getTilePosIndex().y);
                ghost.walk();
            }
        }
        checkPlayer();
    }

    private void checkPlayer() {
        if(player.getInfo().getHealthPoint()<=0){
            if(gameEventListener != null){
                gameEventListener.GameOver();
            }
        }
    }

    private void checkOthers() {
        for (int i = 0; i < Logic.getInstance().getMonsterArray().size; i++) {
            Monster monster = Logic.getInstance().getMonsterArray().get(i);
            if(monster.getInfo().getName().equals(GhostInfo.NAME)){
                Ghost ghost = (Ghost) monster;
                if(ghost.getInfo().getHealthPoint()<=0){
                    ghost.getActor().remove();
                    monsterArray.removeValue(ghost,false);
                }
            }
        }
        for (int i = 0; i < Logic.getInstance().getItemArray().size; i++) {
            Item item = Logic.getInstance().getItemArray().get(i);
            if(item.getInfo().getName().equals(FoodInfo.NAME)){
                Food food = (Food) item;
                if(food.getInfo().getHealthPoint()<=0){
                    food.getActor().remove();
                    itemArray.removeValue(food,false);
                }
            }
        }
    }

    private void endRound(){

    }
    private GameEventListener gameEventListener = null;

    public void setGameEventListener(GameEventListener gameEventListener) {
        this.gameEventListener = gameEventListener;
    }

    public interface GameEventListener{
        void GameOver();
    }
}
