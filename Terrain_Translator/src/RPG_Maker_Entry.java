
public class RPG_Maker_Entry {
	
	//Variables
	String name;

	char type;
	boolean bValue;
	String sValue;
	int iValue;
	
	//Constructors
	RPG_Maker_Entry() {}
	
	RPG_Maker_Entry(String n,boolean b) {type = 'b'; bValue = b;}
	RPG_Maker_Entry(String n, String s) {type = 's'; sValue = s;}
	RPG_Maker_Entry(String n, int i) {type = 'i'; iValue = i;}
	
	//Functions
	void initialize() {type = '0'; bValue = false; sValue = ""; iValue = 0;}
	
	void set_value(boolean b, String n) { bValue = b; type = 'b'; name = n;}
	void set_value(String s, String n) { sValue = s; type = 's'; name = n;}
	void set_value(int i, String n) { iValue = i; type = 'i'; name = n; }
	
	char get_type() {return type;}
	String get_name() {return name;}
	
	boolean get_bValue() {return bValue;}
	String get_sValue() {return sValue;}
	int get_iValue() {return iValue;}
	
	void take_on_value (RPG_Maker_Entry entry){
		
		//reset all self variables
		bValue = false; sValue = ""; iValue = 0;
		
		type = entry.get_type();
		switch (type) {
		case 'b' :
			bValue = entry.get_bValue();
			break;
		case 's' :
			sValue = entry.get_sValue();
			break;
		case 'i' :
			iValue = entry.get_iValue();
			break;
		default: 
			//flag error
			break;
		}
	}

	
}
