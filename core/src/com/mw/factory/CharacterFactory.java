package com.mw.factory;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.mw.actor.CharacterActor;
import com.mw.actor.PlayerActor;
import com.mw.logic.characters.Ghost;
import com.mw.logic.characters.Player;
import com.mw.logic.info.GhostInfo;
import com.mw.logic.info.PlayerInfo;
import com.mw.map.DungeonMap;
import com.mw.stage.MapStage;


/**
 * 角色生成器
 */
public class CharacterFactory {
    private MapStage mapStage;
    private DungeonMap dungeonMap;
    private TextureAtlas textureAtlas;
    private OrthographicCamera camera;
    public CharacterFactory(MapStage mapStage) {
        this.mapStage = mapStage;
        dungeonMap = mapStage.getDungeonMap();
        textureAtlas = mapStage.getTextureAtlas();
        camera = mapStage.getCamera();

    }

    public Player getPlayer(){
        Player player = new Player();
        player.setInfo(getPlayerInfo());
        PlayerActor manActor = new PlayerActor(textureAtlas,"man",camera,dungeonMap);
        manActor.setPosition(-100,-100);
        manActor.setZIndex(2);
        player.setActor(manActor);
        mapStage.addActor(manActor);
        player.getActor().setFocus(true);
        return player;
    }
    public Ghost getGhost(){
        Ghost ghost = new Ghost();
        ghost.setInfo(getGhostInfo());
        CharacterActor ghostActor = new CharacterActor(textureAtlas,"ghost",camera,dungeonMap);
        ghostActor.setTilePosIndex(new GridPoint2(DungeonMap.TILE_SIZE/2,DungeonMap.TILE_SIZE/2));
        ghostActor.setZIndex(1);
        ghost.setActor(ghostActor);
        mapStage.addActor(ghostActor);
        return ghost;
    }
    /**玩家角色信息**/
    private PlayerInfo getPlayerInfo(){
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setAttackPoint(1);
        playerInfo.setDefensePoint(0);
        playerInfo.setHealthPoint(10);
        playerInfo.setSpeed(1);
        playerInfo.setAttackRange(1);
        return  playerInfo;
    }
    /**ghost**/
    private GhostInfo getGhostInfo(){
        GhostInfo ghostInfo = new GhostInfo();
        ghostInfo.setAttackPoint(0);
        ghostInfo.setDefensePoint(0);
        ghostInfo.setHealthPoint(1);
        ghostInfo.setSpeed(1);
        ghostInfo.setAttackRange(1);
        return  ghostInfo;
    }

}
