package palletPacker.client;

import palletPacker.model.CarriersCollection;
import palletPacker.model.Result;
import palletPacker.model.Warehouse;

public class ConsoleClient {
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
			processing.sync(output, 1 - (float)i / N_ITER);
		}

		long end = System.currentTimeMillis();
		
		System.out.print(processing.bestResult.getArea() + "\t" + processing.bestResult.getVolume() + "\t");
		System.out.println("Took " + (end - start) + " ms.");

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

			process(input, output);
		}
	}
}
