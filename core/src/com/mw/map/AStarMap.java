package com.mw.map;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.HashMap; 
import java.util.List;
import java.util.Map; 

public class AStarMap {

	private List<AStarNode> openList = new ArrayList<AStarNode>();
	private Map<String, AStarNode> closeMap = new HashMap<String, AStarNode>();
	// private Set< AStarNode> closeSet = new HashSet< AStarNode>();

	private boolean isFind = false;
	private List<AStarNode> path = new ArrayList<AStarNode>();

	/** 障碍物 */
	public static final int STATE_BARRIER = 2;

	AStarNode target;//目标坐标
	AStarNode source;//起点坐标

	int[][] astarData;

	public AStarMap(int xGridNum, int yGridNum) {
		astarData = new int[yGridNum][xGridNum];
		source = new AStarNode(0, 0);
		target = new AStarNode(xGridNum - 1, yGridNum - 1);
	}
	private int[][]data;
	
	public int[][] getData() {
		return data;
	}

	public void setData(int[][] data) {
		this.data = data;
	}

//	public static void main(String[] args) {
//		AStarMap asm = new AStarMap(100, 200);
//		// int[][]data= {
//		// {0,0,0,0,0},{0,0,0,0,0},{0,0,1,0,0},{0,0,0,0,0},{0,0,0,0,0},
//		// {0,0,0,0,0},{0,0,0,0,0},{0,0,1,0,0},{0,0,0,0,0},{0,0,0,0,0},
//		// {0,0,0,0,0},{0,0,0,0,0},{0,0,1,0,0},{0,0,0,0,0},{0,0,0,0,0},
//		// {0,0,0,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,0,0,0},{0,0,0,0,0},
//		// {0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},
//		// };
//		
//		asm.loadData(data, 1, -1);
//		List list1 = asm.find();
//
//		for (int i = 0; i < list1.size(); i++) {
//			AStarNode asn = (AStarNode) list1.get(i);
//			System.out.println(asn.getX() + "          " + asn.getY());
//		}
//		asm.toString();
//		List list = asm.getOpenList();
//		for (int i = 0; i < list.size(); i++) {
//			AStarNode asn = (AStarNode) list.get(i);
//			System.out.println(asn.getX() + "          " + asn.getY()
//					+ " ====== " + asn.getFather().getX() + "         "
//					+ asn.getFather().getY());
//		}
//
//	}

	public AStarNode getTarget() {
		return target;
	}

	public AStarNode getSource() {
		return source;
	}

	public int[][] getAStarData() {
		return astarData;
	}

	/**
	 * 搜索算法
	 */
	public List<AStarNode> find() {
		init();
		AStarNode current = null;
		while (!isEnd() && !isFind()) {
			current = getMinFNodeFromOpenList();
			if (isAchieve(current)) { // 是否以及找到目标
				buildPath(current);
				isFind = true;
			} else {
				add2CloseMap(current);
				for (AStarNode neighbor : getNeighbor(current)) {
					if (neighbor == null || isInCloseMap(neighbor) || /*
																	 * 如果已经在关闭列表中，
																	 * 那么跳过
																	 */
					isCanNotGo(current, neighbor))
						continue;

					boolean isBetter = true;
					AStarNode nodeFromOpenList = getNodeFromOpenList(neighbor);
					if (nodeFromOpenList != null) { // 如果在开启列表中
						neighbor = nodeFromOpenList;
						int tg = neighbor.distinctG(current);
						isBetter = tg > neighbor.getG() ? false : true;
					} else {
						add2OpenList(neighbor);
					}
					if (isBetter) {
						neighbor.reCalculatorGAndH(current, target);
					}
				}
			}
		}
		return path;
	}

	private AStarNode getNodeFromOpenList(AStarNode node) {
		for (AStarNode openNode : openList) {
			if (openNode.equals(node))
				return openNode;
		}
		return null;
	}

	/**
	 * 判断从from结点到to结点是否不可行
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	private boolean isCanNotGo(AStarNode from, AStarNode to) {
		if (isBarrier(to)) { /* 如果这一格已经是障碍物，那么不能走 */
			return true;
		} else { /* 如果他旁边 */
			int offsetX = from.getX() - to.getX();
			int offsetY = from.getY() - to.getY();
			if (Math.abs(offsetX) == 1 && Math.abs(offsetY) == 1) { // 只有在走斜线的时候才要继续判断
				if ((offsetX == 1 && offsetY == -1 && (isValidX(from.getX() - 1)
						&& STATE_BARRIER == astarData[from.getY()][from.getX() - 1] || isValidY(from
						.getY() + 1)
						&& STATE_BARRIER == astarData[from.getY() + 1][from
								.getX()]))
						|| (offsetX == 1 && offsetY == 1 && (isValidY(from
								.getY() - 1)
								&& STATE_BARRIER == astarData[from.getY() - 1][from
										.getX()] || isValidX(from.getX() - 1)
								&& STATE_BARRIER == astarData[from.getY()][from
										.getX() - 1]))
						|| (offsetX == -1 && offsetY == 1 && (isValidX(from
								.getX() + 1)
								&& STATE_BARRIER == astarData[from.getY()][from
										.getX() + 1] || isValidY(from.getY() - 1)
								&& STATE_BARRIER == astarData[from.getY() - 1][from
										.getX()]))
						|| (offsetX == -1 && offsetY == -1 && (isValidX(from
								.getX() + 1)
								&& STATE_BARRIER == astarData[from.getY()][from
										.getX() + 1] || isValidY(from.getY() + 1)
								&& STATE_BARRIER == astarData[from.getY() + 1][from
										.getX()])))
					return false;//这里本来是TRUE,默认斜线障碍物不能越过。
			}
		}
		return false;
	}

	private boolean isValidX(int x) {
		return x >= 0 && x < astarData[0].length;
	}

	private boolean isValidY(int y) {
		return y >= 0 && y < astarData.length;
	}

	private boolean isBarrier(AStarNode node) {
		return astarData[node.getY()][node.getX()] == STATE_BARRIER;
	}

	private List<AStarNode> buildPath(AStarNode current) {
		if (current != null) {
			do {
				path.add(0, current);
				current = current.getFather();
			} while (current != null);
		}
		return path;
	}

	/**
	 * 指定结点是否在开启列表中
	 * 
	 * @param node
	 * @return
	 */
	private boolean isInOpenList(AStarNode node) {
		return openList.contains(node);
	}

	public List<AStarNode> getOpenList() {
		return openList;
	}

	/**
	 * 指定结点是否在关闭列表中
	 * 
	 * @param node
	 * @return
	 */
	private boolean isInCloseMap(AStarNode node) {
		// return closeSet.contains(node);
		return closeMap.containsKey(node.toString());
	}

	private AStarNode[] getNeighbor(AStarNode current) {
		AStarNode[] neighbors = new AStarNode[9];
		for (int i = 0; i < neighbors.length; i++) {
			int x = current.getX() + i / 3 - 1;
			int y = current.getY() + i % 3 - 1;
			if (x < 0 || y < 0 || x >= astarData[0].length
					|| y >= astarData.length
					|| (x == current.getX() && y == current.getY()))
				continue;
			neighbors[i] = new AStarNode(x, y);
		}
		return neighbors;
	}

	/**
	 * 获取开启列表中F值最小的
	 * 
	 * @return
	 */
	private AStarNode getMinFNodeFromOpenList() {
		int index = 0;
		int minF = openList.get(index).getF();
		int length = openList.size();
		for (int i = 1; i < length; i++) {
			AStarNode aStarNode = openList.get(i);
			if (minF > aStarNode.getF()) {
				minF = aStarNode.getF();
				index = i;
			}
		}
		return openList.remove(index);
	}

	/**
	 * 比较指定结点是否是目标结点
	 * 
	 * @param current
	 * @return
	 */
	private boolean isAchieve(AStarNode current) {
		return current.equals(target);
	}

	/**
	 * 初始化数据 开启列表和关闭列表 将源结点加入到开启列表中
	 */
	private void init() {
		initParameter();
		initOpenListAndCloseMap();
		addSource2OpenList();
	}

	private void initParameter() {
		isFind = false;
		source.init(target);
		path.removeAll(path);
	}

	private void initOpenListAndCloseMap() {
		clearOpenList();
		closeMap.clear();
		// closeSet.clear();
	}

	public void clearOpenList() {
		openList.removeAll(openList);
	}

	private void addSource2OpenList() {
		add2OpenList(source);
	}

	private void add2OpenList(AStarNode node) {
		openList.add(node);
	}

	private void add2CloseMap(AStarNode node) {
		// closeSet.add(node);
		closeMap.put(node.toString(), node);
	}

	/**
	 * 是否找到路径
	 * 
	 * @return
	 */
	private boolean isFind() {
		return isFind;
	}

	/**
	 * 是否搜完整个地图
	 * 
	 * @return
	 */
	private boolean isEnd() {
		return openList.size() == 0;
	}

	public void loadData(int[][] data, int barrierVal, int clearVal) {
		if (data == null)
			return;
		if (data.length != astarData.length
				|| astarData[0].length != data[0].length)
			throw new RuntimeException(
					"new data should be as large as the old astar map data");
//		String str = "\n";
		for (int i = 0; i < astarData.length; i++) {
			for (int j = 0; j < astarData[i].length; j++) {
				if (data[i][j] == barrierVal)
					astarData[i][j] = STATE_BARRIER;
				else if (data[i][j] == clearVal)
					astarData[i][j] = 0;
//				str+="["+i+","+j+"]("+astarData[i][j]+")";
//				if(i==8&&j==8){
//					if(astarData[i][j]==0){
//						str+="@";
//					}else {
//						str+= "&";
//					}
//				}else {
//					str+=""+astarData[i][j];
//				}
			}
//			str+="\n";
		}
//		Gdx.app.log("astar",str);
	}

	public void setTarget(AStarNode target) {
		this.target = target;
	}

	public void setSource(AStarNode source) {
		this.source = source;
	}

	public void initTargetAndSource(int x, int y) {
		this.source.setX(this.target.getX());
		this.source.setY(this.target.getY());
		this.target.setX(x);
		this.target.setY(y);
	}
}