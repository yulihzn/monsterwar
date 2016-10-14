package com.mw.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.mw.model.*;
import com.mw.model.Castle;

import java.util.List;
import java.util.Random;

/**
 * 生成256x256大地图
 * 将地图分成16x16个大区域命名为area0_0，area0_1...保存到一个列表
 * 每个大区域保存了当前生成的地貌属性，例如：整体属性-城堡，其中一格是城墙
 * 挑选特定的一个大区域例如area5_5是野外，其中一个元素map0_0是草地，生成该小区域是类型是野外草地，
 * 将生成的map放入area里面的map列表，直到生成了256个map，代表area建立完毕保存到json。
 * 放置玩家到map8_8里面的8,8的位置定位出生地点，保存玩家的大区域位置，小区域位置和具体位置。
 * 1.读取map8_8到展示的地图里面，当玩家视野到了移动地图的时候截取对应数组部分到新数组里面，但是这样太复杂了。。。
 * 2.一次性读取大区域到map里面，但是可能会占用太多内存。
 * 3.到了地图边界的时候进入下一个场景需要读图。
 * 无缝的办法
 * 1.读取map8_8和它周围的8
 *
 */
public class MapEditor {
	public static final int CASTLE = 100;
	public static final int VILLAGE = 101;

	public static final int DIRT = 9;
	public static final int GRASS = 18;
	public static final int TREE = 25;
	public static final int WATER = 27;
	public static final int STONE = 23;
	public static final int WALL = 0;
	public static final int DOOR = 10;
	public static final int ROAD = 1;
	public static final int BUILDING = 7;
	public static final int BUILDING1 = 8;
	public static final int BUILDING2 = 6;
	public static final int GUARD_WATER = 12;
	public static final int block = 256;
	private int[][] arr = new int[block][block];
	private Random random;
	private PerlinNoise2D perlin = new PerlinNoise2D();
	private double threshold = .14, threshold2 = -.14;
	double x0 = 0, y0 = 0, dx = 20, dy = 20;
	int w = block, h = block, d = 2;

	private WorldMapModel worldMapModel = new WorldMapModel();
	public MapEditor() {
		random = new Random(System.currentTimeMillis());
	}
	public WorldMapModel create() {
		random.setSeed(System.currentTimeMillis());
		dx = 20+random.nextInt(20);
		dy = 20+random.nextInt(20);
		x0 = random.nextInt(9999);
		y0 = random.nextInt(9999);
		for(int i = 0;i < block;i++){
			for(int j = 0;j < block;j++){
				arr[i][j] = 0;
				double x = dx * i / w + x0,
                        y = dy * j / h + y0;
				double p = perlin.perlinNoise(x, y);
                if (p > threshold){
                	arr[i][j] = TREE;
                }else if (p < threshold2){
                	arr[i][j] = WATER;
                }
				double pr = random.nextDouble();
				if(pr>=0&&pr<0.20&&arr[i][j]!=WATER){
					arr[i][j]= STONE;
				}
				if(pr>=0.20&&pr<0.25&&arr[i][j]!=WATER){
					arr[i][j]= GRASS;
				}
			}
		}
		Array<GridPoint2> indexs = new Array<GridPoint2>();
		for (int i = 0; i < arr.length; i+=16) {
			for (int j = 0; j < arr[0].length; j+=16) {
				indexs.add(new GridPoint2(i,j));
			}
		}
		worldMapModel.getAreas().clear();
		for (int i = 0; i < 22; i++) {
			GridPoint2 g = indexs.get(random.nextInt(indexs.size));
			buildCastle(g.x,g.y);
			indexs.removeValue(g,false);
		}
		for (int i = 0; i < 56; i++) {
			GridPoint2 g = indexs.get(random.nextInt(indexs.size));
			buildVillage(g.x,g.y);
			indexs.removeValue(g,false);
		}
		for (int i = 0; i < 178; i++) {
			GridPoint2 g = indexs.get(random.nextInt(indexs.size));
			buildWild(g.x,g.y);
			indexs.removeValue(g,false);
		}
//		for (int i = 0; i < castles.size; i++) {
//			Castle castle = castles.get(i);
//		}
//		buildRoad(castles.get(0),castles.get(1));
		worldMapModel.setArr(arr);
		return worldMapModel;
	}
	private Array<GridPoint2> getRoadList(GridPoint2 p1, GridPoint2 p2){
		int x0 = Math.min(p1.x,p2.x);
		int y0 = Math.min(p1.y,p2.y);
		int x = Math.abs(p1.x-p2.x)+1;
		int y = Math.abs(p1.y-p2.y)+1;
		AStarMap aStarMap = new AStarMap(x,y);
		int[][] aStarData = new int[y][x];
		String str = "\n";
		for (int j = 0; j < x; j++) {
			for (int i = 0; i < y; i++) {
				int block = arr[y0+j][x0+i];
				if(block == WALL
						||block== ROAD
						||block== BUILDING1
						||block== GUARD_WATER){
					aStarData[i][j] = 1;
				}else{
					aStarData[i][j] = 0;
				}
				str += aStarData[i][j]+"";
			}
			str+="\n";
		}
		Gdx.app.log("astar",str);
		aStarMap.loadData(aStarData,1,0);
		aStarMap.setSource(new AStarNode(p1.x-x0,p1.y-y0));
		aStarMap.setTarget(new AStarNode(p2.x-x0,p2.y-y0));
		List<AStarNode> list = aStarMap.find();
		Array<GridPoint2> array = new Array<GridPoint2>();
		for(AStarNode n:list){
			array.add(new GridPoint2(x0+n.getX(),y0+n.getY()));
		}
		return array;
	}
	//连接2个城堡
	private void buildRoad(com.mw.model.Castle castle1, com.mw.model.Castle castle2){
		GridPoint2 p1 = new GridPoint2();
		GridPoint2 p2 = new GridPoint2();
		//判断位置，有8种情况，四面八方
		if(castle1.getX0()>castle2.getX0()){
			if(castle1.getY0()>castle2.getY0()){
				//1在2右下方
				p1 = castle1.getExits()[2];
				p2 = castle2.getExits()[1];
			}
			if(castle1.getY0()<castle2.getY0()){
				//1在2左下方
				p1 = castle1.getExits()[3];
				p2 = castle2.getExits()[1];
			}
			if(castle1.getY0()==castle2.getY0()){
				//1在2正下方
				p1 = castle1.getExits()[0];
				p2 = castle2.getExits()[1];
			}
		}
		if(castle1.getX0()==castle2.getX0()){
			if(castle1.getY0()>castle2.getY0()){
				//1在2右边
				p1 = castle1.getExits()[2];
				p2 = castle2.getExits()[3];
			}
			if(castle1.getY0()<castle2.getY0()){
				//1在2左边
				p1 = castle1.getExits()[3];
				p2 = castle2.getExits()[2];
			}
		}
		if(castle1.getX0()<castle2.getX0()){
			if(castle1.getY0()>castle2.getY0()){
				//1在2右上方
				p1 = castle1.getExits()[2];
				p2 = castle2.getExits()[0];
			}
			if(castle1.getY0()<castle2.getY0()){
				//1在2左上方
				p1 = castle1.getExits()[3];
				p2 = castle2.getExits()[0];
			}
			if(castle1.getY0()==castle2.getY0()){
				//1在2正上方
				p1 = castle1.getExits()[1];
				p2 = castle2.getExits()[0];
			}
		}
		p1.x += castle1.getX0();
		p1.y += castle1.getY0();
		p2.x += castle2.getX0();
		p2.y += castle2.getY0();
		Array<GridPoint2> array = getRoadList(p1,p2);
		for(GridPoint2 p:array){
			arr[p.x][p.y] = ROAD;
		}
	}

	private void buildCastle(int x0,int y0) {
		com.mw.model.Castle castle = new Castle(x0,y0,random);
		int[][] a = castle.getArr();
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				arr[x0+i][y0+j]=a[i][j];
			}
		}
		worldMapModel.getAreas().put(castle.getName(),castle);

	}
	private void buildVillage(int x0,int y0){
		com.mw.model.Village village = new com.mw.model.Village(x0,y0,random);
		int[][] a = village.getArr();
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				arr[x0+i][y0+j]=a[i][j];
			}
		}
		worldMapModel.getAreas().put(village.getName(),village);
	}

	private void buildWild(int x0,int y0){
		Wild wild = new Wild(x0,y0,random);
		int[][] a = wild.getArr();
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				a[i][j]=arr[x0+i][y0+j];
			}
		}
		worldMapModel.getAreas().put(wild.getName(),wild);
	}

	public int[][] getArr() {
		return arr;
	}

	public String getArrayString(){
		StringBuilder stringBuilder = new StringBuilder();
		String[][] strs = new String[block][block];
		for(int i = 0;i < block;i++){
			for(int j = 0;j < block;j++){
				switch (arr[i][j]) {
				case DIRT:strs[i][j]="　";break;
				case GRASS:strs[i][j]="ｗ";break;
				case TREE:strs[i][j]="Ｙ";break;
				case WATER:strs[i][j]="～";break;
				case STONE:strs[i][j]="ｏ";break;
				case WALL:strs[i][j]="＝";break;
				case DOOR:strs[i][j]="－";break;
				case ROAD:strs[i][j]="＋";break;
				case BUILDING:strs[i][j]="Ｍ";break;
				case BUILDING1:strs[i][j]="Ｎ";break;
				case BUILDING2:strs[i][j]="Ｈ";break;
				case GUARD_WATER:strs[i][j]="＊";break;
				}
				stringBuilder.append(strs[i][j]);
			}
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

	public void setArr(int[][] arr) {
		this.arr = arr;
	}
}
