package org.twd2.game.HelloParticle.Field;

import org.twd2.game.HelloParticle.Math.Vector2D;
import org.twd2.game.HelloParticle.Physics.Particle;

public class Electric extends Field {

	public Vector2D value=Vector2D.zero;
	
	//public Shape Region;
	
	@Override
	public Vector2D Force(Particle p) {
		if (p.q==0.0) {
			return Vector2D.zero;
		}
		
		return value.mul(p.q);
	}
	
}
