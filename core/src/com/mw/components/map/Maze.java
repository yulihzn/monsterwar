package com.mw.components.map;

/**
 * Created by BanditCat on 2016/9/13.
 * 新的AStar算法。暂时不用
 */
import java.util.ArrayList;
import java.util.List;

public class Maze {

    public final Integer OBLIQUE = 14;
    public final Integer STEP = 10;
    public int[][] MazeArray ;
    List<Point> CloseList;
    List<Point> OpenList;

    public Maze(int[][] maze)
    {
        this.MazeArray = maze;
        OpenList = new ArrayList<Point>();
        CloseList = new ArrayList<Point>();
    }

    public List<Point> FindPath(Point start, Point end, Boolean IsIgnoreCorner){
//    	System.out.println(start.getX()+":"+start.getY()+"---->"+end.getX()+":"+end.getY());
        if(isSamePoint(start, end)){
            end=OpenList.get(isContains(OpenList,end));
            CloseList.add(OpenList.get(isContains(OpenList,end)));
    		/*for(Point point:CloseList){
    			System.out.print(point.getX()+":"+point.getY()+"---->");
    		}
    		System.out.println();*/
            return CloseList;
        }else {
            if(isContains(OpenList,start)==-1){
                OpenList.add(start);
            }
            getArroudPoint(start,end);
            OpenList.remove(start);
//    		CloseList.add(start);

            //找出F值最小的点
            Point temPoint = MinPoint(OpenList);
            if(isContains(CloseList, temPoint.getParentPoint())==-1){
                CloseList.add(temPoint.getParentPoint());
            }
            return FindPath(temPoint, end, IsIgnoreCorner);

        }

    }

    public void getArroudPoint(Point start,Point end){
        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                if(!(i==0&&j==0)){
                    Point point=new Point(start.getX()+i, start.getY()+j);
                    point.setG(getObsolute(i)==getObsolute(j)?14:10);
                    point.setH(10*((Integer)getObsolute(point.getX()-end.getX())+(Integer)getObsolute(point.getY()-end.getY())));
                    point.CalcF();
                    point.setParentPoint(start);
                    boolean flag=false;
                    Integer integer=0;
                    for(int index=0;index<OpenList.size();index++){
                        if(isSamePoint(OpenList.get(index), point)){
                            flag=true;
                            integer=index;
                            break;
                        }
                    }
                    if(!flag){
                        if(this.MazeArray[point.getX()][point.getY()]!=1&&(isContains(CloseList, point)==-1)){
                            OpenList.add(point);
                        }
                    }else {
                        //已经存在就要进行判断谁去谁留
                        if((OpenList.get(integer).getG()>getG_Sum(point))&&(isContains(CloseList, point)==-1)){
                            //C<D
                            OpenList.remove(integer);
                            OpenList.add(point);
                            if(isContains(CloseList, start)==-1){
                                CloseList.add(start);
                            }
                        }
                    }
                }
            }
        }
    }
    //判断集合list中是否含有某个元素  包含就返回下标，不包含返回-1
    public Integer isContains(List<Point> list , Point point){
        Integer index=-1;
        for(int i=0;i<list.size();i++){
            if((list.get(i).getX()==point.getX())&&(list.get(i).getY()==point.getY())){
                index=i;
            }
        }
        return index;
    }
    //获得整条链上的G值总和
    public Integer getG_Sum(Point point){
        Integer sum=0;
        while(point.getParentPoint()!=null){
            sum+=point.getG();
            point=point.getParentPoint();
        }
        return sum;
    }
    //判断point是相同点
    public Boolean isSamePoint(Point prePoint , Point nextPoint){
        return (prePoint.getX()==nextPoint.getX())&&(prePoint.getY()==nextPoint.getY())?true:false;
    }
    //获得整数绝对值
    public Object getObsolute(Integer num){
        return num>0?num:(-num);
    }
    //获得符合条件的最小路径点
    public Point MinPoint(List<Point> points)
    {
        Integer min = points.get(0).getF();
        Integer index = 0;
        for(int i=0;i<points.size();i++){
            if(min>points.get(i).getF()){
                min=points.get(i).getF();
                index=i;
            }
        }
        return points.get(index);
    }
    public class Point {

        private Point parentPoint;
        private Integer F;
        private Integer G;
        private Integer H;
        private Integer X;
        private Integer Y;

        public Point(Integer x, Integer y) {
            this.X = x;
            this.Y = y;
        }

        public void CalcF() {
            this.F = this.G + this.H;
        }

        public Point getParentPoint() {
            return parentPoint;
        }

        public void setParentPoint(Point parentPoint) {
            this.parentPoint = parentPoint;
        }

        public Integer getF() {
            return F;
        }

        public void setF(Integer f) {
            F = f;
        }

        public Integer getG() {
            return G;
        }

        public void setG(Integer g) {
            G = g;
        }

        public Integer getH() {
            return H;
        }

        public void setH(Integer h) {
            H = h;
        }

        public Integer getX() {
            return X;
        }

        public void setX(Integer x) {
            X = x;
        }

        public Integer getY() {
            return Y;
        }

        public void setY(Integer y) {
            Y = y;
        }
    }
}
