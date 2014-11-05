package com.sebastianwizert.alieninvasion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.Clip;
import javax.swing.Timer;

import com.sebastianwizert.alieninvasion.Sound.Position;

public final class Music {

	private int songNumber =1;
	public static  Timer soundTrackTimer;
	private Sound2 music;

	ActionListener soundActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent actionEvent) {
			if(Setup.soundSwitch == true){
				
				int songLength = 0;
				
				switch(songNumber){
					case 1 : 	System.out.println("play song 1"); 
								if(null != music){
									music.clip.stop(); //if last clip is still on ,stop it
								}
								
								music = new Sound2("sfx/Run.wav"); 
								System.out.println("music1 length -> "+ music.clip.getMicrosecondLength() );
								songLength = (int) (music.clip.getMicrosecondLength() / 1000);
								soundTrackTimer.setDelay(songLength); //set 1
								break;
								
					case 2 : 	System.out.println("play song 2"); 
								if(null != music){
									music.clip.stop();
								}
								music = new Sound2("sfx/mtx1.wav"); 
								System.out.println("music2 length -> "+ music.clip.getMicrosecondLength() );
								songLength = (int) (music.clip.getMicrosecondLength() / 1000);
								soundTrackTimer.setDelay(songLength); //set 2
								break;
								
					case 3 : 	System.out.println("play song 3"); 
								if(null != music){
									music.clip.stop();
								}
								music = new Sound2("sfx/astral1.wav"); 
								System.out.println("music3 length -> "+ music.clip.getMicrosecondLength() );
								songLength = (int) (music.clip.getMicrosecondLength() / 1000);
								soundTrackTimer.setDelay(songLength); //set 3
								break;
								
				}
				
				if(Setup.autoMusicSwitch == true){
					
					songNumber++;
					System.out.println("song number is invcremented to "+ songNumber);
					if(songNumber > 3){
						songNumber = 1;
						
					}
				}
				
				System.out.println("song no. -> "+songNumber+" soundTrackTimer delay -> " + soundTrackTimer.getDelay() );
				
			} else if (Setup.soundSwitch == false){
				
				soundTrackTimer.setDelay(1000);
			}
		}
	};   
	
	public Music(){
		//music player
		soundTrackTimer = new Timer(0, soundActionListener);
		soundTrackTimer.start();
		soundTrackTimer.setRepeats(true);
		//System.out.println("soundTrackTimer delay" + soundTrackTimer.getDelay() );
	}
	
	public int getSongNumber() {
		return songNumber;
	}

	public void setSongNumber(int songNumber) {
		this.songNumber = songNumber;
	}
}
