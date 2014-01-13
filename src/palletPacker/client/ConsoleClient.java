package palletPacker.client;

import palletPacker.model.Warehouse;
import palletPacker.processor.AnnealingOptimizer;

public class ConsoleClient {

	public static void main(String[] args) {

		long start, end;
		// start = System.currentTimeMillis();

		final Warehouse warehouse = new Warehouse();

		try {
			warehouse.ReadData("data/instances-pp1/pp101.in");
		} catch (final Exception e) {
			e.printStackTrace();
			return;
		}

		final AnnealingOptimizer optimizer = new AnnealingOptimizer(warehouse);
		start = System.currentTimeMillis();
		optimizer.optimize();
		end = System.currentTimeMillis();
		optimizer.printResults();

		// final Optimizer opt = new Optimizer();
		// opt.runNaivePacker();
		// opt.printResults();
		//
		// end = System.currentTimeMillis();

		final long duration = end - start;

		System.out.println("Took " + duration + " ms.");
	}
}
