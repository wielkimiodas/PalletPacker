package palletPacker.processor;

import palletPacker.model.Warehouse;

public class Optimizer {

	Warehouse warehouse;
	
	public Optimizer() {
		super();

		InitializeOptimizer();
	}
	
	private void InitializeOptimizer()
	{
		warehouse = new Warehouse("data/example1.txt");		
	}
	
	

}
