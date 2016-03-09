package shapes;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;

public class Ufo {
	
	private Line topLine;
	private Line middleLine;
	private float flyAngle;
	private float flySpeed;
	private int id;

	private boolean clone;
	private boolean topOut = false;
	private boolean rightOut = false;
	private boolean bottomOut = false;
	private boolean leftOut = false;
	private Polygon body;

	public Ufo(){
		body = new Polygon();
		
		body.addPoint(0, 8);
		body.addPoint(12, 0);
		body.addPoint(18, 0);
		body.addPoint(30, 8);
		body.addPoint(21, 13);
		body.addPoint(9, 13);
		body.addPoint(0, 8);

		middleLine = new Line(0, 8, 30, 8);
		topLine = new Line(8,3, 22,3);
	}

	public Line getTopLine() {
		return topLine;
	}

	public void setTopLine(Line topLine) {
		this.topLine = topLine;
	}

	public Line getMiddleLine() {
		return middleLine;
	}

	public void setMiddleLine(Line middleLine) {
		this.middleLine = middleLine;
	}

	public Polygon getBody() {
		return body;
	}

	public void setBody(Polygon body) {
		this.body = body;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

}
