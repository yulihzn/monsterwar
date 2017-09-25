package com.mw.logic;

import com.badlogic.gdx.utils.Array;

/**
 * Created by yuli.he on 2017/9/25.
 */

public class Round {
    private Array<Command> commands;
    public Round() {
        commands = new Array<Command>();
    }

    public Round(Array<Command> commands) {
        this.commands = commands;
    }

    public void start(){
        for(Command command : commands){
            command.execute();
        }

    }
    public void end(){

    }
    public interface RoundListener{
        void start();
        void end();
    }
}
