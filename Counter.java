package com.sebastianwizert.alieninvasion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public final class Counter {
	
	public static Timer counterTimer;
	
	public Counter(){
		
		ActionListener timeActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

				System.out.println("counter - timer - actionListener");
				if(AlienPanel.state != AlienPanel.STATE.MENU){
					if(AlienPanel.state != AlienPanel.STATE.HELP){
						if(AlienPanel.state != AlienPanel.STATE.SETUP){
							AlienPanel.state = AlienPanel.STATE.GAME;
						}
					}
				}
				counterTimer.restart();
			}
		};   
		counterTimer = new Timer(4000, timeActionListener);
		
		
	}

	public void render(Graphics g){
		Graphics2D g2d=(Graphics2D)g;
		
		Font fnt0 = new Font("arial", Font.BOLD, 50);
		g.setFont(fnt0);
		g.setColor(Color.WHITE);
		g.drawString("Alien Invasion", 100, 100);
		
		Font fnt1 = new Font("arial", Font.BOLD, 60);
		g.drawString("GET READY", 100, 250);
	}
}
