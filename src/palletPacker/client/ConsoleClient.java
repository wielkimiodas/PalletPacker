package palletPacker.client;

import palletPacker.processor.Optimizer;

public class ConsoleClient {

	public static void main(String[] args) {

		Optimizer opt = new Optimizer();
		opt.runNaivePacker();
		opt.printResults();
	}
}
