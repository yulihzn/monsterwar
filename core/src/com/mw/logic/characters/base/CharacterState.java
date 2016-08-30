package com.mw.logic.characters.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

/**
 * Created by BanditCat on 2016/7/25.
 */
public enum CharacterState implements State<Character> {
    IDLE(){
        @Override
        public void enter(Character entity) {
            Log(entity.getInfo().getName()+":stay...");
        }

        @Override
        public void update(Character entity) {
            if(entity.getActor().isMoving()){
                entity.getStateMachine().changeState(MOVE);
            }else if(entity.getActor().isAttack()){
                entity.getStateMachine().changeState(ATTACK);
            }
        }
    },
    MOVE(){
        @Override
        public void enter(Character entity) {
            Log(entity.getInfo().getName()+":moving");
        }
        @Override
        public void update(Character entity) {
            if(!entity.getActor().isMoving()){
                entity.getStateMachine().changeState(IDLE);
            }
        }
    },
    ATTACK(){
        @Override
        public void enter(Character entity) {
            Log(entity.getInfo().getName()+":attacking!");
        }
        @Override
        public void update(Character entity) {
            if(!entity.getActor().isAttack()){
                entity.getStateMachine().changeState(IDLE);
            }
        }
    },
    GLOBAL_STATE(){
        @Override
        public void update(Character entity) {
            super.update(entity);
        }
    };



    @Override
    public void enter(Character entity) {

    }

    @Override
    public void update(Character entity) {

    }

    @Override
    public void exit(Character entity) {

    }

    public void Log(String str){
        Gdx.app.log(name(),str);
    }

    @Override
    public boolean onMessage(Character entity, Telegram telegram) {
        return false;
    }

}
