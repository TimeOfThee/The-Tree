package main;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseManager implements MouseListener, MouseMotionListener{

	private boolean m1,m3;
	private int mx,my;
	
	public MouseManager() {
		
	}
	
	public boolean isM1() {return m1;}
	public boolean isM3() {return m3;}
	
	public int getMX() {return mx;}
	public int getMY() {return my;}
	
	@Override
	public void mousePressed(MouseEvent m) {
		if(m.getButton()==MouseEvent.BUTTON1)
			m1=true;
		if(m.getButton()==MouseEvent.BUTTON3)
			m3=true;
	}
	@Override
	public void mouseReleased(MouseEvent m) {
		if(m.getButton()==MouseEvent.BUTTON1)
			m1=false;
		if(m.getButton()==MouseEvent.BUTTON3)
			m3=false;
	}
	@Override
	public void mouseMoved(MouseEvent m) {
		mx=m.getX();
		my=m.getY();
	}
	//-------------------------------------------------------
	@Override
	public void mouseDragged(MouseEvent m) {
		if(m.getButton()==MouseEvent.BUTTON1)
			m1=true;
		if(m.getButton()==MouseEvent.BUTTON3)
			m3=true;
		mx=m.getX();
		my=m.getY();
		if(m.getButton()==MouseEvent.BUTTON1)
			m1=false;
		if(m.getButton()==MouseEvent.BUTTON3)
			m3=false;
	}
	@Override
	public void mouseClicked(MouseEvent m) {
		
	}

	@Override
	public void mouseEntered(MouseEvent m) {
		
	}

	@Override
	public void mouseExited(MouseEvent m) {
		
	}

}
