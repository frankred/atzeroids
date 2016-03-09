package logic;

public class Player {

	private String name;
	private int points;
	private int lives;
	private int currentLevel;
	
	public Player(){
		this.points = 0;
		this.lives = 3;
		this.currentLevel = 1;
		this.name = new String();
	}
	
	public boolean isGameOver(){
		if(this.lives <= 0){
			return true;
		}
		return false;
	}
	
	public void died(){
		this.lives--;
	}
	
	public void up(){
		this.lives++;
	}
	
	public void nextLevel(){
		this.currentLevel++;
	}
	
	public void addPoints(int points){
		this.points = this.points + points;
	}
	
	public void resetPoints(){
		points = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}
}
