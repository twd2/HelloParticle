package org.twd2.game.HelloParticle.Shape;

import org.twd2.game.HelloParticle.Math.Vector2D;

public class Circle extends Shape {

	public Vector2D centre;
	public double radius, radius2;
	
	public Circle(Vector2D centre, double radius) {
		this.centre=centre;
		this.radius=radius;
		this.radius2=radius*radius;
	}
	
	public Circle(double cx, double cy, double radius) {
		this.centre=new Vector2D(cx, cy);
		this.radius=radius;
		this.radius2=radius*radius;
	}
	
	@Override
	public boolean isIn(Vector2D v) {
		return v.add(centre.mul(-1)).length2() <= radius2;
	}

	@Override
	public boolean isInNoBoundary(Vector2D v) {
		return v.add(centre.mul(-1)).length2() < radius2;
	}

	public boolean isCoincide(Circle c) {
		return centre.add(c.centre.mul(-1)).length2() <= radius2+c.radius2;
	}

	@Override
	public boolean isCoincide(Shape b) {
		return false;
	}

}
