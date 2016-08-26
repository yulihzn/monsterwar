package com.mw.logic.characters.base;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.mw.actor.CharacterActor;
import com.mw.logic.characters.info.CharacterInfo;
import com.mw.stage.MapStage;

/**
 * Created by BanditCat on 2016/7/25.
 */
public abstract class Character implements Telegraph {
    protected StateMachine<Character,CharacterState> stateMachine;
    protected CharacterActor characterActor;
    protected CharacterInfo characterInfo;

    public Character() {
        this.stateMachine = new DefaultStateMachine<Character, CharacterState>(this, CharacterState.IDLE, CharacterState.GLOBAL_STATE);
    }

    public CharacterInfo getInfo() {
        return characterInfo;
    }

    public void setInfo(CharacterInfo characterInfo) {
        this.characterInfo = characterInfo;
    }

    public CharacterActor getActor() {
        return characterActor;
    }

    public void setActor(CharacterActor characterActor) {
        this.characterActor = characterActor;
    }

    public StateMachine<Character, CharacterState> getStateMachine() {
        return stateMachine;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return stateMachine.handleMessage(msg);
    }
    public void update (float delta) {
        stateMachine.update();
    }

    public void Attack(Character character){
    }
    public void doClick(MapStage mapStage, int x, int y){

    }

}
