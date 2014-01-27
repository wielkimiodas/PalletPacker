package palletPacker.model;

import java.util.ArrayList;
import java.util.List;

public class Carrier {
	private final Pallet[] allPallets;
	private int currentPalletId;
	private final ArrayList<Package> packagesAssigned;
	private float volumeInUse = 0;
	private float loadInUse = 0;
	private final float typesVolumes[];

	public Carrier(Pallet[] pallets) {
		this.allPallets = pallets;
		this.currentPalletId = 0;
		this.packagesAssigned = new ArrayList<Package>();
		this.typesVolumes = new float[pallets.length];
	}

	public boolean contains(Package pkg) {
		return packagesAssigned.contains(pkg);
	}
	
	public boolean isEmpty(){
		return packagesAssigned.size() == 0;
	}

	public void removePackage(Package pkg, int newPalletId){
		final float packageVolume = pkg.getVolume();

		volumeInUse -= packageVolume;
		loadInUse -= pkg.getLoad();
		typesVolumes[pkg.getDefaultPallet().getId()] -= packageVolume;
		if (!packagesAssigned.contains(pkg)) {
			System.out.println("WRONG!!!");
		}
		
		packagesAssigned.remove(pkg);

		currentPalletId = newPalletId;
	}

	/**
	 * Sprawdza, czy mo�na usun�� wskazan� paczk�.
	 * 
	 * @param pkg
	 *            Paczka do usuni�cia.
	 * @return Id nowej palety lub -1, je�eli nie mo�na usun�� paczki.
	 */
	public int canRemovePackage(Package pkg) {
		if (packagesAssigned.size() == 1){
			return currentPalletId;
		}
		
		final int pkgPalletId = pkg.getDefaultPallet().getId();
		final float packageVolume = pkg.getVolume();

		// typ palety i tak jest inny, wi�c paczk� mo�na wyrzuci�
		if (pkgPalletId != currentPalletId) {
			return currentPalletId;
		} else {
			float secondMax = 0;
			List<Integer> best = new ArrayList<>();
			// utworzenie listy typ�w palet z paczkami o maksymalnej obj�to�ci 
			for (int i = 0; i < typesVolumes.length; i++) {
				if (secondMax <= typesVolumes[i]) {
					float vol = typesVolumes[i];
					if (i == currentPalletId){
						vol -= packageVolume;
					}
					
					if (secondMax < vol){
						best.clear();
					}
					secondMax = vol;
					best.add(i);
				}
			}
			
			if (best.size() == 0){
				System.out.println("Best.size() == 0 !!!");
				return -1;
			}
			
			// obecny typ palety ma nadal najwi�ksz� obj�to�� 
			if (best.size() == 1 && best.contains(currentPalletId)){
				return currentPalletId;
			}
			
			List<Integer> possible = new ArrayList<>();
			
			float newVolume = volumeInUse - packageVolume;
			float newLoad = loadInUse - pkg.getLoad();
			
			// uzupe�nienie listy typ�w palet, kt�re pomieszcz� 
			for (Integer id : best) {
				if (allPallets[id].getMaxVolume() < newVolume){
					continue;
				}
				if (allPallets[id].getMaxLoad() < newLoad){
					continue;
				}
				possible.add(id);
			}
			
			if (possible.size() == 0){
				return -1;
			}
			
			if (possible.size() == 1){
				return possible.get(0);
			}
			
			float minArea = Float.MAX_VALUE;
			int minId = -1;
			for(Integer id : possible){
				if (minArea > allPallets[id].getArea()){
					minArea = allPallets[id].getArea();
					minId = id;
				}
			}
			
			return minId;
		}
	}

	private boolean canAddPackage(Package pcg, int palletId) {
		Pallet pallet = allPallets[palletId];
		if (loadInUse + pcg.getLoad() > pallet.getMaxLoad()) {
			return false;
		}

		if (volumeInUse + pcg.getVolume() > pallet.getMaxVolume()) {
			return false;
		}

		return true;
	}

	/**
	 * Sprawdza, czy mo�na doda� wskazan� paczk�.
	 * 
	 * @param pcg
	 *            Paczka do dodania.
	 * @return ID nowej palety lub -1, je�eli nie mo�na doda� paczki.
	 */
	public int canAddPackage(Package pcg) {
		if (packagesAssigned.size() == 0) {
			return pcg.getDefaultPallet().getId();
		}

		final int pkgPalletId = pcg.getDefaultPallet().getId();

		if (pkgPalletId == currentPalletId) {
			return canAddPackage(pcg, currentPalletId) ? currentPalletId : -1;
		} else {
			float diff = typesVolumes[pkgPalletId] + pcg.getVolume()
					- typesVolumes[currentPalletId];
			if (diff == 0) {
				if (allPallets[pkgPalletId].getArea() < allPallets[currentPalletId]
						.getArea()) {
					if (canAddPackage(pcg, pkgPalletId)) {
						return pkgPalletId;
					}

					if (canAddPackage(pcg, currentPalletId)) {
						return currentPalletId;
					}
					return -1;
				} else {
					if (canAddPackage(pcg, currentPalletId)) {
						return currentPalletId;
					}

					if (canAddPackage(pcg, pkgPalletId)) {
						return pkgPalletId;
					}
					return -1;
				}
			} else if (diff > 0) {
				return canAddPackage(pcg, pkgPalletId) ? pkgPalletId : -1;

			} else /* if (diff < 0) */{
				return canAddPackage(pcg, currentPalletId) ? currentPalletId
						: -1;
			}
		}
	}
	
	public void addPackage(Package pkg, int newPalletId){
		//System.out.println("Add " + newPalletId);
		final float packageVolume = pkg.getVolume();

		volumeInUse += packageVolume;
		loadInUse += pkg.getLoad();
		typesVolumes[pkg.getDefaultPallet().getId()] += packageVolume;
		packagesAssigned.add(pkg);
		
		currentPalletId = newPalletId;
	}

	public int getExtensionsUsed() {
		Pallet palletUsed = getPalletUsed();
		int result = (int) Math.ceil(volumeInUse / palletUsed.getArea()
				/ palletUsed.getExtensionHeight());
		/*if (result < 0) {
			System.out.println("Ext: " + volumeInUse + " "
					+ palletUsed.getArea() + " "
					+ palletUsed.getExtensionHeight());
		}*/
		return result;
	}

	public ArrayList<Package> getPackagesAssigned() {
		return packagesAssigned;
	}
	
	public int getCurrentPalletId(){
		return currentPalletId;
	}

	public Pallet getPalletUsed() {
		return allPallets[currentPalletId];
	}
	
	public float getArea(){
		if (packagesAssigned.size() == 0){
			return 0;
		}
		
		return allPallets[currentPalletId].getArea();
	}
	
	public float getVolume(){
		return getExtensionsUsed() * getPalletUsed().getExtensionHeight() * getArea();
	}
}
