package com.sebastianwizert.alieninvasion;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.Timer;

import com.sebastianwizert.alieninvasion.Sound.Position;

public final class Player {
	
	
	
	//Class Variables
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 600;
	
	
	//Instance variables.
	private double x;
	private double y;
	private double deegre = 90;
	private int width;
	private int height;
	
	private int speed;
	private double health = 200;
	private double lastKnownHealth;
	private int numOfMissiles = 9999;
	private int numOfShockWaves = 9999;

	private boolean turnLeft;
	private boolean turnRight;
	private boolean forward;
	private boolean backward;
	private boolean playerHit;
	private boolean keepShooting;
	private boolean playerDead;

	private File playerImage = new File("gfx/ufo.png");
	private File playerImage2 = new File("gfx/ufo2.png");
	private BufferedImage ufoImage, ufoImage2;

	private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	

	private Timer shakeTimer;
	private int timesShaken;
	
	private long lastFire, firingInterval = 200;
	private String currentWeapon;
	
	//No args constructor.
	public Player(double newX, double newY) {
		//Starting position
		this.x = newX;
		this.y = newY;
		
		
		//Starting width/height.
		width = 100;
		height = 100;
		
		//How much to move every update.
		speed = 15; 
		
		try{
			ufoImage = ImageIO.read(playerImage);
			ufoImage2 = ImageIO.read(playerImage2);
		
		} catch(Exception e){
			System.out.println("file corupted");
		}
		
		//shake player when hit
		ActionListener shakerActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
					timesShaken++;
					//System.out.println("timesShaken-> "+timesShaken);
				}
			};   
		shakeTimer = new Timer(10, shakerActionListener);
	}
	
	
	public void shoot(String weaponType){

		// check that we have waiting long enough to fire
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}
		// if we waited long enough, create the shot entity, and record the time.
		lastFire = System.currentTimeMillis();

		if(weaponType.equals("bullet")){
			if(Setup.soundSwitch == true){
			 //new Sound("src/laser1.wav", Position.RIGHT).start(); 
				new Sound2("sfx/laser1.wav");
			}
			Weapon b = new Bullet((int)x, (int)y, deegre);
			weapons.add(b);
			//System.out.println("players coordinates passed to bullet x "+(int)x + " y "+(int)y+" deegre "+deegre);
		} else if(weaponType.equals("missile")){
			if(numOfMissiles > 0) {
				if(Setup.soundSwitch == true){
					//new Sound("src/rocket2.wav", Position.RIGHT).start(); 
					new Sound2("sfx/rocket2.wav");
				}
				Weapon m = new Missile((int)x, (int)y, deegre);
				weapons.add(m);
				numOfMissiles--;
			}
		} else if(weaponType.equals("shockWave")){
			if(numOfShockWaves > 0) {
				if(Setup.soundSwitch == true){
					//new Sound("src/expl1.wav", Position.RIGHT).start();
					new Sound2("sfx/expl1.wav");
				}
				Weapon sw = new ShockWave((int)x, (int)y);
				weapons.add(sw);
				numOfShockWaves--;
			}
		}
		
	}

	/*
	 * This method will update the position of the object. If they are pressing either the left/right/up/down key, then the shape will move.
	 * This method is called in the shapeUpdate() method of the ShapeAppPanel class.
	 */
	public void update() {
		
		if(keepShooting == true){
			shoot(currentWeapon);
		}
		
		if(health < 0){
			this.playerDead = true;
		} else {
			this.playerDead = false;
		}
		
		if(turnLeft) {
			deegre-=10;
		}
		if(turnRight){
			deegre+=10;
		}

		
		if(forward) {
		this.x +=  this.speed * Math.cos(Math.toRadians(this.deegre-90));
		this.y +=  this.speed * Math.sin(Math.toRadians(this.deegre-90));
		}
		if(backward) {
		this.x -=  this.speed * Math.cos(Math.toRadians(this.deegre-90));
		this.y -=  this.speed * Math.sin(Math.toRadians(this.deegre-90));
		}
		
		//Player vs boundries logic
		//System.out.println("player X " + x + " player Y " + y);
		if(x < 0) { 
			x=550;
		}		
		if(x + width > FRAME_WIDTH+50) { 
			x=0;	
		}
		if(y < 0) {
			y = 500; 
		}
		if(y + height >= FRAME_HEIGHT+50) {
			y = 0;
		}
		
		//Reset the values to 0.
		//dx = 0;
		//dy = 0;
		
		//player hit
		if(this.lastKnownHealth != this.health) {
			playerHit = true;
			this.lastKnownHealth = this.health;
		} else if(lastKnownHealth == this.health) {
			this.playerHit = false;
		}
		
		if(playerHit == true){
			shakeTimer.start();
		} else if(playerHit == false){
			shakeTimer.stop();
			timesShaken = 0;
		}
		//System.out.println("player hit -> "+playerHit+" lastKnownHealth -> "+ lastKnownHealth);
		//System.out.println("x " + x + "y "+y);
		//shake on hit
		for(int i=0; i<timesShaken; i++){
			if(timesShaken % 2 == 0){
				x+=0.9;
				y+=0.9;
			} else if(timesShaken % 2 != 0){
				x-=0.9;
				y-=0.9;
			}
		}
		
		//System.out.println("Player coordinates in update before cast x "+x + " y "+y);
		//System.out.println("Player coordinates in update after cast x "+(int)x + " y "+(int)y);
	}
	
	//Draw the shape of the object.
	public void draw(Graphics2D g) {

		if(deegre > 360) deegre = 0;
		if(deegre < - 360) deegre = 0;
		//Rotation information
		double rotationRequired = Math.toRadians(deegre);
		double locationX = ufoImage.getWidth() / 2;
		double locationY = ufoImage.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		
		int correction= -35;
		
		if(playerHit == true){
			g.drawImage(op.filter(ufoImage2, null), (int)x+correction, (int)y+correction,  null);
			this.playerHit = false;
		}else if(playerHit == false){
			g.drawImage(op.filter(ufoImage, null), (int)x+correction, (int)y+correction, null);
		}
		//g.setColor(Color.YELLOW);
		//g.fillOval((int)x, (int)y, 20, 20);	

	}
	
	public void rotateRight(double newDeegre){
		this.deegre += (newDeegre); 
	}
	public void rotateLeft(double newDeegre){
		this.deegre = this.deegre - (newDeegre); 
	}
	
	//Settings for the movement direction of the object.
	public void setForward (boolean forward){
		this.forward = forward;
	}
	public void setBackward (boolean backward) {
		this.backward = backward;
	}
	public void setTurnLeft(boolean turnLeft) {
		this.turnLeft = turnLeft;
	}
	public void setTurnRight(boolean turnRight) {
		this.turnRight = turnRight;
	}
	public double getX() {
		return this.x;
	}
	public void setX(double newX){
		this.x = newX;
	}
	public double getY() {
		return this.y;
	}
	public void setY(double newY){
		this.y = newY;
	}
	public ArrayList getWeapons(){
		return weapons;
	}

	public double getHealth() {
		return health;
	}
	public void setHealth(double d) {
		this.health = d;
	}
	public Double getDeegreAngle(){
		return this.deegre;
	}
	public int getNumOfMissiles(){
		return this.numOfMissiles;
	}
	public void setNumOfMissiles(int newNumOfMissiles){
		this.numOfMissiles = newNumOfMissiles;
	}
	public int getNumOfShockWaves(){
		return this.numOfShockWaves;
	}
	public void setNumOfShockWaves(int newNumOfShockWaves) {
		this.numOfShockWaves = newNumOfShockWaves;
	}
	public void setShot(boolean isShoting, String weaponType){
		this.currentWeapon = weaponType;
		this.keepShooting = isShoting;
	}

	public boolean isPlayerDead(){
		return this.playerDead;
	}
	public void setPlayerDead(boolean newPlayerDead){
		this.playerDead = newPlayerDead;
	}
	
}
