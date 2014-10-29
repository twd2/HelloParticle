package org.twd2.game.HelloParticle;

public class Equation2Val {

	public double A1,B1,C1,A2,B2,C2;
	
	public Equation2Val(double A1, double B1, double C1, double A2, double B2, double C2) {
		this.A1=A1;
		this.B1=B1;
		this.C1=C1;
		this.A2=A2;
		this.B2=B2;
		this.C2=C2;
	}
	
	public Solution getSolution() {
		Solution result=new Solution();
		//k1y+b1=0, k2x+b2=0
		double k1=B1*A2-A1*B2;//B1A2-A1B2
		double b1=C1*A2-A1*C2; //C1A2-A1C2
		double k2=-k1; //A1B2-A2B1
		double b2=C1*B2-B1*C2; //C1B2-B1C2
		if (Math.abs(k1)<1e-8) { //平行
			if (Math.abs(b1)<1e-8||Math.abs(b2)<1e-8) { //直线重合
				result.type=1;
			} else { //平行无交点
				result.type=-1;
			}
		} else {
			//y=-b1/k1, x=-b2/k2
			result.Solution=new Vector2D(-b2/k2, -b1/k1);
		}
		return result;
	}
	
}
