package org.twd2.game.HelloParticle.Field;

import org.twd2.game.HelloParticle.Math.Vector2D;
import org.twd2.game.HelloParticle.Physics.Particle;

/**
 * 惊悚区域
 * @author twd2
 *
 */
public class Block extends Field {

	public double value=1e6;
	
	@Override
	public Vector2D Force(Particle p) {
		Vector2D F0=p.velocity.rotateNegative90().unit(); //力的单位向量
		Vector2D f=F0.mul(p.m*value);
		return f;
	}
	
}
