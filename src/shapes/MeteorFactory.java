package shapes;

import graph.Settings;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

public class MeteorFactory {

	private int id = 0;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
	
	private int width;
	private int height;
	
	public MeteorFactory(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	public Meteor getBigMeteor(){
		return getMeteor(2.6, false);
	}
	
	public Meteor getSmallMeteor(){
		Random r = new Random();
		Meteor m = getMeteor(0.6, true); 
		
		// 0.2 - 0.5
		m.setFlySpeed((r.nextInt(30) + 20) / 100f);
		
		return m;
	}
	
	
	public Meteor getMeteor(double factor, boolean isSmall){
		Meteor m = new Meteor();
		Polygon p = new Polygon();
		Random r = new Random();
		
		int type = r.nextInt(2);

		// 0 - 360
		m.setFlyAngle(r.nextInt(3610) / 10f);

		// 0.01 - 0.30
		m.setFlySpeed((r.nextInt(31) + 1) / 100f);
		
		// - 0.30 - 0.30 
		m.setRotationSpeed((r.nextInt(61) - 30) / 8000f);
		
		if(type == 0){
			p.addPoint((int)(12 * factor), (int)(0 * factor));
			p.addPoint((int)(24 * factor), (int)(0 * factor));
			p.addPoint((int)(31 * factor), (int)(9 * factor));
			p.addPoint((int)(31 * factor), (int)(18 * factor));
			p.addPoint((int)(23 * factor), (int)(28 * factor));
			p.addPoint((int)(15 * factor), (int)(28 * factor));
			p.addPoint((int)(15 * factor), (int)(18 * factor));
			p.addPoint((int)(7 * factor), (int)(28 * factor));
			p.addPoint((int)(0 * factor), (int)(18 * factor));
			p.addPoint((int)(8 * factor), (int)(14 * factor));
			p.addPoint((int)(0 * factor), (int)(10 * factor));
			p.addPoint((int)(12 * factor), (int)(0 * factor));
		} else if (type == 1){
			p.addPoint((int)(7 * factor),(int)(0 * factor));
			p.addPoint((int)(16 * factor),(int)(0 * factor));
			p.addPoint((int)(27 * factor),(int)(9 * factor));
			p.addPoint((int)(15 * factor),(int)(14 * factor));
			p.addPoint((int)(26 * factor),(int)(21 * factor));
			p.addPoint((int)(20 * factor),(int)(27 * factor));
			p.addPoint((int)(15 * factor),(int)(24 * factor));
			p.addPoint((int)(6 * factor),(int)(27 * factor));
			p.addPoint((int)(0 * factor),(int)(16 * factor));
			p.addPoint((int)(0 * factor),(int)(8 * factor));
			p.addPoint((int)(11 * factor),(int)(8 * factor));
		}
		
		p.setCenterX(r.nextInt(800) + ((width-800) / 2));
		p.setCenterY(r.nextInt(600) + ((height-600) / 2));
		
		m.setSmall(isSmall);
		m.setMeteor(p);
		m.setClone(false);
		m.setId(id++);
		return m;
	}
	
	public Meteor getRandomMeteor(){
		Meteor m = new Meteor();
		Polygon p = new Polygon();
		Random r = new Random();
		
		// 0 - 360
		m.setFlyAngle(r.nextInt(3600) / 10f);

		// 0.01 - 0.30
		m.setFlySpeed((r.nextInt(30) + 1) / 100f);
		
		// - 0.10 - 0.10 
		m.setRotationSpeed((r.nextInt(20) - 10) / 8000f);
		
		Vector2f first = getRandomCoordinate(1);
		Vector2f second = getRandomCoordinate(2);
		Vector2f third = getRandomCoordinate(3);
		Vector2f fourth = getRandomCoordinate(4);
		
		p.addPoint(first.getX(), first.getY());
		p.addPoint(second.getX(), second.getY());
		p.addPoint(fourth.getX(), fourth.getY());
		p.addPoint(third.getX(), third.getY());
		
		p.setCenterX(r.nextInt(800) + ((width-800) / 2));
		p.setCenterY(r.nextInt(600) + ((height-600) / 2));

		m.setSmall(false);
		m.setMeteor(p);
		m.setClone(false);
		m.setId(id++);
		return m;
	}
	
	private static Vector2f getRandomCoordinate(int quadrant){
		Vector2f coordinate = new Vector2f();
		Random r = new Random();
		
		float x = 0;
		float y = 0;
		
		double rand = Math.random();
		
		if(quadrant == 1 || quadrant == 3){
			x = x + 100;
		}
		if(quadrant == 3 || quadrant == 4){
			y = y + 100;
		}

		x = x + r.nextInt(100);
		y = y + r.nextInt(100);
		coordinate.set(x, y);
		
		return coordinate;
	}
}
