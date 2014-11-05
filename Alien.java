package com.sebastianwizert.alieninvasion;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Alien extends Enemy{

	//Instance variables.
	private double x;
	private double y;
	private double speed;
	private double direction; //direction in radians
	private boolean visible;
	private double health = 20;
	
	// If target is not there value is NaN
	private double targetX= Double.NaN;
	private double targetY= Double.NaN;
	//private double numberOfHits=0;
	double lastKnownHealth;
	boolean alienHit;
	private Color myColor = Color.GREEN;
	
	private File alien1 = new File("gfx/alien1.png");
	private File alien1wounded1 = new File("gfx/alien1wounded1.png");
	private File alien1wounded2 = new File("gfx/alien1wounded2.png");
	private File alien1wounded3 = new File("gfx/alien1wounded3.png");
	private File alien1wounded4 = new File("gfx/alien1wounded4.png");
	private File alien2 = new File("gfx/alien2.png");
	private File alien2wounded1 = new File("gfx/alien2wounded1.png");
	private File alien2wounded2 = new File("gfx/alien2wounded2.png");
	private File alien2wounded3 = new File("gfx/alien2wounded3.png");
	private File alien2wounded4 = new File("gfx/alien2wounded4.png");
	private BufferedImage alien1Image;
	private BufferedImage alien1wounded1Image;
	private BufferedImage alien1wounded2Image;
	private BufferedImage alien1wounded3Image;
	private BufferedImage alien1wounded4Image;
	private BufferedImage alien2Image;
	private BufferedImage alien2wounded1Image;
	private BufferedImage alien2wounded2Image;
	private BufferedImage alien2wounded3Image;
	private BufferedImage alien2wounded4Image;
	private double maxHealth;
	
	private ArrayList<Weapon> enemyWeapons = new ArrayList<Weapon>();
	private long firingInterval = 500;
	private long lastFire;
	//private Timer shootTimer;
	private int numOfMissiles= 99;
	private int numOfShockWaves= 99;
	private String currentWeapon = "bullet";
	private boolean keepShooting = true;
	
	
		public Alien(double startX, double startY) {
			//Starting position

			this.x = startX;
			this.y = startY;
			this.maxHealth = this.health;
			
			try{
				alien1Image = ImageIO.read(alien1);
				alien1wounded1Image = ImageIO.read(alien1wounded1);
				alien1wounded2Image = ImageIO.read(alien1wounded2);
				alien1wounded3Image = ImageIO.read(alien1wounded3);
				alien1wounded4Image = ImageIO.read(alien1wounded4);
				alien2Image = ImageIO.read(alien2);
				alien2wounded1Image = ImageIO.read(alien2wounded1);
				alien2wounded2Image = ImageIO.read(alien2wounded2);
				alien2wounded3Image = ImageIO.read(alien2wounded3);
				alien2wounded4Image = ImageIO.read(alien2wounded4);
				} catch(Exception e){
					System.out.println("file corupted");
				}
			
			setShotFrequency();
		}
		
		private void setShotFrequency(){
			Random r = new Random();
			
			firingInterval =  r.nextInt(2000);
			if(firingInterval < 1000) {
				firingInterval = 1000;
			} 
			//System.out.println("firingInterval"+firingInterval);
		}
		public void setShot(boolean isShoting, String weaponType){
			this.currentWeapon = weaponType;
			this.keepShooting = isShoting;
		}
		public void shoot(String weaponType){

			// check that we have waited long enough to fire
			if (System.currentTimeMillis() - lastFire < firingInterval) {
				return;
			}
			// if we waited long enough, create the shot entity, and record the time.
			lastFire = System.currentTimeMillis();

			this.direction = Math.toDegrees(direction)+90;
			
			if(weaponType.equals("bullet")){
				if(Setup.soundSwitch == true){
				 
					new Sound2("sfx/laser1.wav");
				}
				Weapon b = new Bullet(x, y, direction);
				enemyWeapons.add(b);
				//System.out.println("coordinates passed to bullet x "+x + " y "+y+ " direction "+direction);
			} else if(weaponType.equals("missile")){
				if(numOfMissiles > 0) {
					if(Setup.soundSwitch == true){
					
						new Sound2("sfx/rocket2.wav");
					}
					Weapon m = new Missile((int)x, (int)y, direction);
					enemyWeapons.add(m);
					numOfMissiles--;
				}
			} else if(weaponType.equals("shockWave")){
				if(numOfShockWaves > 0) {
					if(Setup.soundSwitch == true){
					
						new Sound2("sfx/expl1.wav");
					}
					Weapon sw = new ShockWave((int)x, (int)y);
					enemyWeapons.add(sw);
					numOfShockWaves--;
				}
			}
			
		}
	
		public void update() {
			
			if(keepShooting == true){
				shoot(currentWeapon);
			}

				//If we have target, go to target
				double dx = this.targetX-this.x;
				double dy = this.targetY-this.y;
				
				this.direction = Math.atan2(dy, dx);
							
			//Update coordinates with speed
			this.x +=  this.speed * Math.cos(this.direction);
			this.y +=  this.speed * Math.sin(this.direction);
			
			//alien hit
			if(this.lastKnownHealth != this.health) {
				this.alienHit = true;
				this.lastKnownHealth = this.health;
			} else if(this.lastKnownHealth == this.health) {
				this.alienHit = false;
			}
			
		}
		
		//Draw the shape of the object.
		public void draw(Graphics2D g) {
			
			if(direction > 360) direction = 0;
			if(direction < - 360) direction = 0;
			//Rotation information
			double rotationRequired = Math.toRadians(direction);
			
			double locationX = alien1Image.getWidth() / 2;
			double locationY = alien1Image.getHeight() / 2;
			AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
						
			int correction = -25;
			
			//System.out.println("alien hit -> "+alienHit+" lastKnownHealth -> "+ lastKnownHealth+ " health -> "+health);
			if(alienHit == true){
				if(this.health < (this.maxHealth/100*30)){
					g.drawImage(op.filter(alien2wounded4Image, null), (int)x+correction, (int)y+correction, null);
				} else if(this.health < (this.maxHealth/100*50)){
					g.drawImage(op.filter(alien2wounded3Image, null), (int)x+correction, (int)y+correction, null);
				} else if(this.health < (this.maxHealth/100*70)){
					g.drawImage(op.filter(alien2wounded2Image, null), (int)x+correction, (int)y+correction, null);
				} else if(this.health < (this.maxHealth/100*90)){
					g.drawImage(op.filter(alien2wounded1Image, null), (int)x+correction, (int)y+correction, null);
				} else {
					g.drawImage(op.filter(alien2Image, null), (int)x+correction, (int)y+correction,  null);
				}
				this.alienHit = false;
			}else if(alienHit == false){
				if(this.health < (this.maxHealth/100*30)){
					g.drawImage(op.filter(alien1wounded4Image, null), (int)x+correction, (int)y+correction, null);
				} else if(this.health < (this.maxHealth/100*50)){
					g.drawImage(op.filter(alien1wounded3Image, null), (int)x+correction, (int)y+correction, null);
				} else if(this.health < (this.maxHealth/100*70)){
					g.drawImage(op.filter(alien1wounded2Image, null), (int)x+correction, (int)y+correction, null);
				} else if(this.health < (this.maxHealth/100*90)){
					g.drawImage(op.filter(alien1wounded1Image, null), (int)x+correction, (int)y+correction, null);
				} else {
					g.drawImage(op.filter(alien1Image, null), (int)x+correction, (int)y+correction, null);
				} 
			}
		
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
		@Override
		public ArrayList getWeapons() {
			// TODO Auto-generated method stub
			return this.enemyWeapons;
		}
		public void setVisible(boolean newVisible){
			this.visible = newVisible;
		}

		@Override
		public double getHealth() {
			return this.health;
		}
		public void setHealth(double d){
			this.health = d;
		}
		
		public void setTarget(double x,double y,double speed) {
			this.targetX = x;
			this.targetY = y;
			this.speed= speed;
		}
		
		
}
