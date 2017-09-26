package com.mw.logic.commands;

import com.mw.components.map.MapGenerator;
import com.mw.logic.characters.base.Character;
import com.mw.logic.commands.base.BaseCommand;
import com.mw.logic.commands.base.Command;

/**
 * Created by yuli.he on 2017/9/26.
 */

public class WalkCommand extends BaseCommand{
    private int x;
    private int y;
    private Character character;

    public WalkCommand(int x, int y, Character character) {
        this.x = x;
        this.y = y;
        this.character = character;
    }

    @Override
    public boolean execute() {
        return playerWalk(x,y);
    }
    /**
     * 点击地图位置，通知L，L让player寻路并规划好要移动的第一格，获取map关于这一格的信息（判断是移动，停止，和该格子互动，或者和该格子的player互动）
     * 执行这些操作，结束player这一回合，执行其他npc的ai
     */
    public boolean playerWalk(int x,int y) {
        //获取玩家为中心的16x16的数组
//        int length = 16;
//        int x0 = player.getActor().getTilePosIndex().x-length/2;
//        int y0 = player.getActor().getTilePosIndex().y-length/2;
//        //传入左上角坐标得到数组
//        int[][] stars = MapGenerator.getInstance().getAStarArray(x0,y0,length);
//        player.upDateAStarArray(x0,y0,stars);
        if(MapGenerator.map().isBlock(x,y)||MapGenerator.map().isForbidden(x,y)){
            return false;
        }
        character.findWay(x,y);
        //判断是否有第一格，没有不执行移动
        if(character.getPath().size() == 0){
            return false;
        }
        character.walk();

        return true;
    }
}
