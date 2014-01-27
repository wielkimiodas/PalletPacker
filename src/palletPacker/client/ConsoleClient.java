package palletPacker.client;

import java.util.ArrayList;
import java.util.Random;

import palletPacker.model.CarriersCollection;
import palletPacker.model.Package;
import palletPacker.model.Result;
import palletPacker.model.Warehouse;

public class ConsoleClient {
	static void exchange(ArrayList<Package> list, int index1, int index2){
		Package p = list.get(index1);
		list.set(index1, list.get(index2));
		list.set(index2, p);
	}
	
	public static boolean process2(String input, String output){
		long start = System.currentTimeMillis();

		final Warehouse warehouse = new Warehouse();
		try {
			warehouse.readData(input);
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}

		final Processing processing = new Processing(warehouse);

		for (int th = 0; th < processing.N_THREADS; th++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					CarriersCollection collection = new CarriersCollection(
							warehouse.getPallets(), warehouse.getPackages());

					Random r = new Random();
					
					do {
						long end = System.currentTimeMillis() + 52;
						
						ArrayList<Package> packages = new ArrayList<>(processing.currentOrder);
						Result localBest = collection.getResult(packages);
						packages = new ArrayList<>(packages);
						
						int count = 0;
						
						while (end > System.currentTimeMillis()) {
							count++;
							
							// losowanie nowej kolejnoœci
							ArrayList<Package> tmpPackages = new ArrayList<>();
							tmpPackages.add(packages.get(0));
							for(int i = 1; i < packages.size(); i++){
								int j = i;
								if (r.nextFloat() <= processing.currentTemp)
									j = r.nextInt(i + 1);
								if (i != j){
									tmpPackages.add(tmpPackages.get(j));
									tmpPackages.set(j, packages.get(i));
								} else {
									tmpPackages.add(packages.get(j));
								}
							}
							
							Result result = collection
									.getResult(tmpPackages);
							
							if (result.compareTo(localBest) > 0){
								// znaleziono najlepszy wynik
								localBest = result;
								packages = new ArrayList<>(localBest.getPackages());
							} else if (result.compareTo(localBest, processing.currentTemp) > 0){
								// znaleziono calkiem niezly wynik
								packages = tmpPackages;
							}
						}
						
						localBest.count = count;
						processing.setResult(localBest);

						processing.waitThread();
					} while (!processing.end);
				}
			}).start();
		}
		
		float N_ITER = 19;
		for(int i = 1; i <= N_ITER; i++){
			processing.sync(output, 1 - (float)i / N_ITER, false);
		}

		long end = System.currentTimeMillis();
		
		System.out.print(processing.bestResult.getArea() + "\t" + processing.bestResult.getVolume() + "\t");
		System.out.println("Took " + (end - start) + " ms. " + processing.bestResult.count);

		return true;
	}
	
	public static boolean process(String input, String output) {
		long start = System.currentTimeMillis();

		final Warehouse warehouse = new Warehouse();
		try {
			warehouse.readData(input);
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}

		final Processing processing = new Processing(warehouse);

		for (int th = 0; th < processing.N_THREADS; th++) {
			final float tempMul = 1 - 0.5f * th / processing.N_THREADS;
			new Thread(new Runnable() {

				@Override
				public void run() {
					CarriersCollection collection = new CarriersCollection(
							warehouse.getPallets(), warehouse.getPackages());

					do {
						collection.setOrder(processing.currentOrder);

						Result result = collection.start(processing.currentTemp, tempMul);
						
						processing.setResult(result);
						
						processing.waitThread();
					} while (!processing.end);
				}
			}).start();
		}
		
		float N_ITER = 19;
		for(int i = 1; i <= N_ITER; i++){
			processing.sync(output, 1 - (float)i / N_ITER, ((i + 3) % 6) == 0);
		}

		long end = System.currentTimeMillis();
		
		System.out.print(processing.bestResult.getArea() + "\t" + processing.bestResult.getVolume() + "\t");
		System.out.println("Took " + (end - start) + " ms. " + processing.bestResult.count);

		return true;
	}

	public static void main(String[] args) {
		for (int i = 1; i <= 10; i++) {
			String input;
			String output;

			if (i < 10) {
				input = "data/instances-pp1/pp10" + i + ".in";
				output = "data/instances-pp1/output10" + i + ".txt";
			} else {
				input = "data/instances-pp1/pp1" + i + ".in";
				output = "data/instances-pp1/output1" + i + ".txt";
			}

			process2(input, output);
		}
	}
}
