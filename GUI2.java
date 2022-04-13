package newAttempt;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

public class GUI2  {
	static JFrame frame;

	
	public static void main(String[] args) {
		
		 frame = new JFrame();
		 frame.setVisible(true);
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(720,720+28));
		
		frame.revalidate();
		
		frame.pack();
	//	System.out.println(frame.getBounds());
	//	o.setPreferredSize(new Dimension(720,720));
		Object2 o = new Object2();
		Agents2 a = new Agents2();
		
		o.setPreferredSize(new Dimension(720,720));
		

		
	//	frame.pack();
	//	frame.add(o);
	//	frame.revalidate();
	//	frame.setSize(720, 720);
		frame.getContentPane().add(a);
		a.setPreferredSize(new Dimension(720,720));
		
	//	frame.getContentPane().add(o);
		


	//	a.setPreferredSize(new Dimension(720,720));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	/*	a.setPreferredSize(new Dimension(720,720));
		frame.getContentPane().add(a);
		frame.pack();*/
		//frame.setVisible(true);
		frame.revalidate();
		frame.pack();

	}


}
