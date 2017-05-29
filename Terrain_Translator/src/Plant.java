import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import com.opencsv.CSVReader;

/*
 * DESIRABILITY needs to become a float. It currently handles fractions which wont work as int unless you do a work around.
 */

public class Plant {
	static int NUMBER_OF_PLANT_ELEMENTS = 15; //this is the number of plant elements expressed in CSV columns
	static int NUMBER_OF_PLANT_VARIETIES;
	static Plant[] Plant_list;
	static int ERROR_CODE_NO_PLANT = 0; // the first plant has to be NO_PLANT
	static int ERROR_CODE = 999;
	static char ERROR_CHAR = '!';

	
	String name;
	char map_code;
	
	boolean is_land_plant;
	boolean is_water_plant;
	
	int ideal_elevation;
	int ideal_hydration;
	
	int tolerance_elevation;
	int tolerance_hydration;
	int tolerance_plants;
	
	int propegation_range;
	int density_penalty;
	
	int life_span;
	
	int sympathy_value; //benefit value
	int antipathy_value; //penalty value
	String [] sympathy_plant_list; //provides benefits to these plants
	String [] antipathy_plant_list; //provides penalties to these plants
	
	static int get_index(String name) {
		for (int i = 0; i < NUMBER_OF_PLANT_VARIETIES; i++) {
			if (name == Plant_list[i].name) {return i;}
		}
		return ERROR_CODE;
	}
	
	static char get_code(String name) {
		for (int i = 0; i < NUMBER_OF_PLANT_VARIETIES; i++) {
			if (name == Plant_list[i].name) {return Plant_list[i].map_code;}
		}
		return ERROR_CHAR;
	}
	
	static Plant get_plant(String plant_variety) {
		for (int i = 0; i < NUMBER_OF_PLANT_VARIETIES; i++) {
			if (plant_variety == Plant_list[i].name) {return Plant_list[i];}
		}
		return Plant_list[Plant.ERROR_CODE_NO_PLANT];
	}
	
	static void Load_Plant_List(String fileName) throws IOException {
		
		CSVReader reader = new CSVReader(new FileReader("filename"));
		List<String[]> myEntries = reader.readAll();
		NUMBER_OF_PLANT_VARIETIES = (myEntries.size() -1);
		Plant_list = new Plant[NUMBER_OF_PLANT_VARIETIES];
		
		for (int i = 0; i < NUMBER_OF_PLANT_VARIETIES; i++) {
			Plant_list[i].sympathy_plant_list = new String [NUMBER_OF_PLANT_VARIETIES];
			Plant_list[i].antipathy_plant_list = new String [NUMBER_OF_PLANT_VARIETIES];
		}
		
		String[] nextLine; 
		int lineNumber = 0;
		 while ((nextLine = reader.readNext()) != null) {
		     if (lineNumber != 0) { //skip the first line because it is column headings 
		    	Plant_list[(lineNumber-1)].name = nextLine[1];
		    	Plant_list[(lineNumber-1)].map_code = nextLine[2].charAt(0);
		    	if (nextLine[3].charAt(0) == 'l') {Plant_list[(lineNumber-1)].is_land_plant = true;}
		    	else if (nextLine[3].charAt(0) == 'w') {Plant_list[(lineNumber-1)].is_water_plant = true;}
		    	Plant_list[(lineNumber-1)].ideal_elevation = Integer.parseInt(nextLine[4]);
		    	Plant_list[(lineNumber-1)].ideal_hydration = Integer.parseInt(nextLine[5]);
		    	Plant_list[(lineNumber-1)].tolerance_elevation = Integer.parseInt(nextLine[6]);
		    	Plant_list[(lineNumber-1)].tolerance_hydration = Integer.parseInt(nextLine[7]);
		    	Plant_list[(lineNumber-1)].tolerance_plants = Integer.parseInt(nextLine[8]);
		    	Plant_list[(lineNumber-1)].propegation_range = Integer.parseInt(nextLine[9]);
		    	Plant_list[(lineNumber-1)].density_penalty = Integer.parseInt(nextLine[10]);
		    	Plant_list[(lineNumber-1)].life_span = Integer.parseInt(nextLine[11]);
		    	Plant_list[(lineNumber-1)].sympathy_value = Integer.parseInt(nextLine[12]);
		    	Plant_list[(lineNumber-1)].antipathy_value = Integer.parseInt(nextLine[13]);
		    	Plant_list[(lineNumber-1)].sympathy_plant_list = nextLine[14].split("/");
		    	Plant_list[(lineNumber-1)].antipathy_plant_list = nextLine[15].split("/");
		     }
			 lineNumber++;
	     }
	    reader.close();
     }
	
	Plant() { initialize();}
	
	void initialize() {
		name = "";
		map_code = 0;
		is_land_plant = false;
		is_water_plant = false;
		ideal_elevation = 0;
		ideal_hydration = 0;
		propegation_range =0;
		density_penalty =0;
		life_span = 0;
	}
	
	//used to calculate general desirability (relative to the current plant) for a map region (not tile based). 
	float calculate_area_desirability(int area_elevation, int area_hydration) {
		float desirability = 1;
		float x;
		
		x = Math.abs(ideal_elevation - area_elevation);
		desirability -= x/tolerance_elevation;
		x = Math.abs(ideal_hydration - area_hydration);
		desirability -= x/tolerance_hydration;
		
		return desirability; 
	}
	
	
	///NEEEDS FLOAT FIX!!!! I THINK THIS IS REALLY BROKEN RIGHT NOW, IT USED TO CHECK AGAINST THE PLANT TEMPLATES
	//takes area tile, calculates that tiles desirability relative to the current plant.
	float calculate_tile_desirability(Area_Tile place, String plant_variety) {
		float desirability = 1;
		float x = 0;
		
		if (is_land_plant != place.supports_land_vegetation && is_water_plant != place.supports_water_vegetation) { 
			return 0; //total undesirability;
		}
		
		x = Math.abs(ideal_hydration - place.hydration_value);
		desirability -= x/tolerance_hydration;
		x = place.dis.report_disposition_towards(plant_variety);
		desirability += x/tolerance_plants;
		
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
	
