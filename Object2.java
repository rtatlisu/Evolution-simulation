package newAttempt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Object2 extends JPanel{
	int recX;
	int recY;
	int recW;
	int recH;

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)  g;
		super.paintComponent(g2d);
		setBackground(Color.black);
		drawRectangle(g2d);
		
	}



	public void drawRectangle(Graphics2D g2d) {
		recW = 33;
		recH = 33;
		recX = GUI2.frame.getBounds().width/2 - (recW/2);
		recY = GUI2.frame.getBounds().height-(GUI2.frame.getBounds().height-100);

		g2d.setColor(Color.white);
		g2d.fillRect(recX, recY, recW, recH);
		g2d.drawRect(recX, recY, recW, recH);
		
		g2d.setColor(Color.red);
		g2d.drawOval((int) (recX+(recW/2)-0.5), (int) (recY+(recH/2)-0.5), 1, 1);
		g2d.fillOval((int) (recX+(recW/2)-0.5), (int) (recY+(recH/2)-0.5), 1, 1);
		//System.out.println("h"+(int) (recX+(recW/2)-0.5) + "  "+ (int) (recY+(recH/2)-0.5));
		//x=409, y=115
		g2d.setColor(Color.white);

	}



}
