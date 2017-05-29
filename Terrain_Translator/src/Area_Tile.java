
public class Area_Tile {
	static int map_width;
	static int map_length;
	static int map_area;
	static int adjacent_squares = 8;
	
	static void initialize(int width, int length) {
		map_width = width;
		map_length = length;
		map_area = length * width;
	}
	
	String my_ground_type;
	String my_current_plant;
	int myLocation;
	
	char ground_code;
	char plant_code;
	Disposition dis;
	
	int hydration_value;
	boolean has_plant;
	boolean supports_land_vegetation;
	boolean supports_water_vegetation;
	
	int [] identify_adjacent_tiles () {
		int[] temp = new int [Area_Tile.adjacent_squares];
		
		temp[0]=myLocation - map_width -1;
		temp[1]=myLocation - map_width;
		temp[2]=myLocation - map_width +1;
		temp[3]=myLocation -1;
		temp[4]=myLocation +1;
		temp[5]=myLocation + map_width -1;
		temp[6]=myLocation + map_width;
		temp[7]=myLocation + map_width +1;
	
		return temp;
	}
	
	float calculate_my_desirability(String plant_variety, Area_Tile[] current_map) {
		float desirability = 1;
		int [] neighboring_tiles = identify_adjacent_tiles();
		int [] neighbors_disposition = new int [Area_Tile.adjacent_squares];
		
		//Get tile's intrinsic dispositions towards a given plant (i.e. its own)
		float x = 0;
		x = Math.abs((Plant.get_plant(plant_variety).ideal_hydration) - hydration_value);
		desirability -= x/(Plant.get_plant(plant_variety).tolerance_hydration);
		x = dis.report_disposition_towards(plant_variety);
		desirability += x/(Plant.get_plant(plant_variety).tolerance_plants);
		
		//Get tile's extrinsic dispositions towards a given plant (i.e. it/neighbors)
		for (int i = 0; i < neighboring_tiles.length; i++) {
			//check for neighbors that are out of bounds
			if (neighboring_tiles[i] < 0 || neighboring_tiles[i] >= map_area){
				neighbors_disposition[i] = 0;
			} else {
				neighbors_disposition[i] = current_map[neighboring_tiles[i]].dis.getValue(my_current_plant);
				}
			}
		
		x = 0;
		for (int i = 0; i < neighbors_disposition.length; i++) {
			x += neighbors_disposition[i];
		}
		
		//Combine intrinsic and extrinisic desire effects
		return (desirability + (x/adjacent_squares));
	}
	
	boolean can_this_plant_grow_here(String this_plant, Area_Tile[] current_map) {
		//check if there's already one here
		if (my_current_plant == this_plant) {return false;}
		//make sure its the right kind of tile
		if (supports_land_vegetation != Plant.get_plant(this_plant).is_land_plant &&
				supports_water_vegetation != Plant.get_plant(this_plant).is_water_plant) {return false;}
		//make sure there's at least one neighboring tile with this type of plant in it
		int[] temp = identify_adjacent_tiles();
		for (int i = 0; i < adjacent_squares; i++) {
			if (current_map[temp[i]].my_current_plant == this_plant) {return true;}
		}
		return false;
	}
	
	void set_plant(String plant_variety) {
		my_current_plant = plant_variety;
		plant_code = Plant.get_code(plant_variety);
		String[] temp_sympathy = Plant.Plant_list[Plant.get_index(plant_variety)].sympathy_plant_list;
		String[] temp_antipathy = Plant.Plant_list[Plant.get_index(plant_variety)].antipathy_plant_list;
		
		for (String t : temp_sympathy) {
			dis.adjust_disposition(t, Plant.Plant_list[Plant.get_index(plant_variety)].sympathy_value);
		}
		for (String t : temp_antipathy) {
			dis.adjust_disposition(t, Plant.Plant_list[Plant.get_index(plant_variety)].antipathy_value);
		}	
	}
	
}
