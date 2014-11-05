package com.sebastianwizert.alieninvasion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public final class Setup {
	public static Music music;
	
	public static boolean soundSwitch = true;
	public static boolean autoMusicSwitch = true;
	
	public Rectangle soundButton = new Rectangle(410, 160, 180 ,35);
	public Rectangle musicdButton = new Rectangle(410, 210, 180 ,35);
	public Rectangle autodButton = new Rectangle(505, 215, 70 ,25);
	public Rectangle songButton = new Rectangle(410, 260, 180 ,35);
	
	public Rectangle easyButton = new Rectangle(10, 160, 180 ,35);
	
	
	public Setup(){
		music = new Music();
	}

	public void render(Graphics g){
		Graphics2D g2d=(Graphics2D)g;
		
		g2d.setColor(Color.WHITE);
		Font fnt1 = new Font("arial", Font.BOLD, 23);
		g.setFont(fnt1);
		g.drawString("SETUP", 450, 150);
		
		if(soundSwitch == true){
			g2d.setColor(Color.GREEN);
			//g.drawRect(490, 167, 38, 22);
			
		} else if(soundSwitch == false){
			g2d.setColor(Color.RED);
			//g.drawRect(540, 167, 45, 22);
		}
		
		g2d.draw(soundButton);
		g2d.draw(musicdButton);
		Font fnt2 = new Font("arial", Font.BOLD, 20);
		g.setFont(fnt2);
		g.drawString("SOUND ON/OFF", 415, 185);
		
		if(autoMusicSwitch == true){
			g2d.draw(autodButton);
		} else if (autoMusicSwitch == false){
			g2d.setColor(Color.RED);
		}
		g.drawString("MUSIC->   AUTO", 415, 235);
		
		g2d.draw(songButton);
		g.drawString("SONG   1  2  3", 415, 285);
		
		//System.out.println("Music.songNumber -> " +Music.songNumber);
		int tempSongNumber = 0;
		if(music.getSongNumber() == 1){
			tempSongNumber = 3;
		} else {
			tempSongNumber = 0;
		}
			
		if(music.getSongNumber()-1 == 1){
			g.drawRect(485, 265, 22, 25);
		} else if(music.getSongNumber()-1 == 2){
			g.drawRect(510, 265, 22, 25);
		} else if(tempSongNumber == 3){
			g.drawRect(535, 265, 22, 25);
		}
	
		//-------------------------difficulty settings
		g2d.setColor(Color.WHITE);
		g2d.draw(easyButton);
	}
	
}
