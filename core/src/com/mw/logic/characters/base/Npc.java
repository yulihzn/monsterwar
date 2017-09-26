package com.mw.logic.characters.base;

/**
 * Created by yuli.he on 2017/9/26.
 */

public interface Npc {
    void walk();
    void attack();
    void useItem();
    void pickUp();
    void discard();
    void speak();
    void death();
    void buffer();
}
