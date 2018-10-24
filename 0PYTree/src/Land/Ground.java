package Land;

import java.awt.Color;
import java.awt.Graphics;

import main.Observer;
import main.Plan;
import time.Time;
import weather.Weather;

public class Ground {

	private float[][] dirt,dirtB,hills,stars;
	private Observer ob;
	
	private Color[] skys=new Color[] {new Color(215, 250, 235),//morning
										new Color(185, 210, 250),//day
										new Color(240, 110, 90),//afternoon
										new Color(50, 30, 125)};//night
	private Color[] fades=new Color[] {new Color(20, 60, 120),
										new Color(70, 30, 180),
										new Color(70, 0, 130),
										new Color(40, 0, 40)};
	private Color[] dirts=new Color[] {new Color(80,95,70),
										new Color(95,120,50),
										new Color(80, 70, 20),
										new Color(45, 45, 25)};
	private Color lastClor=new Color(185,230,230);
	private float[] skyClor=new float[] {skys[3].getRed(),skys[3].getGreen(),skys[3].getBlue()};//65,90,250
	private Color dirtClor=new Color(105,100,40);
	
	private Color sunClor=new Color(235,235,95,20);
	private Color moonClor=new Color(135,160,205,40);
	private Color starClor=new Color(95, 105, 135);
	int sunang=70,moonang=-70;
	int sunsiz=70,moonsiz=40;
	
	int centerx=-1350,centery=-400,centersize=950,salph=255;
	
	//thunder
	int alph=0;
	int[] thunderColor=new int[] {255, 245, 170};
	
	public Ground(Observer ob,int ex,int wy) {
		this.ob=ob;
		
		this.dirt=new float[5][10];
		
		int tem=ob.getTime().getQuarter()-1;
		if(tem<0)tem=3;
		Color temC=Observer.skewColor(dirts[tem], dirts[ob.getTime().getQuarter()],(ob.getTime().getHour()-(0+(6*ob.getTime().getQuarter())))*(100/6));
		
		int xsh=ex-(int)(Math.random()*120)-80;
		int ysh=wy+(int)(Math.random()*30)+30;
		int[] csh=new int[] {(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10};
		dirt[0]=new float[] {-50,ysh,xsh+50,600-ysh,
				temC.getRed(),temC.getGreen(),temC.getBlue(),
				(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10};
		csh=new int[] {(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10};
		dirt[3]=new float[] {xsh-(int)(Math.random()*40)-60,ysh-20,120+(int)(Math.random()*40),40+(int)(Math.random()*30),
				temC.getRed(),temC.getGreen(),temC.getBlue(),
				(int)(Math.random()*21)-10,(int)(Math.random()*11)+10,(int)(Math.random()*16)-5};
		
		xsh=ex+(int)(Math.random()*150)+120;
		csh=new int[] {(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10};
		dirt[1]=new float[] {dirt[0][2]-50,wy,xsh-dirt[0][2]+50,600-wy,
				temC.getRed(),temC.getGreen(),temC.getBlue(),
				(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10};
		ysh=wy+(int)(Math.random()*20)+20;
		csh=new int[] {(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10};
		dirt[4]=new float[]{xsh-(int)(Math.random()*60)-40,ysh-10,120+(int)(Math.random()*80),20+(int)(Math.random()*20),
				temC.getRed(),temC.getGreen(),temC.getBlue(),
				(int)(Math.random()*21)-10,(int)(Math.random()*11)+10,(int)(Math.random()*16)-5};
		
		csh=new int[] {(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10};
		dirt[2]=new float[] {xsh,ysh,1000-xsh+50,600-ysh,
				temC.getRed(),temC.getGreen(),temC.getBlue(),
				(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10};
		

		this.dirtB=new float[][] {
			new float[] {0,dirt[0][1]-1},
			new float[] {dirt[3][0]-1,dirt[0][1]-1},
			new float[] {dirt[3][0]-1,dirt[3][1]-1},
			new float[] {dirt[1][0]-1,dirt[3][1]-1},
			new float[] {dirt[1][0]-1,dirt[1][1]-1},
			new float[] {dirt[1][0]+dirt[1][2],dirt[1][1]-1},
			new float[] {dirt[1][0]+dirt[1][2],dirt[4][1]-1},
			new float[] {dirt[4][0]+dirt[4][2],dirt[4][1]-1},
			new float[] {dirt[4][0]+dirt[4][2],dirt[2][1]-1},
			new float[] {1000,dirt[2][1]-1}
		};
		int[][] line=new int[6][2];
		ysh=wy-10-(int)(Math.random()*30);
		line[0]=new int[] {0,ysh};
		for(int a=0;a<5;a++) {
			xsh=140+(a*140)+(int)(Math.random()*101)-50;			
			ysh=wy-10-(int)(Math.random()*40);
			
			line[a+1]=new int[] {xsh,ysh};
		}
		
		hills=new float[6][7];
		for(int a=0;a<5;a++) {
			csh=new int[] {(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10};
			hills[a]=new float[] {line[a][0],line[a][1],line[a+1][0]-line[a][0],550-line[a][1],
					temC.getRed(),temC.getGreen(),temC.getBlue(),
					(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10};
		}
		csh=new int[] {(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10};
		hills[5]=new float[] {line[5][0],line[5][1],1000-line[5][0],550-line[5][1],
				temC.getRed(),temC.getGreen(),temC.getBlue(),
				(int)(Math.random()*21)-10,(int)(Math.random()*21)-10,(int)(Math.random()*21)-10};
		
		int xrange=800,yrange=500;
		stars=new float[200][3];
		int xplot=0,yplot=0;
		for (int a=0;a<stars.length;a++) {
			xplot+=(int)(Math.random()*6)+5;
			yplot+=(int)(Math.random()*yrange);
			if(xplot>xrange)xplot-=xrange;
			if(yplot>yrange)yplot-=yrange;
			stars[a]=new float[] {xplot,yplot,(int)(Math.random()*2)+1};
		}
	}
	public void update(){
		setTimeClor();
		for(float[] s:stars) {
			s[0]+=0.01*((int)(Math.random()*30)+1);
			if(s[0]>800)s[0]-=800;
		}
		if(alph>0) {
			alph-=15;
			if(alph<0)alph=0;
		}
	}
	public void render(Graphics g){
		Color skyColor=new Color((int)skyClor[0],(int)skyClor[1],(int)skyClor[2]);
		
		int to=ob.getTime().getQuarter()-1;
		if(to<0)to=3;
		
		//sky
		Color fadeClor=Observer.skewColor(fades[to], fades[ob.getTime().getQuarter()],(ob.getTime().getHour()-(0+(6*ob.getTime().getQuarter())))*(100/6));
		
		for(int a=0;a<15;a++) {
			Color set=(ob.skewColor(skyColor,fadeClor,a*(100/20)));//setback to 28
			g.setColor(Observer.skewColor(set,Weather.shiftColor,Weather.fadePower));
			g.fillRect(0, 550-(a*40), 1000, 40);
		}
		g.setColor(Color.white);
		
		//stars
		if(ob.getTime().getHour()>=19 || ob.getTime().getHour()<=1) {
			if(salph<255) {
				if(Plan.debug)salph+=5;
				else salph+=2;
				if(salph>255)salph=255;
			}
		}else{
			if(salph>0) {
				if(Plan.debug)salph-=8;
				else salph-=3;
				if(salph<0)salph=0;
			}
		}
		g.setColor(new Color(starClor.getRed(),starClor.getGreen(),starClor.getBlue(),salph));
		
		if(ob.getWeather().getState()!=Weather.snowing) {
			for(float[] s:stars) {
				g.fillOval( (int)(s[0]-(s[2]/2)), (int)(s[1]-(s[2]/2)), (int)(s[2]*2), (int)(s[2]*2));
			}
		}
		
		if(ob.getTime().getHour()>=15 && ob.getTime().getHour()<= 20) {
			//sun
			double angle=(ob.getTime().getTimePer()*360/100)+sunang;
			
			int[] center= new int[] {
					(int)(centerx+centersize+Observer.findCosSin(angle, centersize)[0]),
					(int)(centery+centersize+Observer.findCosSin(angle, centersize)[1])};
			Color set=sunClor;
			for (int a=4;a>0;a--) {
				g.setColor(Observer.skewColor(set,Weather.shiftColor,(int)(Weather.fadePower*0.5)));
				g.fillOval(center[0]-sunsiz-(int)(a*a*10), center[1]-sunsiz-(int)(a*a*10), (sunsiz*2)+(a*a*20), (sunsiz*2)+(a*a*20));
				set=Observer.skewColor(set, 10, -20, -40);
				int alph=(int)(set.getAlpha()*1.75);
				if(alph>200)alph=200;
				set=new Color(set.getRed(),set.getGreen(),set.getBlue(),alph);
			}
			g.setColor(Observer.skewColor(set,Weather.shiftColor,(int)(Weather.fadePower*0.5)));
			g.fillOval(center[0]-sunsiz, center[1]-sunsiz, sunsiz*2, sunsiz*2);
		}else if(ob.getTime().getHour()>=1 && ob.getTime().getHour()<= 4) {
			//moon
			double angle=(ob.getTime().getTimePer()*360/100)+moonang;
			
			int[] center= new int[] {
					(int)(centerx+centersize+Observer.findCosSin(angle, centersize)[0]),
					(int)(centery+centersize+Observer.findCosSin(angle, centersize)[1])};
			Color set=moonClor;
			for (int a=4;a>0;a--) {
				g.setColor(Observer.skewColor(set,Weather.shiftColor,(int)(Weather.fadePower*0.5)));
				g.fillOval(center[0]-moonsiz-(int)(a*a*3), center[1]-moonsiz-(int)(a*a*3), (moonsiz*2)+(a*a*6), (moonsiz*2)+(a*a*6));
				set=Observer.skewColor(set, 15, 20, 30);
				int alph=(int)(set.getAlpha()*1.75);
				if(alph>200)alph=200;
				set=new Color(set.getRed(),set.getGreen(),set.getBlue(),alph);
			}
			g.setColor(Observer.skewColor(set,Weather.shiftColor,(int)(Weather.fadePower*0.5)));
			g.fillOval(center[0]-moonsiz, center[1]-moonsiz, moonsiz*2, moonsiz*2);
		} 
		
		if(alph>0){
			int set=alph;
			if(set>255)set=255;
			g.setColor(new Color(thunderColor[0],thunderColor[1],thunderColor[2],set));
			g.fillRect(0,0,800,600);
		}
		
		//ground
		for(float[] d:hills) {
			float re=d[4]+d[7];
			float gr=d[5]+d[8];
			float bl=d[6]+d[9];
			if(re>255)re=255;
			else if(re<0)re=0;
			if(gr>255)gr=255;
			else if(gr<0)gr=0;
			if(bl>255)bl=255;
			else if(bl<0)bl=0;
			
			Color set=(Observer.skewColor(new Color((int)re,(int)gr,(int)bl),skyColor, 20));
			g.setColor(Observer.skewColor(set,Weather.shiftColor,Weather.fadePower));
			//g.setColor(Observer.skewColor(set, Weather.shiftColor, Weather.fadePower));
			g.fillRect((int)d[0], (int)d[1], (int)d[2], (int)d[3]);
		}
		
		Color set=(new Color((int)dirt[0][4],(int)dirt[0][5],(int)dirt[0][6]).darker());
		g.setColor(Observer.skewColor(set, Weather.shiftColor, (int)(Weather.fadePower*0.75)));
		int[] ex=new int[dirtB.length];
		int[] wy=new int[dirtB.length];
		for(int a=0;a<dirtB.length;a++) {
			ex[a]=(int)dirtB[a][0];
			wy[a]=(int)dirtB[a][1];
		}
		g.drawPolyline(ex, wy, dirtB.length);
		
		for(float[] d:dirt) {
			float re=d[4]+d[7];
			float gr=d[5]+d[8];
			float bl=d[6]+d[9];
			if(re>255)re=255;
			else if(re<0)re=0;
			if(gr>255)gr=255;
			else if(gr<0)gr=0;
			if(bl>255)bl=255;
			else if(bl<0)bl=0;
			
			set=(new Color((int)re,(int)gr,(int)bl));
			g.setColor(Observer.skewColor(set, Weather.shiftColor,(int)(Weather.fadePower*0.75)));
			g.fillRect((int)d[0], (int)d[1], (int)d[2], (int)d[3]);
		}
	}
	private void setTimeClor() {
		int to=ob.getTime().getQuarter()-1;
		if(to<0)to=3;
		
		Color target=Observer.skewColor(skys[to], skys[ob.getTime().getQuarter()],(ob.getTime().getHour()-(0+(6*ob.getTime().getQuarter())))*(100/6));
		
		for(float[] d:dirt) {
			
		}
		
		float re=skyClor[0],gr=skyClor[1],bl=skyClor[2];
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
		skyClor[0]=re;
		skyClor[1]=gr;
		skyClor[2]=bl;
		//ground fix
		setTimeClorG();
	}
	private void setTimeClorG() {
		int to=ob.getTime().getQuarter()-1;
		if(to<0)to=3;
		
		Color target=Observer.skewColor(dirts[to], dirts[ob.getTime().getQuarter()],(ob.getTime().getHour()-(0+(6*ob.getTime().getQuarter())))*(100/6));
		
		for(float[] d:dirt) {
		
			float re=d[4],gr=d[5],bl=d[6];
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
			d[4]=re;
			d[5]=gr;
			d[6]=bl;
		}
		for(float[] d:hills) {
		
			float re=d[4],gr=d[5],bl=d[6];
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
			d[4]=re;
			d[5]=gr;
			d[6]=bl;
		}
	}
	public float[][] getDirt() {return dirt;}
	public void thunder() {
		alph=270;
	}
}
