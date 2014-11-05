package com.sebastianwizert.alieninvasion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public final class Help {

	public void render(Graphics g){
		
		
		Font fnt0 = new Font("arial", Font.BOLD, 50);
		g.setFont(fnt0);
		g.setColor(Color.WHITE);
		g.drawString("Alien Invasion", 100, 100);
		
		Font fnt1 = new Font("arial", Font.BOLD, 23);
		g.setFont(fnt1);
		g.drawString("HELP", 10, 150);
		Font fnt2 = new Font("arial", Font.BOLD, 15);
		g.setFont(fnt2);
		g.drawString("Move around with arrows :", 10, 180);
		g.drawString("Up - Forward", 10, 200);
		g.drawString("Down - Backward", 10, 220);
		g.drawString("Left - Spin Left", 10, 240);
		g.drawString("Right - Spin Right", 10, 260);
		g.drawString("CTRL - Shoot", 10, 280);
		g.drawString("SPACE - Missile", 10, 300);
		g.drawString("X - ShockWave", 10, 320);
		g.drawString("ESC - Pause / Resume", 10, 340);
	}

}
