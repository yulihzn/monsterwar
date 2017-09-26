package com.mw.logic.commands.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mw.utils.L;

/**
 * Created by yuli.he on 2017/9/25.
 */

public class CommandQueue extends BaseCommand {
    private Array<Command> commands;
    public CommandQueue() {
        commands = new Array<Command>();
    }

    public CommandQueue(Array<Command> commands) {
        this.commands = commands;
    }

    public void addCommand(Command command){
        commands.add(command);
    }
    public void removeCommand(Command command){
        commands.removeValue(command,false);
    }
    public void removeCommand(int index){
        if(index < 0 || index >= commands.size){
            return;
        }
        commands.removeIndex(index);
    }
    public void clear(){
        commands.clear();
    }
    public int size(){
        return commands.size;
    }

    @Override
    public boolean execute() {
        for(Command command : commands){
            if(!command.execute()){
                L.i("Command",command.toString()+" executeFailed");
                return false;
            }

        }
        return true;
    }
}
