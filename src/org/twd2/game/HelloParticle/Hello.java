package org.twd2.game.HelloParticle;

import static org.lwjgl.opengl.GL11.*;
//import java.io.InputStream;

//import java.nio.IntBuffer;

import org.lwjgl.LWJGLException;
//import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.twd2.game.HelloParticle.Field.ElectricField;
import org.twd2.game.HelloParticle.Field.Field;
import org.twd2.game.HelloParticle.Field.Magnetic;
import org.twd2.game.HelloParticle.Math.Line;
import org.twd2.game.HelloParticle.Math.Vector2D;
import org.twd2.game.HelloParticle.Physics.Particle;
import org.twd2.game.HelloParticle.Physics.World;
import org.twd2.game.HelloParticle.Shape.Circle;
import org.twd2.game.HelloParticle.Shape.Rectangle;
import org.twd2.game.HelloParticle.Shape.Shape;


public class Hello {
	
	public final static int WIDTH = 1024;
	public final static int HEIGHT= 768;
	
	public final static float pxpercm=25f;
	
	public final static boolean enableCollision=true;
	
	public float zoom=1f;
	public static Vector2D offset=new Vector2D(10,10);
	
	public /*final static*/ boolean strobePhoto=true;
	
	public /*final static*/ boolean renderVelocity=true;
	
	/** time at last frame */
	long lastFrame;
	
	long lastFPS=0; 
	int fps=0;
	
	int MaxFPS=-1;//60;
	
	public World world=new World();
	public Particle mousep=new Particle(1e15d);
	
	public Hello() {

	}
	
	public void printInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("万呆的HelloParticle\r\n");
		sb.append("红色框: 电场, 绿色框: 磁场\r\n");
		sb.append("操作说明: \r\n");
		sb.append("\tEsc\t退出\r\n");
		sb.append("\t0\t重置坐标原点\r\n");
		sb.append("\tF\t切换全屏模式\r\n");
		sb.append("\tS\t切换是否频闪照片\r\n");
		sb.append("\tV\t切换是否渲染速度\r\n");
		sb.append("\tZ\t重置缩放比例\r\n");
		sb.append("\t↑\t画布向下移动\r\n");
		sb.append("\t↓\t画布向上移动\r\n");
		sb.append("\t←\t画布向右移动\r\n");
		sb.append("\t→\t画布向左移动\r\n");
		sb.append("\t鼠标滚轮\t放大或缩小\r\n");
		System.out.println(sb.toString());
	}

	public void init() {
		printInfo();
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			//InputStream ins = this.getClass().getResourceAsStream("resources/favicon.png");
			//System.out.println(ins.available());
			
			//Display.setIcon(IconLoader.load(ins));
			Display.create();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		initWorld();
		initGL();
		//setupCoordinate();
	}

	public void initWorld() {
		mousep.enable=false;
		//mousep.q=1000d;
		world.addParticle(mousep);
		Examples.ex4(world);
	}
    
	public void initGL() {
		// init OpenGL
		//glEnable ( GL_DEPTH_TEST ); 
		glPointSize(5);  
		glLineWidth(2);  
		glEnable(GL_POINT_SMOOTH);
		glEnable(GL_LINE_SMOOTH);        //线抗锯齿  
		glEnable(GL_POLYGON_SMOOTH);     //多边形抗锯齿 
		glHint(GL_POINT_SMOOTH_HINT, GL_NICEST); // Make round points, not square points  
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);  // Antialias the lines 
		glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST); 
		glEnable(GL_BLEND);  
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);  
		//glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH, 0, HEIGHT, 1, -1);
		//glMatrixMode(GL_MODELVIEW);
	}
	
	public void start() {
		init();
		getDelta();
		while(!Display.isCloseRequested())
		{
			update(getDelta());
			renderGL();
			
			updateFPS();
			Display.update();
			if (MaxFPS>0) {
				Display.sync(MaxFPS);
			}
		}
		Display.destroy();
		System.exit(0);
	}
	
	public void clearGL() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		//glClear(GL_COLOR_BUFFER_BIT);
		//glClear(GL_DEPTH_BUFFER_BIT);
		glFlush();
	}
	
	public void renderGL() {
		if (!strobePhoto)
			clearGL();
		
		glColor4f(0.5f, 0.5f, 0.5f, 0.5f);
		//原点
		glBegin(GL_POINTS);
		glVertex2f((float)offset.x*pxpercm*zoom + 0*pxpercm*zoom, (float)offset.y*pxpercm*zoom + 0*pxpercm*zoom);
		glEnd();
		//坐标轴
		glBegin(GL_LINES);
		
		//x
		glVertex2f((float)offset.x*pxpercm*zoom + -10f*pxpercm*zoom, (float)offset.y*pxpercm*zoom + 0f*pxpercm*zoom);
		glVertex2f((float)offset.x*pxpercm*zoom + 10f*pxpercm*zoom, (float)offset.y*pxpercm*zoom + 0f*pxpercm*zoom);
		glVertex2f((float)offset.x*pxpercm*zoom + 9.75f*pxpercm*zoom, (float)offset.y*pxpercm*zoom + 0.25f*pxpercm*zoom);
		glVertex2f((float)offset.x*pxpercm*zoom + 10f*pxpercm*zoom, (float)offset.y*pxpercm*zoom + 0f*pxpercm*zoom);
		glVertex2f((float)offset.x*pxpercm*zoom + 9.75f*pxpercm*zoom, (float)offset.y*pxpercm*zoom + -0.25f*pxpercm*zoom);
		glVertex2f((float)offset.x*pxpercm*zoom + 10f*pxpercm*zoom, (float)offset.y*pxpercm*zoom + 0f*pxpercm*zoom);

		//y
		glVertex2f((float)offset.x*pxpercm*zoom + 0f*pxpercm*zoom, (float)offset.y*pxpercm*zoom + -10f*pxpercm*zoom);
		glVertex2f((float)offset.x*pxpercm*zoom + 0f*pxpercm*zoom, (float)offset.y*pxpercm*zoom + 10f*pxpercm*zoom);
		glVertex2f((float)offset.x*pxpercm*zoom + 0.25f*pxpercm*zoom, (float)offset.y*pxpercm*zoom + 9.75f*pxpercm*zoom);
		glVertex2f((float)offset.x*pxpercm*zoom + 0f*pxpercm*zoom, (float)offset.y*pxpercm*zoom + 10f*pxpercm*zoom);
		glVertex2f((float)offset.x*pxpercm*zoom + -0.25f*pxpercm*zoom, (float)offset.y*pxpercm*zoom + 9.75f*pxpercm*zoom);
		glVertex2f((float)offset.x*pxpercm*zoom + 0f*pxpercm*zoom, (float)offset.y*pxpercm*zoom + 10f*pxpercm*zoom);

		
		glEnd();
				
		renderFields();
		renderBoundary();
		renderWorld();
		//renderVector(Vector2D.makeNew(10, 45f/180f*(float)Math.PI));
		
		
		
		glFlush();
	}
	
	public void renderFields() {
		for(int i=0;i<world.fields.size();++i) {
			Field cf=world.fields.get(i);
			
			if (cf instanceof ElectricField) {
				renderE((ElectricField)cf);
			} else if (cf instanceof Magnetic) {
				renderB((Magnetic)cf);
			} else {
				
			}
			
			/*Shape r=cf.Region;
			glColor4f(0.5f, 1f, 0.5f, 0.5f);
			renderShape(r);*/
		}
	}
	
	public void renderB(Magnetic b) {
		glColor4f(0.5f, 1f, 0.5f, 0.5f);
		renderShape(b.Region);
	}
	
	public void renderE(ElectricField e) {
		glColor4f(1f, 0.5f, 0.5f, 0.5f);
		renderShape(e.Region);
	}
	
	/*public void renderB() {
		if (world.B.value!=0.0) {
			glPointSize(1);  
			glLineWidth(1); 
			glColor4f(0.5f, 0.5f, 0.5f, 0.5f);
			if (world.B.Direction) { //.
				glBegin(GL_POINTS);
				for (int y=0;y<=100;++y) {
					for (int x=0;x<=100;++x) {
						glVertex2f(x*pxpercm, y*pxpercm);
					}
				}
				glEnd();
			} else { //x
				glBegin(GL_LINES);
				for (int y=0;y<=100;++y) {
					for (int x=0;x<=100;++x) {
						glVertex2f((x-0.25f)*pxpercm, (y-0.25f)*pxpercm);
						glVertex2f((x+0.25f)*pxpercm, (y+0.25f)*pxpercm);
						glVertex2f((x-0.25f)*pxpercm, (y+0.25f)*pxpercm);
						glVertex2f((x+0.25f)*pxpercm, (y-0.25f)*pxpercm);
					}
				}
				glEnd();
			}
		}
		for(int i=0;i<world.aB.size();++i) {
			Magnetic cB=world.aB.get(i);
			Shape r=cB.Region;
			glColor4f(0.5f, 1f, 0.5f, 0.5f);
			renderShape(r);
		}
	}
	
	public void renderE() {
		for(int i=0;i<world.aE.size();++i) {
			ElectricField cE=world.aE.get(i);
			Shape r=cE.Region;
			glColor4f(1f, 0.5f, 0.5f, 0.5f);
			renderShape(r);
		}
	}*/
	
	public void renderBoundary() {
		for(int i=0;i<world.boundaries.size();++i) {
			Line cB=world.boundaries.get(i);
			glColor4f(0f, 1f, 1f, 0.5f);
			glBegin(GL_LINES);
			glVertex2f((float)(offset.x*pxpercm*zoom + cB.p0.x*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + cB.p0.y*pxpercm*zoom));
			glVertex2f((float)(offset.x*pxpercm*zoom + cB.p1.x*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + cB.p1.y*pxpercm*zoom));
			glEnd();
		}
	}
	
	public void renderShape(Shape r) {
		if (r instanceof Rectangle) {
			renderRect((Rectangle)r);
		} else if (r instanceof Circle) {
			renderCircle((Circle)r);
		}
	}
	
	public void renderRect(Rectangle r) {
		glBegin(GL_LINES);
		glVertex2f((float)(offset.x*pxpercm*zoom + r.x0*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y0*pxpercm*zoom));
		glVertex2f((float)(offset.x*pxpercm*zoom + r.x0*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y1*pxpercm*zoom));
		glVertex2f((float)(offset.x*pxpercm*zoom + r.x0*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y1*pxpercm*zoom));
		glVertex2f((float)(offset.x*pxpercm*zoom + r.x1*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y1*pxpercm*zoom));
		glVertex2f((float)(offset.x*pxpercm*zoom + r.x1*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y1*pxpercm*zoom));
		glVertex2f((float)(offset.x*pxpercm*zoom + r.x1*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y0*pxpercm*zoom));
		glVertex2f((float)(offset.x*pxpercm*zoom + r.x1*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y0*pxpercm*zoom));
		glVertex2f((float)(offset.x*pxpercm*zoom + r.x0*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y0*pxpercm*zoom));
		glEnd();
	}
	
	public void renderCircle(Circle r) {
		glBegin(GL_LINES);
		double di=Math.PI/400;
		double lastx=r.centre.x+r.radius;
		double lasty=r.centre.y;
		for(double i=0;i<2*Math.PI;i+=di) {
			glVertex2f((float)(offset.x*pxpercm*zoom + lastx*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + lasty*pxpercm*zoom));
			lastx=r.centre.x+r.radius*Math.cos(i);
			lasty=r.centre.y+r.radius*Math.sin(i);
			glVertex2f((float)(offset.x*pxpercm*zoom + lastx*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + lasty*pxpercm*zoom));
		}
		//TODO: 
		/*glVertex2f((float)(offset.x*pxpercm*zoom + r.x0*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y0*pxpercm*zoom));
		glVertex2f((float)(offset.x*pxpercm*zoom + r.x0*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y1*pxpercm*zoom));
		glVertex2f((float)(offset.x*pxpercm*zoom + r.x0*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y1*pxpercm*zoom));
		glVertex2f((float)(offset.x*pxpercm*zoom + r.x1*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y1*pxpercm*zoom));
		glVertex2f((float)(offset.x*pxpercm*zoom + r.x1*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y1*pxpercm*zoom));
		glVertex2f((float)(offset.x*pxpercm*zoom + r.x1*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y0*pxpercm*zoom));
		glVertex2f((float)(offset.x*pxpercm*zoom + r.x1*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y0*pxpercm*zoom));
		glVertex2f((float)(offset.x*pxpercm*zoom + r.x0*pxpercm*zoom), (float)(offset.y*pxpercm*zoom + r.y0*pxpercm*zoom));
		*/
		glEnd();
	}
	
	public void renderWorld() {
		glPointSize(5);  
		glLineWidth(2); 
		glColor4f(0.5f, 0.5f, 1f, 1f);
		for(int i=0;i<world.particles.size();++i) {
			Particle p=world.particles.get(i);
			if (!p.enable) continue;
			renderParticle(p);
		}
	}
	
	public void renderParticle(Particle p) {
		glBegin(GL_POINTS);
		glVertex2f((float)offset.x*pxpercm*zoom + (float)p.position.x*pxpercm*zoom, (float)offset.y*pxpercm*zoom + (float)p.position.y*pxpercm*zoom);
		glEnd();
		//p.velocity=p.velocity.unitV().mul(10);
		//System.out.println(p.velocity);
		if (renderVelocity) {
			renderVector(p.velocity);
		}
		//System.out.println(p.velocity.rad());
	}
	
	public void renderVector(Vector2D v) {
		glBegin(GL_POINTS);
		glVertex2f((float)offset.x*pxpercm*zoom + (float)v.x*pxpercm*zoom, (float)offset.y*pxpercm*zoom + (float)v.y*pxpercm*zoom);
		glEnd();
		glBegin(GL_LINES);
		glVertex2f((float)offset.x*pxpercm*zoom + 0*pxpercm*zoom, (float)offset.y*pxpercm*zoom + 0*pxpercm*zoom);
		glVertex2f((float)offset.x*pxpercm*zoom + (float)v.x*pxpercm*zoom, (float)offset.y*pxpercm*zoom + (float)v.y*pxpercm*zoom);
		glEnd();
	}
	
	public void update(float delta) {
		updateInput(delta);
		world.next(0.001f,1000,enableCollision);
	}
	
	public void updateInput(float delta) {
		onMouseDrag(Mouse.isButtonDown(0));
		//System.out.println(Mouse.isButtonDown(0));
		int dWheel = Mouse.getDWheel();
		if (dWheel!=0)
			clearGL();
		//System.out.println(dWheel);
		if (dWheel>0)
			zoom*=0.01f*dWheel;
		else if (dWheel<0)
			zoom/=0.01f*(-dWheel);
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			offset.y -=0.05f*delta/zoom;
			clearGL();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			offset.y +=0.05f*delta/zoom;
			clearGL();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			offset.x +=0.05f*delta/zoom;
			clearGL();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			offset.x -=0.05f*delta/zoom;
        	clearGL();
		}
		
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		        if (Keyboard.getEventKey() == Keyboard.KEY_0) {
		        	offset=Vector2D.zero.mul(1);
		        	clearGL();
		        } else if (Keyboard.getEventKey() == Keyboard.KEY_Z) {
		        	zoom=1f;
		        	clearGL();
		        } else if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
		        	System.out.println("用户按下了esc");
		        	System.exit(0);
		        } else if (Keyboard.getEventKey() == Keyboard.KEY_F) {
		        	setDisplayMode(WIDTH, HEIGHT, !Display.isFullscreen());
		        } 
		        /*else if (Keyboard.getEventKey() == Keyboard.KEY_V) {
		        	vsync = !vsync;
		        	Display.setVSyncEnabled(vsync);
		        }*/
		        else if (Keyboard.getEventKey() == Keyboard.KEY_S) {
		        	strobePhoto=!strobePhoto;
		        }
		        else if (Keyboard.getEventKey() == Keyboard.KEY_V) {
		        	renderVelocity=!renderVelocity;
		        }
		    }
		}
	}
	
	/**
	 * Set the display mode to be used 
	 * 
	 * @param width The width of the display required
	 * @param height The height of the display required
	 * @param fullscreen True if we want fullscreen mode
	 */
	public void setDisplayMode(int width, int height, boolean fullscreen) {

		// return if requested DisplayMode is already set
                if ((Display.getDisplayMode().getWidth() == width) && 
			(Display.getDisplayMode().getHeight() == height) && 
			(Display.isFullscreen() == fullscreen)) {
			return;
		}
		
		try {
			DisplayMode targetDisplayMode = null;
			
			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;
				
				for (int i=0;i<modes.length;i++) {
					DisplayMode current = modes[i];
					
					if ((current.getWidth() == width) && (current.getHeight() == height)) {
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
							if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						// if we've found a match for bpp and frequence against the 
						// original display mode then it's probably best to go for this one
						// since it's most likely compatible with the monitor
						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
						    (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				targetDisplayMode = new DisplayMode(width,height);
			}
			
			if (targetDisplayMode == null) {
				System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);
			
		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
		}
	}
	
	/**
	 * Get the time in milliseconds
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	/** 
	 * Calculate how many milliseconds have passed 
	 * since last frame.
	 * 
	 * @return milliseconds passed since last frame 
	 */
	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
 
	    return delta;
	}
	
	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		 //Display.setTitle(String.valueOf(getTime()));
	    if (getTime() - lastFPS > 1000) {
	        Display.setTitle("FPS: " + fps); 
	        fps = 0; //reset the FPS counter
	        lastFPS = getTime(); //add one second
	    }
	   fps++;
	}
	
	private boolean lastMouseState=false; 
	public void onMouseDrag(boolean buttonDown) {
		//从没有按下到按下
		if (!lastMouseState && buttonDown) {
			mousep.enable=true;
			//System.out.println("用户按下了鼠标");
		}
		if(buttonDown) {
			//处理鼠标移动
			mousep.velocity=Vector2D.zero;
			mousep.position=new Vector2D((double)Mouse.getX()/pxpercm/zoom-offset.x,(double)Mouse.getY()/pxpercm/zoom-offset.y);
			//System.out.println(mousep.postion);
		}
		//从按下到没有按下
		if (lastMouseState && !buttonDown) {
			mousep.enable=false;
			//System.out.println("用户松开了鼠标");
		}
		
		lastMouseState=buttonDown;
	}
}
