package palletPacker.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.Semaphore;

import palletPacker.model.CarriersCollection;
import palletPacker.model.Package;
import palletPacker.model.Result;
import palletPacker.model.Warehouse;

class Processing {
	public final int N_THREADS = 4;
	
	Warehouse warehouse;

	Semaphore threadsReady = new Semaphore(0);
	Semaphore processPermit = new Semaphore(0);
	Semaphore bestResultSem = new Semaphore(1);

	public ArrayList<Package> currentOrder = new ArrayList<>();
	Result bestResult = Result.getWorst();

	public boolean end = false;
	public float currentTemp = 1.0f;

	public Processing(Warehouse warehouse) {
		this.warehouse = warehouse;
		for (Package p : warehouse.getPackages()) {
			currentOrder.add(p);
		}

		Collections.sort(currentOrder, new Comparator<Package>() {
			@Override
			public int compare(Package p1, Package p2) {
				return (int) (p2.getVolume() - p1.getVolume());
			}
		});
	}

	public void setResult(Result result) {
		try {
			bestResultSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (result.compareTo(bestResult) > 0) {
			bestResult = result;
		}
		bestResultSem.release();
	}
	
	public void waitThread(){
		threadsReady.release();
		try {
			processPermit.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void sync(String output, float temp, boolean mess){
		for(int i = 0; i <N_THREADS; i++){
			try {
				threadsReady.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		try {
			bestResultSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		end = temp == 0.0f;
		if (mess) {
			Random r = new Random();
			for(int i = 0; i < 2 * currentOrder.size(); i++){
				int index1 = r.nextInt(currentOrder.size());
				int index2 = r.nextInt(currentOrder.size());
				Package p = currentOrder.get(index1);
				currentOrder.set(index1, currentOrder.get(index2));
				currentOrder.set(index2, p);
			}
		} else {
			currentOrder.clear();
			currentOrder.addAll(bestResult.getPackages());
		}
		
		for(int i = 0; i < N_THREADS; i++){
			processPermit.release();
		}
		
		CarriersCollection collection = new CarriersCollection(warehouse.getPallets(), warehouse.getPackages());
		collection.setOrder(bestResult.getPackages());
		collection.save(output);
		bestResultSem.release();
	}
}