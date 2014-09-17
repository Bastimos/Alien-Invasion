package com.sebastianwizert.alieninvasion;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Alien {

	//Instance variables.
	private double x;
	private double y;
	private double speed;
	private double direction; //direction in radians
		
	
	// If target is not there value is NaN
	private double targetX= Double.NaN;
	private double targetY= Double.NaN;
	
	private Alien meFollows;
	
	//shape size and colour
	int myWidth = 10;
	int myHeight = 10;
	Color myColor = Color.GREEN;
	
	File f = new File("src/alien1.png");
	File f2 = new File("src/alien2.png");
	BufferedImage alienImage;
	BufferedImage alienImage2;
	
	private int numberOfHits=0;
		
		
		public Alien(double startX, double startY) {
			//Starting position

			this.x = startX;
			this.y = startY;
			
			
			try{
				alienImage = ImageIO.read(f);
				alienImage2 = ImageIO.read(f2);
				} catch(Exception e){
					System.out.println("file corupted");
				}
		}
		
		public void followAdult(Alien a){
			this.meFollows = a;
			
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
			//If we follow update target 
			if(meFollows != null){
				if(targetX == meFollows.getTargetX()) {
					this.targetX = meFollows.getX();
					this.targetY = meFollows.getY();
				}
				//System.out.println("got his target");
			}
			
			//If we have target, go to target
				double dx = this.targetX-this.x;
				double dy = this.targetY-this.y;
				
				double dist = (dx*dx) + (dy*dy);
				//slow down before target
				if (dist < (this.speed*this.speed)) this.speed = Math.sqrt(dist);
				
				this.direction = Math.atan2(dy, dx);
				
			
			
			//Update coordinates with speed
			this.x +=  this.speed * Math.cos(this.direction);
			this.y +=  this.speed * Math.sin(this.direction);
			
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
			
			
		}
		
		
		
		//Draw the shape of the object.
		public void draw(Graphics2D g) {
			
			int x,y;
			//Shape dimensions
			int width = 10;
			int height = 10;
			//getting frame dimensions
			Rectangle bounds = g.getDeviceConfiguration().getBounds();
			
			
			x=(int)((double)bounds.width * this.x);
			y=(int)((double)bounds.height * this.y);
			g.setColor(this.myColor);
			if(numberOfHits == 0){
				g.drawImage(alienImage, (int)x, (int)y, null); 
			}
			if(numberOfHits > 0){
				g.drawImage(alienImage2, (int)x, (int)y, null);
				g.setColor(Color.GREEN);
				g.fillOval( (int)x+20, (int)y+20, 20, 15);
				if(numberOfHits > 4){
					g.fillOval( (int)x+20, (int)y+35, 20, 15);
					if(numberOfHits > 8){
						g.fillOval( (int)x+10, (int)y+15, 20, 35);
					}
				}
			}
			
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
		
		public void setShape(int width, int height, Color c){
			this.myWidth = width;
			this.myHeight = height;
			this.myColor = c;
		}

		public double getSpeed() {
			
			return this.speed;
		}
		public void flipDirection(){
			direction = -direction;
		}

		public int getNumberOfHits() {
			return numberOfHits;
		}

		public void setNumberOfHits(int numberOfHits) {
			this.numberOfHits = numberOfHits;
		}
		
}
