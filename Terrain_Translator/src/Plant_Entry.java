
public class Plant_Entry {
	String myVariety;
	int max_population;
	int current_population;
	float desire_score;
	int reproduction_rate;
	static int ERROR_CODE_CANNOT_GROW = -1;
	
	
	void initialize() {
		myVariety = ""; max_population = 0; current_population = 0; desire_score = 0; reproduction_rate = 0;}
	
	boolean is_population_full() {
		if (max_population >= current_population) return true;
		else return false;
	}
	
	int find_best_grow_spot(Area_Tile[] current_map) {
		int best_location = ERROR_CODE_CANNOT_GROW;
		float desire_score = 0;
		float highest_score = 0;
		
		for (int i = 0; i < Area_Tile.map_area; i++) {
			desire_score = current_map[i].calculate_my_desirability(myVariety, current_map);
			if (desire_score > 0 && desire_score > highest_score) {
				highest_score = desire_score;
				best_location = i;
			}
		}
		return best_location;
	}
	
	void setDesire(int d) {desire_score = d;}
	float getDesire() {return desire_score;}
}
