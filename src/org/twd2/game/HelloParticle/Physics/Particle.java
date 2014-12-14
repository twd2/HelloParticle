package org.twd2.game.HelloParticle.Physics;

import org.twd2.game.HelloParticle.Math.Vector2D;

public class Particle {
	
	public Vector2D acceleration=Vector2D.zero.clone(),
			         velocity=Vector2D.zero.clone(),
			         position=Vector2D.zero.clone(),
			         force=Vector2D.zero.clone(),
			         userForce=Vector2D.zero.clone();
	public double m, q;
	public boolean enable=true, flag=false, enableColl=true;
	public boolean fixed=false;
	public Vector2D lastColl=null;
	
	public Particle(double m){
		this.m=m;
	}

	public Particle(double m, Vector2D position){
		this.m=m;
		this.position=position;
	}
	
	public void move(double dt) {
		if (fixed) return;
		position=position.add(velocity.mul(dt).add(acceleration.mul(0.5d*dt*dt))); //delta=v0*t+1/2*a*t*t
	}
	
	public Vector2D newPosition(double dt) {
		if (fixed) return position;
		return position.add(velocity.mul(dt).add(acceleration.mul(0.5d*dt*dt))); //delta=v0*t+1/2*a*t*t
	}
	
	public Vector2D newPositionEstimate(double dt) {
		if (fixed) return position;
		return position.add(velocity.mul(dt)); //delta=v0*t
	}
	
	public void addForce(Vector2D f) {
		userForce=userForce.add(f);
	}
}
