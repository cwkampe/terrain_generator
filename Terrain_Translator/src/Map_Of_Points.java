
public class Map_Of_Points {
	
	static int threshold_slope_rock = 100;
	static int threshold_ground_green = 30;
	static int threshold_ground_dirt = 0;
	static int threshold_marsh = 100;
	static int threshold_water = 100;
	static int threshold_deep_water = 100;
	
	Map_Point [][] Local_Map;
	
	int length;
	int width;
	int mean_altitude;
	int mean_land_hydration;
	int stdv_altitude;
	int stdv_land_hydration;
	int num_tiles;
	int num_water_tiles;
	int num_water_tiles_shallow;
	int num_water_tiles_deep;
	int num_land_tiles;
	int num_land_tiles_dirt;
	int num_land_tiles_grass;
	int num_land_tiles_rock;
	int num_land_tiles_marsh;
	
	Map_Of_Points() {}
	Map_Of_Points(Map_Point[][] Generated) {
		initialize(Generated);
	}
	
	void initialize(Map_Point[][] Generated) {
		setPoints(Generated);
		calculateTopographicFeatures();
		calculateTileNumbers(); //also assigns terrain_type to each Map_Point
	}
	
	void setPoints(Map_Point[][] Generated) {
		
		width = Generated[0].length;
		length = Generated.length;
		
		Local_Map = new Map_Point[length][width];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < width; j++){
				Local_Map[i][j].setAllValues(Generated[i][j]);
			}
		}
	}
	void calculateTopographicFeatures() {
		
	}
	
	void calculateTileNumbers() {
		num_tiles = width * length;
		num_water_tiles_shallow = calculateNumberByThreshold('w');
		num_water_tiles_deep = calculateNumberByThreshold('x');
		num_water_tiles = num_water_tiles_shallow + num_water_tiles_deep;
		num_land_tiles_marsh = calculateNumberByThreshold('m');
		num_land_tiles_dirt = calculateNumberByThreshold('d');
		num_land_tiles_grass = calculateNumberByThreshold('g');
		num_land_tiles_rock = calculateNumberByThreshold('r');
		num_land_tiles = num_land_tiles_marsh + num_land_tiles_dirt + num_land_tiles_grass + num_land_tiles_rock;
		//Check to make sure num_tiles == (num_land_tiles + num_water_tiles)
	}
	
	//this function returns a number and assigns a tile code to each map point included in the tally. 
	int calculateNumberByThreshold (char key) {
		int tally = 0;
		int temp = 0; 
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < width; j++){
				switch (key) {
				case 'r':
					//calculate rocks from slope
					temp = Local_Map[i][j].slopeZX + Local_Map[i][j].slopeZY; 
					if (passedThreshold(key, temp)) {tally++; Local_Map[i][j].setTerrainType(key);}
					temp = 0;
					break;
				case 'g':
					//calculate grass tiles
					if (passedThreshold(key, Local_Map[i][j].hydration)) {tally++; Local_Map[i][j].setTerrainType(key);}
					break;
				case 'd':
					//calculate dirt tiles
					if (passedThreshold(key, Local_Map[i][j].hydration)) {tally++; Local_Map[i][j].setTerrainType(key);}
					break;
				case 'm':
					//calculate marsh tiles;
					if (passedThreshold(key, Local_Map[i][j].hydration)) {tally++; Local_Map[i][j].setTerrainType(key);}
					break;
				case 'w':
					//calculate shallow water tiles
					if (passedThreshold(key, Local_Map[i][j].hydration)) {tally++; Local_Map[i][j].setTerrainType(key);}
					break;
				case 'x':
					//calculate deep water tiles
					if (passedThreshold(key, Local_Map[i][j].hydration)) {tally++; Local_Map[i][j].setTerrainType(key);}
					break;
				default:
					//flag as problem BAD INPUT
					break;
				}
			}
		}
		return tally;
	}
		
	boolean passedThreshold(char key, int value) {
		switch (key) {
		case 'r':
			//calculate rocks from slope
			if (value >= threshold_slope_rock) return true;
			break;
		case 'g':
			//calculate grass tiles
			if (value >= threshold_ground_green && value < threshold_marsh) return true; 
			break;
		case 'd':
			//calculate dirt tiles
			if (value < threshold_ground_green) return true;
			break;
		case 'm':
			//calculate marsh tiles
			if (value >= threshold_marsh && value < threshold_water) return true;
			break;
		case 'w':
			//calculate shallow water tiles
			if (value >= threshold_water && value < threshold_deep_water) return true;
			break;
		case 'x':
			//calculate deep water tiles
			if (value >= threshold_deep_water)
			break;
		default:
			//Flag as problem BAD INPUT
			return false;
		}
		return false;
	}
	
}
