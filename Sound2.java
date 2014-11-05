package com.sebastianwizert.alieninvasion;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class Sound2 {
	
	Clip clip;
	AudioInputStream sound;
	private String filename;

	public Sound2(String newFileName){
		this.filename = newFileName;
		// specify the sound to play
	    // (assuming the sound can be played by the audio system)
	    File soundFile = new File(filename);
	    
		try {
			sound = AudioSystem.getAudioInputStream(soundFile);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    // load the sound into memory (a Clip)
	    DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
	    
		try {
			clip = (Clip) AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			clip.open(sound);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    // due to bug in Java Sound, explicitly exit the VM when
	    // the sound has stopped.
	    clip.addLineListener(new LineListener() {
	      public void update(LineEvent event) {
	        if (event.getType() == LineEvent.Type.STOP) {
	          event.getLine().close();
	          //System.exit(0);
	        }
	      }
	    });

	    // play the sound clip
	    clip.start();
	}
	
	public Clip getClip(){
		return this.clip;
	}
}
