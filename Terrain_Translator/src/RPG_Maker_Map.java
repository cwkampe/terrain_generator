
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;

public class RPG_Maker_Map {
	
	public static void main(String[] args) {
		
	RPG_Maker_Map test = new RPG_Maker_Map();
	test.initialize();

	}
	
	
	int MAX_ENTRIES = 21;
	int RPG_MAKER_DATA_ARRAY_NUM = 6;
	
	int map_width = 5;
	int map_height = 5;
	int map_area = map_width * map_height;
	int map_tileset_ID = 1;
	
	
	RPG_Maker_Entry entries[] = new RPG_Maker_Entry[MAX_ENTRIES];
	String MapName = "Map010";
	String MapExtension =".json";
	
	
	RPG_Maker_Map() {}
	
	void initialize() {

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
				writer.print(",\n");
			}
			
			//get map values
			char terrain_map[] = make_test_map();//load input values
			int map_values [] = make_int_map(map_width, map_height);
			for (int i = 0; i < map_area; i++) {
				//Convert char to integer value
				map_values[i] = convert_letter_to_code(terrain_map[i]);
			}
			
			//Write data section with map values
			writer.print("\"data\":[");
			for (int i = 0; i < map_values.length; i++) {
				writer.print(map_values[i]);
				if (i+1 != map_values.length) {writer.println(',');	}
			} //skip the comma for the last entry
			writer.print("],\n\"events\":[\n]\n}");
			
		} catch (IOException e) {
			//we got a problem
		}
		
	}
	
	char[] make_test_map () {

		char testMap[] = new char[map_area];
		for (int i = 0; i < map_area; i++) {
			testMap[i] = 'g';
			if (i%3 == 0) {testMap[i] = 'w';}
		}
		return testMap;
	}
	
	int[] make_int_map(int width, int height){
		int area = height * width * RPG_MAKER_DATA_ARRAY_NUM;
		int map [] = new int [area];
		for (int i = 0; i < map_area; i++) {
			map[i] = 0;
		}
		return map;
	}
	
	int convert_letter_to_code(char x){
		if (x == 'g') {return 2211;}
		if (x == 'w') {return 2211;}
		else return 0; 
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
		String default_bgs = "\"name\":\"\",\"pan\":0,\"pitch\":100,\"volume\":90}";
		String default_bgm = "\"name\":\"\",\"pan\":0,\"pitch\":100,\"volume\":90}";
		
		entries[0].set_value(false, "autoplayBgm");
		entries[1].set_value(false, "autoplayBgs");
		entries[2].set_value("\"\"", "battleback1Name");
		entries[3].set_value("\"\"", "battleback2Name");
		entries[4].set_value(default_bgs, "bgs");
		entries[5].set_value(default_bgm, "bgm");
		entries[7].set_value(false, "disableDashing");
		entries[8].set_value("\"\"", "displayName");
		entries[9].set_value("[]", "encounterList");
		entries[10].set_value(30, "encounterStep");
		entries[11].set_value(map_height, "height");
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

/*******************SAMPLE DEFAULT TEXT
"autoplayBgm":false,
"autoplayBgs":false,
"battleback1Name":"",
"battleback2Name":"",
"bgm":{"name":"","pan":0,"pitch":100,"volume":90},
"bgs":{"name":"","pan":0,"pitch":100,"volume":90},
"disableDashing":false,
"displayName":"",
"encounterList":[],
"encounterStep":30,
"height":11,
"note":"",
"parallaxLoopX":false,
"parallaxLoopY":false,
"parallaxName":"",
"parallaxShow":true,
"parallaxSx":0,
"parallaxSy":0,
"scrollType":0,
"specifyBattleback":false,
"tilesetId":1,
"width":7,
*/