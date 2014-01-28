package put.two.to.contest.model;

import java.util.ArrayList;

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

	public void addPackage(Package pkg, int newPalletId) {
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

		return result;
	}

	public ArrayList<Package> getPackagesAssigned() {
		return packagesAssigned;
	}

	public int getCurrentPalletId() {
		return currentPalletId;
	}

	public Pallet getPalletUsed() {
		return allPallets[currentPalletId];
	}

	public float getArea() {
		if (packagesAssigned.size() == 0) {
			return 0;
		}

		return allPallets[currentPalletId].getArea();
	}

	public float getVolume() {
		return getExtensionsUsed() * getPalletUsed().getExtensionHeight()
				* getArea();
	}
}
