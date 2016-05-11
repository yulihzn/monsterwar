package com.mw.utils;

import com.badlogic.gdx.Input;

/**
 * Created by BanditCat on 2016/5/11.
 */
public class KeyBoardController {
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    private static KeyBoardController instance = new KeyBoardController();
    private KeyBoardController (){}
    public static KeyBoardController getInstance() {
        return instance;
    }
    public int getKeyType(int keyCode){
        int type = -1;
        switch (keyCode){
            case Input.Keys.W:
            case Input.Keys.UP:
                type = UP;
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                type = DOWN;
                break;
            case Input.Keys.A:
            case Input.Keys.LEFT:
                type = LEFT;
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                type = RIGHT;
                break;
        }
        return type;
    }
}
