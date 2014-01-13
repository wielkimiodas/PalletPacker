package palletPacker.processor;

import java.io.IOException;
import java.util.ArrayList;

import palletPacker.model.Carrier;
import palletPacker.model.Package;
import palletPacker.model.Warehouse;

public class Optimizer {

	private Warehouse warehouse;
	private ArrayList<Carrier> carriers;
	private int totalArea = 0;
	private float minPalletVolume;

	public Optimizer() {
		super();

		initializeOptimizer();
	}

	private void computeTargetFunctionValue() {
		minPalletVolume = Float.MAX_VALUE;
		for (final Carrier carrier : carriers) {
			totalArea += carrier.getPalletUsed().getArea();
			final float usedSpace = carrier.getVolumeInUse();
			if (usedSpace < minPalletVolume) {
				minPalletVolume = usedSpace;
			}
		}
	}

	private void initializeOptimizer() {
		warehouse = new Warehouse();
		try {
			warehouse.ReadData("data/example1.txt");
		} catch (final NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printResults() {
		System.out.println("1");
		System.out.println(totalArea + "\t" + minPalletVolume);
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

	public void runNaivePacker() {
		carriers = new ArrayList<Carrier>();
		final Package[] pckgs = warehouse.getPackages();

		for (int i = 0; i < pckgs.length; i++) {
			if (!tryArrangeOnExistingCarrier(pckgs[i])) {
				final Carrier c = new Carrier(carriers.size() + 1,
						pckgs[i].getDefaultPallet(),
						warehouse.getPallets().length);
				c.addPackage(pckgs[i]);
				carriers.add(c);
			}
		}
		computeTargetFunctionValue();
	}

	private Boolean tryArrangeOnExistingCarrier(Package givenPackage) {
		// final Set<Pallet> compatiblePallets = givenPackage
		// .getCompatiblePallets();
		// for (final Carrier carrier : carriers) {
		// if (!compatiblePallets.contains(carrier.getPalletUsed())) {
		// continue;
		// }
		//
		// if (carrier.canHandlePackage(givenPackage)) {
		// carrier.addPackage(givenPackage);
		// return true;
		// }
		// }

		return false;
	}
}
