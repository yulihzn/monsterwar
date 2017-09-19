package com.mw.components.map.circle.elements;

/**
 * Created by yuli.he on 2017/9/11.
 */

public class Tiles {
    private static Tiles instance;

    public static Tiles tile() {
        if (instance == null) {
            instance = new Tiles();
        }
        return instance;
    }

    public TileType empty;
    public TileType roomfloor;
    public TileType roomwall;
    public TileType roomcorner;
    public TileType corridorwall;
    public TileType corridorfloor;
    public TileType closedoor;
    public TileType opendoor;
    public TileType upstairs;
    public TileType downstairs;
    public TileType water;
    public TileType stone;

    private Tiles() {
        empty = getObstacle("　", 0);
        roomfloor = getPassable("．", 1);
        roomwall = getObstacle("＃", 2);
        closedoor = getObstacle("Ｎ", 3);
        opendoor = getPassable("ｎ", 4);
        upstairs = getPassable("Ｕ", 5);
        downstairs = getPassable("Ｄ", 6);
        water = getObstacle("～", 7);
        corridorwall = getObstacle("＊", 8);
        corridorfloor = getPassable("｀", 9);
        roomcorner = getObstacle("＄", 10);
        stone = getPassable("ｏ", 11);
    }

    private TileType getObstacle(String name, int value) {
        return new TileType(name, value, true);
    }

    private TileType getPassable(String name, int value) {
        return new TileType(name, value, false);
    }
    public TileType getTileType(String name,int value){
        return new TileType(name,value,false);
    }

    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }
}
