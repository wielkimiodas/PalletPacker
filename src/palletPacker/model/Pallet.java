package palletPacker.model;

public class Pallet {

	String id;
	int area;
	float height;
	float maxHeight;
	
	public Pallet(String id, int area, float height, float maxHeight) {
		super();
		this.id = id;
		this.area = area;
		this.height = height;
		this.maxHeight = maxHeight;
	}	
	
}
