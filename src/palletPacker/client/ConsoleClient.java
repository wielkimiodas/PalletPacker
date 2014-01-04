package palletPacker.client;

import palletPacker.model.Warehouse;
import palletPacker.processor.AnnealingOptimizer;
import palletPacker.processor.Optimizer;
import palletPacker.processor.Solver;

public class ConsoleClient {

	public static void main(String[] args) {

		long start, end;
		//start = System.currentTimeMillis();
		if (true){
			Warehouse warehouse = new Warehouse();
	
			try {
				warehouse.ReadData("data/bigTest2.txt");
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			AnnealingOptimizer optimizer = new AnnealingOptimizer(warehouse);
			start = System.currentTimeMillis();
			optimizer.optimize();
			end = System.currentTimeMillis();
			optimizer.printResults();
		}else {
			Optimizer opt = new Optimizer();
			opt.runNaivePacker();
			opt.printResults();
		}
		//end = System.currentTimeMillis();
		long duration = end - start;

		System.out.println("Took " + duration + " ms.");
	}
}
