package weather;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import main.Observer;
import main.Plan;
import tree.Segment;
import tree.Tree;

public class Rain {

	private Observer ob;
	private Weather mother;
	private Wind wi;
	private int density;
	private Color rainClor;
	private ArrayList<float[]> drops;
	int x=0,y=1,speed=2,ang=3,alive=4,skr=5,skg=6,skb=7,spl1=8,spl2=9,spl3=10,spl4=11;
	int dying=5;
	
	int maxden=10,delay=0;
	
	public Rain(Weather mother) {
		this.mother=mother;
		this.ob=mother.getObserver();
		this.wi=mother.getWind();
		this.drops=new ArrayList<float[]>();
		this.density=1;
	}
	public void update() {
		rainClor=Observer.skewColor(mother.getCloud().getCloudColor(),-10,30,60);
		
		if(mother.getState()==Weather.raining) {
			if((int)(Math.random()*70)==0) {
				density+=(int)(Math.random()*3)-1;
				if(density<1)density=1;
				else if(density>maxden)density=maxden;
			}
			
			if(delay==0) {
				if(ob.getKM().kDO) {
					if(density<maxden) {
						density++;
						delay=2;
					}
				}else if(ob.getKM().kUP) {
					if(density>1) {
						density--;
						delay=2;
					}
				}
			}else delay--;
			
			if(density>=8) {
				if( (int)(Math.random()*350)==0 ) {
					ob.getGround().thunder();
				}
			}
			
			makeDrop(density);
		}
		
		Iterator<float[]> it=drops.iterator();
		while(it.hasNext()) {
			float[] d=it.next();
			
			drop(d);
			
			if(d[y]<-100 || d[y]>700 || d[alive]<=0)it.remove();
			else if(d[ang]>90 && d[ang]<=270 && d[x]<-100)it.remove();
			else if((d[ang]<=90 || d[ang]>270) && d[x]>900)it.remove();
		}
	}
	public void render(Graphics g) {
		
		int skx=Weather.skewx, sky=Weather.skewy;
		if(Plan.debug && mother.getState()==Weather.raining) {
			g.setColor(new Color(230,160,160));
			double angle=90;
			int dir=(int)(Observer.findDif(90, wi.getDirection()));
			if(dir!=0) {
				dir=dir/Math.abs(dir);
			}
			angle+=dir*(Math.abs(Observer.findDif(90, wi.getDirection()))*wi.getForcePer()/200);
			
			g.drawLine(skx, sky, skx+(int)Observer.findCosSin(angle, 30)[0], sky+(int)Observer.findCosSin(angle, 30)[1]);
			
			g.drawLine(skx-50-1, sky-11, skx-50-1, sky-21);
			g.drawLine(skx-50+((maxden-1)*10)+1, sky-11, skx-50+((maxden-1)*10)+1, sky-21);
			g.drawLine(skx-50+((density-1)*10), sky-11, skx-50+((density-1)*10), sky-21);
			g.setColor(new Color(160,80,100));
			g.drawString(Integer.toString((int)angle), skx+(int)(Observer.findCosSin(angle, 30)[0]/2), sky+(int)(Observer.findCosSin(angle, 30)[1]/2));
		}
		
		for(float[] d:drops) {
			int red=(int)(rainClor.getRed()+d[skr]);
			int gre=(int)(rainClor.getGreen()+d[skg]);
			int blu=(int)(rainClor.getBlue()+d[skb]);
			if(red>255)red=255;
			else if(red<0)red=0;
			if(gre>255)gre=255;
			else if(gre<0)gre=0;
			if(blu>255)blu=255;
			else if(blu<0)blu=0;
			
			Color set=new Color(red,gre,blu);
			g.setColor(Observer.skewColor(set, Weather.shiftColor, (int)(Weather.fadePower*0.75)));
		
			if(d[x]>-10 && d[x]<810 && d[y]>-10 && d[y]<610){
				if(d[alive]>dying) {
					g.drawLine((int)d[x], (int)d[y], (int)(d[x]-Observer.findCosSin(d[ang], d[speed]*0.5)[0]), (int)(d[y]-Observer.findCosSin(d[ang], d[speed]*0.5)[1]));
					g.fillOval((int)d[x]-1, (int)d[y]-1, 3, 3);
				}else { 
					g.fillOval((int)(d[x]-((dying-d[alive])*d[spl1])), (int)(d[y]-((dying-d[alive]))*d[spl2])-1, 2, 2);
					g.fillOval((int)(d[x]+((dying-d[alive])*d[spl3])), (int)(d[y]-((dying-d[alive]))*d[spl4])-1, 2, 2);
				}
			}
		}
		
	}
	private void makeDrop(int amount) {
		
		double angle=90;
		int dir=(int)(Observer.findDif(90, wi.getDirection()));
		if(dir!=0) {
			dir=dir/Math.abs(dir);
		}
		angle+=dir*(Math.abs(Observer.findDif(90, wi.getDirection()))*wi.getForcePer()/200);
		
		double xl=650/Math.tan(Math.toRadians(180-angle))-50;
		double xm=900;
		if(angle>90) {
			xm=xl+900;
			xl=-50;
		}
		
		//System.out.println(xl+" "+xm);
		for(int a=0;a<amount;a++) {
			
			int x=(int)xm;
			x=(int)(Math.random()*(xm-xl)+xl);
			
			int y=(int)(-100+(Math.random()*80));
			float[] add=new float[] {x,y,30,(float)angle,dying+1,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,
					(float)((int)(Math.random()*5)+8)/10,
					(float)((int)(Math.random()*5)+8)/10,
					(float)((int)(Math.random()*5)+8)/10,
					(float)((int)(Math.random()*5)+8)/10};
			fixAngle(add);
			drops.add(add);
		}
	}
	private void drop(float[] drop){
		if(drop[alive]<=dying) {
			if(drop[alive]>0)drop[alive]--;
		}
		else {
			
			float spd=(float)(mother.getWind().getForcePer()*8/100);
			spd+=drop[speed];
			
			fixAngle(drop);
			drop[x]+=Observer.findCosSin(drop[ang], spd)[0];
			drop[y]+=Observer.findCosSin(drop[ang], spd)[1];
			
			float[] dropLine;
			if(drop[ang]==90) {
				dropLine=new float[] {
						drop[0],drop[1],drop[0]-spd,(float)(drop[1]-Observer.findCosSin(drop[ang], spd*2)[1])};
			}else {
				dropLine=new float[] {
						drop[0],drop[1],(float)(drop[0]-Observer.findCosSin(drop[ang], spd*2)[0]),(float)(drop[1]-Observer.findCosSin(drop[ang], spd*2)[1])};
			}
			
			boolean cancel=false;
			for(float[] dirt:ob.getDirt()) {
				double[] intersection=Observer.intersectsAt(dropLine,new float[] {
							dirt[0],dirt[1],dirt[0]+dirt[2],dirt[1]});
				if(intersection!=null) {
					cancel=true;
					drop[x]=(float)intersection[0];
					drop[y]=(float)intersection[1];
					drop[alive]--;
				}
			}
			if(!cancel) {
				for(Tree t:ob.getTrees()) {
					for(Segment bark:t.getBark()) {
						double[] intersection=Observer.intersectsAt(dropLine,bark.getLine());
						if(intersection!=null  && (int)(Math.random()*2)!=0) {
							cancel=true;
							drop[x]=(float)intersection[0];
							drop[y]=(float)intersection[1];
							drop[alive]--;
						}
					}
				}
			}
		}
	}
	
	private void fixAngle(float[] drop) {
		double angle=90;
		int dir=(int)(Observer.findDif(90, wi.getDirection()));
		if(dir!=0) {
			dir=dir/Math.abs(dir);
		}
		angle+=dir*(Math.abs(Observer.findDif(90, wi.getDirection()))*wi.getForcePer()/200);
		angle+=(int)(Math.random()*7)-3;
		drop[ang]=(int)angle;
	}
	public int getDensity() {return this.density;}
	public void setDensity(int to) {
		this.density=to;
		if(this.density>maxden)this.density=maxden;
		else if(this.density<1)this.density=1;
	}
}
