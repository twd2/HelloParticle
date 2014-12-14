package org.twd2.game.HelloParticle.Joint;

import org.twd2.game.HelloParticle.Physics.Particle;

public abstract class Joint {

	public Particle p1,p2;
	public boolean enable=true;
	
	public abstract void Force();
	
}
