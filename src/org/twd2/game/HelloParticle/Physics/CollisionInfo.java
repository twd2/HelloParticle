package org.twd2.game.HelloParticle.Physics;

import org.twd2.game.HelloParticle.Math.Line;

/**
 * 用来存储一组碰撞的信息
 * @author twd2
 *
 */
public class CollisionInfo {

	public double time=0;
	public Particle p=null;
	public Line boundary=null;
	public IntersectionResult ir=null;
	
	public CollisionInfo(double time, Particle p, Line boundary, IntersectionResult ir) {
		this.time=time;
		this.p=p;
		this.boundary=boundary;
		this.ir=ir;
	}
	
}
