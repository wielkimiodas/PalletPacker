package palletPacker.model;

import java.util.ArrayList;

public class Carrier {
	private final Pallet[] allPallets;
	private int currentPalletId;
	private final ArrayList<Package> packagesAssigned;
	private int volumeInUse = 0;
	private int loadInUse = 0;
	private final float typesVolumes[];

	public Carrier(Pallet[] pallets) {
		this.allPallets = pallets;
		this.currentPalletId = 0;
		this.packagesAssigned = new ArrayList<Package>();
		this.typesVolumes = new float[pallets.length];
	}

	public void addPackage(Package packageToAdd) throws Exception {
		final int packageId = packageToAdd.getDefaultPallet().getId(); 
		final float packageVolume = packageToAdd.getVolume();
		final float packageLoad = packageToAdd.getLoad();
		
		if (canHandlePackage(packageToAdd, packageId)){
			currentPalletId = packageId;
		} else if (!canHandlePackage(packageToAdd, currentPalletId)){
			throw new Exception("Something wrong");
		}
		
		volumeInUse += packageVolume;
		loadInUse += packageLoad;
		typesVolumes[packageId] += packageVolume;
		packagesAssigned.add(packageToAdd);
	}
	
	public void show(){
		for(Package p : packagesAssigned){
			System.out.print(p.getId() + " ");
		}
		System.out.println();
	}
	
	public boolean contains(Package pkg){
		/*for(Package p : packagesAssigned){
			System.out.print(p.getId() + " ");
		}
		System.out.println(pkg.getId());*/
		return packagesAssigned.contains(pkg);
	}
	
	public boolean removePackage(Package packageToRemove){
		final int packageId = packageToRemove.getDefaultPallet().getId();
		final float packageVolume = packageToRemove.getVolume();
		
		if (packageId == currentPalletId){
			float secondMax = 0;
			for(int i = 0; i < typesVolumes.length; i++){
				if (i == packageId){
					continue;
				}
				if (secondMax < typesVolumes[i]){
					secondMax = typesVolumes[i];
				}
			}
			
			if (secondMax > typesVolumes[packageId] - packageVolume){
				return false;
			}
		}
		
		final float packageLoad = packageToRemove.getLoad();
		
		volumeInUse -= packageVolume;
		loadInUse -= packageLoad;
		typesVolumes[packageId] -= packageVolume;
		packagesAssigned.remove(packageToRemove);
		
		return true;
	}
	
	private boolean canHandlePackage(Package packageToCheck, int palletId){
		if (packagesAssigned.size() == 0){
			return true;
		}
		
		final int packageId = packageToCheck.getDefaultPallet().getId();
		final float packageVolume = packageToCheck.getVolume();
		final float packageLoad = packageToCheck.getLoad();
		
		if (loadInUse + packageLoad > allPallets[palletId].getMaxLoad()){
			return false;
		}
		
		if (palletId == packageId){
			if (typesVolumes[packageId] + packageVolume < typesVolumes[currentPalletId]){
				return false;
			}
			
			return true;
		}
		if (palletId == currentPalletId){
			if (typesVolumes[packageId] + packageVolume > typesVolumes[currentPalletId]){
				return false;
			}
			
			return true;
		}
		
		return false;
	}

	public boolean canHandlePackage(Package packageToCheck) {
		if (canHandlePackage(packageToCheck, packageToCheck.getDefaultPallet().getId())){
			return true;
		}
		if (canHandlePackage(packageToCheck, currentPalletId)){
			return true;
		}

		return false;
	}

	public int getExtensionsUsed() {
		Pallet palletUsed = getPalletUsed();
		return (int) Math.ceil(volumeInUse / palletUsed.getArea()
				/ palletUsed.getExtensionHeight());
	}

	public ArrayList<Package> getPackagesAssigned() {
		return packagesAssigned;
	}

	public Pallet getPalletUsed() {
		return allPallets[currentPalletId];
	}

	public int getVolumeInUse() {
		return volumeInUse;
	}
}
