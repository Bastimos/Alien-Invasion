package com.sebastianwizert.alieninvasion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.Timer;

public class Stats {
	
	private String playerName;
	private String fileName = "src/scores.txt";
	private BufferedWriter bufferedWriter = null;
	private BufferedReader bufferedReader = null;
	private String[] lines = new String[5];
	private String[] names = new String[5];
	private int[] scores = new int[5];
	private int newScore;
	
	public static boolean updateScores;

	public Rectangle backButton = new Rectangle(450, 450, 120 ,50);

	public Stats() {
		
	}

	public void render(Graphics g){
		Graphics2D g2d=(Graphics2D)g;
		
		if(updateScores){
			//writeToAFile();
			readFromAFile();
			updateScores = false;
		}
		
		Font fnt0 = new Font("arial", Font.BOLD, 50);
		g.setFont(fnt0);
		g.setColor(Color.WHITE);
		g.drawString("Alien Invasion", 100, 100);
		
		Font fnt1 = new Font("arial", Font.BOLD, 30);
		g.setFont(fnt1);
		g.drawString("HIGH SCORES", 10, 150);
		
		g2d.draw(backButton);
		g.drawString(" BACK", backButton.x+7, backButton.y+35);
		
		Font fnt2 = new Font("arial", Font.BOLD, 25);
		g.setFont(fnt2);

		Arrays.sort(scores);  //, Collections.reverseOrder()
		for(int i = lines.length - 1; i >= 0; i--){
			g.drawString(lines[i], 10, 180+(i*25));
		}
		
	}
	
	public void readFromAFile(){
		try {
			FileReader fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			
			for(int i=0; i<lines.length; ++i){
				lines[i] = bufferedReader.readLine();
				int tempScore = 0;
				StringBuilder scoreBuilder = new StringBuilder();
				StringBuilder nameBuilder = new StringBuilder();
				boolean whiteSpace = false;
				for(int j=0 ; j<lines[i].length(); ++j){
					char tempChar = lines[i].charAt(j);
					if((tempChar != ' ') && whiteSpace == false){
						nameBuilder.append(tempChar);
					}else if(tempChar == ' '){
						whiteSpace = true;
						continue;
					} else {
						scoreBuilder.append(tempChar);
					}
					//System.out.println(tempChar );
				}
				//System.out.println(nameBuilder.toString() + "  "+ scoreBuilder.toString());
				scores[i] = Integer.parseInt(scoreBuilder.toString());
				names[i] = nameBuilder.toString();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void checkScore(String newPlayerName, int newScore){
		this.playerName = newPlayerName;
		this.newScore = newScore;
		
		readFromAFile();
		
		int replaceScoreNumber = 0;
		int i = 0;
		for(i=scores.length-1; i>=0; --i){
			if(newScore > scores[i]){
				//scores[i] = this.newScore;
				//names[i] = this.playerName;
				//break;
				replaceScoreNumber = i;
				//System.out.println("replaceScoreNumber -> "+replaceScoreNumber);
			}
		}
		scores[replaceScoreNumber] = this.newScore;
		names[replaceScoreNumber] = this.playerName;
		
		writeToAFile();
		readFromAFile();
	}
	
	public void writeToAFile(){
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			bufferedWriter = new BufferedWriter(fileWriter);
			
			for(int i=0; i<lines.length; ++i){

				bufferedWriter.write(names[i] + "  "+scores[i]);
				bufferedWriter.newLine();

			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		
			
				try {
					if(bufferedWriter != null){
						bufferedWriter.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
	}
	
	public String getPlayerName() {
		return playerName;
	}



	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	

	
	
}
