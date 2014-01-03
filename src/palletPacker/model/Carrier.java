package palletPacker.model;

import java.util.ArrayList;

public class Carrier {
	int id;
	Pallet palletUsed;
	int extensionsUsed;
	ArrayList<Package> packagesAssigned;
	float volumeLeft;
	float volumeInUse;
	float tomekArray[];

	public Carrier(int id, Pallet palletUsed, int palletTypes) {
		this.id = id;
		this.palletUsed = palletUsed;
		packagesAssigned = new ArrayList<Package>();
		this.volumeLeft = palletUsed.maxVolume;
		tomekArray = new float[palletTypes];
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

	public void addPackage(Package packageToAdd) {
		this.packagesAssigned.add(packageToAdd);
		volumeLeft -= packageToAdd.volume;
		volumeInUse += packageToAdd.volume;
		extensionsUsed = (int) Math.ceil(volumeInUse / palletUsed.area
				/ palletUsed.extensionHeight);
		tomekArray[packageToAdd.defaultPallet.id] += packageToAdd.volume;		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Pallet getPalletUsed() {
		return palletUsed;
	}

	public void setPalletUsed(Pallet palletUsed) {
		this.palletUsed = palletUsed;
	}

	public int getExtensionsUsed() {
		return extensionsUsed;
	}

	public void setExtensionsUsed(int extensionsUsed) {
		this.extensionsUsed = extensionsUsed;
	}

}
