package org.twd2.game.HelloParticle;

public class Rect {

	public double x0,y0,x1,y1;
	
	public Rect(Vector2D v0, Vector2D v1) {
		x0=Math.min(v0.x, v1.x);
		y0=Math.min(v0.y, v1.y);
		x1=Math.max(v0.x, v1.x);
		y1=Math.max(v0.y, v1.y);
	}
	
	public Rect(double x0, double y0, double x1, double y1) {
		this.x0=Math.min(x0, x1);
		this.y0=Math.min(y0, y1);
		this.x1=Math.max(x0, x1);
		this.y1=Math.max(y0, y1);
	}
	
	public Vector2D v0() {
		return new Vector2D(x0, y0);
	}
	
	public Vector2D v1() {
		return new Vector2D(x1, y1);
	}
	
	public double width() {
		return x1-x0;
	}
	
	public double height() {
		return y1-y0;
	}
	
	public boolean isIn(Vector2D v) {
		return v.x >= x0 && v.x <= x1 &&
			    v.y >= y0 && v.y <= y1;
	}
	
	public boolean isInNoBoundary(Vector2D v) {
		return v.x > x0 && v.x < x1 &&
				v.y > y0 && v.y < y1;
	}

	public boolean isCoincide(Rect b) {
		return this.x0<=b.x1 && this.x1>=b.x0 &&
				this.y0<=b.y1 && this.y1>=b.y0;
	}
	
}
