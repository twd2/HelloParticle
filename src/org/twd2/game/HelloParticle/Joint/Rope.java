package org.twd2.game.HelloParticle.Joint;

import org.twd2.game.HelloParticle.Math.Vector2D;
import org.twd2.game.HelloParticle.Physics.Particle;

/**
 * 绳子
 * @author twd2
 *
 */
public class Rope extends Joint {

	public final double k0=1e5;
	public double k=k0;
	public double length=0;
	
	/**
	 * 断掉的力, 小于零则绳子不会断掉
	 */
	public double maxForce=-1d;
	
	public Rope(Particle p1, Particle p2) {
		this.p1=p1;
		this.p2=p2;
		k=Math.sqrt(p1.m*p2.m)*k0;
		this.length=p1.position.add(p2.position.mul(-1)).length();
	}
	
	public Rope(Particle p1, Particle p2, double length) {
		this.p1=p1;
		this.p2=p2;
		k=Math.sqrt(p1.m*p2.m)*k0;
		this.length=length;
	}
	
	public Rope(Particle p1, Particle p2, double length, double maxForce) {
		this.p1=p1;
		this.p2=p2;
		k=Math.sqrt(p1.m*p2.m)*k0;
		this.length=length;
		this.maxForce=maxForce;
	}
	
	@Override
	public void Force() {
		if (!enable) return;
		
		Vector2D newlengthVec=p1.position.add(p2.position.mul(-1)); //p1-p2位置, 指向p1
		double newlength=newlengthVec.length();
		Vector2D unitVec=newlengthVec.mul(1/newlength);
		double deltaLength=newlength-length;
		if (deltaLength<=0d) return;
		double force=k*deltaLength;
		Vector2D force2=unitVec.mul(force); //给p2的力指向p1
		
		//可以断掉的绳子
		if (maxForce>0 && Math.abs(force)>maxForce) {
			System.out.println("断掉了");
			enable=false;
			return;
		}
		
		p2.force=p2.force.add(force2);
		p1.force=p1.force.add(force2.mul(-1)); //指向p2

	}
}
