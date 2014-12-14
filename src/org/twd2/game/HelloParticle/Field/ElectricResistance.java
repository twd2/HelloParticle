package org.twd2.game.HelloParticle.Field;

import org.twd2.game.HelloParticle.Math.Vector2D;
import org.twd2.game.HelloParticle.Physics.Particle;

/**
 * 电荷阻尼场
 * @author twd2
 *
 */
public class ElectricResistance extends Field {

	public double u;
	
	public ElectricResistance(double u) {
		this.u=u;
	}
	
	@Override
	public Vector2D Force(Particle p) {
		return p.velocity.mul(-u*Math.abs(p.q));
	}

}
