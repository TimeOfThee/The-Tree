package weather;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import main.Observer;
import main.Plan;
import tree.Segment;
import tree.Tree;

public class Snow {

	private Observer ob;
	private Weather mother;
	private Wind wi;
	private int density;
	private Color snowClor;
	private ArrayList<float[]> flakes;
	int x=0,y=1,speed=2,ang=3,skewloc=4,alive=5;
	
	int maxden=10,delay=0;
	
	private ArrayList<Clump> clumps;
	
	public Snow(Weather mother) {
		this.mother=mother;
		this.ob=mother.getObserver();
		this.wi=mother.getWind();
		this.flakes=new ArrayList<float[]>();
		this.density=1;
		this.clumps=new ArrayList<Clump>();
	}
	public void update() {
		snowClor=Observer.skewColor(mother.getCloud().getCloudColor(),50,80,90);
		
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
		
		if(mother.getState()==Weather.snowing) {
			makeFlake(density);
		}
		
		Iterator<float[]> it=flakes.iterator();
		while(it.hasNext()) {
			float[] d=it.next();
			
			if(d[alive]<=0) {
				it.remove();
			}else {
				drop(d);
				if(d[y]<-100 || d[y]>700)it.remove();
				else if(d[ang]>90 && d[ang]<=270 && d[x]<-100)it.remove();
				else if((d[ang]<=90 || d[ang]>270) && d[x]>900)it.remove();
			}	
		}
		//clumps
		Iterator<Clump> itt=clumps.iterator();
		while(itt.hasNext()) {
			Clump c=itt.next();
			
			if(!c.isAlive()) {
				itt.remove();
			}else {
				c.update();
			}
			
		}
	}
	public void render(Graphics g) {
		
		int skx=Weather.skewx, sky=Weather.skewy;
		if(Plan.debug && mother.getState()==Weather.snowing) {
			g.setColor(new Color(230,160,160));
			double angle=specificAngle();
			
			g.drawLine(skx, sky, skx+(int)Observer.findCosSin(angle, 30)[0], sky+(int)Observer.findCosSin(angle, 30)[1]);
			
			g.drawLine(skx-50-1, sky-11, skx-50-1, sky-21);
			g.drawLine(skx-50+((maxden-1)*10)+1, sky-11, skx-50+((maxden-1)*10)+1, sky-21);
			g.drawLine(skx-50+((density-1)*10), sky-11, skx-50+((density-1)*10), sky-21);
			g.setColor(new Color(160,80,100));
			g.drawString(Integer.toString((int)angle), skx+(int)(Observer.findCosSin(angle, 30)[0]/2), sky+(int)(Observer.findCosSin(angle, 30)[1]/2));
		}
		
		for(float[] f:flakes) {
			
			Color set=snowClor;
			g.setColor(Observer.skewColor(set, Weather.shiftColor, (int)(Weather.fadePower*0.75)));
		
			if(f[x]>-10 && f[x]<810 && f[y]>-10 && f[y]<610){
				if(f[alive]>0) {
					g.fillOval((int)f[x]-2, (int)f[y]-2, 4, 3);
				}
			}
		}
		
		//clumps
		Iterator<Clump> it=clumps.iterator();
		while(it.hasNext()) {
			Clump c=it.next();
			
			if(!c.isAlive()) {
				it.remove();
			}else {
				c.render(g);
			}
			
		}
	}
	private void makeFlake(int amount) {
		
		double angle=specificAngle();
		
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
			float[] add=new float[] {x,y,(int)(Math.random()*9)+5,(float)angle,1,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,
					(float)((int)(Math.random()*5)+8)/10,
					(float)((int)(Math.random()*5)+8)/10,
					(float)((int)(Math.random()*5)+8)/10,
					(float)((int)(Math.random()*5)+8)/10};
			fixAngle(add);
			flakes.add(add);
		}
	}
	private void drop(float[] flake){
		float spd=(float)(mother.getWind().getForcePer()*8/100);
		spd+=flake[speed];
		
		fixAngle(flake);
		flake[x]+=Observer.findCosSin(flake[ang], spd)[0];
		flake[y]+=Observer.findCosSin(flake[ang], spd)[1];
		
		float[] dropLine;
		if(flake[ang]==90) {
			dropLine=new float[] {
				flake[0],flake[1],flake[0]-spd,(float)(flake[1]-Observer.findCosSin(flake[ang], spd*2)[1])};
		}else {
			dropLine=new float[] {
				flake[0],flake[1],(float)(flake[0]-Observer.findCosSin(flake[ang], spd*2)[0]),(float)(flake[1]-Observer.findCosSin(flake[ang], spd*2)[1])};
		}
		
		boolean cancel=false;
		double[] intersection=null;
		for(float[] dirt:ob.getDirt()) {
			double[] test=Observer.intersectsAt(dropLine,new float[] {
						dirt[0],dirt[1],dirt[0]+dirt[2],dirt[1]});
			if(test!=null) {
				if(intersection!=null) {
					if(test[1]<intersection[1]) {
						intersection=test.clone();
					}
				}else {
					intersection=test.clone();
				}
			}
			
			if(intersection!=null) {
				cancel=true;
				flake[x]=(float)intersection[0];
				flake[y]=(float)intersection[1];
				flake[alive]--;
				addClump((float)intersection[0],(float)intersection[1]);
				break;
			}
		}
		if(!cancel) {
			intersection=null;
			for(Tree t:ob.getTrees()) {
				for(Segment bark:t.getBark()) {
					if(bark.equals(null))continue;
					else if(bark.getFrost()==100)continue;
					double[] test=Observer.intersectsAt(dropLine,bark.getLine());
					
					if(test!=null) {
						if(intersection!=null) {
							if(test[1]<intersection[1]) {
								intersection=test.clone();
							}
						}else {
							intersection=test.clone();
						}
					}
					
					
					
					if(intersection!=null  && (int)(Math.random()*((100-bark.getFrost())+2))!=0) {
						cancel=true;
						flake[x]=(float)intersection[0];
						flake[y]=(float)intersection[1];
						flake[alive]--;
						bark.setFrost(bark.getFrost()+1);
					}
				}
			}
		}
	}
	
	private void fixAngle(float[] drop) {
		double angle=specificAngle();
		
		angle+=(int)(Math.random()*21)-10;
		drop[ang]=(int)angle;
	}
	public int getDensity() {return this.density;}
	public void setDensity(int to) {
		this.density=to;
		if(this.density>maxden)this.density=maxden;
		else if(this.density<1)this.density=1;
	}
	public Weather getWeather() {
		return this.mother;
	}
	public Wind getWi() {return this.wi;}
	public Observer getOb() {return this.ob;}
	private double specificAngle() {
		double angle=90;
		int dir=(int)(Observer.findDif(90, wi.getDirection()));
		if(dir!=0) {
			dir=dir/Math.abs(dir);
		}
		angle+=dir*(Math.abs(Observer.findDif(90, wi.getDirection()))*wi.getForcePer()/150);
		
		return angle;
	}
	public void addClump(float x,float y) {
		for(Clump c:clumps) {
			if((int)c.getPoint()[1]==(int)y) {
				if( x>=(c.getPoint()[0]-(c.getPoint()[2]*Clump.extend/2)) && x<=(c.getPoint()[0]+(c.getPoint()[2]*Clump.extend/2)) ){
					c.grow();
					return;
				}
			}
		}
		clumps.add(new Clump(this,x,y));
	}
}