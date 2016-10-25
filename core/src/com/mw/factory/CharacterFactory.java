package com.mw.factory;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.mw.actor.CharacterActor;
import com.mw.actor.GhostActor;
import com.mw.actor.PlayerActor;
import com.mw.logic.Logic;
import com.mw.logic.characters.npc.Ghost;
import com.mw.logic.characters.base.Player;
import com.mw.logic.characters.info.GhostInfo;
import com.mw.logic.characters.info.PlayerInfo;
import com.mw.map.DungeonMap;
import com.mw.stage.MapStage;


/**
 * 角色生成器
 */
public class CharacterFactory {
    private Stage stage;
    private DungeonMap dungeonMap;
    private OrthographicCamera camera;
    public CharacterFactory(Stage stage) {
        this.stage = stage;
        camera = ((MapStage)stage).getCamera();
    }

    public Player getPlayer(){
        Player player = new Player();
        player.setInfo(new PlayerInfo(10,1,1,0,1,5));
        PlayerActor manActor = new PlayerActor(player,PlayerInfo.REGION_NAME,camera,dungeonMap);
        manActor.setPosition(0,0);
        manActor.setScale(1f/32f);
        player.setActor(manActor);
        stage.addActor(manActor);
        manActor.setZIndex(300);
        Logic.getInstance().setPlayer(player);
        return player;
    }
    public Ghost getGhost(){
        Ghost ghost = new Ghost();
        ghost.setInfo(new GhostInfo(1,1,1,0,1,3));
        GhostActor ghostActor = new GhostActor(ghost,GhostInfo.REGION_NAME,camera,dungeonMap);
        ghostActor.setTilePosIndex(new GridPoint2(DungeonMap.TILE_SIZE_WIDTH/2,DungeonMap.TILE_SIZE_HEIGHT/2));
        ghost.setActor(ghostActor);
        stage.addActor(ghostActor);
        ghostActor.setZIndex(2);
        return ghost;
    }

}
