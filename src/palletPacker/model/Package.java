package palletPacker.model;

public class Package {
	
	String id;
	int volume;
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
