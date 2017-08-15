
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
		resetValues();
		setPoints(Generated);
		setTerrain();
		calculateTileNumbers(); //also assigns terrain_type to each Map_Point
		calculateTopographicFeatures();
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
	
	void setTerrain() {
		setTerrainType('r');
		setTerrainType('x');
		setTerrainType('w');
		setTerrainType('m');
		setTerrainType('g');
		setTerrainType('d');
	}
	
	/****************!!!!!!!!!!!! DO ME****************/
	void calculateTopographicFeatures() {
		
	}
	
	int [] getArrayForValue (char key) {
		
		int [] temp_array = null;
		
		switch (key) {
		case 'a':
			temp_array = new int [num_land_tiles];
			break;
		case 'h':
			temp_array = new int [num_land_tiles];
			break;
		default:
				break;
		}
		
		int temp_index = 0;
		switch (key) {
		case 'a':
			for (int i = 0; i < Local_Map.length; i++) {
				for (int j = 0; j < Local_Map[0].length; j++) {
						if (Local_Map[i][j].hydration < threshold_water) {
							temp_array[temp_index] = Local_Map[i][j].altitude;
							temp_index++;
						}
					}
				}
			break;
		case 'h':
			for (int i = 0; i < Local_Map.length; i++) {
				for (int j = 0; j < Local_Map[0].length; j++) {
						if (Local_Map[i][j].hydration < threshold_water) {
							temp_array[temp_index] = Local_Map[i][j].hydration;
							temp_index++;
						}
					}
				}
			break;
		default:
			break;
		
		}
		return temp_array;
	}
	
	double calculateMean(int [] value) {
		double temp = 0; 
		for (int i = 0; i < value.length; i++) {
			temp += (double)value[i]; 
		}
		return temp/value.length;
	}
	
	/* function taken from https://stackoverflow.com/questions/18390548/how-to-calculate-standard-deviation-using-java */
	double calculateSD(int [] value) {
		
		double powerSum1 = 0;
		double powerSum2 = 0;
		double stdev = 0;
		
		for (int i = 0; i < value.length; i++) {
			powerSum1 += (double) value[i];
			powerSum2 += Math.pow((double)value[i], 2);
			stdev += Math.sqrt((double)(i*powerSum2 - Math.pow(powerSum1, 2)))/i;
		}
		
		return stdev;
	}
	
	void calculateTileNumbers() {
		num_tiles = width * length;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < width; j++){
				switch (Local_Map[i][j].terrain_type) {
				case 'r': num_land_tiles_rock++;
					break;
				case 'g': num_land_tiles_grass++;
					break;
				case 'd': num_land_tiles_dirt++;
					break;
				case 'm': num_land_tiles_marsh++;
					break;
				case 'w': num_water_tiles_shallow++;
					break;
				case 'x': num_water_tiles_deep++;
					break;
				default:
					//flag as problem BAD INPUT
					break;
				}
			}
		}
		num_water_tiles = num_water_tiles_shallow + num_water_tiles_deep;
		num_land_tiles = num_land_tiles_marsh + num_land_tiles_dirt + num_land_tiles_grass + num_land_tiles_rock;
		//Check to make sure num_tiles == (num_land_tiles + num_water_tiles)
	}
	
	//this function assigns a tile code to each map point based on threshold values. 
	void setTerrainType (char key) {
		int temp = 0; 
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < width; j++){
				switch (key) {
				case 'r':
					//calculate rocks from slope
					temp = Local_Map[i][j].slopeZX + Local_Map[i][j].slopeZY; 
					if (passedThreshold(key, temp)) {Local_Map[i][j].setTerrainType(key);}
					temp = 0;
					break;
				case 'g':
					//calculate grass tiles
					if (Local_Map[i][j].terrain_type == 'r') break;
					if (passedThreshold(key, Local_Map[i][j].hydration)) {Local_Map[i][j].setTerrainType(key);}
					break;
				case 'd':
					//calculate dirt tiles
					if (Local_Map[i][j].terrain_type == 'r') break;
					if (passedThreshold(key, Local_Map[i][j].hydration)) {Local_Map[i][j].setTerrainType(key);}
					break;
				case 'm':
					//calculate marsh tiles;
					if (passedThreshold(key, Local_Map[i][j].hydration)) {Local_Map[i][j].setTerrainType(key);}
					break;
				case 'w':
					//calculate shallow water tiles
					if (passedThreshold(key, Local_Map[i][j].hydration)) {Local_Map[i][j].setTerrainType(key);}
					break;
				case 'x':
					//calculate deep water tiles
					if (passedThreshold(key, Local_Map[i][j].hydration)) {Local_Map[i][j].setTerrainType(key);}
					break;
				default:
					//flag as problem BAD INPUT
					break;
				}
			}
		}
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
	
	void resetValues() {
		length = 0;
		width = 0;
		mean_altitude = 0;
		mean_land_hydration = 0;
		stdv_altitude = 0;
		stdv_land_hydration = 0;
		num_tiles = 0;
		num_water_tiles = 0;
		num_water_tiles_shallow = 0;
		num_water_tiles_deep = 0;
		num_land_tiles = 0;
		num_land_tiles_dirt = 0;
		num_land_tiles_grass = 0;
		num_land_tiles_rock = 0;
		num_land_tiles_marsh = 0;
	}
	
}
