package palletPacker.client;

import palletPacker.processor.Optimizer;

public class ConsoleClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Optimizer opt = new Optimizer();
		opt.RunNaivePacker();
		opt.PrintResults();
	}

}
