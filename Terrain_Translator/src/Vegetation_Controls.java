import java.util.Random;

public class Vegetation_Controls {
	
	//Test Values
	int test_slope = 0;
	int test_hydration;
	int test_elevation;
	int test_vegetation_density = 100;
	
	int vegetation_density = test_vegetation_density;
	int number_of_plants;
	int number_of_darts = 0;
	int min_desirability = 0;
	
	int NUMBER_OF_PLANT_VARIETIES = 2; // this is probably not a constant?
	int MIN_DARTS = 3; //this may shift
	int dart_multiplier = 1; // tweakable value for the dart system
	
	
	char[] plant_area_tile_map;
	
	Plant[] plant_templates = new Plant [NUMBER_OF_PLANT_VARIETIES];
	Plant_Entry[] area_populations = new Plant_Entry [NUMBER_OF_PLANT_VARIETIES];
	Plant_Instance[][] plants_in_area;
	
	Vegetation_Controls() { initialize();}
	
	
	char[] create_plant_tile_map (int map_width, int map_length, Area_Tile[] terrain_map) {
		int map_area = map_width * map_length; 
		double temp = (double)((map_area * vegetation_density)/100);
		
		number_of_plants = (int) temp;
		number_of_darts = (int) Math.sqrt(temp);   ; //Darts should be a relative function of map size?
		if (number_of_darts < MIN_DARTS) {number_of_darts = MIN_DARTS;}
		
		plants_in_area = new Plant_Instance [NUMBER_OF_PLANT_VARIETIES][number_of_plants];
		
		
		char[] plant_tile_map = new char [map_area];
		int [] plant_population_limit = new int [NUMBER_OF_PLANT_VARIETIES];
		int current_plant_quantity = 0;
		
		int [] dart_pool = new int [number_of_plants];
		dart_pool = flush(dart_pool);
		
		//Get a list of limits
		for (int i = 0; i < NUMBER_OF_PLANT_VARIETIES; i++) {
			plant_population_limit[i] = vegetation_density * area_populations[i].getPopulation();
		}
		
		for (int i = 0; i < NUMBER_OF_PLANT_VARIETIES; i++) {
			temp = Math.sqrt((double) plant_population_limit[i]);
			number_of_darts = ((int)temp)*dart_multiplier;
			if (number_of_darts < MIN_DARTS) {number_of_darts = MIN_DARTS;}
			
			//throw a number of darts proportinate to a given plants population size
			for (int j = 0; j < number_of_darts; j++) {
				plants_in_area[i][j].
= throw_a_dart (terrain_map, plant_templates[i]);
				
			
			}
						 
		}		
		/* 1) Throw darts until you have X good ones
		 * 2) Scan the map, rank it in terms of favorable locations. 
		 * 3) Grow into a number of those locations based on population_limit
		 * 4) Repeat until all numbers are satisfied. 
		 * !!! if the throw_a_dart returns terrain_map.length, it's an error and the system needs to stop.
		 */



		
		return plant_tile_map;
	}
	
	//get a random value, ensure the tile is legal & desirable for the plant
	int throw_a_dart (Area_Tile[] terrain_map, Plant current_plant) {
		int index = 0;
		Random generator = new Random();
		int cycle_tracker = 0;
		int escape_limit = (terrain_map.length * 10);
		
		do {
			index = generator.nextInt(terrain_map.length);
			if (check_applicability(terrain_map[index], current_plant) == false) {index = 0;}
			cycle_tracker++;
			if (cycle_tracker >= escape_limit) {return terrain_map.length; }
		} while (index == 0);
		
		return index;

	}
	
	boolean check_applicability(Area_Tile tile, Plant plant) {
		if (tile.supports_land_vegetation != plant.is_land_plant) {return false;}
		if (tile.supports_water_vegetation != plant.is_water_plant) {return false;}
		if (tile.has_plant == true) {return false;}
		if (plant.calculate_tile_desirability(tile, plant_templates) >= min_desirability) {return false;}
		
		return true;
	}
	
	//Maybe INCOMPLETE
	void setPlantMap(int length, int width) {
		int mapArea = length * width;
		plant_area_tile_map = new char [mapArea];
	}
	
	void initialize() {
		
		int temp_sum = 0;
		
		for (int i = 0; i < NUMBER_OF_PLANT_VARIETIES; i++) {
			area_populations[i].initialize();
			plant_templates[i].initialize();
			//load plant templates in from excel file
			area_populations[i].setName(plant_templates[i].name);
			area_populations[i].setDesire(plant_templates[i].calculate_area_desirability(test_elevation, test_hydration));
			temp_sum += area_populations[i].getDesire();
		}
		
		for (int i = 0; i < NUMBER_OF_PLANT_VARIETIES; i++) {
			area_populations[i].setPopulation(area_populations[i].getDesire()/temp_sum);
		}
		
	}
	
	int [] flush(int [] array) {
		for (int i = 0; i < array.length; i++ ) {
			array[i] = 0;
		}
		return array;
	}
	
}