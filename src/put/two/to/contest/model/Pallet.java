package put.two.to.contest.model;

public class Pallet {

	private final int id;
	private final float area;
	private final float extensionHeight;
	private final float maxHeight;
	private final float maxVolume;
	private final float maxLoad;
	private final String name;

	public Pallet(int id, String name, float area, float extensionHeight,
			float maxHeight, float maxLoad) {
		super();
		this.id = id;
		this.area = area;
		this.extensionHeight = extensionHeight;
		this.maxHeight = maxHeight;
		this.maxVolume = maxHeight * area;
		this.name = name;
		this.maxLoad = maxLoad;
	}

	public float getArea() {
		return area;
	}

	public float getExtensionHeight() {
		return extensionHeight;
	}

	public int getId() {
		return id;
	}

	public float getMaxHeight() {
		return maxHeight;
	}

	public float getMaxLoad() {
		return maxLoad;
	}

	public float getMaxVolume() {
		return maxVolume;
	}

	public String getName() {
		return name;
	}

}
