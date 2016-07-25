package com.mw.factory;


import com.mw.logic.Player;
import com.mw.logic.info.PlayerInfo;


/**
 * 角色生成器
 */
public class CharacterFactory {
    private static CharacterFactory instance = new CharacterFactory();
    public static CharacterFactory getInstance(){
        return instance;
    }
    private CharacterFactory() {
    }

    public Player getPlayer(){
        Player player = new Player();
        player.setInfo(getPlayerInfo());
        return player;
    }
    /**玩家角色信息**/
    private PlayerInfo getPlayerInfo(){
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setName("man");
        playerInfo.setAttackPoint(1);
        playerInfo.setDefensePoint(0);
        playerInfo.setHealthPoint(10);
        playerInfo.setSpeed(1);
        playerInfo.setAttackRange(1);
        return  playerInfo;
    }


}
