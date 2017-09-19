package com.mw.components.map.circle.elements;


import com.mw.components.map.circle.section.Section;
import com.mw.components.map.circle.section.SectionUtils;
import com.mw.components.map.circle.section.Rect;
import com.mw.components.map.circle.section.SectionExit;
import com.mw.components.map.circle.utils.MathUtils;
import com.mw.components.map.circle.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yuli.he on 2017/9/11.
 * 环形地牢
 * 生成策略
 * 房间元素
 */

public class CircleDungeon {
    public int width = 64;
    public int height = 64;
    private TileType[][] maps;
    private List<Section> sections = new ArrayList<Section>();
    private Path mainPath;
    private Path mainPathWall;
    private List<Path> sectionPaths;
    private List<Path> sectionPathWalls;
    private int mainSectionsIndex = 0;

    public CircleDungeon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public CircleDungeon() {

    }

    /**
     * 生成地牢
     */
    public void createDungeon() {
        sections.clear();
        mainPath = null;
        maps = new TileType[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                maps[i][j] = Tiles.tile().empty;
            }
        }
        //添加圆路径
        addCirclePath(width / 2, height / 2, RandomUtils.nextInt(width / 4, width / 2 - 1));
        //添加房间
        for (int i = 0; i < 500; i++) {
            addSection(RandomUtils.nextInt(width), RandomUtils.nextInt(height));
        }
        //放置房间和圆圈然后生成通路
        putPathIntoMap(mainPathWall);
        putPathIntoMap(mainPath);
        //修改和圆路径相交的房间
        int index = 1;
        for (Point p : mainPath.getList()) {
            for (Section sec : sections) {
                if (sec.contains(p.x, p.y)) {
                    if (sec.getIndex() == 0) {
                        sec.setIndex(index++);
                        sec.updateArea(sec.left + sec.width() / 2, sec.top + sec.height() / 2, Tiles.tile().getTileType(Tiles.ToSBC("" + sec.getIndex()), 1));
                    }
                    int dir = sec.isSide(p.x,p.y);
                    if (dir!=-1) {
                        sec.setExit(p.x, p.y,dir);
                        changeSectionCorner(sec,p.x,p.y);
                    }
                }
            }
        }
        mainSectionsIndex = index-1;

        for (Section sec : sections) {
            putSectionIntoMap(sec);
        }
        for (Section sec : sections) {
            removeOtherDoor(sec);
        }

        addSectionPath();
        Iterator<Section> it = sections.iterator();
        while(it.hasNext()){
            Section sec = it.next();
            //去掉封闭房间
            if(sec.getExitCount() == 0){
                it.remove();

            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                maps[i][j] = Tiles.tile().empty;
            }
        }

        putPathIntoMap(mainPathWall);
        putPathIntoMap(mainPath);
        for(Path path : sectionPaths){
            Path wall = new Path(getPathWallPoints(path),Tiles.tile().corridorwall);
            putPathIntoMap(wall);
            putPathIntoMap(path);
        }

        for (Section sec : sections) {
            putSectionIntoMap(sec);
        }

    }

    private void removeOtherDoor(Section sec) {
        Iterator<SectionExit> it = sec.getExits().iterator();
        while(it.hasNext()){
            SectionExit exit = it.next();
            int offsetX = 0;
            int offsetY = 0;
            switch (exit.dir){
                case 0:offsetX = -1;offsetY = 0;break;
                case 1:offsetX = 0;offsetY = -1;break;
                case 2:offsetX = 1;offsetY = 0;break;
                case 3:offsetX = 0;offsetY = 1;break;
            }
            if(maps[exit.x+offsetX][exit.y+offsetY].getValue()==Tiles.tile().corridorwall.getValue()){
                sec.updateArea(exit.x,exit.y,Tiles.tile().roomwall);
                it.remove();
            }
        }

    }


    /**
     * 生成房间之间的通路
     * 1.选取主圈房间四条边的一个点
     * 点满足条件：1.不是角落,2两边都是障碍物
     * 2.从这个点沿着边的垂直方向往外走，没有障碍且遇到其它非主圈房间的边，则形成通路
     */
    private void addSectionPath() {
        sectionPaths = new ArrayList<Path>();
        for (Section sec : sections) {
            for (int dir = 0; dir < 4; dir++) {
                Path path = getSectionPath(sec,dir);
                if(path != null){
                    sectionPaths.add(path);
                }
            }
        }
    }
    private Path getSectionPath(Section sec,int type){
        //只处理主圈上的房间
        if(sec.getIndex() > 0){
            return null;
        }
        //随机选择该边去掉两头的一个点
        int x = RandomUtils.nextInt(sec.left+1,sec.right-2);
        int y = RandomUtils.nextInt(sec.top+1,sec.bottom-2);
        Path path = new Path();
        boolean isPath = false;
        int i=0;
        boolean flag=true;
        int offset=0;
        int tempx = x;
        int tempy = i;
        int count = 0;
        //判断方向
        switch (type){
            case 0:i = sec.left;offset = -1;break;
            case 1:i = sec.top;offset = -1;break;
            case 2:i = sec.right-1;offset = 1;break;
            case 3:i = sec.bottom-1;offset = 1;break;
            default:break;
        }
        while (flag){
            switch (type){
                case 0:flag = i > 0;tempx= i;tempy=y;break;
                case 1:flag = i > 0;tempx= x;tempy=i;break;
                case 2:flag = i < width-1;tempx= i;tempy=y;break;
                case 3:flag = i < height-1;tempx= x;tempy=i;break;
                default:break;
            }
            i+=offset;//根据方向i循环递增或者递减
            if(!flag||tempx<0||tempy<0){
                break;
            }
            path.getList().add(new Point(tempx,tempy));
            if(maps[tempx][tempy].getValue() == Tiles.tile().roomwall.getValue()){
                //路径为有效路径
                if(count != 0){
                    isPath = true;
                    //设置开始的点为门
                    sec.setExit(path.getList().get(0).x,path.getList().get(0).y,type);
                    //搜索对应终点也为门
                    for(Section secOther : sections){
                        if(secOther.contains(tempx,tempy)){
                            secOther.setExit(tempx,tempy,type);
                        }
                    }
                    break;
                }
            }
            int value = maps[tempx][tempy].getValue();
            if(value == Tiles.tile().roomcorner.getValue()
                    ||value == Tiles.tile().corridorwall.getValue()
                    ){
                path.getList().remove(path.getList().size()-1);
                isPath = false;
                break;
            }
            if(count++ > 16){
                isPath = false;
                break;
            }

        }
        if(!isPath){
            path = null;
        }
        return path;
    }

    /**
     * 如果相交的是角落，那么选择角落和它旁边的块为地板
     * @param sec
     * @param x
     * @param y
     */
    private void changeSectionCorner(Section sec,int x,int y){
        if(sec.isCorner(x,y)){
            sec.updateArea(x,y,Tiles.tile().roomfloor);
            sec.removeExit(x, y);
            int offsetX = (x == sec.left)?1:-1;
            int offsetY = (y == sec.top)?1:-1;
//            if(sec.getArea()[x-sec.left+offsetX][y-sec.top].isObstacle()&&sec.getArea()[x-sec.left][y-sec.top+offsetY].isObstacle()){
//
//            }
            sec.getArea()[x-sec.left+offsetX][y-sec.top] = Tiles.tile().roomfloor;
            sec.getArea()[x-sec.left][y-sec.top+offsetY] = Tiles.tile().roomfloor;
        }
    }

    /**
     * 添加圆形路径和对应墙体
     * @param x0
     * @param y0
     * @param r
     */
    private void addCirclePath(int x0, int y0, int r) {
        mainPath = new Path(MathUtils.getCircleDungeonPoints(x0, y0, r), Tiles.tile().corridorfloor);
        mainPathWall = new Path(getPathWallPoints(mainPath), Tiles.tile().corridorwall);
    }
    private List<Point> getPathWallPoints(Path main){
        List<Point> walls = new ArrayList<Point>();
        for(Point p : main.getList()){
            walls.add(new Point(p.x,p.y+1));
            walls.add(new Point(p.x,p.y-1));
            walls.add(new Point(p.x+1,p.y));
            walls.add(new Point(p.x+1,p.y+1));
            walls.add(new Point(p.x+1,p.y-1));
            walls.add(new Point(p.x-1,p.y));
            walls.add(new Point(p.x-1,p.y+1));
            walls.add(new Point(p.x-1,p.y-1));
        }
        return walls;
    }
    private void putTileIntoMap(int x,int y,TileType tileType){
        if (x >= 0 && x < width && y >= 0 && y < height) {
            maps[x][y] = tileType;
        }
    }
    private void putPathIntoMap(Path path) {
        for (int i = 0; i < path.size(); i++) {
            Point p = path.getList().get(i);
            int x = p.x;
            int y = p.y;
            if (x >= 0 && x < width && y >= 0 && y < height) {
//                maps[x][y] = Tiles.tile().getTileType(Tiles.ToSBC(""+i%10),0);
//                maps[x][y] = Tiles.tile().wall;
                maps[x][y] = path.getTileType();
            }
        }
    }

    private void addSection(int left, int top) {
        int right = left + RandomUtils.nextInt(8, 16);
        int bottom = top + RandomUtils.nextInt(8, 16);
        if (right >= width || bottom >= height) {
            return;
        }
        for (Section sec : sections) {
            if (sec.isIntersect(sec, new Rect(left, top, right, bottom))) {
                return;
            }
        }
        Section section = SectionUtils.getRestSection(left, top, right, bottom);
        sections.add(section);
    }


    private void putSectionIntoMap(Section section) {
        for (int j = 0; j < section.height(); j++) {
            for (int i = 0; i < section.width(); i++) {
                maps[section.left + i][section.top + j] = section.getArea()[i][j];
            }
        }
    }

    public String getDisPlay() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                stringBuilder.append(maps[i][j].getName());
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public int getMainSectionsIndex() {
        return mainSectionsIndex;
    }

    public TileType[][] getMaps() {
        return maps;
    }

    public List<Section> getSections() {
        return sections;
    }
}
