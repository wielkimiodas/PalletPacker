package palletPacker.processor;

import java.util.ArrayList;

import palletPacker.model.Carrier;
import palletPacker.model.Package;
import palletPacker.model.Pallet;

public class Solver {
	private final Pallet[] pallets;
	private final ArrayList<Carrier> carriers;

	private Result result;

	public Solver(Pallet[] pallets) {
		this.pallets = pallets;
		this.carriers = new ArrayList<>();
	}

	public void clear() {
		carriers.clear();
		result = null;
	}

	private Result computeTargetFunctionValue() {
		float minPalletVolume = Float.MAX_VALUE;
		int totalArea = 0;
		for (final Carrier carrier : carriers) {
			totalArea += carrier.getPalletUsed().getArea();
			final float usedSpace = carrier.getVolumeInUse();
			if (minPalletVolume > usedSpace) {
				minPalletVolume = usedSpace;
			}
		}

		return new Result(totalArea, minPalletVolume);
	}

	public ArrayList<Carrier> getCarriers() {
		return carriers;
	}

	public void printResults() {
		System.out.println("1");
		System.out.println(result.getTotalArea() + "\t"
				+ result.getMinPalletVolume());
		System.out.println(carriers.size());
		int pckgs = 0;
		for (final Carrier c : carriers) {
			System.out.println("n" + c.getId() + "\t"
					+ c.getPalletUsed().getName() + "\t"
					+ c.getExtensionsUsed());
			pckgs += c.getPackagesAssigned().size();
		}
		System.out.println(pckgs);
		for (final Carrier c : carriers) {
			for (final Package p : c.getPackagesAssigned()) {
				System.out.println(p.getId() + "\t" + "n" + c.getId());
			}
		}
	}

	public void run(Configuration conf) {
		for (int i = 0; i < conf.getPackages().length; i++) {
			final Package currentPackage = conf.getPackages()[i];
			if (!tryArrangeOnExistingCarrier(currentPackage)) {
				final Carrier carrier = new Carrier(carriers.size() + 1,
						currentPackage.getDefaultPallet(), pallets.length);
				carrier.addPackage(currentPackage);
				carriers.add(carrier);
			}
		}

		conf.setResult(computeTargetFunctionValue());
		conf.setCarriers(carriers);
	}

	private boolean tryArrangeOnExistingCarrier(Package givenPackage) {
		// final Set<Pallet> compatiblePallets = givenPackage

		for (final Carrier carrier : carriers) {
			// if (!compatiblePallets.contains(carrier.getPalletUsed())) {
			// continue;
			// }

			if (carrier.canHandlePackage(givenPackage)) {
				carrier.addPackage(givenPackage);
				return true;
			}
		}

		return false;
	}
}
