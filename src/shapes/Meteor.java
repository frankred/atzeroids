package shapes;

import org.newdawn.slick.geom.Polygon;

public class Meteor {
	
	private float flyAngle;
	private float flySpeed;
	private float rotationSpeed;
	private Polygon meteor;
	private int id;
	private boolean clone;
	private boolean topOut = false;
	private boolean rightOut = false;
	private boolean bottomOut = false;
	private boolean leftOut = false;
	private boolean small;
	
	public boolean isSmall() {
		return small;
	}

	public void setSmall(boolean small) {
		this.small = small;
	}

	public Meteor(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getFlyAngle() {
		return flyAngle;
	}

	public void setFlyAngle(float flyAngle) {
		this.flyAngle = flyAngle;
	}

	public float getFlySpeed() {
		return flySpeed;
	}

	public void setFlySpeed(float flySpeed) {
		this.flySpeed = flySpeed;
	}

	public float getRotationSpeed() {
		return rotationSpeed;
	}

	public void setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	public Polygon getMeteor() {
		return meteor;
	}

	public void setMeteor(Polygon meteor) {
		this.meteor = meteor;
	}

	public boolean isClone() {
		return clone;
	}

	public void setClone(boolean clone) {
		this.clone = clone;
	}
	
	public boolean isTopOut() {
		return topOut;
	}

	public void setTopOut(boolean topOut) {
		this.topOut = topOut;
	}

	public boolean isRightOut() {
		return rightOut;
	}

	public void setRightOut(boolean rightOut) {
		this.rightOut = rightOut;
	}

	public boolean isBottomOut() {
		return bottomOut;
	}

	public void setBottomOut(boolean bottomOut) {
		this.bottomOut = bottomOut;
	}

	public boolean isLeftOut() {
		return leftOut;
	}

	public void setLeftOut(boolean leftOut) {
		this.leftOut = leftOut;
	}

	@Override
	public Meteor clone(){
		Meteor newMeteor = new Meteor();
		newMeteor.setFlyAngle(this.getFlyAngle());
		newMeteor.setMeteor(this.getMeteor().copy());
		newMeteor.setRotationSpeed(this.getRotationSpeed());
		newMeteor.setFlySpeed(this.getFlySpeed());
		newMeteor.setId(this.getId());
		newMeteor.setClone(true);
		newMeteor.setSmall(this.isSmall());
		return newMeteor;
	}
}