package org.twd2.game.HelloParticle;

public class Magnetic {

	boolean Direction=true; //true: ��ֱ��Ļ����, false: ��ֱ��Ļ����
	double B=0; //�Ÿ�Ӧǿ��
	public Rect Region;
	
	public Vector2D Force(Particle p) {
		if (p.velocity.length()==0.0 || p.q==0.0) {
			return Vector2D.zero;
		}
		Vector2D fB;
		if (Direction) { //��ֱ��Ļ����
			Vector2D F0=p.velocity.rotateNegative90().unit(); //���ĵ�λ����
			fB=F0.mul(p.q*p.velocity.length()*B);
		} else { //��ֱ��Ļ����
			Vector2D F0=p.velocity.rotatePositive90().unit(); //���ĵ�λ����
			fB=F0.mul(p.q*p.velocity.length()*B);
		}
		return fB;
	}
	
}
