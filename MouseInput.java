package com.sebastianwizert.alieninvasion;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

public class MouseInput implements MouseListener{

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		//play button
		if(mx > 200 && mx < 320){
			if(my > 150 && my < 200){

				AlienPanel.state = AlienPanel.STATE.GAME;
				
				//CrowdPanel cp = new CrowdPanel();
				//System.out.println("num of aliens " + cp.numberOfAliens);
				
			}
		}
		//quit button
		if(mx > 200 && mx < 320){
			if(my > 350 && my < 400){
				System.exit(0);
			}
		}
		/**
		public Rectangle playButton = new Rectangle(200, 150, 120 ,50);
		public Rectangle helpButton = new Rectangle(200, 250, 120 ,50);
		public Rectangle quitButton = new Rectangle(200, 350, 120 ,50);
		 */
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
	

}
