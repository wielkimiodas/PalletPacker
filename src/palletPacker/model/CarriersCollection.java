package palletPacker.model;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class CarriersCollection {
	Random r = new Random();
	Pallet[] pallets;
	Package[] packages;
	ArrayList<Carrier> carriers = new ArrayList<Carrier>();
	HashMap<Package, Carrier> packageToCarrier = new HashMap<>();
	
	public CarriersCollection(Pallet[] pallets, Package[] packages){
		this.pallets = pallets;
		this.packages = packages;
	}
	
	public void setOrder(List<Package> list){
		carriers.clear();
		packageToCarrier.clear();
		
		for (Package p : list) {
			add(p);
		}
	}
	
	public List<Package> getOrder(){
		List<Package> result = new ArrayList<>();
		
		for (Carrier c : carriers) {
			result.addAll(c.getPackagesAssigned());
		}
		
		return result;
	}
	
	public List<Move> random(float temperature){
		int changes = (int)Math.max((packages.length * temperature), 3);
		
		List<Move> result = new ArrayList<>();
		
		HashSet<Integer> packagesUsed = new HashSet<>();
		
		for(int i = 0; i < changes; i++){
			int index = r.nextInt(packages.length);

			if (packagesUsed.contains(index)){
				i--;
				continue;
			}
			
			Tuple<Carrier, Integer> t = remove(packages[index]); 
			if (t == null){
				i--;
				continue;
			}
			
			result.add(new Move(packages[index], t));
			
			packagesUsed.add(index);
		}
		
		for(int i = result.size() - 1; i >= 0; i--){
			Move move = result.get(i);
			Tuple<Carrier, Integer> t = add(move.pkg);
			move.setTo(t);
		}
		
		return result;
	}
	
	public Tuple<Carrier, Integer> add(Package pkg) {
		for(Carrier c : carriers){
			int palletId = c.canAddPackage(pkg);
			if (palletId >= 0){
				int oldId = c.getCurrentPalletId();
				c.addPackage(pkg, palletId);
				packageToCarrier.put(pkg, c);
				return new Tuple<>(c, oldId);
			}
		}
		
		Carrier carrier = new Carrier(pallets);
		carrier.addPackage(pkg, pkg.getDefaultPallet().getId());
		carriers.add(carrier);
		
		packageToCarrier.put(pkg, carrier);
		
		return new Tuple<>(carrier, -1);
	}
	
	private Tuple<Carrier, Integer> remove(Package pkg){
		/*for(Carrier c : carriers){
			if (!c.contains(pkg)){
				continue;
			}
			int oldId = c.getCurrentPalletId();
			int palletId = c.canRemovePackage(pkg);
			if (palletId >= 0){
				c.removePackage(pkg, palletId);
				return new Tuple<>(c, oldId);
			} else {
				return null;
			}
		}
		
		System.out.println("Not contatins");
		return null;*/
		
		Carrier c = packageToCarrier.get(pkg);
		
		int oldId = c.getCurrentPalletId();
		int palletId = c.canRemovePackage(pkg);
		if (palletId >= 0){
			c.removePackage(pkg, palletId);
			return new Tuple<>(c, oldId);
		} else {
			return null;
		}
	}
	
	public void commit(){
		List<Carrier> toRemove = new ArrayList<>();
		for(Carrier c : carriers){
			if (c.isEmpty()){
				toRemove.add(c);
			}
		}
		
		carriers.removeAll(toRemove);
	}
	
	public void rollback(List<Move> moves){
		for(int i = 0; i < moves.size(); i++){
			Move move = moves.get(i);
			Tuple<Carrier, Integer> to = move.getTo(); 
			to.x.removePackage(move.pkg, to.y);
			
			if (to.y == -1){
				carriers.remove(to.x);
			}
		}
		
		for(int i = moves.size() - 1; i >= 0; i--){
			Move move = moves.get(i);
			Tuple<Carrier, Integer> from = move.getFrom();
			from.x.addPackage(move.pkg, from.y);
			packageToCarrier.put(move.pkg, from.x);
		}
	}
	
	private int getTotalArea(){
		int result = 0;
		for(Carrier c : carriers){
			result += c.getArea();
		}
		
		return result;
	}
	
	private float getMinPalletVolume(){
		float min = Integer.MAX_VALUE;
		for(Carrier c : carriers) {
			float volume = c.getVolume();
			if (volume <= 0){
				if (volume < 0){
					System.out.println("Error: Volume < 0");
				}
				continue;
			}
			if (min > volume){
				min = volume;
			}
		}
		
		return min;
	}
	
	public Result start(float initTemp, float tempMul){
		float bestArea = Float.MAX_VALUE;
		float bestVolume = 0;
		List<Package> bestOrder = null;
		int count = 0;
		
		float temp = initTemp;
		long end = System.currentTimeMillis() + 50;
		
		while (end > System.currentTimeMillis()){
			count++;
			List<Move> moves = random(temp);
			
			float current = getTotalArea();
			if (bestArea >= current){
				float current2 = getMinPalletVolume();
				if (bestVolume > current2 || bestArea > current) {
					bestArea = current;
					bestVolume = current2;
					bestOrder = getOrder();

					if (bestVolume <= 0) {
						System.out.println("Best2 <= 0");
					}
				}
			}
			
			if (current * (1 - temp * tempMul) <= bestArea){
				commit();
			} else {
				rollback(moves);
			}
		}
		
		return new Result(bestArea, bestVolume, bestOrder);
	}
	
	public void save(String output){
		try {
			printResults(new PrintStream(output));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void printResults(PrintStream printStream){
		printStream.println("1");
		printStream.println(getTotalArea() + "\t" + getMinPalletVolume());
		
		printStream.println(carriers.size());
		int pckgs = 0;
		for (int i = 0; i < carriers.size(); i++) {
			Carrier c = carriers.get(i);
			printStream.println("n" + (i+1) + "\t"
					+ c.getPalletUsed().getName() + "\t"
					+ c.getExtensionsUsed());
			pckgs += c.getPackagesAssigned().size();
		}
		
		printStream.println(pckgs);
		for (int i = 0; i < carriers.size(); i++) {
			Carrier c = carriers.get(i);
			for (final Package p : c.getPackagesAssigned()) {
				printStream.println(p.getId() + "\t" + "n" + (i+1));
			}
		}
	}
}
