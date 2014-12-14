package org.twd2.game.HelloParticle.Joint;

import org.twd2.game.HelloParticle.Math.Vector2D;
import org.twd2.game.HelloParticle.Physics.Particle;

/**
 * 弹簧
 * @author twd2
 *
 */
public class Spring extends Joint {
	
	public double k=0;
	public double length=0;
	
	public Spring(Particle p1, Particle p2, double k, double length) {
		this.p1=p1;
		this.p2=p2;
		this.k=k;
		this.length=length;
	}

	@Override
	public void Force() {
		if (!enable) return;
		
		Vector2D newlengthVec=p1.position.add(p2.position.mul(-1)); //p1-p2位置, 指向p1
		double newlength=newlengthVec.length();
		Vector2D unitVec=newlengthVec.mul(1/newlength);
		double deltaLength=newlength-length;
		Vector2D force2=unitVec.mul(k*deltaLength); //给p2的力指向p1
		p2.force=p2.force.add(force2);
		p1.force=p1.force.add(force2.mul(-1)); //指向p2
	}
	
}
