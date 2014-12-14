package org.twd2.game.HelloParticle.Field;

import org.twd2.game.HelloParticle.Math.Vector2D;
import org.twd2.game.HelloParticle.Physics.Particle;

public class ElectricResistance extends Field {

	public double u;
	
	public ElectricResistance(double u) {
		this.u=u;
	}
	
	@Override
	public Vector2D Force(Particle p) {
		return p.velocity.mul(-u*p.q);
	}

}
