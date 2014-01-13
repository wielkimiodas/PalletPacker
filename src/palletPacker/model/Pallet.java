package palletPacker.model;

public class Pallet {

	private final int id;
	private final int area;
	private final int extensionHeight;
	private final int maxHeight;
	private final int maxVolume;
	private final int maxLoad;
	private final String name;

	public Pallet(int id, String name, int area, int extensionHeight,
			int maxHeight, int maxLoad) {
		super();
		this.id = id;
		this.area = area;
		this.extensionHeight = extensionHeight;
		this.maxHeight = maxHeight;
		this.maxVolume = maxHeight * area;
		this.name = name;
		this.maxLoad = maxLoad;
	}

	public int getArea() {
		return area;
	}

	public int getExtensionHeight() {
		return extensionHeight;
	}

	public int getId() {
		return id;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public int getMaxLoad() {
		return maxLoad;
	}

	public int getMaxVolume() {
		return maxVolume;
	}

	public String getName() {
		return name;
	}

}
