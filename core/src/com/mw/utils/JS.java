package com.mw.utils;

import com.badlogic.gdx.Gdx;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by BanditCat on 2016/9/23.
 */

public class JS {
    private ScriptEngineManager sem = new ScriptEngineManager();
    private ScriptEngine engine;

    public JS() {
        engine = sem.getEngineByExtension("js");
    }
    public void getJs(){
        try{
            engine.eval("if(6>5){flag=true;}else{flag=false;}");
        }catch (ScriptException e){
            e.printStackTrace();
        }
        System.out.println((engine.get("flag")));
    }
    public void doJs()throws ScriptException,NoSuchMethodException{
        // 创建脚本
        String script = "function max(first,second) "
                + "{ return (first > second) ?true:false;}";
        // 执行脚本
        engine.eval(script);
        Invocable inv = (Invocable) engine;
        // 执行方法并传递参数
        Object obj = inv.invokeFunction("max", "0", "1");
        // 打印结果
        System.out.println((Boolean) obj == false);
    }
}
