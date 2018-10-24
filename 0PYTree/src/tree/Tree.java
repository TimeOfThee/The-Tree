package tree;


import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import main.Observer;

public abstract class Tree {

	public static float baseSeglen=40,baseThickness=20;
	
	protected Observer ob;
	protected Trunk trunk;
	protected ArrayList<Segment> bark;
	protected ArrayList<Leaf> leaves,leavesB;
	protected ArrayList<Flower>flowers;
	protected int fertilityB,fertilityF,fertilityFl;
	
	protected Color barkC,foliageC,foliageC2,pedalC,foliageFrostC;
	protected Color[] barkCS,foliageCS,foliageC2S,pedalCS;
	
	public Tree(Observer ob,float x,float y,Trunk trunk,int ferB,int ferF,int ferFl,Color[] barkCS, Color[] foliageCS,Color[] pedalCS,Color foliageFrostC) {
		this.ob=ob;
		this.trunk=trunk;//new (type)trunk
		
		this.bark=new ArrayList<Segment>();
		this.leaves=new ArrayList<Leaf>();
		this.leavesB=new ArrayList<Leaf>();
		this.flowers=new ArrayList<Flower>();
		
		this.foliageCS=foliageCS;
		this.foliageC2S=new Color[4];
		int shift=-40;
		for(int a=0;a<4;a++) {
			foliageC2S[a]=ob.skewColor(foliageCS[a], shift, shift, shift);
		}
		
		this.barkCS=barkCS;
		this.barkC=barkCS[0];
		this.foliageC=foliageCS[0];
		this.foliageC2=foliageC2S[0];
		this.pedalCS=pedalCS;
		this.pedalC=pedalCS[0];
		this.foliageFrostC=foliageFrostC;
		
		this.fertilityB=ferB;
		this.fertilityF=ferF;
		this.fertilityFl=ferFl;
	}
	public abstract void update();
	public abstract void render(Graphics g);
	
	public void recount() {
		bark=new ArrayList<Segment>();
		leaves=new ArrayList<Leaf>();
		flowers=new ArrayList<Flower>();
		
		for(Leaf l:trunk.getLeaves()) {
			leavesB.add(l);
		}
		for(Segment s:trunk.getBark()) {
			if(s!=null)bark.add(s);
		}
		for(Branch b:trunk.getBranches()) {
			for(Segment s:b.getBark()) {
				if(s!=null)bark.add(s);
			}
			for(Leaf l:b.getLeaves()) {
				leaves.add(l);
				if(l.getFlower()!=null) {
					flowers.add(l.getFlower());
				}
			}
		}
	}
	
	public Trunk getTrunk() {
		return trunk;
	}
	public void setTrunk(Trunk trunk) {
		this.trunk = trunk;
	}
	public ArrayList<Segment> getBranches() {
		return bark;
	}
	public void setBranches(ArrayList<Segment> bark) {
		this.bark = bark;
	}
	public ArrayList<Leaf> getLeaves() {
		return leaves;
	}
	public void setLeaves(ArrayList<Leaf> leaves) {
		this.leaves = leaves;
	}
	public ArrayList<Flower> getFlowers() {
		return flowers;
	}
	public void setFlowers(ArrayList<Flower> flowers) {
		this.flowers = flowers;
	}
	public ArrayList<Leaf> getLeavesB() {
		return leavesB;
	}
	public void setLeavesB(ArrayList<Leaf> leaves) {
		this.leavesB = leaves;
	}
	public int getFertilityB() {
		return fertilityB;
	}
	public void setFertilityB(int fertilityB) {
		this.fertilityB = fertilityB;
	}
	public int getFertilityF() {
		return fertilityF;
	}
	public void setFertilityF(int fertilityF) {
		this.fertilityF = fertilityF;
	}
	public int getFertilityFl() {
		return fertilityFl;
	}
	public void setFertilityFl(int fertilityFl) {
		this.fertilityFl = fertilityFl;
	}
	public Color getBarkC() {
		return barkC;
	}
	public void setBarkC(Color barkC) {
		this.barkC = barkC;
	}
	public Color[] getBarkCS() {
		return barkCS;
	}
	public void setBarkCS(Color[] barkCS) {
		this.barkCS = barkCS;
	}
	public Color[] getFoliageCS() {
		return foliageCS;
	}
	public void setFoliageCS(Color[] foliageCS) {
		this.foliageCS = foliageCS;
	}
	public Color[] getFoliageC2S() {
		return foliageC2S;
	}
	public void setFoliageC2S(Color[] foliageC2S) {
		this.foliageC2S = foliageC2S;
	}
	public Color getFoliageC() {
		return foliageC;
	}
	public void setFoliageC(Color foliageC) {
		this.foliageC = foliageC;
	}
	public Color getFoliageC2() {
		return foliageC2;
	}
	public void setFoliageC2(Color foliageC) {
		this.foliageC2 = foliageC;
	}
	public Color getPedalC() {
		return pedalC;
	}
	public void setPedalC(Color pedalC) {
		this.pedalC = pedalC;
	}
	public Color[] getPedalCS() {
		return pedalCS;
	}
	public void setPedalCS(Color[] pedalCS) {
		this.pedalCS = pedalCS;
	}
	public Color getFoliageFrostC() {
		return foliageFrostC;
	}
	public Observer getObserver() {
		return ob;
	}
	public ArrayList<Segment> getBark(){return bark;}
}
