package com.mw.map;

import java.util.Random;

public class MapEditor {
	public static final int DIRT = 0;
	public static final int GRASS = 1;
	public static final int TREE = 2;
	public static final int WATER = 3;
	public static final int STONE = 4;
	public static final int WALL = 5;
	public static final int DOOR = 6;
	public static final int ROAD = 7;
	public static final int BUILDING = 8;
	public static final int BUILDING1 = 9;
	public static final int BUILDING2 = 10;
	public static final int block = 256;
	private int[][] arr = new int[block][block];
	private Random random;
	private PerlinNoise2D perlin = new PerlinNoise2D();
	private double threshold = .14, threshold2 = -.14;
	double x0 = 0, y0 = 0, dx = 20, dy = 20;
	int w = block, h = block, d = 2;
	public MapEditor() {
		random = new Random(System.currentTimeMillis());
	}
	public void Create() {
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
		for (int i = 0; i < 22; i++) {
			buildCastle(random.nextInt(16)*16,random.nextInt(16)*16);
		}
	}

	private void buildCastle(int x0,int y0) {
		Castle castle = new Castle(x0,y0,random);
		int[][] a = castle.getArr();
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				arr[x0+i][y0+j]=a[i][j];
			}
		}

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
				}
				stringBuilder.append(strs[i][j]);
			}
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}
}
