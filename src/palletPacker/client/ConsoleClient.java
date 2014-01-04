package palletPacker.client;

import palletPacker.model.Warehouse;
import palletPacker.processor.AnnealingOptimizer;
import palletPacker.processor.Optimizer;
import palletPacker.processor.Solver;

public class ConsoleClient {

	public static void main(String[] args) {

		long start = System.currentTimeMillis();
		
		if (true){
			Warehouse warehouse = new Warehouse();
	
			try {
				warehouse.ReadData("data/bigTest.txt");
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			AnnealingOptimizer optimizer = new AnnealingOptimizer(warehouse);
			optimizer.optimize();
			optimizer.printResults();
		}else {
			Optimizer opt = new Optimizer();
			opt.runNaivePacker();
			opt.printResults();
		}
		long end = System.currentTimeMillis();
		long duration = end - start;

		System.out.println("Took " + duration + " ms.");
	}
}
