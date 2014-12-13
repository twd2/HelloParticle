package org.twd2.game.HelloParticle.Shape;

import org.twd2.game.HelloParticle.Math.Vector2D;

public abstract class Shape {

	public abstract boolean isIn(Vector2D v);
	
	public abstract boolean isInNoBoundary(Vector2D v); 

	public abstract boolean isCoincide(Shape b); 
	
}
