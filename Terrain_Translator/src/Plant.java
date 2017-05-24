

public class Plant {
	String name;
	int map_code;
	boolean is_land_plant;
	boolean is_water_plant;
	
	int ideal_elevation;
	int ideal_hydration;
	int tolerance;
	
	int propegation_range;
	int density_penalty;
	
	int life_span;
	int current_age;
	
	int sympathy_value; //benefit value
	int antipathy_value; //penalty value
	String [] sympathy_plant_list; //provides benefits to these plants
	String [] antipathy_plant_list; //provides penalties to these plants
	
	Plant() { initialize();}
	
	void initialize() {
		name = "";
		map_code = 0;
		is_land_plant = false;
		is_water_plant = false;
		ideal_elevation = 0;
		ideal_hydration = 0;
		tolerance = 0;
		propegation_range =0;
		density_penalty =0;
		life_span = 0;
		current_age = 0;
	}
	
	//used to calculate general desirability (relative to the current plant) for a map region (not tile based). 
	int calculate_area_desirability(int area_elevation, int area_hydration) {
		int desirability = 1;
		int x;
		
		x = Math.abs(ideal_elevation - area_elevation);
		desirability -= x/tolerance;
		x = Math.abs(ideal_hydration - area_hydration);
		desirability -= x/tolerance;
		
		return desirability; 
	}
	
	//takes area tile, calculates that tiles desirability relative to the current plant.
	int calculate_tile_desirability(Area_Tile place, Plant [] plant_index) {
		int desirability = 1;
		int x = 0;
		int temp_index = 0;
		
		if (is_land_plant != place.supports_land_vegetation && is_water_plant != place.supports_water_vegetation) { 
			return 0; //total undesirability;
		}
		
		for (int i = 0; i < plant_index.length; i++) {
			if (plant_index[i].name == place.current_plant_name) {temp_index = i;}
		}
		
		x = Math.abs(ideal_hydration - place.hydration_value);
		desirability -= x/tolerance;
		x = plant_index[temp_index].determine_relative_sympathy(name);
		desirability += x/tolerance;
		
		return desirability; 
	}
	
	int determine_relative_sympathy (String prospective) {
		int sympathy = 0;		
		if (has_sympathy(prospective)) {sympathy += sympathy_value;}
		if (has_antipathy(prospective)) {sympathy -= antipathy_value;}
		return sympathy;
	}
	
	boolean has_sympathy(String plant_name) {
		for (int i = 0; i < sympathy_plant_list.length; i++) {
			if (plant_name == sympathy_plant_list[i]) {return true;}
		}
		return false; 
	}
	boolean has_antipathy(String plant_name) {
		for (int i = 0; i < antipathy_plant_list.length; i++) {
			if (plant_name == antipathy_plant_list[i]) {return true;}
		}
		return false; 
	}
}
	
