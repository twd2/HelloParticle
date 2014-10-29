package org.twd2.game.HelloParticle;

public class Vector2D {
	
	public static final Vector2D zero = new Vector2D(0, 0);
	public double x,y;	
	
	/**
	 * ����������
	 */
	public Vector2D() {
		this.x=0;
		this.y=0;
	}
	
	/**
	 * ʹ�����깹������ 
	 * @param x ������
	 * @param y ������
	 */
	public Vector2D(double x, double y) {
		this.x=x;
		this.y=y;
	}

	/**
	 * ���ݽǶȺͳ��ȴ������� 
	 * @param length ����
	 * @param rad �Ƕ�
	 * @return ������
	 */
	public static Vector2D makeNew(double length,double rad) {
		return new Vector2D(length*Math.cos(rad),length*Math.sin(rad));
	}
	
	/**
	 * ��������ֵ
	 */
	public String toString()
	{
		return "(" + String.valueOf(x) + ", " + String.valueOf(y) + ")";
	
	}
	
	/**
	 * �����ӷ�
	 * @param b ����һ������ 
	 * @return ��
	 */
	public Vector2D add(Vector2D b){
		Vector2D v=new Vector2D(x,y);
		v.x+=b.x;
		v.y+=b.y;
		return v;
	}
	
	/**
	 * ��������
	 * @param n ��
	 * @return ������
	 */
	public Vector2D mul(double n){
		Vector2D v=new Vector2D(x,y);
		v.x*=n;
		v.y*=n;
		return v;
	}
	
	/**
	 * ������
	 * @param b ����һ������
	 * @return ���
	 */
	public double DotP(Vector2D b){
		return this.x*b.x+this.y*b.y;
	}
	
	/**
	 * ������ 
	 * @param b ����һ������
	 * @return ��� 
	 */
	public double CrossP(Vector2D b){
		return this.x*b.y-b.x*this.y;
	}
	
	/**
	 * ��������ĽǶ�
	 * @return �����ĽǶ�
	 */
	public double rad(){
		return Math.atan2(y,x);
	}
	
	/**
	 * �������ģ��ƽ��
	 * @return ����ģ��ƽ��
	 */
	public double length2(){
		return x*x+y*y;
	}
	
	/**
	 * ���������ģ
	 * @return ������ģ
	 */
	public double length(){
		return Math.sqrt(x*x+y*y);
	}
	
	/**
	 * ��õ�λ����
	 * @return ��λ����
	 */
	public Vector2D unit(){
		return this.mul(1f/length());
	}

	/**
	 * ��ʱ����ת90��
	 * @return ����
	 */
	public Vector2D rotatePositive90() {
		return new Vector2D(-y, x);
	}
	
	/**
	 * ˳ʱ����ת90��
	 * @return ����
	 */
	public Vector2D rotateNegative90() {
		return new Vector2D(y, -x);
	}
	
	public Vector2D clone() {
		return new Vector2D(x,y);
	}
	
}
