package newAttempt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Agents2 extends Object2{
	int populationSize = 20;
	double magnitude=0.02;
	int lifetime = 490;
	double mutationRate=0.05;
	int storedLifetime=lifetime;
	int lifetimeFinished;
	boolean elite;
	float agentsX;
	float agentsY;
	int agentsW;
	int agentsH;
	int storedFitness;
	int gen=1;
	int recW = 33;
	int recH = 33;
	int recX = GUI2.frame.getBounds().width/2 - (recW/2);
	int recY =  GUI2.frame.getBounds().height-(GUI2.frame.getBounds().height-100);
	double vel;
	boolean finished;
	int randomGen = 0;
	int prev=0;
	int fitness;
	int count=0;
	double distance;
	double maxDistance;
	int maxFitness;
	int averageFitness=0;
	String text;
	String fitnessString;
	String genString;
	
//	Agents2[] agentsArr;
	ArrayList<Agents2> agentsArr;
	//ArrayList<Integer> collided;
	boolean collided;
	int collidedAngle;
	ArrayList<Agents2> genesStored;
	ArrayList<Agents2> chooseTwo;
	ArrayList<Agents2> copyAgentsArr;
	ArrayList<Agents2> agentsPool;
	ArrayList<Integer> mutationPool;
	ArrayList<Double> head;
	double dirX;
	double dirY;
	Timer timer;
	AffineTransform old;
	AffineTransform old2;
	ArrayList<Integer> genes;
	Random random = new Random();
	Rectangle2D.Double rect;
	Rectangle2D.Double test;


	public Agents2() {
	//	System.out.println(Math.floor(489*100/490));
		//ArrayList<Double> head;
		finished=false;
		elite=false;
		lifetimeFinished=0;
		vel = 1 + (3 - 1) * random.nextDouble();
		collided=false;
		collidedAngle=0;
		fitness=0;
		dirX=0;
		dirY=-1;
		agentsW=5;
		agentsH=33;
		agentsX=GUI2.frame.getBounds().width/2 - agentsW/2;	
		agentsY=GUI2.frame.getBounds().height - 100;
		maxDistance = Math.sqrt(
				Math.pow((agentsX - (recX+(recW/2)-0.5)), 2) +
				Math.pow((agentsY - (recY+(recH/2)-0.5)), 2));
		
		
		//Create genes with a degree of randomness
		genes = new ArrayList<Integer>();
		
		for(int i = 0; i < storedLifetime; i++) {
			//randomGen = random.nextInt(360+359)-359;
			
			
			if(i != 0) {
				randomGen = random.nextInt(360+359)-359;
				randomGen*=magnitude;
			}
			else {
				randomGen = random.nextInt(361);
			}
			genes.add(randomGen + prev);
			prev = genes.get(i);
			
	
		}
		
	
	}



	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g2d);
		old=g2d.getTransform();
		
		if(agentsArr==null && gen==1) {
			createAgents(populationSize);
		}
		move(g2d);
		g2d.setTransform(old);
		g2d.setColor(Color.red);
		g2d.drawRect(GUI2.frame.getBounds().width/2 - agentsW/2-(33/2),
				GUI2.frame.getBounds().height - 100, 33, 33);
		textArea(g2d);	
		colorizeEliteAgent(g2d);

		
		
	}
	
	
	
	
	public void textArea(Graphics2D g2d) {
		text = "Lifetime: "+ lifetime;
		g2d.setColor(Color.white);
		g2d.drawString(text, 33, 33);
	
		//average fitness
		averageFitness=0;
		for(int i = 0; i < agentsArr.size(); i++) {
			averageFitness += agentsArr.get(i).fitness;
		}
		
		averageFitness/=agentsArr.size();
		fitnessString="Average fitness: "+averageFitness;
		g2d.drawString(fitnessString, 33, 50);
		//generation display
		genString="Generation: " + gen;
		g2d.drawString(genString, 33, 67);
		
	}
	
	
	
	//berchnet fitness basierend auf der euklidschen distanz zwischen agent und quadrat
	public void calcFitness(int x) {	
		if(!agentsArr.get(x).collided) {
				agentsArr.get(x).distance =  Math.sqrt(
				Math.pow((agentsArr.get(x).agentsX - (recX+(recW/2)-0.5)), 2) +
				Math.pow((agentsArr.get(x).agentsY - (recY+(recH/2)-0.5)), 2));
		
				agentsArr.get(x).fitness= (int) Math.floor( ((agentsArr.get(x).distance*100)
						/maxDistance) );
				agentsArr.get(x).fitness-=100;
				agentsArr.get(x).fitness*=-1;
				
				if(agentsArr.get(x).fitness < 0) {
					agentsArr.get(x).fitness=0;
				}	
				
				//add time component to the fitness, the faster you reach the target, the better is
				//your fitness
				if(lifetime > storedLifetime/10) {
					agentsArr.get(x).fitness = (int) Math.floor(agentsArr.get(x).fitness *
							(Math.floor(lifetime*100/storedLifetime))/100);
				}
				else  {
					agentsArr.get(x).fitness = (int) Math.floor((storedLifetime/10)/storedLifetime);
				}
				
		}
		//collided into a wall -> fitness/2
	/*	else if(agentsArr.get(x).finished==false && agentsArr.get(x).collided==true) {	
			agentsArr.get(x).fitness= agentsArr.get(x).storedFitness/2;
		}
		*/
		//reached the target -> fitness*2
		else if(agentsArr.get(x).finished==true) {
			agentsArr.get(x).fitness = agentsArr.get(x).storedFitness*2;
		}
		
}

	
	
	
	public void createAgents(int x) {	
		if(gen==1) {
			agentsArr= new ArrayList<Agents2>();
			for(int i = 0; i < populationSize; i++) {
				Agents2 a = new Agents2();
				agentsArr.add(a);
			}
		}
	
			
			
			if(gen > 1) {
				for(int i = 0; i < agentsArr.size(); i++) {
					selection(agentsArr);
		
						if(!agentsArr.get(i).elite) {
							agentsArr.get(i).genes = crossover(chooseTwo.get(0).genes,chooseTwo.get(1).genes);
							
							if(chooseTwo.get(0).fitness >= chooseTwo.get(0).fitness) {
								agentsArr.get(i).vel =chooseTwo.get(0).vel;
							}
							else {
								agentsArr.get(i).vel =chooseTwo.get(1).vel;
							}
						}	
						else {
							System.out.println("Time spend to reach target of the elite: " +
						(storedLifetime - agentsArr.get(i).lifetimeFinished));
							System.out.println(agentsArr.get(i).elite);
						
						}
					

					mutation(mutationRate);
					
					//reset the agents attributes that are not relevant for evolution
			//		finished=false;
			//		elite=false;
					agentsArr.get(i).lifetimeFinished=0;
				//	vel = 1 + (3 - 1) * random.nextDouble();
					agentsArr.get(i).collided=false;
					agentsArr.get(i).collidedAngle=0;
					agentsArr.get(i).fitness=0;
					agentsArr.get(i).dirX=0;
					agentsArr.get(i).dirY=-1;
					agentsArr.get(i).agentsX=GUI2.frame.getBounds().width/2 - agentsW/2;	
					agentsArr.get(i).agentsY=GUI2.frame.getBounds().height - 100;
		
				}
				System.out.println();
				count=0;
				
			}	
	}



	public void move(Graphics2D g2d) {
		g2d.setColor(new Color(255,255,255,150));
		//initiate new generation
		if(lifetime==0) {
	
			lifetime=storedLifetime;
			gen++;
			prev=0;
			copyAgentsArr = new ArrayList<Agents2>(agentsArr);	
			determineElite();
			createAgents(populationSize);
			agentsPool.removeAll(agentsPool);
			agentsPool=null;	
		}
		
		if(lifetime>0) {
			for(int agents=0; agents < agentsArr.size(); agents++) {
					
					calcFitness(agents);
				
				if(!agentsArr.get(agents).collided) {
					agentsArr.get(agents).storedFitness = agentsArr.get(agents).fitness;
					collision(agents);
				}
		ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		};

		if(timer == null) {
			timer = new Timer(1000/60, listener);
			timer.start();
		}
		
		if(old2 != null) {
			g2d.setTransform(old2);
		}
		
		//if(collision(agents)) {
		if(agentsArr.get(agents).collided) {
			g2d.rotate(Math.toRadians(agentsArr.get(agents).collidedAngle),
					agentsArr.get(agents).agentsX,agentsArr.get(agents).agentsY);
		}
		else  {
		
			g2d.rotate(Math.toRadians(agentsArr.get(agents).genes.get(genes.size()-lifetime)),
					agentsArr.get(agents).agentsX,agentsArr.get(agents).agentsY);
			
		}
		
			//die werte, um die x und y erhöht werden müssen, um eine realistische und dem
		//angle entsprechende bewegung zu erzeugen, werden hier berechnet.
		//der y-wert muss negativ gesetzt werden, da der cosinus die inverse vom sinus ist
			agentsArr.get(agents).dirX = Math.sin(Math.toRadians(agentsArr.get(agents).genes.get(genes.size()-lifetime)));
			agentsArr.get(agents).dirY = Math.cos(Math.toRadians(agentsArr.get(agents).genes.get(genes.size()-lifetime)));
			agentsArr.get(agents).dirY*=-1;

			rect = new Rectangle2D.Double(agentsArr.get(agents).agentsX, agentsArr.get(agents).agentsY, agentsW, agentsH);

			g2d.fill(rect);
			g2d.draw(rect);
		

			
			if(agentsArr.get(agents).collided) {
			agentsArr.get(agents).dirX=0;
			agentsArr.get(agents).dirY=0;
		}
		else {
			//die x-und-y-koordinaten hängen vom angle ab, auf diesen wird der jetztige wert
			//und ein geschwindigkeitsfaktor addiert
			agentsArr.get(agents).agentsX+=agentsArr.get(agents).dirX*agentsArr.get(agents).vel;
			agentsArr.get(agents).agentsY+=agentsArr.get(agents).dirY*agentsArr.get(agents).vel;

		}
			g2d.setTransform(old);
			
			}
			lifetime--;
		}	
	}
	
	
	
	//es wird random ein mittelpunkt gezogen basierend auf der genlänge und die kinder erhalten
	//alle gene vor der mitte vom ersten vererber und alle danach vom zweiten 
	public ArrayList<Integer> crossover(ArrayList<Integer> p1, ArrayList<Integer> p2) {
		int mid = random.nextInt(genes.size());
		ArrayList<Integer> genesChild = new ArrayList<Integer>();
		for(int i = 0; i < genes.size(); i++) {
			if(i < mid) {
				genesChild.add(p1.get(i));
			}
			else if(i >= mid){
				genesChild.add(p2.get(i));
			}
		}
	
		return genesChild;
	}
	
	
	
	
	public void selection(ArrayList<Agents2> agents) {
		chooseTwo = new ArrayList<Agents2>();
		if(agentsPool==null) {
			
			agentsPool = new ArrayList<Agents2>();
		//je höher die fitness eines agenten ist, desto öfter kommt er im pool vor und
		//desto höher ist die wahrscheinlichkeit, dass er gezogen wird (survival of the fittest)
			for(int i = 0; i < agents.size(); i++) {
				for(int j = 0; j < agents.get(i).fitness; j++) {
					agentsPool.add(agents.get(i));			
				}			
			}
		}
		
	//hätte jeder eine fitness von 0, wäre der pool leer und man könnte keine 2 agenten ziehen
		if(agentsPool.size()!=0) {
			chooseTwo.add(agentsPool.get(random.nextInt(agentsPool.size())));
			chooseTwo.add(agentsPool.get(random.nextInt(agentsPool.size())));
		}
		else {
			//hardcoded
			chooseTwo.add(agents.get(0));
			chooseTwo.add(agents.get(1));
		}
	}
	
	
	
	//mutation ratio d scalable from 1% to 100%
	//if mutation occurs, an angle of -359 to +395 multiplied by a magnitude(e.g. 0.02) is
	//applied on top of the existing angle
	public void mutation(double d) {
		if(d==0) {			
		}
		else if (d >= 0.01) {
			//if mutation occurs, all genes are altered by the same amount
			//this makes mutation more impacting and stops this
			//flickering movement from happening  after many generations
			if(!agentsArr.get(count).elite) {
				int x =  (random.nextInt( 100 )+1); 
				int x2 =(int) ((random.nextInt(360+359)-359)*(magnitude*3));
					if(x >= 1 && x <= d*100) {
						//mutation	
						for(int i = 0; i < genes.size(); i++) {
							agentsArr.get(count).genes.set(i, x2 + agentsArr.get(count).genes.get(i));
						}
					}
					
					//mutates the speed
					if(agentsArr.get(count).vel < 5) {
						if((random.nextInt(20)+1) == 1) {
							agentsArr.get(count).vel += 1 + (3 - 1) * random.nextDouble();
						}	
					}
			}
		}
	
		
		count++;
		
	
	}
	
	
	

	public boolean collision(int x) {
		 agentsArr.get(x).head = new ArrayList<Double>();
		 agentsHead(x);
		
	//target sqaure collision-requirements
	//if collided -> save the collision angle for correct drawing afterwards
		if((agentsArr.get(x).agentsX >= recX
			&& agentsArr.get(x).agentsX <= recX + recW
			&& agentsArr.get(x).agentsY >= recY
			&& agentsArr.get(x).agentsY <= recY + recH) ||
				
				(agentsArr.get(x).head.get(0) >= recX && agentsArr.get(x).head.get(0) <= recX+recW
				&& agentsArr.get(x).head.get(1) >= recY && agentsArr.get(x).head.get(1) <=recY+recH)) {

			agentsArr.get(x).finished=true;
			agentsArr.get(x).collided=true;
			agentsArr.get(x).lifetimeFinished=lifetime;
		
			if(agentsArr.get(x).collidedAngle==0) {
				agentsArr.get(x).collidedAngle = agentsArr.get(x).genes.get(genes.size()-this.lifetime);
			}
		}

		//collision requirements for hitting either of the 4 walls
		//if collided, save the angle of collision for drawing afterwards
		if(agentsArr.get(x).agentsX >= 720 || agentsArr.get(x).agentsX <= 0 
				|| agentsArr.get(x).agentsY >= 720 || agentsArr.get(x).agentsY <= 0
				
				|| agentsArr.get(x).head.get(0) >= 720 || agentsArr.get(x).head.get(0) <= 0 ||
						agentsArr.get(x).head.get(1) >= 720 || agentsArr.get(x).head.get(1) <= 0) {

			agentsArr.get(x).collided=true;
			agentsArr.get(x).finished=false;
			agentsArr.get(x).lifetimeFinished=0;
			agentsArr.get(x).elite=false;
			
			if(agentsArr.get(x).collidedAngle==0) {
				agentsArr.get(x).collidedAngle = agentsArr.get(x).genes.get(genes.size()-this.lifetime);
			}
		}
		
		
			
		return agentsArr.get(x).collided;
	}
	
	
	
	public void determineElite() {
		for(int i = 0; i < agentsArr.size()-1; i++) {
			for(int j = 0; j < agentsArr.size(); j++) {
				if(agentsArr.get(i).lifetimeFinished > 0 || agentsArr.get(j).lifetimeFinished >0) {
						if(agentsArr.get(i).lifetimeFinished >= agentsArr.get(j).
								lifetimeFinished) {
							agentsArr.get(i).elite=true;
							agentsArr.get(j).elite=false;
						}
						else {
							agentsArr.get(i).elite=false;
							agentsArr.get(j).elite=true;
							break;
						}
					
				}
			}
			if(agentsArr.get(i).elite) {
				break;
			}
		}
	}
	
	public ArrayList<Double> agentsHead(int x) {

		if(agentsArr.get(x).dirX == 0 && agentsArr.get(x).dirY == 1) {
			agentsArr.get(x).head.add(agentsArr.get(x).agentsX
						+ ((agentsArr.get(x).dirY*-1) * agentsArr.get(x).agentsW));
			agentsArr.get(x).head.add(agentsArr.get(x).agentsY + (agentsArr.get(x).dirX*agentsArr.get(x).agentsW));
		}
		else if(agentsArr.get(x).dirX == 1 && agentsArr.get(x).dirY == 0) {
			agentsArr.get(x).head.add(agentsArr.get(x).agentsX
					+ (agentsArr.get(x).dirY * agentsArr.get(x).agentsW));
			agentsArr.get(x).head.add(agentsArr.get(x).agentsY + ((agentsArr.get(x).dirX*-1)*agentsArr.get(x).agentsW));
		}
		else if(agentsArr.get(x).dirX == 0 && agentsArr.get(x).dirY == -1) {
			agentsArr.get(x).head.add(agentsArr.get(x).agentsX
						+ ((agentsArr.get(x).dirY*-1) * agentsArr.get(x).agentsW));
			agentsArr.get(x).head.add(agentsArr.get(x).agentsY + (agentsArr.get(x).dirX*agentsArr.get(x).agentsW));
		}
		else if(agentsArr.get(x).dirX == -1 && agentsArr.get(x).dirY == 0) {
			agentsArr.get(x).head.add(agentsArr.get(x).agentsX
					+ (agentsArr.get(x).dirY * agentsArr.get(x).agentsW));
			agentsArr.get(x).head.add(agentsArr.get(x).agentsY + (agentsArr.get(x).dirX*agentsArr.get(x).agentsW));
		}
		else if(agentsArr.get(x).dirX < 0 && agentsArr.get(x).dirY < 0) {
			agentsArr.get(x).head.add(agentsArr.get(x).agentsX
					+ ((agentsArr.get(x).dirY*-1) * agentsArr.get(x).agentsW));
			agentsArr.get(x).head.add(agentsArr.get(x).agentsY + (agentsArr.get(x).dirX*agentsArr.get(x).agentsW));
		}
		else if(agentsArr.get(x).dirX > 0 && agentsArr.get(x).dirY > 0) {
			agentsArr.get(x).head.add(agentsArr.get(x).agentsX
					+ ((agentsArr.get(x).dirY*-1) * agentsArr.get(x).agentsW));
			agentsArr.get(x).head.add(agentsArr.get(x).agentsY + (agentsArr.get(x).dirX*agentsArr.get(x).agentsW));
		}
		else if(agentsArr.get(x).dirX < 0 && agentsArr.get(x).dirY > 0) {
			agentsArr.get(x).head.add(agentsArr.get(x).agentsX
					+ ((agentsArr.get(x).dirY*-1) * agentsArr.get(x).agentsW));
			agentsArr.get(x).head.add(agentsArr.get(x).agentsY + (agentsArr.get(x).dirX*agentsArr.get(x).agentsW));
		}
		else if(agentsArr.get(x).dirX > 0 && agentsArr.get(x).dirY < 0) {
			agentsArr.get(x).head.add(agentsArr.get(x).agentsX
					+ ((agentsArr.get(x).dirY*-1) * agentsArr.get(x).agentsW));
			agentsArr.get(x).head.add(agentsArr.get(x).agentsY + (agentsArr.get(x).dirX*agentsArr.get(x).agentsW));
		}
		else {
			
		}
		
		return agentsArr.get(x).head;
	}
	
	public void colorizeEliteAgent(Graphics2D g2d) {
		g2d.setColor(Color.red);
		for(int agent = 0; agent < populationSize; agent++) {
			if(agentsArr.get(agent).elite) {
				rect = new Rectangle2D.Double(agentsArr.get(agent).agentsX, agentsArr.get(agent).agentsY
						, agentsArr.get(agent).agentsW, agentsArr.get(agent).agentsH);
		
				if(agentsArr.get(agent).collided) {
					g2d.rotate(Math.toRadians(agentsArr.get(agent).collidedAngle),
							agentsArr.get(agent).agentsX,agentsArr.get(agent).agentsY);
				}
				else {
					if(lifetime > 0) {
					g2d.rotate(Math.toRadians(agentsArr.get(agent).genes.get(genes.size()-lifetime)),
							agentsArr.get(agent).agentsX,agentsArr.get(agent).agentsY);
					}
				}
					g2d.fill(rect);
					g2d.draw(rect);
					break;
			}
		}
	}


}
