package com.sebastianwizert.alieninvasion;

import java.awt.Color;
import java.awt.Graphics2D;

public final class ShockWave extends Weapon{

	private double x;
	private double y;
	private double width = 1;
	private double height = 1;
	private double speed;
	private double moveBy;
	private boolean visible;
	private boolean hitTarget;

	public ShockWave(int startX, int startY) {
		this.x = startX;
		this.y = startY;
		speed = 15;
		visible = true;
	}
	public void update() {
		this.width += this.speed;
		this.height += this.speed;
		moveBy = Math.sqrt(this.speed)+(speed/5);
		this.x -= moveBy; 
		this.y -= moveBy;
		if(width > 400){
			visible = false;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		int correction=10;
		g.drawOval((int)x+correction, (int)y+correction, (int)width, (int)height);
		//centre
		//g.setColor(Color.YELLOW);
		//g.fillOval((int)x+correction, (int)y+correction, 20, 20);
	}

	public double getX() {
		return this.x;
	}


	public double getY() {
		return this.y;
	}

	public boolean isHitTarget(){
		return this.hitTarget;
	}
	public void setHitTarget(boolean newHitTarget){
		this.hitTarget= newHitTarget;
	}
	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;

	}
	public boolean isVisible() {
		return this.visible;
	}

}
