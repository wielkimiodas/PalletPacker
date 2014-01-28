package put.two.to.contest.client;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Semaphore;

import put.two.to.contest.SolutionAcceptor;
import put.two.to.contest.model.CarriersCollection;
import put.two.to.contest.model.Package;
import put.two.to.contest.model.Result;
import put.two.to.contest.model.Warehouse;

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
		int count = bestResult.count + result.count;
		if (result.compareTo(bestResult) > 0) {
			bestResult = result;
		}
		bestResult.count = count;
		bestResultSem.release();
	}

	public void waitThread() {
		threadsReady.release();
		try {
			processPermit.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void sync(SolutionAcceptor solutionAcceptor, float temp) {
		for (int i = 0; i < N_THREADS; i++) {
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
		currentOrder = new ArrayList<>();
		currentOrder.addAll(bestResult.getPackages());

		for (int i = 0; i < N_THREADS; i++) {
			processPermit.release();
		}
		
		OutputStream outputStream = solutionAcceptor.newSolutionOutputStream();
		
		if (outputStream != null) {
			CarriersCollection collection = new CarriersCollection(
					warehouse.getPallets(), warehouse.getPackages());
			collection.setOrder(bestResult.getPackages());
			collection.save(outputStream);
		}
		
		bestResultSem.release();
	}
}