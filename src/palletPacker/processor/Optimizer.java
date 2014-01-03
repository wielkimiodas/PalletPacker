package palletPacker.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import palletPacker.model.Carrier;
import palletPacker.model.Pallet;
import palletPacker.model.Warehouse;
import palletPacker.model.Package;

public class Optimizer {

	private Warehouse warehouse;
	private ArrayList<Carrier> carriers;
	private int totalArea = 0;
	private float minPalletVolume;

	public Optimizer() {
		super();

		initializeOptimizer();
	}

	private ArrayList<Carrier> getSpecificCarriers(String palleteName) {
		ArrayList<Carrier> specificCarriers = new ArrayList<Carrier>();
		for (Carrier carrier : carriers) {
			if (carrier.getPalletUsed().getName().equals(palleteName)) {
				specificCarriers.add(carrier);
			}
		}
		return specificCarriers;
	}

	private Boolean iryArrangeOnExistingCarrier(Package givenPackage) {
		ArrayList<Carrier> availableCarriers = getSpecificCarriers(givenPackage
				.getDefaultPallet().getName());
		for (Carrier carrier : availableCarriers) {
			if (carrier.getVolumeLeft() >= givenPackage.getVolume()) {
				carrier.addPackage(givenPackage);
				return true;
			}
		}

		Set<Pallet> compatiblePallets = givenPackage.getCompatiblePallets();
		for (Carrier carrier : carriers) {
			if (!compatiblePallets.contains(carrier.getPalletUsed())) {
				continue;
			}

			if (carrier.canHandlePackage(givenPackage)) {
				carrier.addPackage(givenPackage);
				return true;
			}
		}

		return false;
	}

	private void initializeOptimizer() {
		warehouse = new Warehouse();
		try {
			warehouse.ReadData("data/example1.txt");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void computeTargetFunctionValue() {
		minPalletVolume = Float.MAX_VALUE;
		for (Carrier carrier : carriers) {
			totalArea += carrier.getPalletUsed().getArea();
			float usedSpace = carrier.getVolumeInUse();
			if (usedSpace < minPalletVolume) {
				minPalletVolume = usedSpace;
			}
		}
	}

	public void runNaivePacker() {
		carriers = new ArrayList<Carrier>();
		Package[] pckgs = warehouse.getPackages();

		for (int i = 0; i < pckgs.length; i++) {
			if (!iryArrangeOnExistingCarrier(pckgs[i])) {
				Carrier c = new Carrier(carriers.size() + 1,
						pckgs[i].getDefaultPallet(),
						warehouse.getPallets().length);
				c.addPackage(pckgs[i]);
				carriers.add(c);
			}
		}
		computeTargetFunctionValue();
	}

	public void printResults() {
		System.out.println("1");
		System.out.println(totalArea + "\t" + minPalletVolume);
		System.out.println(carriers.size());
		int pckgs = 0;
		for (Carrier c : carriers) {
			System.out.println("n" + c.getId() + "\t"
					+ c.getPalletUsed().getName() + "\t"
					+ c.getExtensionsUsed());
			pckgs += c.getPackagesAssigned().size();
		}
		System.out.println(pckgs);
		for (Carrier c : carriers) {
			for (Package p : c.getPackagesAssigned()) {
				System.out.println(p.getId() + "\t" + "n" + c.getId());
			}
		}

	}
}
