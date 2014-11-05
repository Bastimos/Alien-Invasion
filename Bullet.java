package com.sebastianwizert.alieninvasion;

import java.awt.Graphics2D;

public final class Bullet extends Weapon{
	
	private double x;
	private double y;
	private double speed;
	private boolean visible;
	private boolean hitTarget;
	private double angleDeegre;

	public Bullet(double startX, double startY, double newAngleDeegre) {
		this.x = startX;
		this.y = startY;
		this.angleDeegre = newAngleDeegre;
		speed = 30;
		visible = true;
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
	public void draw(Graphics2D g){
		
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

	public boolean isHitTarget(){
		return this.hitTarget;
	}
	public void setHitTarget(boolean newHitTarget){
		this.hitTarget= newHitTarget;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isVisible() {
		return this.visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}


}
