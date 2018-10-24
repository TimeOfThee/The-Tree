package treeBase;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import main.Observer;
import tree.Branch;
import tree.Leaf;
import tree.Segment;
import tree.Tree;
import tree.Trunk;
import weather.Gust;

public class TrunkBase extends Trunk{
	
	//maxseglen exists

	public TrunkBase(Observer ob,Tree mother, float x, float y, Color[] color) {
		super(ob, mother, x, y, 7+(int)(Math.random()*4), 2, 10, color);
		this.maxseglen=60;
		this.bark[0].setMaxLen(maxseglen);
		this.growthRate=5;//setback to 5
		if(TreeBase.fast) {
			this.growthRate=100;
		}
		this.leafamount+=(int)(Math.random()*8)-3;
	}

	@Override
	public void update() {
		setTimeClor();
		for(Segment s:bark) {
			if(s==null)continue;
			s.influence(ob.getWind().getForce()*0.2f, ob.getWind().getDirection());
			s.reset(2);
			s.update();
		}
		grow();
		
		for(Segment s:bark) {
			if(s==null || s.getRoot()==null)continue;
			s.snapToRoot();
		}
		for(Branch b:branches) {
			b.update();
		}
		for(Leaf l:leaves) {
			l.update();
		}
	}

	@Override
	public void render(Graphics g) {
		for(Leaf l:leaves) {
			l.render(g);
		}
		for(Segment s: bark) {
			if(s==null)continue;
			s.render(g);
		}
		for(int a=branches.size();a>0;a--) {
			branches.get(a-1).render(g);
		}
		/*for(Branch b:branches) {
			for(Leaf l:b.getLeaves()) {
				l.render(g);
			}
		}*/
	}

	@Override
	public void grow() {
		if(mother.getFertilityB()>0) {
			for(Segment s:bark) {
				if(s==null)continue;
				if(mother.getFertilityB()>0) {
					int growth=0;
					if(s.getFertile(true)>0) {
						growth=1;
						if(s.getFertile(false)>0)growth+=(int)(Math.random()*2);
					}else if(s.getFertile(false)>0)growth=2;
					
					if(growth>0){
						if( (int)(Math.random()*50)<growthRate ) {
							float maxlen=s.getlengthMax();
							
							if(s.getLength()<maxlen-(maxlen/4)) {//not mature
								s.grow(0.3f, true);
								
								if( (int)(Math.random()*10)==0 ) {
									s.sway(3);
								}
							}else if(s.getLength()<maxlen) {//not max
								if( (int)(Math.random()*2)==0 ) {//setback to 25
									if(growth==2) {//branch
										if(s!=bark[0]) {
											sprout(s,1,true);
										}
									}else {//split
										split(s);
									}
								}
								else {
									s.grow(0.3f, true);
								}
								
								if( (int)(Math.random()*15)==0 ) {
									s.sway(2);
								}
							}else {
								if( (int)(Math.random()*20)==0 ) {
									if(growth==2) {//branch
										if(s!=bark[0]) {
											sprout(s,1,true);
										}
									}else {//split
										split(s);
									}
								}
								/*if((int)(Math.random()*15)==0  && s!= bark[0]) {
									sprout(s,2,false);
								}*/
								
								if( (int)(Math.random()*30)==0 ) {
									s.sway(2);
								}
							}
						}
					}
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
			float thickness=root.getThickness()-2;
			if(thickness<5)thickness=5;
			bark[look]=new Segment(root.getEnd(false)[0], root.getEnd(false)[1], root.getAngle()+(int)(Math.random()*41)-20, 10, (int)(thickness), 
															new Color((int)barkClor[0],(int)barkClor[1],(int)barkClor[2]), 
															new int[] {(int)(Math.random()*31)-15,(int)(Math.random()*31)-15,(int)(Math.random()*31)-15}, 
															new int[] {maxsegsplit,maxsegsplit+(int)(Math.random()*2)});
			root.setFertile(true, root.getFertile(true)-1);
			bark[look].setRoot(root);
			bark[look].setMaxLen(maxseglen+( (int)(Math.random()*51)-30 ));
			
			for(Segment s:bark) {
				if(s==null || s==bark[look])continue;
				
				if(s.getRoot()==bark[look].getRoot()) {
					int range=60;
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
	public void sprout(Segment root,int amount,boolean branch) {
		if(mother.getFertilityB()<=0)return;
		if(branch) {
			for(int a=0;a<amount;a++) {
				if(mother.getFertilityB()<=0)break;
				if(root.getFertile(false)<=0)break;
				
				double direction=(int)(Math.random()*101)-50;
				if((int)(Math.random()*2)==0) {
					direction=direction*-1;
				}
				direction+=root.getBoundingAngle();
					
				float loc=(int)(Math.random()*81)+10;
				float thickness=root.getThickness()-2;
				if(thickness<3)thickness=3;
				
				Branch nbran=new BranchBase(ob, mother, root, loc, direction, 10,thickness, mother.getBarkCS());
				branches.add(nbran);
				root.setFertile(false, root.getFertile(false)-1);
				mother.setFertilityB(mother.getFertilityB()-1);	
			}
		}
		else{
			for(int a=0;a<amount;a++) {
				if(leafAttached(root)<this.leafamount) {
					int loc=(int)(Math.random()*101);
					int ang=(int)(Math.random()*361);
					Leaf nlea=new LeafBase(ob, mother, root, loc, ang, 
							mother.getFoliageC2S(),
							new int[] {(int)(Math.random()*61)-30,(int)(Math.random()*61)-30,(int)(Math.random()*61)-30 }
							,true);
					leaves.add(nlea);
				}
			}
		}
	}
	public int leafAttached(Segment root) {
		int amount=0;
		for(Leaf l:leaves) {
			if(l.getRoot()==root)amount++;
		}
		return amount;
	}
}
