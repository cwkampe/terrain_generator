
public class Plant_Instance {
	String myVariety;
	int myLocation;
	
	int desire_to_hold;
	int age;
	int [] neighboring_tiles = new int [8];
	int [] desirability_of_neighbors = new int [8];
	
	void calculate_desirability_of_neighbors(Area_Tile[] current_map, Plant current_plant) {
		for (int i = 0; i < neighboring_tiles.length; i++) {
			
			//check for neighbors that are out of bounds
			if (neighboring_tiles[i] < 0 || neighboring_tiles[i] >= current_map.length){
				desirability_of_neighbors[i] = 0;
			} else {
			desirability_of_neighbors[i] = current_map[neighboring_tiles[i]].dis.getValue(myVariety);
			}
		}
	}
	
	void initialize(String name, int location, Area_Tile[] current_map, int map_width) {
		age = 1;
		myVariety = name;
		myLocation = location;
		neighboring_tiles = current_map[location].
		//desire_to_hold = plant.
	}
}
