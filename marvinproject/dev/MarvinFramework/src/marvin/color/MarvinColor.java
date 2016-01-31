package marvin.color;

import java.util.HashMap;
import java.util.Map;

public class MarvinColor {

	public int		id;
	public String	name; 
	public int 		red,green,blue;
	
	/**
	 * Constructor for developer created colors
	 * @param r
	 * @param g
	 * @param b
	 */
	public MarvinColor(int r, int g, int b){
		this(-1, null, r, g, b);
	}
	
	/**
	 * Constructor for predefined colors 
	 * @param id
	 * @param name
	 * @param r
	 * @param g
	 * @param b
	 */
	public MarvinColor(int id, String name, int r, int g, int b){
		this.id = id;
		this.name = name;
		this.red = r;
		this.green = g;
		this.blue = b;
	}
	
	public static MarvinColor getColorById(Integer id){
		return mapColorById.get(id);
	}
	
	public static MarvinColor getColorByName(String name){
		return mapColorByName.get(name);
	}
	
	// BLACK AND WHITE
	public static MarvinColor	BLACK 			= new MarvinColor(0, 	"black", 		0, 0, 0);
	public static MarvinColor	WHITE			= new MarvinColor(1, 	"white", 		255, 255, 255);
	// GRAY
	public static MarvinColor	DARK_GRAY		= new MarvinColor(2, 	"dark_gray", 	64, 64, 64);
	public static MarvinColor	GRAY			= new MarvinColor(3, 	"dark_gray", 	127, 127, 127);
	public static MarvinColor	LIGHT_GRAY		= new MarvinColor(4, 	"light_gray", 	191, 191, 191);
	// RED
	public static MarvinColor	DARK_RED		= new MarvinColor(5, 	"dark_red", 	127, 0, 0);
	public static MarvinColor	RED				= new MarvinColor(6, 	"red", 			255, 0, 0);
	public static MarvinColor	LIGHT_RED		= new MarvinColor(7, 	"light_red", 	255, 127, 127);
	// GREEN
	public static MarvinColor	DARK_GREEN		= new MarvinColor(8, 	"dark_green", 	0, 127, 0);
	public static MarvinColor	GREEN			= new MarvinColor(9, 	"green", 		0, 255, 0);
	public static MarvinColor	LIGHT_GREEN		= new MarvinColor(10, 	"light_green",	127, 255, 127);
	// BLUE
	public static MarvinColor	DARK_BLUE		= new MarvinColor(11, 	"dark_blue",	0, 0, 127);
	public static MarvinColor	BLUE			= new MarvinColor(12, 	"blue",			0, 0, 255);
	public static MarvinColor	LIGHT_BLUE		= new MarvinColor(13, 	"light_blue",	127, 127, 255);
	// ORANGE
	public static MarvinColor	ORANGE			= new MarvinColor(14, 	"orange",		255, 127, 0);
	public static MarvinColor	LIGHT_ORANGE	= new MarvinColor(15, 	"light_orange",	255, 192, 130);
	// BROWN
	public static MarvinColor	DARK_BROWN		= new MarvinColor(16, 	"dark_brown",	106, 53, 0);
	public static MarvinColor	BROWN			= new MarvinColor(17, 	"brown",		150, 75, 0);
	// YELLOW
	public static MarvinColor	DARK_YELLOW		= new MarvinColor(18, 	"dark_yellow",	191, 191, 0);
	public static MarvinColor	YELLOW			= new MarvinColor(19, 	"yellow",	255, 255, 0);
	public static MarvinColor	LIGHT_YELLOW	= new MarvinColor(20, "light_yellow",	255, 255, 155);
	
	private static Map<Integer, MarvinColor> 	mapColorById;
	private static Map<String, MarvinColor>		mapColorByName;
	
	static{
		mapColorById = new HashMap<Integer, MarvinColor>();
		mapColorByName = new HashMap<String, MarvinColor>();
		
		// BLACK AND WHITE
		mapColorById	.put(BLACK.id, 				BLACK);
		mapColorByName	.put(BLACK.name, 			BLACK);
		mapColorById	.put(WHITE.id, 				WHITE);
		mapColorByName	.put(WHITE.name, 			WHITE);
		// GRAY
		mapColorById	.put(DARK_GRAY.id, 			DARK_GRAY);
		mapColorByName	.put(DARK_GRAY.name, 		DARK_GRAY);
		mapColorById	.put(GRAY.id, 				GRAY);
		mapColorByName	.put(GRAY.name, 			GRAY);
		mapColorById	.put(LIGHT_GRAY.id, 		LIGHT_GRAY);
		mapColorByName	.put(LIGHT_GRAY.name, 		LIGHT_GRAY);
		// RED
		mapColorById	.put(DARK_RED.id, 			DARK_RED);
		mapColorByName	.put(DARK_RED.name, 		DARK_RED);
		mapColorById	.put(RED.id, 				RED);
		mapColorByName	.put(RED.name, 				RED);
		mapColorById	.put(LIGHT_RED.id, 			LIGHT_RED);
		mapColorByName	.put(LIGHT_RED.name, 		LIGHT_RED);
		// GREEN
		mapColorById	.put(DARK_GREEN.id, 		DARK_GREEN);
		mapColorByName	.put(DARK_GREEN.name, 		DARK_GREEN);
		mapColorById	.put(GREEN.id, 				GREEN);
		mapColorByName	.put(GREEN.name, 			GREEN);
		mapColorById	.put(LIGHT_GREEN.id, 		LIGHT_GREEN);
		mapColorByName	.put(LIGHT_GREEN.name, 		LIGHT_GREEN);
		// BLUE
		mapColorById	.put(DARK_BLUE.id, 			DARK_BLUE);
		mapColorByName	.put(DARK_BLUE.name, 		DARK_BLUE);
		mapColorById	.put(BLUE.id, 				BLUE);
		mapColorByName	.put(BLUE.name, 			BLUE);
		mapColorById	.put(LIGHT_BLUE.id, 		LIGHT_BLUE);
		mapColorByName	.put(LIGHT_BLUE.name, 		LIGHT_BLUE);
		// ORANGE
		mapColorById	.put(ORANGE.id, 			ORANGE);
		mapColorByName	.put(ORANGE.name, 			ORANGE);
		mapColorById	.put(LIGHT_ORANGE.id, 		LIGHT_ORANGE);
		mapColorByName	.put(LIGHT_ORANGE.name, 	LIGHT_ORANGE);
		// BROWN
		mapColorById	.put(DARK_BROWN.id, 		DARK_BROWN);
		mapColorByName	.put(DARK_BROWN.name, 		DARK_BROWN);
		mapColorById	.put(BROWN.id, 				BROWN);
		mapColorByName	.put(BROWN.name, 			BROWN);
		// YELLOW
		mapColorById	.put(DARK_YELLOW.id, 		DARK_YELLOW);
		mapColorByName	.put(DARK_YELLOW.name, 		DARK_YELLOW);
		mapColorById	.put(YELLOW.id, 			YELLOW);
		mapColorByName	.put(YELLOW.name, 			YELLOW);
		mapColorById	.put(LIGHT_YELLOW.id, 		LIGHT_YELLOW);
		mapColorByName	.put(LIGHT_YELLOW.name, 	LIGHT_YELLOW);
	}
}
