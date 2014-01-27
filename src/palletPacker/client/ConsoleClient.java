package palletPacker.client;

import java.util.ArrayList;

import palletPacker.model.CarriersCollection;
import palletPacker.model.Package;
import palletPacker.model.Result;
import palletPacker.model.Warehouse;

public class ConsoleClient {
	public static boolean process(String input, String output, final int time, final int iterations, final boolean messing) {
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
			final float mul = 1 - (float) th / processing.N_THREADS;
			new Thread(new Runnable() {

				@Override
				public void run() {
					CarriersCollection collection = new CarriersCollection(
							warehouse.getPallets(), warehouse.getPackages());

					int iterationsCount = 0;
					do {
						ArrayList<Package> list = new ArrayList<>(processing.currentOrder);
						if (messing && ((iterationsCount + 3) % 6 == 0)){
							list = collection.random(list, 1);
						}
						Result result = collection.process(list,
								processing.currentTemp, mul, time);

						processing.setResult(result);

						processing.waitThread();
					} while (!processing.end);
				}
			}).start();
		}

		float N_ITER = iterations;
		for (int i = 1; i <= N_ITER; i++) {
			processing.sync(output, 1 - (float) i / N_ITER);
		}

		long end = System.currentTimeMillis();

		/*System.out.print(processing.bestResult.getArea() + "\t"
				+ processing.bestResult.getVolume() + "\t");
		System.out.println("Took " + (end - start) + " ms. " + processing.bestResult.count);*/

		//return true;
		return processing.bestResult.getVolume() == 1200;
	}

	public static void main(String[] args) {
		for (int m = 0; m < 2; m++) {
			for (int time = 10; time <= 50; time += 5) {
				for (int i = 9; i <= 9; i++) {
					String input;
					String output;

					if (i < 10) {
						input = "data/instances-pp1/pp10" + i + ".in";
						output = "data/instances-pp1/output10" + i + ".txt";
					} else {
						input = "data/instances-pp1/pp1" + i + ".in";
						output = "data/instances-pp1/output1" + i + ".txt";
					}

					final int N = 50;
					int count = 0;
					for (int it = 0; it < N; it++)
						if (process(input, output, time, 20, m == 1))
							count++;

					System.out.println(time + ":\t" + (100.0f * count / N));
				}
			}
		}
	}
}
