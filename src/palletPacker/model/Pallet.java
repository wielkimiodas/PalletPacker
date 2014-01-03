package palletPacker.model;

public class Pallet {

	int id;
	int area;
	float extensionHeight;
	float maxHeight;
	float maxVolume;
	String name;

	public Pallet(int id, String name, int area, float height, float maxHeight) {
		super();
		this.id = id;
		this.area = area;
		this.extensionHeight = height;
		this.maxHeight = maxHeight;
		this.maxVolume = maxHeight * area;
		this.name=name;
	}

	public int getId() {
		return id;
	}

	public int getArea() {
		return area;
	}

	public float getExtensionHeight() {
		return extensionHeight;
	}

	public float getMaxHeight() {
		return maxHeight;
	}

	public float getMaxVolume() {
		return maxVolume;
	}
	
	public String getName() {
		return name;
	}

}
