package org.twd2.game.HelloParticle;



//import java.nio.IntBuffer;

//import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.twd2.game.HelloParticle.Math.Vector2D;
import org.twd2.game.HelloParticle.Physics.Particle;
import org.twd2.game.HelloParticle.Physics.World;


public class Hello {
	
	public final static boolean enableCollision=true;
	
	/** time at last frame */
	private long lastFrame;	
	
	private World world=new World();
	private Renderer renderer;
	private Particle mouseParticle=new Particle(1e15d);
	
	public Hello() {

	}
	
	public void printInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("万呆的HelloParticle\r\n");
		sb.append("红色边界: 电场, 绿色边界: 磁场\r\n");
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
		initWorld();
		renderer=new Renderer(world);
		renderer.init();
	}

	public void initWorld() {
		mouseParticle.enable=false;
		mouseParticle.fixed=true;
		//mouseParticle.q=1000d;
		world.addParticle(mouseParticle);
		Examples.ex9(world);
	}
    
	public void start() {
		init();
		getDelta();
		while(!Display.isCloseRequested())
		{
			update(getDelta());
			renderer.next();
		}
		Display.destroy();
		System.exit(0);
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
			renderer.clear();
		//System.out.println(dWheel);
		if (dWheel>0)
			renderer.setZoom(renderer.getZoom() * (0.01f*dWheel));
		else if (dWheel<0)
			renderer.setZoom(renderer.getZoom() / (0.01f*(-dWheel)));
		
		Vector2D offset=renderer.getOffset();
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			offset.y -=0.05f*delta/renderer.getZoom();
			renderer.setOffset(offset);
			renderer.clear();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			offset.y +=0.05f*delta/renderer.getZoom();
			renderer.setOffset(offset);
			renderer.clear();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			offset.x +=0.05f*delta/renderer.getZoom();
			renderer.setOffset(offset);
			renderer.clear();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			offset.x -=0.05f*delta/renderer.getZoom();
			renderer.setOffset(offset);
			renderer.clear();
		}
		
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		        if (Keyboard.getEventKey() == Keyboard.KEY_0) {
		        	renderer.setOffset(Vector2D.zero.mul(1));
		        	renderer.clear();
		        } else if (Keyboard.getEventKey() == Keyboard.KEY_Z) {
		        	renderer.setZoom(1f);
		        	renderer.clear();
		        } else if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
		        	System.out.println("用户按下了esc");
		        	System.exit(0);
		        } else if (Keyboard.getEventKey() == Keyboard.KEY_F) {
		        	renderer.setFullscreen();
		        } 
		        /*else if (Keyboard.getEventKey() == Keyboard.KEY_V) {
		        	vsync = !vsync;
		        	Display.setVSyncEnabled(vsync);
		        }*/
		        else if (Keyboard.getEventKey() == Keyboard.KEY_S) {
		        	renderer.strobePhoto=!renderer.strobePhoto;
		        }
		        else if (Keyboard.getEventKey() == Keyboard.KEY_V) {
		        	renderer.renderVelocity=!renderer.renderVelocity;
		        }
		    }
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
	
	
	
	private boolean lastMouseState=false; 
	public void onMouseDrag(boolean buttonDown) {
		//从没有按下到按下
		if (!lastMouseState && buttonDown) {
			mouseParticle.enable=true;
			//System.out.println("用户按下了鼠标");
		}
		if(buttonDown) {
			//处理鼠标移动
			mouseParticle.velocity=Vector2D.zero;
			mouseParticle.position=new Vector2D((double)Mouse.getX()/renderer.pxPerUnit/renderer.getZoom()-renderer.getOffset().x,(double)Mouse.getY()/renderer.pxPerUnit/renderer.getZoom()-renderer.getOffset().y);
			//System.out.println(mousep.postion);
		}
		//从按下到没有按下
		if (lastMouseState && !buttonDown) {
			mouseParticle.enable=false;
			//System.out.println("用户松开了鼠标");
		}
		
		lastMouseState=buttonDown;
	}
}
