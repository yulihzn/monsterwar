package com.mw.map;

/**
 * Created by BanditCat on 2016/12/22.
 * 大地图片段类型，目前只有基础类型，具体外观变化以后加入
 */

public enum SegType {
    /**
     * 通用
     **/
    TRANS(0,"　"),
    SHADOW(1,"　"),
    /**
     * 野外
     **/
    WILD_DIRT(2,"．"),//泥土
    WILD_WATER(5,"～"),//水
    WILD_GRASS(7,"ｒ"),//草地
    WILD_STONE(14,"ｏ"),//石头
    WILD_TREE(8,"ｆ"),//树
    WILD_MAGMA(6,"ｘ"),//岩浆
    WILD_BUSH(3006,"ｗ"),//灌木丛
    WILD_CAVE(3007,"Ｄ"),//洞穴
    WILD_CABIN(3008,"ｎ"),//小屋
    WILD_BRIDGE(3009,"＋"),//桥
    WILD_ROAD(3010,"＝"),//小路
    WILD_STREET(3011,"＃"),//干道
    /**
     * 城堡
     **/
    CASTLE_DIRT(1000,"．"),//泥土
    CASTLE_WATER(1001,"～"),//水
    CASTLE_GRASS(1002,"Ｒ"),//草地
    CASTLE_STONE(1003,"Ｏ"),//石头
    CASTLE_TREE(1004,"Ｆ"),//树
    CASTLE_TOMB(1005,"Ｃ"),//墓地
    CASTLE_BUSH(1006,"Ｗ"),//灌木丛
    CASTLE_WALL(1007,"Ａ"),//城墙
    CASTLE_BATTLEMENT(1008,"Ｂ"),//城垛
    CASTLE_GATE(1009,"Ｃ"),//城门
    CASTLE_BRIDGE(1010,"Ｄ"),//城桥
    CASTLE_ROAD(1011,"Ｅ"),//小路
    CASTLE_STREET(1012,"Ｇ"),//干道
    CASTLE_AVENUE(1013,"Ｈ"),//主干道
    CASTLE_HOUSE(1014,"Ｉ"),//民房
    CASTLE_FORTRESSES(1015,"Ｊ"),//要塞
    CASTLE_GUILD_WARRIOR(1016,"Ｋ"),//战士公会
    CASTLE_GUILD_MAGIC(1017,"Ｌ"),//法师公会
    CASTLE_GUILD_RANGER(1018,"Ｍ"),//游侠公会
    CASTLE_GUILD_MINISTER(1019,"Ｎ"),//占星家公会
    CASTLE_STORE(1020,"Ｐ"),//杂货店
    CASTLE_SMITHY(1021,"Ｑ"),//铁匠铺
    CASTLE_INN(1022,"Ｓ"),//酒馆
    CASTLE_MARKET(1023,"Ｔ"),//集市
    CASTLE_GUARD_WATER(1024,"Ｕ"),//护城河
    /**
     * 村庄
     **/
    VILLAGE_DIRT(2000,"｀"),//泥土
    VILLAGE_WATER(2001,"－"),//水
    VILLAGE_GRASS(2002,"ｒ"),//草地
    VILLAGE_STONE(2003,"ｏ"),//石头
    VILLAGE_TREE(2004,"ｙ"),//树
    VILLAGE_TOMB(2005,"ｘ"),//墓地
    VILLAGE_BUSH(2006,"ｗ"),//灌木丛
    VILLAGE_WALL(2008,"ｕ"),//城墙
    VILLAGE_FARM(2007,"ｖ"),//农场
    VILLAGE_BATTLEMENT(2009,"ｐ"),//城垛
    VILLAGE_GATE(2010,"ｑ"),//城门
    VILLAGE_BRIDGE(2011,"ｓ"),//桥
    VILLAGE_ROAD(2012,"ｔ"),//小路
    VILLAGE_STREET(2013,"ｊ"),//干道
    VILLAGE_AVENUE(2014,"ｋ"),//主干道
    VILLAGE_HOUSE(2015,"ｌ"),//民房
    VILLAGE_FORTRESSES(2016,"ｍ"),//要塞
    VILLAGE_GUILD_WARRIOR(2017,"Ｋ"),//战士公会
    VILLAGE_GUILD_MAGIC(2018,"Ｌ"),//法师公会
    VILLAGE_GUILD_RANGER(2019,"Ｍ"),//游侠公会
    VILLAGE_GUILD_MINISTER(2020,"Ｎ"),//占星家公会
    VILLAGE_STORE(2021,"Ｐ"),//杂货店
    VILLAGE_SMITHY(2022,"Ｑ"),//铁匠铺
    VILLAGE_INN(2023,"Ｓ"),//酒馆
    VILLAGE_MARKET(2024,"Ｔ"),//集市
    VILLAGE_FIELD(2025,"ａ"),//田

    /**
     * 地牢
     **/
    DUNGEON(4000,"");
    

    private int value;
    private String name;

    SegType(int value,String name) {
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
    public static SegType getType(int value){
        for (SegType segType:values()){
            if(value==segType.getValue()){
                return segType;
            }
        }
        return null;
    }
    
}
