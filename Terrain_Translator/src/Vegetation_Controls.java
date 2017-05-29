import java.io.IOException;
import java.util.Random;

public class Vegetation_Controls {
	
	//Test Values
	int test_slope = 0;
	int test_hydration;
	int test_elevation;
	int test_vegetation_density = 50;
	
	int absolute_plant_population;
	int vegetation_density = test_vegetation_density;
	int number_of_plants;
	int number_of_darts = 0;
	int min_desirability = 0;
	
	static int NUMBER_OF_PLANT_VARIETIES; // value populated in initialize
	static int ERROR_CODE_DART = -1;
	
	char[] plant_area_tile_map;
	
	Plant_Entry[] area_populations = new Plant_Entry [NUMBER_OF_PLANT_VARIETIES];
	Plant_Instance[][] plants_in_area;
	
	Vegetation_Controls() { 
		}
	
	void initialize(int map_width, int map_length) throws IOException {
		
		int temp_sum = 0;
		
		Plant.Load_Plant_List("plants.csv");
		NUMBER_OF_PLANT_VARIETIES = Plant.NUMBER_OF_PLANT_VARIETIES;
		absolute_plant_population = (map_length * map_width * vegetation_density)/100;
		
		for (int i = 0; i < NUMBER_OF_PLANT_VARIETIES; i++) {
			area_populations[i].initialize();
			//skip none or dead plants
			
			area_populations[i].myVariety = Plant.Plant_list[i].name;
			area_populations[i].desire_score = Plant.Plant_list[i].calculate_area_desirability(test_elevation, test_hydration);
			temp_sum += area_populations[i].getDesire();
		}
		
		float x = 0;
		for (int i = 0; i < NUMBER_OF_PLANT_VARIETIES; i++) {
			x = (area_populations[i].desire_score * absolute_plant_population)/temp_sum;
			area_populations[i].max_population = (int) x; 
			area_populations[i].reproduction_rate = (int)Math.sqrt((double)area_populations[i].max_population);
			x = 0;
		}
	}
	
	void generate_area_vegetation(Area_Tile[] terrain_map) {
	
		int temp_dart = 0;
		
		for (int i = 0; i < area_populations.length; i++) {
			for (int j = 0; j < area_populations[i].reproduction_rate; j++) {
				//if population is full, break
				if (area_populations[i].is_population_full()) break;
				
				//if below minimum, just throw darts 
				if (area_populations[i].current_population < area_populations[i].reproduction_rate) { 
					temp_dart = throw_a_dart(terrain_map, area_populations[i].myVariety);	
				} else { //try to grow
					temp_dart = area_populations[i].find_best_grow_spot(terrain_map);
					//If cannot grow, throw darts
					if (temp_dart == ERROR_CODE_DART) {temp_dart = throw_a_dart(terrain_map, area_populations[i].myVariety);}
				}
				if (temp_dart == ERROR_CODE_DART) break; //this means that even the darts can't find a spot
				terrain_map[temp_dart].set_plant(area_populations[i].myVariety);
				area_populations[i].current_population++;
			}
		}
		//recursion until full or until error is thrown
		for (int i = 0; i <area_populations.length; i++) {
			if (!area_populations[i].is_population_full()) {
				if (temp_dart != ERROR_CODE_DART) {generate_area_vegetation(terrain_map);}
			}
		}
	}
	
	
	//incomplete
	char[] create_plant_tile_map (Area_Tile[] terrain_map) {
		char[] plant_tile_map = new char [Area_Tile.map_area];
		
		return plant_tile_map;
	}
	
	
	//get a random value, ensure the tile is legal & desirable for the plant
	int throw_a_dart (Area_Tile[] terrain_map, String current_plant) {
		int index = 0;
		Random generator = new Random();
		int cycle_tracker = 0;
		int escape_limit = (terrain_map.length * 5);
		
		do {
			index = generator.nextInt(terrain_map.length);
			if (check_applicability(terrain_map[index], current_plant) == false) {index = 0;}
			cycle_tracker++;
			if (cycle_tracker >= escape_limit) {return ERROR_CODE_DART; }
		} while (index == 0);
		
		return index;

	}
	
	boolean check_applicability(Area_Tile tile, String plant_variety) {
		if (tile.supports_land_vegetation != Plant.Plant_list[Plant.get_index(plant_variety)].is_land_plant) {return false;}
		if (tile.supports_water_vegetation != Plant.Plant_list[Plant.get_index(plant_variety)].is_water_plant) {return false;}
		if (tile.has_plant == true) {return false;}
		if (Plant.Plant_list[Plant.get_index(plant_variety)].calculate_tile_desirability(tile, plant_variety) >= min_desirability) {return false;}
		
		return true;
	}
	
	int [] flush(int [] array) {
		for (int i = 0; i < array.length; i++ ) {
			array[i] = 0;
		}
		return array;
	}
	
}
