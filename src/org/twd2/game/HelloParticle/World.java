package org.twd2.game.HelloParticle;

import java.io.IOException;
import java.util.ArrayList;

public class World {

	
	public boolean enableGravity=true;//true;
	public boolean enableCoulombForce=true;//=true;
	
	public final double G=6.6732e-11d; //������������
	public final double k=8987000000d; //���׶��ɳ���
	
	public ElectricField E=new ElectricField();
	public Vector2D g=new Vector2D(0,-9.80665d);//�糡���������ٶ�
	public Magnetic B=new Magnetic();
	public double u=0.0; //ȫ������ϵ��
	
	ArrayList<Particle> ap=new ArrayList<Particle>();
	ArrayList<Magnetic> aB=new ArrayList<Magnetic>();
	ArrayList<ElectricField> aE=new ArrayList<ElectricField>();
	ArrayList<Line> aBoundary=new ArrayList<Line>();
	
	public World() {
		
	}
	
	public void addParticle(Particle p) {
		ap.add(p);
	}
	
	public void addMagnetic(Magnetic b) {
		aB.add(b);
	}
	
	public void addElectricField(ElectricField e) {
		aE.add(e);
	}
	
	public void addBoundary(Line b) {
		aBoundary.add(b);
	}
	
	public void addBoundary(Rect r) {
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
			calcForce(); //��������
			calcAcceleration(); //����������ٶ�
			calcPosition(dt); //������λ��
			calcVelocity(dt); //�ü��ٶ������ʱ��ĩ���ٶ�
		} else {
			while(dt>0.0) {
				calcForce(); //��������
				calcAcceleration(); //����������ٶ�
				dt-=calcCollision(dt); //��ײ���
				break;
			}
		}
	}
	
	public void calcForce() {
		for(int i=0;i<ap.size();++i) {
			Particle p=ap.get(i);
			if (!p.enable) continue;
			 calcParticleForce(p);
		}  
	}
	
	public void calcParticleForce(Particle p) {
		p.force=Vector2D.zero;
		
		if (enableGravity || enableCoulombForce) {
			if (enableGravity && enableCoulombForce) {
				//����������������
				for(int i=0;i<ap.size();++i) {
					Particle cp=ap.get(i);
					if (cp==p) continue; //���Լ�������������������������������
					if (!cp.enable) continue;
					Vector2D delta=p.position.add(cp.position.mul(-1)); //ָ�򱻷�������
					double R2=delta.length2();
					double R=Math.sqrt(R2);
					Vector2D F0=delta.mul(1f/R); //��ָ�򱻷�������ĵ�λ����
					 
					Vector2D FG=F0.mul(-G*p.m*cp.m/R2); //��������, ������Ϊ��ʹ����������Ӷ�ָ������һ������, ������
					Vector2D Fk=F0.mul(k*p.q*cp.q/R2); //������, ͬ������
					p.force=p.force.add(FG).add(Fk);
				}
			}
			if (enableGravity && !enableCoulombForce) {
				//����������
				for(int i=0;i<ap.size();++i) {
					Particle cp=ap.get(i);
					if (cp==p) continue; //���Լ�������������������������������
					if (!cp.enable) continue;
					Vector2D delta=p.position.add(cp.position.mul(-1)); //ָ�򱻷�������
					double R2=delta.length2();
					double R=Math.sqrt(R2);
					Vector2D F0=delta.mul(1f/R); //��ָ�򱻷�������ĵ�λ����
					 
					Vector2D FG=F0.mul(-G*p.m*cp.m/R2); //��������, ������Ϊ��ʹ����������Ӷ�ָ������һ������, ������
					p.force=p.force.add(FG);
				}
			}
			if (!enableGravity && enableCoulombForce) {
				//��������
				for(int i=0;i<ap.size();++i) {
					Particle cp=ap.get(i);
					if (cp==p) continue; //���Լ�������������������������������
					if (!cp.enable) continue;
					Vector2D delta=p.position.add(cp.position.mul(-1)); //ָ�򱻷�������
					double R2=delta.length2();
					double R=Math.sqrt(R2);
					Vector2D F0=delta.mul(1f/R); //��ָ�򱻷�������ĵ�λ����

					Vector2D Fk=F0.mul(k*p.q*cp.q/R2); //������, ͬ������
					p.force=p.force.add(Fk);
				}
			}
		}
		for(int i=0;i<aE.size();++i) {
			 ElectricField cE=aE.get(i);
			 if (cE.Region.isIn(p.position)) {			 
				 Vector2D cFE=cE.Force(p);
				 p.force=p.force.add(cFE); //�糡��
			 }
		}
		for(int i=0;i<aB.size();++i) {
			 Magnetic cB=aB.get(i);
			 if (cB.Region.isIn(p.position)) {			 
				 Vector2D cfB=cB.Force(p);
				 p.force=p.force.add(cfB); //��������
			 }
		}
		Vector2D FE=E.Force(p); //ȫ�ֵ糡��
		Vector2D fB=B.Force(p); //ȫ����������
		p.force=p.force.add(FE).add(fB);
		p.force=p.force.add(p.velocity.mul(-u)).add(g.mul(p.m)); //��������������
	}
	
	public void calcAcceleration() {
		for(int i=0;i<ap.size();++i) {
			 Particle p=ap.get(i);
			 if (!p.enable) continue;
			 p.acceleration=p.force.mul(1f/p.m); //a=F/m
		}
	}
	
	public void calcVelocity(double dt) {
		for(int i=0;i<ap.size();++i) {
			 Particle p=ap.get(i);
			 if (!p.enable) continue;
			 double av=p.velocity.DotP(p.acceleration);
			 //System.out.println(av);
			 if (Math.abs(av)>MyMath.zero || p.velocity.length()<=MyMath.zero)  {
				 p.velocity=p.velocity.add(p.acceleration.mul(dt));
			 } else { //��Ϊav��ֱ, ֻ�ı��ٶȷ���, ��ֹ�������⵼���ٶ�Խ��Խ��
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
		for(int i=0;i<ap.size();++i) {
			 Particle p=ap.get(i);
			 if (!p.enable) continue;
			 p.move(dt);
		}
	}
	
	public double calcCollision(double dt) {
		
		//Ѱ�����һ����ײ
		boolean hasCollision=false;
		ArrayList<CollisionInfo> closestCollision=new ArrayList<CollisionInfo>();
		double closestCollisionTime=dt;
		//Particle minp=null;
		//Line minB=null;
		for(int i=0;i<ap.size();++i) {
			 Particle p=ap.get(i);
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
			 for(int j=0;j<aBoundary.size();++j) {
				 Line cB=aBoundary.get(j);
				 IntersectionResult ir=cB.findIntersection(s);
				 if (ir.type == 0) { //��⵽��ײ
					 System.out.println("Intersection: "+String.valueOf(ir.Intersection));
					 //�ϴθ�����������, ������
					 if (p.flag && p.lastColl != null && p.lastColl.add(ir.Intersection.mul(-1)).length()<MyMath.zero) {
						 p.flag=false;
						 System.out.println("�ϴθ�����������");
						 continue;
					 }
					
					 Vector2D ns=ir.Intersection.add(p.position.mul(-1));
					 double time=ns.length()/p.velocity.length(); //�ﵽ��ײ����ʱ��
					 System.out.println("time: "+String.valueOf(time));
					 if (time <= 0.0) {
						 continue;
					 }
					 if (time > 0.0 && Math.abs(time-closestCollisionTime)<1e-10) {
						 p.flag=true;
						 p.lastColl=ir.Intersection;
						 closestCollision.add(new CollisionInfo(time, p, cB, ir)); //��ӵ����η�������ײ
					 } else if (time < closestCollisionTime) {
						 p.flag=true;
						 p.lastColl=ir.Intersection;
						 if (time > 0.0) {
							 hasCollision=true;
							 closestCollisionTime=time; //���ָ���ʱ���ڵ���ײ
							 closestCollision.clear();
							 closestCollision.add(new CollisionInfo(time, p, cB, ir)); //��ӵ����η�������ײ
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
			//��ײǰ
			for(int i=0;i<closestCollision.size();++i) {
				System.out.println("set enableColl=false");
				closestCollision.get(i).p.enableColl = false;
			}
			/*if (minp != null) {
				System.out.println("minp.enableColl=false");
				minp.enableColl=false;
			}*/
			//����
			next(closestCollisionTime, true);
			//��ײ��
			for(int i=0;i<closestCollision.size();++i) {
				CollisionInfo ci=closestCollision.get(i);
				System.out.println("Vbefore: "+String.valueOf(ci.p.velocity));
				//����
				ci.p.velocity=ci.boundary.getSymmetry(ci.p.velocity).mul(ci.boundary.k);//��ײ��
				System.out.println("Vafter: "+String.valueOf(ci.p.velocity));
				
				System.out.println("set enableColl=true");
				ci.p.enableColl = true;
			}
			
		/*} else if (hasCollision && mint<=0) { //��ײʱ
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
