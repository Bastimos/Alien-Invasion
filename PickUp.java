package com.sebastianwizert.alieninvasion;

import java.awt.Graphics2D;

public abstract class PickUp {

  public abstract void draw(Graphics2D g);
	
	public abstract void update();
	
	public abstract boolean isVisible();
	
	public abstract double getX();
	
	public abstract double getY();
	
	public  abstract void setCollected(boolean newHitTarget);
}
