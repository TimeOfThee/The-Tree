package main;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Display {
	private JFrame frame;
	private Canvas canvas;
	
	private String title;
	private int height,width;
	
	public Display(String title,int width,int height) {
		this.title=title;
		this.height=height;
		this.width=width;
		createDisplay();
	}
	
	private void createDisplay() {
		frame=new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//essential to closing
		frame.setResizable(false);//lock window size
		frame.setLocationRelativeTo(null);//center the window
		frame.setVisible(true);
		
		canvas =new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setFocusable(false);
		frame.add(canvas);
		frame.pack();
	}
	
	public Canvas getCanvas(){return canvas;}
	public JFrame getJFrame() {return frame;}
}
