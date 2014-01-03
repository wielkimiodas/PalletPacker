package palletPacker.client;

import palletPacker.processor.Optimizer;

public class ConsoleClient {

	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		Optimizer opt = new Optimizer();
		opt.runNaivePacker();
		opt.printResults();
		long end = System.currentTimeMillis();
		long duration = end-start;
		
		System.out.println("Took "+ duration + " ms.");
	}
}
