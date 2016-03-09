package de.slick.tutorial;

import graph.Settings;

import java.util.Iterator;
import java.util.LinkedList;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Transform;

import shapes.Meteor;
import shapes.Plane;
import shapes.Shoot;

public class SpacyBasicGame extends BasicGame {

	// Bilder
//	Image plane = null;

//	Image background = null;

	// Emitter, absorbiert den Rauch der aus dem Raumschiff kommen soll.
//	ConfigurableEmitter spaceShuttleTailEmitterDrive;

	// ParticleSystem ist ein Container in dem mehrere Emitter hinzugefügt
	// werden können.
//	ParticleSystem particleSystemShuttle;
//	int maxParticleCount = 200;
//	int particleSpawn = 20;
//	int particleSpawnStop = 2;
//	int particleDistance = 14;

	float x;
	float y;

	// Raumschiffradius
	float r;

	
	LinkedList<Shoot> shoots = new LinkedList<Shoot>();
	LinkedList<Meteor> meteors = new LinkedList<Meteor>();
	LinkedList<Plane> planes = new LinkedList<Plane>();

	// Punktestand des Spielers
	int points = 0;

	boolean keyWasDown = true;
	
	// Sound
	Sound shootSound;
	Sound explodeSound;
	Sound dieSound;
	
	@SuppressWarnings("deprecation")
	TrueTypeFont font;
	
	public SpacyBasicGame() {
		super("#klo");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void init(GameContainer gc) throws SlickException {
//		plane = new Image("data/plane.png");
		
//		background = new Image("data/bg_space.gif");

		// calculate plane radius
		// r = plane.getHeight() / 2;
		
		Plane plane = new Plane(Settings.SCREEN_WIDTH / 2,Settings.SCREEN_HEIGHT / 2,0, false);
		planes.add(plane);

//		try {
//			spaceShuttleTailEmitterDrive = ParticleIO
//					.loadEmitter("emitter/pink_smoke.xml");
//			spaceShuttleTailEmitterDrive.initialDistance
//					.setMin(particleDistance);
//			spaceShuttleTailEmitterDrive.initialDistance
//					.setMax(particleDistance);
//
//			particleSystemShuttle = new ParticleSystem("testdata/smoke.tga",
//					maxParticleCount);
//			particleSystemShuttle.addEmitter(spaceShuttleTailEmitterDrive);
//
//			particleSystemShuttle
//					.setBlendingMode(ParticleSystem.BLEND_ADDITIVE);
//			particleSystemShuttle.setUsePoints(false);

			
			// Sound
			shootSound = new Sound("sound/shoot.wav");
			explodeSound = new Sound("sound/explode.wav");
			dieSound = new Sound("sound/game_over.wav");
			
			// Meteor
			meteors.add(Meteor.getRandomMeteor());
			
			// Font
			font = new TrueTypeFont(Settings.STATUS_FONT, false);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		gc.setMouseGrabbed(true);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {

		Input input = gc.getInput();

		if(input.isKeyPressed(Input.KEY_F5)){
			restartGame();
		}
		
		if(input.isKeyDown(Input.KEY_M)){
			meteors.add(Meteor.getRandomMeteor());
		}
		
		if (input.isKeyDown(Input.KEY_LEFT)) {
//			plane.rotate(-0.3f * delta);
			for (Iterator<Plane> iterPlanes = planes.iterator(); iterPlanes.hasNext();) {
				Plane plane = iterPlanes.next();
				plane.setPlane((Polygon)plane.getPlane().transform(Transform.createRotateTransform(Settings.SPACE_SHUTTLE_ROTATION_SPEED * delta, plane.getPlane().getCenterX(), plane.getPlane().getCenterY())));
				plane.setFlyAngle(plane.getFlyAngle() + (float)Math.toDegrees(Settings.SPACE_SHUTTLE_ROTATION_SPEED * delta));
			}
		}

		if (input.isKeyDown(Input.KEY_RIGHT)) {
//			plane.rotate(0.3f * delta);
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
			shootSound.play(1,Settings.SOUND_VOLUME);
			
			for (Iterator<Plane> iterPlanes = planes.iterator(); iterPlanes.hasNext();) {
				Plane plane = iterPlanes.next();
				Shoot shoot = new Shoot(plane.getPlane().getCenterX(), plane.getPlane().getCenterY(), plane.getFlyAngle());
				shoots.add(shoot);
			}
		}

		// Calculate shoots
		for (Iterator<Shoot> iter = shoots.iterator(); iter.hasNext();) {
			Shoot shoot = iter.next();
			float hip = Settings.SHOOT_SPEED * delta;
			
			shoot.setLastShootx(shoot.getShoot().getCenterX());
			shoot.setLastShooty(shoot.getShoot().getCenterY());
			
			shoot.getShoot().setCenterX((float) (shoot.getShoot().getCenterX() + hip * Math.sin(Math.toRadians(shoot.getAngle()))));
			shoot.getShoot().setCenterY((float) (shoot.getShoot().getCenterY() - hip * Math.cos(Math.toRadians(shoot.getAngle()))));

			if (shoot.getShoot().getCenterX() > Settings.SCREEN_WIDTH + Settings.SHOOT_RADIUS 
					|| (shoot.getShoot().getCenterX() < (0-Settings.SHOOT_RADIUS)) 
					|| (shoot.getShoot().getCenterY() < (0-Settings.SHOOT_RADIUS)) 
					|| (shoot.getShoot().getCenterY() > (Settings.SCREEN_HEIGHT + Settings.SHOOT_RADIUS))){
				iter.remove();
			}
		}
		
		// Calculate Meteor
		for (Iterator<Meteor> iter = meteors.iterator(); iter.hasNext();) {
			Meteor meteor = iter.next();
			float hip = meteor.getFlySpeed() * delta;
			meteor.getMeteor().setX((float)(meteor.getMeteor().getX() + hip * Math.sin(Math.toRadians(meteor.getFlyAngle()))));
			meteor.getMeteor().setY((float)(meteor.getMeteor().getY() - hip * Math.cos(Math.toRadians(meteor.getFlyAngle()))));
			meteor.setMeteor((Polygon) meteor.getMeteor().transform(Transform.createRotateTransform((float)(delta * meteor.getRotationSpeed()), meteor.getMeteor().getCenterX(), meteor.getMeteor().getCenterY())));
		}
		
		// Calculate collision
		for (Iterator<Meteor> meteorIter = meteors.iterator(); meteorIter.hasNext();) {
			Meteor meteor = meteorIter.next();	
			for (Iterator<Shoot> shootIter = shoots.iterator(); shootIter.hasNext();) {
				Shoot shoot = shootIter.next();

				Line helpLine = new Line(shoot.getLastShootx(),
						shoot.getLastShooty(), shoot.getShoot().getCenterX(),
						shoot.getShoot().getCenterY());

				if (helpLine.intersects(meteor.getMeteor())) {
					meteorIter.remove();
					shootIter.remove();
					explodeSound.play(1, Settings.SOUND_VOLUME * 3);
					points++;
					break;
				}
			}
			
			for (Iterator<Plane> iterPlanes = planes.iterator(); iterPlanes.hasNext();) {
				Plane plane = iterPlanes.next();
				if(plane.getPlane().intersects(meteor.getMeteor())){
					meteorIter.remove();
					iterPlanes.remove();
					dieSound.play(1, Settings.SOUND_VOLUME * 5);
				}
			}
		}

		// Calculate plane copy, when exit screen
		LinkedList<Plane> newPlanes = new LinkedList<Plane>();
		for (Iterator<Plane> iterPlanes = planes.iterator(); iterPlanes.hasNext();) {
			Plane plane = iterPlanes.next();
			if(plane.getPlane().getMaxX() > Settings.SCREEN_WIDTH && !containsIdent(1)){
				Plane newPlane = plane.clone();
				newPlane.getPlane().setCenterX(newPlane.getPlane().getCenterX()-Settings.SCREEN_WIDTH);
				newPlane.setIdent(1);
				newPlanes.add(newPlane);
			}
			if(plane.getPlane().getMaxY() > Settings.SCREEN_HEIGHT && !containsIdent(2)){
				Plane newPlane = plane.clone();
				newPlane.getPlane().setCenterY(newPlane.getPlane().getCenterY()-Settings.SCREEN_HEIGHT);
				newPlane.setIdent(2);
				newPlanes.add(newPlane);
			}
			if(plane.getPlane().getMinX() < 0 && !containsIdent(3)){
				Plane newPlane = plane.clone();
				newPlane.getPlane().setCenterX(newPlane.getPlane().getCenterX()+Settings.SCREEN_WIDTH);
				newPlane.setIdent(3);
				newPlanes.add(newPlane);
			}
			if(plane.getPlane().getMinY() < 0 && !containsIdent(4)){
				Plane newPlane = plane.clone();
				newPlane.getPlane().setCenterY(newPlane.getPlane().getCenterY()+Settings.SCREEN_HEIGHT);
				newPlane.setIdent(4);
				newPlanes.add(newPlane);
			}
		}
		planes.addAll(newPlanes);

		// Reset ident number for verify clones
		if(planes.size() == 1){
			planes.getFirst().setIdent(-1);
		}

		// Remove planes out of view
		for (Iterator<Plane> iterPlanes = planes.iterator(); iterPlanes.hasNext();) {
			Plane plane = iterPlanes.next();
			if(plane.getPlane().getMinX() > Settings.SCREEN_WIDTH ){
				iterPlanes.remove();
			}
			if(plane.getPlane().getMinY() > Settings.SCREEN_HEIGHT){
				iterPlanes.remove();
			}
			if(plane.getPlane().getMaxX() < 0){
				iterPlanes.remove();
			}
			if(plane.getPlane().getMaxY() < 0){
				iterPlanes.remove();
			}
		}

/*		// Set particle emitter position to center of plane
//		((ConfigurableEmitter) particleSystemShuttle.getEmitter(0)).setPosition(mx, my, false);
//
//		// Da der ParticleEmitter in die falsch Richtung zeigt wird er um 180°
//		// gedreht.
//		spaceShuttleTailEmitterDrive.angularOffset.setValue((plane.getRotation() + 180) % 360);
//		particleSystemShuttle.update(delta);
 */
	}

	@SuppressWarnings({ "deprecation", "deprecation" })
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// Komponenten rendern
//		background.draw(0, 0);
//		particleSystemShuttle.render();
//		plane.draw(x, y);

		for(Plane plane : planes){
			g.draw(plane.getPlane());
		}
		
		for (Shoot shoot : shoots) {
			g.draw(shoot.getShoot());
		}
		
		for (Meteor meteor : meteors) {
			g.draw(meteor.getMeteor());
		}
		
		font.drawString(Settings.SCREEN_WIDTH - 50, 15, "FPS: " + gc.getFPS());
		font.drawString(15, 15, "Punkte: " + points);
//		font.drawString(5, 15,"Particle Count: " + particleSystemShuttle.getParticleCount());
	}
	private boolean containsIdent(int ident){
		for(Plane plane : planes){
			if(plane.getIdent() == ident){
				return true;
			}
		}
		return false;
	}
	
	private void restartGame(){
		planes = new LinkedList<Plane>();
		meteors = new LinkedList<Meteor>();
		shoots = new LinkedList<Shoot>();
		
		Plane plane = new Plane(Settings.SCREEN_WIDTH / 2,Settings.SCREEN_HEIGHT / 2,0, false);
		planes.add(plane);
	}
}