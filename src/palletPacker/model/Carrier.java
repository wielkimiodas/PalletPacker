package palletPacker.model;

import java.util.ArrayList;

public class Carrier {
	private int id;
	private Pallet palletUsed;
	private int extensionsUsed;
	private ArrayList<Package> packagesAssigned;
	private float volumeLeft;
	private float volumeInUse;
	private float typesArray[];

	public Carrier(int id, Pallet palletUsed, int nPalletTypes) {
		this.id = id;
		this.palletUsed = palletUsed;
		packagesAssigned = new ArrayList<Package>();
		this.volumeLeft = palletUsed.maxVolume;
		typesArray = new float[nPalletTypes];
	}

	public float getVolumeInUse() {
		return volumeInUse;
	}

	public float getVolumeLeft() {
		return volumeLeft;
	}

	public ArrayList<Package> getPackagesAssigned() {
		return packagesAssigned;
	}

	public boolean canHandlePackage(Package packageToCheck) {
		float packageVolume = packageToCheck.getVolume();
		if (getVolumeLeft() < packageVolume) {
			return false;
		}

		if (typesArray[packageToCheck.getDefaultPallet().getId()]
				+ packageVolume > typesArray[palletUsed.getId()]) {
			return false;
		}

		return true;
	}

	public void addPackage(Package packageToAdd) {
		this.packagesAssigned.add(packageToAdd);
		float packageVolume = packageToAdd.getVolume();
		volumeLeft -= packageVolume;
		volumeInUse += packageVolume;
		extensionsUsed = (int) Math.ceil(volumeInUse / palletUsed.area
				/ palletUsed.extensionHeight);
		typesArray[packageToAdd.getDefaultPallet().id] += packageVolume;
	}

	public int getId() {
		return id;
	}

	public Pallet getPalletUsed() {
		return palletUsed;
	}

	public int getExtensionsUsed() {
		return extensionsUsed;
	}
}
