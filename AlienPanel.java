package com.sebastianwizert.crowdpanic;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;



public class AlienPanel extends JPanel implements Runnable, KeyListener{

	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	
	//Calendar calendar = new GregorianCalendar();
	//final javax.swing.Timer countdownTimer =  new javax.swing.Timer(1000, null);

	ArrayList <Alien> alienList;
	ArrayList<Bullet> bullets;
	
	Thread thread;
	Player player;
	
	private boolean running;
	
	Graphics2D g;
	BufferedImage image;
	
	Alien autoAlien;
	
	File f = new File("src/spaceBg.png");
	BufferedImage bgImage;
	int deegre;
	int numberOfAliensAtStart =15;
	int dynamicNumberOfAliens ;
	int timeLimit = 150;
	int gameDurationTime;
	int waveCount = 1;
	String displayScore;
	String displayTimeLimit= "";
	Timer timer2;
	boolean wrongPosition;
	
	
	
	private int FPS = 30;

	//No args constructor.
	public AlienPanel() {
		this.setBounds(0, 0, WIDTH, HEIGHT);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		this.requestFocus();
		
		try{
			bgImage = ImageIO.read(f);
		} catch(Exception e){
			System.out.println("file corupted");
		}
		
		//Time Limit
		ActionListener timerActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				
				if(timeLimit == 0 ){
					player.setHealth(0);
					timeLimit = timeLimit + gameDurationTime;
					gameDurationTime = 0;	
				}
					timeLimit--;
					gameDurationTime++;
					StringBuilder timeBuilder = new StringBuilder();
					timeBuilder.append(timeLimit);
					displayTimeLimit = timeBuilder.toString();
					
					//System.out.println("Timer "+ displayTimeLimit + " timeLimit--" + timeLimit+ "   gameDurationTime -->"+gameDurationTime);
				}
			};   
		timer2 = new Timer(1000, timerActionListener);
	}
	
	public static enum STATE{
		MENU,
		GAME;
	}
	private enum WAVE {
		WAVE1,WAVE2,WAVE3;
	}
	WAVE wave = WAVE.WAVE1;
	
	public static STATE state = STATE.MENU;
	private Menu menu;
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) { //If the thread hasn't been created yet, create and then start it.
			thread = new Thread(this); 
			thread.start();
		}
		addKeyListener(this); //Add the key listener
		addMouseListener(new MouseInput());
	}
	
	@Override
	public void run() {
		running = true;
		
		//create a new BufferedImage object with the specified width, height and image type.
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		//Create a new Graphics2D object with the graphics returned using the .getGraphics method on the image object.
		g = (Graphics2D) image.getGraphics();
		
		long startTime = 0L; 
		long URDTime = 0L;
		long waitTime = 0L;
		long totalTime = 0L;
		
		int targetTime = 1000 / FPS;
		int frameCount = 0;
		int maxFrameCount = FPS;
		double averageFPS = 0;
		
		player = new Player();
		menu = new Menu();
		
		createAliens();

		while(running) {
			
			startTime = System.nanoTime();
			if(state == STATE.GAME){
				shapeUpdate(); //Call the shapeUpdate (see method for more information).
				shapeRender(); //Call the shapeRender (see method for more information).
				timer2.start();
			} else if(state == STATE.MENU){
				menu.render(g);
				timer2.stop();
				
			}
			shapeDraw();   //Call the shapeDraw (see method for more information).
			URDTime = (System.nanoTime() - startTime) / 1000000;  
			
			waitTime = targetTime - URDTime;
			
			//System.out.println("Wait Time: " + waitTime);
			
			try { //Depending on the waitTime, make the thread sleep for that length.
				thread.sleep(waitTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			totalTime += (System.nanoTime() - startTime);
			frameCount++;
			
			if(frameCount == maxFrameCount) {
				//Calculating the average FPS.
				averageFPS = 1000.0 / ((totalTime/frameCount) / 1000000);
				frameCount = 0;
				totalTime = 0;
			}
			//System.out.println("Average FPS: " + averageFPS);	
			
			//bullets validation
			bullets = player.getBullets();
			for (int i=0; i<bullets.size(); ++i) {
				Bullet b = (Bullet)bullets.get(i);
				if(b.isVisible()==true){
					b.update();
				} else {
					bullets.remove(i);
				}
			}
			
			//logic
			if(dynamicNumberOfAliens == 0){
				waveCount++;
				createAliens();
			}
			//wave modifier
			if(waveCount == 1){
				wave = WAVE.WAVE1;
			} else if(waveCount == 2){
				wave = WAVE.WAVE2;
			} else if(waveCount >= 3){
				wave = WAVE.WAVE3;
			}
			
			// if player dies
			//System.out.println("health -> " + player.getHealth());
			if(player.getHealth() == 0){
				state = STATE.MENU;
				waveCount = 1;
				createAliens();
				timeLimit = timeLimit + gameDurationTime;
				gameDurationTime = 0;
				
			}
		}
	}
	
	public void createAliens (){
		if(wave == WAVE.WAVE1){
			timer2.restart();
			player.setHealth(200);
			numberOfAliensAtStart = 15;
			
		} else if(wave == WAVE.WAVE2){
			numberOfAliensAtStart = 30;
		} else if(wave == WAVE.WAVE3){
			numberOfAliensAtStart = 60;
		}
		dynamicNumberOfAliens = numberOfAliensAtStart;
		double awayFromPlayerX = player.getX()*0.001;
		double awayFromPlayerY = player.getY()*0.001;
		System.out.println("awayFromPlayerX "+awayFromPlayerX+"  awayFromPlayerY "+awayFromPlayerY);
		alienList = new ArrayList<Alien>();
		Random r = new Random();
		for(int i=0; i<dynamicNumberOfAliens; ++i){

			double newAlienX = r.nextDouble()*1.0;
			double newAlienY = r.nextDouble()*1.0;
			autoAlien = new Alien(newAlienX, newAlienY);
			alienList.add(autoAlien);

		}
	}
	
	/*
	 * This method will update the coordinates, etc., of every object that needs to be updated.
	 * In this case, only the shape object is updated as it's the only object on the screen and that moves. 
	 */
	public void shapeUpdate() {
		 
		double AlienDistanceToAlienX = 0;
		double AlienDistanceToAlienY = 0;
		double AlienDistanceToAlient = 0;
		
		int score = alienList.size();
		StringBuilder builder = new StringBuilder();
		builder.append(score);
		displayScore = builder.toString();
		
		player.update();
		
		for(int i=0; i<alienList.size(); ++i){
			if(i!=1 | i!=2 | i!=3) {
				alienList.get(i).setTarget(player.getX()/WIDTH,player.getY()/HEIGHT,0.0005);
				alienList.get(i).update();	
			}
		}
		
		//collisions
		double tempX, tempY;
		Alien a = null;
	
		for(int i=0; i < alienList.size(); ++i) {

			tempX = alienList.get(i).getX()*WIDTH;
			tempY = alienList.get(i).getY()*HEIGHT;
			a = alienList.get(i);
			
			//allien to player collisions
			double AlienDistanceToPlayerX = tempX - player.getX();
			double AlienDistanceToPlayerY = tempY - player.getY();
			double AlienDistanceToPlayer = Math.sqrt((AlienDistanceToPlayerX*AlienDistanceToPlayerX)+
												(AlienDistanceToPlayerY*AlienDistanceToPlayerY));
			//System.out.println("alien"+i+" distance to player =" + AlienDistanceToPlayer);
			if(AlienDistanceToPlayer < 70) {
				player.setHealth(player.getHealth()-5);
				a.setNumberOfHits(a.getNumberOfHits()+3);
				if(a.getNumberOfHits() > 10){
					alienList.remove(i);
					dynamicNumberOfAliens--;
				}
			}
			
			//alien to bullet collisions
			if(null != bullets){
				for(int j=0; j<bullets.size(); j++){
					double tempBulletX, tempBulletY;
					tempBulletX = bullets.get(j).getX();
					tempBulletY = bullets.get(j).getY();
					double AlienDistanceToBulletX = tempX - tempBulletX;
					double AlienDistanceToBulletY = tempY - tempBulletY;
					double AlienDistanceToBullet = Math.sqrt((AlienDistanceToBulletX*AlienDistanceToBulletX)+
																	(AlienDistanceToBulletY*AlienDistanceToBulletY));
					//System.out.println("alien"+ i+ " to bullet distance"  + AlienDistanceToBullet);
					if(AlienDistanceToBullet < 50){
						//System.out.println("BOOOOMMMMMMM");
						a.setNumberOfHits(a.getNumberOfHits()+1);
						if(a.getNumberOfHits() > 10){
							bullets.get(j).setHitTarget(true);
							try{
								alienList.remove(i); // this might try to remove alien that is already dead :)
							} catch (IndexOutOfBoundsException ioobe) {
								System.out.println("Alien"+i+" is already DEAD !!!");
							}
							dynamicNumberOfAliens--;
						}
					}
				}
			}
			
			//alien to alien collisions
			for(Alien an : alienList) {

					if(an != a){
						
						if(tempX != an.getX() && tempY != an.getY() ) {
							AlienDistanceToAlienX = tempX - an.getX();
							AlienDistanceToAlienY = tempY - an.getY();
							AlienDistanceToAlient = Math.sqrt((AlienDistanceToAlienX*AlienDistanceToAlienX) + 
																(AlienDistanceToAlienY*AlienDistanceToAlienY));
							
							if(AlienDistanceToAlient < 0.1){
								//alien to alien collisions
								//an.setSpeed(-an.getSpeed());
								//an.flipDirection();
							}	
						}
					}
			}
			AlienDistanceToAlienX = 0;
			AlienDistanceToAlienY = 0;
			AlienDistanceToAlient = 0;
		}
	}
	
	/*
	 * This method will render the Graphics2D object we have. It will render anything that we need rendered in the background so we can just
	 * copy it from the background and show it on the screen so it looks smoother.
	 */
	public void shapeRender() { 
		g.drawImage(bgImage, 0, 0, null);
		
		for(int i=0; i<alienList.size(); ++i){
			if(i!=1 | i!=2 | i!=3) {
				alienList.get(i).draw(g);
			}
		}
		
		ArrayList bullets = player.getBullets();
		for(int i=0; i<bullets.size(); ++i){
			Bullet b = (Bullet)bullets.get(i);
			g.setColor(Color.YELLOW);
			g.fillOval((int)b.getX()+30, (int)b.getY()+30, 10, 5);
			g.fillOval((int)b.getX()+50, (int)b.getY()+30, 10, 5);
		}
		
		g.setFont(new Font("Purisa", Font.PLAIN, 40));
		g.drawString("Aliens "+displayScore, 50, 550);
		g.drawString("Time " + displayTimeLimit, 300, 50);
		player.draw(g);
		
		g.setColor(Color.GRAY);
		g.fillRect(5, 5, 200, 25);
		
		g.setColor(Color.GREEN);
		g.fillRect(5, 5, player.getHealth(), 25);
		
		g.setColor(Color.WHITE);
		g.drawRect(5, 5, 200, 25);
		
		g.setFont(new Font("Purisa", Font.PLAIN, 40));
		g.setColor(Color.YELLOW);
		g.drawString("WAVE "+waveCount, 5, 100);
	}
	
	/*
	 * This method will take whatever was rendered in the background (from the shapeRender() method) and copy it to the screen.
	 */
	public void shapeDraw() {
		//System.out.println("Shape drawn");
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		
		
	}
	@Override
	public void keyPressed(KeyEvent key) {
		int keyCode = key.getKeyCode();
		if(state == STATE.GAME){
			switch(keyCode) {// If the user presses the appropriate key, then set the shapes boolean value to true.

				case KeyEvent.VK_LEFT: { player.setTurnLeft(true); break;}
				case KeyEvent.VK_RIGHT: { player.setTurnRight(true); break;}
				case KeyEvent.VK_UP: { player.setForward(true); break;}
				case KeyEvent.VK_DOWN: { player.setBackward(true); break;}
				case KeyEvent.VK_CONTROL: { player.shoot(); break; }
				case KeyEvent.VK_ESCAPE: { state = STATE.MENU; break;}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent key) { // If the user releases the appropriate key, then set the shapes boolean value to false.
		int keyCode = key.getKeyCode();
		if(state == STATE.GAME || state == STATE.MENU){
			switch(keyCode) {
				
				case KeyEvent.VK_LEFT:  { player.setTurnLeft(false); break;}
				case KeyEvent.VK_RIGHT: { player.setTurnRight(false); break;}
				case KeyEvent.VK_UP: { player.setForward(false); break;}
				case KeyEvent.VK_DOWN: { player.setBackward(false); break;}
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent key) {
		// TODO Auto-generated method stub

	}

	public int getNumberOfAliens(){
		return this.dynamicNumberOfAliens;
	}
}
