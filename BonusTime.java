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

public final class BonusTime extends PickUp{

	private double x;
	private double y;
	private boolean visible;
	private boolean collected;
	private int additionalTime;
	
	private Timer timer;
	private int initialDisplayTime = 3;
	private int displayedFor;
	private File bonusTimeFile = new File("gfx/hourglass.png");
	private BufferedImage bonusTimeImage;
	
	public BonusTime(double newX, double newY, int newAdditionalTime){ 
		this.x = newX;
		this.y = newY;
		this.additionalTime = newAdditionalTime;
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
			bonusTimeImage = ImageIO.read(bonusTimeFile);
			} catch(Exception e){
				System.out.println("file corupted");
			}
	}


	@Override
	public void draw(Graphics2D g) {
		
		g.drawImage(bonusTimeImage, (int)x, (int)y, null);
		Font fnt0 = new Font("arial", Font.BOLD, 10);
		g.setFont(fnt0);
		g.setColor(Color.WHITE);
		String displayTimeBonus = "+"+additionalTime+" TIME";
		g.drawString(displayTimeBonus, (int)x - 12, (int)y+ 48);

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
	
	public int getBonusTime(){
		return this.additionalTime;
	}
	
}
