package put.two.to.contest.model;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CarriersCollection {
	Random r = new Random();
	Pallet[] pallets;
	Package[] packages;
	ArrayList<Carrier> carriers = new ArrayList<Carrier>();

	public CarriersCollection(Pallet[] pallets, Package[] packages) {
		this.pallets = pallets;
		this.packages = packages;
	}

	public void setOrder(List<Package> list) {
		carriers.clear();

		for (Package p : list) {
			add(p);
		}
	}

	public List<Package> getOrder() {
		List<Package> result = new ArrayList<>();

		for (Carrier c : carriers) {
			result.addAll(c.getPackagesAssigned());
		}

		return result;
	}

	public void add(Package pkg) {
		for (Carrier c : carriers) {
			int palletId = c.canAddPackage(pkg);
			if (palletId >= 0) {
				c.addPackage(pkg, palletId);
				return;
			}
		}

		Carrier carrier = new Carrier(pallets);
		carrier.addPackage(pkg, pkg.getDefaultPallet().getId());
		carriers.add(carrier);
	}

	private int getTotalArea() {
		int result = 0;
		for (Carrier c : carriers) {
			result += c.getArea();
		}

		return result;
	}

	private float getMinPalletVolume() {
		float min = Integer.MAX_VALUE;
		for (Carrier c : carriers) {
			float volume = c.getVolume();
			if (volume <= 0) {
				if (volume < 0) {
					System.out.println("Error: Volume < 0");
				}
				continue;
			}
			if (min > volume) {
				min = volume;
			}
		}

		return min;
	}

	public ArrayList<Package> random(List<Package> baseOrder, float temp) {
		ArrayList<Package> result = new ArrayList<>();
		result.add(baseOrder.get(0));
		for (int i = 1; i < baseOrder.size(); i++) {
			int j;
			if (r.nextFloat() <= temp)
				j = r.nextInt(i + 1);
			else
				j = i;
			if (i != j) {
				result.add(result.get(j));
				result.set(j, baseOrder.get(i));
			} else {
				result.add(baseOrder.get(i));
			}
		}

		return result;
	}

	public Result process(List<Package> packages, float initTemp,
			float tempMul, int time) {
		Result localBest = getResult(packages);
		packages = new ArrayList<>(packages);

		int count = 1;

		long end = System.currentTimeMillis() + time;

		while (end > System.currentTimeMillis()) {
			count++;

			ArrayList<Package> tmpPackages = random(packages, initTemp);

			Result result = getResult(tmpPackages);

			if (result.compareTo(localBest) > 0) {
				// znaleziono najlepszy wynik
				localBest = result;
				packages = new ArrayList<>(localBest.getPackages());
			} else if (result.compareTo(localBest, initTemp * tempMul) > 0) {
				// znaleziono calkiem niezly wynik
				packages = tmpPackages;
			}
		}

		localBest.count = count;
		return localBest;
	}

	public Result getResult(List<Package> packages) {
		setOrder(packages);
		return new Result(getTotalArea(), getMinPalletVolume(), packages, 1);
	}

	public void save(OutputStream output) {
		printResults(new PrintStream(output));
	}

	public void printResults(PrintStream printStream) {
		printStream.println("1");
		printStream.println(getTotalArea() + "\t" + getMinPalletVolume());

		printStream.println(carriers.size());
		int pckgs = 0;
		for (int i = 0; i < carriers.size(); i++) {
			Carrier c = carriers.get(i);
			printStream.println("n" + (i + 1) + "\t"
					+ c.getPalletUsed().getName() + "\t"
					+ c.getExtensionsUsed());
			pckgs += c.getPackagesAssigned().size();
		}

		printStream.println(pckgs);
		for (int i = 0; i < carriers.size(); i++) {
			Carrier c = carriers.get(i);
			for (final Package p : c.getPackagesAssigned()) {
				printStream.println(p.getId() + "\t" + "n" + (i + 1));
			}
		}
	}
}
