package com.mw.components.map.world;

/**
 * Created by yuli.he on 2017/9/21.
 */

public enum WorldAreaType {

    CASTLE_FOOL("The Fool",0),//愚者
    CASTLE_MAGICIAN("The Magician",1),//魔术师
    CASTLE_PRIESTESS("The High Priestess or the Popess",2),//女祭师
    CASTLE_EMPRESS("The Empress",3),//王后
    CASTLE_EMPEROR("The Emperor",4),//国王
    CASTLE_HIEROPHANT("The Hierophant or the Pope",5),//教皇
    CASTLE_LOVERS("The Lovers",6),//恋人
    CASTLE_CHARIOT("The Chariot",7),//战车
    CASTLE_STRENGTH("Strength",8),//力量
    CASTLE_HERMIT("The Hermit",9),//隐士
    CASTLE_WHEEL("The Wheel of Fortune",10),//命运之轮
    CASTLE_JUSTICE("Justice",11),//正义
    CASTLE_HANGED("The Hanged Man",12),//倒吊男
    CASTLE_DEATH("Death",13),//死亡
    CASTLE_TEMPERANCE("Temperance",14),//节欲
    CASTLE_DEVIL("The Devil ",15),//恶魔
    CASTLE_TOWER("The Tower",16),//塔
    CASTLE_STAR("The Star",17),//星星
    CASTLE_MOON("The Moon",18),//月亮
    CASTLE_SUN("The Sun",19),//太阳
    CASTLE_JUDGMENT("Judgment",20),//审判
    CASTLE_WORLD("The World",21);//世界


    private int value;
    private String name;

    WorldAreaType(String name,int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
