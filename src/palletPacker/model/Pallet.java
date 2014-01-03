package palletPacker.model;

public class Pallet {

	String id;
	int area;
	float extensionHeight;
	float maxHeight;
	float maxVolume;

	public Pallet(String id, int area, float height, float maxHeight) {
		super();
		this.id = id;
		this.area = area;
		this.extensionHeight = height;
		this.maxHeight = maxHeight;
		this.maxVolume = maxHeight * area;
	}

	public String getId() {
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

}
