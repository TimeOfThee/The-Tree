package treeBase;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import main.Observer;
import main.Plan;
import tree.Branch;
import tree.Flower;
import tree.Leaf;
import tree.Segment;
import tree.Tree;
import tree.Trunk;
import weather.Weather;

public class TreeBase extends Tree{
	
	private static Color[] barkC=new Color[] {new Color(165,135,80),
											new Color(150,120,0),
											new Color(110, 65, 35),
											new Color(60, 50, 25)};
	private static Color[] foliageC=new Color[] {new Color(40,180,80),
												new Color(70,180,70),
												new Color(90,120,50),
												new Color(15,70,40)};
	private static Color[] pedalC=new Color[] {new Color(230,200,180),
												new Color(230,190,120),
												new Color(200,150,100),
												new Color(100,90,70)};
	private static Color foliageFrostC=new Color(125, 225, 190);
	
	public static boolean fast=Plan.debug;
	
	public TreeBase(Observer ob, float x, float y) {
		super(ob,x,y,null, 70,900,60,barkC,foliageC,pedalC,foliageFrostC);
		this.trunk=new TrunkBase(ob, this, x, y, barkC);
	}
	public TreeBase(Observer ob, float x, float y, int ferB, int ferF,int ferFl,Color[] barkC, Color[] foliageC,Color[] pedalC) {
		super(ob,x,y,null, ferB,ferF,ferFl,barkC,foliageC,pedalC,foliageFrostC);
		this.trunk=new TrunkBase(ob, this, x, y, barkC);
		this.setBarkC(barkC[0]);
		this.setFoliageC(foliageC[0]);
	}
	
	@Override
	public void update() {
		trunk.update();
		recount();
	}
	@Override
	public void render(Graphics g) {
		Segment mark;
		/*for(Leaf l:leavesB) {
			l.shadeRender(g, l.getColor().darker());
		}*/
		for(Segment s:bark) {
			mark=new Segment(s,s.getThickness()+2,s.getColor().darker());
			mark.render(g);
		}
		for(Branch b:trunk.getBranches()) {
			for(Leaf l:b.getLeaves()) {
				
				l.shadeRender(g, new Color((int)l.getLeafColor()[0],(int)l.getLeafColor()[1],(int)l.getLeafColor()[2]).darker());
			}
		}
		trunk.render(g);
		for(Flower f:flowers) {
			int re=(int)f.getPedalColor()[0],gr=(int)f.getPedalColor()[1],bl=(int)f.getPedalColor()[2];
			if(re>255)re=0;
			else if(re<0)re=0;
			if(gr>255)gr=0;
			else if(gr<0)gr=0;
			if(bl>255)bl=0;
			else if(bl<0)bl=0;
			Color set=new Color(re,gr,bl).darker();
			f.shadeRender(g,Observer.skewColor(set, Weather.shiftColor, Weather.fadePower));
			f.render(g);
		}
		
		/*for(Segment s:bark) {
			if(s.getFrost()>50) {
				s.renderBug(g,Color.blue);
			}
		}
		for(Segment s:trunk.getBark()) {
			if(s==null)continue;
			if(s.getFrost()>50) {
				s.renderBug(g,Color.blue);
			}
		}
		for(Branch b:trunk.getBranches()) {
			for(Segment s:b.getBark()) {
				if(s==null)continue;
				if(s.getFrost()>50) {
					s.renderBug(g,Color.blue);
				}
			}
		}*/
	}
}
