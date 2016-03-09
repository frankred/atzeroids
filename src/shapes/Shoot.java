package shapes;

import graph.Settings;

import org.newdawn.slick.geom.Circle;

public class Shoot {

	private float angle;
	private Circle shoot;
	private float lastShootx;
	private float lastShooty;
	
	public Shoot(float x, float y, float angle) {
		this.shoot = new Circle(x,y, Settings.SHOOT_RADIUS);
		this.angle = angle;
		this.lastShootx = x;
		this.lastShooty = y;
	}

	public float getLastShootx() {
		return lastShootx;
	}


	public void setLastShootx(float lastShootx) {
		this.lastShootx = lastShootx;
	}


	public float getLastShooty() {
		return lastShooty;
	}


	public void setLastShooty(float lastShooty) {
		this.lastShooty = lastShooty;
	}


	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public Circle getShoot() {
		return shoot;
	}

	public void setShoot(Circle shoot) {
		this.shoot = shoot;
	}
}
