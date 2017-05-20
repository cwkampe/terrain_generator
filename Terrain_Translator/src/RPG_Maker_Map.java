
import java.io.PrintWriter;
import java.io.IOException;
import java.math.*;

public class RPG_Maker_Map {
	
	int MAX_ENTRIES = 22;
	int RPG_MAKER_DATA_ARRAY_NUM = 6;
	
	int map_width = 10;
	int map_height = 10;
	int map_area = map_width * map_height;
	int map_tileset_ID = 1;
	
	
	public static RPG_Maker_Entry[] entries;
	String MapName = "Map010";
	String MapExtension =".json";
	
	
	RPG_Maker_Map() {}
	
	void initialize() {
		entries = new RPG_Maker_Entry[MAX_ENTRIES];
		for (int i = 0; i < MAX_ENTRIES; i++) {
			entries[i] = new RPG_Maker_Entry();
			entries[i].initialize();
		}
		set_default_values();
		String fileName = MapName + MapExtension;
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
			int[] map_values = make_int_map(map_width, map_height);
			
			//add ground to map_values
			for (int i = 0; i < map_area; i++) {
				//Convert char to integer value
				map_values[i] = get_auto_tile_value(convert_letter_to_code(terrain_map[i]), i, terrain_map); 
				//map_values[i] = convert_letter_to_code(terrain_map[i]);
			}
			
			//get map values (give 1 of trees)
			terrain_map = flush_map(terrain_map);
			terrain_map = make_test_map(1);
			//add level level 1 above ground to map_values
			for (int i = 0; i < map_area; i++){
				map_values[i+map_area] = get_auto_tile_value(convert_letter_to_code(terrain_map[i]), i, terrain_map);
			}
			
			//Write data section with map_values
			writer.print("\n\"data\":[");
			for (int i = 0; i < map_values.length; i++) {
				writer.print(map_values[i]);
				if (i+1 != map_values.length) {writer.print(',');	}
			} //skip the comma for the last entry
			writer.print("],\n\"events\":[\n]\n}");
			writer.close();
		} catch (IOException e) {
			//we got a problem; hasn't come up
		}
		
	}
	
	int convert_letter_to_code(char x){
		if (x == 'w') {return 2048;}
		if (x == 'g') {return 2816;}
		if (x == 't') {return 3008;}
		else return 0; 
	}
	
	
	char[] make_test_map (int c) {
		
		char[] testMap = new char[map_area];
		switch (c) {
		case 1: 
			for (int i = 0; i < map_area; i++) {
				
				testMap[i] = '0';
				if (i%4 != 0) {testMap[i] = 't';}
			}
			break;
		default: 
			for (int i = 0; i < map_area; i++) {
				//testMap[i] = 'g';
				//if (i%6 == 0) {testMap[i] = 'w';}
			}
			break;
		}
		return testMap;
	}
	
	int[] make_int_map(int width, int height){
		int area = height * width * RPG_MAKER_DATA_ARRAY_NUM;
		int[] map = new int [area];
		for (int i = 0; i < map_area; i++) {
			map[i] = 0;
		}
		return map;
	}
	
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
	int get_auto_tile_value (int base, int index, char[] terrain) {
		
		if (terrain[index] == '0') {return 0;}
		
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