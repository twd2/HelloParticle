package org.twd2.game.HelloParticle;

import org.twd2.game.HelloParticle.Field.Block;
import org.twd2.game.HelloParticle.Field.Electric;
import org.twd2.game.HelloParticle.Field.ElectricResistance;
import org.twd2.game.HelloParticle.Field.Magnetic;
import org.twd2.game.HelloParticle.Field.Resistance;
import org.twd2.game.HelloParticle.Joint.Rope;
import org.twd2.game.HelloParticle.Joint.Spring;
import org.twd2.game.HelloParticle.Math.Line;
import org.twd2.game.HelloParticle.Math.Vector2D;
import org.twd2.game.HelloParticle.Physics.Particle;
import org.twd2.game.HelloParticle.Physics.World;
import org.twd2.game.HelloParticle.Shape.Circle;
import org.twd2.game.HelloParticle.Shape.NotShape;
import org.twd2.game.HelloParticle.Shape.Rectangle;

public class Examples {

	/**
	 * 在有阻尼的磁场中运动
	 * @param world
	 */
	public static void ex0(World world) {
		//world.enableGravity=false;
		world.enableCoulombForce=false;
		
		world.addField(new Resistance(0.1));

		Magnetic bc=new Magnetic(1d, false);
		bc.Region=new Circle(0,0,3);
		world.addField(bc);

		Particle p1=new Particle(1d,new Vector2D(0,-2.9));
		p1.velocity=new Vector2D(29d,0d);
		p1.q=10;
		world.addParticle(p1);

	}
	
	/**
	 * 一群粒子过一个圆形磁场, 其中, 粒子在磁场中运动半径等于磁场半径
	 * @param world
	 */
	public static void ex1(World world) {
		world.enableGravity=false;
		world.enableCoulombForce=false;

		Magnetic bc=new Magnetic(1e16d, false);
		bc.Region=new Circle(0,0,3);
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
		
		Magnetic b=new Magnetic(1e20d, false);
		b.Region=new Rectangle(-5,-5,5,5);
		world.addField(b);
		
		Electric e=new Electric(0f,1e18f);
		e.Region=new Rectangle(-9,-9,9,9);
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

		Electric e=new Electric(0,-1e16d);
		e.Region=new Rectangle(-100,10,100,0.5);
		//world.addField(e);
		
		Magnetic bc=new Magnetic(1e16d, false);
		bc.Region=new Rectangle(-100,-0.5,100,-50);
		world.addField(bc);
		
		Line l1=new Line(0.01,0,101,0);
		l1.k=0d;
		world.addBoundary(l1);
		
		Line l2=new Line(-0.01,0,-101,0);
		l2.k=0d;
		world.addBoundary(l2);
		
		Line l3=new Line(-101,-51,101,-51);
		l3.k=0d;
		world.addBoundary(l3);
		
		Particle p0=new Particle(10*Math.random(),new Vector2D(0,10));
		p0.velocity=new Vector2D(0d,-5d);
		p0.q=0;
		world.addParticle(p0);
		
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

		Electric e=new Electric(0,-50d);
		e.Region=new Rectangle(-10,-10,100,10);
		world.addField(e);
		
		Magnetic bc=new Magnetic(5d, false);
		bc.Region=new Rectangle(-10,-9.5,100,9.5);
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
		
		Particle p4=new Particle(1,new Vector2D(0,0));
		p4.velocity=new Vector2D(9d,0d);
		world.addParticle(p4);
		
		Particle p5=new Particle(1,new Vector2D(0,1));
		p5.velocity=new Vector2D(10d,0d);
		p5.q=-1d;
		world.addParticle(p5);
		
		Particle p6=new Particle(1,new Vector2D(0,6));
		p4.velocity=new Vector2D(5d,0d);
		p4.q=-1d;
		world.addParticle(p4);
		
		Particle p7=new Particle(1,new Vector2D(0,-4));
		p7.velocity=new Vector2D(15d,0d);
		p7.q=-1d;
		world.addParticle(p7);
	}
	
	/**
	 * 连接体
	 * @param world
	 */
	public static void ex5(World world) {
		world.addField(world.defaultGravity);
		world.addField(new Resistance(1));
		
		world.enableGravity=false;
		world.enableCoulombForce=false;
		
		Block b=new Block();
		b.Region=new Circle(0,0,3);
		world.addField(b);
		
		Particle p0=new Particle(1,new Vector2D(0,10));
		world.addParticle(p0);
		
		Particle p1=new Particle(1,new Vector2D(1,5));
		p1.velocity=new Vector2D(1d,0d);
		p1.q=1d;
		p1.fixed=true;
		world.addParticle(p1);
		
		Particle p2=new Particle(10,new Vector2D(4,0));
		p2.velocity=new Vector2D(20d,0d);
		p2.q=1d;
		world.addParticle(p2);
		
		Rope j=new Rope(p1,p2,p1.position.add(p2.position.mul(-1)).length(),1e5);
		world.addJoint(j);
		
		Line ln=new Line(-100,1,100,1);
		ln.k=1;
		world.addBoundary(ln);
		
		Line ln1=new Line(-100,100,100,100);
		ln1.k=1;
		world.addBoundary(ln1);
		
		Line ln2=new Line(100,0,100,100);
		ln2.k=1;
		world.addBoundary(ln2);
		
		Line ln3=new Line(-100,0,-100,100);
		ln3.k=1;
		world.addBoundary(ln3);
		
		Particle p3=new Particle(1,new Vector2D(0,5));
		p3.velocity=new Vector2D(15d,0d);
		p3.q=1d;
		world.addParticle(p3);
		
		Spring j1=new Spring(p1,p3,1e2,p1.position.add(p3.position.mul(-1)).length());
		world.addJoint(j1);
		
		Spring j2=new Spring(p2,p3,1e2,p2.position.add(p3.position.mul(-1)).length());
		world.addJoint(j2);
	}
	
	/**
	 * 断掉的绳子
	 * @param world
	 */
	public static void ex6(World world) {
		world.addField(world.defaultGravity);
		world.addField(new Electric(5d,9d));
		world.addField(new ElectricResistance(1d));
		
		
		world.enableGravity=false;
		world.enableCoulombForce=false;
		
		Particle p0=new Particle(1,new Vector2D(0,0));
		p0.fixed=true;
		world.addParticle(p0);
		
		Particle p1=new Particle(1,new Vector2D(0,5));
		//p1.q=1d;
		p1.velocity=new Vector2D(10d,0d);
		world.addParticle(p1);
		
		Rope j1=new Rope(p0,p1,p0.position.add(p1.position.mul(-1)).length(),70);
		world.addJoint(j1);
		
		Particle p2=new Particle(1d,new Vector2D(0,-5));
		p2.q=1d;
		p2.velocity=new Vector2D(10d,0d);
		world.addParticle(p2);
		
		Rope j2=new Rope(p0,p2,p0.position.add(p2.position.mul(-1)).length(),50);
		world.addJoint(j2);
		
		Particle p3=new Particle(1d,new Vector2D(0,4));
		p3.q=-1d;
		p3.velocity=new Vector2D(0d,0d);
		world.addParticle(p3);
		
		Rope j3=new Rope(p0,p3,p0.position.add(p3.position.mul(-1)).length(),4500);
		world.addJoint(j3);
		
		Line ln=new Line(-100,-10,100,-10);
		ln.k=1;
		world.addBoundary(ln);
		
		Line ln1=new Line(-100,100,100,100);
		ln1.k=1;
		world.addBoundary(ln1);
		
		Line ln2=new Line(100,-10,100,100);
		ln2.k=1;
		world.addBoundary(ln2);
		
		Line ln3=new Line(-100,-10,-100,100);
		ln3.k=1;
		world.addBoundary(ln3);
	}
	
	/**
	 * NotShape
	 * @param world
	 */
	public static void ex7(World world) {
		world.addField(world.defaultGravity);
		world.addField(new Resistance(1));
		
		//world.enableGravity=false;
		world.enableCoulombForce=false;
		
		Block b=new Block();
		b.Region=new NotShape(new Circle(0,0,3));
		world.addField(b);
		
		Particle p0=new Particle(1,new Vector2D(0,0));
		world.addParticle(p0);
		
	}
	
	/**
	 * 正负电荷
	 * @param world
	 */
	public static void ex8(World world) {
		world.enableGravity=false;
		//world.enableCoulombForce=false;
		
		Line ln1=new Line(-1,-10,-1,10);
		ln1.k=0.99;
		world.addBoundary(ln1);
		
		Line ln2=new Line(1,-10,1,10);
		ln2.k=0.99;
		world.addBoundary(ln2);
		
		Particle p1=new Particle(1,new Vector2D(-10,0));
		p1.velocity=new Vector2D(0d,0d);
		p1.q=2*1e-4d;
		world.addParticle(p1);
		
		Particle p2=new Particle(1,new Vector2D(10,0));
		p2.velocity=new Vector2D(0d,0d);
		p2.q=-2*1e-4d;
		world.addParticle(p2);
	}
	
	/**
	 * 弹簧
	 * @param world
	 */
	public static void ex9(World world) {
		world.enableGravity=false;
		world.enableCoulombForce=false;
		
		Line ln1=new Line(-1,-10,-1,10);
		ln1.k=0.9;
		world.addBoundary(ln1);
		
		Line ln2=new Line(1,-10,1,10);
		ln2.k=1;
		world.addBoundary(ln2);
		
		Line ln3=new Line(-100,-10,100,-10);
		ln3.k=1;
		world.addBoundary(ln3);
		
		Line ln4=new Line(-100,10,100,10);
		ln4.k=1;
		world.addBoundary(ln4);
		
		Particle p1=new Particle(1,new Vector2D(-10,0));
		p1.velocity=new Vector2D(0d,1d);
		world.addParticle(p1);
		
		Particle p2=new Particle(1,new Vector2D(10,0));
		p2.velocity=new Vector2D(0d,1d);
		world.addParticle(p2);
		
		Spring s=new Spring(p1,p2,1e4,10);
		world.addJoint(s);
	}
	
}
