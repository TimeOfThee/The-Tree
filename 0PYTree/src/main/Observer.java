package main;

import java.awt.Color;
import java.util.ArrayList;

import Land.Ground;
import time.Time;
import tree.Segment;
import tree.Tree;
import weather.Gust;
import weather.Weather;
import weather.Wind;
import weathermap.WeatherInput;

public class Observer {

	private Plan plan;
	private WeatherInput weathermap;
	
	public Observer(Plan plan) {
		this.plan=plan;
		this.weathermap=null;//set when ready
	}
	public static double findHypotenuse(float xs,float ys,float xe,float ye) {
		return Math.sqrt( (xe-xs)*(xe-xs)+(ye-ys)*(ye-ys) );
	}
	public static double findAngle(float xs,float ys,float xe,float ye) {
		double ang;
		if(xe-xs!=0) {
			ang= Math.toDegrees( Math.atan((ye-ys)/(xe-xs)) );
			if(xe<xs) {ang+=180;}
		}
		else {
			if(ye-ys<0)ang=270;else ang=90;
		}
		return ang;
	}
	public static double findDif(double angle,double angt) {
		double dif1=0,dif2=0;
		
		dif1=angt-angle;
		dif2=dif1-(360*(dif1/Math.abs(dif1)));
		
		if(dif1>360) dif1-=360;
		else if(dif1<-360)dif1+=360;
		
		if(dif2>360) dif2-=360;
		else if(dif2<-360)dif2+=360;
		
		boolean debug=false;
		
		if(Math.abs(dif2)<Math.abs(dif1)) {
			return dif2;
		}else {
			return dif1;
		}
	}
	/**
	 * 
	 * @param ang le to move by
	 * @param dis tance to move
	 * @return a point moved DIS away at an angle ANG along the [x axis,y axis]
	 */
	public static double[] findCosSin(double ang,double dis) {
		double cos=Math.cos( Math.toRadians(ang) )*dis;
		double sin=Math.sin( Math.toRadians(ang) )*dis;
		return new double[] {cos,sin};
	}
	public static float[] findSegLoc(Segment root,float per) {
		float dis=(root.getLength()/100)*per;
		float x=(float)(findCosSin(root.getAngle(),dis)[0])+root.getEnd(true)[0];
		float y=(float)(findCosSin(root.getAngle(),dis)[1])+root.getEnd(true)[1];
		return new float[] {x,y};
	}
	public static Color skewColor(Color c,int range) {
		int r=c.getRed()+(int)(Math.random()*(range*2)-range);
		int g=c.getGreen()+(int)(Math.random()*(range*2)-range);
		int b=c.getBlue()+(int)(Math.random()*(range*2)-range);
		
		if(r>255)r=255;
		else if(r<0)r=0;
		if(g>255)g=255;
		else if(g<0)g=0;
		if(b>255)b=255;
		else if(b<0)b=0;
		
		return new Color(r,g,b,c.getAlpha());
	}
	public static Color skewColor(Color c,int re,int gr,int bl) {
		int r=c.getRed()+re;
		int g=c.getGreen()+gr;
		int b=c.getBlue()+bl;
		
		if(r>255)r=255;
		else if(r<0)r=0;
		if(g>255)g=255;
		else if(g<0)g=0;
		if(b>255)b=255;
		else if(b<0)b=0;
		
		return new Color(r,g,b,c.getAlpha());
	}
	public static Color skewColor(Color c,Color by,int power) {
		if(power>100)power=100;
		else if(power<0)power=0;
		int re=c.getRed()+(int)((by.getRed()-c.getRed())*(power*0.01));
		int gr=c.getGreen()+(int)((by.getGreen()-c.getGreen())*(power*0.01));
		int bl=c.getBlue()+(int)((by.getBlue()-c.getBlue())*(power*0.01));
		return new Color(re,gr,bl,c.getAlpha());
	}
	public static boolean intersects(float[] l1,float[] l2) {
		
		if((l1[0] > l2[0] && 
			l1[0] > l2[2] && 
			l1[2] > l2[0] && 
			l1[2] > l2[2])
				||
		   (l1[0] < l2[0] && 
			l1[0] < l2[2] && 
			l1[2] < l2[0] && 
			l1[2] < l2[2])) {
			return false;
		}
		if((l1[1] > l2[1] && 
			l1[1] > l2[3] && 
			l1[3] > l2[1] && 
			l1[3] > l2[3])
				||
		   (l1[1] < l2[1] && 
			l1[1] < l2[3] && 
			l1[3] < l2[1] && 
			l1[3] < l2[3])) {
			return false;	
		}
		double sl1=(findCosSin(findAngle(l1[0],l1[1],l1[2],l1[3]),1)[1])
				/(findCosSin(findAngle(l1[0],l1[1],l1[2],l1[3]),1)[0])
			,sl2=(findCosSin(findAngle(l2[0],l2[1],l2[2],l2[3]),1)[1])
				/(findCosSin(findAngle(l2[0],l2[1],l2[2],l2[3]),1)[0]);
		
		double ix=( (l2[1] -sl2*l2[0]) - (l1[1] -sl1*l1[0]) ) / ( sl1-sl2 );
		double iy=sl1*ix+l1[1]-(sl1*l1[0]);
		
		if(isBetween(ix,l2[0],l2[2]) && isBetween(iy,l2[1],l2[3]) && isBetween(ix,l1[0],l1[2]) && isBetween(iy,l1[1],l1[3])) {
			return true;
		}else {
			return false;
		}
	}
public static double[] intersectsAt(float[] l1,float[] l2) {
		
		if((l1[0] > l2[0] && 
			l1[0] > l2[2] && 
			l1[2] > l2[0] && 
			l1[2] > l2[2])
				||
		   (l1[0] < l2[0] && 
			l1[0] < l2[2] && 
			l1[2] < l2[0] && 
			l1[2] < l2[2])) {
			return null;
		}
		if((l1[1] > l2[1] && 
			l1[1] > l2[3] && 
			l1[3] > l2[1] && 
			l1[3] > l2[3])
				||
		   (l1[1] < l2[1] && 
			l1[1] < l2[3] && 
			l1[3] < l2[1] && 
			l1[3] < l2[3])) {
			return null;	
		}
		double sl1=(findCosSin(findAngle(l1[0],l1[1],l1[2],l1[3]),1)[1])
				/(findCosSin(findAngle(l1[0],l1[1],l1[2],l1[3]),1)[0])
			,sl2=(findCosSin(findAngle(l2[0],l2[1],l2[2],l2[3]),1)[1])
				/(findCosSin(findAngle(l2[0],l2[1],l2[2],l2[3]),1)[0]);
		
		double ix=( (l2[1] -sl2*l2[0]) - (l1[1] -sl1*l1[0]) ) / ( sl1-sl2 );
		double iy=sl1*ix+l1[1]-(sl1*l1[0]);
		
		if(isBetween(ix,l2[0],l2[2]) && isBetween(iy,l2[1],l2[3]) && isBetween(ix,l1[0],l1[2]) && isBetween(iy,l1[1],l1[3])) {
			return new double[] {ix,iy};
		}else {
			return null;
		}
	}
	public static boolean isBetween(double look,double start,double end) {
		if(start<=end) {
			if(look>=start-0.1 && look<=end+0.1)return true;
			return false;
		}
		else {
			if(look>=end-0.1 && look<=start+0.1)return true;
			return false;
		}
	}
	public MouseManager getMM() {return plan.getMM();}
	public KeyManager getKM() {return plan.getKM();}
	public Wind getWind() {return plan.getWind();}
	public ArrayList<Gust> getGusts(){return getWind().getGusts();}
	public Time getTime() {return plan.getTime();}
	public float[][] getDirt() {return plan.getDirt();}
	public ArrayList<Tree> getTrees(){return plan.getTrees();}
	public Weather getWeather() {return plan.getWeather();}
	public Ground getGround() {return plan.getGround();}
	public WeatherInput getWeathermap() {return this.weathermap;}
}
