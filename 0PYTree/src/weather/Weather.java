package weather;

import java.awt.Color;
import java.awt.Graphics;

import main.Observer;
import main.Plan;
import weathermap.WeatherInput;

public class Weather {

	public static Color shiftColor=new Color(50,50,100);
	private static Color targetColor=shiftColor;
	public static int fadePower=0;
	
	private Observer ob;
	private Wind wind;
	private int state=0; 
	public static final int knot=0,raining=1,snowing=2;
	private Cloud cloud;
	private Rain rain;
	private Snow snow;
	
	public static int skewx=400,skewy=100;
	
	boolean tog=true;
	
	public Weather(Observer ob,float force,double direction,float speed) {
		this.ob=ob;
		wind=new Wind(this,force,direction,speed);
		this.cloud=new Cloud(this);
		this.rain=new Rain(this);
		this.snow=new Snow(this);
	}
	public void update() {
		changeState();
		
		wind.update();
		cloud.update();
		if(ob.getKM().kSP) {
			if(tog) {
			state++;
			if(state>2)state=0;
			tog=false;}
		}else tog=true;
		
		int shiftSpd=3;
		int target;
		switch(state) {
		case raining:
			shiftColor=new Color(0,0,60);
			target=40;
			
			break;
		case snowing:
			shiftColor=new Color(170,170,210);
			target=50;
		
			break;
		default:
			target=0;
		}
		rain.update();
		snow.update();
		
		if(fadePower<target) {
			fadePower+=shiftSpd;
			if(fadePower>target)fadePower=target;
		}
		else if(fadePower>target){
			fadePower-=shiftSpd;
			if(fadePower<target)fadePower=target;
		}
	}
	public void render(Graphics g) {
		wind.render(g);
		if(Plan.debug) {
			g.setColor(new Color(230,160,160));
			for(int a=0;a<state;a++) {
				g.drawRect(skewx+2+(a*6), skewy-6, 4, 4);
			}
		}
		rain.render(g);
		snow.render(g);
		
	}
	public void renderC(Graphics g) {
		cloud.render(g);
	}
	public Wind getWind() {return wind;}
	public int getState() {return state;}
	public void setState(int to) {
		this.state=to;
		if(to==1) {
			rain.setDensity(1);
		}else if(to==2) {
			snow.setDensity(1);
		}
	}
	public Cloud getCloud() {return this.cloud;}
	public Rain getRain() {return this.rain;}
	public Observer getObserver() {return this.ob;}
	
	private void changeState() {

		if(WeatherInput.active==false) {
			switch(state) {
			case raining:
				if((int)(Math.random()*1500)==0) {
					if(rain.getDensity()<=3) {
						setState(snowing);
					}else {
						rain.setDensity(rain.getDensity()-1);
					}
				}
				break;
			case snowing:
				if((int)(Math.random()*1500)==0) {
					if(snow.getDensity()<=3) {
						setState(knot);
					}else {
						snow.setDensity(rain.getDensity()-1);
					}
				}
				break;
			default:
				if((int)(Math.random()*2000)==0) {
					setState(raining);
				}
			}
		}
		else {
			switch(state) {
			case raining:
				rain.setDensity((int)ob.getWeathermap().getMap(raining));
				break;
			case snowing:
				rain.setDensity((int)ob.getWeathermap().getMap(snowing));
				break;
			default:
			}
		}
	}
}
