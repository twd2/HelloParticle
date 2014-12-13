package org.twd2.game.HelloParticle.Field;

import org.twd2.game.HelloParticle.Math.Vector2D;
import org.twd2.game.HelloParticle.Physics.Particle;
import org.twd2.game.HelloParticle.Shape.Shape;
import org.twd2.game.HelloParticle.Shape.Unlimited;

public abstract class Field {

	public Shape Region=Unlimited.i;
	
	public abstract Vector2D Force(Particle p);
	
}
