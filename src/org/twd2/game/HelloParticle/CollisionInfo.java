package org.twd2.game.HelloParticle;

/**
 * �����洢һ����ײ����Ϣ
 * @author twd2
 *
 */
public class CollisionInfo {

	double time=0;
	Particle p=null;
	Line boundary=null;
	IntersectionResult ir=null;
	
	public CollisionInfo(double time, Particle p, Line boundary, IntersectionResult ir) {
		this.time=time;
		this.p=p;
		this.boundary=boundary;
		this.ir=ir;
	}
	
}
