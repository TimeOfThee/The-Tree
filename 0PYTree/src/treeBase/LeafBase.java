package treeBase;

import java.awt.Color;
import java.awt.Graphics;

import main.Observer;
import tree.Flower;
import tree.Leaf;
import tree.Segment;
import tree.Tree;
import weather.Weather;

public class LeafBase extends Leaf{

	public LeafBase(Observer ob, Tree mother, Segment root,int loc,int ang, Color[] color,int[] skewClor,boolean big) {
		super(ob, mother, root, loc, ang,5, color,skewClor);
		this.big=big;
		this.maxlen=20+(int)(Math.random()*6)-(loc/10);
		if(big)this.maxlen+=20;
		this.growthRate=15;//setback to 15
		if(TreeBase.fast) {
			this.growthRate=100;
		}
	}
	public LeafBase(Observer ob, Tree mother,float x,float y,int loc,int ang, Color[] color,int[] skewClor,boolean big) {
		super(ob, mother, x, y, loc, ang,5, color,skewClor);
		this.big=big;
		this.maxlen=20+(int)(Math.random()*6)-(loc/10);
		if(big)this.maxlen+=30;
		this.growthRate=15;//setback to 15
		if(TreeBase.fast) {
			this.growthRate=100;
		}
	}

	@Override
	public void update() {
		setTimeClor();
		if(isAttached()) {
			setEnd(Observer.findSegLoc(root, loc).clone());
		}
		grow();
		
		if(flower!=null)flower.update();
	}

	@Override
	public void render(Graphics g) {
		float midx=start[0]+(float)Observer.findCosSin(angle, length*0.4)[0];
		float midy=start[1]+(float)Observer.findCosSin(angle, length*0.4)[1];
		
		double angleft=angle-90;
		if(angleft<0)angleft+=360;
		double angright=angleft-180;
		if(angright<0)angright+=360;
		
		int[] ex=new int[] {(int)start[0],
				(int)(midx+(float)Observer.findCosSin(angleft, length/4)[0]),
				(int)(start[0]+(float)Observer.findCosSin(angle, length)[0]),
				(int)(midx+(float)Observer.findCosSin(angright, length/4)[0])
		};
		int[] wy=new int[] {(int)(start[1]),
				(int)(midy+(float)Observer.findCosSin(angleft, length/4)[1]),
				(int)(start[1]+(float)Observer.findCosSin(angle, length)[1]),
				(int)(midy+(float)Observer.findCosSin(angright, length/4)[1])
		};
		
		Color set=(Observer.skewColor(new Color((int)leafClor[0],(int)leafClor[1],(int)leafClor[2]),skewClor[0],skewClor[1],skewClor[2]));
		g.setColor(Observer.skewColor(set, Weather.shiftColor,(int)(Weather.fadePower*0.75)));
		g.fillPolygon(ex,wy,4);
		
		/*if(flower!=null) {
			flower.shadeRender(g,flower.getColor().darker());
			flower.render(g);
		}*/
	}
	public void shadeRender(Graphics g,Color color){
		float midx=start[0]+(float)Observer.findCosSin(angle, length*0.4)[0];
		float midy=start[1]+(float)Observer.findCosSin(angle, length*0.4)[1];

		double temang=angle+180;
		if(temang>360)temang-=360;
		float dis=0;
		
		int[] ex=new int[4];
		int[] wy=new int[4];
		
		for(int a=0;a<4;a++) {
			switch(a) {
			case 0:
				dis=(length*0.4f)+2;
				break;
			case 2:
				dis=(length*0.6f)+2;
				break;
			default:
				dis=(length*0.25f)+1;
			}
			ex[a]=(int)(midx+(float)Observer.findCosSin(temang, dis)[0]);
			wy[a]=(int)(midy+(float)Observer.findCosSin(temang, dis)[1]);
			
			temang+=90;
			if(temang>360)temang-=360;
		}
		Color set=(Observer.skewColor(color,skewClor[0],skewClor[1],skewClor[2]));
		g.setColor(Observer.skewColor(set, Weather.shiftColor, (int)(Weather.fadePower*0.75)));
		g.drawPolygon(ex,wy,4);
	}

	@Override
	public void grow() {
		if( (int)(Math.random()*50)<growthRate ) {	
			if(length<maxlen*0.75) {
				sway(2);
				length+=0.1;
			}
			else if(length<maxlen) {
				if((int)(Math.random()*15)==0) {
					sway(3);
				}
				length+=0.1;
			}
			else {
				if((int)(Math.random()*10)==0) {
					sway(3);
				}
				if((int)(Math.random()*100)<=0) {
					sprout();
				}
			}
		}
	}
	public void sprout(){
		if(flower!=null  || mother.getFertilityFl()<=0)return;
		Flower set=new FlowerBase(mother, this, 3, mother.getPedalCS(),new int[] {(int)(Math.random()*61)-30,(int)(Math.random()*61)-30,(int)(Math.random()*61)-30});
		
		for(Flower f:mother.getFlowers()) {
			float xdis=f.getLoc()[0]-set.getLoc()[0];
			float ydis=f.getLoc()[1]-set.getLoc()[1];
			float dis=(float)(Math.sqrt( (xdis*xdis)+(ydis*ydis) ));
			if( dis<=10 )return;
		}
		
		flower=set;
		mother.setFertilityFl(mother.getFertilityFl()-1);
	}
}
