package com.sebastianwizert.alieninvasion;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public final class Missile extends Weapon{

	private double x;
	private double y;
	private double speed;
	private boolean visible;
	private boolean hitTarget;
	private double angleDeegre;
	private double deegre;
	
	//private Player player;
	private File mi = new File("gfx/missile1.png");
	private BufferedImage missileImage;
	

	public Missile(int startX, int startY, double newAngleDeegre) {
		this.x = startX;
		this.y = startY;
		this.angleDeegre = newAngleDeegre;
		speed = 20;
		visible = true;
		
		try{
			missileImage = ImageIO.read(mi);
		
		} catch(Exception e){
			System.out.println("file corupted");
		}
	}

	public void update(){
		
		this.x +=  this.speed * Math.cos(Math.toRadians(this.angleDeegre-90));
		this.y +=  this.speed * Math.sin(Math.toRadians(this.angleDeegre-90));
		
		//y -= speedX;
		if(hitTarget){
			visible = false;
		}
		if(y>600 || x>600 || x<0 || y<0){
			visible = false;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		deegre = angleDeegre;
		
		if(deegre > 360) deegre = 0;
		if(deegre < - 360) deegre = 0;
		//Rotation information
		double rotationRequired = Math.toRadians(deegre);
		double locationX = missileImage.getWidth() / 2;
		double locationY = missileImage.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		
		int correction= 0;   // -35;
	
		g.drawImage(op.filter(missileImage, null), (int)x+correction, (int)y+correction,  null);
	}
	
	
	public double getX() {
		 return this.x;
	 }


	 public double getY() {
		 return this.y;
	 }


	 public double getSpeed() {
		 return this.speed;
	 }


	 public boolean isVisible() {
		 return this.visible;
	 }
	 
	 public boolean isHitTarget(){
		 return this.hitTarget;
	 }
	 public void setHitTarget(boolean newHitTarget){
		 this.hitTarget= newHitTarget;
	 }


	 public void setX(int x) {
		 this.x = x;
	 }


	 public void setY(int y) {
		 this.y = y;
	 }


	 public void setSpeed(int speed) {
		 this.speed = speed;
	 }


	 public void setVisible(boolean visible) {
		 this.visible = visible;
	 }

}
