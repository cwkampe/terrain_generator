
import java.io.PrintWriter;
import java.io.IOException;

public class RPG_Maker_Map {
	
	//EDITABLE VARIABLES
	int map_tileset_ID = 1;
	String MapName = "Map010";
	int map_width = 10;
	int map_height = 10;
	
	//Temporary value holders
	int[] map_values; //CORE
	public static RPG_Maker_Entry[] entries; //CORE
	int temp_set_length = 0; //used in order to hold active set values after lookup; passed to build_set() 
	int temp_set_width = 0; //used in order to hold active set values after lookup; passed to build_set() 
	int current_level = 0; //this value is used in place_set_tile; the value is manually changed prior to the function being called. 
	
	//CONSTANTS
	int MAX_ENTRIES = 22;
	int RPG_MAKER_DATA_ARRAY_NUM = 6;
	int SET_ROW_LENGTH = 8;
	int map_area = map_width * map_height;
	String MapExtension =".json"; //probably never touch this
 
	//constructor
	RPG_Maker_Map() {}
	
	/*******************MAIN FUNCTION*********************
	 * BLOATED AS HELL.
	 */
	void initialize() {
		//creates entries for RPG Maker MapFile.json
		entries = new RPG_Maker_Entry[MAX_ENTRIES];
		for (int i = 0; i < MAX_ENTRIES; i++) {
			entries[i] = new RPG_Maker_Entry();
			entries[i].initialize();
		}
		
		//apply default values to each entry
		set_default_values();
		String fileName = MapName + MapExtension;
		
		//start writing file
		try{
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			writer.print("{\n");
			
			//format each entry
			for (int i = 0; i < MAX_ENTRIES; i++) {
				writer.print('\"');
				writer.print(entries[i].get_name());
				writer.print("\":");
				if (entries[i].get_type() == 'b') {writer.print(entries[i].get_bValue());}
				else if (entries[i].get_type() == 'i') {writer.print(entries[i].get_iValue());}
				else if (entries[i].get_type() == 's') {writer.print(entries[i].get_sValue());}
				writer.print(",");
			}
			
			//get map values (give 0 for ground) 
			char[] terrain_map = make_test_map(0);//load input values
			map_values = make_int_map(map_width, map_height);
			
			//add ground to map_values (LEVEL 0)
			for (int i = 0; i < map_area; i++) {
				//Convert char to integer value
				map_values[i] = get_auto_tile_value(convert_letter_to_code(terrain_map[i]), i, terrain_map); 
			}
			
			//get map values (give 1 of trees) (LEVEL 1)
			terrain_map = flush_map(terrain_map);
			terrain_map = make_test_map(1);
			//add level level 1 above ground to map_values
			for (int i = 0; i < map_area; i++){
				map_values[i+map_area] = get_auto_tile_value(convert_letter_to_code(terrain_map[i]), i, terrain_map);
			}
			
			//get map values (2: of structures) (LEVEL 2)
			current_level = 2;
			terrain_map = flush_map(terrain_map);
			terrain_map = make_test_map(2);
			for (int i = 0; i < map_area; i++){
				if (map_values[i+map_area*2] == 0) {
					if (convert_letter_to_code(terrain_map[i]) != 0) {
					place_set_value(convert_letter_to_code(terrain_map[i]), i+map_area*2);
					}
				}
			}
			
			//get map values (3: of structures) (LEVEL 3)
			current_level = 3; 
			terrain_map = flush_map(terrain_map);
			terrain_map = make_test_map(3);
			for (int i = 0; i < map_area; i++){
				if (map_values[i+map_area*3] == 0) {
					if (convert_letter_to_code(terrain_map[i]) != 0) {
					place_set_value(convert_letter_to_code(terrain_map[i]), i+map_area*3);
					}
				}
			}
			
			/* LEVEL 4 is shadow; each square is divided into 4 sections
			 * UL = 1, UR = 2, BL = 4, BR = 8.  
			 * Compositions are additive in nature. For instance Top Side = 3 (1+2); Bottom Side = 12 (4+8); Full = 15. 
			 */
			
			/* LEVEL 5 is encounter (values can be 1-255); */
			
			
			//Write data section with map_values
			writer.print("\n\"data\":[");
			for (int i = 0; i < map_values.length; i++) {
				writer.print(map_values[i]);
				if (i+1 != map_values.length) {writer.print(',');}
			} //skip the comma for the last entry
			writer.print("],\n\"events\":[\n]\n}");
			writer.close();
		} catch (IOException e) {
			//we got a problem; hasn't come up
		}
	} //if this ran correctly, a map.json file should be created
	/*************END OF MAIN FUNCTION**********************/
	
	/*************CLASS FUNCTIONS**************************/
	
	/*THIS FELLOW ASSIGNS CODE NUMBERS TO LETTER VALUES*/
	int convert_letter_to_code(char x){
		if (x == 'w') {return 2048;}
		if (x == 'g') {return 2816;}
		if (x == 't') {return 3008;}
		if (x == '!') {temp_set_width = 1; temp_set_length = 2; return 112;}
		if (x == 'c') {temp_set_width = 2; temp_set_length = 2; return 128;}
		if (x == 'f') {temp_set_width = 1; temp_set_length = 1; return 303;}
		else return 0; 
	}
	
	/* THIS FELLOW MAKES THE TEST MAPS*/
	
	char[] make_test_map (int c) {
		
		char[] testMap = new char[map_area];
		switch (c) {
		case 3:
			for (int i = 0; i < map_area; i++) {
				testMap[i] = '0';
				if (i%19 == 0 && i != 0) {testMap[i] = 'f';}
				}
			break;		
		case 2:
			for (int i = 0; i < map_area; i++) {
				testMap[i] = '0';
				if (i%(map_width -1) == 0) {testMap[i] = '!';}
				if (i%(map_height*map_width/2) == 0) {testMap[i] = 'c';}
				}
			break;
		case 1: 
			for (int i = 0; i < map_area; i++) {
				
				testMap[i] = '0';
				if (i%4 != 0) {testMap[i] = 't';}
			}
			break;
		default: 
			for (int i = 0; i < map_area; i++) {
				testMap[i] = 'g';
				if (i%6 == 0) {testMap[i] = 'w';}
			}
			break;
		}
		return testMap;
	}
	
	/* a set behaves in a really predictable manner, to include a horizontal member x+1, vertical member y+1 
	 * This function creates a full set of tiles (as determine by set) in  a given map location.
	 * This function employs build_set()
	 * NOTE this offsets current_level by +1 */
	void place_set_value( int base, int index){
		
		int[][] temp_set = build_set( temp_set_width, temp_set_length, base);
		int temp_index = 0;
		
		for (int i = 0; i < temp_set_width; i++) {
			for (int j = 0; j < temp_set_length; j++) {
				temp_index = index + i + (j*map_width);
				
				if (temp_index/(current_level+1) < map_width*map_height) {
					map_values[temp_index] = temp_set[i][j];
				}
			}
		}
	}
	
	//used by place_set_values; establishes the relative offset values associated with a set of tiles
	int [][] build_set (int set_width, int set_length, int set_base) {
		int [][] set = new int [set_width][set_length];
		for (int i = 0; i < set_width; i++) {
			for (int j = 0; j < set_length; j++) {
				set[i][j] = set_base + i + (j * SET_ROW_LENGTH);
			}
		}
		return set;
	}
	
	//used to make map_values (i.e. the map data)
	int[] make_int_map(int width, int height){
		int area = height * width * RPG_MAKER_DATA_ARRAY_NUM;
		int[] map = new int [area];
		for (int i = 0; i < map_area; i++) {
			map[i] = 0;
		}
		return map;
	}
	
	//clears any char[] handed to it; called before terrain_map is redrawn 
	char[] flush_map(char[] map) {
		char[] temp_map = new char[map.length];
		for (int i = 0; i < map.length; i++ ) {
			temp_map[i] = '0';
		}
		return temp_map; 
	}
	
	boolean check_bounds(int v) {
		if (v >= 0 && v < map_height*map_width) return true;
		else return false;
	}
	
	//this gets the autotile value ; it may have a wrap effect in place?
	//THIS WILL NOT WORK WITH Set_tiles
	int get_auto_tile_value (int base, int index, char[] terrain) {
		
		if (terrain[index] == '0') {return 0;} //this prevents offset from being added to a blank tile
		
		int adjacent_squares = 8;
		int temp_offset = 0;
		int[] temp = new int [adjacent_squares];
		
		temp[0]=index - map_width -1;
		temp[1]=index - map_width;
		temp[2]=index - map_width +1;
		temp[3]=index -1;
		temp[4]=index +1;
		temp[5]=index + map_width -1;
		temp[6]=index + map_width;
		temp[7]=index + map_width +1;		
		
		//check top side and top left and right corners
		if (check_bounds(temp[1])) { if (terrain[temp[1]] == terrain[index]) {
			if (check_bounds(temp[3]) && check_bounds(temp[0])) { if (terrain[temp[3]] == terrain[index] && terrain[temp[0]] != terrain[index]) {temp_offset += 1;}}
			if (check_bounds(temp[5]) && check_bounds(temp[2])) { if (terrain[temp[5]] == terrain[index] && terrain[temp[2]] != terrain[index]) {temp_offset += 2;}}
		} else temp_offset += 32; } else temp_offset += 0;
		//check left side and bottom left
		if (check_bounds(temp[3])) { if (terrain[temp[3]] == terrain[index]) {
			if (check_bounds(temp[5]) && check_bounds(temp[6])) { if (terrain[temp[6]] == terrain[index] && terrain[temp[5]] != terrain[index]) {temp_offset += 8;}}
		} else temp_offset += 16; } else temp_offset += 0;
		//check right side and bottom right corner
		if (check_bounds(temp[4])) { if (terrain[temp[4]] == terrain[index]){
			if (check_bounds(temp[6]) && check_bounds(temp[7])) { if (terrain[temp[6]] == terrain[index] && terrain[temp[7]] != terrain[index]) {temp_offset += 4;}}
		} else temp_offset += 64; } else temp_offset += 0;
		//check bottom center
		if (check_bounds(temp[6])) { if (terrain[temp[6]] == terrain[index]){ /*do nothing*/ } else temp_offset += 128; } else temp_offset += 0;
		
		//determine actual offset, add to base value and return. 
		return (base + convert_offset(temp_offset));
		
	}

	//Converts the byte value into correct offset for autotile
	int convert_offset(int v) {
		if (v >= 0 && v <= 16) {return v;}
		else if ( v == 18) {return 17;}
		else if ( v == 20) {return 18;}
		else if ( v == 22) {return 19;}
		else if ( v == 32) {return 20;}
		else if ( v == 36) {return 21;}
		else if ( v == 40) {return 22;}
		else if ( v == 44) {return 23;}
		else if ( v == 64) {return 24;}
		else if ( v == 72) {return 25;}
		else if ( v == 65) {return 26;}
		else if ( v == 73) {return 27;}
		else if ( v == 128) {return 28;}
		else if ( v == 129) {return 29;}
		else if ( v == 130) {return 30;}
		else if ( v == 131) {return 31;}
		else if ( v == 80) {return 32;}
		else if ( v == 160) {return 33;}
		else if ( v == 48) {return 34;}
		else if ( v == 52) {return 35;}
		else if ( v == 96) {return 36;}
		else if ( v == 104) {return 37;}
		else if ( v == 192) {return 38;}
		else if ( v == 193) {return 39;}
		else if ( v == 144) {return 40;}
		else if ( v == 146) {return 41;} //slight defect visible at bottom most row
		else if ( v == 112) {return 42;}
		else if ( v == 176) {return 43;}
		else if ( v == 208) {return 44;}
		else if ( v == 224) {return 45;}
		else if ( v == 240) {return 46;}
		else {return 0;}
	}
	
	//takes a new value, looks up that values name, then replace the values in entries
	void change_setting_value (RPG_Maker_Entry NewValue) {
		for (int i = 0; i < MAX_ENTRIES; i++ ) {
			if(NewValue.get_name() == entries[i].get_name()) {
				entries[i].take_on_value(NewValue);
				break; 
			}
		} //throw error if it doesn't work?
	}
	
	void set_default_values() {
		String default_bgs = "{\"name\":\"\",\"pan\":0,\"pitch\":100,\"volume\":90}";
		String default_bgm = "{\"name\":\"\",\"pan\":0,\"pitch\":100,\"volume\":90}";
		
		entries[0].set_value(false, "autoplayBgm");
		entries[1].set_value(false, "autoplayBgs");
		entries[2].set_value("\"\"", "battleback1Name");
		entries[3].set_value("\"\"", "battleback2Name");
		entries[4].set_value(default_bgm, "bgm");
		entries[5].set_value(default_bgs, "bgs");
		entries[6].set_value(false, "disableDashing");
		entries[7].set_value("\"\"", "displayName");
		entries[8].set_value("[]", "encounterList");
		entries[9].set_value(30, "encounterStep");
		entries[10].set_value(map_height, "height");
		entries[11].set_value("\"\"", "note");
		entries[12].set_value(false, "parallaxLoopX");
		entries[13].set_value(false, "parallaxLoopY");
		entries[14].set_value("\"\"", "parallaxName");
		entries[15].set_value(true, "parallaxShow");
		entries[16].set_value(0, "parallaxSx");
		entries[17].set_value(0, "parallaxSy");
		entries[18].set_value(0, "scrollType");
		entries[19].set_value(false, "specifyBattleback");
		entries[20].set_value(map_tileset_ID, "tilesetId");
		entries[21].set_value(map_width, "width");
	}

}