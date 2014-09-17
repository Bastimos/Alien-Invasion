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

public class Player {
	
	//Class Variables
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 600;
	
	
	//Instance variables.
	private double x;
	private double y;
	private int width;
	private int height;
	
	private int speed;
	private int health = 200;
	private int lastKnownHealth;

	private boolean turnLeft;
	private boolean turnRight;
	private boolean forward;
	private boolean backward;
	private boolean playerHit;
	private boolean playersDeath;
	private int dx = 0;
	private int dy = 0;
	private double deegre = 0;
	
	File f = new File("src/ufo.png");
	File f2 = new File("src/ufo2.png");
	BufferedImage ufoImage, ufoImage2;
	
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();

	private Timer shakeTimer;
	int timesShaken;
	
	
	//No args constructor.
	public Player() {
		//Starting position
		x = 300; 
		y = 300;
		
		//Starting width/height.
		width = 100;
		height = 100;
		
		//How much to move every update.
		speed = 15; 
		
		try{
			ufoImage = ImageIO.read(f);
			ufoImage2 = ImageIO.read(f2);
		
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
	
	public void shoot(){
		Bullet b = new Bullet((int)x, (int)y, deegre);
		bullets.add(b);
	}
	
	/*
	 * This method will update the position of the object. If they are pressing either the left/right/up/down key, then the shape will move.
	 * This method is called in the shapeUpdate() method of the ShapeAppPanel class.
	 */
	public void update() {
		

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
		
		
		if(x < 0) { // If they hit the left border, then set the position of x to 0.
			x = 0;
		}
		
		if(x + width > FRAME_WIDTH) { //If they hit the right border, then set the position of x to the width of the panel - the width of the object.
			x = FRAME_WIDTH - width;
		}
		
		//If they hit the top border, then set the position of u to 0.
		if(y < 0) {
			y = 0; 
		}
		
		//If they hit the bottom border, then set the position of y to the height of the panel - the height of the object.
		if(y + height >= FRAME_HEIGHT) {
			y = FRAME_HEIGHT - height;
		}
		
		//Reset the values to 0.
		dx = 0;
		dy = 0;
		
		//player hit
		if(lastKnownHealth != this.health) {
			playerHit = true;
			lastKnownHealth = this.health;
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
	public double getY() {
		return this.y;
	}
	public ArrayList getBullets(){
		return bullets;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	
}
