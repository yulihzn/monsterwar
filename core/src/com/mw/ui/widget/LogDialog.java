package com.mw.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.mw.logic.model.LogModel;

/**
 * Created by BanditCat on 2016/8/8.
 */
public class LogDialog extends Dialog {
    private Array<LogModel> msgList;
    public LogDialog(String title, Skin skin) {
        super(title, skin);
    }

    public LogDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    public LogDialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);
    }
    private void init(){
        msgList = new Array<LogModel>();
    }
    private void addLog(LogModel logModel){
        logModel.setId(msgList.size);
        msgList.add(logModel);
    }
}
