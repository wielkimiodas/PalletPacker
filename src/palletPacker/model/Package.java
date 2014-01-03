package palletPacker.model;

import java.util.HashSet;
import java.util.Set;

public class Package {

	private String id;
	private int volume;
	private Pallet defaultPallet;
	private Set<Pallet> compatiblePallets;

	public String getId() {
		return id;
	}

	public int getVolume() {
		return volume;
	}

	public Pallet getDefaultPallet() {
		return defaultPallet;
	}

	public Set<Pallet> getCompatiblePallets() {
		return compatiblePallets;
	}

	public Package(String id, int volume, Pallet defaultPallet,
			Set<Pallet> compatiblePallets) {
		super();
		this.id = id;
		this.volume = volume;
		this.defaultPallet = defaultPallet;
		this.compatiblePallets = new HashSet<>(compatiblePallets);
	}
}
