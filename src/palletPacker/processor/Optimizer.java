package palletPacker.processor;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;

import palletPacker.model.Carrier;
import palletPacker.model.Pallet;
import palletPacker.model.Warehouse;
import palletPacker.model.Package;

public class Optimizer {

	Warehouse warehouse;
	ArrayList<Carrier> carriers;
	int totalArea = 0;
	float minPalletVolume;

	public Optimizer() {
		super();

		InitializeOptimizer();
	}

	private ArrayList<Carrier> GetSpecificCarriers(String palleteName) {
		ArrayList<Carrier> specificCarriers = new ArrayList<Carrier>();
		for (Carrier carrier : carriers) {
			if (carrier.getPalletUsed().getId().equals(palleteName)) {
				specificCarriers.add(carrier);
			}
		}
		return specificCarriers;
	}

	private Boolean TryArrangeOnExistingCarrier(Package givenPackage) {
		ArrayList<Carrier> availableCarriers = GetSpecificCarriers(givenPackage
				.getDefaultPallet().getId());
		for (Carrier carrier : availableCarriers) {
			if (carrier.getVolumeLeft() >= givenPackage.getVolume()) {
				carrier.addPackage(givenPackage);
				return true;
			}
		}

		if(givenPackage.getCompatiblePallets()!=null)
		for (Pallet compatiblePallet : givenPackage.getCompatiblePallets()) {
			availableCarriers = GetSpecificCarriers(compatiblePallet.getId());
			for (Carrier carrier : availableCarriers) {
				if (carrier.getVolumeLeft() >= givenPackage.getVolume()) {
					carrier.addPackage(givenPackage);
					return true;
				}
			}
		}

		return false;
	}

	private void InitializeOptimizer() {
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

	private void ComputeTargetFunctionValue() {
		float minPalletVolume = Float.MAX_VALUE;
		for (Carrier carrier : carriers) {
			totalArea += carrier.getPalletUsed().getArea();
			float usedSpace = carrier.getPalletUsed().getMaxVolume()
					- carrier.getVolumeLeft();
			if (usedSpace < minPalletVolume) {
				minPalletVolume = usedSpace;
			}
		}
	}

	public void RunNaivePacker() {
		carriers = new ArrayList<Carrier>();
		Package[] pckgs = warehouse.getPackages();

		for (int i = 0; i < pckgs.length; i++) {
			if (!TryArrangeOnExistingCarrier(pckgs[i])) {
				Carrier c = new Carrier(carriers.size()+1,
						pckgs[i].getDefaultPallet());
				c.addPackage(pckgs[i]);
				carriers.add(c);
			}
		}
		ComputeTargetFunctionValue();
	}

	public void PrintResults() {
		System.out.println("1");
		System.out.println(totalArea + "\t" + minPalletVolume);
		System.out.println(carriers.size());
		for (Carrier c : carriers) {
			System.out.println("n" + c.getId() + "\t"
					+ c.getPalletUsed().getId() + "\t" + c.getExtensionsUsed());
		}
		System.out.println("miljart");
		for (Carrier c : carriers) {
			for (Package p : c.getPackagesAssigned()) {
				System.out.println(p.getId() + "\t" + "n"+ c.getId());
			}
		}

	}
}
