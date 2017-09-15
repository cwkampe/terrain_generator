
public class Map_Point {
	int altitude;
	int slopeZX;
	int slopeZY;
	int rainfall;
	int hydration;
	MapReader.SoilType soil;
	MapReader.CompassDirection face;
	
	public enum TerrainType {
		UNKNOWN, WATER_SHALLOW, WATER_NORMAL, WATER_DEEP, LAND_SAND, LAND_DIRT, LAND_GRASS, LAND_MARSH, LAND_ROCK
	};
	
	public enum TileAspect {
		ALTITUDE, SLOPE_ZX, SLOPE_ZY, RAINFALL, HYDRATION, SOIL, FACE
	};
	
	char terrain_type;
	
	Map_Point() {}
	
	Map_Point(int alt, int slpZX, int slpZY, int  rain, int hydr, String soil) {
		altitude = alt;
		slopeZX = slpZX;
		slopeZY = slpZY;
		rainfall = rain;
		hydration = hydr;
		soil_type = soil;
	}
	void setAllValues(Map_Point Value) {
		altitude = Value.altitude;
		slopeZX = Value.slopeZX;
		slopeZY = Value.slopeZY;
		rainfall = Value.rainfall;
		hydration = Value.hydration;
		soil_type = Value.soil_type;
		terrain_type = '!'; // Default error state
	}
	
	void setValue(char key, int value) {
		switch (key) {
			case 'a':
				altitude = value;
				break;
			case 'x':
				slopeZX = value;
				break;
			case 'y':
				slopeZY = value;
				break;
			case 'r':
				rainfall = value;
				break;
			case 'h':
				hydration = value;
				break;
			default:
				break;
		}
	}
	
	
	void setSoil (enum SoilType current) {
		soil_type
	}
	
	void setFace (enum CompassDirection current) {
		
	}
	
	void setTerrainType(char key) {
		terrain_type = key;
	}
}

