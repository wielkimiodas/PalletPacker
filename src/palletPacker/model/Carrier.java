package palletPacker.model;

import java.util.ArrayList;

public class Carrier {
	private final int id;
	private final Pallet palletUsed;
	private int extensionsUsed;
	private final ArrayList<Package> packagesAssigned;
	private float volumeLeft;
	private float volumeInUse;
	private final float typesArray[];

	public Carrier(int id, Pallet palletUsed, int nPalletTypes) {
		this.id = id;
		this.palletUsed = palletUsed;
		packagesAssigned = new ArrayList<Package>();
		this.volumeLeft = palletUsed.getMaxVolume();
		typesArray = new float[nPalletTypes];
	}

	public void addPackage(Package packageToAdd) {
		this.packagesAssigned.add(packageToAdd);
		final float packageVolume = packageToAdd.getVolume();
		volumeLeft -= packageVolume;
		volumeInUse += packageVolume;
		extensionsUsed = (int) Math.ceil(volumeInUse / palletUsed.getArea()
				/ palletUsed.getExtensionHeight());
		typesArray[packageToAdd.getDefaultPallet().getId()] += packageVolume;
	}

	public boolean canHandlePackage(Package packageToCheck) {
		final float packageVolume = packageToCheck.getVolume();
		if (getVolumeLeft() < packageVolume) {
			return false;
		}

		if (typesArray[packageToCheck.getDefaultPallet().getId()]
				+ packageVolume > typesArray[palletUsed.getId()]) {
			return false;
		}

		return true;
	}

	public int getExtensionsUsed() {
		return extensionsUsed;
	}

	public int getId() {
		return id;
	}

	public ArrayList<Package> getPackagesAssigned() {
		return packagesAssigned;
	}

	public Pallet getPalletUsed() {
		return palletUsed;
	}

	public float getVolumeInUse() {
		return volumeInUse;
	}

	public float getVolumeLeft() {
		return volumeLeft;
	}
}
