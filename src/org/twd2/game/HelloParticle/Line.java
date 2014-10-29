package org.twd2.game.HelloParticle;

public class Line {
	
	Vector2D p0, p1;
	Rect rect;
	
	double A,B,C;
	
	public double k=1.0; //�ָ�ϵ�� 
	
	public Line(Vector2D p0, Vector2D p1) {
		this.p0=p0;
		this.p1=p1;
		rect = new Rect(p0,p1);
		A=p1.y-p0.y;
		B=p0.x-p1.x;
		C=-A*p0.x-B*p0.y;
	}
	
	public Line(double x0, double y0, double x1, double y1) { 
		this.p0=new Vector2D(x0, y0);
		this.p1=new Vector2D(x1, y1);
		rect = new Rect(p0,p1);
		A=p1.y-p0.y;
		B=p0.x-p1.x;
		C=-A*p0.x-B*p0.y;
	}
	
	/**
	 * ��������ֵ
	 */
	public String toString()
	{
		return "(" + String.valueOf(A) + ")x + (" + String.valueOf(B) + ")y + (" + String.valueOf(C) + ") = 0";
	
	}
	
	/**
	 * �󽻵�
	 * @param b ����һ���߶�
	 * @return ������Ϣ
	 */
	public IntersectionResult findIntersection(Line b) {
		IntersectionResult result=new IntersectionResult();
		if (!rect.isCoincide(b.rect)) { //�޽���
			result.type=-1;
			return result;
		}
		Equation2Val equ=new Equation2Val(A,B,C,b.A,b.B,b.C);
		Solution sol=equ.getSolution();
		if (sol.Solution != null) {
			if (!rect.isIn((sol.Solution)) || !b.rect.isIn((sol.Solution))) { //�������߶�����
				sol.type=-1;
			}
		}
		
		result.type=sol.type;
		result.Intersection=sol.Solution;
		
		/*
		//k1y+b1=0, k2x+b2=0
		double k1=B*b.A-A*b.B;//B1A2-A1B2
		double b1=C*b.A-A*b.C; //C1A2-A1C2
		double k2=-k1; //A1B2-A2B1
		double b2=C*b.B-B*b.C; //C1B2-B1C2
		if (Math.abs(k1)<MyMath.zero) { //ƽ��
			if (Math.abs(b1)<MyMath.zero||Math.abs(b2)<MyMath.zero) { //ֱ���غ�
				result.type=1;
			} else { //ƽ���޽���
				result.type=-1;
			}
		} else {
			//y=-b1/k1, x=-b2/k2
			result.Intersection=new Vector2D(-b2/k2, -b1/k1);
			if (!rect.isIn((result.Intersection)) || !b.rect.isIn((result.Intersection))) { //�������߶�����
				result.type=-1;
			}
		}*/
		return result;
	}
	
	/**
	 * ��������
	 * @return ��������
	 */
	public Vector2D getDirection() {
		return new Vector2D(-B,A);
	}
	
	/**
	 * �ԳƵ�
	 * @param p ��
	 * @return �ԳƵ�
	 */
	public Vector2D getSymmetry(Vector2D p) {
		Vector2D dir=getDirection();
		double C1,C2;
		//XcXa+YcYa-YbYa-XbXa=0
		//XcYa-YcXa+YaXb-XaYb=0
		C1=-p.y*dir.y-p.x*dir.x;
		C2=dir.y*p.x-dir.x*p.y;
		Equation2Val equ=new Equation2Val(dir.x,dir.y,C1,dir.y,-dir.x,C2);
		Solution sol=equ.getSolution();
		return sol.Solution;
	}
	
}