import java.io.IOException;

public class Test {
	
	static int test_length = 10;
	static int test_width = 10;

	public static void main(String[] args) throws IOException {
		
		Area_Tile.initialize(test_width, test_length);
		Area_Tile[] Sample_map = new Area_Tile[Area_Tile.map_area];
		//Initialize Sample_Map;
		//Generate ASCII ground map;
		
		Vegetation_Controls vegControl = new Vegetation_Controls();
		vegControl.initialize(test_width, test_length);
		vegControl.generate_area_vegetation(Sample_map);
		//Generate ASCII plant map



			//that should be it. 
			}// TODO Auto-generated method stub
	}

