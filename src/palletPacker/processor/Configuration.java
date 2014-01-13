package palletPacker.processor;

import java.util.ArrayList;
import java.util.Random;

import palletPacker.model.Carrier;
import palletPacker.model.Package;

public class Configuration {
	private final Package[] packages;
	private Result result;
	private ArrayList<Carrier> carriers;
	private final Random random;

	public Configuration(Package[] packages) {
		this.packages = packages;
		this.carriers = new ArrayList<>();
		this.random = new Random();
	}

	public void change(float temperature) {
		final int changes = (int) Math.max(packages.length * temperature, 1);
		for (int i = 0; i < changes; i++) {
			final int index = random.nextInt(packages.length);
			int move = (int) (changes * (random.nextDouble() - 0.5f));
			move = Math
					.max(Math.min(move, -index), packages.length - index - 1);
			if (move == 0) {
				continue;
			}
			swap(index, index + move);
		}
	}

	public Configuration copy() {
		final Package[] packages = new Package[this.packages.length];
		System.arraycopy(this.packages, 0, packages, 0, packages.length);

		return new Configuration(packages);
	}

	public ArrayList<Carrier> getCarriers() {
		return carriers;
	}

	public Package[] getPackages() {
		return packages;
	}

	public Result getResult() {
		return result;
	}

	public void setCarriers(ArrayList<Carrier> carriers) {
		this.carriers = carriers;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	private void swap(int index1, int index2) {
		final Package tmpPackage = packages[index1];
		packages[index1] = packages[index2];
		packages[index2] = tmpPackage;
	}
}
