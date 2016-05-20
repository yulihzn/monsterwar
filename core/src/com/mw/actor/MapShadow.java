package com.mw.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.mw.utils.Dungeon;

import java.awt.Point;
import java.util.Collections;

/**
 * Created by BanditCat on 2016/3/29.
 */
public class MapShadow extends Actor{
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private GridPoint2 sightPosIndex = new GridPoint2(0,0);
    private int width = 0,height = 0;
    private int sightRadius = 6;
    private Array<EdgeLine> lines = new Array<EdgeLine>();
    private int[][] dungeonArray;
    //test
    private Array<Vector2> points = new Array<Vector2>();

    public MapShadow(OrthographicCamera camera,int width,int height,int[][] dungeonArray) {
        this.dungeonArray = dungeonArray;
        this.camera = camera;
        this.height = height;
        this.width = width;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setProjectionMatrix(camera.combined);
        //混合模式
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        //画视野
        int sightX = 32*getSightPosIndex().x-32*sightRadius;
        int sightY = 32*getSightPosIndex().y-32*sightRadius;
        int sightWidth = 32*(sightRadius*2+1);
        int sightHeight = 32*(sightRadius*2+1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0,0,0,0.9f));
        shapeRenderer.rect(sightX,sightY,sightWidth,sightHeight);
        //画阴影
        shapeRenderer.setColor(new Color(0,0,0,0.75f));
        shapeRenderer.rect(0,0,sightX,height);
        shapeRenderer.rect(sightX,0,sightWidth,sightY);
        shapeRenderer.rect(sightX+sightWidth,0,width-sightX-sightWidth,height);
        shapeRenderer.rect(sightX,sightY+sightHeight,sightWidth,height-sightY-sightHeight);
        shapeRenderer.end();

        //画线
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        float sx = (sightPosIndex.x*32)+16;//视野的横坐标
        float sy = (sightPosIndex.y*32)+16;//视野的纵坐标
        for (int i = 0;i < lines.size;i++){
            EdgeLine ed = lines.get(i);
            if(ed.isNeedToDraw()){
                shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.line(ed.getStart().x,ed.getStart().y,ed.getEnd().x,ed.getEnd().y);
                if(ed.getNext() != -1){
                    shapeRenderer.setColor(Color.BLUE);
                    shapeRenderer.line(ed.getEnd().x,ed.getEnd().y,
                            lines.get(ed.getNext()).getStart().x,lines.get(ed.getNext()).getStart().y);
                }
                if(ed.getPrev() != -1){
                    shapeRenderer.setColor(Color.BLUE);
                    shapeRenderer.line( lines.get(ed.getPrev()).getStart().x,lines.get(ed.getPrev()).getStart().y
                    ,ed.getStart().x,ed.getStart().y);
                }
            }
        }
        //画点
        shapeRenderer.setColor(Color.ORANGE);
        for (Vector2 v : points){
            shapeRenderer.point(v.x,v.y,0);
        }
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public GridPoint2 getSightPosIndex() {
        return sightPosIndex;
    }

    public void dispose(){
        shapeRenderer.dispose();
    }

    private EdgeLine getEdgeLine(int x,int y,int x1,int y1){
        int id = lines.size -1;//按加入位置创建id
        if(id < 0){
            id = 0;
        }
        float sx = (sightPosIndex.x*32)+16;//视野的横坐标
        float sy = (sightPosIndex.y*32)+16;//视野的纵坐标
        EdgeLine edgeLine = new EdgeLine();
        edgeLine.getStart().x = x;
        edgeLine.getStart().y = y;
        edgeLine.getEnd().x = x1;
        edgeLine.getEnd().y = y1;
        float dx = x+(x1-x)/2;
        float dy = y+(y1-y)/2;
        edgeLine.setDistance(Vector2.dst(sx,sy,dx,dy));
        edgeLine.setId(id);
        return edgeLine;
    }

    public void updateLines(){
        lines.clear();
        int sx = (sightPosIndex.x*32)+16;//视野的横坐标
        int sy = (sightPosIndex.y*32)+16;//视野的纵坐标
        int x = 0;//tile左下角横坐标
        int y = 0;//tile左下角纵坐标

        int x1 = 0;//tile左上角横坐标
        int y1 = 0;//tile左上角纵坐标

        int x2 = 0;//tile右上角横坐标
        int y2 = 0;//tile右上角纵坐标

        int x3 = 0;//tile右下角横坐标
        int y3 = 0;//tile右下角纵坐标
        int iMin = sightPosIndex.x - sightRadius;
        if(iMin < 0){
            iMin = 0;
        }
        int iMax = sightPosIndex.x + sightRadius+1;
        if(iMax > 32-1){
            iMax = 32-1;
        }
        int jMin = sightPosIndex.y - sightRadius;
        if(jMin < 0){
            jMin = 0;
        }
        int jMax = sightPosIndex.y + sightRadius+1;
        if(jMax > 32-1){
            jMax = 32-1;
        }
        for (int i = iMin; i < iMax; i++) {
            for (int j = jMin; j < jMax; j++) {
                x = i*32;
                y = j*32;
                x1 = x;
                y1 = y+1*32;
                x2 = x+1*32;
                y2 = y+1*32;
                x3 = x+1*32;
                y3 = y;
                if(dungeonArray[i][j] != Dungeon.tileStoneWall
                        &&dungeonArray[i][j]!= Dungeon.tileDirtWall
                        &&dungeonArray[i][j]!= Dungeon.tileUnused){
                    if(!(i==iMin||i==iMax-1||j==jMin||j==jMax-1)){

                        continue;//如果不是视野障碍物而且不是边就跳过
                    }
                }
                /**这里 边被照亮的情况有八种**/
                if(sx < x && sy < y){
                    //左下
                    // Px,y->Px1,y1
                    // Px3,y3->Px,y
                    lines.add(getEdgeLine(x,y,x1,y1));
                    lines.add(getEdgeLine(x3,y3,x,y));
                }
                else if(sx <= x && sy >= y && sy <= y1){
                    //左中
                    // Px,y->Px1,y1
                    lines.add(getEdgeLine(x,y,x1,y1));
                }
                else if(sx < x1 && sy > y1){
                    //左上
                    // Px,y->Px1,y1
                    // Px1,y1->Px2,y2
                    lines.add(getEdgeLine(x,y,x1,y1));
                    lines.add(getEdgeLine(x1,y1,x2,y2));
                }
                else if(sx >= x1 && sy >= y1 && sx <= x2){
                    //上中
                    // Px1,y1->Px2,y2
                    lines.add(getEdgeLine(x1,y1,x2,y2));
                }
                else if(sx > x2 && sy > y2){
                    //右上
                    // Px1,y1->Px2,y2
                    // Px2,y2->Px3,y3
                    lines.add(getEdgeLine(x1,y1,x2,y2));
                    lines.add(getEdgeLine(x2,y2,x3,y3));
                }
                else if(sx >= x2 && sy <= y2 && sy >= y3){
                    //右中
                    // Px2,y2->Px3,y3
                    lines.add(getEdgeLine(x2,y2,x3,y3));
                }
                else if(sx > x3 && sy < y3){
                    //右下
                    // Px2,y2->Px3,y3
                    // Px3,y3->Px,y
                    lines.add(getEdgeLine(x2,y2,x3,y3));
                    lines.add(getEdgeLine(x3,y3,x,y));
                }
                else if(sx >= x && sy <= y && sx <= x3){
                    //下中
                    // Px3,y3->Px,y
                    lines.add(getEdgeLine(x3,y3,x,y));
                }
            }
        }
        //按距离从近到远排序
        lines.sort();
        for (int i=0; i<this.lines.size; i++) {
            EdgeLine eNow = lines.get(i);
            if (eNow.getPrev() != -1 && eNow.getNext() != -1) {
                continue;
            }
            for(int j=0; j<lines.size; j++) {
                if (i == j) {
                    continue;
                }
                EdgeLine eCheck = lines.get(j);
                if (eCheck.getPrev() != -1 && eCheck.getNext() != -1) {
                    continue;
                }
                //如果一条边的头是另一条边的尾，那么这条边的prev是另一条，另一条的next是这一条
                if (eNow.getEnd().x == eCheck.getStart().x && eNow.getEnd().y == eCheck.getStart().y) {
                    eNow.setNext(j);;
                    eCheck.setPrev(i);
                }
            }
        }

//        //切割边缘
        points.clear();
        for (int k = 0;k < lines.size;k++) {
            EdgeLine e = lines.get(k);
            boolean getOne = false;
            if(e.getNext() == -1){//当前线段的下一条不存在
                for (int i = k+1; i < lines.size; i++) {//从列表当前线段的下一条开始读取
                    EdgeLine ed = lines.get(i);
                    if( i==k){
                        continue;
                    }
                    //获取下一条线段是否和当前线段有交点
                    Vector2 p = getIntersection(sx,sy,e.getEnd().x,e.getEnd().y,
                    ed.getStart().x,ed.getStart().y,ed.getEnd().x,ed.getEnd().y);
                    //如果有交点，下一条的头是交点，设置当前线段的下一条
                    if(p.x != -1 && p.y != -1){
                        points.add(p);
                        ed.getStart().x = p.x;
                        ed.getStart().y = p.y;
                        e.setNext(i);
                        ed.setPrev(k);
                        getOne = true;
                        break;
                    }
                }
            }
            if(getOne){
//                break;
            }
            if(e.getPrev() == -1){
                for (int i = k+1; i < lines.size; i++) {
                    EdgeLine ed = lines.get(i);
                    if(i==k){
                        continue;
                    }
                    Vector2 p = getIntersection(sx,sy,e.getStart().x,e.getStart().y,
                            ed.getStart().x,ed.getStart().y,ed.getEnd().x,ed.getEnd().y);
                    if(p.x != -1 && p.y != -1){
                        points.add(p);
                        ed.getEnd().x = p.x;
                        ed.getEnd().y = p.y;
                        e.setPrev(i);
                        ed.setNext(k);
                        break;
                    }
                }
            }
        }
    }
    private Vector2 getIntersection(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
        try {
            if(x1 - x2 == 0){
                return  new Vector2(-1,-1);
            }
            //第一条直线
            double a = (y1 - y2) / (x1 - x2);
            double b = (x1 * y2 - x2 * y1) / (x1 - x2);
//            System.out.println("求出该直线方程为: y=" + a + "x + " + b);
            if(x3 - x4 == 0){
                return  new Vector2(-1,-1);
            }
            //第二条
            double c = (y3 - y4) / (x3 - x4);
            double d = (x3 * y4 - x4 * y3) / (x3 - x4);
//            System.out.println("求出该直线方程为: y=" + c + "x + " + d);
            if((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4) == 0){
                return  new Vector2(-1,-1);
            }

            double x = ((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1))
                    / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));

            double y = ((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4))
                    / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4));

            System.out.println("他们的交点为: (" + x + "," + y + ")");
            //判断是否在线段上
            boolean isOnSegment = true;
            if((x > x3 && x > x4) ||(x < x3 && x < x4)){
                isOnSegment = false;
            }
            if((y > y3 && y > y4) ||(y < y3 && y < y4)){
                isOnSegment = false;
            }
            //判断是否在射线上
            //保持x,y和x2,y2在同一象限
            if((x1 - x2 > 0 && x1 - x < 0)
                    ||(x1 - x2 < 0 && x1 - x > 0)
                    ||(y1 - y2 > 0 && y1 - y < 0)
                    ||(y1 - y2 < 0 && y1 - y > 0)){
                isOnSegment = false;
            }

            if(!isOnSegment){
                x = -1;
                y = -1;
            }
            return  new Vector2((float) x,(float)y);
        }catch (Exception e){
            Gdx.app.log("exec","除0异常");
            return  new Vector2(-1,-1);
        }
    }

}