package org.twd2.game.HelloParticle.Physics;

import java.util.ArrayList;

import org.twd2.game.HelloParticle.Field.Electric;
import org.twd2.game.HelloParticle.Field.Field;
import org.twd2.game.HelloParticle.Field.Gravity;
import org.twd2.game.HelloParticle.Field.Magnetic;
import org.twd2.game.HelloParticle.Math.Line;
import org.twd2.game.HelloParticle.Math.MyMath;
import org.twd2.game.HelloParticle.Math.Vector2D;
import org.twd2.game.HelloParticle.Shape.Rectangle;

public class World {

	
	public boolean enableGravity=true;//true;
	public boolean enableCoulombForce=true;//=true;
	
	public final double G=6.6732e-11d; //万有引力常数
	public final double k=8987000000d; //库伦定律常数
	
	//public ElectricField E=new ElectricField();
	
	//public Vector2D g=new Vector2D(0,-9.80665d);//电场、重力加速度

	//public Magnetic B=new Magnetic();
	//public double u=0.0; //全局阻尼系数
	
	public Gravity defaultGravity=new Gravity(0,-9.80665d); //默认重力场
	
	public ArrayList<Particle> particles=new ArrayList<Particle>();
	public ArrayList<Field> fields=new ArrayList<Field>();
	
	//public ArrayList<Magnetic> aB=new ArrayList<Magnetic>();
	//public ArrayList<ElectricField> aE=new ArrayList<ElectricField>();
	
	public ArrayList<Line> boundaries=new ArrayList<Line>();
	
	public World() {
		
	}
	
	public void addParticle(Particle p) {
		particles.add(p);
	}
	
	/*public void addMagnetic(Magnetic b) {
		aB.add(b);
	}
	
	public void addElectricField(ElectricField e) {
		aE.add(e);
	}*/
	
	public void addField(Field f) {
		fields.add(f);
	}
	
	public void addBoundary(Line b) {
		boundaries.add(b);
	}
	
	public void addBoundary(Rectangle r) {
		/*addBoundary(new Line(r.x0,r.y0-1e-1, r.x0,r.y1+1e-1));
		addBoundary(new Line(r.x0-1e-1,r.y1, r.x1+1e-1,r.y1));
		addBoundary(new Line(r.x1,r.y1+1e-1, r.x1,r.y0-1e-1));
		addBoundary(new Line(r.x1+1e-1,r.y0, r.x0-1e-1,r.y0));*/
		addBoundary(new Line(r.x0,r.y0, r.x0,r.y1));
		addBoundary(new Line(r.x0,r.y1, r.x1,r.y1));
		addBoundary(new Line(r.x1,r.y1, r.x1,r.y0));
		addBoundary(new Line(r.x1,r.y0, r.x0,r.y0));
	}
		
	public void next(double dt, int iter) {
		next(dt, iter, true);
	}
	
	public void next(double dt, int iter, boolean collision) {
		double ddt=dt/iter;
		for(int i=0;i<iter;++i)
			next(ddt, collision);
	}
	
	public void next(double dt) {
		next(dt, true);
	}
	
	public void next(double dt, boolean collision) {
		if (!collision) {
			calcForce(); //受力分析
			calcAcceleration(); //用力算出加速度
			calcPosition(dt); //计算新位置
			calcVelocity(dt); //用加速度算出该时刻末的速度
		} else {
			while(dt>0.0) {
				calcForce(); //受力分析
				calcAcceleration(); //用力算出加速度
				dt-=calcCollision(dt); //碰撞检测
				break;
			}
		}
	}
	
	public void calcForce() {
		for(int i=0;i<particles.size();++i) {
			Particle p=particles.get(i);
			if (!p.enable) continue;
			 calcParticleForce(p);
		}  
	}
	
	public void calcParticleForce(Particle p) {
		p.force=Vector2D.zero;
		
		if (enableGravity || enableCoulombForce) {
			if (enableGravity && enableCoulombForce) {
				//万有引力、库伦力
				for(int i=0;i<particles.size();++i) {
					Particle cp=particles.get(i);
					if (cp==p) continue; //对自己不进行万有引力、库伦力受力分析
					if (!cp.enable) continue;
					Vector2D delta=p.position.add(cp.position.mul(-1)); //指向被分析对象
					double R2=delta.length2();
					double R=Math.sqrt(R2);
					Vector2D F0=delta.mul(1f/R); //力指向被分析对象的单位向量
					 
					Vector2D FG=F0.mul(-G*p.m*cp.m/R2); //万有引力, 负号是为了使得力方向反向从而指向另外一个对象, 吸引力
					Vector2D Fk=F0.mul(k*p.q*cp.q/R2); //库伦力, 同斥异吸
					p.force=p.force.add(FG).add(Fk);
				}
			}
			if (enableGravity && !enableCoulombForce) {
				//仅万有引力
				for(int i=0;i<particles.size();++i) {
					Particle cp=particles.get(i);
					if (cp==p) continue; //对自己不进行万有引力、库伦力受力分析
					if (!cp.enable) continue;
					Vector2D delta=p.position.add(cp.position.mul(-1)); //指向被分析对象
					double R2=delta.length2();
					double R=Math.sqrt(R2);
					Vector2D F0=delta.mul(1f/R); //力指向被分析对象的单位向量
					 
					Vector2D FG=F0.mul(-G*p.m*cp.m/R2); //万有引力, 负号是为了使得力方向反向从而指向另外一个对象, 吸引力
					p.force=p.force.add(FG);
				}
			}
			if (!enableGravity && enableCoulombForce) {
				//仅库伦力
				for(int i=0;i<particles.size();++i) {
					Particle cp=particles.get(i);
					if (cp==p) continue; //对自己不进行万有引力、库伦力受力分析
					if (!cp.enable) continue;
					Vector2D delta=p.position.add(cp.position.mul(-1)); //指向被分析对象
					double R2=delta.length2();
					double R=Math.sqrt(R2);
					Vector2D F0=delta.mul(1f/R); //力指向被分析对象的单位向量

					Vector2D Fk=F0.mul(k*p.q*cp.q/R2); //库伦力, 同斥异吸
					p.force=p.force.add(Fk);
				}
			}
		}
		//TODO
		
		//全部的场
		for(int i=0;i<fields.size();++i) {
			 Field f=fields.get(i);
			 if (f.Region.isIn(p.position)) {			 
				 Vector2D force=f.Force(p);
				 p.force=p.force.add(force);
			 }
		}
		
		/*for(int i=0;i<aE.size();++i) {
			 ElectricField cE=aE.get(i);
			 if (cE.Region.isIn(p.position)) {			 
				 Vector2D cFE=cE.Force(p);
				 p.force=p.force.add(cFE); //电场力
			 }
		}
		for(int i=0;i<aB.size();++i) {
			 Magnetic cB=aB.get(i);
			 if (cB.Region.isIn(p.position)) {			 
				 Vector2D cfB=cB.Force(p);
				 p.force=p.force.add(cfB); //洛仑兹力
			 }
		}
		
		Vector2D FE=E.Force(p); //全局电场力
		Vector2D fB=B.Force(p); //全局洛仑兹力
		p.force=p.force.add(FE).add(fB);
		p.force=p.force.add(p.velocity.mul(-u)).add(g.mul(p.m)); //空气阻力、重力
		*/
	}
	
	public void calcAcceleration() {
		for(int i=0;i<particles.size();++i) {
			 Particle p=particles.get(i);
			 if (!p.enable) continue;
			 p.acceleration=p.force.mul(1f/p.m); //a=F/m
		}
	}
	
	public void calcVelocity(double dt) {
		for(int i=0;i<particles.size();++i) {
			 Particle p=particles.get(i);
			 if (!p.enable) continue;
			 double av=p.velocity.DotP(p.acceleration);
			 //System.out.println(av);
			 if (Math.abs(av)>MyMath.zero || p.velocity.length()<=MyMath.zero)  {
				 p.velocity=p.velocity.add(p.acceleration.mul(dt));
			 } else { //认为av垂直, 只改变速度方向, 防止精度问题导致速度越来越大
				 //System.out.println("av");
				 Vector2D oldv=p.velocity;
				 p.velocity=p.velocity.add(p.acceleration.mul(dt));
				 //System.out.println(oldv.length());
				 //System.out.println(p.velocity.length());
				 p.velocity=p.velocity.mul(oldv.length() / p.velocity.length());
			 }
		}
	}

	public void calcPosition(double dt) {
		for(int i=0;i<particles.size();++i) {
			 Particle p=particles.get(i);
			 if (!p.enable) continue;
			 p.move(dt);
		}
	}
	
	public double calcCollision(double dt) {
		
		//寻找最近一次碰撞
		boolean hasCollision=false;
		ArrayList<CollisionInfo> closestCollision=new ArrayList<CollisionInfo>();
		double closestCollisionTime=dt;
		//Particle minp=null;
		//Line minB=null;
		for(int i=0;i<particles.size();++i) {
			 Particle p=particles.get(i);
			 if (!p.enable || p.velocity.length()<=0d) continue;
			 if (!p.enableColl) {
				 System.out.println("enableColl==false, continue");
				 p.enableColl=true;
				 continue;
			 }
			 //if (Math.abs(p.position.y+10)<1e-4) {
			 //	 System.out.println("!");
			 //}
			 //Line s=new Line(p.position, p.newPosition(dt));
			 Line s=new Line(p.position, p.newPositionEstimate(dt));
			 for(int j=0;j<boundaries.size();++j) {
				 Line cB=boundaries.get(j);
				 IntersectionResult ir=cB.findIntersection(s);
				 if (ir.type == 0) { //检测到碰撞
					 System.out.println("Intersection: "+String.valueOf(ir.Intersection));
					 //上次刚在这里碰过, 就跳过
					 if (p.flag && p.lastColl != null && p.lastColl.add(ir.Intersection.mul(-1)).length()<MyMath.zero) {
						 p.flag=false;
						 System.out.println("上次刚在这里碰过");
						 continue;
					 }
					
					 Vector2D ns=ir.Intersection.add(p.position.mul(-1));
					 double time=ns.length()/p.velocity.length(); //达到碰撞所需时间
					 System.out.println("time: "+String.valueOf(time));
					 if (time <= 0.0) {
						 continue;
					 }
					 if (time > 0.0 && Math.abs(time-closestCollisionTime)<1e-10) {
						 p.flag=true;
						 p.lastColl=ir.Intersection;
						 closestCollision.add(new CollisionInfo(time, p, cB, ir)); //添加到本次发生的碰撞
					 } else if (time < closestCollisionTime) {
						 p.flag=true;
						 p.lastColl=ir.Intersection;
						 if (time > 0.0) {
							 hasCollision=true;
							 closestCollisionTime=time; //发现更短时间内的碰撞
							 closestCollision.clear();
							 closestCollision.add(new CollisionInfo(time, p, cB, ir)); //添加到本次发生的碰撞
						 }
					 }
				 } /*else {
					 if (Math.abs(p.position.y+10)<1e-4) {
						 System.out.println("ir type: " + String.valueOf(ir.type));
						 System.out.println("ir type: " + String.valueOf(cB));
						 System.out.println("ir type: " + String.valueOf(s));
					 }
				 }*/
			 }
		}
		
		//if (hasCollision)
		 //System.out.println("min time: "+String.valueOf(mint));
		
		if (hasCollision /* && mint>0*/) {
			System.out.println(1);
			System.out.println("size: "+String.valueOf(closestCollision.size()));
			//碰撞前
			for(int i=0;i<closestCollision.size();++i) {
				System.out.println("set enableColl=false");
				closestCollision.get(i).p.enableColl = false;
			}
			/*if (minp != null) {
				System.out.println("minp.enableColl=false");
				minp.enableColl=false;
			}*/
			//迭代
			next(closestCollisionTime, true);
			//碰撞后
			for(int i=0;i<closestCollision.size();++i) {
				CollisionInfo ci=closestCollision.get(i);
				System.out.println("Vbefore: "+String.valueOf(ci.p.velocity));
				//反弹
				ci.p.velocity=ci.boundary.getSymmetry(ci.p.velocity).mul(ci.boundary.k);//碰撞后
				System.out.println("Vafter: "+String.valueOf(ci.p.velocity));
				
				System.out.println("set enableColl=true");
				ci.p.enableColl = true;
			}
			
		/*} else if (hasCollision && mint<=0) { //碰撞时
			System.out.println(2);
			//mint=1e-18d;
			//next(mint, false);*/
		} else if (!hasCollision) {
			//System.out.println(3);
			next(closestCollisionTime, false);
		}
		return closestCollisionTime;
	}
	
}
