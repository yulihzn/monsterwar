package com.mw.map;

 
public class AStarNode  implements Comparable<AStarNode>{

	/**
	 * 		G8	G1	G2
	 * 		G7		G3
	 * 		G6	G5	G4
	 */
	//边角的G值也是一样
	private final static int G_1	= 10;
	private final static int G_2	= 10;
	private final static int G_3	= 10;
	private final static int G_4	= 10;
	private final static int G_5	= 10;
	private final static int G_6	= 10;
	private final static int G_7	= 10;
	private final static int G_8	= 10;
	private final static int G_0 	= 10;
	
	private int g;	//		从起点source移动到当前点的耗费
	private int h;	//		从当前点到终点的估值耗费
	private int f;		//		f = g + h
	
	private int x;
	private int y;
	
	private AStarNode father;	//		父结点

	public AStarNode(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public AStarNode getFather() {
		return father;
	}

	public void setFather(AStarNode father) {
		this.father = father;
	}

	public void init(AStarNode target) {
		this.g = 0;
		this.h = heuristicCostEstimate(this, target);
		this.f = g+h;
	}

	/**
	 * 计算H
	 * @param source
	 * @param target
	 * @return
	 */
	public int heuristicCostEstimate(AStarNode source, AStarNode target) {
//		return (int) Math.sqrt((source.x - target.x)*(source.x - target.x)+(source.y - target.y)*(source.y - target.y))*G_0;
		return (Math.abs(source.x - target.x) + Math.abs(source.y - target.y))*G_0;
	}

	@Override
	public int compareTo(AStarNode o) {
		return this.f < o.f ? -1 : 1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null || !(obj instanceof AStarNode) ) return false;
		AStarNode node = (AStarNode)obj;
		return node.x == this.x && node.y == this.y;
	}
	
//	
//	@Override
//	public int hashCode() {
//		return toString().hashCode();
//	}

	@Override
	public String toString(){
		return x+", "+y;
	}

	public void reCalculatorGAndH(AStarNode father, AStarNode target) {
		this.g = distinctG(father);
		this.h = heuristicCostEstimate(this, target);
		this.f = g+h;
		this.father = father;
	}

	public int distinctG(AStarNode father) {
		int offsetX = x-father.x;
		int offsety = y-father.y;
		int distinct = 0;
		if(offsetX==0 && offsety==-1)
			distinct = G_1;
		else if(offsetX==1 && offsety==-1)
			distinct = G_2;
		else if(offsetX==1 && offsety==0)
			distinct = G_3;
		else if(offsetX==1 && offsety==1)
			distinct = G_4;
		else if(offsetX==0 && offsety==1)
			distinct = G_5;
		else if(offsetX==-1 && offsety==1)
			distinct = G_6;
		else if(offsetX==-1 && offsety==0)
			distinct = G_7;
		else if(offsetX==-1 && offsety==-1)
			distinct = G_8;
		else
			try {
				throw new Exception("Unvalid relation between current node("+this+") and fater node("+father+")");
			} catch (Exception e) {
 				e.printStackTrace();
			}
		return distinct+father.g;
	}
	
	public int getG(){
		return g;
	}

	/**
	 * 是否比指定的点更好
	 * @param node
	 * @return
	 */
	public boolean isBetter(AStarNode node) {
		return isGBetter(node);
	}
	
	public boolean isGBetter(AStarNode node){
		return g+distinctG(node) < node.g;
	}

	public boolean isFBetter(AStarNode node) {
		return f<node.f;
	}

	public int getF() {
		return f;
	}
}