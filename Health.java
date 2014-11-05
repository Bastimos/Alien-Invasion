package com.sebastianwizert.alieninvasion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public final class Health extends PickUp{

	private double x;
	private double y;
	private boolean visible;
	private boolean collected;
	private int bonusHealth;
	
	private Timer timer;
	private int initialDisplayTime = 3;
	private int displayedFor;
	private File healthFile = new File("gfx/health.png");
	private BufferedImage healthImage;
	
	public Health(double newX, double newY, int healthBonus){
		this.x = newX;
		this.y = newY;
		this.bonusHealth = healthBonus;
		this.visible = true;

		//set visible after time runs out
		ActionListener shakerActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				displayedFor++;
				if(displayedFor >= initialDisplayTime){
					visible = false;
				}
			}
		};   
		timer = new Timer(1000, shakerActionListener);
		
		try{
			healthImage = ImageIO.read(healthFile);
			} catch(Exception e){
				System.out.println("file corupted");
			}
	}


	@Override
	public void draw(Graphics2D g) {
		
		g.drawImage(healthImage, (int)x, (int)y, null);
		Font fnt0 = new Font("arial", Font.BOLD, 10);
		g.setFont(fnt0);
		g.setColor(Color.WHITE);
		String displayHealth = "+"+bonusHealth+" HEALTH";
		g.drawString(displayHealth, (int)x - 12, (int)y+ 48);
		/*
		g.setColor(Color.WHITE);
		g.fillOval((int)x, (int)y, 40, 40);
		g.setColor(Color.RED);
		g.fillRect((int)x+5, (int)y+16, 30, 10);
		g.fillRect((int)x+16, (int)y+5, 10, 30);
		*/
	}
	@Override
	public void update() {
		
		timer.start();
		
		if(collected){
			visible = false;
		}
		if(y>600 || x>600 || x<0 || y<0){
			visible = false;
		}
	}
	@Override
	public boolean isVisible() {
		
		return visible;
	}
	@Override
	public double getX() {
		
		return this.x;
	}
	@Override
	public double getY() {
		
		return this.y;
	}
	@Override
	public void setCollected(boolean newCollected) {
		
		this.collected = newCollected;
	}
	
	public int getBonusHealth(){
		return this.bonusHealth;
	}
	
}
