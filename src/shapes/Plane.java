package shapes;

import org.newdawn.slick.geom.Polygon;

public class Plane {
	
	private float currentSpeed;
	private float flyAngle;
	private Polygon plane;
	private int ident;
	
	public Plane(){
		
	}

	public Plane(float x, float y, float angle, boolean copy){
		plane = new Polygon();
		
		this.plane.addPoint(9, 0);
		this.plane.addPoint(17, 21);
		this.plane.addPoint(9, 25);
		this.plane.addPoint(0, 21);
		
		plane.setCenterX(x);
		plane.setCenterY(y);
		
		this.flyAngle = angle;
		this.setIdent(-1);
		this.currentSpeed = 0.2f;
	}


	@Override
	public Plane clone(){
		Plane newPlane = new Plane();
		newPlane.setFlyAngle(this.getFlyAngle());
		newPlane.setPlane(this.getPlane().copy());
		newPlane.setCurrentSpeed(this.getCurrentSpeed());
		return newPlane;
	}
	
	public float getFlyAngle() {
		return flyAngle;
	}
	public void setFlyAngle(float flyAngle) {
		this.flyAngle = flyAngle;
	}
	public Polygon getPlane() {
		return plane;
	}
	public void setPlane(Polygon plane) {
		this.plane = plane;
	}

	public int getIdent() {
		return ident;
	}

	public void setIdent(int ident) {
		this.ident = ident;
	}
	public float getCurrentSpeed() {
		return currentSpeed;
	}

	public void setCurrentSpeed(float currentSpeed) {
		this.currentSpeed = currentSpeed;
	}
}
