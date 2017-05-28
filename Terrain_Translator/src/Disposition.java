
public class Disposition {
	String [] plant;
	int [] value;
	
	
	//Constructors
	Disposition() {;}
	Disposition(int NUMBER_OF_PLANT_VARIETIES, Plant [] all_plants) {
		initialize(NUMBER_OF_PLANT_VARIETIES, all_plants);
	}
	
	//Functions
	int getValue(String plant_name) {
		for (int i = 0; i < plant.length; i++) {
			if (plant[i] == plant_name) {return value[i];}
		}
		return 0;
	}
	
	void setValue(String p, int v) {
		for (int i = 0; i < plant.length; i++) {
			if (plant[i] == p) {
				
				//if positive, take the higher value
				if (value[i] > 0 && v > 0 ) {
					if (v >= value[i]) {value[i] = v;}
				}
				
				//if negative, take the lower value
				if (value[i] < 0 && v < 0 ) {
					if (v <= value[i]) {value[i] = v;}
				}
				
				//if pos & neg, add the values together and keep the result
				if (value[i] < 0 && v > 0 || value[i] > 0 && v < 0) {
					value[i] += v;
				}
			}
		}
	}
	
	void initialize(int NUMBER_OF_PLANT_VARIETIES, Plant [] all_plants) {
		
		plant = new String [NUMBER_OF_PLANT_VARIETIES];
		
		for (int i = 0; i < NUMBER_OF_PLANT_VARIETIES; i++) {
			plant [i] = all_plants[i].name;
			value [i] = 0;
		}
	}
}
