package com.mw.components.map.world;

/**
 * Created by yuli.he on 2017/9/21.
 */

public enum WorldAreaType {

    CASTLE_00_FOOL("Ａ","The Fool",0),//愚者
    CASTLE_01_MAGICIAN("Ｂ","The Magician",1),//魔术师
    CASTLE_02_PRIESTESS("Ｃ","The High Priestess or the Popess",2),//女祭师
    CASTLE_03_EMPRESS("Ｄ","The Empress",3),//王后
    CASTLE_04_EMPEROR("Ｅ","The Emperor",4),//国王
    CASTLE_05_HIEROPHANT("Ｆ","The Hierophant or the Pope",5),//教皇
    CASTLE_06_LOVERS("Ｇ","The Lovers",6),//恋人
    CASTLE_07_CHARIOT("Ｈ","The Chariot",7),//战车
    CASTLE_08_STRENGTH("Ｉ","Strength",8),//力量
    CASTLE_09_HERMIT("Ｊ","The Hermit",9),//隐士
    CASTLE_10_WHEEL("Ｋ","The Wheel of Fortune",10),//命运之轮
    CASTLE_11_JUSTICE("Ｌ","Justice",11),//正义
    CASTLE_12_HANGED("Ｍ","The Hanged Man",12),//倒吊男
    CASTLE_13_DEATH("Ｎ","Death",13),//死亡
    CASTLE_14_TEMPERANCE("Ｏ","Temperance",14),//节欲
    CASTLE_15_DEVIL("Ｐ","The Devil ",15),//恶魔
    CASTLE_16_TOWER("Ｑ","The Tower",16),//塔
    CASTLE_17_STAR("Ｒ","The Star",17),//星星
    CASTLE_18_MOON("Ｓ","The Moon",18),//月亮
    CASTLE_19_SUN("Ｔ","The Sun",19),//太阳
    CASTLE_20_JUDGMENT("Ｕ","Judgment",20),//审判
    CASTLE_21_WORLD("Ｖ","The World",21),//世界
    WILD_WATER("～","Water",22),//水
    WILD_DIRT("ｘ","Dirt",23),//泥土区
    WILD_GRASS("ｒ","Grass",24),//草地区
    WILD_FOREST("ｗ","Forest",25),//森林区
    WILD_ROCK("ｏ","Rock",26),//岩石区
    DUNGEON("ｏ","Dungeon",27),//地牢
    TOWER("ｏ","Tower",28);//塔牢

    private int value;
    private String name;
    private String str;

    WorldAreaType(String str,String name,int value) {
        this.name = name;
        this.value = value;
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

    public int getValue() {
        return value;
    }

}
