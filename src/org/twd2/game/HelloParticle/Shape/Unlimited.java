package org.twd2.game.HelloParticle.Shape;

import org.twd2.game.HelloParticle.Math.Vector2D;

public class Unlimited extends Shape {

	public static final Unlimited i=new Unlimited();
	
	@Override
	public boolean isIn(Vector2D v) {
		return true;
	}

	@Override
	public boolean isInNoBoundary(Vector2D v) {
		return true;
	}

	@Override
	public boolean isCoincide(Shape s) {
		return true;
	}

}
