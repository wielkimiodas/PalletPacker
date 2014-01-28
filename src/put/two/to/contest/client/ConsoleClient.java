package put.two.to.contest.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import put.two.to.contest.SolutionAcceptor;
import put.two.to.contest.model.CarriersCollection;
import put.two.to.contest.model.Package;
import put.two.to.contest.model.Result;
import put.two.to.contest.model.Warehouse;

public class ConsoleClient {
	static class SolutionAcceptorImplementation implements SolutionAcceptor {
		String path;
		int callsCount = 0;

		public SolutionAcceptorImplementation(String path) {
			this.path = path;
		}

		@Override
		public OutputStream newSolutionOutputStream() {
			if (++callsCount > 20) {
				return null;
			}

			try {
				return new FileOutputStream(path);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}

	}

	public static Result process(InputStream input,
			SolutionAcceptor solutionAcceptor, final int time,
			final int iterations, final boolean messing) {
		final Warehouse warehouse = new Warehouse();
		try {
			warehouse.readData(input);
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}

		final Processing processing = new Processing(warehouse);

		for (int th = 0; th < processing.N_THREADS; th++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					CarriersCollection collection = new CarriersCollection(
							warehouse.getPallets(), warehouse.getPackages());

					int iterationsCount = 0;
					do {
						ArrayList<Package> list = new ArrayList<>(
								processing.currentOrder);
						if (messing && ((iterationsCount + 3) % 6 == 0)) {
							collection.random(list, 1);
						}
						Result result = collection.process(list,
								processing.currentTemp, time);

						processing.setResult(result);

						processing.waitThread();
					} while (!processing.end);
				}
			}).start();
		}

		float N_ITER = iterations;
		float temp = 1;
		for (int i = 1; i <= N_ITER; i++) {
			processing.sync(solutionAcceptor, temp, i == N_ITER);
			temp *= 0.8f;
		}

		return processing.bestResult;
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

			try {
				process(new FileInputStream(input),
						new SolutionAcceptorImplementation(output), 120, 8,
						true);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
