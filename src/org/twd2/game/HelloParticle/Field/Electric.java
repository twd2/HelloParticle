package org.twd2.game.HelloParticle.Field;

import org.twd2.game.HelloParticle.Math.Vector2D;
import org.twd2.game.HelloParticle.Physics.Particle;

/**
 * 电场
 * @author twd2
 *
 */
public class Electric extends Field {

	public Vector2D value=Vector2D.zero;
	
	public Electric(Vector2D value) {
		this.value=value;
	}
	
	public Electric(double x, double y) {
		this.value=new Vector2D(x,y);
	}
	
	@Override
	public Vector2D Force(Particle p) {
		if (p.q==0.0) {
			return Vector2D.zero;
		}
		
		return value.mul(p.q);
	}
	
}
