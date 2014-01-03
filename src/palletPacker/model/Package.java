package palletPacker.model;

public class Package {

	String id;
	int volume;

	public String getId() {
		return id;
	}

	public int getVolume() {
		return volume;
	}

	public Pallet getDefaultPallet() {
		return defaultPallet;
	}

	public Pallet[] getCompatiblePallets() {
		return CompatiblePallets;
	}

	Pallet defaultPallet;
	Pallet[] CompatiblePallets;

	public Package(String id, int volume, Pallet defaultPallet,
			Pallet[] compatiblePallets) {
		super();
		this.id = id;
		this.volume = volume;
		this.defaultPallet = defaultPallet;
		CompatiblePallets = compatiblePallets;
	}
}
