package org.twd2.game.HelloParticle.Field;

import org.twd2.game.HelloParticle.Math.Vector2D;
import org.twd2.game.HelloParticle.Physics.Particle;

public class Gravity extends Field {

	public Vector2D g;
	
	public Gravity(Vector2D g) {
		this.g=g;
	}
	
	public Gravity(double x, double y) {
		this.g=new Vector2D(x,y);
	}
	
	@Override
	public Vector2D Force(Particle p) {
		return g.mul(p.m);
	}

}
