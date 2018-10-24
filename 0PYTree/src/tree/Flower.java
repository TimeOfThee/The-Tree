package tree;

import java.awt.Color;
import java.awt.Graphics;

import main.Observer;
import time.Time;

public abstract class Flower {
	
	protected Leaf root;
	protected float[] start;
	protected Tree mother;
	protected Color target;
	protected float[] color;
	protected Color[] targets;
	protected int[] skewClor;
	protected float size,maxsize=8,growthRate=10;
	
	public Flower(Tree mother,Leaf root,float size,Color[] targets,int[] skewClor) {
		this.mother=mother;
		this.root=root;
		this.start=new float[] { (float)(root.getEnd()[0]+Observer.findCosSin(root.getAngle(), root.getLength()*0.3)[0]), (float)(root.getEnd()[1]+Observer.findCosSin(root.getAngle(), root.getLength()*0.3)[1])};
		this.size=size;
		
		float re=root.getLeafColor()[0]+10;
		float gr=root.getLeafColor()[1]+10;
		float bl=root.getLeafColor()[2]+10;
		
		if(re>255)re=255;
		if(gr>255)gr=255;
		if(bl>255)bl=255;
		
		this.color=new float[] {re,gr,bl};
		this.skewClor=skewClor;
		this.targets=targets;
		this.target=targets[0];
	}
	
	public abstract void update();
	public abstract void render(Graphics g);
	public abstract void shadeRender(Graphics g,Color color);
	public abstract void grow();
	
	protected void setTimeClor() {
		int to=mother.getObserver().getTime().getQuarter()-1;
		if(to<0)to=3;
		
		Color target=Observer.skewColor(this.targets[to], this.targets[mother.getObserver().getTime().getQuarter()],(mother.getObserver().getTime().getHour()-(0+(6*mother.getObserver().getTime().getQuarter())))*(100/6));
		
		float re=this.color[0],gr=this.color[1],bl=this.color[2];
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
		this.color[0]=re;
		this.color[1]=gr;
		this.color[2]=bl;
	}
	
	public float[] getLoc() {return start;}
	public void setLoc(float[] to) {this.start=to;}
	public Leaf getRoot() {return root;}
	public void setRoot(Leaf to) {this.root=to;}
	public float[] getPedalColor() {return color;}
	public void setPedalColor(float[] to) {this.color=to;}
	public float getSize() {return size;}
	public void setSize(float to) {this.size=to;}
	
	public boolean isAttached() {
		if(root==null)return false;
		return true;
	}

}
