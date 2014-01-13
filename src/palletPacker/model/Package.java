package palletPacker.model;

public class Package {

	private final String id;
	private final int volume;
	private final Pallet defaultPallet;
	private final int load;

	// obolete private Set<Pallet> compatiblePallets;

	public Package(String id, int volume, Pallet defaultPallet, int load) {
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

	// public Set<Pallet> getCompatiblePallets() {
	// return compatiblePallets;
	// }

	public int getLoad() {
		return load;
	}

	public int getVolume() {
		return volume;
	}
}
