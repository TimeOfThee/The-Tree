package treeBase;

import java.awt.Color;
import java.awt.Graphics;

import main.Observer;
import tree.Branch;
import tree.Leaf;
import tree.Segment;
import tree.Tree;
import weather.Gust;

public class BranchBase extends Branch{
	
	public int flowers=0;

	public BranchBase(Observer ob,Tree mother,Segment root,float loc,double ang,float dis,float thickness,Color[] color) {
		super(ob, mother, root, loc, ang, dis, thickness, 2+(int)(Math.random()*5), color);
		this.maxseglen=20;
		this.bark[0].setMaxLen(maxseglen);
		this.growthRate=10;//setback to 10
		if(TreeBase.fast) {
			this.growthRate=100;
		}
		this.leafamount+=(int)(Math.random()*10);
		
		this.direction=ob.getWind().getDirection();
		this.force=ob.getWind().getForce();
	}
	@Override
	public void update() {
		setTimeClor();
		for(Gust gu:ob.getWind().getGusts()) {
			for(Segment s:bark) {
				if(s==null)continue;
				if(Observer.findHypotenuse(gu.getEnd()[0], gu.getEnd()[1], s.getEnd(false)[0], s.getEnd(false)[1])<gu.geteRad()) {
					windOn(gu.getForce(),gu.getDirection());
				}
			}
		}
		
		if(bark[0].getRoot()!=null) {
			bark[0].shiftTo(Observer.findSegLoc(root, loc)[0], Observer.findSegLoc(root, loc)[1]);
			if(bark[0].getRoot().getRotated()!=0) {
				bark[0].rotate(bark[0].getRoot().getRotated());
				bark[0].setBoundingAngle(bark[0].getBoundingAngle()+bark[0].getRoot().getRotated());
			}
		}
		for(Segment s:bark) {
			if(s==null)continue;
			
			if(s.getBoundingAngle()>30 && s.getBoundingAngle()<=90) {
				if(s.getRoot()!=null) {
					s.setBoundingAngle(30);
					if(s.getRoot().getBoundingAngle()<=30) {
						s.setBoundingAngle(s.getBoundingAngle()-((float)(Math.random()*31)+20 ));
						if(s.getBoundingAngle()<0) {
							s.setBoundingAngle(s.getBoundingAngle()+360);
						}
					}
				}
				
			}
			else if(s.getBoundingAngle()>90 && s.getBoundingAngle()<150) {
				s.setBoundingAngle(150);
				if(s.getRoot()!=null) {
					if(s.getRoot().getBoundingAngle()>=150 && s.getRoot().getBoundingAngle()>=180) {
						s.setBoundingAngle(s.getBoundingAngle()+((float)(Math.random()*31)+20 ));
					}
				}
			}
			
			s.reset();
			s.update( s==bark[0] );
			s.setRotated(0);
		}
		grow();
		
		for(Segment s:bark) {
			if(s==null || s.getRoot()==null)continue;
			if(s!=bark[0])s.snapToRoot();
		}
		
		double[][] avgs=new double[bark.length][2];//[ang][amount]
		for(int a=0;a<bark.length;a++) {
			avgs[a]=new double[] {0,0};
		}
		for(Leaf l:leaves) {
			l.update();
			l.windRot(force, direction);
			l.reset();
		}
		
		if(force>0) {
			force-=force/4;
		}else {
			force=0;
		}
	}

	@Override
	public void render(Graphics g) {
		for(Segment s: bark) {
			if(s==null)continue;
			s.render(g);
		}
		for(Leaf l:leaves) {
			l.render(g);
		}
	}
	@Override
	public void render(Graphics g,boolean specific) {
		for(Segment s: bark) {
			if(s==null)continue;
			s.render(g);
		}
		if(!specific) {
			for(Leaf l:leaves) {
				l.render(g);
			}
		}
	}

	@Override
	public void grow() {
		for(Segment s:bark) {
			if(s==null)continue;
			int growth=0;
			if(s.getFertile(true)>0) {
				growth=1;
			}
			
			if(growth>0){
				float maxlen=s.getlengthMax();
				if( (int)(Math.random()*50)<growthRate ) {
					
					if(s.getLength()<maxlen-(maxlen/4)) {//not mature
						s.grow(0.3f, true);
						
						if( (int)(Math.random()*10)==0 ) {
							s.sway(3);
						}
					}else if(s.getLength()<maxlen) {//not max
						if( (int)(Math.random()*25)==0 ) {
							split(s);
						}
						else {
							s.grow(0.3f, true);
						}
						
						if( (int)(Math.random()*15)==0 ) {
							int rotby=(int)(Math.random()*5)-2;
							if(Observer.findDif(s.getAngle(),s.getBoundingAngle())<0) {
								rotby-=1;
							}else if(Observer.findDif(s.getAngle(),s.getBoundingAngle())>0) {
								rotby+=1;
							}
							s.rotate( rotby );
						}
					}else {
						if( (int)(Math.random()*20)==0 ) {
							split(s);
						}
						
						if( (int)(Math.random()*30)==0 ) {
							s.sway(2);
						}
					}
				}
					if( s.getLength()>=maxlen*0.75 && (int)(Math.random()*60)<growthRate ) {
						sprout(s,1);
					}
					
					if( (int)(Math.random()*25)==0 ) {
						s.sway(2);
					}
						
			}
		}
	}
	public void split(Segment root) {
		if(mother.getFertilityB()<=0)return;
		int look=-1;
		for(int a=0;a<bark.length;a++) {
			if(bark[a]==null) {
				look=a;
				break;
			}
		}
		if(look>-1) {
			bark[look]=new Segment(root.getEnd(false)[0], root.getEnd(false)[1], root.getAngle()+(int)(Math.random()*81)-40, 10, (int)(root.getThickness()),
															new Color((int)barkClor[0],(int)barkClor[1],(int)barkClor[2]), 
															new int[] {(int)(Math.random()*31)-15,(int)(Math.random()*31)-15,(int)(Math.random()*31)-15}, 
															new int[] {maxsegsplit,maxsegsplit});
			root.setFertile(true, root.getFertile(true)-1);
			bark[look].setRoot(root);
			bark[look].setMaxLen(maxseglen+( (int)(Math.random()*51)-30 ));
			
			for(Segment s:bark) {
				if(s==null || s==bark[look])continue;
				
				if(s.getRoot()==bark[look].getRoot()) {
					int range=10;
					if(Observer.findDif(bark[look].getAngle(), s.getAngle())<0) {
						s.setBoundingAngle(s.getBoundingAngle()-(range/2)+( (int)(Math.random()*(range+1))-(range/2) ));
						bark[look].setBoundingAngle(bark[look].getBoundingAngle()+20);
					}else {
						s.setBoundingAngle(s.getBoundingAngle()+(range/2)+( (int)(Math.random()*(range+1))-(range/2) ));
						bark[look].setBoundingAngle(bark[look].getBoundingAngle()-20);
					}
				}
			}
			mother.setFertilityB(mother.getFertilityB()-1);
		}
	}
	public int leafAttached(Segment root) {
		int amount=0;
		for(Leaf l:leaves) {
			if(l.getRoot()==root)amount++;
		}
		return amount;
	}
	public void sprout(Segment root,int amount){
		if(mother.getFertilityF()<=0) return;
		for(int a=0;a<amount;a++) {
			if(mother.getFertilityF()<=0) break;
			if(leafAttached(root)<this.leafamount) {
				int loc=(int)(Math.random()*101);
				int ang=(int)(Math.random()*361);
				Leaf nlea=new LeafBase(ob, mother, root, loc, ang, mother.getFoliageCS(),
						new int[] {(int)(Math.random()*61)-30,(int)(Math.random()*61)-30,(int)(Math.random()*61)-30}
						,false);
				/*for(Leaf l:leaves) {
					System.out.println(l.getLoc()-loc);
					if(Math.abs(l.getLoc()-loc)<=100) {
						float[]l1=new float[] {nlea.getEnd()[0],nlea.getEnd()[1],
												(float)(nlea.getEnd()[0]+Observer.findCosSin(ang, nlea.getMaxLength())[0]),
												(float)(nlea.getEnd()[1]+Observer.findCosSin(ang, nlea.getMaxLength())[1])
						};
						float[]l2=new float[] {l.getEnd()[0],l.getEnd()[1],
								(float)(l.getEnd()[0]+Observer.findCosSin(ang, l.getMaxLength())[0]),
								(float)(l.getEnd()[1]+Observer.findCosSin(ang, l.getMaxLength())[1])
						};
						if(Observer.intersects(l1, l2)) {
							leafamount--;
							return;
						}
					}
				}*/
				leaves.add(nlea);
				mother.setFertilityF(mother.getFertilityF()-1);
			}
		}
	}
	public void windOn(float force, double direction) {
		
		this.direction=this.direction+(Observer.findDif(this.direction,direction)/2);
		if(this.direction>360)this.direction-=360;
		else if(this.direction<0)this.direction+=360;
		
		this.force+=force/50;
		
	}
}
