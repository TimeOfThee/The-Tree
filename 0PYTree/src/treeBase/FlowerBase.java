package treeBase;

import java.awt.Color;
import java.awt.Graphics;

import main.Observer;
import tree.Flower;
import tree.Leaf;
import tree.Tree;
import weather.Weather;

public class FlowerBase extends Flower{

	private double angle;
	
	private float last=0;//-------------------------------------------------------------------------------
	
	public FlowerBase(Tree mother, Leaf root, float size,Color[] target,int[] skewClor) {
		super(mother, root, size,target,skewClor);
		last=target[0].getBlue();
		this.angle=(int)(Math.random()*90);
		this.maxsize=3+(int)(Math.random()*3);
		this.growthRate=10;
		if(TreeBase.fast) {
			this.growthRate=100;
		}
	}

	@Override
	public void update() {
		setTimeClor();
		if(isAttached()) {
			start[0]=(float)(root.getEnd()[0]+Observer.findCosSin(root.getAngle(), root.getLength()*0.3)[0]);
			start[1]=(float)(root.getEnd()[1]+Observer.findCosSin(root.getAngle(), root.getLength()*0.3)[1]);
		}
		grow();
	}

	@Override
	public void render(Graphics g) {
		
		int[] ex=new int[8];
		int[] wy=new int[8];
		
		for(int a=0;a<4;a++) {
			ex[a*2]=(int)( start[0]+Observer.findCosSin(angle+(a*90), size)[0] );
			wy[a*2]=(int)( start[1]+Observer.findCosSin(angle+(a*90), size)[1] );
			
			ex[(a*2)+1]=(int)( start[0]+Observer.findCosSin(angle+(a*90)+45, size/2)[0] );
			wy[(a*2)+1]=(int)( start[1]+Observer.findCosSin(angle+(a*90)+45, size/2)[1] );
		}
		Color set=(Observer.skewColor(new Color((int)color[0],(int)color[1],(int)color[2]), skewClor[0], skewClor[1], skewClor[2]));
		g.setColor(Observer.skewColor(set, Weather.shiftColor,(int)(Weather.fadePower*0.75)));
		g.fillPolygon(ex, wy, 8);
		
		int r=(int)(color[0]+30),gr=(int)(color[1]+50),b=(int)(color[2]-20);
		if(r>255)r=255;
		if(gr>255)gr=255;
		if(b<0)b=0;
		Color sett=(new Color(r,gr,b));
		g.setColor(Observer.skewColor(sett, Weather.shiftColor, (int)(Weather.fadePower*0.75)));
		g.fillOval((int)(start[0]-size/2), (int)(start[1]-size/2), (int)(size), (int)(size));
	}

	@Override
	public void shadeRender(Graphics g, Color color) {
		int[] ex=new int[8];
		int[] wy=new int[8];
		
		for(int a=0;a<4;a++) {
			ex[a*2]=(int)( start[0]+Observer.findCosSin(angle+(a*90), size+1)[0] );
			wy[a*2]=(int)( start[1]+Observer.findCosSin(angle+(a*90), size+1)[1] );
			
			ex[(a*2)+1]=(int)( start[0]+Observer.findCosSin(angle+(a*90)+45, (size/2)+1)[0] );
			wy[(a*2)+1]=(int)( start[1]+Observer.findCosSin(angle+(a*90)+45, (size/2)+1)[1] );
		}
		
		g.setColor(color);
		g.fillPolygon(ex, wy, 8);
	}

	@Override
	public void grow() {
		if( (int)(Math.random()*50)<growthRate ) {
			if(size<maxsize) {
				size+=0.01;
			}
		}
	}
	
}
