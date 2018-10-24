package main;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import Land.Ground;
import time.Time;
import tree.Segment;
import tree.Tree;
import treeBase.LeafBase;
import treeBase.TreeBase;
import treeBase.TrunkBase;
import weather.Weather;
import weather.Wind;

public class Plan {
	
	public static boolean debug=false;
	
	private KeyManager kM;
	private MouseManager mM;
	private Observer ob;
	public static ArrayList<int[]> pivot=new ArrayList<int[]>();
	private ArrayList<Tree> trees=new ArrayList<Tree>();
	private Ground ground;
	
	private Weather weather;
	
	private Time time;
	private int resetTimer=7000;
	
	//put variables here
	
	int flash=0;
	int ex=400,wy=490;
	int set=0;
	boolean timeTog=true;
	
	public Plan(KeyManager km,MouseManager mm) {
		this.kM=km;
		this.mM=mm;
		
		this.ob=new Observer(this);
		this.time=new Time(ob);
		this.trees.add(new TreeBase(ob, ex, wy));
		//trees.add(new TreeBase(ob, 300, 500));
		
		this.weather=new Weather(ob,10,0,30);
		
		this.ground=new Ground(ob,ex,wy);
	}
	
	public void update() {
		//update variables here
		if(!debug) {
			if(resetTimer>0) {
				resetTimer--;
				System.out.println(resetTimer);
			}else {
				resetTimer=7000;
				this.trees=new ArrayList<Tree>();
				this.trees.add(new TreeBase(ob, ex, wy));
				flash=255;
			}
		}
		if(flash>0) {
			flash-=20;
			if(flash<0)flash=0;
		}
		time.update();
		
		if(kM.kEN) {
			if(timeTog) {
				time.setTime(60*(24/4)*set);
				set++;
				if(set>3)set=0;
				timeTog=false;
				flash=255;
			}	
		}else {
			timeTog=true;
		}
		
		if(kM.kP) {
			this.trees=new ArrayList<Tree>();
			this.trees.add(new TreeBase(ob, ex, wy));
			flash=255;
		}
		
		//ground
		ground.update();
		
		//tree
		for(Tree t:trees) {
			t.update();
		}
		
		//weather
		weather.update();
		
		for(int a=pivot.size()-1;a>=0;a--) {
			if(pivot.get(a)[4]<=0) {
				pivot.remove(pivot.get(a));
			}else {pivot.get(a)[4]--;}
		}
	}
	public void render(Graphics g) {
		//draw here
		
		//ground
		ground.render(g);
		//wind[clouds]
		weather.renderC(g);
		//tree
		for(Tree t:trees) {
			t.render(g);
		}
		
		//wind
		weather.render(g);
		
		for(int[] l:pivot) {
			int tran=25*l[4]; 
			if(tran>255)tran=255;
			g.setColor(new Color(10,10,10,tran));//setback to 150
			if(l.length==5) {
				g.drawLine(l[0],l[1], l[2], l[3]);
			}
			else if(l.length==6) {
				if(l[5]==0) {
					g.drawOval(l[0], l[1], l[2], l[3]);
				}else {
					g.fillOval(l[0], l[1], l[2], l[3]);
				}
			}
			else if(l.length==7) {
				g.drawArc(l[0], l[1], l[2], l[3], l[5], l[6]);
			}
		}
		
		if(debug)time.render(g);
		
		g.setColor(new Color(200,200,255,flash));
		g.fillRect(0, 0, 800, 600);
	}
	public static void pivotLine(int x,int y,int ex, int wy,int health) {
		pivot.add(new int[] {x,y,ex,wy,health});
	}
	public static void pivotArc(int x,int y,int ex, int wy,int health,int ang,int ang2) {
		pivot.add(new int[] {x,y,ex,wy,health,ang,ang2});
	}
	public MouseManager getMM() {return mM;}
	public KeyManager getKM() {return kM;}
	public Weather getWeather() {return weather;}
	public Wind getWind() {return this.weather.getWind();}
	public Time getTime() {return this.time;}
	public float[][] getDirt(){return this.ground.getDirt();}
	public Ground getGround(){return this.ground;}
	public ArrayList<Tree> getTrees(){return this.trees;}
}
