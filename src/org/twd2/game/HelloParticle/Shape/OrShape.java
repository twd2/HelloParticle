package org.twd2.game.HelloParticle.Shape;

import org.twd2.game.HelloParticle.Math.Vector2D;

public class OrShape extends Shape {
	
	public Shape org1, org2;
	
	public OrShape(Shape org1, Shape org2) {
		this.org1=org1;
		this.org2=org2;
	}

	@Override
	public boolean isIn(Vector2D v) {
		return org1.isIn(v) || org2.isIn(v);
	}

	@Override
	public boolean isInNoBoundary(Vector2D v) {
		return org1.isInNoBoundary(v) || org2.isInNoBoundary(v);
	}

	@Override
	public boolean isCoincide(Shape b) {
		return org1.isCoincide(b) || org2.isCoincide(b);
	}

}
