package palletPacker.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import palletPacker.model.CarriersCollection;
import palletPacker.model.CarriersCollection.Tuple;
import palletPacker.model.Package;
import palletPacker.model.Warehouse;

public class ConsoleClient {

	public static void main(String[] args) {

		for (int i = 1; i <= 10; i++) {
			long start, end;
			// start = System.currentTimeMillis();

			final Warehouse warehouse = new Warehouse();

			try {
				if (i < 10) {
					warehouse.readData("data/instances-pp1/pp10" + i + ".in");
				} else {
					warehouse.readData("data/instances-pp1/pp1" + i + ".in");
				}
			} catch (final Exception e) {
				e.printStackTrace();
				return;
			}

			for (int t = 0; t < 11; t++) {

				// final AnnealingOptimizer optimizer = new
				// AnnealingOptimizer(warehouse);
				start = System.currentTimeMillis();
				CarriersCollection collection = new CarriersCollection(
						warehouse.getPallets(), warehouse.getPackages());

				ArrayList<palletPacker.model.Package> packages = new ArrayList<>();
				for (palletPacker.model.Package p : warehouse.getPackages()) {
					packages.add(p);
				}

				Collections.sort(packages,
						new Comparator<palletPacker.model.Package>() {

							@Override
							public int compare(Package p1, Package p2) {
								return (int) (p2.getVolume() - p1.getVolume());
							}

						});

				for (int a = 0; a < warehouse.getPackages().length; a++) {
					palletPacker.model.Package pkg = packages.get(a);
					try {
						collection.add(pkg);
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}

				Tuple<Float, Float> tuple = collection.start();
				System.out.println(tuple.x + "\t" + tuple.y);
				end = System.currentTimeMillis();

				// collection.printResults();

				// final Optimizer opt = new Optimizer();
				// opt.runNaivePacker();
				// opt.printResults();
				//
				// end = System.currentTimeMillis();

				final long duration = end - start;
			}
			System.out.println();

			// System.out.println("Took " + duration + " ms.");
		}
	}
}
