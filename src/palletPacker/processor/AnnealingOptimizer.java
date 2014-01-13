package palletPacker.processor;

import palletPacker.model.Carrier;
import palletPacker.model.Package;
import palletPacker.model.Warehouse;

public class AnnealingOptimizer {
	private final Warehouse warehouse;
	Configuration bestConfiguration;

	public AnnealingOptimizer(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Configuration getBestConfiguration() {
		return bestConfiguration;
	}

	public void optimize() {
		Configuration conf = warehouse.getDefaultConfiguration();
		final Solver solver = new Solver(warehouse.getPallets());

		final int iterations = 500;
		for (int i = 0; i < iterations; i++) {
			solver.run(conf);
			if (bestConfiguration == null
					|| conf.getResult()
							.compareTo(bestConfiguration.getResult()) > 0) {
				bestConfiguration = conf;
				conf = conf.copy();
			}

			if (i % 100 == 0) {
				conf = bestConfiguration.copy();
			}

			conf.change((iterations - i) / (float) iterations);
			solver.clear();
		}
	}

	public void printResults() {
		System.out.println("1");
		System.out.println(bestConfiguration.getResult().getTotalArea() + "\t"
				+ bestConfiguration.getResult().getMinPalletVolume());

		final Solver solver = new Solver(warehouse.getPallets());
		solver.run(bestConfiguration);

		System.out.println(solver.getCarriers().size());
		int pckgs = 0;
		for (final Carrier c : solver.getCarriers()) {
			System.out.println("n" + c.getId() + "\t"
					+ c.getPalletUsed().getName() + "\t"
					+ c.getExtensionsUsed());
			pckgs += c.getPackagesAssigned().size();
		}
		System.out.println(pckgs);
		for (final Carrier c : solver.getCarriers()) {
			for (final Package p : c.getPackagesAssigned()) {
				System.out.println(p.getId() + "\t" + "n" + c.getId());
			}
		}
	}
}
