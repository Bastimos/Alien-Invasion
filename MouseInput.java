package com.sebastianwizert.alieninvasion;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;



public final class MouseInput implements MouseListener{
	
	Stats stats;
	Music music;
	
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
		if(mx > 250 && mx < 370){
			if(my > 125 && my < 175){
				if(AlienPanel.state == AlienPanel.STATE.MENU){
					AlienPanel.state = AlienPanel.STATE.COUNTER;
					Counter.counterTimer.start();
				} else if(AlienPanel.state == AlienPanel.STATE.HELP){
					AlienPanel.state = AlienPanel.STATE.COUNTER;
					Counter.counterTimer.start();
				} if(AlienPanel.state == AlienPanel.STATE.SETUP){
					AlienPanel.state = AlienPanel.STATE.COUNTER;
					Counter.counterTimer.start();
				}
			}
		}
		
		//help button
		if(mx > 250 && mx < 370){
			if(my > 200 && my < 250){
				if(AlienPanel.state == AlienPanel.STATE.MENU){
					AlienPanel.state = AlienPanel.STATE.HELP;
				} else if(AlienPanel.state == AlienPanel.STATE.HELP) {
					AlienPanel.state = AlienPanel.STATE.MENU;
				} else if(AlienPanel.state == AlienPanel.STATE.SETUP){
					AlienPanel.state = AlienPanel.STATE.HELP;
				} 
			}
		}
		
		//Setup button
		if(mx > 250 && mx < 370){
			if(my > 275 && my < 325){
				if(AlienPanel.state == AlienPanel.STATE.MENU){
					AlienPanel.state = AlienPanel.STATE.SETUP;
				} else if(AlienPanel.state == AlienPanel.STATE.SETUP) {
					AlienPanel.state = AlienPanel.STATE.MENU;
				} else if(AlienPanel.state == AlienPanel.STATE.HELP) {
					AlienPanel.state = AlienPanel.STATE.SETUP;
				} 
			}
		}
		//Setup -> Sound on/off button
		//x410, y160, width180 ,height35)
		if(mx > 410 && mx < 590){
			if(my > 160 && my < 195){
				if(AlienPanel.state == AlienPanel.STATE.SETUP){
					Setup.soundSwitch = !Setup.soundSwitch;
				} 
			}
		}
		//Setup -> auto music switch
		//515, 215, 70 ,25);
		if(mx > 505 && mx < 585){
			if(my > 215 && my < 240){
				if(AlienPanel.state == AlienPanel.STATE.SETUP){
					Setup.autoMusicSwitch = !Setup.autoMusicSwitch;
				} 
			}
		}
		//Setup -> song number 1
		//g.drawRect(485, 265, 22, 25); -1
		if(mx > 485 && mx < 507){
			if(my > 265 && my < 290){
				if(AlienPanel.state == AlienPanel.STATE.SETUP){
					//music.setSongNumber(1);
					Setup.music.setSongNumber(1);
					Music.soundTrackTimer.setDelay(0);
					Music.soundTrackTimer.restart();
				}
			}
		}
		//Setup -> song number 2
		//g.drawRect(510, 265, 22, 25); -2
		if(mx > 510 && mx < 532){
			if(my > 265 && my < 290){
				if(AlienPanel.state == AlienPanel.STATE.SETUP){
					//music.setSongNumber(1);
					Setup.music.setSongNumber(2);
					Music.soundTrackTimer.setDelay(0);
					Music.soundTrackTimer.restart();
				}
			}
		}
		//Setup -> song number 3
		//g.drawRect(535, 265, 22, 25); -3
		if(mx > 535 && mx < 557){
			if(my > 265 && my < 290){
				if(AlienPanel.state == AlienPanel.STATE.SETUP){
					//music.setSongNumber(1);
					Setup.music.setSongNumber(3);
					Music.soundTrackTimer.setDelay(0);
					Music.soundTrackTimer.restart();
				}
			}
		}

		//Stats
		//(250, 350, 120 ,50
		if(mx > 250 && mx < 370){
			if(my > 350 && my < 400){
				if(AlienPanel.state == AlienPanel.STATE.MENU){
					AlienPanel.state = AlienPanel.STATE.STATS;
					Stats.updateScores = true;
				} else if(AlienPanel.state == AlienPanel.STATE.HELP){
					AlienPanel.state = AlienPanel.STATE.STATS;
					Stats.updateScores = true;
				} else if(AlienPanel.state == AlienPanel.STATE.SETUP){
					AlienPanel.state = AlienPanel.STATE.STATS;
					Stats.updateScores = true;
				} 
			}
		}
		//Stats -back to main menu button
		//450, 450, 120 ,50
		if(mx > 450 && mx < 570){
			if(my > 450 && my < 500){
				if(AlienPanel.state == AlienPanel.STATE.STATS){
					AlienPanel.state = AlienPanel.STATE.MENU;
				} 
			}
		}

		//quit button
		if(AlienPanel.state == AlienPanel.STATE.MENU || AlienPanel.state == AlienPanel.STATE.HELP){
			if(mx > 250 && mx < 370){
				if(my > 425 && my < 475){
					System.exit(0);
				}
			}
		}

		

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
	

}
