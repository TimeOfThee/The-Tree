package weather;

import java.awt.Graphics;
import java.util.ArrayList;

import main.Observer;
import tree.Segment;

public class Clump {

	private float[] point;//x,y,height
	private Snow mother;
	private Segment root;
	private int delay;
	private boolean alive;
	private int maxH;//6
	
	float rate=0.1f;
	public static float extend=2f;
	
	public Clump(Snow mother,float x,float y) {
		this.mother=mother;
		this.point=new float[] {(int)x,(int)y,rate};
		this.root=null;
		this.delay=20;
		this.alive=true;
		this.maxH=4+(int)(Math.random()*5)-2;
	}
	public void update() {
		if(delay<=0) {
			if(mother.getWeather().getState()!=Weather.snowing) {
				point[2]-=rate;
			}
			
			if(point[2]<=0) {
				alive=false;
			}
			delay=10;
		}else {
			delay--;
		}
	}
	public void render(Graphics g) {
		g.setColor(Observer.skewColor(mother.getWeather().getCloud().getCloudColor(),40,70,80));
		int[] xs=new int[] {
				(int)(point[0]-(point[2]*extend)),
				(int)point[0],
				(int)point[0],
				(int)(point[0]+(point[2]*extend)),
				(int)point[0]
		};
		if(mother.getWi().getDirection()>90 && mother.getWi().getDirection()<=270) {//left
			xs[1]=(int)( point[0]-(mother.getWi().getForcePer()/100*extend*2)-(extend*1.4) );
			xs[2]=(int)( point[0]-(mother.getWi().getForcePer()/100*extend*2)+(extend*1.4) );
		}else {
			xs[1]=(int)( point[0]+(mother.getWi().getForcePer()/100*extend*2)-(extend*1.4) );
			xs[2]=(int)( point[0]+(mother.getWi().getForcePer()/100*extend*2)+(extend*1.4) );
		}
		
		int[] ys=new int[] {
				(int)point[1],
				(int)(point[1]-point[2]),
				(int)(point[1]-point[2]),
				(int)point[1],
				(int)(point[1]+(point[2]/2)),
		};
		g.fillPolygon(xs,ys,5);
	}
	public void grow() {
		if(this.point[2]<maxH) {
			this.point[2]+=(Math.random()*0.5)+rate;
		}
	}
	public float[] getPoint() {
		return this.point;
	}
	public Segment getRoot() {
		return this.root;
	}
	public boolean isAlive() {return alive;}
}
