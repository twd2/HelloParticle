package org.twd2.game.HelloParticle;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.twd2.game.HelloParticle.Field.Electric;
import org.twd2.game.HelloParticle.Field.Field;
import org.twd2.game.HelloParticle.Field.Magnetic;
import org.twd2.game.HelloParticle.Field.Block;
import org.twd2.game.HelloParticle.Joint.Joint;
import org.twd2.game.HelloParticle.Math.Line;
import org.twd2.game.HelloParticle.Math.Vector2D;
import org.twd2.game.HelloParticle.Physics.Particle;
import org.twd2.game.HelloParticle.Physics.World;
import org.twd2.game.HelloParticle.Shape.AndShape;
import org.twd2.game.HelloParticle.Shape.Circle;
import org.twd2.game.HelloParticle.Shape.NotShape;
import org.twd2.game.HelloParticle.Shape.OrShape;
import org.twd2.game.HelloParticle.Shape.Rectangle;
import org.twd2.game.HelloParticle.Shape.Shape;
import org.twd2.game.HelloParticle.Shape.XorShape;

public class Renderer {
	
	public final static int WIDTH = 1024;
	public final static int HEIGHT= 768;
	
	public final static float pxPerUnit=25f;
	
	private float zoom=1f;
	
	private float zoomwithpxPerUnit;
	
	private Vector2D offset=new Vector2D(10,10);
	private Vector2D zoomedOffset=new Vector2D(10,10);
	
	public boolean renderParticleAsDot=false;	
	
	private boolean internalRenderParticleAsDot=false;
	
	public /*final static*/ boolean strobePhoto=true;
	
	public /*final static*/ boolean renderVelocity=true;
	
	
	long lastFPS=0; 
	public int fps=0;
	
	public int MaxFPS=-1;//60;
	
	public World world;
	
	public Renderer(World world) {
		this.world=world;
		updateZoom();
		internalInit();
	}
	
	private void updateZoom() {
		zoomwithpxPerUnit=pxPerUnit*getZoom();
		zoomedOffset=getOffset().mul(zoomwithpxPerUnit);
		if (zoom<1) {
			internalRenderParticleAsDot=true;
		} else {
			internalRenderParticleAsDot=renderParticleAsDot;
		}
	}
	
	private void internalInit() {
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
	}
	
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		//glClear(GL_COLOR_BUFFER_BIT);
		//glClear(GL_DEPTH_BUFFER_BIT);
		glFlush();
	}
	
	public void init() {
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
	
	public void next() {
		render();
		
		updateFPS();
		Display.update();
		if (MaxFPS>0) {
			Display.sync(MaxFPS);
		}
	}
	
	/**
	 * 经过缩放和偏移的glVertex2f
	 * @param x 横坐标
	 * @param y 纵坐标
	 */
	private void glVertex2fWithZoomAndOffset(float x, float y) {
		glVertex2f((float)zoomedOffset.x + x*zoomwithpxPerUnit, (float)zoomedOffset.y + y*zoomwithpxPerUnit);
	}
	
	
	public void render() {
		if (!strobePhoto)
			clear();
		
		glColor4f(0.5f, 0.5f, 0.5f, 0.5f);
		//原点
		glBegin(GL_POINTS);
		glVertex2fWithZoomAndOffset(0,0);
		glEnd();
		//坐标轴
		glBegin(GL_LINES);
		
		//x
		glVertex2fWithZoomAndOffset(-10f , 0f    );
		glVertex2fWithZoomAndOffset(10f  , 0f    );
		glVertex2fWithZoomAndOffset(9.75f, 0.25f );
		glVertex2fWithZoomAndOffset(10f  , 0f    );
		glVertex2fWithZoomAndOffset(9.75f, -0.25f);
		glVertex2fWithZoomAndOffset(10f  , 0f    );
		
		//y
		glVertex2fWithZoomAndOffset(0f    , -10f );
		glVertex2fWithZoomAndOffset(0f    , 10f  );
		glVertex2fWithZoomAndOffset(0.25f , 9.75f);
		glVertex2fWithZoomAndOffset(0f    , 10f  );
		glVertex2fWithZoomAndOffset(-0.25f, 9.75f);
		glVertex2fWithZoomAndOffset(0f    , 10f  );
		
		glEnd();
				
		renderFields();
		renderBoundary();
		renderWorld();
		renderJoint();
		//renderVector(Vector2D.makeNew(10, 45f/180f*(float)Math.PI));

		glFlush();
	}
	
	public void renderFields() {
		for(int i=0;i<world.fields.size();++i) {
			Field cf=world.fields.get(i);
			
			if (cf instanceof Electric) {
				glColor4f(1f, 0.5f, 0.5f, 0.5f);
				renderShape(cf.Region);
			} else if (cf instanceof Magnetic) {
				glColor4f(0.5f, 1f, 0.5f, 0.5f);
				renderShape(cf.Region);
			} else if(cf instanceof Block) {
				glColor4f(0.5f, 0.5f, 0.5f, 0.5f);
				renderShape(cf.Region);
			} else {
				
			}
		}
	}
	
	public void renderJoint() {
		for(int i=0;i<world.joints.size();++i) {
			Joint j=world.joints.get(i);
			if (!j.enable) continue;
			glColor4f(1f, 1f, 0f, 0.5f);
			glBegin(GL_LINES);
			glVertex2fWithZoomAndOffset((float)j.p1.position.x, (float)j.p1.position.y);
			glVertex2fWithZoomAndOffset((float)j.p2.position.x, (float)j.p2.position.y);
			glEnd();
		}
	}
	
	public void renderBoundary() {
		for(int i=0;i<world.boundaries.size();++i) {
			Line cB=world.boundaries.get(i);
			glColor4f(0f, 1f, 1f, 0.5f);
			glBegin(GL_LINES);
			glVertex2fWithZoomAndOffset((float)cB.p0.x, (float)cB.p0.y);
			glVertex2fWithZoomAndOffset((float)cB.p1.x, (float)cB.p1.y);
			glEnd();
		}
	}
	
	public void renderShape(Shape r) {
		if (r instanceof Rectangle) {
			renderRectangle((Rectangle)r);
		} else if (r instanceof Circle) {
			renderCircle((Circle)r);
		} else if (r instanceof NotShape) {
			renderShape(((NotShape)r).org);
		} else if (r instanceof OrShape) {
			OrShape s=(OrShape)r;
			renderShape(s.org1);
			renderShape(s.org2);
		} else if (r instanceof XorShape) {
			XorShape s=(XorShape)r;
			renderShape(s.org1);
			renderShape(s.org2);
		} else if (r instanceof AndShape) {
			AndShape s=(AndShape)r;
			renderShape(s.org1);
			renderShape(s.org2);
		}
	}
	
	public void renderRectangle(Rectangle r) {
		glBegin(GL_LINES);
		glVertex2fWithZoomAndOffset((float)r.x0, (float)r.y0);
		glVertex2fWithZoomAndOffset((float)r.x0, (float)r.y1);
		glVertex2fWithZoomAndOffset((float)r.x0, (float)r.y1);
		glVertex2fWithZoomAndOffset((float)r.x1, (float)r.y1);
		glVertex2fWithZoomAndOffset((float)r.x1, (float)r.y1);
		glVertex2fWithZoomAndOffset((float)r.x1, (float)r.y0);
		glVertex2fWithZoomAndOffset((float)r.x1, (float)r.y0);
		glVertex2fWithZoomAndOffset((float)r.x0, (float)r.y0);
		glEnd();
	}
	
	public void renderCircle(Vector2D centre, double radius) {
		renderCircle(centre, radius, 400);
	}
	
	public void renderCircle(Vector2D centre, double radius, int ndi) {
		glBegin(GL_LINES);
		double di=2*Math.PI/ndi;
		double lastx=centre.x+radius;
		double lasty=centre.y+0d;
		for(double i=0;i<2*Math.PI+di;i+=di) {
			glVertex2f((float)lastx, (float)lasty);
			lastx=centre.x+radius*Math.cos(i);
			lasty=centre.y+radius*Math.sin(i);
			glVertex2f((float)lastx, (float)lasty);
		}
		glEnd();
	}
	
	public void renderCircle(Circle r) {
		renderCircle(r,400);
	}
	
	public void renderCircle(Circle r, int ndi) {
		renderCircle(r.centre.mul(zoomwithpxPerUnit).add(zoomedOffset), r.radius*zoomwithpxPerUnit, ndi);
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
		if (internalRenderParticleAsDot) {
			glBegin(GL_POINTS);
			glVertex2fWithZoomAndOffset((float)p.position.x, (float)p.position.y);
			glEnd();
		} else {
			Vector2D centre=p.position.mul(zoomwithpxPerUnit).add(zoomedOffset);
			renderCircle(centre,5,20);
			if (p.q>0d) {
				glBegin(GL_LINES);
				glVertex2f((float)(centre.x-5), (float)(centre.y));
				glVertex2f((float)(centre.x+5), (float)(centre.y));
				glVertex2f((float)(centre.x), (float)(centre.y-5));
				glVertex2f((float)(centre.x), (float)(centre.y+5));
				glEnd();
			} else if (p.q<0d) {
				glBegin(GL_LINES);
				glVertex2f((float)(centre.x-5), (float)(centre.y));
				glVertex2f((float)(centre.x+5), (float)(centre.y));
				glEnd();
			}
		}
		
		if (renderVelocity && !p.fixed) {
			renderVector(p.velocity);
		}
	}
	
	public void renderVector(Vector2D v) {
		glBegin(GL_POINTS);
		glVertex2fWithZoomAndOffset((float)v.x, (float)v.y);
		glEnd();
		glBegin(GL_LINES);
		glVertex2fWithZoomAndOffset(0,0);
		glVertex2fWithZoomAndOffset((float)v.x, (float)v.y);
		glEnd();
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
	
	public void setFullscreen() {
		setDisplayMode(WIDTH, HEIGHT, !Display.isFullscreen());
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

	public Vector2D getOffset() {
		return offset;
	}

	public void setOffset(Vector2D offset) {
		this.offset = offset;
		updateZoom();
	}

	public float getZoom() {
		return zoom;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
		updateZoom();
	}
}
