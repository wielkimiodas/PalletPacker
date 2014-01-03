package palletPacker.processor;

import java.util.ArrayList;
import java.util.Random;

import palletPacker.model.Carrier;
import palletPacker.model.Package;

public class Configuration {
	private Package[] packages;
	private boolean[] newCarriers;
	private Result result;
	private ArrayList<Carrier> carriers;
	private Random random;
	
	public Package[] getPackages() {
		return packages;
	}

	public boolean[] getNewCarriers() {
		return newCarriers;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public ArrayList<Carrier> getCarriers() {
		return carriers;
	}

	public void setCarriers(ArrayList<Carrier> carriers) {
		this.carriers = carriers;
	}

	public Configuration(Package[] packages, boolean[] newCarriers){
		this.packages = packages;
		this.newCarriers = newCarriers;
		this.carriers = new ArrayList<>();
		this.random = new Random();
	}
	
	public Configuration copy() {
		Package[] packages = new Package[this.packages.length];
		System.arraycopy(this.packages, 0, packages, 0, packages.length);
		
		boolean[] newCarriers = new boolean[this.newCarriers.length];
		System.arraycopy(this.newCarriers, 0, newCarriers, 0, newCarriers.length);
		
		return new Configuration(packages, newCarriers);
	}
	
	public void change(float temperature) {
		int changes = (int)Math.max(packages.length * temperature, 1);
		for(int i = 0; i < changes; i++){
			int index = random.nextInt(packages.length);
			int move = (int)(changes * (random.nextDouble() - 0.5f));
			move = Math.max(Math.min(move, -index), packages.length - index - 1);
			if (move == 0){
				continue;
			}
			swap(index, index + move);
		}
		
		for(int i = 0; i < changes / 2; i++){
			int index = random.nextInt(packages.length);
			newCarriers[index] = !newCarriers[index];
		}
	}
	
	private void swap(int index1, int index2){
		Package tmpPackage = packages[index1];
		packages[index1] = packages[index2];
		packages[index2] = tmpPackage;
		boolean tmpBool = newCarriers[index1];
		newCarriers[index1] = newCarriers[index2];
		newCarriers[index2] = tmpBool;
	}
}
