package tree;

import java.awt.Color;
import java.awt.Graphics;

import main.Observer;
import weather.Weather;

public class Segment {

	private float[] start,end;
	private float thickness,length,maxlen;
	private double angle;
	private Color color;
	private int[] skewClor;
	private Segment root;
	private int[] fertile;//fertile longer,fertile outwards
	private double rotated=0,boundingangle,guidingangle;
	
	//wind
	private float wr=400;//wind resistance
	
	//snow
	private int frost;//frost percent
	private int delay;
	
	public Segment(float xs,float ys, float xe,float ye,float thickness,Color color,int[] skewClor,int[] fertile) {
		this.start=new float[] {xs,ys};
		this.end=new float[] {xe,ye};
		this.thickness=thickness;
		this.color=color;
		this.skewClor=skewClor;
		this.fertile=fertile;
		this.length=findLength();
		this.angle=Observer.findAngle(start[0], start[1], end[0], end[1]);
		this.boundingangle=angle;
		this.guidingangle=boundingangle;
		this.frost=0;
		this.delay=0;
	}
	public Segment(float xs,float ys, double ang,float dis,float thickness,Color color,int[] skewClor,int[] fertile) {
		this.start=new float[] {xs,ys};
		double cos=Observer.findCosSin(ang, dis)[0];
		double sin=Observer.findCosSin(ang, dis)[1];
		this.end=new float[] {xs+(float)cos,ys+(float)sin};
		this.thickness=thickness;
		this.color=color;
		this.skewClor=skewClor;
		this.fertile=fertile;
		this.length=findLength();
		this.angle=ang;
		this.boundingangle=angle;
		this.guidingangle=boundingangle;
		this.frost=0;
		this.delay=0;
	}
	public Segment(Segment image, float thickness,Color color) {
		this.start=image.getEnd(true).clone();
		this.end=image.getEnd(false).clone();
		this.thickness=thickness;
		this.length=image.getLength();
		this.maxlen=image.getlengthMax();
		this.angle=image.getAngle();
		this.color=color;
		this.skewClor=image.getSkewClor();
		this.fertile=new int[] {0,0};//fertile longer,fertile outwards
		this.boundingangle=image.getBoundingAngle();
		this.guidingangle=boundingangle;
		this.frost=0;
		this.delay=0;
	}
	public void update() {
		this.rotated=0;
		
		if(this.root!=null) {
			snapToRoot();
			if(this.root.getRotated()!=0) {
				rotate(this.root.getRotated());
				this.boundingangle+=this.root.getRotated();
			}
		}
		
		this.angle=Observer.findAngle(start[0], start[1], end[0], end[1]);
		
		//frost
		if(frost>0) {
			if(delay<=0) {
				frost--;
				delay=5;
			}else {
				delay--;
			}
		}
	}
	public void update(boolean specific) {
		if(specific) {
			this.rotated=0;
			this.angle=Observer.findAngle(start[0], start[1], end[0], end[1]);
		}else{
			update();
		}
		//frost
		if(frost>0) {
			if(delay<=0) {
				frost--;
				delay=5;
			}else {
				delay--;
			}
		}
	}
	public void render(Graphics g) {
		Color set=Observer.skewColor(this.color, this.skewClor[0], this.skewClor[1], this.skewClor[2]);
		g.setColor(Observer.skewColor(set,Weather.shiftColor,(int)(Weather.fadePower*0.75)));
		
		double ang1=Observer.findAngle(end[0], end[1], start[0], start[1]);
		float[][] end1=new float[4][2];
		
		double ang2=Observer.findAngle(start[0], start[1], end[0], end[1]);
		float[][] end2=new float[4][2];
		
		for(int a=0;a<4;a++) {
		end1[a]=new float[] { (float)( start[0]+Observer.findCosSin( (ang1+90)-(60*a) , thickness/2)[0] ),
							(float)( start[1]+Observer.findCosSin( (ang1+90)-(60*a), thickness/2)[1] ) };
		
		end2[a]=new float[] { (float)( end[0]+Observer.findCosSin( (ang2+90)-(60*a) , thickness/2)[0] ),
							(float)( end[1]+Observer.findCosSin( (ang2+90)-(60*a), thickness/2)[1] ) };		
		}
		
		g.fillPolygon(new int[] { (int)end1[0][0],(int)end1[1][0],(int)end1[2][0],(int)end1[3][0],
									(int)end2[0][0],(int)end2[1][0],(int)end2[2][0],(int)end2[3][0] }, 
					new int[] { (int)end1[0][1],(int)end1[1][1],(int)end1[2][1],(int)end1[3][1],
									(int)end2[0][1],(int)end2[1][1],(int)end2[2][1],(int)end2[3][1] },
					8);
	}
	public void outline(Graphics g) {
		g.setColor(this.color);
		double ang1=Observer.findAngle(end[0], end[1], start[0], start[1]);
		float[][] end1=new float[4][2];
		
		double ang2=Observer.findAngle(start[0], start[1], end[0], end[1]);
		float[][] end2=new float[4][2];
		
		for(int a=0;a<4;a++) {
		end1[a]=new float[] { (float)( start[0]+Observer.findCosSin( (ang1+90)-(60*a) , thickness/2)[0] ),
							(float)( start[1]+Observer.findCosSin( (ang1+90)-(60*a), thickness/2)[1] ) };
		
		end2[a]=new float[] { (float)( end[0]+Observer.findCosSin( (ang2+90)-(60*a) , thickness/2)[0] ),
							(float)( end[1]+Observer.findCosSin( (ang2+90)-(60*a), thickness/2)[1] ) };		
		}
		
		g.drawPolygon(new int[] { (int)end1[0][0],(int)end1[1][0],(int)end1[2][0],(int)end1[3][0],
									(int)end2[0][0],(int)end2[1][0],(int)end2[2][0],(int)end2[3][0] }, 
					new int[] { (int)end1[0][1],(int)end1[1][1],(int)end1[2][1],(int)end1[3][1],
									(int)end2[0][1],(int)end2[1][1],(int)end2[2][1],(int)end2[3][1] },
					8);
	}
	public void renderBug(Graphics g,Color color) {
		g.setColor(color);
		double ang1=Observer.findAngle(end[0], end[1], start[0], start[1]);
		float[][] end1=new float[4][2];
		
		double ang2=Observer.findAngle(start[0], start[1], end[0], end[1]);
		float[][] end2=new float[4][2];
		
		for(int a=0;a<4;a++) {
		end1[a]=new float[] { (float)( start[0]+Observer.findCosSin( (ang1+90)-(60*a) , thickness/2)[0] ),
							(float)( start[1]+Observer.findCosSin( (ang1+90)-(60*a), thickness/2)[1] ) };
		
		end2[a]=new float[] { (float)( end[0]+Observer.findCosSin( (ang2+90)-(60*a) , thickness/2)[0] ),
							(float)( end[1]+Observer.findCosSin( (ang2+90)-(60*a), thickness/2)[1] ) };		
		}
		
		g.fillPolygon(new int[] { (int)end1[0][0],(int)end1[1][0],(int)end1[2][0],(int)end1[3][0],
									(int)end2[0][0],(int)end2[1][0],(int)end2[2][0],(int)end2[3][0] }, 
					new int[] { (int)end1[0][1],(int)end1[1][1],(int)end1[2][1],(int)end1[3][1],
									(int)end2[0][1],(int)end2[1][1],(int)end2[2][1],(int)end2[3][1] },
					8);
		g.setColor(color.darker());
		g.drawLine((int)start[0], (int)start[1], (int)(start[0]+Observer.findCosSin(boundingangle, 50)[0]), (int)(start[1]+Observer.findCosSin(boundingangle, 50)[1]));
		g.setColor(color.darker());
		g.drawLine((int)start[0], (int)start[1], (int)(start[0]+Observer.findCosSin(guidingangle, 50)[0]), (int)(start[1]+Observer.findCosSin(guidingangle, 50)[1]));
	}
	
	public float[] getEnd(boolean start) {
		if(!start)return end;
		else return this.start;
	}
	public void setEnd(boolean start,float[] to) {
		if(!start) {this.end=to.clone();}
		else {this.start=to.clone();}
		this.length=findLength();
	}
	public float[] getLine() {
		return new float[] {start[0],start[1],end[0],end[1]};
	}
	public Color getColor() {return color;}
	public void setColor(Color to) {this.color=to;}
	public float getThickness() {return thickness;}
	public void setThickness(float to) {this.thickness=to;}
	public int getFertile(boolean longer){
		if(!longer) {return fertile[1];}//return outwards
		else {return fertile[0];}//return longer
	}
	public void setFertile(boolean longer,int to) {
		if(!longer) {this.fertile[1]=to;}
		else {this.fertile[0]=to;}
	}
	public float findLength() {
		float xdis=end[0]-start[0];
		float ydis=end[1]-start[1];
		double dis=Math.sqrt( (xdis*xdis)+(ydis*ydis) );
		return (float)dis;
	}
	public float getLength() {return length;}
	public double getRotated() {return rotated;}
	public void setRotated(double to) {this.rotated=to;}
	public void grow(float dis,boolean out) {
		length+=dis;
		
		if(!out) {
			double ang=Observer.findAngle(end[0], end[1],start[0], start[1]);
			start=new float[] { start[0]+(float)( Observer.findCosSin(ang, dis)[0] )
							,start[1]+(float)( Observer.findCosSin(ang, dis)[1] )};
		}else {
			double ang=Observer.findAngle(start[0], start[1],end[0], end[1]);
			end=new float[] { end[0]+(float)( Observer.findCosSin(ang, dis)[0] )
							,end[1]+(float)( Observer.findCosSin(ang, dis)[1] )};
		}
	}
	public double getAngle() {
		return angle;
	}
	public void setRoot(Segment r) {this.root=r;}
	public Segment getRoot() {return root;}
	public double getBoundingAngle() {return boundingangle;}
	public void setBoundingAngle(double to) {this.boundingangle=to;}
	public float getlengthMax() {return maxlen;}
	public void setMaxLen(float to) {this.maxlen=to;}
	public int[] getSkewClor() {return this.skewClor;}
	public int getFrost() {return this.frost;}
	public void setFrost(int to) {
		this.frost=to;
		if(this.frost<0)this.frost=0;
		else if(this.frost>100)this.frost=100;
	}
	
	public void rotate(double by) {
		this.rotated=angle;
		angle+=by;
		if(angle>360)angle-=360;
		else if(angle<0)angle+=360;
		
		if(Math.abs(Observer.findDif(boundingangle, angle))>60) {
			if(Observer.findDif(boundingangle, angle)>0){
				angle=boundingangle+60;
			}else {
				angle=boundingangle-60;
			}
		}
		
		if(this.angle>360)this.angle-=360;
		else if(this.angle<0)this.angle+=360;
		
		this.rotated=Observer.findDif(this.rotated, angle);
		double cos=Observer.findCosSin(angle,length)[0];
		double sin=Observer.findCosSin(angle,length)[1];
		setEnd(false,new float[] { (float)(start[0]+cos),(float)(start[1]+sin) });
	}
	public void influence(float force,double direction) {
		
		double dif=Observer.findDif(angle,direction);
		if(dif==0)return;
		float per=(force*100)/wr;
		int turn=(int)(dif*per)/100;
		
		this.guidingangle=boundingangle+turn;
		if(guidingangle>360)guidingangle-=360;
		else if(guidingangle<0)guidingangle+=360;
		
		rotate(turn/40);
	}
	public void reset() {
		double target=Observer.findDif(boundingangle, guidingangle)/2;
		target+=boundingangle;
		if(target>360)target-=360;
		else if(target<0)target+=360;
		if(Math.abs(Observer.findDif(angle, target))>20) {
			if(Observer.findDif(angle, target)>0) {
				rotate(2);
			}else {
				rotate(-2);
			}
		}
	}
	public void reset(int turnTo) {
		double target=Observer.findDif(boundingangle, guidingangle)/2;
		target+=boundingangle;
		if(target>360)target-=360;
		else if(target<0)target+=360;
		if(Math.abs(Observer.findDif(angle, target))>turnTo) {
			if(Observer.findDif(angle, target)>0) {
				rotate(2);
			}else {
				rotate(-2);
			}
		}
	}
	public void sway(int range) {
		int rotby=(int)(Math.random()*(range*2+1)-range);
		
		double target=Observer.findDif(boundingangle, guidingangle)/2;
		target+=boundingangle;
		if(target>360)target-=360;
		else if(target<0)target+=360;
		
		if(Observer.findDif(angle,target)<0) {
			rotby-=1;
		}else if(Observer.findDif(angle,target)>0) {
			rotby+=1;
		}
		rotate( rotby );
	}
	public void sway(int range,int amount) {
		int rotby=(int)(Math.random()*(range*2+1)-range);
		rotby+=amount;
		
		double target=Observer.findDif(boundingangle, guidingangle)/2;
		target+=boundingangle;
		if(target>360)target-=360;
		else if(target<0)target+=360;
		
		if(Observer.findDif(angle,target)<0) {
			rotby-=1;
		}else if(Observer.findDif(angle,target)>0) {
			rotby+=1;
		}
		rotate( rotby );
	}
	public void shift(float x, float y) {
		start[0]+=x;
		end[0]+=x;
		start[1]+=y;
		end[1]+=y;
	}
	public void shiftTo(float x,float y) {
		shift( x-start[0],y-start[1] );
	}
	public void snapToRoot() {
		if(this.root!=null) {
			shiftTo(root.getEnd(false)[0],root.getEnd(false)[1]);
		}
	}
}
