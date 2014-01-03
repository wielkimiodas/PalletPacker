package palletPacker.processor;

import java.io.IOException;

import palletPacker.model.Warehouse;

public class Optimizer {

	Warehouse warehouse;
	
	public Optimizer() {
		super();

		InitializeOptimizer();
	}
	
	private void InitializeOptimizer()
	{
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
	
	private void NaivePacker()
	{
		
	}
	
	

}
