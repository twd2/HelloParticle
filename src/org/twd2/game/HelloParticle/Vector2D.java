package org.twd2.game.HelloParticle;

public class Vector2D {
	
	public static final Vector2D zero = new Vector2D(0, 0);
	public double x,y;	
	
	/**
	 * 构造零向量
	 */
	public Vector2D() {
		this.x=0;
		this.y=0;
	}
	
	/**
	 * 使用坐标构造向量 
	 * @param x 横坐标
	 * @param y 纵坐标
	 */
	public Vector2D(double x, double y) {
		this.x=x;
		this.y=y;
	}

	/**
	 * 根据角度和长度创建向量 
	 * @param length 长度
	 * @param rad 角度
	 * @return 新向量
	 */
	public static Vector2D makeNew(double length,double rad) {
		return new Vector2D(length*Math.cos(rad),length*Math.sin(rad));
	}
	
	/**
	 * 返回字面值
	 */
	public String toString()
	{
		return "(" + String.valueOf(x) + ", " + String.valueOf(y) + ")";
	
	}
	
	/**
	 * 向量加法
	 * @param b 另外一个向量 
	 * @return 和
	 */
	public Vector2D add(Vector2D b){
		Vector2D v=new Vector2D(x,y);
		v.x+=b.x;
		v.y+=b.y;
		return v;
	}
	
	/**
	 * 数乘向量
	 * @param n 数
	 * @return 新向量
	 */
	public Vector2D mul(double n){
		Vector2D v=new Vector2D(x,y);
		v.x*=n;
		v.y*=n;
		return v;
	}
	
	/**
	 * 计算点积
	 * @param b 另外一个向量
	 * @return 点积
	 */
	public double DotP(Vector2D b){
		return this.x*b.x+this.y*b.y;
	}
	
	/**
	 * 计算叉积 
	 * @param b 另外一个向量
	 * @return 叉积 
	 */
	public double CrossP(Vector2D b){
		return this.x*b.y-b.x*this.y;
	}
	
	/**
	 * 获得向量的角度
	 * @return 向量的角度
	 */
	public double rad(){
		return Math.atan2(y,x);
	}
	
	/**
	 * 获得向量模的平方
	 * @return 向量模的平方
	 */
	public double length2(){
		return x*x+y*y;
	}
	
	/**
	 * 获得向量的模
	 * @return 向量的模
	 */
	public double length(){
		return Math.sqrt(x*x+y*y);
	}
	
	/**
	 * 获得单位向量
	 * @return 单位向量
	 */
	public Vector2D unit(){
		return this.mul(1f/length());
	}

	/**
	 * 逆时针旋转90°
	 * @return 向量
	 */
	public Vector2D rotatePositive90() {
		return new Vector2D(-y, x);
	}
	
	/**
	 * 顺时针旋转90°
	 * @return 向量
	 */
	public Vector2D rotateNegative90() {
		return new Vector2D(y, -x);
	}
	
	public Vector2D clone() {
		return new Vector2D(x,y);
	}
	
}
