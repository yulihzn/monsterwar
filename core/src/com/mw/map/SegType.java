package com.mw.map;

/**
 * Created by BanditCat on 2016/12/22.
 * 大地图片段类型，目前只有基础类型，具体外观变化以后加入
 */

public class SegType {
    /**
     * 通用
     **/
    public static final int TRANS = 0;
    public static final int SHADOW = 1;
    /**
     * 城堡
     **/
    public static final int CASTLE_DIRT = 1000;//泥土
    public static final int CASTLE_WATER = 1001;//水
    public static final int CASTLE_GRASS = 1002;//草地
    public static final int CASTLE_STONE = 1003;//石头
    public static final int CASTLE_TREE = 1004;//树
    public static final int CASTLE_TOMB = 1005;//墓地
    public static final int CASTLE_BUSH = 1006;//灌木丛
    public static final int CASTLE_WALL = 1007;//城墙
    public static final int CASTLE_BATTLEMENT = 1008;//城垛
    public static final int CASTLE_GATE = 1009;//城门
    public static final int CASTLE_BRIDGE = 1010;//城桥
    public static final int CASTLE_ROAD = 1011;//小路
    public static final int CASTLE_STREET = 1012;//干道
    public static final int CASTLE_AVENUE = 1013;//主干道
    public static final int CASTLE_HOUSE = 1014;//民房
    public static final int CASTLE_FORTRESSES = 1015;//要塞
    public static final int CASTLE_GUILD_WARRIOR = 1016;//战士公会
    public static final int CASTLE_GUILD_MAGIC = 1017;//法师公会
    public static final int CASTLE_GUILD_RANGER = 1018;//游侠公会
    public static final int CASTLE_GUILD_MINISTER = 1019;//占星家公会
    public static final int CASTLE_STORE = 1020;//杂货店
    public static final int CASTLE_SMITHY = 1021;//铁匠铺
    public static final int CASTLE_INN = 1022;//酒馆
    public static final int CASTLE_MARKET = 1023;//集市
    public static final int CASTLE_GUARD_WATER = 1024;//护城河
    /**
     * 村庄
     **/
    public static final int VILLAGE_DIRT = 2000;//泥土
    public static final int VILLAGE_WATER = 2001;//水
    public static final int VILLAGE_GRASS = 2002;//草地
    public static final int VILLAGE_STONE = 2003;//石头
    public static final int VILLAGE_TREE = 2004;//树
    public static final int VILLAGE_TOMB = 2005;//墓地
    public static final int VILLAGE_BUSH = 2006;//灌木丛
    public static final int VILLAGE_WALL = 2008;//城墙
    public static final int VILLAGE_FARM = 2007;//农场
    public static final int VILLAGE_BATTLEMENT = 2009;//城垛
    public static final int VILLAGE_GATE = 2010;//城门
    public static final int VILLAGE_BRIDGE = 2011;//桥
    public static final int VILLAGE_ROAD = 2012;//小路
    public static final int VILLAGE_STREET = 2013;//干道
    public static final int VILLAGE_AVENUE = 2014;//主干道
    public static final int VILLAGE_HOUSE = 2015;//民房
    public static final int VILLAGE_FORTRESSES = 2016;//要塞
    public static final int VILLAGE_GUILD_WARRIOR = 2017;//战士公会
    public static final int VILLAGE_GUILD_MAGIC = 2018;//法师公会
    public static final int VILLAGE_GUILD_RANGER = 2019;//游侠公会
    public static final int VILLAGE_GUILD_MINISTER = 2020;//占星家公会
    public static final int VILLAGE_STORE = 2021;//杂货店
    public static final int VILLAGE_SMITHY = 2022;//铁匠铺
    public static final int VILLAGE_INN = 2023;//酒馆
    public static final int VILLAGE_MARKET = 2024;//集市
    public static final int VILLAGE_FIELD = 2025;//田
    /**
     * 野外
     **/
    public static final int WILD_DIRT = 3000;//泥土
    public static final int WILD_WATER = 3001;//水
    public static final int WILD_GRASS = 3002;//草地
    public static final int WILD_STONE = 3003;//石头
    public static final int WILD_TREE = 3004;//树
    public static final int WILD_MAGMA = 3005;//岩浆
    public static final int WILD_BUSH = 3006;//灌木丛
    public static final int WILD_CAVE = 3007;//洞穴
    public static final int WILD_CABIN = 3008;//小屋
    public static final int WILD_BRIDGE = 3009;//桥
    public static final int WILD_ROAD = 3010;//小路
    public static final int WILD_STREET = 3011;//干道
    /**
     * 地牢
     **/
    public static final int DUNGEON = 4000;
}
