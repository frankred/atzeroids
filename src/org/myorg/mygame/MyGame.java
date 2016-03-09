package org.myorg.mygame;

import graph.Settings;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import logic.GameStates;
import logic.Player;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import shapes.Meteor;
import shapes.MeteorFactory;
import shapes.Plane;
import shapes.Shoot;
import submit.SubmitScore;

@SuppressWarnings("deprecation")
public class MyGame extends BasicGame {
	
	// Banner
	private Image banner = null;

	// Game state
	private GameStates currentState = GameStates.ENTER_NAME;

	private int menues = 0;
	
	// Resolution
	private int width = 0;
	private int height = 0;
	
	// Graphics
	private ParticleSystem system;
	private MeteorFactory meteorFactory;
	private LinkedList<Shoot> shoots = new LinkedList<Shoot>();
	private LinkedList<Meteor> meteors = new LinkedList<Meteor>();
	private LinkedList<Plane> planes = new LinkedList<Plane>();
	private Circle planeSaveZone;
	private TextField textfield;
	private Polygon live;
	private Rectangle gameBorder;
	private Polygon gameBorderShadowLeft;
	private Polygon gameBorderShadowRight;
	
	
	// Player
	private Player player;

	// Sound
	private Sound shootSound;
	private Sound explodeSound;
	private Sound dieSound;
	
	private boolean planeDestroyed = false;
	private boolean emitterSpawned = false;
	
	// Fonts
	private TrueTypeFont gameStatusFont;
	private TrueTypeFont menuFont;
	private TrueTypeFont menuFontCredits;
	private TrueTypeFont menuFontActive;
	
	// New Objects
	private LinkedList<Meteor> newMeteors = new LinkedList<Meteor>();
	private ArrayList<Integer> removedMeteors = new ArrayList<Integer>();
	private LinkedList<Meteor> newMeteors2 = new LinkedList<Meteor>();
	private LinkedList<Plane> newPlanes = new LinkedList<Plane>();
	
	// Property file
	private Properties properties = new Properties();
	
	// Highscore
	private boolean highscoreSubmitted = false;

	public static int currentRanking = 0;
	public static boolean currentRankingEditable = false;
	
	private int gameWidth = 800;
	private int gameHeight = 600;
	
	private int gameWidthMargin;
	private int gameHeightMargin;
	
	
	public MyGame(int width, int height) {
		super("atzeroidz");
		this.height = height;
		this.width = width;
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		// Meteor Factory
		meteorFactory = new MeteorFactory(width, height);
		
		// Banner
		 banner = new Image("data/banner.png");
		 
		// Font
		gameStatusFont = new TrueTypeFont(Settings.STATUS_FONT, false);
		menuFont = new TrueTypeFont(Settings.MENU_FONT, false);
		menuFontActive = new TrueTypeFont(Settings.MENU_FONT_ACTIVE, false);
		menuFontCredits = new TrueTypeFont(Settings.MENU_FONT_CREDITS, false);
		
		// Sound
		shootSound = new Sound("sound/shoot.wav");
		explodeSound = new Sound("sound/explode.wav");
		dieSound = new Sound("sound/game_over.wav");
		
		// Player
		player = new Player();
		
		// Read property file
//		try {
//			properties.load(new FileInputStream(Settings.SETTINGS_FILE));
//			player.setName(properties.getProperty("name"));
//		} catch (FileNotFoundException e) {
//			player.setName("Player");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		
		// Name textfield
		int twidth = 200;
		int theight = 28;
		textfield = new TextField(gc, menuFont, width/2-twidth/2, height/2-theight/2, twidth, theight);
		textfield.setBackgroundColor(Color.white);
		textfield.setBorderColor(Color.white);
		textfield.setText(player.getName());
		textfield.setCursorPos(textfield.getText().length());
		textfield.setTextColor(Color.black);
		textfield.setFocus(true);
		
		live = new Polygon();
		live.addPoint(9, 0);
		live.addPoint(17, 21);
		live.addPoint(9, 25);
		live.addPoint(0, 21);
		
		this.gameWidthMargin = (width - gameWidth) / 2;
		this.gameHeightMargin = (height - gameHeight) /2;
		gameBorder = new Rectangle(gameWidthMargin, gameHeightMargin, gameWidth, gameHeight);

		
		gameBorderShadowLeft = new Polygon();
		gameBorderShadowLeft.addPoint(0, 0);
		gameBorderShadowLeft.addPoint(0, height);
		gameBorderShadowLeft.addPoint(width, height);
		gameBorderShadowLeft.addPoint(width, height-gameHeightMargin);
		gameBorderShadowLeft.addPoint(gameWidthMargin, height-gameHeightMargin);
		gameBorderShadowLeft.addPoint(gameWidthMargin, 0);		
		
		gameBorderShadowRight = new Polygon();
		gameBorderShadowRight.addPoint(0,0);
		gameBorderShadowRight.addPoint(width,0);		
		gameBorderShadowRight.addPoint(width,height);
		gameBorderShadowRight.addPoint(width-gameWidthMargin,height);
		gameBorderShadowRight.addPoint(width-gameWidthMargin,height-gameHeightMargin);
		gameBorderShadowRight.addPoint(width-gameWidthMargin,gameHeightMargin);
		gameBorderShadowRight.addPoint(0,gameHeightMargin);
		
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();

		// Enter Name
		if(currentState == GameStates.ENTER_NAME){
			if(input.isKeyPressed(Input.KEY_ENTER)){
				currentState = GameStates.MENUE;
				player.setName(textfield.getText());
//				properties.setProperty("name", textfield.getText());
//				 try {
//					properties.store(new FileOutputStream(Settings.SETTINGS_FILE), null);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}
		}
		
		// Main menue
		if(currentState == GameStates.MENUE){
			if(input.isKeyPressed(Input.KEY_ENTER)){
				if(menues == 0){
					currentState = GameStates.PLAY;
					refreshGame();
				}
				if(menues == 1){
					System.exit(0);
				}
			}
			if (input.isKeyPressed(Input.KEY_UP)) {
				menues--;
			}
			if (input.isKeyPressed(Input.KEY_DOWN)) {
				menues++;
			}
			if(menues < 0){
				menues++;
			}
			if(menues > 1){
				menues--;
			}
		}
		
		// Paused
		if(currentState == GameStates.PLAY_PAUSED){
			if(input.isKeyPressed(Input.KEY_P)){
				gc.pause();
				currentState = GameStates.PLAY;
			}
		}
		
		// Level over
		if(currentState == GameStates.PLAY_LEVEL_OVER){
			if(input.isKeyPressed(Input.KEY_ENTER)){
				currentState = GameStates.PLAY;
				player.nextLevel();
				player.up();
				refreshGame();				
			}
		}
		
		// Game over
		if(currentState == GameStates.GAME_OVER){
			
			if(!highscoreSubmitted){
				currentRankingEditable = true;
				highscoreSubmitted = true;
				Thread submitScore = new Thread(new SubmitScore(player.getPoints(), player.getName()));
				submitScore.start();
			}
			
			if(input.isKeyPressed(Input.KEY_ENTER)){
				currentState = GameStates.MENUE;
				String savePlayerName = player.getName();
				player = new Player();
				player.setName(savePlayerName);
				highscoreSubmitted = false;
				currentRankingEditable = false;
				currentRanking = 0;
			}
		}

		if(currentState == GameStates.PLAY_PLAYER_DEAD){
			// Player revives
			if(input.isKeyPressed(Input.KEY_ENTER)){
				Plane plane = new Plane(width / 2,height / 2,0, false);
				planes.add(plane);
				currentState = GameStates.PLAY;
			}
		}
		
		// Gameplay
		if(currentState == GameStates.PLAY || currentState == GameStates.PLAY_LEVEL_OVER || currentState == GameStates.PLAY_PLAYER_DEAD || currentState == GameStates.GAME_OVER){
			// Slick-Bugfix
			if(input.isKeyPressed(Input.KEY_ENTER)){
				
			}
			
			// Paused
			if(input.isKeyPressed(Input.KEY_P)){
				gc.pause();
				currentState = GameStates.PLAY_PAUSED;
			}
			
			if (input.isKeyDown(Input.KEY_LEFT)) {
				for (Iterator<Plane> iterPlanes = planes.iterator(); iterPlanes.hasNext();) {
					Plane plane = iterPlanes.next();
					plane.setPlane((Polygon)plane.getPlane().transform(Transform.createRotateTransform(Settings.SPACE_SHUTTLE_ROTATION_SPEED * delta, plane.getPlane().getCenterX(), plane.getPlane().getCenterY())));
					plane.setFlyAngle(plane.getFlyAngle() + (float)Math.toDegrees(Settings.SPACE_SHUTTLE_ROTATION_SPEED * delta));
				}
			}

			if (input.isKeyDown(Input.KEY_RIGHT)) {
				for (Iterator<Plane> iterPlanes = planes.iterator(); iterPlanes.hasNext();) {
					Plane plane = iterPlanes.next();
					plane.setPlane((Polygon)plane.getPlane().transform(Transform.createRotateTransform(-Settings.SPACE_SHUTTLE_ROTATION_SPEED * delta, plane.getPlane().getCenterX(), plane.getPlane().getCenterY())));			
					plane.setFlyAngle(plane.getFlyAngle() - (float)Math.toDegrees(Settings.SPACE_SHUTTLE_ROTATION_SPEED * delta));
				}
			}

			if (input.isKeyDown(Input.KEY_UP)) {
				double hip = 0.2f * delta;
				for (Iterator<Plane> iterPlanes = planes.iterator(); iterPlanes.hasNext();) {
					Plane plane = iterPlanes.next();
					plane.getPlane().setCenterX(plane.getPlane().getCenterX() + (float)(hip * Math.sin(Math.toRadians(plane.getFlyAngle()))));
					plane.getPlane().setCenterY(plane.getPlane().getCenterY() - (float)(hip * Math.cos(Math.toRadians(plane.getFlyAngle()))));
				}
			}

			
			// Create Shoots
			if (input.isKeyPressed(Input.KEY_LCONTROL)) {
				boolean playSound = false;
				for (Iterator<Plane> iterPlanes = planes.iterator(); iterPlanes.hasNext();) {
					Plane plane = iterPlanes.next();
					
					if(player.getCurrentLevel() == 1){
						Shoot shoot = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle());
						shoots.add(shoot);
					}else if( player.getCurrentLevel() == 2){
						Shoot shoot1 = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle()+2);
						Shoot shoot2 = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle()-2);
						shoots.add(shoot1);
						shoots.add(shoot2);
					}else if(player.getCurrentLevel() >= 3 && player.getCurrentLevel() <= 6){
						Shoot shoot1 = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle()+3);
						Shoot shoot2 = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle()-3);
						Shoot shoot3 = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle());
						shoots.add(shoot1);
						shoots.add(shoot2);
						shoots.add(shoot3);	
					}else if(player.getCurrentLevel() >= 7 && player.getCurrentLevel() <= 8){
						Shoot shoot0 = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle());
						Shoot shoot1 = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle()+4);
						Shoot shoot2 = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle()-4);
						Shoot shoot3 = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle() +2);
						Shoot shoot4 = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle() - 2);
						shoots.add(shoot0);
						shoots.add(shoot1);
						shoots.add(shoot2);
						shoots.add(shoot3);	
						shoots.add(shoot4);	
					} else if(player.getCurrentLevel() >= 9){
						Shoot shoot0 = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle());
						Shoot shoot1 = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle()+4);
						Shoot shoot2 = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle()-4);
						Shoot shoot3 = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle() +2);
						Shoot shoot4 = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle() - 2);
						shoots.add(shoot0);
						shoots.add(shoot1);
						shoots.add(shoot2);
						shoots.add(shoot3);	
						shoots.add(shoot4);
					}
					playSound = true;
				}
				if(playSound){
					shootSound.play(1,Settings.SOUND_VOLUME);
				}
			}

			// Calculate shoots
			for (Iterator<Shoot> shootIter = shoots.iterator(); shootIter.hasNext();) {
				Shoot shoot = shootIter.next();
				float hip = Settings.SHOOT_SPEED * delta;
				
				shoot.setLastShootx(shoot.getShoot().getCenterX());
				shoot.setLastShooty(shoot.getShoot().getCenterY());
				
				shoot.getShoot().setCenterX((float) (shoot.getShoot().getCenterX() + hip * Math.sin(Math.toRadians(shoot.getAngle()))));
				shoot.getShoot().setCenterY((float) (shoot.getShoot().getCenterY() - hip * Math.cos(Math.toRadians(shoot.getAngle()))));

				if (shoot.getShoot().getCenterX() > ((width-gameWidthMargin) + Settings.SHOOT_RADIUS) 
						|| (shoot.getShoot().getCenterX() < (gameWidthMargin-Settings.SHOOT_RADIUS)) 
						|| (shoot.getShoot().getCenterY() < (gameHeightMargin-Settings.SHOOT_RADIUS)) 
						|| (shoot.getShoot().getCenterY() > (height-gameHeightMargin + Settings.SHOOT_RADIUS))){
					shootIter.remove();
				}
			}
			
			// Calculate meteors
			for (Iterator<Meteor> meteorIter = meteors.iterator(); meteorIter.hasNext();) {
				Meteor meteor = meteorIter.next();
				float hip = meteor.getFlySpeed() * delta;
				meteor.getMeteor().setX((float)(meteor.getMeteor().getX() + hip * Math.sin(Math.toRadians(meteor.getFlyAngle()))));
				meteor.getMeteor().setY((float)(meteor.getMeteor().getY() - hip * Math.cos(Math.toRadians(meteor.getFlyAngle()))));
				meteor.setMeteor((Polygon) meteor.getMeteor().transform(Transform.createRotateTransform((float)(delta * meteor.getRotationSpeed()), meteor.getMeteor().getCenterX(), meteor.getMeteor().getCenterY())));

				// Right Out
				if(!meteor.isClone() && !meteor.isRightOut() && meteor.getMeteor().getMaxX() > gameBorder.getMaxX()){
					Meteor newMeteor = meteor.clone();
					meteor.setRightOut(true);
					newMeteor.getMeteor().setCenterX(newMeteor.getMeteor().getCenterX() - gameBorder.getWidth());
					newMeteors.add(newMeteor);
				} 
				
				// Bottom Out
				if(!meteor.isClone() && !meteor.isBottomOut() && meteor.getMeteor().getMaxY() > gameBorder.getMaxY()){
					Meteor newMeteor = meteor.clone();
					meteor.setBottomOut(true);
					newMeteor.getMeteor().setCenterY(newMeteor.getMeteor().getCenterY() - gameBorder.getHeight());
					newMeteors.add(newMeteor);
				}
				
				// Left Out
				if(!meteor.isClone() && !meteor.isLeftOut() && meteor.getMeteor().getMinX() < gameBorder.getMinX()){
					Meteor newMeteor = meteor.clone();
					meteor.setLeftOut(true);
					newMeteor.getMeteor().setCenterX(newMeteor.getMeteor().getCenterX() + gameBorder.getWidth());
					newMeteors.add(newMeteor);
				}
				
				// Top Out
				if(!meteor.isClone() && !meteor.isTopOut() && meteor.getMeteor().getMinY() < gameBorder.getMinY()){
					Meteor newMeteor = meteor.clone();
					meteor.setTopOut(true);
					newMeteor.getMeteor().setCenterY(newMeteor.getMeteor().getCenterY() + gameBorder.getHeight());
					newMeteors.add(newMeteor);
				}
				
				if(meteor.getMeteor().getMinX() > gameBorder.getMaxX()
						|| meteor.getMeteor().getMinY() > gameBorder.getMaxY()
						|| meteor.getMeteor().getMaxX() < gameBorder.getMinX()
						|| meteor.getMeteor().getMaxY() < gameBorder.getMinY()){
					if(!meteor.isClone()){
						removedMeteors.add(meteor.getId());						
					}
					meteorIter.remove();
				}
			}
			
			for(int i : removedMeteors){
				refreshClones(i);
			}
			meteors.addAll(newMeteors);
			
			newMeteors.clear() ;
			removedMeteors.clear();
			
			
			
			// Calculate collisions shoot <=> meteors 
			emitterSpawned = false;
			for (Iterator<Shoot> shootIter = shoots.iterator(); shootIter.hasNext();) {
				Shoot shoot = shootIter.next();
				
				int meteorDeleteId = -1; 
				
				for (Iterator<Meteor> meteorIter = meteors.iterator(); meteorIter.hasNext();) {
					Meteor meteor = meteorIter.next();	
					if (shoot.getShoot().intersects(meteor.getMeteor()) || meteor.getMeteor().contains(shoot.getShoot()) || shoot.getShoot().contains(meteor.getMeteor())) {
						explodeSound.play(1, Settings.SOUND_VOLUME * 3);
						try {
							ConfigurableEmitter emitter = ParticleIO.loadEmitter("emitter/explode_2.xml");
							emitter.resetState();
							emitter.setPosition(shoot.getShoot().getCenterX(), shoot.getShoot().getCenterY(), false);
							emitterSpawned = true;
							system.addEmitter(emitter);
							
							if(meteor.isSmall()){
								player.addPoints(200);
							}else{
								player.addPoints(100);
								Meteor s1 = meteorFactory.getSmallMeteor();
								s1.getMeteor().setCenterX(shoot.getShoot().getCenterX());
								s1.getMeteor().setCenterY(shoot.getShoot().getCenterY());

								Meteor s2 = meteorFactory.getSmallMeteor();
								s2.getMeteor().setCenterX(shoot.getShoot().getCenterX());
								s2.getMeteor().setCenterY(shoot.getShoot().getCenterY());			
								newMeteors2.add(s1);
								newMeteors2.add(s2);									
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						meteorDeleteId = meteor.getId();
						meteorIter.remove();
						shootIter.remove();
						break;
					}
				}
				
				if(meteorDeleteId > 0){
					deleteMeteors(meteorDeleteId);      
				}
			}
			meteors.addAll(newMeteors2);
			newMeteors2.clear();
			
			// Calculate collisions meteors <=> planes
			for (Iterator<Meteor> meteorIter = meteors.iterator(); meteorIter.hasNext();) {
				Meteor meteor = meteorIter.next();	
				for (Iterator<Plane> iterPlanes = planes.iterator(); iterPlanes.hasNext();) {
					Plane plane = iterPlanes.next();
					if(plane.getPlane().intersects(meteor.getMeteor())){
						planeDestroyed = true;
						explodeSound.play(1, Settings.SOUND_VOLUME * 3);
						try {
							ConfigurableEmitter emitter = ParticleIO.loadEmitter("emitter/shuttleExplosion.xml");
							emitter.resetState();
							emitter.setPosition(plane.getPlane().getCenterX(),plane.getPlane().getCenterY(), false);
							emitterSpawned = true;
							system.addEmitter(emitter);
						} catch (IOException e) {
							e.printStackTrace();
						}

						player.died();
						currentState = GameStates.PLAY_PLAYER_DEAD;
						explodeSound.play(1, Settings.SOUND_VOLUME * 3);
						dieSound.play(4, Settings.SOUND_VOLUME * 2);
						meteorIter.remove();
						iterPlanes.remove();
						break;
					}
				}
			}
			
			// Destroy all planes
			if(planeDestroyed){
				planes = new LinkedList<Plane>();
				planeDestroyed = false;
			}

			// Delete used emitter
			if(!emitterSpawned){
				for(int i = 0; i < system.getEmitterCount(); i++){
					system.getEmitter(i).wrapUp();
				}			
			}

			// Calculate plane copy, when exit screen
			for (Iterator<Plane> iterPlanes = planes.iterator(); iterPlanes.hasNext();) {
				Plane plane = iterPlanes.next();
				if(plane.getPlane().getMaxX() > width && !containsPlaneIdent(1)){
					Plane newPlane = plane.clone();
					newPlane.getPlane().setCenterX(newPlane.getPlane().getCenterX()-width);
					newPlane.setIdent(1);
					newPlanes.add(newPlane);
				}
				if(plane.getPlane().getMaxY() > height && !containsPlaneIdent(2)){
					Plane newPlane = plane.clone();
					newPlane.getPlane().setCenterY(newPlane.getPlane().getCenterY()-height);
					newPlane.setIdent(2);
					newPlanes.add(newPlane);
				}
				if(plane.getPlane().getMinX() < 0 && !containsPlaneIdent(3)){
					Plane newPlane = plane.clone();
					newPlane.getPlane().setCenterX(newPlane.getPlane().getCenterX()+width);
					newPlane.setIdent(3);
					newPlanes.add(newPlane);
				}
				if(plane.getPlane().getMinY() < 0 && !containsPlaneIdent(4)){
					Plane newPlane = plane.clone();
					newPlane.getPlane().setCenterY(newPlane.getPlane().getCenterY()+height);
					newPlane.setIdent(4);
					newPlanes.add(newPlane);
				}
			}
			planes.addAll(newPlanes);
			newPlanes.clear();
			
			// Reset ident number for verify clones
			if(planes.size() == 1){
				planes.getFirst().setIdent(-1);
			}

			// Remove planes out of view
			for (Iterator<Plane> iterPlanes = planes.iterator(); iterPlanes.hasNext();) {
				Plane plane = iterPlanes.next();
				if(plane.getPlane().getMinX() > width ){
					iterPlanes.remove();
				}
				if(plane.getPlane().getMinY() > height){
					iterPlanes.remove();
				}
				if(plane.getPlane().getMaxX() < 0){
					iterPlanes.remove();
				}
				if(plane.getPlane().getMaxY() < 0){
					iterPlanes.remove();
				}
			}
			
			// Explosion
			system.update(delta);
			
			if(player.isGameOver()){
				currentState = GameStates.GAME_OVER;
			}
			
			if(meteors.size() == 0){
				currentState = GameStates.PLAY_LEVEL_OVER;
			}
		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		
		// Game paused
		if(currentState == GameStates.PLAY_PAUSED){
			String pause = new String("Pause");
			gameStatusFont.drawString(width / 2 - gameStatusFont.getWidth(pause)/2,height / 2 - gameStatusFont.getHeight(pause) , pause);
		}
		
		
		// Level over
		if(currentState == GameStates.PLAY_LEVEL_OVER){
			String levelFinished = new String("LEVEL FINISHED");
			menuFontActive.drawString(width / 2 - menuFontActive.getWidth(levelFinished)/2,height / 2 - menuFontActive.getHeight(levelFinished) , levelFinished);
			String pressEnterToContinue = new String("[PRESS ENTER TO CONTINUE TO LEVEL: " + (player.getCurrentLevel()+1) +"]");
			gameStatusFont.drawString(width / 2 - gameStatusFont.getWidth(pressEnterToContinue)/2,height / 2 - gameStatusFont.getHeight(pressEnterToContinue) + menuFontActive.getHeight(levelFinished), pressEnterToContinue);
		}
		
		// Player dead
		if(currentState == GameStates.PLAY_PLAYER_DEAD){
			String pressEnterToRevive = new String("[PRESS ENTER TO REVIVE]");
			gameStatusFont.drawString(width / 2 - gameStatusFont.getWidth(pressEnterToRevive)/2,height / 2 - gameStatusFont.getHeight(pressEnterToRevive) + menuFontActive.getHeight(pressEnterToRevive), pressEnterToRevive);
		}
		
		// Game over
		if(currentState == GameStates.GAME_OVER){
			String gameOver = new String("GAME OVER");
			menuFontActive.drawString(width / 2 - menuFontActive.getWidth(gameOver)/2,height / 2, gameOver);
			String score = new String("SCORE: " + player.getPoints());
			menuFont.drawString(width/2 - menuFont.getWidth(score)/2, height/2 + menuFontActive.getHeight(gameOver) , score);

			
			if(currentRanking == 0){
				String ranking = new String("RANKING: " + "loading...");
				menuFont.drawString(width/2 - menuFont.getWidth(ranking)/2, height/2 + menuFontActive.getHeight(gameOver) +  menuFont.getHeight(score), ranking);
			}else{
				String ranking = new String("RANKING: " + currentRanking);
				menuFont.drawString(width/2 - menuFont.getWidth(ranking)/2, height/2 + menuFontActive.getHeight(gameOver) +  menuFont.getHeight(score), ranking);
			}
		}
		
		// Gameplay
		if(currentState == GameStates.PLAY || currentState == GameStates.PLAY_PAUSED || currentState == GameStates.PLAY_LEVEL_OVER ||  currentState == GameStates.PLAY_PLAYER_DEAD || currentState == GameStates.GAME_OVER){

			for(Plane plane : planes){
				g.draw(plane.getPlane());
			}
			
			for (Shoot shoot : shoots) {
				g.fill(shoot.getShoot());
				g.draw(shoot.getShoot());
			}
			
			for (Meteor meteor : meteors) {
				g.draw(meteor.getMeteor());
			}

			system.render();

			g.setColor(Color.black);
			g.fill(gameBorderShadowLeft);
			g.draw(gameBorderShadowLeft);
			g.fill(gameBorderShadowRight);
			g.draw(gameBorderShadowRight);
			g.setColor(Color.white);
			
			g.draw(gameBorder);
			
			gameStatusFont.drawString(width - 80, 15, "FPS: " + gc.getFPS());
			gameStatusFont.drawString(15, 15, "Score: ");
			menuFont.drawString(70, 10, player.getPoints()+"");
			gameStatusFont.drawString(150, 15, "Level: " + player.getCurrentLevel());
			gameStatusFont.drawString(250, 15, "Lives: ");
			
			for(int i = 0; i < player.getLives(); i++){
				live.setCenterX(320 + (i*28));
				live.setCenterY(30);
				g.draw(live);
			}
		}
		
		// Main menue
		if(currentState == GameStates.MENUE){
			float x = width / 2;
			float y = height / 3;
			
			String start = new String("START");
			String optionen = new String("OPTIONEN");
			String end = new String("ENDE");
			
			banner.draw(x - banner.getWidth()/2, y - banner.getHeight());
			
			String madeBy = new String("Atzenroidz Version 0.1 made by Frank Roth (frankred@web.de) with SLICK2D");
			menuFontCredits.drawString(10, height - 30, madeBy);
			
			float marginTop = 0;
			if(menues == 0){
				menuFontActive.drawString(x - menuFontActive.getWidth(start)/2, y + marginTop, start);
				marginTop = marginTop + menuFontActive.getHeight(start);
			}else{
				menuFont.drawString(x - menuFont.getWidth(start)/2, y + marginTop, start);
				marginTop = marginTop + menuFont.getHeight(start);
			}

			if(menues == 1){
				menuFontActive.drawString(x - menuFontActive.getWidth(end)/2, y + marginTop, end);
				marginTop = marginTop + menuFontActive.getHeight(end);
			}else{
				menuFont.drawString(x - menuFont.getWidth(end)/2, y + marginTop, end);
				marginTop = marginTop + menuFont.getHeight(end);
			}
		}
		
		// Name
		if(currentState == GameStates.ENTER_NAME){
			String madeBy = new String("Atzenroidz Version 0.1 made by Frank Roth (frankred@web.de) with SLICK2D");
			menuFontCredits.drawString(10, height - 30, madeBy);
			
			float x = width / 2;
			float y = height / 3;
			banner.draw(x - banner.getWidth()/2, y - banner.getHeight());
			textfield.render(gc, g);
			String enterName = "Enter your player name and confirm with [ENTER]";
			menuFont.drawString(width/2 - menuFont.getWidth(enterName)/2, textfield.getY() - menuFont.getHeight(enterName) - 10, enterName);
		}
		
	}
	
	private void refreshGame(){
		// Explosion
		system = new ParticleSystem("data/explode.GIF",100);
		system.setRemoveCompletedEmitters(true);
		
		planes = new LinkedList<Plane>();
		meteors = new LinkedList<Meteor>();
		shoots = new LinkedList<Shoot>();
		
		Plane plane = new Plane(width / 2,height / 2,0, false);
		planes.add(plane);
		planeSaveZone = new Circle(width / 2, height / 2, width / 6);
		
		for(int i = 0; i < (player.getCurrentLevel()*2); i++){
			Meteor m = meteorFactory.getBigMeteor();
			if(!planeSaveZone.contains(m.getMeteor()) && !planeSaveZone.intersects(m.getMeteor())){
				meteors.add(m);					
			}else{
				i--;
			}
		}
	}
	
	private void deleteMeteors(int id){
		for (Iterator<Meteor> meteorIter = meteors.iterator(); meteorIter.hasNext();) {
			Meteor meteor = meteorIter.next();
			if(meteor.getId() == id){
				meteorIter.remove();
			}
		}			
	}
	
	private void refreshClones(int id){
		for(Meteor m : meteors){
			if(m.getId() == id){
				m.setClone(false);
			}
		}
	}
	
	private boolean containsPlaneIdent(int ident){
		for(Plane plane : planes){
			if(plane.getIdent() == ident){
				return true;
			}
		}
		return false;
	}
	
	
	public static void main(String[] args) throws SlickException {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		AppGameContainer app = new AppGameContainer(new MyGame((int)screenSize.getWidth(),(int)screenSize.getHeight()));
		app.setDisplayMode((int)screenSize.getWidth(), (int)screenSize.getHeight(), false);
		app.setShowFPS(false);
		app.setVSync(true);
		app.setFullscreen(true);
		app.start();
	}
}