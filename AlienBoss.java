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

public final class AlienBoss extends Enemy {

	private double x;
	private double y;
	private double speed;
	private double direction=0; //direction in radians
	private boolean visible;
	private double AlienBossHealth = 500;

	private double targetX= Double.NaN;
	private double targetY= Double.NaN;
	private double numberOfHits=0;
	private Color myColor = Color.GREEN;

	private File f = new File("gfx/boss1new.png");
	private File f2 = new File("gfx/boss2new.png");
	private BufferedImage alienBossImage;
	private BufferedImage alienBossImage2;
	
	private ArrayList<Weapon> enemyWeapons = new ArrayList<Weapon>();
	private long firingInterval = 3000;
	private long lastFiredShotInterval;
	private long lastCreatedEnemyInterval;
	//private Timer newAlienCreationTimer;
	private int numOfMissiles= 99999;
	private int numOfShockWaves= 99999;
	private String currentWeapon = "shockWave";
	private boolean keepShooting = true;
	private double lastKnownHealthOfAlienBoss;
	private boolean alienHit;
	private double maxHealth;
	
	Player player;
	Enemy enemy;
	
	
	public AlienBoss(double startX, double startY) {
		//Starting position

		this.x = startX;
		this.y = startY;
		this.visible = true;
		this.maxHealth = this.AlienBossHealth;

		try{
			alienBossImage = ImageIO.read(f);
			alienBossImage2 = ImageIO.read(f2);
		} catch(Exception e){
			System.out.println("file corupted");
		}
		
		setShotFrequency();

		//spawn new alien
		/*
		ActionListener spawnNewAlienActionListener = new ActionListener() {


			public void actionPerformed(ActionEvent actionEvent) {
				spawnNewAlien();
				System.out.println("alien created");
			}
		};   
		newAlienCreationTimer = new Timer(2000, spawnNewAlienActionListener);
		newAlienCreationTimer.start();
		*/
	} 

	public void spawnNewAlien(){
		if (System.currentTimeMillis() - lastCreatedEnemyInterval < firingInterval) {
			return;
		}
		// if we waited long enough, create the shot entity, and record the time.
		lastCreatedEnemyInterval = System.currentTimeMillis();
		
		enemy = new AlienSoldier(this.x, this.y);
		AlienPanel.dynamicNumberOfAliens++;
		if(null != player) {
			enemy.setTarget(player.getX(), player.getY(), 1);
		}
		AlienPanel.enemies.add(enemy);
		System.out.println("AlieSoldier created");
		return;
	}

		public void setTarget(double x,double y,double speed) {
			this.targetX = x;
			this.targetY = y;
			this.speed= speed;
		}
		
		private void setShotFrequency(){
			Random r = new Random();
			
			firingInterval =  r.nextInt(6000);
			if(firingInterval < 3500) {
				firingInterval = 3500;
			} 
			System.out.println("boss firingInterval"+firingInterval);
		}
	
		/*
		 * This method will update the position of the object. If they are pressing either the left/right/up/down key, then the shape will move.
		 * This method is called in the shapeUpdate() method of the ShapeAppPanel class.
		 */
		public void update() {
			
			if(keepShooting){
				this.spawnNewAlien();
				this.shoot(currentWeapon);
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
			
			//alien boss hit
			if(this.lastKnownHealthOfAlienBoss != this.AlienBossHealth) {
				this.alienHit = true;
				this.lastKnownHealthOfAlienBoss = this.AlienBossHealth;
			} else if(this.lastKnownHealthOfAlienBoss == this.AlienBossHealth) {
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
			if (System.currentTimeMillis() - lastFiredShotInterval < firingInterval) {
				return;
			}
			// if we waited long enough, create the shot entity, and record the time.
			lastFiredShotInterval = System.currentTimeMillis();
			
			System.out.println("begining of the shot method");

			this.direction = Math.toDegrees(direction)+90;
			
			if(weaponType.equals("bullet")){
				if(Setup.soundSwitch == true){
				// new Sound("src/laser1.wav").start(); 
					new Sound2("sfx/laser1.wav");
				}
				Weapon b = new Bullet(x, y, direction);
				enemyWeapons.add(b);
				//System.out.println("coordinates passed to bullet x "+(int)x + " y "+(int)y+ " direction "+direction);
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
			
			return;
		}
		
		
		//Draw the shape of the object.
		public void draw(Graphics2D g) {
			
			if(direction > 360) direction = 0;
			if(direction < - 360) direction = 0;
			//Rotation information
			double rotationRequired = Math.toRadians(direction);
			
			double locationX = alienBossImage.getWidth() / 2;
			double locationY = alienBossImage.getHeight() / 2;
			AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			
			int correction = -100;
			
			//if(numberOfHits == 0){
				//g.drawImage(alienBossImage, (int)x+correction, (int)y+correction, null); 
			//}
			
			//draw wounds with hits
			/*
			if(numberOfHits > 0){
				//g.drawImage(alienBossImage2, (int)x+correction, (int)y+correction, null);
				g.setColor(Color.MAGENTA);
				g.fillOval( (int)x+correction+120, (int)y+correction+ 70, 20, 15);
				if(numberOfHits > 4){
					g.fillOval( (int)x+correction+130, (int)y+correction+80, 20, 15);
					if(numberOfHits > 8){
						g.fillOval( (int)x+correction+90, (int)y+correction+100, 20, 15);
					}
				}
			}
			*/
			
			//check THIS alien boss health set wrongly
			//System.out.println("alien boss  hit -> "+alienHit+" lastKnownHealth -> "+ this.lastKnownHealthOfAlienBoss+ " health -> "+this.AlienBossHealth);
			if(alienHit == true){
				g.drawImage(op.filter(alienBossImage2, null), (int)x+correction, (int)y+correction,  null);
				this.alienHit = false;
			}else if(alienHit == false){
				g.drawImage(op.filter(alienBossImage, null), (int)x+correction, (int)y+correction, null);
			}
			/*
			//show actual x, y position
			g.setColor(Color.YELLOW);
			g.fillOval( (int)x, (int)y, 20, 20);
			//first arm position / bottom - right
			g.setColor(Color.BLUE);
			g.fillOval( (int)x+25, (int)y+40, 20, 20);
			*/
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
			this.x = newY;
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
			this.direction = -direction;
		}

		public double getNumberOfHits() {
			return numberOfHits;
		}

		public void setNumberOfHits(double d) {
			this.numberOfHits = d;
		}
		public ArrayList<Weapon> getWeapons(){
			return this.enemyWeapons;
		}	
		public void setVisible(boolean newVisible){
			this.visible = newVisible;
		}
		@Override
		public double getHealth() {
			return this.AlienBossHealth;
		}
		public void setHealth(double d) {
			this.AlienBossHealth = d;
		}
}
