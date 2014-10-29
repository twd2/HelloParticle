package org.twd2.game.HelloParticle;

public class Magnetic {

	boolean Direction=true; //true: 垂直屏幕向外, false: 垂直屏幕向里
	double B=0; //磁感应强度
	public Rect Region;
	
	public Vector2D Force(Particle p) {
		if (p.velocity.length()==0.0 || p.q==0.0) {
			return Vector2D.zero;
		}
		Vector2D fB;
		if (Direction) { //垂直屏幕向外
			Vector2D F0=p.velocity.rotateNegative90().unit(); //力的单位向量
			fB=F0.mul(p.q*p.velocity.length()*B);
		} else { //垂直屏幕向里
			Vector2D F0=p.velocity.rotatePositive90().unit(); //力的单位向量
			fB=F0.mul(p.q*p.velocity.length()*B);
		}
		return fB;
	}
	
}
