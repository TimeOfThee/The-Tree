package tree;

import java.awt.Color;
import java.awt.Graphics;

import main.Observer;
import main.Plan;
import time.Time;

public abstract class Leaf {

	protected Observer ob;
	protected Tree mother;
	protected Segment root;
	protected float[] start;
	protected float loc,length,maxlen=10,growthRate=15;//loc=starting location on segment
	protected double angle,boundingangle;
	protected boolean big=false;
	protected float[] leafClor;
	protected Color[] leafs;
	protected int[] skewClor;
	protected Flower flower;
	
	//wind
	protected float wr=200;//wind resistance;
	
	public Leaf(Observer ob,Tree mother,Segment root,float loc,double ang,float length,Color[] leafs,int[] skewClor) {
		this.ob=ob;
		this.mother=mother;
		this.loc=loc;
		this.angle=ang;
		this.boundingangle=ang;
		this.length=length;
		this.leafs=leafs;
		int tem=ob.getTime().getQuarter()-1;
		if(tem<0)tem=3;
		Color temC=Observer.skewColor(this.leafs[tem], this.leafs[ob.getTime().getQuarter()],(this.ob.getTime().getHour()-(0+(6*this.ob.getTime().getQuarter())))*(100/6));
		this.leafClor=new float[] {temC.getRed(),temC.getGreen(),temC.getBlue()};
		this.skewClor=skewClor;
		this.root=root;
		this.start=Observer.findSegLoc(root, loc);
	}
	public Leaf(Observer ob,Tree mother,float x,float y,float loc,double ang,float length,Color[] leafs,int[] skewClor) {
		this.ob=ob;
		this.mother=mother;
		this.loc=loc;
		this.angle=ang;
		this.boundingangle=ang;
		this.length=length;
		this.leafs=leafs;
		int tem=ob.getTime().getQuarter()-1;
		if(tem<0)tem=3;
		Color temC=Observer.skewColor(this.leafs[tem], this.leafs[ob.getTime().getQuarter()],(this.ob.getTime().getHour()-(0+(6*this.ob.getTime().getQuarter())))*(100/6));
		this.leafClor=new float[] {temC.getRed(),temC.getGreen(),temC.getBlue()};
		this.skewClor=skewClor;
		this.root=null;
		this.start=new float[] {x,y};
	}
	
	public abstract void update();
	public abstract void render(Graphics g);
	public abstract void shadeRender(Graphics g,Color color);
	public abstract void grow();
	
	public Segment getRoot() {return root;}
	public void setRoot(Segment to) {this.root=to;}
	public Flower getFlower() {return flower;}
	public void setFlower(Flower to) {this.flower=to;}
	public float[] getEnd() {return start;}
	public void setEnd(float[] to) {this.start=to;}
	public boolean isAttached() {
		if(root!=null)return true;
		else return false;
	}
	
	protected void setTimeClor() {
		int to=this.ob.getTime().getQuarter()-1;
		if(to<0)to=3;
		
		Color base=this.leafs[to],coat=this.leafs[ob.getTime().getQuarter()];
		if(this.root!=null) {
			base=Observer.skewColor(base, mother.getFoliageFrostC(), (int)(this.root.getFrost()*0.33));
			coat=Observer.skewColor(coat, mother.getFoliageFrostC(), (int)(this.root.getFrost()*0.33));
		}
		
		Color target=Observer.skewColor(base, coat,(this.ob.getTime().getHour()-(0+(6*this.ob.getTime().getQuarter())))*(100/6));
		
		float re=this.leafClor[0],gr=this.leafClor[1],bl=this.leafClor[2];
		float change=Time.change;
		
		if(re>target.getRed()) {
			re-=change;
			if(re<target.getRed())re=target.getRed();
		}else if(re<target.getRed()) {
			re+=change;
			if(re>target.getRed())re=target.getRed();
		}
		if(gr>target.getGreen()) {
			gr-=change;
			if(gr<target.getGreen())gr=target.getGreen();
		}else if(gr<target.getGreen()) {
			gr+=change;
			if(gr>target.getGreen())gr=target.getGreen();
		}
		if(bl>target.getBlue()) {
			bl-=change;
			if(bl<target.getBlue())bl=target.getBlue();
		}else if(re<target.getBlue()) {
			bl+=change;
			if(bl>target.getBlue())bl=target.getBlue();
		}
		this.leafClor[0]=re;
		this.leafClor[1]=gr;
		this.leafClor[2]=bl;
	}
	public void sway(int range) {
		int rotby=(int)(Math.random()*(range*2+3)-range);
		if(Observer.findDif(angle,boundingangle)<0) {
			rotby-=2;
		}else if(Observer.findDif(angle,boundingangle)>0) {
			rotby+=2;
		}
		rotate( rotby );
	}
	public void reset() {
		if(Math.abs(Observer.findDif(angle, boundingangle))>30) {
			if(Observer.findDif(angle, boundingangle)>0) {
				rotate(3);
			}else {
				rotate(-3);
			}
		}
	}
	public void sway(int range,int amount) {
		int rotby=(int)(Math.random()*(range*2+1)-range);
		rotby+=amount;
		if(Observer.findDif(angle,boundingangle)<0) {
			rotby-=1;
		}else if(Observer.findDif(angle,boundingangle)>0) {
			rotby+=1;
		}
		rotate( rotby );
	}
	public void rotate(int by) {
		this.angle+=by;
		if(this.angle>360)this.angle-=360;
		else if(this.angle<0)this.angle+=360;
		
		//if(root!=null) {
			if(Math.abs(Observer.findDif(boundingangle, angle))>120) {
				if(Observer.findDif(boundingangle, angle)>0){
					angle=boundingangle+120;
				}else {
					angle=boundingangle-120;
				}
			}
		//}

		if(this.angle>360)this.angle-=360;
		else if(this.angle<0)this.angle+=360;
	}
	public void windRot(float force,double direction) {
		
		double dif=Observer.findDif(angle,direction);
		if(dif==0)return;
		float per=(force*100)/wr;
		per=per/10;
		int turn=(int)(dif*per)/100;
		
		//Plan.pivotArc((int)start[0]-50, (int)start[1]-50, 100, 100, 10, -(int)angle, -(int)turn);
		
		rotate((int)turn);
		if(root!=null) {
			root.influence(force, direction);
		}
	}
	
	public float[] getLeafColor() {return this.leafClor;}
	public double getAngle() {return this.angle;}
	public double getBoundingAngle() {return this.boundingangle;}
	public void setBoundingAngle(double to) {this.boundingangle=to;}
	public float getLength() {return length;}
	public float getMaxLength() {return maxlen;}
	public float getLoc() {return loc;}
	public float[] getLine() {
		return new float[] {start[0],start[1],(float)(start[0]+Observer.findCosSin(angle, length)[0]),(float)(start[1]+Observer.findCosSin(angle, length)[1])};
	}
}
