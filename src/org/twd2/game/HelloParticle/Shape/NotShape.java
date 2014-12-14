package org.twd2.game.HelloParticle.Shape;

import org.twd2.game.HelloParticle.Math.Vector2D;

public class NotShape extends Shape {
	
	public Shape org;
	
	public NotShape(Shape org) {
		this.org=org;
	}

	@Override
	public boolean isIn(Vector2D v) {
		return !org.isIn(v);
	}

	@Override
	public boolean isInNoBoundary(Vector2D v) {
		return !org.isInNoBoundary(v);
	}

	@Override
	public boolean isCoincide(Shape b) {
		return !org.isCoincide(b);
	}

}