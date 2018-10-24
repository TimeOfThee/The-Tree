package weather;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import main.KeyManager;
import main.MouseManager;
import main.Observer;
import main.Plan;

public class Wind {

	private Observer ob;
	private float force,speed;
	private double direction;
	private ArrayList<Gust> gusts;
	
	private Color windClor=new Color(200,200,250);
	
	private float[] pointer,origin=new float[] {0,0},change=new float[] {0,0},target=new float[] {0,0};
	private float maxx=200,maxy=30;
	
	private Weather mother;
	
	ArrayList<float[]> gustRen=new ArrayList<float[]>();
	int timer=0;
	
	public Wind(Weather mother,float force,double direction,float speed) {
		this.mother=mother;
		this.ob=mother.getObserver();
		this.force=force;
		this.direction=direction;
		
		this.pointer=new float[] {(float)Observer.findCosSin(direction, force)[0],(float)Observer.findCosSin(direction, force)[1]};
		this.speed=speed;
		this.gusts=new ArrayList<Gust>();
	}
	public void update() {
		changeWind();
		windClor=mother.getCloud().getCloudColor();
		force=(float)Observer.findHypotenuse(origin[0], origin[1], pointer[0], pointer[1]);
		direction=Observer.findAngle(origin[0], origin[1], pointer[0], pointer[1]);
		
		float per=(force*100)/200;
		
		if((int)(Math.random()*200)<=per+20) {
			float x=(float)(400-Observer.findCosSin(direction, 400)[0]);
			float y=(float)((350+(int)(Math.random()*101)-50)-Observer.findCosSin(direction, 400)[1]);
			gusts.add(new Gust(ob,this,x,y));
		}
		
		Iterator<Gust> it=gusts.iterator();
		while(it.hasNext()) {
			Gust gu=it.next();
			gu.update();
			if(gu.getEnd()[0]>800 || gu.getEnd()[0]<0 || gu.getSpeed()<30 ) {
				it.remove();
			}
		}
		
		timer++;
		if(timer>=(100-per)/4) {
			if(mother.getState()==Weather.knot) {
				float x=(int)(Math.random()*1000)-100;
				float y=(int)(Math.random()*800)-100;
				gustRen.add(new float[]{x,y,x,y,60});
			}
			timer=0;
		}
		
		for(int a=gustRen.size()-1;a>=0;a--){
			float[] gu=gustRen.get(a);
			gu[2]=(gu[2]+gu[0])/2;
			gu[3]=(gu[3]+gu[1])/2;
			
			float speed=30-(int)(Math.abs(30-gu[4]));
			gu[4]--;
			
			gu[0]+=Observer.findCosSin(direction+(int)(Math.random()*21)-10, speed*(force/50))[0];
			gu[1]+=Observer.findCosSin(direction+(int)(Math.random()*21)-10, speed*(force/50))[1];
			
			if(gu[2]>900 || gu[2]<-100 || gu[3]>700 || gu[3]<-100 || gu[4]<=0) {
				gustRen.remove(gu);
			}
		}
	}
	public void render(Graphics g) {
	
		if(Plan.debug) {
			int skx=Weather.skewx,sky=Weather.skewy;
			g.setColor(new Color(230,160,160));
			g.drawOval((int)(pointer[0]-2)+skx, (int)(pointer[1]-2)+sky, 4, 4);
			g.drawOval((int)(target[0]-2)+skx, (int)(target[1]-2)+sky, 4, 4);
			g.drawLine(skx, sky, (int)(pointer[0]+skx), (int)(pointer[1]+sky));
			g.drawLine(skx, sky, skx, (int)(sky+maxy));
			g.drawLine((int)(skx-maxx), sky, (int)(skx+maxx), sky);
			g.setColor(new Color(160,80,100));
			g.drawString(Integer.toString((int)force),(int)(skx+Observer.findCosSin(direction, force)[0]/2)-14,(int)(sky+Observer.findCosSin(direction, force)[1]/2));
			g.drawString(Integer.toString((int)getForcePer())+"%",(int)(skx+Observer.findCosSin(direction, force)[0]/2)-14,(int)(sky+Observer.findCosSin(direction, force)[1]/2)+10);
		}
		
		/*for(Gust gu:gusts) {
			gu.render(g);
		}*/
		g.setColor(windClor);
		for(float[] gu:gustRen) {
			g.drawLine((int)gu[0], (int)gu[1], (int)gu[2], (int)gu[3]);
		}		
	}
	public void changeWind() {
		if((int)(Math.random()*900)==0) {
			float x=pointer[0]+(int)((Math.random()*201)-100);
			float y=pointer[1]+(int)((Math.random()*201)-100);
			if(x<-maxx)x=-maxx;
			else if(x>maxx)x=maxx;
			if(y<0)y=0;
			else if(y>maxy)y=maxy;
			target=new float[] {x,y};
		}
		
		float dcc=0.1f;
		float acc=1f;
		
		if((int)(Math.random()*100)<=0) {
			if((int)(Math.random()*2)==0) {
				change[0]+=acc+(int)(Math.random()*acc);
			}else {
				change[0]-=acc+(int)(Math.random()*acc);
			}
			if((int)(Math.random()*2)==0) {
				change[1]+=acc+(int)(Math.random()*acc);
			}else {
				change[1]-=acc+(int)(Math.random()*acc);
			}
			if(pointer[0]>target[0]+1) {
				change[0]-=acc*0.75;
			}else if(pointer[0]<target[0]-1) {
				change[0]+=acc*0.75;
			}
			if(pointer[1]>target[1]+1) {
				change[1]-=acc*0.75;
			}else if(pointer[1]<target[1]-1) {
				change[1]+=acc*0.75;
			}  
		}else {
			if(change[0]>dcc) {
				change[0]-=dcc;
			}else if(change[0]<-dcc) {
				change[0]+=dcc;
			}else {
				change[0]=0;
			}
			
			if(change[1]>dcc) {
				change[1]-=dcc;
			}else if(change[1]<-dcc) {
				change[1]+=dcc;
			}else {
				change[1]=0;
			}
		}
		
		if(ob.getKM().kD) {
			pointer[0]+=2;
		}if(ob.getKM().kA) {
			pointer[0]-=2;
		}if(ob.getKM().kS) {
			pointer[1]+=2;
		}if(ob.getKM().kW) {
			pointer[1]-=2;
		}
		
		pointer[0]+=change[0];
		pointer[1]+=change[1];
		
		if(pointer[0]>maxx) {
			pointer[0]=maxx;
			change[0]=0;
		}else if(pointer[0]<-maxx) {
			pointer[0]=-maxx;
			change[0]=0;
		}
		if(pointer[1]>maxy) {
			pointer[1]=maxy;
			change[1]=0;
		}else if(pointer[1]<0) {
			pointer[1]=0;
			change[1]=0;
		}
	}
	public float getForce() {return force;}
	public double getForcePer() {
		return (force*100)/Observer.findHypotenuse(0, 0, maxx, maxy);
	}
	public void setForce(float to) {this.force=to;}
	public float getSpeed() {return speed;}
	public void setSpeed(float to) {this.speed=to;}
	public double getDirection() {return direction;}
	public void setDirection(double to) {this.direction=to;}
	public ArrayList<Gust> getGusts(){return gusts;}
	
	public Color getWindClor() {return windClor;}
}
