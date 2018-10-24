package tree;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import main.Observer;
import time.Time;

public abstract class Trunk {

	protected Observer ob;
	protected Tree mother;
	protected Segment[] bark;
	protected ArrayList<Branch> branches;
	protected ArrayList<Leaf> leaves;
	
	protected float[] barkClor;
	protected Color[] barks;
	protected int maxsegs,maxseglen=50,maxsegsplit=2,growthRate=5;
	protected int leafamount=20;
	
	/**manually set maxseglen=50,growthRate=5 ( if random<gr grow),leafamount
	 */
	public Trunk(Observer ob,Tree mother,float x,float y,int maxsegs, int maxsegsplit,int thickness,Color[] barks) {
		this.ob=ob;
		this.mother=mother;
		bark=new Segment[maxsegs];
		this.maxsegs=maxsegs;
		this.barks=barks;
		int tem=ob.getTime().getQuarter()-1;
		if(tem<0)tem=3;
		Color temC=Observer.skewColor(this.barks[tem], this.barks[ob.getTime().getQuarter()],(this.ob.getTime().getHour()-(0+(6*this.ob.getTime().getQuarter())))*(100/6));
		this.barkClor=new float[] {temC.getRed(),temC.getGreen(),temC.getBlue()};
		this.maxsegsplit=maxsegsplit;
		this.bark[0]=new Segment(x, y, 270d, 10, thickness, new Color((int)barkClor[0],(int)barkClor[1],(int)barkClor[2]),new int[] {0,0,0}, new int[] {maxsegsplit,maxsegsplit});
		this.branches=new ArrayList<Branch>();
		this.leaves=new ArrayList<Leaf>();
	}
	public abstract void update();
	public abstract void render(Graphics g);
	public abstract void grow();
	
	protected void setTimeClor() {
		int to=this.ob.getTime().getQuarter()-1;
		if(to<0)to=3;
		
		Color target=Observer.skewColor(this.barks[to], this.barks[ob.getTime().getQuarter()],(this.ob.getTime().getHour()-(0+(6*this.ob.getTime().getQuarter())))*(100/6));
		
		float re=this.barkClor[0],gr=this.barkClor[1],bl=this.barkClor[2];
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
		this.barkClor[0]=re;
		this.barkClor[1]=gr;
		this.barkClor[2]=bl;
		
		for(Segment s:this.bark) {
			if(s==null)continue;
			s.setColor(new Color((int)this.barkClor[0],(int)this.barkClor[1],(int)this.barkClor[2]));
		}
	}
	
	public Segment[] getBark(){return bark;}
	public ArrayList<Branch> getBranches(){return branches;}
	public ArrayList<Leaf> getLeaves(){return leaves;}
}
