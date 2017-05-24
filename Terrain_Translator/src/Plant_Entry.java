
public class Plant_Entry {
	String name;
	int population;
	int desire_score;
	
	void initialize() {name = ""; population = 0; desire_score = 0;}
	void setName(String n) {name = n;}
	void setPopulation(int p) {population = p;}
	void setDesire(int d) {desire_score = d;}
	String getName() {return name;}
	int getPopulation() {return population;}
	int getDesire() {return desire_score;}
}
