package com.mw.components.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.mw.components.map.model.Castle;

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
 * NEW
 * 生成256x256大地图
 * 将地图分成16x16个大区域命名为area0_0，area0_1...放到一个列表
 * 每个大区域保存了当前生成的地貌属性->
 * 区域类型：城堡，村庄，野外
 * 瓷砖类型：草地，石头，泥土，水，城墙，哨所，道路，要塞，杂货店，民房...
 * 每一个大区域都有一个数组保存瓷砖和一个变量保存区域类型，以及原点和名字
 * 大地图文件存放在save/world/world.tmx
 * 其中瓷砖类型会放入数组，大区域会按key-value的形式放入它的类型，原点，和名字
 *
 * 每次重新进入游戏MapEditor初始化会先去取大地图，如果大地图为空或者新的存档就建立一个大地图
 * 如果不为空，tiledmap转为MapEditor的各个变量
 *
 *
 * 从存档选择当前玩家在的大区域初始化，传入MapEditor里对应的Area，读取地图save/world/area/area0_0.tmx如果大区域为空或者新的存档就建立一个大区域
 * 如果不为空，tiledmap转为一个AreaEditor
 * AreaEditor生成256x256大区域
 * 将区域分成16x16个小区域命名为map0_0,map0_1...放到一个列表
 * 每个小区域保存了具体每个tile的类型->
 * 瓷砖类型：地板，障碍，装饰，阴影
 * 遍历大数组根据区域类型和瓷砖类型来生成地形，保存地图
 * 大区域地图存放在save/world/area/area0_0
 * 其中瓷砖类型会放入数组，属性存入key-value
 *
 *
 * 大地图上的每一个元素都是16x16的小区域
 * 根据大地图上面设定好的类型来选择，这样做的前置是设定好所有的类型合理分类，例如草地可能有多种，但是共用一个类
 * 小区域里面再细分功能和具体实现
 */
public class MapEditor {
	public static long SEED = 3;//种子为3生成的00是城堡

	public static final int CASTLE = 100;//城堡
	public static final int VILLAGE = 101;//村子
	public static final int WILD = 102;//野外
	public static final int DUNGEON = 103;//地牢

//	public static final int SHADOW = 1;
//	public static final int TRANS = 0;
//	public static final int DIRT = 2;
//	public static final int WATER = 5;
//	public static final int GRASS = 7;
//	public static final int TREE = 8;
//	public static final int GUARD_WATER = 9;
//	public static final int ROAD = 11;
//	public static final int WALL = 13;
//	public static final int STONE = 14;
//	public static final int DOOR = 15;
//	public static final int BUILDING2 = 16;
//	public static final int BUILDING = 17;
//	public static final int BUILDING1 = 18;
public static final int WIDTH = 256;
	public static final int HEIGHT = 256;
	private int[][] arr = new int[WIDTH][HEIGHT];
	private Random random;
	private PerlinNoise2D perlin = new PerlinNoise2D();
	private double threshold = .14, threshold2 = -.14;
	double x0 = 0, y0 = 0, dx = 20, dy = 20;
	int w = WIDTH, h = HEIGHT, d = 2;

	private com.mw.components.map.model.WorldMapModel worldMapModel = new com.mw.components.map.model.WorldMapModel();
	public MapEditor(long seed) {
		random = new Random(seed);
	}
	public com.mw.components.map.model.WorldMapModel create() {
		//噪声建立地形
		dx = 20+random.nextInt(20);
		dy = 20+random.nextInt(20);
		x0 = random.nextInt(9999);
		y0 = random.nextInt(9999);
		for(int i = 0;i < w;i++){
			for(int j = 0;j < h;j++){
				arr[i][j] = com.mw.components.map.SegType.WILD_DIRT.getValue();
				double x = dx * i / w + x0,
                        y = dy * j / h + y0;
				double p = perlin.perlinNoise(x, y);
                if (p > threshold){
                	arr[i][j] = com.mw.components.map.SegType.WILD_TREE.getValue();
                }else if (p < threshold2){
                	arr[i][j] = com.mw.components.map.SegType.WILD_WATER.getValue();
                }
				double pr = random.nextDouble();
				if(pr>=0&&pr<0.20&&arr[i][j]!= com.mw.components.map.SegType.WILD_WATER.getValue()){
					arr[i][j]= com.mw.components.map.SegType.WILD_STONE.getValue();
				}
				if(pr>=0.20&&pr<0.25&&arr[i][j]!= com.mw.components.map.SegType.WILD_WATER.getValue()){
					arr[i][j]= com.mw.components.map.SegType.WILD_GRASS.getValue();
				}
			}
		}
		//存储256个点遍历列表生成对应大区域
		Array<GridPoint2> indexs = new Array<GridPoint2>();
		for (int i = 0; i < arr.length; i+=16) {
			for (int j = 0; j < arr[0].length; j+=16) {
				indexs.add(new GridPoint2(i,j));
			}
		}
		//初始化大区域列表
		worldMapModel.getAreas().clear();
		//添加22个城堡
		for (int i = 0; i < 22; i++) {
			GridPoint2 g = indexs.get(random.nextInt(indexs.size));
			buildCastle(g.x,g.y);
			indexs.removeValue(g,false);
		}

		//添加178个野外
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
		com.mw.components.map.AStarMap aStarMap = new com.mw.components.map.AStarMap(x,y);
		int[][] aStarData = new int[y][x];
		String str = "\n";
		for (int j = 0; j < x; j++) {
			for (int i = 0; i < y; i++) {
				int block = arr[y0+j][x0+i];
				com.mw.components.map.SegType segType = com.mw.components.map.SegType.getType(block);
				if(segType == com.mw.components.map.SegType.CASTLE_WALL
						||segType== com.mw.components.map.SegType.WILD_STREET
						||segType== com.mw.components.map.SegType.CASTLE_BATTLEMENT
						||segType== com.mw.components.map.SegType.CASTLE_GUARD_WATER){
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
		aStarMap.setSource(new com.mw.components.map.AStarNode(p1.x-x0,p1.y-y0));
		aStarMap.setTarget(new com.mw.components.map.AStarNode(p2.x-x0,p2.y-y0));
		List<com.mw.components.map.AStarNode> list = aStarMap.find();
		Array<GridPoint2> array = new Array<GridPoint2>();
		for(com.mw.components.map.AStarNode n:list){
			array.add(new GridPoint2(x0+n.getX(),y0+n.getY()));
		}
		return array;
	}
	//连接2个城堡
	private void buildRoad(Castle castle1, Castle castle2){
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
			arr[p.x][p.y] = com.mw.components.map.SegType.WILD_STREET.getValue();
		}
	}

	private void buildCastle(int x0,int y0) {
		Castle castle = new Castle(x0,y0).init();
		int[][] a = castle.getArr();
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				arr[x0+i][y0+j]=a[i][j];
			}
		}
		worldMapModel.getAreas().put(castle.getName(),castle);

	}


	private void buildWild(int x0,int y0){
		com.mw.components.map.model.Wild wild = new com.mw.components.map.model.Wild(x0,y0,random);
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
		String[][] strs = new String[WIDTH][HEIGHT];
		for(int i = 0;i < WIDTH;i++){
			for(int j = 0;j < HEIGHT;j++){
				strs[i][j] = com.mw.components.map.SegType.getType(arr[i][j]).toString();
//				switch (arr[i][j]) {
//				case SegType.WILD_DIRT:strs[i][j]="　";break;
//				case SegType.WILD_GRASS:strs[i][j]="ｗ";break;
//				case SegType.WILD_TREE:strs[i][j]="Ｙ";break;
//				case SegType.WILD_WATER:strs[i][j]="～";break;
//				case SegType.WILD_STONE:strs[i][j]="ｏ";break;
//				case SegType.CASTLE_WALL:strs[i][j]="＝";break;
//				case SegType.CASTLE_GATE:strs[i][j]="－";break;
//				case SegType.WILD_STREET:strs[i][j]="＋";break;
//				case SegType.CASTLE_FORTRESSES:strs[i][j]="Ｍ";break;
//				case SegType.CASTLE_HOUSE:strs[i][j]="Ｎ";break;
//				case SegType.CASTLE_BATTLEMENT:strs[i][j]="Ｈ";break;
//				case SegType.CASTLE_GUARD_WATER:strs[i][j]="＊";break;
//				}
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
