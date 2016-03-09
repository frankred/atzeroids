package graph;

import java.awt.Font;

public class Settings {

	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;
	
	public static final float SHOOT_RADIUS = 2.5f;
	public static final float SHOOT_SPEED = 1.2f;
	public static final float SOUND_VOLUME = 0.1f;
	
	public static final float SPACE_SHUTTLE_ROTATION_SPEED = -0.0052f;
	
	public static final Font STATUS_FONT = new Font("Verdana", Font.PLAIN, 12);
	public static final Font MENU_FONT = new Font("Verdana", Font.PLAIN, 18);
	public static final Font MENU_FONT_CREDITS = new Font("Verdana", Font.PLAIN, 14);
	public static final Font MENU_FONT_ACTIVE = new Font("Verdana", Font.BOLD, 40);
	
	public static final String SETTINGS_FILE = "settings.properties";
}