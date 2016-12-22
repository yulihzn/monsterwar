package com.mw.map;

/**
 * Created by BanditCat on 2016/12/22.
 * 大地图片段类型，目前只有基础类型，具体外观变化以后加入
 */

public enum SegType {
    /**
     * 通用
     **/
    TRANS(0,""),
    SHADOW(1,""),
    /**
     * 城堡
     **/
    CASTLE_DIRT(1000,""),//泥土
    CASTLE_WATER(1001,""),//水
    CASTLE_GRASS(1002,""),//草地
    CASTLE_STONE(1003,""),//石头
    CASTLE_TREE(1004,""),//树
    CASTLE_TOMB(1005,""),//墓地
    CASTLE_BUSH(1006,""),//灌木丛
    CASTLE_WALL(1007,""),//城墙
    CASTLE_BATTLEMENT(1008,""),//城垛
    CASTLE_GATE(1009,""),//城门
    CASTLE_BRIDGE(1010,""),//城桥
    CASTLE_ROAD(1011,""),//小路
    CASTLE_STREET(1012,""),//干道
    CASTLE_AVENUE(1013,""),//主干道
    CASTLE_HOUSE(1014,""),//民房
    CASTLE_FORTRESSES(1015,""),//要塞
    CASTLE_GUILD_WARRIOR(1016,""),//战士公会
    CASTLE_GUILD_MAGIC(1017,""),//法师公会
    CASTLE_GUILD_RANGER(1018,""),//游侠公会
    CASTLE_GUILD_MINISTER(1019,""),//占星家公会
    CASTLE_STORE(1020,""),//杂货店
    CASTLE_SMITHY(1021,""),//铁匠铺
    CASTLE_INN(1022,""),//酒馆
    CASTLE_MARKET(1023,""),//集市
    CASTLE_GUARD_WATER(1024,""),//护城河
    /**
     * 村庄
     **/
    VILLAGE_DIRT(2000,""),//泥土
    VILLAGE_WATER(2001,""),//水
    VILLAGE_GRASS(2002,""),//草地
    VILLAGE_STONE(2003,""),//石头
    VILLAGE_TREE(2004,""),//树
    VILLAGE_TOMB(2005,""),//墓地
    VILLAGE_BUSH(2006,""),//灌木丛
    VILLAGE_WALL(2008,""),//城墙
    VILLAGE_FARM(2007,""),//农场
    VILLAGE_BATTLEMENT(2009,""),//城垛
    VILLAGE_GATE(2010,""),//城门
    VILLAGE_BRIDGE(2011,""),//桥
    VILLAGE_ROAD(2012,""),//小路
    VILLAGE_STREET(2013,""),//干道
    VILLAGE_AVENUE(2014,""),//主干道
    VILLAGE_HOUSE(2015,""),//民房
    VILLAGE_FORTRESSES(2016,""),//要塞
    VILLAGE_GUILD_WARRIOR(2017,""),//战士公会
    VILLAGE_GUILD_MAGIC(2018,""),//法师公会
    VILLAGE_GUILD_RANGER(2019,""),//游侠公会
    VILLAGE_GUILD_MINISTER(2020,""),//占星家公会
    VILLAGE_STORE(2021,""),//杂货店
    VILLAGE_SMITHY(2022,""),//铁匠铺
    VILLAGE_INN(2023,""),//酒馆
    VILLAGE_MARKET(2024,""),//集市
    VILLAGE_FIELD(2025,""),//田
    /**
     * 野外
     **/
    WILD_DIRT(3000,""),//泥土
    WILD_WATER(3001,""),//水
    WILD_GRASS(3002,""),//草地
    WILD_STONE(3003,""),//石头
    WILD_TREE(3004,""),//树
    WILD_MAGMA(3005,""),//岩浆
    WILD_BUSH(3006,""),//灌木丛
    WILD_CAVE(3007,""),//洞穴
    WILD_CABIN(3008,""),//小屋
    WILD_BRIDGE(3009,""),//桥
    WILD_ROAD(3010,""),//小路
    WILD_STREET(3011,""),//干道
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
