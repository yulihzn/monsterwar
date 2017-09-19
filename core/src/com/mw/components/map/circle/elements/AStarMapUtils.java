package com.mw.components.map.circle.elements;


import com.mw.components.map.circle.section.Section;

import java.util.List;

/**
 * Created by yuli.he on 2017/9/14.
 */

public class AStarMapUtils {
    private CircleDungeon dungeon;
    private com.mw.components.map.AStarMap aStarMap;
    private String[][] pathMap;

    public AStarMapUtils(CircleDungeon dungeon) {
        dungeon.createDungeon();
        this.dungeon = dungeon;
        upDateMap();
    }
    public void upDateMap(){
        aStarMap = new com.mw.components.map.AStarMap(dungeon.width,dungeon.height);
        int[][] aStarData = new int[dungeon.height][dungeon.width];
        pathMap = new String[dungeon.height][dungeon.width];
        for (int i = 0; i < dungeon.height; i++) {
            for (int j = 0; j < dungeon.width; j++) {
                int v = dungeon.getMaps()[j][i].getValue();
                aStarData[i][j] = 1;
                if(v == Tiles.tile().corridorfloor.getValue()
                        ||v == Tiles.tile().roomfloor.getValue()
                        ||v == Tiles.tile().opendoor.getValue()){
                    aStarData[i][j] = 0;
                }
                pathMap[j][i] = Tiles.tile().empty.getName();
            }
        }
        aStarMap.loadData(aStarData,1,0);
    }
    public String mapStr(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < dungeon.height; j++) {
            for (int i = 0; i < dungeon.width; i++) {
                stringBuilder.append(Tiles.ToSBC(""+aStarMap.getAStarData()[j][i]));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
    public String pathStr(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < dungeon.height; j++) {
            for (int i = 0; i < dungeon.width; i++) {
                stringBuilder.append(pathMap[i][j]);
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public void find(int indexS,int indexT){
        int sx = 0;
        int sy = 0;
        int tx = 0;
        int ty = 0;
        for (Section sec : dungeon.getSections()) {
            if(sec.getIndex() == indexS){
                sx = sec.left+sec.width()/2;
                sy = sec.top+sec.height()/2;

            }
            if(sec.getIndex() == indexT){
                tx = sec.left+sec.width()/2;
                ty = sec.top+sec.height()/2;
            }
        }
        if(tx==0||ty==0){
            return;
        }
        aStarMap.setSource(new com.mw.components.map.AStarNode(sx,sy));
        aStarMap.setTarget(new com.mw.components.map.AStarNode(tx,ty));
        List<com.mw.components.map.AStarNode> list = aStarMap.find();
        for (int j = 0; j < dungeon.height; j++) {
            for (int i = 0; i < dungeon.width; i++) {
                pathMap[i][j] = Tiles.tile().empty.getName();
            }
        }

        for(com.mw.components.map.AStarNode node : list){
            pathMap[node.getX()][node.getY()] = Tiles.tile().stone.getName();
        }
        pathMap[sx][sy] = Tiles.tile().downstairs.getName();
        pathMap[tx][ty] = Tiles.tile().upstairs.getName();
    }
}
