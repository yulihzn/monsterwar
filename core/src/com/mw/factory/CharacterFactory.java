package com.mw.factory;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.mw.actor.CharacterActor;
import com.mw.actor.PlayerActor;
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
        player.setInfo(new PlayerInfo(10,1,1,0,1));
        PlayerActor manActor = new PlayerActor(textureAtlas,"man",camera,dungeonMap);
        manActor.setPosition(-100,-100);
        player.setActor(manActor);
        mapStage.addActor(manActor);
        manActor.setZIndex(2);
        player.getActor().setFocus(true);
        return player;
    }
    public Ghost getGhost(){
        Ghost ghost = new Ghost();
        ghost.setInfo(new GhostInfo(10,1,1,0,1));
        CharacterActor ghostActor = new CharacterActor(textureAtlas,"ghost",camera,dungeonMap);
        ghostActor.setTilePosIndex(new GridPoint2(DungeonMap.TILE_SIZE/2,DungeonMap.TILE_SIZE/2));
        ghost.setActor(ghostActor);
        mapStage.addActor(ghostActor);
        ghostActor.setZIndex(2);
        return ghost;
    }

}
