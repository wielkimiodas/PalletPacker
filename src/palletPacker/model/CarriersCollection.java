package palletPacker.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class CarriersCollection {
	public class Tuple<X, Y> {
		public final X x;
		public final Y y;

		public Tuple(X x, Y y) {
			this.x = x;
			this.y = y;
		}
	}
	
	Pallet[] pallets;
	Package[] packages;
	ArrayList<Carrier> carriers = new ArrayList<Carrier>();
	
	public CarriersCollection(Pallet[] pallets, Package[] packages){
		this.pallets = pallets;
		this.packages = packages;
	}
	
	public void add(Package pkg) throws Exception{
		for(Carrier c : carriers){
			if (c.canHandlePackage(pkg)){
				c.addPackage(pkg);
				return;
			}
		}
		
		Carrier carrier = new Carrier(pallets);
		carrier.addPackage(pkg);
		carriers.add(carrier);
	}
	
	public void show(){
		System.out.println("Show");
		for(Carrier c : carriers){
			c.show();
		}
		System.out.println("End show");
	}
	
	private boolean remove(Package pkg){
		for(Carrier c : carriers){
			if (!c.contains(pkg)){
				continue;
			}
			return c.removePackage(pkg);
		}
		
		System.out.println("Not contatins");
		return false;
	}
	
	private int getTotalArea(){
		int result = 0;
		for(Carrier c : carriers){
			result += c.getPalletUsed().getArea();
		}
		
		return result;
	}
	
	private float getMinPalletVolume(){
		float min = Integer.MAX_VALUE;
		for(Carrier c : carriers) {
			float volume = c.getExtensionsUsed() * c.getPalletUsed().getExtensionHeight() * c.getPalletUsed().getArea();
			if (min > volume){
				min = volume;
			}
		}
		
		return min;
	}
	
	public Tuple<Float, Float> start(){
		float best = Float.MAX_VALUE;
		float best2 = 0;
		float swaps = 0;
		
		Random r = new Random();
		HashSet<Integer> set = new HashSet<>();
		for(int it = -1; it < 9; it++){
			//System.out.println("Begin iteration: " + it);
			long end = System.currentTimeMillis() + 10;
			while (end > System.currentTimeMillis()){
				swaps++;
				set.clear();
				
				int a = 0;
				
				for(int i = 0; i < 12 - it; i++){
					if (it == -1){
						break;
					}
					if (++a > 1000){
						return new Tuple<>(best, best2);
					}
					
					int index = r.nextInt(packages.length);
					if (set.contains(index)){
						i--;
						continue;
					}
					
					if (!remove(packages[index])){
						i--;
						continue;
					}
					
					set.add(index);
				}
				
				for(int index : set){
					try {
						add(packages[index]);
					} catch (Exception e) {
						e.printStackTrace();
						return new Tuple<>(best, best2);
					}
				}
			}
			
			float current = getTotalArea();
			if (best > current){
				best = current;
				best2 = getMinPalletVolume();
				
				if (best2 <= 0){
					printResults();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		//System.out.println(best);
		
		return new Tuple<>(best, best2);
	}
	
	public void printResults() {
		//System.out.println("1");
		System.out.println(getTotalArea() + "\t" + getMinPalletVolume());
		
		System.out.println(carriers.size());
		int pckgs = 0;
		for (int i = 0; i < carriers.size(); i++) {
			Carrier c = carriers.get(i);
			System.out.println("n" + (i+1) + "\t"
					+ c.getPalletUsed().getName() + "\t"
					+ c.getExtensionsUsed());
			pckgs += c.getPackagesAssigned().size();
		}
		
		System.out.println(pckgs);
		for (int i = 0; i < carriers.size(); i++) {
			Carrier c = carriers.get(i);
			for (final Package p : c.getPackagesAssigned()) {
				System.out.println(p.getId() + "\t" + "n" + (i+1));
			}
		}
	}
}
