package com.sebastianwizert.alieninvasion;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.ArrayList;
import java.util.Random;


public final class AlienPanel extends JPanel implements Runnable, KeyListener{

	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;

	public static ArrayList <Enemy> enemies;
	private ArrayList<PickUp> pickUps;
	private ArrayList<Weapon> weapons;
	private ArrayList<Weapon> enemyWeapons;

	private Thread thread;
	private Player player;
	private Menu menu;
	private Enemy autoEnemy;
	private Help help;
	private Counter counter;
	private Stats stats;
	private Setup setup;
	
	private boolean running;
	boolean giveBonus;

	private Graphics2D g;
	private BufferedImage image;

	private File f = new File("gfx/spaceBg.png");
	private BufferedImage bgImage;
	private int numberOfAliensAtStart ;
	private int numberOfAlienSoldiersAtStart;
	private int numberOfAlienBossAtStart;
	public static int dynamicNumberOfAliens ;
	private int timeLimit = 60;
	private int gameDurationTime;
	private int waveCount = 1;
	private int FPS = 30;
	private int enemySize=0;
	public static int currentScore = 0;
	
	private String displayNumOfEnemies;
	private String displayTimeLimit= "";
	private Timer timer2;
	
	double awayFromPlayerX;
	double awayFromPlayerY;


	//No args constructor.
	public AlienPanel() {
		this.setBounds(0, 0, WIDTH, HEIGHT);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		this.requestFocus();

		try{
			bgImage = ImageIO.read(f);
		} catch(Exception e){
			System.out.println("file corupted");
		}

		//Time Limit
		ActionListener timerActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

				if(timeLimit == 0 ){
					player.setHealth(0);
					timeLimit = timeLimit + gameDurationTime;
					gameDurationTime = 0;	
				}
				timeLimit--;
				gameDurationTime++;
				StringBuilder timeBuilder = new StringBuilder();
				timeBuilder.append(timeLimit);
				displayTimeLimit = timeBuilder.toString();

				//System.out.println("Timer "+ displayTimeLimit + " timeLimit--" + timeLimit+ "   gameDurationTime -->"+gameDurationTime);
			}
		};   
		timer2 = new Timer(1000, timerActionListener);
		
		
	}

	public static enum STATE{
		MENU,
		GAME,
		HELP,
		COUNTER,
		STATS,
		SETUP;
	}
	private enum WAVE {
		WAVE1,WAVE2,WAVE3,WAVE4,WAVE5,WAVE6,WAVE7,WAVE8,WAVE9,WAVE10;;
	}
	WAVE wave = WAVE.WAVE1;

	public static STATE state = STATE.MENU;


	public void addNotify() {
		super.addNotify();
		if(thread == null) { //If the thread hasn't been created yet, create and then start it.
			thread = new Thread(this); 
			thread.start();
		}
		addKeyListener(this); //Add the key listener
		addMouseListener(new MouseInput());
	}

	@Override
	public void run() {
		running = true;

		//create a new BufferedImage object with the specified width, height and image type.
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		//Create a new Graphics2D object with the graphics returned using the .getGraphics method on the image object.
		g = (Graphics2D) image.getGraphics();

		long startTime = 0L; 
		long URDTime = 0L;
		long waitTime = 0L;
		long totalTime = 0L;

		int targetTime = 1000 / FPS;
		int frameCount = 0;
		int maxFrameCount = FPS;
		double averageFPS = 0;

		player = new Player(300, 300);
		//player.setPlayerDead(false);
		//keep track on player position
		awayFromPlayerX = player.getX();
		awayFromPlayerY = player.getY();
		menu = new Menu();
		help = new Help();
		setup = new Setup();
		stats = new Stats();
		counter = new Counter();
		//music = new Music();
		pickUps = new ArrayList<PickUp>();
		createAliens();
		
		//BCK Music
		//new Sound("src/spacesignal.wav").start(); 
	
		
		while(running) {

			
			startTime = System.nanoTime();
			if(state == STATE.GAME){
				Counter.counterTimer.stop();
				shapeUpdate(); //Call the shapeUpdate (see method for more information).
				shapeRender(); //Call the shapeRender (see method for more information).
				timer2.start();
			} else if(state == STATE.MENU){
				
				shapeRender(); //Call the shapeRender (see method for more information).
				menu.render(g);
				timer2.stop();
			} else if (state == STATE.HELP){
				shapeRender();
				help.render(g);
				menu.render(g);
			} else if (state == STATE.SETUP){
				shapeRender();
				setup.render(g);
				menu.render(g);
			} else if (state == STATE.COUNTER){
				shapeRender();;
				counter.render(g);
			} else if (state == STATE.STATS){
				shapeRender();
				stats.render(g);
				//menu.render(g);
			}
			shapeDraw();   //Call the shapeDraw (see method for more information).
			URDTime = (System.nanoTime() - startTime) / 1000000;  

			waitTime = targetTime - URDTime;

			//System.out.println("Wait Time: " + waitTime);

			try { //Depending on the waitTime, make the thread sleep for that length.
				if(waitTime >= 0)
					thread.sleep(waitTime);
			} catch (Exception e) {
				e.printStackTrace();
			}

			totalTime += (System.nanoTime() - startTime);
			frameCount++;

			if(frameCount == maxFrameCount) {
				//Calculating the average FPS.
				averageFPS = 1000.0 / ((totalTime/frameCount) / 1000000);
				frameCount = 0;
				totalTime = 0;
			}
			//System.out.println("Average FPS: " + averageFPS);	

			//pickups validation
			for (int i=0; i<pickUps.size(); ++i) {
				PickUp p = pickUps.get(i);
				if(p.isVisible()==true){
					p.update();
				} else {
					pickUps.remove(i);
				}
			}


			//weapons validation
			weapons = player.getWeapons();
			for (int i=0; i<weapons.size(); ++i) {
				Weapon w = weapons.get(i);
				if(w.isVisible()==true){
					w.update();
				} else {
					weapons.remove(i);
				}
			}
			
			//alien weapons validation
			for(int i=0; i<enemies.size(); ++i){
				Enemy e = enemies.get(i);
				//if( e instanceof AlienSoldier | e instanceof AlienBoss){
					enemyWeapons = e.getWeapons();
					for(int j=0; j<enemyWeapons.size(); ++j){
						Weapon w = enemyWeapons.get(j);
						if(w.isVisible()==true){
							w.update();
						} else {
							enemyWeapons.remove(j);
						}
					}
				//}
			}

			//wave logic/modifier
			if(waveCount == 1){
				wave = WAVE.WAVE1;
			} else if(waveCount == 2){
				wave = WAVE.WAVE2;
			} else if(waveCount == 3){
				wave = WAVE.WAVE3;
			} else if(waveCount == 4){
				wave = WAVE.WAVE4;
			} else if(waveCount == 5){
				wave = WAVE.WAVE5;
			} else if(waveCount == 6){
				wave = WAVE.WAVE6;
			} else if(waveCount == 7){
				wave = WAVE.WAVE7;
			} else if(waveCount == 8){
				wave = WAVE.WAVE8;
			} else if(waveCount == 9){
				wave = WAVE.WAVE9;
			} else if(waveCount == 10){
				wave = WAVE.WAVE10;
			}

			if(dynamicNumberOfAliens <= 0){
				waveCount++;
				player.setNumOfMissiles(player.getNumOfMissiles()+3);
				player.setNumOfShockWaves(player.getNumOfShockWaves()+3);
			
				//keep track on player position
				awayFromPlayerX = player.getX();
				awayFromPlayerY = player.getY();
				createAliens();
			}

			// if player dies
			//System.out.println("health -> " + player.getHealth());
			if(player.isPlayerDead()){
				player.setPlayerDead(false);
				
				System.out.println("if player dies this should be displayed only once !!!");
				timer2.stop();
				boolean oneTimeOnlyPlease = true;
				String newName = "";
				if(oneTimeOnlyPlease) {
					newName =( JOptionPane.showInputDialog(null,
							"What is your name?", null) ) ;
					oneTimeOnlyPlease = false;
				}
				stats.checkScore(newName, currentScore);
				//stats.writeToAFile();
				//stats.readFromAFile();
				state = STATE.STATS;
				//System.out.println("AlienPanel-> while lopp -> players name set to -> "+ player.getName() );
				waveCount = 1;
				
				timeLimit = timeLimit + gameDurationTime;
				gameDurationTime = 0;
				player.setX(300);
				player.setY(300);
				//keep track on player position
				awayFromPlayerX = player.getX();
				awayFromPlayerY = player.getY();
				
				currentScore=0;
				player.setNumOfMissiles(10);
				player.setNumOfShockWaves(10);
				createAliens();
			}
			//System.out.println("wave ->"+wave+ " wave count -> "+waveCount+ "\n dynamic number of aliens -> "+
			//dynamicNumberOfAliens+ " number of aliens at start ->" + numberOfAliensAtStart);

		}
	}


	public void createAliens (){
		
		int difficulty = 3; //1-easy /2-medium/3-hard
		if(wave == WAVE.WAVE1){
			numberOfAliensAtStart = 5*difficulty;
			numberOfAlienSoldiersAtStart = 0*difficulty;
			numberOfAlienBossAtStart = 0*difficulty;
			timer2.restart();
			player.setHealth(200);	
		} else if(wave == WAVE.WAVE2){
			numberOfAliensAtStart = 7*difficulty;
			numberOfAlienSoldiersAtStart = 1*difficulty;
			numberOfAlienBossAtStart = 0*difficulty;
		} else if(wave == WAVE.WAVE3){
			numberOfAliensAtStart = 9*difficulty;
			numberOfAlienSoldiersAtStart = 1*difficulty;
			numberOfAlienBossAtStart = 0*difficulty;
		} else if(wave == WAVE.WAVE4){
			numberOfAliensAtStart = 11*difficulty;
			numberOfAlienSoldiersAtStart = 2*difficulty;
			numberOfAlienBossAtStart = 0*difficulty;
		} else if(wave == WAVE.WAVE5){
			numberOfAliensAtStart = 12*difficulty;
			numberOfAlienSoldiersAtStart = 3*difficulty;
			numberOfAlienBossAtStart = 0*difficulty;
		} else if(wave == WAVE.WAVE6){
			numberOfAliensAtStart = 15*difficulty;
			numberOfAlienSoldiersAtStart = 3*difficulty;
			numberOfAlienBossAtStart = 0*difficulty;
		} else if(wave == WAVE.WAVE7){
			numberOfAliensAtStart = 18*difficulty;
			numberOfAlienSoldiersAtStart = 4*difficulty;
			numberOfAlienBossAtStart = 0*difficulty;
		} else if(wave == WAVE.WAVE8){
			numberOfAliensAtStart = 21*difficulty;
			numberOfAlienSoldiersAtStart = 5*difficulty;
			numberOfAlienBossAtStart = 0*difficulty;
		} else if(wave == WAVE.WAVE9){
			numberOfAliensAtStart = 25*difficulty;
			numberOfAlienSoldiersAtStart = 6*difficulty;
			numberOfAlienBossAtStart = 0*difficulty;
		} else if(wave == WAVE.WAVE10){
			numberOfAliensAtStart = 30*difficulty;
			numberOfAlienSoldiersAtStart = 8*difficulty;
			numberOfAlienBossAtStart = 1*difficulty;
		}
		dynamicNumberOfAliens = numberOfAliensAtStart+numberOfAlienSoldiersAtStart+numberOfAlienBossAtStart;

		//System.out.println("awayFromPlayerX "+awayFromPlayerX+"  awayFromPlayerY "+awayFromPlayerY);
		//alienList = new ArrayList<Alien>();
		enemies = new ArrayList<Enemy>();
		Random r = new Random();
		for(int i=0; i<dynamicNumberOfAliens; ++i){

			double newAlienX = r.nextDouble()*1000;
			double newAlienY = r.nextDouble()*1000;
			//System.out.println("before validation -> enemy "+i+" newX "+newAlienX+" newY "+newAlienY);
			
			if(newAlienX > awayFromPlayerX-200 && newAlienX < awayFromPlayerX+200){
				if(newAlienY > awayFromPlayerY-200 && newAlienY < awayFromPlayerY+200){

					newAlienX -= 500;
					newAlienY -= 500;
					
				}
			}
			int localAlienCounter = 0;
			int localAlienSoldierCounter = 0;
			int localAlienBossCounter = 0;
			if(i < numberOfAliensAtStart){
				autoEnemy = new Alien(newAlienX, newAlienY);
				localAlienCounter++;
			} else if(i >= numberOfAliensAtStart && i < (numberOfAlienSoldiersAtStart+numberOfAliensAtStart)) {
				autoEnemy = new AlienSoldier(newAlienX, newAlienY);
				localAlienSoldierCounter++;
			} else {
				autoEnemy = new AlienBoss(newAlienX, newAlienY);
				localAlienBossCounter++;
			}
		
				//System.out.println("after validation -> enemy "+i+" newX "+newAlienX+" newY "+newAlienY);
		
			enemies.add(autoEnemy);
			//autoEnemy.setNumberOfHits(99);
			
			//System.out.println("dynamicNumberOfAliens "+dynamicNumberOfAliens +
							//	"  numberOfAliensAtStart "+numberOfAliensAtStart
							//	+"  numberOfAlienSoldiersAtStart "+numberOfAlienSoldiersAtStart);
		}
	}

	/*
	 * This method will update the coordinates, etc., of every object that needs to be updated.
	 * In this case, only the shape object is updated as it's the only object on the screen and that moves. 
	 */
	public void shapeUpdate() {

		//display number of enemies
		int numOfEnemies = enemies.size();
		StringBuilder builder = new StringBuilder();
		builder.append(numOfEnemies);
		displayNumOfEnemies = builder.toString();

		player.update();

		for(int i=0; i<enemies.size(); ++i){
			Enemy e = enemies.get(i);			
			if(e instanceof Alien){
				e = (Alien)e;
				e.setTarget(player.getX(),player.getY(),1);
			} 
			if(e instanceof AlienSoldier){
				e = (AlienSoldier)e;
				e.setTarget(player.getX(),player.getY(),1);
			} 
			if(e instanceof AlienBoss){
				e=(AlienBoss)e;
				e.setTarget(player.getX(),player.getY(),0.5);
			}
			//player vs enemy weapon collisions
			if(null != e.getWeapons()){
				for(int j=0; j< e.getWeapons().size(); ++j){
					Weapon w = (Weapon) e.getWeapons().get(j);
					double WeaponToPlayerDistanceX = w.getX() - player.getX();
					double WeaponToPlayerDistanceY = w.getY() - player.getY();
					double WeaponToPlayerDistance = Math.sqrt((WeaponToPlayerDistanceX*WeaponToPlayerDistanceX)+
							(WeaponToPlayerDistanceY*WeaponToPlayerDistanceY));
					//System.out.println("WeaponToPlayerDistance "+j+" -> "+ WeaponToPlayerDistance+" enemyWeapons.size() -> " + enemyWeapons.size());
					if(WeaponToPlayerDistance < enemySize){
						
						if(w instanceof Bullet){
							player.setHealth(player.getHealth()-0.5);
						} 
						if(w instanceof Missile){
							player.setHealth(player.getHealth()-1);
						} 
						if(w instanceof ShockWave){
							player.setHealth(player.getHealth()-10);
						}
						
						w.setVisible(false);
					}
				}
			}
			
			enemies.get(i).update();
		}

		//pickups logic
		for(int i=0; i < pickUps.size(); ++i) {
			pickUps.get(i).update();
			double tempPickUpX = pickUps.get(i).getX();
			double tempPickUpY = pickUps.get(i).getY();
			double PlayerToPickUpDistanceX =  player.getX() - tempPickUpX;
			double PlayerToPickUpDistanceY =  player.getY() - tempPickUpY;
			double PlayerToPickUpDistance = Math.sqrt((PlayerToPickUpDistanceX*PlayerToPickUpDistanceX)+
					(PlayerToPickUpDistanceY*PlayerToPickUpDistanceY));
			//System.out.println(tempPickUpX + " -- " + tempPickUpY);
			//System.out.println("PlayerToPickUpDistance "+i+" is " +PlayerToPickUpDistance);
			if(PlayerToPickUpDistance < 50){
				
				if(pickUps.get(i) instanceof Health){
					Health h = (Health)pickUps.get(i);
					if(player.getHealth() <= 200){
						player.setHealth(player.getHealth()+h.getBonusHealth());
						h.setCollected(true);
						currentScore+= 25;
					}
				} else if(pickUps.get(i) instanceof Ammo){
					Ammo am = (Ammo)pickUps.get(i);
					player.setNumOfMissiles(player.getNumOfMissiles()+am.getBonusAmmo());
					player.setNumOfShockWaves(player.getNumOfShockWaves()+am.getBonusAmmo());
					am.setCollected(true);
					currentScore+= 25;
				} else if(pickUps.get(i) instanceof BonusTime){
					BonusTime bt = (BonusTime)pickUps.get(i);
					timeLimit = timeLimit+bt.getBonusTime();
					bt.setCollected(true);
					currentScore+= 25;
				}
			}
		}

		//collisions
		double tempAlienX = 0, tempAlienY=0;
		Enemy a = null;

		for(int i=0; i < enemies.size(); ++i) {
			
			a = enemies.get(i);
			
			if(a instanceof Alien){
				a = (Alien)a;
				enemySize = 60;
			} else if (a instanceof AlienSoldier){
				a = (AlienSoldier)a;
				enemySize = 60;
			}	else if(a instanceof AlienBoss){
				a=(AlienBoss)a;
				enemySize = 100;
			}
			tempAlienX = enemies.get(i).getX();
			tempAlienY = enemies.get(i).getY();
			
			

			//allien to player collisions
			double AlienDistanceToPlayerX = tempAlienX - player.getX();
			double AlienDistanceToPlayerY = tempAlienY - player.getY();
			double AlienDistanceToPlayer = Math.sqrt((AlienDistanceToPlayerX*AlienDistanceToPlayerX)+
					(AlienDistanceToPlayerY*AlienDistanceToPlayerY));
			//System.out.println("alien"+i+" distance to player =" + AlienDistanceToPlayer);
			if(AlienDistanceToPlayer < enemySize) {
				player.setHealth(player.getHealth()-5);
				//a.setNumberOfHits(a.getNumberOfHits()+3);
				//if(a.getNumberOfHits() > a.getHealth()){
				a.setHealth(a.getHealth()-1);
				if(a.getHealth() <= 0){
					a.setVisible(false);
					enemies.remove(i);
					dynamicNumberOfAliens--;
				}
			}
			//new weapons logic
			if(null != weapons){
				for(int d=0; d<weapons.size(); d++) {
					double tempWeaponX = weapons.get(d).getX();
					double tempWeaponY = weapons.get(d).getY();
					if(weapons.get(d) instanceof Bullet){
						double AlienDistanceToBulletX = tempAlienX - tempWeaponX;
						double AlienDistanceToBulletY = tempAlienY - tempWeaponY;
						double AlienDistanceToBullet = Math.sqrt((AlienDistanceToBulletX*AlienDistanceToBulletX)+
								(AlienDistanceToBulletY*AlienDistanceToBulletY));
						//System.out.println("alien"+ i+ " to bullet distance"  + AlienDistanceToBullet);
						if(AlienDistanceToBullet < enemySize){
							weapons.get(d).setHitTarget(true);
							a.setHealth(a.getHealth()-1);
							if(a.getHealth() <= 0){
								try{
									a.setVisible(false);
									enemies.remove(i); // this might try to remove alien that is already dead :)
									giveBonus = true;
									currentScore+= 50;
								} catch (IndexOutOfBoundsException ioobe) {
									System.out.println("Alien"+i+" is already DEAD !!!");
								}
								dynamicNumberOfAliens--;
							}
						}
					} else if(weapons.get(d) instanceof Missile){
						double AlienDistanceToMissileX = tempAlienX - tempWeaponX;
						double AlienDistanceToMissileY = tempAlienY - tempWeaponY;
						double AlienDistanceToMissile = Math.sqrt((AlienDistanceToMissileX*AlienDistanceToMissileX)+
								(AlienDistanceToMissileY*AlienDistanceToMissileY));
						if(AlienDistanceToMissile < enemySize) {
							weapons.get(d).setHitTarget(true);
							a.setHealth(a.getHealth()-10);
							if(a.getHealth() <= 0){
								try{
									a.setVisible(false);
									enemies.remove(i); // this might try to remove alien that is already dead :)
									giveBonus = true;
									currentScore+= 75;
								} catch (IndexOutOfBoundsException ioobe) {
									System.out.println("Alien"+i+" is already DEAD !!!");
								}
								dynamicNumberOfAliens--;
							}
						}
					} else if(weapons.get(d) instanceof ShockWave){
						double ShockWaveDistanceFromPlayerX = player.getX() - tempWeaponX;
						double ShockWaveDistanceFromPlayerY = player.getY() - tempWeaponY;
						double ShockWaveDistanceFromPlayer = Math.sqrt((ShockWaveDistanceFromPlayerX*ShockWaveDistanceFromPlayerX)+
								(ShockWaveDistanceFromPlayerY*ShockWaveDistanceFromPlayerY));

						if(ShockWaveDistanceFromPlayer > AlienDistanceToPlayer){
							weapons.get(d).setHitTarget(true);
							a.setHealth(a.getHealth()-1);
							if(a.getHealth() <= 0){
								try{
									a.setVisible(false);
									enemies.remove(i);
									giveBonus = true;
									currentScore+= 100;
								} catch (IndexOutOfBoundsException ioobe){
									System.out.println("Alien"+i+" is already DEAD !!!");
								}
								dynamicNumberOfAliens--;
							}
						}
						
					}
					while(giveBonus) {
						
						Random r = new Random();
						int luckyDrop = r.nextInt(4);
						if(luckyDrop == 0) {
							PickUp bonusHealth = new Health(tempAlienX, tempAlienY, 10);
							pickUps.add(bonusHealth);
						} else if(luckyDrop == 1) {
							PickUp bonusAmmo = new Ammo(tempAlienX, tempAlienY, 10);
							pickUps.add(bonusAmmo);
						} else if(luckyDrop == 2) {
							PickUp bonusTime = new BonusTime(tempAlienX, tempAlienY, 10);
							pickUps.add(bonusTime);
						}
						
						System.out.println("Pick Up Created at position X "+ tempAlienX + " Y " + tempAlienY+ " luckyDrop "+luckyDrop);
						
						giveBonus = false;
					} 
			}
		}

	}
}

/*
 * This method will render the Graphics2D object we have. It will render anything that we need rendered in the background so we can just
 * copy it from the background and show it on the screen so it looks smoother.
 */
public void shapeRender() { 
	g.drawImage(bgImage, 0, 0, null);

	//draw enemies
	for(int i=0; i<enemies.size(); ++i){
		Enemy e = enemies.get(i);
		e.draw(g);

		//draw alien weapons
		//if(e instanceof AlienSoldier || e instanceof AlienBoss){
			
			enemyWeapons = e.getWeapons();
			for(int j=0; j<enemyWeapons.size(); ++j){
				Weapon w = enemyWeapons.get(j);
				if(w instanceof Bullet){
					g.setColor(Color.YELLOW);
					g.fillOval((int)w.getX()-5, (int)w.getY()-5, 10, 5);
					g.fillOval((int)w.getX(), (int)w.getY(), 10, 5);
				} else if(w instanceof Missile){
					//Weapon w = (Missile)weapons.get(i);
					w.draw(g);
				} else if(w instanceof ShockWave){
					w.draw(g);
				}
				//System.out.println("enemy"+i+" shoot bullet no "+j+" current x"+(int)w.getX() +"  y "+(int)w.getY());
			}
		//}
	}

	//draw player weapons
	weapons = player.getWeapons();
	for(int i=0; i<weapons.size(); ++i){
		Weapon w = weapons.get(i);
		if(w instanceof Bullet){
			//Weapon w = (Bullet)weapons.get(i);
			g.setColor(Color.YELLOW);
			g.fillOval((int)w.getX()-5, (int)w.getY()-5, 10, 5);
			g.fillOval((int)w.getX(), (int)w.getY(), 10, 5);
			//System.out.println("players bullet"+i+" current x"+(int)w.getX() +"  y "+(int)w.getY());
		} else if(w instanceof Missile){
			//Weapon w = (Missile)weapons.get(i);
			w.draw(g);
		} else if(w instanceof ShockWave){
			w.draw(g);
		}
	}
	

	//draw PickUps
	for(int i=0; i<pickUps.size(); ++i){
		PickUp p = pickUps.get(i);
		p.draw(g);
	}
	//draw PLAYER

	player.draw(g);


	//HUD
	g.setFont(new Font("Purisa", Font.PLAIN, 40));
	g.drawString("Aliens "+displayNumOfEnemies, 10, 550);
	g.drawString("Score "+currentScore, 10, 500);
	g.drawString("Time " + displayTimeLimit, 400, 50);


	g.setColor(Color.GRAY);
	g.fillRect(5, 5, 200, 25);

	if( player.getHealth() < 100){
		g.setColor(Color.RED);
	} else {
		g.setColor(Color.GREEN);
	}
	g.fillRect(5, 5, (int)player.getHealth(), 25);

	g.setColor(Color.WHITE);
	g.drawRect(5, 5, 200, 25);

	g.setFont(new Font("Purisa", Font.PLAIN, 30));
	g.setColor(Color.YELLOW);
	g.drawString("WAVE "+waveCount, 5, 60);
	//weapon count
	g.setFont(new Font("Purisa", Font.PLAIN, 25));
	g.drawString("Missile x " + player.getNumOfMissiles(), 350, 525);
	g.drawString("ShockWave x " + player.getNumOfShockWaves(), 350, 550);
}

/*
 * This method will take whatever was rendered in the background (from the shapeRender() method) and copy it to the screen.
 */
public void shapeDraw() {
	//System.out.println("Shape drawn");
	Graphics g2 = this.getGraphics();
	g2.drawImage(image, 0, 0, null);
	g2.dispose();


}
@Override
public void keyPressed(KeyEvent key) {
	int keyCode = key.getKeyCode();
	if(state == STATE.GAME){
		switch(keyCode) {// If the user presses the appropriate key, then set the shapes boolean value to true.

		case KeyEvent.VK_LEFT: { player.setTurnLeft(true); break;}
		case KeyEvent.VK_RIGHT: { player.setTurnRight(true); break;}
		case KeyEvent.VK_UP: { player.setForward(true); break;}
		case KeyEvent.VK_DOWN: { player.setBackward(true); break;}
		case KeyEvent.VK_CONTROL: { player.setShot(true, "bullet"); break; }
		case KeyEvent.VK_SPACE: { player.setShot(true,"missile"); break;}
		case KeyEvent.VK_X: { player.setShot(true,"shockWave"); break;}
		case KeyEvent.VK_ESCAPE: { state = STATE.MENU; break;}
		}
	} else if(state == STATE.MENU || state == STATE.HELP ){
		switch(keyCode) {
		case KeyEvent.VK_ESCAPE: { state = STATE.GAME; break;}
		}
	} else if(state == STATE.SETUP ){
		switch(keyCode) {
		case KeyEvent.VK_ESCAPE: { state = STATE.GAME; break;}
		}
	} else if(state == STATE.HELP ){
		switch(keyCode) {
		case KeyEvent.VK_ESCAPE: { state = STATE.GAME; break;}
		}
	} else if(state == STATE.STATS ){
		switch(keyCode) {
		case KeyEvent.VK_ESCAPE: { state = STATE.GAME; break;}
		}
	}
}

@Override
public void keyReleased(KeyEvent key) { // If the user releases the appropriate key, then set the shapes boolean value to false.
	int keyCode = key.getKeyCode();

	switch(keyCode) {

	case KeyEvent.VK_LEFT:  { player.setTurnLeft(false); break;}
	case KeyEvent.VK_RIGHT: { player.setTurnRight(false); break;}
	case KeyEvent.VK_UP: { player.setForward(false); break;}
	case KeyEvent.VK_DOWN: { player.setBackward(false); break;}
	case KeyEvent.VK_CONTROL: { player.setShot(false, "bullet"); break; }
	case KeyEvent.VK_SPACE: { player.setShot(false,"missile"); break;}
	case KeyEvent.VK_X: { player.setShot(false,"shockWave"); break;}
	}
}
@Override
public void keyTyped(KeyEvent key) {
	// TODO Auto-generated method stub

}


}
