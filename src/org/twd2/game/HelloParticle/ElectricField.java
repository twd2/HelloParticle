package org.twd2.game.HelloParticle;

public class ElectricField {

	public Vector2D E=Vector2D.zero;
	public Rect Region;
	
	public Vector2D Force(Particle p) {
		if (p.q==0.0) {
			return Vector2D.zero;
		}
		
		return E.mul(p.q);
	}
	
}
