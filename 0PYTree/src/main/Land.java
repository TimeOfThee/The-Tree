package main;
import java.awt.Color;
import java.awt.Graphics;

import tree.Segment;
import tree.Tree;

public class Land {

	private Observer ob;
	private int tick;
	private boolean tock;
	
	private Tree[] trees;
	
	public Land(Observer ob) {
		this.ob=ob;
		this.trees=new Tree[1];
	}
	public void update() {
		passTime();
		
		for(Tree t:trees) {
			t.update();
		}
		
	}
	public void render(Graphics g) {
		
		for(Tree t:trees) {
			t.render(g);
		}
	}
	private void passTime() {
		tock=false;
		tick++;
		if(tick>=60) {
			tock=true;
			tick=0;
		}
	}
	
	public boolean tocked() {
		return tock;
	}
}
