package com.sebastianwizert.alieninvasion;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public final class AlienSoldier extends Enemy {

	private double x;
	private double y;
	private double speed;
	private double direction=0; //direction in radians
	private boolean visible;
	private double health = 40;

	private double targetX= Double.NaN;
	private double targetY= Double.NaN;
	//private double numberOfHits=0;
	//private Color myColor = Color.GREEN;

	private File alienSoldier1wounded1 = new File("gfx/alienSoldier1wounded1.png");
	private File alienSoldier1wounded2 = new File("gfx/alienSoldier1wounded2.png");
	private File alienSoldier1wounded3 = new File("gfx/alienSoldier1wounded3.png");
	private File alienSoldier1wounded4 = new File("gfx/alienSoldier1wounded4.png");
	private File alienSoldier1wounded5 = new File("gfx/alienSoldier1wounded5.png");
	
	private File alienSoldier2wounded1 = new File("gfx/alienSoldier2wounded1.png");
	private File alienSoldier2wounded2 = new File("gfx/alienSoldier2wounded2.png");
	private File alienSoldier2wounded3 = new File("gfx/alienSoldier2wounded3.png");
	private File alienSoldier2wounded4 = new File("gfx/alienSoldier2wounded4.png");
	private File alienSoldier2wounded5 = new File("gfx/alienSoldier2wounded5.png");
	
	private BufferedImage alienSoldier1wounded1Image;
	private BufferedImage alienSoldier1wounded2Image;
	private BufferedImage alienSoldier1wounded3Image;
	private BufferedImage alienSoldier1wounded4Image;
	private BufferedImage alienSoldier1wounded5Image;
	
	private BufferedImage alienSoldier2wounded1Image;
	private BufferedImage alienSoldier2wounded2Image;
	private BufferedImage alienSoldier2wounded3Image;
	private BufferedImage alienSoldier2wounded4Image;
	private BufferedImage alienSoldier2wounded5Image;
	private double maxHealth;
	
	private ArrayList<Weapon> enemyWeapons = new ArrayList<Weapon>();
	private long firingInterval = 500;
	private long lastFire;
	//private Timer shootTimer;
	private int numOfMissiles= 99;
	private int numOfShockWaves= 99;
	private String currentWeapon = "missile";
	private boolean keepShooting = true;
	private boolean alienHit;
	private double lastKnownHealth;
	

	
	
		public AlienSoldier(double startX, double startY) {
			//Starting position

			this.x = startX;
			this.y = startY;
			this.visible = true;
			this.maxHealth = this.health;
			
			try{
				alienSoldier1wounded1Image = ImageIO.read(alienSoldier1wounded1);
				alienSoldier1wounded2Image = ImageIO.read(alienSoldier1wounded2);
				alienSoldier1wounded3Image = ImageIO.read(alienSoldier1wounded3);
				alienSoldier1wounded4Image = ImageIO.read(alienSoldier1wounded4);
				alienSoldier1wounded5Image = ImageIO.read(alienSoldier1wounded5);
				
				alienSoldier2wounded1Image = ImageIO.read(alienSoldier2wounded1);
				alienSoldier2wounded2Image = ImageIO.read(alienSoldier2wounded2);
				alienSoldier2wounded3Image = ImageIO.read(alienSoldier2wounded3);
				alienSoldier2wounded4Image = ImageIO.read(alienSoldier2wounded4);
				alienSoldier2wounded5Image = ImageIO.read(alienSoldier2wounded5);

				} catch(Exception e){
					System.out.println("file corupted");
				}
			
			setShotFrequency();
			
			//shake player when hit
			/*
			ActionListener shootActionListener = new ActionListener() {
			

				public void actionPerformed(ActionEvent actionEvent) {
						shoot("bullet");
						System.out.println("aliensoldier shooting");
					}
				};   
			shootTimer = new Timer(1000, shootActionListener);
			*/
		}
		
		private void setShotFrequency(){
			Random r = new Random();
			
			firingInterval =  r.nextInt(2000);
			if(firingInterval < 1000) {
				firingInterval = 1000;
			} 
			//System.out.println("firingInterval"+firingInterval);
		}

		
		public void setTarget(double x,double y,double speed) {
			this.targetX = x;
			this.targetY = y;
			this.speed= speed;
			
		}
	
		/*
		 * This method will update the position of the object. If they are pressing either the left/right/up/down key, then the shape will move.
		 * This method is called in the shapeUpdate() method of the ShapeAppPanel class.
		 */
		public void update() {
			
			if(keepShooting == true){
				shoot(currentWeapon);
			}

				//If we have target, go to target
				double dx = this.targetX-this.x;
				double dy = this.targetY-this.y;
				
				this.direction = Math.atan2(dy, dx);
				//this.direction = Math.toDegrees(direction);
							
			//Update coordinates with speed
			this.x +=  this.speed * Math.cos(this.direction);
			this.y +=  this.speed * Math.sin(this.direction);
			
			//this.x +=  this.speed * Math.cos(Math.toRadians(this.direction-90));
			//this.y +=  this.speed * Math.sin(Math.toRadians(this.direction-90));
			
			//alien soldier hit
			if(this.lastKnownHealth != this.health) {
				this.alienHit = true;
				this.lastKnownHealth = this.health;
			} else if(this.lastKnownHealth == this.health) {
				this.alienHit = false;
			}
			
			//System.out.println("AlieSoldier x "+x+" y "+y + " speed "+speed+ " direction "+direction);
		/*
			// Check bounds and add reflection from boundaries
			if (this.x > 1.0) {
				this.x = 1.0;
				
				//drop angle = reflection angle
				this.direction =  2 * this.direction + (Math.PI);
			} else if (this.x < 0.0) {
				this.x = 0.0;
				//drop angle = reflection angle
				this.direction =  2 * this.direction + (Math.PI);
			}
			
			if (this.y > 1.0) {
				this.y = 1.0;
				//drop angle = reflection angle
				this.direction =  2 * this.direction + (Math.PI);
			} else if (this.y < 0.0) {
				this.y = 0.0;
				//drop angle = reflection angle
				this.direction =  2 * this.direction + (Math.PI);
			}
	*/
			//let him shoot
			/*
			if(AlienPanel.state == AlienPanel.STATE.GAME){
				if(visible == true){
					shootTimer.start();
				}
			}
			
			if(AlienPanel.state != AlienPanel.STATE.GAME){
				shootTimer.stop();
			}
			if(this.visible == false) {
				shootTimer.stop();
			}
			*/
			
			//correct
			//System.out.println("AS coordinates in update before cast x "+x + " y "+y);
			//System.out.println("AS coordinates in update after cast x "+(int)x + " y "+(int)y);
		}
		
		public void setShot(boolean isShoting, String weaponType){
			this.currentWeapon = weaponType;
			this.keepShooting = isShoting;
		}
		
		public void shoot(String weaponType){

			// check that we have waiting long enough to fire
			if (System.currentTimeMillis() - lastFire < firingInterval) {
				return;
			}
			// if we waited long enough, create the shot entity, and record the time.
			lastFire = System.currentTimeMillis();

			this.direction = Math.toDegrees(direction)+90;
			
			if(weaponType.equals("bullet")){
				if(Setup.soundSwitch == true){
				 //new Sound("src/laser1.wav").start(); 
					new Sound2("sfx/laser1.wav");
				}
				Weapon b = new Bullet(x, y, direction);
				enemyWeapons.add(b);
				//System.out.println("coordinates passed to bullet x "+x + " y "+y+ " direction "+direction);
			} else if(weaponType.equals("missile")){
				if(numOfMissiles > 0) {
					if(Setup.soundSwitch == true){
						//new Sound("src/rocket2.wav").start(); 
						new Sound2("sfx/rocket2.wav");
					}
					Weapon m = new Missile((int)x, (int)y, direction);
					enemyWeapons.add(m);
					numOfMissiles--;
				}
			} else if(weaponType.equals("shockWave")){
				if(numOfShockWaves > 0) {
					if(Setup.soundSwitch == true){
						//new Sound("src/expl1.wav").start();
						new Sound2("sfx/expl1.wav");
					}
					Weapon sw = new ShockWave((int)x, (int)y);
					enemyWeapons.add(sw);
					numOfShockWaves--;
				}
			}
			
		}
		
		
		//Draw the shape of the object.
		public void draw(Graphics2D g) {
			
			if(direction > 360) direction = 0;
			if(direction < - 360) direction = 0;
			//Rotation information
			double rotationRequired = Math.toRadians(direction);
			
			double locationX = alienSoldier1wounded1Image.getWidth() / 2;
			double locationY = alienSoldier1wounded1Image.getHeight() / 2;
			AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			
			int correction = -25;
			
			//System.out.println("alien soldier  hit -> "+alienHit+" lastKnownHealth -> "+ lastKnownHealth+ " health -> "+health);
			if(alienHit == true){
				if(this.health < (this.maxHealth/100*30)){
					g.drawImage(op.filter(alienSoldier2wounded5Image, null), (int)x+correction, (int)y+correction, null);
				} else if(this.health < (this.maxHealth/100*50)){
					g.drawImage(op.filter(alienSoldier2wounded4Image, null), (int)x+correction, (int)y+correction, null);
				} else if(this.health < (this.maxHealth/100*70)){
					g.drawImage(op.filter(alienSoldier2wounded3Image, null), (int)x+correction, (int)y+correction, null);
				} else if(this.health < (this.maxHealth/100*90)){
					g.drawImage(op.filter(alienSoldier2wounded2Image, null), (int)x+correction, (int)y+correction, null);
				} else {
					g.drawImage(op.filter(alienSoldier2wounded1Image, null), (int)x+correction, (int)y+correction,  null);
				}
				this.alienHit = false;
			}else if(alienHit == false){
				if(this.health < (this.maxHealth/100*30)){
					g.drawImage(op.filter(alienSoldier1wounded5Image, null), (int)x+correction, (int)y+correction, null);
				} else if(this.health < (this.maxHealth/100*50)){
					g.drawImage(op.filter(alienSoldier1wounded4Image, null), (int)x+correction, (int)y+correction, null);
				} else if(this.health < (this.maxHealth/100*70)){
					g.drawImage(op.filter(alienSoldier1wounded3Image, null), (int)x+correction, (int)y+correction, null);
				} else if(this.health < (this.maxHealth/100*90)){
					g.drawImage(op.filter(alienSoldier1wounded2Image, null), (int)x+correction, (int)y+correction, null);
				} else {
					g.drawImage(op.filter(alienSoldier1wounded1Image, null), (int)x+correction, (int)y+correction, null);
				} 
			}

			
			/*
			if(numberOfHits == 0){
				g.drawImage(alienSoldierImage, (int)x+correction, (int)y+correction, null); 
			}
			if(numberOfHits > 0){
				g.drawImage(alienSoldierImage2, (int)x+correction, (int)y+correction, null);
				g.setColor(Color.RED);
				g.fillOval( (int)x+20+correction, (int)y+20+correction, 20, 15);
				if(numberOfHits > 4){
					g.fillOval( (int)x+20+correction, (int)y+35+correction, 20, 15);
					if(numberOfHits > 8){
						g.fillOval( (int)x+10+correction, (int)y+15+correction, 20, 35);
					}
				}
			}
			*/
			
			//show actual x, y position
			//g.setColor(Color.YELLOW);
			//g.fillOval( (int)x, (int)y, 20, 20);
		}
	

		public double getX() {
			return this.x;
		}
		public double getY() {
			return this.y;
		}
		public double getTargetX() {
			return this.targetX;
		}
		public double getTargetY() {
			return this.targetY;
		}
		public void setTargetX(double newTargetX) {
			this.targetX = newTargetX;
		}
		public void setTargetY(double newTargetY) {
			this.targetY = newTargetY;
		}
		
				
		public void setSpeed(double newSpeed) {
			 this.speed = newSpeed;
		}

		public double getSpeed() {
			
			return this.speed;
		}
		public void flipDirection(){
			direction = -direction;
		}
    /*
		public double getNumberOfHits() {
			return numberOfHits;
		}

		public void setNumberOfHits(double d) {
			this.numberOfHits = d;
		}
	*/
		public ArrayList<Weapon> getWeapons(){
			return this.enemyWeapons;
		}	
		public void setVisible(boolean newVisible){
			this.visible = newVisible;
		}
		@Override
		public double getHealth() {
			return this.health;
		}
		public void setHealth(double d) {
			this.health = d;
		}
}
