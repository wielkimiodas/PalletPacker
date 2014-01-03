package palletPacker.processor;

import palletPacker.model.Carrier;
import palletPacker.model.Package;
import palletPacker.model.Warehouse;

public class AnnealingOptimizer {
	private Warehouse warehouse;
	Configuration bestConfiguration;
	
	public Configuration getBestConfiguration() {
		return bestConfiguration;
	}

	public AnnealingOptimizer(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	
	public void optimize(){
		Configuration conf = warehouse.getDefaultConfiguration();
		Solver solver = new Solver(warehouse.getPallets());
		
		for(int i = 0; i < 100; i++){
			solver.run(conf);
			if (bestConfiguration == null || conf.getResult().compareTo(bestConfiguration.getResult()) > 0){
				bestConfiguration = conf;
				conf = conf.copy();
			}
			
			conf.change(1);
			solver.clear();
		}
	}
	
	public void printResults() {
		System.out.println("1");
		System.out.println(bestConfiguration.getResult().getTotalArea()+ "\t" + bestConfiguration.getResult().getMinPalletVolume());
		
		Solver solver = new Solver(warehouse.getPallets());
		solver.run(bestConfiguration);
		
		System.out.println(solver.getCarriers().size());
		int pckgs = 0;
		for (Carrier c : solver.getCarriers()) {
			System.out.println("n" + c.getId() + "\t"
					+ c.getPalletUsed().getName() + "\t"
					+ c.getExtensionsUsed());
			pckgs += c.getPackagesAssigned().size();
		}
		System.out.println(pckgs);
		for (Carrier c : solver.getCarriers()) {
			for (Package p : c.getPackagesAssigned()) {
				System.out.println(p.getId() + "\t" + "n" + c.getId());
			}
		}
	}
}
