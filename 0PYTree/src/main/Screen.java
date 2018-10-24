package main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Screen implements Runnable{

	private Plan plan;
	private Display display;
	private int width,height;
	public String title;
	private boolean running=false;
	private Thread thread;
	
	private BufferStrategy bs;
	private Graphics g;
	public Color back;
	
	private KeyManager keyM;
	private MouseManager mouM;
	
	public Screen(String title,int width,int height) {
		this.width=width;
		this.height=height;
		this.title=title;
		keyM=new KeyManager();
		mouM=new MouseManager();
		back=(new Color(200,200,200));
		
		this.plan=new Plan(keyM,mouM);
	}
	public void run() {
		init();
		int fps=60;
		double timePerTick=1000000000/fps;
		double delta=0;
		long now;
		long lastTime=System.nanoTime();
		long timer=0;
		int ticks=0;
		
		while(running) {
			now=System.nanoTime();
			delta+=(now-lastTime)/timePerTick;
			timer+=now-lastTime;
			lastTime=now;
				
			if(delta>=1) {
				update();//----------------------------------
				render();
				ticks++;
				delta--;
			}
			if (timer>1000000000) {
				//System.out.println(ticks);
				ticks=0;
				timer=0;
			}
		}
			
		stop();
			
	}
	public synchronized void start() {
		if(running)return;//stops the game from running if already running
		running=true;
		thread=new Thread(this);
		thread.start();//.start will call run()
	}
	public synchronized void stop() {
		if(!running)return;//stops the game from stopping if already stopped
		running=false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void init() {
		display=new Display(title,width,height);
		display.getJFrame().addKeyListener(keyM);
		display.getJFrame().addMouseListener(mouM);
		display.getJFrame().addMouseMotionListener(mouM);
		display.getCanvas().addMouseListener(mouM);
		display.getCanvas().addMouseMotionListener(mouM);
	}
	
	private void update() {//--------------------------------------------------------
		keyM.update();
		plan.update();
	}
	private void render() {//------------------------------------------------------------------------
		bs=display.getCanvas().getBufferStrategy();
		if(bs==null) {
			display.getCanvas().createBufferStrategy(3);
			return;//return if you don't want errors
		}
		g=bs.getDrawGraphics();
		
		g.clearRect(0,0,width,height);
		backFill();
		
		//draw here{
		plan.render(g);
		//}
		
		bs.show();//reveal the buffers
		g.dispose();//gets rid of the graphics object properly
	}
	public void backFill() {
		g.setColor(back);
		g.fillRect(0,0,width,height);
	}
}
