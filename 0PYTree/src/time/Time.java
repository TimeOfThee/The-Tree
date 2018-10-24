package time;

import java.awt.Color;
import java.awt.Graphics;

import main.Observer;
import main.Plan;

public class Time {
	
	private float minview;
	private int minute,hour,quarter,timer=0;
	private Observer ob;
	
	public static float change;//in update
	
	private float rate=1f;
	float toslow=0.3f;
	float tofast=3f;
	
	int temtim=0;
	
	public Time(Observer ob) {
		this.ob=ob;
		this.minute=0;
		this.minview=0;
		if(Plan.debug) {
			tofast=7f;
			toslow=2f;
			rate=3f;
		}
	}
	public void update() {
		temtim+=6;
		if(temtim>360)temtim-=360;
		
		if(timer>0) {
			change=tofast;
			timer--;
		}else {
			change=toslow;
		}
		
		minview+=rate;
		if(minview>=1440)minview-=1440;
		minute=(int)minview;
		hour=0;
		for(int check=minute;check>=60;check-=60) {
			hour++;
		}
		if(hour>=18)quarter=3;
		else if(hour>=12)quarter=2;
		else if(hour>=6)quarter=1;
		else quarter=0;
	}
	public void render(Graphics g) {
		int msiz=20;
		g.setColor(Color.white);
		//minute
		g.drawOval(800-(20+msiz*4), 10, msiz*2, msiz*2);
		g.drawLine( (int)(800-(20+msiz*3)), (int)(10+msiz), (int)(800-(20+msiz*3)+Observer.findCosSin(getTimePer()*360/100, msiz)[0]), (int)(10+msiz+Observer.findCosSin(getTimePer()*3.6, msiz)[1]));
		//second
		g.drawLine(800-(int)(10+msiz-Observer.findCosSin(temtim, msiz)[0]), (int)(10+msiz-Observer.findCosSin(temtim, msiz)[1]), 800-(int)(10+msiz+Observer.findCosSin(temtim, msiz)[0]), (int)(10+msiz+Observer.findCosSin(temtim, msiz)[1]));
		g.drawOval(800-(10+msiz*2), 10, msiz*2, msiz*2);
		g.drawLine(800-15, 10+msiz, 800-10, 10+msiz);
		if(temtim<=90) {
			g.setColor(new Color(255,255,255,200-(temtim*(200/90))));
			g.drawOval(800-(10+msiz*2)-(temtim/9), 10-(temtim/9), (msiz*2)+((temtim/9)*2), (msiz*2)+((temtim/9)*2));
		}
	}
	
	public int getMinute() {
		return this.minute;
	}
	public int getHour() {		
		return this.hour;
	}
	public int getQuarter() {
		return this.quarter;
	}
	public void setTime(int to) {
		this.minview=to;
		if(minview>1439)this.minview=1439;
		else if(minview<0)this.minview=0;
		timer=60;
	}
	public double getTimePer() {
		//System.out.println((double)minute*100/1439);
		return (double)minute*100/1439;
	}
}
