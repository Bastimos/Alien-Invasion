package com.sebastianwizert.alieninvasion;

import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class Enemy {

	public abstract void draw(Graphics2D g);
	
	public abstract void update();
	
	public abstract double getX();
	
	public abstract double getY();
	
	public abstract void setTarget(double x,double y,double speed);

	public abstract ArrayList getWeapons();
	
	public abstract void setVisible(boolean newVisible);
	
	public abstract double getHealth();
	
	public abstract void setHealth(double d);
}
