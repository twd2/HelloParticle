package org.twd2.game.HelloParticle;

public class Particle {
	
	public Vector2D acceleration=Vector2D.zero.clone(),
			         velocity=Vector2D.zero.clone(),
			         position=Vector2D.zero.clone(),
			         force=Vector2D.zero.clone();
	public double m, q;
	public boolean enable=true, flag=false, enableColl=true;
	public Vector2D lastColl=null;
	
	public Particle(double m){
		this.m=m;
	}

	public Particle(double m, Vector2D position){
		this.m=m;
		this.position=position;
	}
	
	public void move(double dt) {
		position=position.add(velocity.mul(dt).add(acceleration.mul(0.5d*dt*dt))); //delta=v0*t+1/2*a*t*t
	}
	
	public Vector2D newPosition(double dt) {
		return position.add(velocity.mul(dt).add(acceleration.mul(0.5d*dt*dt))); //delta=v0*t+1/2*a*t*t
	}
	
	public Vector2D newPositionEstimate(double dt) {
		return position.add(velocity.mul(dt)); //delta=v0*t
	}
}
