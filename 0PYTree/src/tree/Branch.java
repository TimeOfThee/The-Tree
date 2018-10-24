package tree;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import main.Observer;
import time.Time;

public abstract class Branch {
	
	protected Observer ob;
	protected Tree mother;
	protected Segment[] bark;
	protected Segment root;
	protected float loc;//%
	protected ArrayList<Branch> children;
	protected ArrayList<Leaf> leaves;
	
	protected float[] barkClor;
	protected Color[] barks;
	protected int maxsegs,maxseglen=20,growthRate=10,maxsegsplit=3;
	protected int leafamount=10;
	
	//wind
	protected float force=0;
	protected double direction=0;
	
	/**manually set maxseglen=20,growthRate=5 ( if random<gr grow),leafamount=10
	 */
	public Branch(Observer ob,Tree mother,float x,float y,double ang,float dis,float thickness,int maxsegs,Color[] barks) {
		this.ob=ob;
		this.mother=mother;
		bark=new Segment[maxsegs];
		this.maxsegs=maxsegs;
		
		this.barks=barks;
		int tem=ob.getTime().getQuarter()-1;
		if(tem<0)tem=3;
		Color temC=Observer.skewColor(this.barks[tem], this.barks[ob.getTime().getQuarter()],(this.ob.getTime().getHour()-(0+(6*this.ob.getTime().getQuarter())))*(100/6));
		this.barkClor=new float[] {temC.getRed(),temC.getGreen(),temC.getBlue()};
		
		bark[0]=new Segment(x, y, ang, dis, thickness, new Color((int)barkClor[0],(int)barkClor[1],(int)barkClor[2]),
											new int[] {(int)(Math.random()*31)-15,(int)(Math.random()*31)-15,(int)(Math.random()*31)-15},
											new int[] {maxsegsplit,0});
		bark[0].setRoot(root);
		children=new ArrayList<Branch>();
		leaves=new ArrayList<Leaf>();

	}
	/**manually set maxseglen=30,growthRate=5 ( if random<gr grow)
	 */
	public Branch(Observer ob,Tree mother,Segment root,float loc,double ang,float dis,float thickness,int maxsegs,Color[] barks) {
		this.ob=ob;
		this.mother=mother;
		this.root=root;
		this.loc=loc;
		bark=new Segment[maxsegs];
		this.maxsegs=maxsegs;
		
		this.barks=barks;
		int tem=ob.getTime().getQuarter()-1;
		if(tem<0)tem=3;
		Color temC=Observer.skewColor(this.barks[tem], this.barks[ob.getTime().getQuarter()],(this.ob.getTime().getHour()-(0+(6*this.ob.getTime().getQuarter())))*(100/6));
		this.barkClor=new float[] {temC.getRed(),temC.getGreen(),temC.getBlue()};
		
		bark[0]=new Segment(Observer.findSegLoc(root, loc)[0], Observer.findSegLoc(root, loc)[1], ang, dis, thickness, new Color((int)barkClor[0],(int)barkClor[1],(int)barkClor[2]),new int[] {(int)(Math.random()*31)-15,(int)(Math.random()*31)-15,(int)(Math.random()*31)-15}, new int[] {maxsegsplit,0});
		bark[0].setRoot(root);
		children=new ArrayList<Branch>();
		leaves=new ArrayList<Leaf>();
	}
	public abstract void update();
	public abstract void render(Graphics g);
	public abstract void render(Graphics g,boolean specific);
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
	
	public void setRoot(Segment r) {this.root=r;}
	public Segment getRoot() {return this.root;}
	
	public Segment[] getBark(){return bark;}
	public ArrayList<Leaf> getLeaves(){return leaves;}
}
