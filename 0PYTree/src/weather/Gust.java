package weather;

import java.awt.Color;
import java.awt.Graphics;

import main.Observer;

public class Gust {
	
	private Observer ob;
	private Wind mother;
	private float[] start;
	private float force,eRad,speed;
	private double direction;
	
	double skewD=10;
	
	public Gust(Observer ob,Wind mother,float x,float y) {
		this.ob=ob;
		this.mother=mother;
		this.force=(float)(mother.getForce()+(Math.random()*21)-10);//skew random;
		this.direction=mother.getDirection();
		this.start=new float[] {x,y};
		this.eRad=force/2;
	}
	public void update() {
		rotate((int)(Math.random()*3)-1);
		force=mother.getForce()+(int)(Math.random()*9)-4;
		speed=Math.abs(force);
		//speed=mother.getSpeed()+(int)(Math.random()*9)-4;
		move();
	}
	public void render(Graphics g) {
		g.setColor(mother.getWindClor());
		g.drawLine((int)start[0], (int)start[1], (int)(start[0]+Observer.findCosSin(direction, force)[0]), (int)(start[1]+Observer.findCosSin(direction, force)[1]));
		g.drawOval((int)(start[0]-eRad), (int)(start[1]-eRad), (int)(eRad*2), (int)(eRad*2));
	}
	public void move() {
		//skew direction
		this.start[0]+=Observer.findCosSin(direction, speed)[0]/4;
		this.start[1]+=Observer.findCosSin(direction, speed)[1]/4;
	}
	public void rotate(int by) {
		direction+=by;
		if(direction>360)direction-=360;
		else if(direction<0)direction+=360;
		
		if(Math.abs(Observer.findDif(mother.getDirection(), direction))>skewD) {
			if(Observer.findDif(mother.getDirection(), direction)>0)
				direction=mother.getDirection()+skewD;
			else direction=mother.getDirection()-skewD;
		}

		if(direction>360)direction-=360;
		else if(direction<0)direction+=360;
	}
	
	public float getForce() {
		return force;
	}
	public void setForce(float force) {
		this.force = force;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public double getDirection() {
		return direction;
	}
	public void setDirection(double direction) {
		this.direction = direction;
	}
	public float[] getEnd() {
		return this.start;
	}
	public void setEnd(float[] to) {
		this.start=to;
	}
	public float geteRad() {return eRad;}
}
