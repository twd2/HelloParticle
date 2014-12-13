package org.twd2.game.HelloParticle;

import org.twd2.game.HelloParticle.Field.Electric;
import org.twd2.game.HelloParticle.Field.Magnetic;
import org.twd2.game.HelloParticle.Math.Line;
import org.twd2.game.HelloParticle.Math.Vector2D;
import org.twd2.game.HelloParticle.Physics.Particle;
import org.twd2.game.HelloParticle.Physics.World;
import org.twd2.game.HelloParticle.Shape.Circle;
import org.twd2.game.HelloParticle.Shape.Rectangle;

public class Examples {

	/**
	 * 一群粒子过一个圆形磁场, 其中, 粒子在磁场中运动半径等于磁场半径
	 * @param world
	 */
	public static void ex1(World world) {
		world.enableGravity=false;
		world.enableCoulombForce=false;

		Magnetic bc=new Magnetic();
		bc.Region=new Circle(0,0,3);
		bc.Direction=false;
		bc.value=1e16d;
		world.addField(bc);
		
		int n=4;
		
		for(int i=0;i<=10*n;++i) {
			Particle p1=new Particle(3d,new Vector2D(-10, -5+(double)i/n));
			p1.velocity=new Vector2D(10d,0d);
			p1.q=1e-15;
			world.addParticle(p1);
		}

	}
	
	/**
	 * 碰撞测试
	 * @param world
	 */
	public static void ex2(World world) {
		world.addField(world.defaultGravity);
		
		Magnetic b=new Magnetic();
		b.Region=new Rectangle(-5,-5,5,5);
		b.Direction=false;
		b.value=1e20d;
		world.addField(b);
		
		Electric e=new Electric();
		e.Region=new Rectangle(-9,-9,9,9);
		e.value=new Vector2D(0f,1e18f);
		world.addField(e);
		
		Line ln=new Line(-100,1,100,1);
		ln.k=1;
		world.addBoundary(ln);
		
		Rectangle r1=new Rectangle(-10,-10,10,10);
		world.addBoundary(r1);
		
		Rectangle r2=new Rectangle(-20,-20,20,20);
		world.addBoundary(r2);
		
		Line l1=new Line(-10,-10,10,10);
		l1.k=0.5d;
		world.addBoundary(l1);
		
		Line l2=new Line(-10,10,10,-10);
		l2.k=0.5d;
		world.addBoundary(l2);
		
		//i=4可以修改为i=0
		for(int i=4;i<=4;++i) {
			Particle p1=new Particle(1d,new Vector2D(-10+(double)i,5));
			p1.velocity=new Vector2D(1d,0d);
			p1.q=Math.random()*1e-16;
			world.addParticle(p1);
		}
	}
	
	/**
	 * 质谱仪
	 * @param world
	 */
	public static void ex3(World world) {
		world.enableGravity=false;
		world.enableCoulombForce=false;

		Electric e=new Electric();
		e.Region=new Rectangle(-100,10,100,0.5);
		e.value=new Vector2D(0,-1e16d);
		//world.addField(e);
		
		Magnetic bc=new Magnetic();
		bc.Region=new Rectangle(-100,-0.5,100,-50);
		bc.Direction=false;
		bc.value=1e16d;
		world.addField(bc);
		
		Line l1=new Line(0.01,0,101,0);
		l1.k=0d;
		world.addBoundary(l1);
		
		Line l2=new Line(-0.01,0,-101,0);
		l2.k=0d;
		world.addBoundary(l2);
		
		for(int i=0;i<=10;++i) {
			Particle p1=new Particle(10*Math.random(),new Vector2D(0,10));
			p1.velocity=new Vector2D(0d,-5d);
			p1.q=(1-2*Math.random())*1e-15;
			world.addParticle(p1);
		}

	}
	
	/**
	 * 速度选择器
	 * @param world
	 */
	public static void ex4(World world) {
		world.enableGravity=false;
		world.enableCoulombForce=false;

		Electric e=new Electric();
		e.Region=new Rectangle(-10,-10,100,10);
		e.value=new Vector2D(0,-50d);
		world.addField(e);
		
		Magnetic bc=new Magnetic();
		bc.Region=new Rectangle(-10,-9.5,100,9.5);
		bc.Direction=false;
		bc.value=5d;
		world.addField(bc);
		
		Line l1=new Line(100.5,0.01,100.5,10);
		l1.k=0d;
		world.addBoundary(l1);
		
		Line l2=new Line(100.5,-0.01,100.5,-10);
		l2.k=0d;
		world.addBoundary(l2);
		
		Particle p1=new Particle(1,new Vector2D(0,0));
		p1.velocity=new Vector2D(10d,0d);
		p1.q=1d;
		world.addParticle(p1);
		
		Particle p2=new Particle(1,new Vector2D(0,5));
		p2.velocity=new Vector2D(5d,0d);
		p2.q=1d;
		world.addParticle(p2);
		
		Particle p3=new Particle(1,new Vector2D(0,-5));
		p3.velocity=new Vector2D(15d,0d);
		p3.q=1d;
		world.addParticle(p3);
	}
	
}
