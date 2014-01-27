package put.two.to.contest.model;

public class Package {

	private final String id;
	private final float volume;
	private final Pallet defaultPallet;
	private final float load;

	// obolete private Set<Pallet> compatiblePallets;

	public Package(String id, float volume, Pallet defaultPallet, float load) {
		super();
		this.id = id;
		this.volume = volume;
		this.defaultPallet = defaultPallet;
		this.load = load;
	}

	public Pallet getDefaultPallet() {
		return defaultPallet;
	}

	public String getId() {
		return id;
	}

	public float getLoad() {
		return load;
	}

	public float getVolume() {
		return volume;
	}
}
