package weather;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import main.Observer;
import main.Plan;
import time.Time;

public class Cloud {

	int x=0,y=1,w=2,h=3,re=4,gr=5,bl=6,skr=7,skg=8,skb=9,al=10,hp=11,size=12;
	
	private ArrayList<float[]> fluffs;//x y w l r g b a hp size(10-20)
	private Wind wind;
	private Observer ob;
	private Weather mother;
	private Color[] cloudCS=new Color[] {new Color(180,200,220),
										new Color(225,225,250),
										new Color(200,150,170),
										new Color(70,70,120)};
	private float[] cloudClor=new float[] {cloudCS[1].getRed(),cloudCS[1].getGreen(),cloudCS[1].getBlue()};
	
	private int timer=0;
	private int cloudLim=50;
	private int cloudLimA=0;
	
	public Cloud(Weather mother) {
		this.mother=mother;
		this.ob=mother.getObserver();
		this.wind=mother.getWind();
		this.fluffs=new ArrayList<float[]>();
		makeFluff(2, 400, 200);
	}
	public void update() {
		setTimeClor();
		float per=(wind.getForce()*100)/200;
		
		cloudLim=10+(int)((per*40)/100);
		if(mother.getState()==Weather.raining) {
			cloudLimA=5*mother.getRain().getDensity();
		}else {
			cloudLimA=0;
		}
		
		float elx,ely;
		
		timer++;
		if(timer>=((100-per)/4)+(int)(Math.random()*10)+5) {
			elx=(float)(400-300-(Observer.findCosSin(wind.getDirection(), wind.getForce())[0]));
			elx+=(int)(Math.random()*601);
			
			ely=(int)(Math.random()*260)+10;
			
			makeFluff((int)(Math.random()*3)+1,elx,ely);
			if(mother.getState()==Weather.raining)makeFluff( (mother.getRain().getDensity()/4),elx,ely );
				
			timer=0;
		}
		
		Iterator<float[]> it=fluffs.iterator();
		while(it.hasNext()) {
			float[] f=it.next();
			
			if(f[hp]>0) {
				f[hp]-=(int)(Math.random()*2)+1;
				if(f[hp]<0)f[hp]=0;
				
				if(cloudCount()>cloudLim+cloudLimA) {
					if((int)(Math.random()*20)==0) {//chance to go
						f[hp]=0;
					}
				}
			}
			
			if(f[hp]<=0) {
				if(f[al]>0)f[al]-=10;
				if(f[al]<=0)it.remove();
			}else {
				if(f[al]<255) {
					f[al]+=5;
					if(f[al]>255)f[al]=255;
				}
			}
			f[x]+=(Observer.findCosSin(wind.getDirection(), wind.getForce()/ (f[size]*5) )[0])*0.5;
			if((int)(Math.random()*20)==0) {
				f[y]+=(Observer.findCosSin(wind.getDirection(), wind.getForce()/ (f[size]*2) )[1])*0.5;
			}
			
			if(f[x]>=900 || f[x]+f[h]<=-100 || f[y]+f[h]<-100 ) {
				f[hp]=0;
			}
			
		}
	}
	public void render(Graphics g) {
		if(Plan.debug) {
			g.setColor(new Color(160,80,100));
			g.drawString(Integer.toString(fluffs.size())+" / "+Integer.toString(cloudLim+cloudLimA), Weather.skewx-50,Weather.skewy-1);
		}
		for(float[] f:fluffs) {
			float red=f[re]+f[skr];
			float gre=f[gr]+f[skg];
			float blu=f[bl]+f[skb];
			if(red>255)red=255;
			else if(red<0)red=0;
			if(gre>255)gre=255;
			else if(gre<0)gre=0;
			if(blu>255)blu=255;
			else if(blu<0)blu=0;
			
			Color set=Observer.skewColor(new Color((int)red,(int)gre,(int)blu,(int)f[al]), Weather.shiftColor, Weather.fadePower);
			g.setColor(set);

			if(f[x]<810 && f[x]+f[w]>-10)
				g.fillRect((int)f[x], (int)f[y], (int)f[w], (int)f[h]);
		}
	}
	
	private void makeFluff(int amount,float x,float y) {
		for(int a=0;a<amount;a++) {
			
			if(cloudCount()<cloudLim+cloudLimA) {
			
				int wid=(int)(Math.random()*80)+60;//60 150
				int hei=(int)(Math.random()*30)+10;//10 40
												//600 6000
				float per= (((wid*hei)-600)*100)/((150*40)-(60*10));
				int siz=((int)(10*per)/100)+10;
				
				int tem=ob.getTime().getQuarter()-1;
				if(tem<0)tem=3;
				

				Color temC=Observer.skewColor(cloudCS[tem], cloudCS[ob.getTime().getQuarter()],(ob.getTime().getHour()-(0+(6*ob.getTime().getQuarter())))*(100/6));
				
				float[] set=new float[] {
					(int)(x-(Math.random()*wid)),
					(int)(y-(Math.random()*hei)),
					wid,hei,
					temC.getRed(),
					temC.getGreen(),
					temC.getBlue(),
					(int)(Math.random()*11)-5,
					(int)(Math.random()*11)-5,
					(int)(Math.random()*11)-5,
					0,500,siz
				};
				
				fluffs.add(set);
			}else {
				if((int)(Math.random()*20)==0) {
					fluffs.get((int)(Math.random()*cloudLim-1))[hp]=0;
					makeFluff(1,x,y);
				}
			}
		}
	}
	public int cloudCountO() {
		int ret=0;
		
		for(float[] f:fluffs) {
			if(f[hp]>0)ret++;
		}
		
		return ret;
	}
	public int cloudCount() {
		return fluffs.size();
	}
	private void setTimeClor() {
		int to=ob.getTime().getQuarter()-1;
		if(to<0)to=3;
		
		Color target=Observer.skewColor(cloudCS[to], cloudCS[ob.getTime().getQuarter()],(ob.getTime().getHour()-(0+(6*ob.getTime().getQuarter())))*(100/6));
		
		for(float[] f:fluffs) {
		
			float red=f[re],gre=f[gr],blu=f[bl];
			float change=Time.change;
			
			if(red>target.getRed()) {
				red-=change;
				if(red<target.getRed())red=target.getRed();
			}else if(red<target.getRed()) {
				red+=change;
				if(red>target.getRed())red=target.getRed();
			}
			if(gre>target.getGreen()) {
				gre-=change;
				if(gre<target.getGreen())gre=target.getGreen();
			}else if(gre<target.getGreen()) {
				gre+=change;
				if(gre>target.getGreen())gre=target.getGreen();
			}
			if(blu>target.getBlue()) {
				blu-=change;
				if(blu<target.getBlue())blu=target.getBlue();
			}else if(blu<target.getBlue()) {
				blu+=change;
				if(blu>target.getBlue())blu=target.getBlue();
			}
			f[re]=red;
			f[gr]=gre;
			f[bl]=blu;
		}
	}
	public Color getCloudColor() {
		int to=ob.getTime().getQuarter()-1;
		if(to<0)to=3;
		 return Observer.skewColor(cloudCS[to], cloudCS[ob.getTime().getQuarter()],(ob.getTime().getHour()-(0+(6*ob.getTime().getQuarter())))*(100/6));
	}
}
