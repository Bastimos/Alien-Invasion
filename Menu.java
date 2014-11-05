package com.sebastianwizert.alieninvasion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public final class Menu {
	
	public Rectangle playButton = new Rectangle(250, 125, 120 ,50);
	public Rectangle helpButton = new Rectangle(250, 200, 120 ,50);
	public Rectangle setupButton = new Rectangle(250, 275, 120 ,50);
	public Rectangle statsButton = new Rectangle(250, 350, 120 ,50);
	public Rectangle quitButton = new Rectangle(250, 425, 120 ,50);

	public Menu(){
		

	}
	
	public void render(Graphics g){
		Graphics2D g2d=(Graphics2D)g;
		
		Font fnt0 = new Font("arial", Font.BOLD, 50);
		g.setFont(fnt0);
		g.setColor(Color.WHITE);
		g.drawString("Alien Invasion", 100, 100);
		
		Font fnt1 = new Font("arial", Font.BOLD, 30);
		g.setFont(fnt1);
		g.drawString("PLAY", playButton.x+20, playButton.y+35);
		g.drawString("HELP", helpButton.x+20, helpButton.y+35);
		g.drawString("SETUP", setupButton.x+7, setupButton.y+35);
		g.drawString("STATS", statsButton.x+7, statsButton.y+35);
		g.drawString("QUIT", quitButton.x+20, quitButton.y+35);
		g2d.draw(playButton);
		g2d.draw(helpButton);
		g2d.draw(setupButton);
		g2d.draw(statsButton);
		g2d.draw(quitButton);
	}
}
